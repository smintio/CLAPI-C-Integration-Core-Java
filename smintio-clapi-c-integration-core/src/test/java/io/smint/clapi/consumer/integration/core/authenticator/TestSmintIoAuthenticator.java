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

package io.smint.clapi.consumer.integration.core.authenticator;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.mock.Behavior;
import okhttp3.mock.HttpCode;
import okhttp3.mock.MockInterceptor;

import okio.Buffer;

import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import io.smint.clapi.consumer.integration.core.authenticator.impl.SmintIoAuthenticatorImpl;
import io.smint.clapi.consumer.integration.core.configuration.IAuthTokenStorage;
import io.smint.clapi.consumer.integration.core.configuration.models.IAuthTokenModel;
import io.smint.clapi.consumer.integration.core.configuration.models.ISettingsModel;
import io.smint.clapi.consumer.integration.core.exceptions.SmintIoAuthenticatorException;
import io.smint.clapi.consumer.integration.core.exceptions.SmintIoAuthenticatorException.AuthenticatorError;


@ExtendWith(MockitoExtension.class)
@DisplayName("Test OAuth Authenticator: SmintIoAuthenticatorImpl.class")
public class TestSmintIoAuthenticator {

    // CHECKSTYLE OFF: MultipleStringLiterals
    private static final String ACCESS_TOKEN_OLD = "9834hjsdfkj3";
    private static final String ACCESS_TOKEN_NEW = "k2h277718k24";

    private static final String REFRESH_TOKEN_OLD = "REFRESH-9827";
    private static final String REFRESH_TOKEN_NEW = "REFRESH-2992";
    private static final String IDENTITY_TOKEN_NEW = "NR82oojh23sd";

    private static final String CLIENT_ID = "uierer";
    private static final String CLIENT_SECRET = "super-secret";

    private static final String TENANT_ID = "tenant";
    private static final String ENDPOINT_URL = MessageFormat
        .format(SmintIoAuthenticatorImpl.OAUTH_TOKEN_ENDPOINT, TENANT_ID);


    public SmintIoAuthenticatorImpl createAuthenticator(
        final MockInterceptor interceptor,
        final ISettingsModel settings,
        final IAuthTokenStorage tokenStorage,
        final IAuthTokenModel authData,
        final String jsonAnswer
    ) {

        final IAuthTokenModel[] authDataStorage = new IAuthTokenModel[1];
        authDataStorage[0] = authData;

        lenient().when(tokenStorage.get()).then((invocation) -> authDataStorage[0]);
        lenient().when(tokenStorage.getAuthData()).then((invocation) -> authDataStorage[0]);
        lenient().when(tokenStorage.storeAuthData((IAuthTokenModel) any()))
            .then((invocation) -> {
                final IAuthTokenModel[] customDataStorage = authDataStorage;
                customDataStorage[0] = invocation.getArgument(0);
                return invocation.getMock();
            });

        lenient().when(settings.getTenantId()).thenReturn(TENANT_ID);
        lenient().when(settings.getOAuthClientId()).thenReturn(CLIENT_ID);
        lenient().when(settings.getOAuthClientSecret()).thenReturn(CLIENT_SECRET);
        lenient().when(authData.getAccessToken()).thenReturn(ACCESS_TOKEN_OLD);
        lenient().when(authData.getRefreshToken()).thenReturn(REFRESH_TOKEN_OLD);


        if (jsonAnswer != null && !jsonAnswer.isEmpty()) {
            interceptor.addRule()
                .post()
                .url(ENDPOINT_URL)
                .respond(jsonAnswer, MediaType.parse("application/json; charset=utf-8"));
        }


        interceptor.addRule()
            .pathMatches(Pattern.compile("^.*$"))
            .anyTimes()
            .respond(HttpCode.HTTP_404_NOT_FOUND);

        final SmintIoAuthenticatorImpl authenticator = new SmintIoAuthenticatorImpl(
            () -> new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build()
        );

        return authenticator;
    }


