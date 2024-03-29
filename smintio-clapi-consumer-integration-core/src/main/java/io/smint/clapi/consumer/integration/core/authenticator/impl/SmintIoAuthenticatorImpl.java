// MIT License
//
// Copyright (c) 2019 Smint.io GmbH
//
// Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
// documentation files (the "Software"), to deal in the Software without restriction, including without limitation the
// rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
// permit persons to whom the Software is furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice (including the next paragraph) shall be included in all
// copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
// WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
// COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
// OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
//
// SPDX-License-Identifier: MIT

package io.smint.clapi.consumer.integration.core.authenticator.impl;

import java.io.IOException;
import java.text.MessageFormat;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.inject.Provider;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import org.json.JSONException;
import org.json.JSONObject;

import io.smint.clapi.consumer.integration.core.authenticator.ISmintIoAuthenticator;
import io.smint.clapi.consumer.integration.core.configuration.IAuthTokenStorage;
import io.smint.clapi.consumer.integration.core.configuration.models.IAuthTokenModel;
import io.smint.clapi.consumer.integration.core.configuration.models.ISettingsModel;
import io.smint.clapi.consumer.integration.core.configuration.models.impl.AuthTokenImpl;
import io.smint.clapi.consumer.integration.core.exceptions.SmintIoAuthenticatorException;
import io.smint.clapi.consumer.integration.core.exceptions.SmintIoAuthenticatorException.AuthenticatorError;


/**
 * Authenticator to refresh access token (=authorization) with Smint.io API.
 *
 * <p>
 * General authenticator uses the refresh token to renew the validity of the access token. See article
 * <a href="https://www.oauth.com/oauth2-servers/making-authenticated-requests/refreshing-an-access-token/">Refreshing
 * an access token</a> for some explanation, how refreshing the access token is performed.
 * </p>
 */
public class SmintIoAuthenticatorImpl implements ISmintIoAuthenticator {

    /**
     * The URL to the Smint.ip API OAuth token end point that is used to refresh the OAuth access token.
     *
     * <p>
     * the placeholder {@code {0}} is replaced with the tenant ID, fetched from {@code ISettingsModel#getTenantId()}.
     * {@link MessageFormat#format(String, Object...)} is used to fill the placeholder.
     * </p>
     *
     * <pre>
     * {@code OAUTH_TOKEN_ENDPOINT} = {@value #OAUTH_TOKEN_ENDPOINT}
     * </pre>
     */
    public static final String OAUTH_TOKEN_ENDPOINT = "https://{0}.smint.io/connect/token";


    private static final Logger LOG = Logger.getLogger(SmintIoAuthenticatorImpl.class.getName());


    private final Provider<OkHttpClient> _httpClientProvider;

    @Inject
    public SmintIoAuthenticatorImpl(final Provider<OkHttpClient> httpClientProvider) {
        this._httpClientProvider = httpClientProvider;
    }

    @Override
    public SmintIoAuthenticatorImpl refreshSmintIoToken(
        final ISettingsModel settings, final IAuthTokenStorage authTokenStorage
    ) throws SmintIoAuthenticatorException {

        if (authTokenStorage == null) {
            LOG.warning("No access token can be renewed, as no token storage has been provided.");
            return this;
        }

        SmintIoAuthenticatorImpl.validateForTokenRefresh(authTokenStorage.getAuthData());

        if (settings == null) {
            LOG.warning("No access token can be renewed, as no settings storage has been provided.");
            return this;
        }

        this.validateForAuthenticator(settings);


        // CHECKSTYLE OFF: MultipleStringLiterals
        final JSONObject response = this.performSmintIoApiRequest(settings, authTokenStorage);
        if (response == null || response.has("error")) {

            if (response != null && "invalid_grant".equalsIgnoreCase(response.getString("error"))) {
                // invalidate authentication data in case of "invalid_grant" error
                authTokenStorage.storeAuthData(new AuthTokenImpl().setIsSuccess(false));

            } else {
                authTokenStorage.storeAuthData(new AuthTokenImpl(authTokenStorage.getAuthData()).setIsSuccess(false));
            }

        } else {
            authTokenStorage.storeAuthData(
                this.extractTokenFromJsonResponse(response, authTokenStorage.getAuthData())
            );
        }
        // CHECKSTYLE ON: MultipleStringLiterals

        // warning: performSmintIoApiRequest changes the authentication data, so fetch it again afterwards.
        if (!authTokenStorage.getAuthData().isSuccess()) {
            // CHECKSTYLE OFF: MultipleStringLiterals
            final String serverErrorMessage = response.has("error_msg") ? response.getString("error_msg") : null;
            // CHECKSTYLE ON: MultipleStringLiterals
            throw new SmintIoAuthenticatorException(
                AuthenticatorError.CannotRefreshSmintIoToken,
                serverErrorMessage != null && !serverErrorMessage.isEmpty() ? serverErrorMessage
                    : "Failed to renew access token."
            );
        }


        final String newAccessToken = authTokenStorage.getAuthData().getAccessToken();
        if (newAccessToken == null || newAccessToken.isEmpty()) {
            throw new SmintIoAuthenticatorException(
                AuthenticatorError.CannotRefreshSmintIoToken,
                "Smint.io OAuth server failed to create a new access token."
            );
        }


        LOG.info("Successfully refreshed token for Smint.io!");
        return this;
    }


