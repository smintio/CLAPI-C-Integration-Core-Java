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

import java.text.MessageFormat;
import java.util.Objects;

import com.github.scribejava.apis.openid.OpenIdJsonTokenExtractor;
import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.extractors.TokenExtractor;
import com.github.scribejava.core.model.OAuth2AccessToken;

import io.smint.clapi.consumer.integration.core.configuration.models.ISettingsModel;


/**
 * The API description for <a href="https://github.com/scribejava/scribejava">ScribeJava</a>.
 *
 * <p>
 * see <a href=
 * "https://github.com/scribejava/scribejava/blob/master/scribejava-apis/src/main/java/com/github/scribejava/apis/GoogleApi20.java"
 * >https://github.com/scribejava/scribejava/blob/master/scribejava-apis/src/main/java/com/github/scribejava/apis/GoogleApi20.java
 * </a>
 * </p>
 */
public class SmintIoApiForScribe extends DefaultApi20 {

    /**
     * The Smint.io API end point for initiating the OAuth authorization process.
     *
     * <p>
     * the placeholder {@code {0}} is replaced with the tenant ID, fetched from {@code ISettingsModel#getTenantId()}.
     * {@link MessageFormat#format(String, Object...)} is used to fill the placeholder.
     * </p>
     *
     * <pre>
     * {@code SMINT_IO__AUTHORIZATION_ENDPOINT} = {@value #SMINT_IO__AUTHORIZATION_ENDPOINT}
     * </pre>
     */
    public static final String SMINT_IO__AUTHORIZATION_ENDPOINT = "https://{0}.smint.io/connect/authorize";


    /**
     * The Smint.io API end point for revoke an OAuth access token.
     *
     * <p>
     * the placeholder {@code {0}} is replaced with the tenant ID, fetched from {@code ISettingsModel#getTenantId()}.
     * {@link MessageFormat#format(String, Object...)} is used to fill the placeholder.
     * </p>
     *
     * <pre>
     * {@code SMINT_IO__REVOKE_TOKEN_ENDPOINT} = {@value #SMINT_IO__REVOKE_TOKEN_ENDPOINT}
     * </pre>
     */
    public static final String SMINT_IO__REVOKE_TOKEN_ENDPOINT = "https://{0}.smint.io/connect/revocation";


    /**
     * The Smint.io API end point to create the OAuth access token.
     *
     * <p>
     * the placeholder {@code {0}} is replaced with the tenant ID, fetched from {@code ISettingsModel#getTenantId()}.
     * {@link MessageFormat#format(String, Object...)} is used to fill the placeholder.
     * </p>
     *
     * <pre>
     * {@code SMINT_IO__TOKEN_ENDPOINT} = {@value #SMINT_IO__TOKEN_ENDPOINT}
     * </pre>
     */
    public static final String SMINT_IO__TOKEN_ENDPOINT = "https://{0}.smint.io/connect/token";


    private final String _tenantId;

    protected SmintIoApiForScribe(final String tenantId) {
        super();
        this._tenantId = tenantId;

        Objects.requireNonNull(tenantId, "Invalid tenantId has been set.");
    }


    private static SmintIoApiForScribe _instance = null;


    /**
     * Provides the singleton instance and returns it.
     *
     * @return the singleton instance, never {@code null}.
     */
    public static SmintIoApiForScribe instance() {
        Objects.requireNonNull(_instance, "No Smint.io API description instance is available.");
        return _instance;
    }


    /**
     * Provides the singleton instance and returns it.
     *
     * @param settings the settings to use, where to read the OAuth success redirection URL from.
     * @return the singleton instance, never {@code null}.
     */
    public static void createSingleton(final ISettingsModel settings) {
        Objects.requireNonNull(settings, "No settings have been made available.");
        _instance = new SmintIoApiForScribe(settings.getTenantId());
    }


    @Override
    public String getAccessTokenEndpoint() {
        return MessageFormat.format(SMINT_IO__TOKEN_ENDPOINT, this._tenantId);
    }

    @Override
    protected String getAuthorizationBaseUrl() {
        return MessageFormat.format(SMINT_IO__AUTHORIZATION_ENDPOINT, this._tenantId);
    }


    @Override
    public TokenExtractor<OAuth2AccessToken> getAccessTokenExtractor() {
        return OpenIdJsonTokenExtractor.instance();
    }

    @Override
    public String getRevokeTokenEndpoint() {
        return MessageFormat.format(SMINT_IO__REVOKE_TOKEN_ENDPOINT, this._tenantId);
    }

}
