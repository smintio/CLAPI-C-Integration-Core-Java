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
import java.net.MalformedURLException;
import java.net.URL;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import javax.inject.Inject;
import javax.inject.Provider;

import com.github.scribejava.apis.openid.OpenIdOAuth2AccessToken;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.oauth.OAuth20Service;

import okhttp3.OkHttpClient;

import io.smint.clapi.consumer.integration.core.authenticator.ISmintIoOAuthAuthorizer;
import io.smint.clapi.consumer.integration.core.configuration.IAuthTokenStorage;
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
public class SmintIoOAuthAuthorizer extends SmintIoAuthenticatorImpl implements ISmintIoOAuthAuthorizer {


    private final Provider<ISettingsModel> _settingsProvider;
    private final IAuthTokenStorage _tokenStorage;
    private String _oAuthApiSecret;
    private OAuth20Service _service;

    /**
     * Initializes this instance with an internally used, default {@code OkHttpClient}.
     *
     * @param settings     the settings to use for the authorizer, where to read OAuth redirect target URL from.
     * @param tokenStorage the token storage to use for newly created access token data.
     */
    public SmintIoOAuthAuthorizer(final Provider<ISettingsModel> settings, final IAuthTokenStorage tokenStorage) {
        this(settings, tokenStorage, () -> new OkHttpClient());
    }


    /**
     * (preferred) Fully initializes this instance with all related instances needed.
     *
     * @param settings           the settings to use for the authorizer, where to read OAuth redirect target URL from.
     * @param tokenStorage       the token storage to use for newly created access token data.
     * @param httpClientProvider provides a valid OkHttp client to perform the requests. Its value is passed to the
     *                           super class constructor.
     */
    @Inject
    public SmintIoOAuthAuthorizer(
        final Provider<ISettingsModel> settings,
        final IAuthTokenStorage tokenStorage,
        final Provider<OkHttpClient> httpClientProvider
    ) {
        super(httpClientProvider);
        this._settingsProvider = settings;
        this._tokenStorage = tokenStorage;

        Objects.requireNonNull(settings, "No OAuth settings have been provided!");
        Objects.requireNonNull(tokenStorage, "invalid token storage - no token can be stored!");
    }


    @Override
    public URL createAuthorizationUrl() throws SmintIoAuthenticatorException {

        final ISettingsModel settings = this._settingsProvider.get();
        this.validateForOAuth(settings);

        // Scribe uses "singletons" but the singleton depends on settings.
        // hence a new one is created if needed.
        SmintIoApiForScribe.createSingleton(settings);

        this._oAuthApiSecret = "secret-" + new Random().nextInt(999_999);


        // see:
        // https://github.com/scribejava/scribejava/blob/master/scribejava-apis/src/test/java/com/github/scribejava/apis/examples/Google20Example.java
        this._service = new ServiceBuilder(settings.getOAuthClientId())
            .apiSecret(settings.getOAuthClientSecret())
            .defaultScope(SMINTIO_OAUTH_SCOPE)
            .callback(settings.getOAuthLocalUrlReceivingAccessData().toExternalForm())
            .build(SmintIoApiForScribe.instance());


        final String authorizationUrl = this._service.createAuthorizationUrlBuilder()
            .state(this._oAuthApiSecret)
            .build();

        try {
            return new URL(authorizationUrl);

        } catch (final MalformedURLException excp) {
            throw new SmintIoAuthenticatorException(
                AuthenticatorError.CannotAcquireSmintIoToken,
                "OAuth base URL is invalid",
                excp
            );
        }
    }