    @DisplayName("OAuth returns standard set of tokens")
    @Test
    public void testRefreshAccessToken(
        @Mock final ISettingsModel settings,
        @Mock final IAuthTokenStorage tokenStorage,
        @Mock final IAuthTokenModel authData
    ) throws SmintIoAuthenticatorException {

        final long expireAfterSeconds = 3600L;
        final OffsetDateTime expiryDate = OffsetDateTime.now(ZoneId.systemDefault()).plusSeconds(expireAfterSeconds);
        final String jsonAnswer = "{\n"
            + "\"success\":true,\n"
            + "\"access_token\": \"" + ACCESS_TOKEN_NEW + "\",\n"
            + "\"refresh_token\": \"" + REFRESH_TOKEN_NEW + "\",\n"
            + "\"token_type\": \"bearer\",\n"
            + "\"expires\": " + expireAfterSeconds + "\n"
            + "}";

        final SmintIoAuthenticatorImpl authenticator = this.createAuthenticator(
            new MockInterceptor(), settings, tokenStorage, authData, jsonAnswer
        );
        authenticator.refreshSmintIoToken(settings, tokenStorage);

        final IAuthTokenModel newAuthData = tokenStorage.getAuthData();
        Assertions.assertEquals(ACCESS_TOKEN_NEW, newAuthData.getAccessToken(), "Failed to get hold on access token.");
        Assertions.assertEquals(
            REFRESH_TOKEN_NEW,
            newAuthData.getRefreshToken(),
            "Failed to get hold on access token."
        );

        Assertions.assertNotNull(newAuthData.getExpiration(), "Expiry date was not consumed!");
        Assertions.assertEquals(
            expiryDate.toEpochSecond(),
            newAuthData.getExpiration().toEpochSecond(),
            "Expiry date was not read correctly"
        );
    }


    @DisplayName("OAuth returns minimal set of tokens")
    @Test
    public void testRefreshAccessTokenMinimal(
        @Mock final ISettingsModel settings,
        @Mock final IAuthTokenStorage tokenStorage,
        @Mock final IAuthTokenModel authData
    ) throws SmintIoAuthenticatorException {

        final String jsonAnswer = "{\n"
            + "\"access_token\": \"" + ACCESS_TOKEN_NEW + "\",\n"
            + "\"token_type\": \"bearer\"\n"
            + "}";

        final SmintIoAuthenticatorImpl authenticator = this.createAuthenticator(
            new MockInterceptor(), settings, tokenStorage, authData, jsonAnswer
        );
        authenticator.refreshSmintIoToken(settings, tokenStorage);

        final IAuthTokenModel newAuthData = tokenStorage.getAuthData();
        Assertions.assertEquals(ACCESS_TOKEN_NEW, newAuthData.getAccessToken(), "Failed to read new access token.");
        Assertions.assertEquals(
            REFRESH_TOKEN_OLD,
            newAuthData.getRefreshToken(),
            "Refresh token was removed unintentionally."
        );
    }


    @DisplayName("OAuth returns full set of tokens")
    @Test
    public void testRefreshAccessTokenFull(
        @Mock final ISettingsModel settings,
        @Mock final IAuthTokenStorage tokenStorage,
        @Mock final IAuthTokenModel authData
    ) throws SmintIoAuthenticatorException {

        final long expireAfterSeconds = 800L;
        final OffsetDateTime expiryDate = OffsetDateTime.now(ZoneId.systemDefault()).plusSeconds(expireAfterSeconds);
        final String jsonAnswer = "{\n"
            + "\"success\":true,\n"
            + "\"access_token\": \"" + ACCESS_TOKEN_NEW + "\",\n"
            + "\"refresh_token\": \"" + REFRESH_TOKEN_NEW + "\",\n"
            + "\"identity_token\": \"" + IDENTITY_TOKEN_NEW + "\",\n"
            + "\"token_type\": \"bearer\",\n"
            + "\"expires\": " + expireAfterSeconds + "\n"
            + "}";


        final SmintIoAuthenticatorImpl authenticator = this.createAuthenticator(
            new MockInterceptor(), settings, tokenStorage, authData, jsonAnswer
        );
        authenticator.refreshSmintIoToken(settings, tokenStorage);

        final IAuthTokenModel newAuthData = tokenStorage.getAuthData();
        Assertions.assertEquals(ACCESS_TOKEN_NEW, newAuthData.getAccessToken(), "Failed to read new access token.");
        Assertions.assertEquals(
            REFRESH_TOKEN_NEW, newAuthData.getRefreshToken(),
            "Refresh token is missing, although in existent in response."
        );

        Assertions.assertEquals(
            IDENTITY_TOKEN_NEW, newAuthData.getIdentityToken(),
            "Identity token did not make its way from the response the the auth data."
        );

        Assertions.assertEquals(
            expiryDate.toEpochSecond(),
            newAuthData.getExpiration().toEpochSecond(),
            "Expiry date was not read from response!"
        );
    }