    /**
     * Calls the Smint.io API and returns the parsed JSON response.
     *
     * @param settings         the settings to read the URL from.
     * @param authTokenStorage get the OAuth authorization data to refresh the token.
     * @return the response JSON.
     * @throws SmintIoAuthenticatorException in case HTTP request failed or an invalid response is received.
     */
    private JSONObject performSmintIoApiRequest(
        final ISettingsModel settings, final IAuthTokenStorage authTokenStorage
    ) throws SmintIoAuthenticatorException {

        LOG.entering(this.getClass().getName(), ":performSmintIoApiRequest");

        Objects.requireNonNull(settings, "No settings provided!");
        Objects.requireNonNull(authTokenStorage, "No auth token storage provided!");


        final IAuthTokenModel authData = authTokenStorage.getAuthData();
        final AuthTokenImpl newAuthData = new AuthTokenImpl(authData);
        newAuthData.setIsSuccess(false);


        final String tokenEndPoint = MessageFormat.format(OAUTH_TOKEN_ENDPOINT, settings.getTenantId());
        LOG.info(() -> "Trying to access Smint.io OAuth end point: " + tokenEndPoint);


        // CHECKSTYLE OFF: MultipleStringLiterals
        final RequestBody formBody = new FormBody.Builder()
            .add("grant_type", "refresh_token")
            .add("refresh_token", authData.getRefreshToken())
            .add("client_id", settings.getOAuthClientId())
            .add("client_secret", settings.getOAuthClientSecret())
            .build();
        // CHECKSTYLE ON: MultipleStringLiterals
        LOG.fine(() -> "Sending Smint.io OAuth data to end point: " + formBody.toString());

        final Request request = new Request.Builder()
            .url(tokenEndPoint)
            .post(formBody)
            .build();


        String responseText = null;
        try {

            final OkHttpClient client = this._httpClientProvider != null ? this._httpClientProvider.get()
                : new OkHttpClient();

            final Response httpResponse = client.newCall(request).execute();

            responseText = httpResponse.body().string();
            if (responseText == null || responseText.isEmpty()) {

                authTokenStorage.storeAuthData(newAuthData);
                throw new SmintIoAuthenticatorException(
                    AuthenticatorError.CannotRefreshSmintIoToken,
                    "Smint.io OAuth server returned an empty response. Token endpoint was: " + tokenEndPoint
                );
            }

            final String responseTextLog = responseText;
            LOG.fine(() -> "Received Smint.io OAuth data response: " + responseTextLog);
            return new JSONObject(responseText);

        } catch (final IOException excp) {

            authTokenStorage.storeAuthData(newAuthData);
            throw new SmintIoAuthenticatorException(
                AuthenticatorError.CannotRefreshSmintIoToken,
                "Failed to perform request to token endpoint " + tokenEndPoint,
                excp
            );

        } catch (final JSONException jsonExcp) {

            authTokenStorage.storeAuthData(newAuthData);
            throw new SmintIoAuthenticatorException(
                AuthenticatorError.CannotRefreshSmintIoToken,
                "Smint.io OAuth server returned an invalid response: " + responseText,
                jsonExcp
            );
        } finally {
            LOG.exiting(this.getClass().getName(), ":performSmintIoApiRequest");
        }
    }


