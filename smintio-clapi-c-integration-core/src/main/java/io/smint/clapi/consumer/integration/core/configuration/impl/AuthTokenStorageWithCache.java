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

import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import io.smint.clapi.consumer.integration.core.configuration.IAuthTokenStorage;
import io.smint.clapi.consumer.integration.core.configuration.models.IAuthTokenModel;


/**
 * Wraps a {@link IAuthTokenProvider} and provides a cache to its retrieved data.
 *
 * <p>
 * This instance requires another instance to wrap, which will be queried for data. Hence this instance is following the
 * proxy pattern. It is used internally during synchronization jobs. Therefore its lifetime is very short which will
 * avoid any stale cache. Nevertheless be aware that stale caches could happen.
 * </p>
 */
@Named("authTokenStorage")
@Singleton
public class AuthTokenStorageWithCache implements IAuthTokenStorage {

    private final IAuthTokenStorage _tokenStorage;
    private IAuthTokenModel _cachedToken;


    /**
     * Create a new facade to another token provider, caching its result for improved performance.
     *
     * @param tokenStorage the storage to wrap and query for data - must not be {@code null}. If available, the DI
     *                     injects an instance named {@code authTokenPersistentStorage}.
     */
    @Inject
    public AuthTokenStorageWithCache(@Named("authTokenPersistentStorage") final IAuthTokenStorage tokenStorage) {
        this._tokenStorage = tokenStorage;

        Objects.requireNonNull(tokenStorage, "Other token storage to wrap with proxy cache is invalid (null)!");
    }


    @Override
    public IAuthTokenModel get() {
        return this.getAuthData();
    }


    @Override
    public IAuthTokenModel getAuthData() {
        if (this._cachedToken == null) {
            this._cachedToken = this._tokenStorage.getAuthData();
        }

        return this._cachedToken;
    }


    @Override
    public IAuthTokenStorage storeAuthData(final IAuthTokenModel newAuthTokenData) {
        this._cachedToken = null;
        this._tokenStorage.storeAuthData(newAuthTokenData);
        return this;
    }
}