    @DisplayName("OAuth 'token_type' is being ignored if missing.")
    @Test
    public void testRefreshAccessTokenIgnoreBearerType(
        @Mock final ISettingsModel settings,
        @Mock final IAuthTokenStorage tokenStorage,
        @Mock final IAuthTokenModel authData
    ) throws SmintIoAuthenticatorException {

        final String jsonAnswer = "{\n"
            + "\"access_token\": \"" + ACCESS_TOKEN_NEW + "\",\n"
            + "}";

        final SmintIoAuthenticatorImpl authenticator = this.createAuthenticator(
            new MockInterceptor(), settings, tokenStorage, authData, jsonAnswer
        );
        authenticator.refreshSmintIoToken(settings, tokenStorage);

        final IAuthTokenModel newAuthData = tokenStorage.getAuthData();
        Assertions.assertEquals(ACCESS_TOKEN_NEW, newAuthData.getAccessToken(), "Failed to read new access token.");
    }


    @DisplayName("OAuth error JSON flag in response throws error")
    @Test
    public void testRefreshAccessTokenWithFalseSuccessFlag(
        @Mock final ISettingsModel settings,
        @Mock final IAuthTokenStorage tokenStorage,
        @Mock final IAuthTokenModel authData
    ) throws SmintIoAuthenticatorException {

        final long expireAfterSeconds = 800L;
        OffsetDateTime.now(ZoneId.systemDefault()).plusSeconds(expireAfterSeconds);
        final String jsonAnswer = "{\n"
            + "\"success\":false,\n"
            + "\"access_token\": \"" + ACCESS_TOKEN_NEW + "\",\n"
            + "\"refresh_token\": \"" + REFRESH_TOKEN_NEW + "\",\n"
            + "\"identity_token\": \"" + IDENTITY_TOKEN_NEW + "\",\n"
            + "\"token_type\": \"bearer\",\n"
            + "\"expires\": " + expireAfterSeconds + "\n"
            + "}";


        final SmintIoAuthenticatorImpl authenticator = this.createAuthenticator(
            new MockInterceptor(), settings, tokenStorage, authData, jsonAnswer
        );

        final SmintIoAuthenticatorException thrown = Assertions.assertThrows(
            SmintIoAuthenticatorException.class,
            () -> authenticator.refreshSmintIoToken(settings, tokenStorage)
        );
        Assertions.assertEquals(
            AuthenticatorError.CannotRefreshSmintIoToken,
            thrown.getErrorType(),
            "Thrown exception uses inexpected error type flag!"
        );
    }