    private IAuthTokenModel extractTokenFromJsonResponse(
        final JSONObject response, final IAuthTokenModel oldAuthData
    ) throws SmintIoAuthenticatorException {


        LOG.entering(this.getClass().getName(), ":extractTokenFromJsonResponse");
        Objects.requireNonNull(response, "Invalid response!");

        // CHECKSTYLE OFF: MultipleStringLiterals
        if (response.has("error")) {
            throw new SmintIoAuthenticatorException(
                AuthenticatorError.CannotRefreshSmintIoToken, response.getString("error")
            );
        }
        // CHECKSTYLE ON: MultipleStringLiterals


        final AuthTokenImpl newAuthData = new AuthTokenImpl(oldAuthData);
        newAuthData.setAccessToken(null);
        newAuthData.setIsSuccess(true);


        // CHECKSTYLE OFF: MultipleStringLiterals
        if (response.has("success")) {
            newAuthData.setIsSuccess(response.getBoolean("success"));
        }
        // CHECKSTYLE ON: MultipleStringLiterals


        this.setTokenValueFromResponse(response, "access_token", (token) -> newAuthData.setAccessToken(token));
        this.setTokenValueFromResponse(response, "refresh_token", (token) -> newAuthData.setRefreshToken(token));
        this.setTokenValueFromResponse(response, "identity_token", (token) -> newAuthData.setIdentityToken(token));


        // CHECKSTYLE OFF: MultipleStringLiterals
        final long newExpireDuration = response.has("expires_in") ? response.getLong("expires_in") : 0;
        if (newExpireDuration > 0) {
            newAuthData.setExpiration(
                OffsetDateTime.now(ZoneId.systemDefault()).plusSeconds(newExpireDuration)
            );

        } else {
            LOG.warning("Smint.io OAuth service did not return an expiration date.");
            newAuthData.setExpiration(null);
        }
        // CHECKSTYLE ON: MultipleStringLiterals


        LOG.exiting(this.getClass().getName(), ":extractTokenFromJsonResponse");
        return newAuthData;
    }


    /**
     * Provides the OkHttpClient provider, internally used.
     *
     * @return the provider or {@code null} if none has been set.
     */
    public Provider<OkHttpClient> getHttpClientProvider() {
        return this._httpClientProvider;
    }


    /**
     * Provides the OkHttpClient internally used, fetched from the provider {@link #getHttpClientProvider()}.
     *
     * @return the OkHttpClient or {@code null} if the provider has not been set or did not provide a value.
     */
    public OkHttpClient getHttpClient() {
        final Provider<OkHttpClient> provider = this.getHttpClientProvider();
        return provider != null ? provider.get() : null;
    }

    /**
     * Validates the available OAuth data for refreshing access token.
     *
     * <p>
     * Validates that a refresh token is available.
     * </p>
     *
     * @param authData the OAuth data to validate.
     * @throws SmintIoAuthenticatorException in case no refresh token is available.
     */
    public static void validateForTokenRefresh(final IAuthTokenModel authData) throws SmintIoAuthenticatorException {

        if (authData == null) {
            throw new SmintIoAuthenticatorException(
                AuthenticatorError.SmintIoIntegrationWrongState,
                "No authentication data is available."
            );
        }


        final String refreshToken = authData.getRefreshToken();
        if (!authData.isSuccess() || refreshToken == null || refreshToken.isEmpty()) {
            throw new SmintIoAuthenticatorException(
                AuthenticatorError.SmintIoIntegrationWrongState, "The refresh token is missing"
            );
        }
    }

    /**
     * Validates the available settings data for authenticator.
     *
     * <p>
     * Validates that the settings are valid.
     * </p>
     *
     * @param setttings the settingsa to validate.
     * @throws SmintIoAuthenticatorException in case the settings are invalid.
     */
    private void validateForAuthenticator(final ISettingsModel settings) throws SmintIoAuthenticatorException {

        if (settings == null) {
            throw new SmintIoAuthenticatorException(
                AuthenticatorError.SmintIoIntegrationWrongState,
                "No available available."
            );
        }


        final String tenantId = settings.getTenantId();
        if (tenantId == null || tenantId.isEmpty()) {
            throw new SmintIoAuthenticatorException(
                AuthenticatorError.SmintIoIntegrationWrongState, "The tenant ID is missing"
            );
        }

        final String clientId = settings.getOAuthClientId();
        if (clientId == null || clientId.isEmpty()) {
            throw new SmintIoAuthenticatorException(
                AuthenticatorError.SmintIoIntegrationWrongState, "The client ID is missing"
            );
        }

        final String clientSecret = settings.getOAuthClientSecret();
        if (clientSecret == null || clientSecret.isEmpty()) {
            throw new SmintIoAuthenticatorException(
                AuthenticatorError.SmintIoIntegrationWrongState, "The client secret is missing"
            );
        }
    }


    /**
     * Checks the value from the response and if there is a value, it is passed to the consumer.
     *
     * @param response     the JSON response to analyze.
     * @param responseName the name within the response the read its String value.
     * @param valueUser    the consumer of the value.
     */
    private void setTokenValueFromResponse(
        final JSONObject response, final String responseName, final Consumer<String> valueUser
    ) {

        final String newValue = response.has(responseName) ? response.getString(responseName) : null;
        if (newValue != null && !newValue.isEmpty()) {
            valueUser.accept(newValue);
        }
    }
}
