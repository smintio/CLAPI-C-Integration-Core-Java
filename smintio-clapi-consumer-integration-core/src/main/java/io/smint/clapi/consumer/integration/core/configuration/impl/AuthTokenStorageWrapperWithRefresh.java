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

package io.smint.clapi.consumer.integration.core.configuration.impl;

import javax.inject.Inject;

import io.smint.clapi.consumer.integration.core.authenticator.IAuthTokenRefreshUtility;
import io.smint.clapi.consumer.integration.core.configuration.IAuthTokenStorage;
import io.smint.clapi.consumer.integration.core.configuration.models.IAuthTokenModel;


/**
 * Stores OAuth authentication data in another storage and automatically refresh the token, when needed.
 *
 * <p>
 * Whenever the token model is requested, it is checked whether it needs a refresh. If so, then the OAuth server is
 * asked to return a new OAuth token.
 * </p>
 * <p>
 * This is a wrapper class. It does not store the token itself but uses another token storage implementation as a
 * delegate. The delegate must be provided in the constructor.
 * </p>
 */
public class AuthTokenStorageWrapperWithRefresh implements IAuthTokenStorage {


    private final IAuthTokenRefreshUtility _refreshUtility;
    private final IAuthTokenStorage _tokenStorage;


    @Inject
    public AuthTokenStorageWrapperWithRefresh(
        final IAuthTokenStorage delegateTokenStorage,
        final IAuthTokenRefreshUtility refreshUtility
    ) {
        if (delegateTokenStorage == null) {
            throw new IllegalArgumentException("Invalid token storage delegate provided!");
        }

        if (refreshUtility == null) {
            throw new IllegalArgumentException("Invalid refresh utility has been provided!");
        }

        this._refreshUtility = refreshUtility;
        this._tokenStorage = delegateTokenStorage;
    }

    @Override
    public IAuthTokenModel get() {
        return this.getAuthData();
    }


    @Override
    public IAuthTokenModel getAuthData() {
        final IAuthTokenModel authData = this._tokenStorage.getAuthData();

        if (authData != null && authData.isSuccess() && authData.hasExpired()) {
            // token data has expired, to try to refresh it

            final IAuthTokenModel refreshedAuthData = this._refreshUtility.refreshOAuthToken(authData);
            if (refreshedAuthData != null && refreshedAuthData.isSuccess()) {
                return this._tokenStorage.storeAuthData(refreshedAuthData).getAuthData();
            }
        }

        return authData;
    }


    @Override
    public IAuthTokenStorage storeAuthData(final IAuthTokenModel newAuthTokenData) {
        this._tokenStorage.storeAuthData(newAuthTokenData);
        return this;
    }
}