    @DisplayName("OAuth error JSON flag in response with custom message.")
    @Test
    public void testRefreshAccessTokenWithCustomErrorMessage(
        @Mock final ISettingsModel settings,
        @Mock final IAuthTokenStorage tokenStorage,
        @Mock final IAuthTokenModel authData
    ) throws SmintIoAuthenticatorException {

        final String errorMessage = "Error message thrown!";
        final String jsonAnswer = "{\n"
            + "\"error_msg\":\"" + errorMessage + "\",\n"
            + "\"success\":false,\n"
            + "\"token_type\": \"bearer\",\n"
            + "}";


        final SmintIoAuthenticatorImpl authenticator = this.createAuthenticator(
            new MockInterceptor(), settings, tokenStorage, authData, jsonAnswer
        );

        final SmintIoAuthenticatorException thrown = Assertions.assertThrows(
            SmintIoAuthenticatorException.class,
            () -> authenticator.refreshSmintIoToken(settings, tokenStorage)
        );
        Assertions.assertEquals(
            errorMessage,
            thrown.getMessage(),
            "The error message of server is not passed to thrown exception."
        );
    }


    @DisplayName("OAuth response does not contain an access token.")
    @Test
    public void testRefreshAccessTokenFoundNoToken(
        @Mock final ISettingsModel settings,
        @Mock final IAuthTokenStorage tokenStorage,
        @Mock final IAuthTokenModel authData
    ) throws SmintIoAuthenticatorException {

        final String jsonAnswer = "{\n"
            + "\"success\":true,\n"
            + "\"refresh_token\": \"" + REFRESH_TOKEN_NEW + "\",\n"
            + "\"identity_token\": \"" + IDENTITY_TOKEN_NEW + "\",\n"
            + "\"token_type\": \"bearer\",\n"
            + "}";


        final SmintIoAuthenticatorImpl authenticator = this.createAuthenticator(
            new MockInterceptor(), settings, tokenStorage, authData, jsonAnswer
        );

        final SmintIoAuthenticatorException thrown = Assertions.assertThrows(
            SmintIoAuthenticatorException.class,
            () -> authenticator.refreshSmintIoToken(settings, tokenStorage)
        );
        Assertions.assertEquals(
            AuthenticatorError.CannotRefreshSmintIoToken,
            thrown.getErrorType(),
            "Thrown exception uses inexpected error type flag!"
        );
    }


    @DisplayName("OAuth response is empty JSON.")
    @Test
    public void testRefreshAccessTokenEmptyJsonResponse(
        @Mock final ISettingsModel settings,
        @Mock final IAuthTokenStorage tokenStorage,
        @Mock final IAuthTokenModel authData
    ) throws SmintIoAuthenticatorException {

        final String jsonAnswer = "{\n"
            + "}";


        final SmintIoAuthenticatorImpl authenticator = this.createAuthenticator(
            new MockInterceptor(), settings, tokenStorage, authData, jsonAnswer
        );

        final SmintIoAuthenticatorException thrown = Assertions.assertThrows(
            SmintIoAuthenticatorException.class,
            () -> authenticator.refreshSmintIoToken(settings, tokenStorage)
        );
        Assertions.assertEquals(
            AuthenticatorError.CannotRefreshSmintIoToken,
            thrown.getErrorType(),
            "Thrown exception uses inexpected error type flag!"
        );
    }


    @DisplayName("OAuth response indicates invalid authorization.")
    @Test
    public void testRefreshAccessTokenInvalidAuthorization(
        @Mock final ISettingsModel settings,
        @Mock final IAuthTokenStorage tokenStorage,
        @Mock final IAuthTokenModel authData
    ) throws SmintIoAuthenticatorException {

        final MockInterceptor interceptor = new MockInterceptor();
        interceptor.addRule()
            .pathMatches(Pattern.compile(".*"))
            .anyTimes()
            .respond(HttpCode.HTTP_401_UNAUTHORIZED)
            .header("WWW-Authenticate", "Basic");


        final SmintIoAuthenticatorImpl authenticator = this.createAuthenticator(
            interceptor, settings, tokenStorage, authData, null
        );

        final SmintIoAuthenticatorException thrown = Assertions.assertThrows(
            SmintIoAuthenticatorException.class,
            () -> authenticator.refreshSmintIoToken(settings, tokenStorage)
        );
        Assertions.assertEquals(
            AuthenticatorError.CannotRefreshSmintIoToken,
            thrown.getErrorType(),
            "Thrown exception uses inexpected error type flag!"
        );
    }


