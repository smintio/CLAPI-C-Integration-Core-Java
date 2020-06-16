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

package io.smint.clapi.consumer.integration.core.factory.impl;

import javax.inject.Inject;
import javax.inject.Provider;

import io.smint.clapi.consumer.integration.core.authenticator.IAuthTokenRefreshUtility;
import io.smint.clapi.consumer.integration.core.configuration.IAuthTokenStorage;
import io.smint.clapi.consumer.integration.core.configuration.impl.AuthTokenStorageWrapperWithRefresh;
import io.smint.clapi.consumer.integration.core.factory.ISyncTargetFactory;


/**
 * Provides a storage instance by wrapping the target storage with {@link AuthTokenStorageWrapperWithRefresh}.
 *
 * <p>
 * Requires an instance of {@link SyncTargetFactory} as the sync target storage is fetched and wrapped with
 * {@link AuthTokenStorageWrapperWithRefresh}.
 * </p>
 */
class AuthTokenStorageProvider implements Provider<IAuthTokenStorage> {

    private final IAuthTokenRefreshUtility _refreshUtility;
    private final ISyncTargetFactory _syncTargetFactory;

    private IAuthTokenStorage storage;


    @Inject
    public AuthTokenStorageProvider(
        final ISyncTargetFactory syncTargetFactory,
        final IAuthTokenRefreshUtility refreshUtility
    ) {
        if (syncTargetFactory == null) {
            throw new IllegalArgumentException("Invalid sync target factory provided!");
        }

        if (refreshUtility == null) {
            throw new IllegalArgumentException("Invalid refresh utility has been provided!");
        }

        this._refreshUtility = refreshUtility;
        this._syncTargetFactory = syncTargetFactory;
    }


    @Override
    public IAuthTokenStorage get() {
        if (this.storage == null) {
            this.storage = new AuthTokenStorageWrapperWithRefresh(
                this._syncTargetFactory.getAuthTokenStorage(), this._refreshUtility
            );
        }

        return this.storage;
    }
}
