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
 * Wraps a {@link IAuthTokenStorage} and calls {@link Object#notifyAll()} on the wrapped instance and itself.
 *
 * <p>
 * This instance requires another instance to wrap, which will be queried for data. Hence this instance is following the
 * proxy pattern. It is used internally during synchronization jobs. Its usage is vital to notify waiting instances of
 * new OAuth data. eg: right at the beginning, there is no OAuth token data available. So a process to create on must be
 * started, involving the user's interaction. The rest of the synchronization library must wait for token data to become
 * available. So, any instance waiting for this token data may call {@link Object#wait()} on the token data storage and
 * get notified, as soon as {@link #storeAuthData(IAuthTokenModel)} is being called.
 * </p>
 *
 * <p>
 * This proxy instance is used to guarantee, that the required notifications to waiting instances will be created.
 * </p>
 */
@Singleton
public class AuthTokenStorageWithNotify implements IAuthTokenStorage {

    private final IAuthTokenStorage _tokenStorage;


    /**
     * Create a new facade to another token provider, calling {@link Object #notifyAll()} for new token data.
     *
     * @param tokenStorage the storage to wrap and query for data - must not be {@code null}. If available, the DI
     *                     injects an instance named {@code authTokenPersistentStorage}.
     */
    @Inject
    public AuthTokenStorageWithNotify(@Named("authTokenPersistentStorage") final IAuthTokenStorage tokenStorage) {
        this._tokenStorage = tokenStorage;

        Objects.requireNonNull(tokenStorage, "Other token storage to wrap with proxy cache is invalid (null)!");
    }


    @Override
    public IAuthTokenModel get() {
        return this.getAuthData();
    }


    @Override
    public IAuthTokenModel getAuthData() {
        return this._tokenStorage.getAuthData();
    }


    @Override
    public IAuthTokenStorage storeAuthData(final IAuthTokenModel newAuthTokenData) {
        try {
            this._tokenStorage.storeAuthData(newAuthTokenData);
        } finally {
            this._tokenStorage.notifyAll();
            this.notifyAll();
        }
        return this;
    }
}