    @Override
    public ISmintIoOAuthAuthorizer analyzeReceivedAuthorizationData(
        final Map<String, String[]> urlParameters
    ) throws SmintIoAuthenticatorException {


        Objects.requireNonNull(urlParameters, "No url parameters to analyze have been passed!");

        if (this._service == null || this._oAuthApiSecret == null) {
            throw new SmintIoAuthenticatorException(
                AuthenticatorError.SmintIoIntegrationWrongState,
                "OAuth authorization proces has not been started yet!"
            );
        }

        if (urlParameters.isEmpty()) {
            throw new SmintIoAuthenticatorException(
                AuthenticatorError.SmintIoIntegrationWrongState,
                "No URL parameters have been passed to analyze!"
            );
        }


        if (urlParameters.get("code") == null || urlParameters.get("code").length != 1) {
            throw new SmintIoAuthenticatorException(
                AuthenticatorError.SmintIoIntegrationWrongState,
                "Invalid response from OAuth service, with wrong URL parameter 'code'"
            );
        }


        // check "state" parameter to match our stored value
        if (urlParameters.get("state") == null || urlParameters.get("state").length != 1) {
            throw new SmintIoAuthenticatorException(
                AuthenticatorError.SmintIoIntegrationWrongState,
                "Invalid response from OAuth service, with wrong URL parameter 'state'"
            );
        }

        if (!this._oAuthApiSecret.equals(urlParameters.get("state")[0])) {
            throw new SmintIoAuthenticatorException(
                AuthenticatorError.SmintIoIntegrationWrongState,
                "Wrong URL parameter 'state' in OAuth authorization - answer was not for me!"
            );
        }

        final ISettingsModel settings = this._settingsProvider.get();
        this.validateForOAuth(settings);
        SmintIoApiForScribe.createSingleton(settings);

        try {
            final OAuth2AccessToken accessToken = this._service.getAccessToken(urlParameters.get("code")[0]);

            final AuthTokenImpl accessData = new AuthTokenImpl()
                .setAccessToken(accessToken.getAccessToken())
                .setRefreshToken(accessToken.getRefreshToken())
                .setIsSuccess(true)
                .setExpiration(OffsetDateTime.now().plus(accessToken.getExpiresIn(), ChronoUnit.SECONDS));

            if (accessToken instanceof OpenIdOAuth2AccessToken) {
                accessData.setIdentityToken(((OpenIdOAuth2AccessToken) accessToken).getOpenIdToken());
            }

            this._tokenStorage.storeAuthData(accessData);

        } catch (IOException | InterruptedException | ExecutionException e) {
            throw new SmintIoAuthenticatorException(
                AuthenticatorError.CannotAcquireSmintIoToken,
                "Failed to redeem access token for the OAuth code."
            );
        }

        return this;
    }


    @Override
    public boolean isTargetOfReceivedAuthorizationData(final Map<String, String[]> urlParameters) {

        Objects.requireNonNull(urlParameters, "No url parameters with authentication data have been passed!");

        return !urlParameters.isEmpty()
            && urlParameters.get("code") != null && urlParameters.get("code").length == 1
            && urlParameters.get("state") != null && urlParameters.get("state").length == 1
            && this._oAuthApiSecret.equals(urlParameters.get("state")[0]);
    }


    /**
     * Validates the available settings data for requesting an OAuth access token.
     *
     * <p>
     * At least a client ID, a client secret and a local redirection URL are necessary.
     * </p>
     *
     * @param settings the settings data.
     * @throws SmintIoAuthenticatorException in case the settings are invalid.
     */
    private void validateForOAuth(final ISettingsModel settings) throws SmintIoAuthenticatorException {

        if (settings == null) {
            throw new SmintIoAuthenticatorException(
                AuthenticatorError.Generic,
                "No settings data available, holding necessary OAuth data."
            );
        }

        if (settings.getTenantId() == null || settings.getTenantId().isEmpty()) {
            throw new SmintIoAuthenticatorException(AuthenticatorError.Generic, "No tenant ID has been set.");
        }

        if (settings.getOAuthClientId() == null || settings.getOAuthClientId().isEmpty()) {
            throw new SmintIoAuthenticatorException(AuthenticatorError.Generic, "No OAuth client ID has been set.");
        }

        if (settings.getOAuthClientSecret() == null || settings.getOAuthClientSecret().isEmpty()) {
            throw new SmintIoAuthenticatorException(AuthenticatorError.Generic, "No OAuth client secret available.");
        }

        if (settings.getOAuthLocalUrlReceivingAccessData() == null) {
            throw new SmintIoAuthenticatorException(
                AuthenticatorError.Generic, "No OAuth local redirect URL available."
            );
        }
    }
}
