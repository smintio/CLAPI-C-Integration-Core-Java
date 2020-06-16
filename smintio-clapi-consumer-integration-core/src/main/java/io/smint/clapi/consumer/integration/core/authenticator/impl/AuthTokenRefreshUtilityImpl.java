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

import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;

import io.smint.clapi.consumer.integration.core.authenticator.IAuthTokenRefreshUtility;
import io.smint.clapi.consumer.integration.core.authenticator.ISmintIoAuthenticator;
import io.smint.clapi.consumer.integration.core.configuration.impl.AuthTokenMemoryStorage;
import io.smint.clapi.consumer.integration.core.configuration.models.IAuthTokenModel;
import io.smint.clapi.consumer.integration.core.configuration.models.ISettingsModel;
import io.smint.clapi.consumer.integration.core.configuration.models.impl.AuthTokenImpl;
import io.smint.clapi.consumer.integration.core.configuration.models.impl.AuthTokenJsonConverter;
import io.smint.clapi.consumer.integration.core.exceptions.SmintIoAuthenticatorException;


/**
 * A utility to refresh the OAuth authentication token, in case it has expired.
 *
 * <p>
 * Since refreshing a once valid OAuth authentication token can be performed without user interaction, such utility can
 * be very simple. Implementing classes are NOT required to support complex first authorization, as this involves manual
 * interaction. Utilities of that kind are only intended to <i>refresh</i> authentication tokens and not to obtain such
 * token initially.
 * </p>
 */
public class AuthTokenRefreshUtilityImpl implements IAuthTokenRefreshUtility {

    private static final Logger LOG = Logger.getLogger(AuthTokenRefreshUtilityImpl.class.getName());


    private ISmintIoAuthenticator _authenticator;
    private ISettingsModel _settings;


    @Inject
    public AuthTokenRefreshUtilityImpl(final ISmintIoAuthenticator authenticator, final ISettingsModel settings) {

        Objects.requireNonNull(authenticator, "Invalid authenticator has been provided.");
        Objects.requireNonNull(settings, "Invalid settings have been provided.");

        this._authenticator = authenticator;
        this._settings = settings;
    }

    public ISmintIoAuthenticator getAuthenticator() {
        return this._authenticator;
    }

    public AuthTokenRefreshUtilityImpl setAuthenticator(final ISmintIoAuthenticator newAuthenticator) {
        this._authenticator = newAuthenticator;
        return this;
    }

    public ISettingsModel getSettings() {
        return this._settings;
    }

    public AuthTokenRefreshUtilityImpl setSettings(final ISettingsModel newSettings) {
        this._settings = newSettings;
        return this;
    }

    @Override
    public IAuthTokenModel refreshOAuthToken(final IAuthTokenModel expiredToken) throws SmintIoAuthenticatorException {

        if (expiredToken == null) {
            throw new IllegalArgumentException("Provided 'expiredToken' parameter is null!");
        }

        final ISmintIoAuthenticator authenticator = this.getAuthenticator();
        if (authenticator == null) {
            throw new NullPointerException("No authenticator available!");
        }


        // check validity for refresh
        try {
            SmintIoAuthenticatorImpl.validateForTokenRefresh(expiredToken);

        } catch (final SmintIoAuthenticatorException authException) {
            LOG.log(
                Level.WARNING,
                authException,
                () -> "authentication token data did not contain sufficent data for refreshing. Token Data: "
                    + new AuthTokenJsonConverter().encode(expiredToken)
            );

            if (expiredToken.isSuccess() && !expiredToken.hasExpired()) {
                return new AuthTokenImpl(expiredToken).setIsSuccess(false);
            }
        }


        // wrap the token data in memory based token storage
        final AuthTokenMemoryStorage authTokenStorage = new AuthTokenMemoryStorage();
        authTokenStorage.storeAuthData(expiredToken);

        // refresh the token
        authenticator.refreshSmintIoToken(this.getSettings(), authTokenStorage);

        // get the token
        final IAuthTokenModel refreshedToken = authTokenStorage.get();
        if (refreshedToken != null) {
            return refreshedToken;

        } else {
            return new AuthTokenImpl().setIsSuccess(false);
        }
    }
}