    @DisplayName("OAuth response is 'URL not found (" + HttpCode.HTTP_404_NOT_FOUND + ")'.")
    @Test
    public void testRefreshAccessTokenHttpNotFound(
        @Mock final ISettingsModel settings,
        @Mock final IAuthTokenStorage tokenStorage,
        @Mock final IAuthTokenModel authData
    ) throws SmintIoAuthenticatorException {

        final SmintIoAuthenticatorImpl authenticator = this.createAuthenticator(
            new MockInterceptor(), settings, tokenStorage, authData, null
        );

        final SmintIoAuthenticatorException thrown = Assertions.assertThrows(
            SmintIoAuthenticatorException.class,
            () -> authenticator.refreshSmintIoToken(settings, tokenStorage)
        );
        Assertions.assertEquals(
            AuthenticatorError.CannotRefreshSmintIoToken,
            thrown.getErrorType(),
            "Unkown custom type flag for thrown exception!"
        );
    }


    @DisplayName("OAuth request uses refresh token.")
    @Test
    public void testRefreshAccessTokenUsesRefreshToken(
        @Mock final ISettingsModel settings,
        @Mock final IAuthTokenStorage tokenStorage,
        @Mock final IAuthTokenModel authData
    ) throws SmintIoAuthenticatorException, IOException {

        final String jsonAnswer = "{\n"
            + "\"success\":true,\n"
            + "\"access_token\": \"" + ACCESS_TOKEN_NEW + "\",\n"
            + "\"refresh_token\": \"" + REFRESH_TOKEN_NEW + "\",\n"
            + "\"token_type\": \"bearer\",\n"
            + "\"expires\": 3600\n"
            + "}";

        final Request[] requests = new Request[1];
        final MockInterceptor interceptor = new MockInterceptor(Behavior.UNORDERED);
        interceptor.addRule()
            .post()
            .url(ENDPOINT_URL)
            .anyTimes()
            .answer((request) -> {
                requests[0] = request;
                return new Response.Builder()
                    .code(HttpCode.HTTP_200_OK)
                    .body(ResponseBody.create(MediaType.parse("application/json; charset=utf-8"), jsonAnswer));
            });


        final SmintIoAuthenticatorImpl authenticator = this.createAuthenticator(
            interceptor, settings, tokenStorage, authData, null
        );
        authenticator.refreshSmintIoToken(settings, tokenStorage);

        Assertions.assertNotNull(requests[0], "Failed to store request of OAuth token refresh!");
        Assertions.assertNotNull(requests[0].body(), "Request of token refresh does not contain a request body!");


        final Buffer buffer = new Buffer();
        requests[0].body().writeTo(buffer);
        final String body = buffer.readString(StandardCharsets.UTF_8);

        final JSONObject json = new JSONObject(body);

        Assertions.assertTrue(json.has("refresh_token"), "OAuth refresh request does not contain a refresh token!");
        Assertions.assertTrue(json.has("grant_type"), "OAuth refresh request does not contain the token type!");
        Assertions.assertTrue(json.has("client_id"), "OAuth refresh request does not contain the client ID!");
        Assertions.assertTrue(json.has("client_secret"), "OAuth refresh request does not contain the client secret!");

        Assertions.assertEquals(
            json.getString("client_id"),
            CLIENT_ID,
            "OAuth refresh request does not contain the client ID!"
        );

        Assertions.assertEquals(
            json.getString("client_secret"),
            CLIENT_SECRET,
            "OAuth refresh request does not contain the client secret!"
        );

        Assertions.assertEquals(
            json.getString("refresh_token"),
            REFRESH_TOKEN_OLD,
            "OAuth refresh request does not contain refresh token!"
        );

        Assertions.assertEquals(
            json.getString("grant_type"),
            "refresh_token",
            "OAuth refresh request does not contain a grant type!"
        );
    }

    // CHECKSTYLE ON: MultipleStringLiterals
}
