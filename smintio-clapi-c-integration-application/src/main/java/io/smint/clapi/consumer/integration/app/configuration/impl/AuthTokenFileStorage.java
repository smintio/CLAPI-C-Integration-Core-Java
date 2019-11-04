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

package io.smint.clapi.consumer.integration.app.configuration.impl;

import java.io.File;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import io.smint.clapi.consumer.integration.core.configuration.IAuthTokenStorage;
import io.smint.clapi.consumer.integration.core.configuration.models.IAuthTokenModel;
import io.smint.clapi.consumer.integration.core.configuration.models.IModelStringConverter;
import io.smint.clapi.consumer.integration.core.configuration.models.impl.AuthTokenJsonConverter;


/**
 * Stores a {@link IAuthTokenModel} to a file and reads the token from the file.
 *
 * <p>
 * The format of the file is defined by the instance of {@link IModelStringConverter} that is provided to this instance.
 * </p>
 *
 * <p>
 * No caching is performed. The token is read from the file with every call to {@link #getAuthData()} or {@link #get()}
 * over and over again.
 * </p>
 */
@Named("authTokenPersistentStorage")
@Singleton
public class AuthTokenFileStorage extends FileModelStorage<IAuthTokenModel> implements IAuthTokenStorage {

    /**
     * Create a new facade to another token provider, caching its result for improved performance.
     *
     * @param tokenConverter the provider to wrap and query for data - must not be {@code null}.
     * @param fileStorage    the file to store the token to.
     */
    @Inject
    public AuthTokenFileStorage(
        final AuthTokenJsonConverter tokenConverter,
        @Named("smint.io-auth-token-file") final File fileStorage
    ) {
        super(tokenConverter, fileStorage);
    }


    @Override
    public IAuthTokenModel getAuthData() {
        return this.get();
    }


    @Override
    public AuthTokenFileStorage storeAuthData(final IAuthTokenModel newAuthTokenData) {
        this.store(newAuthTokenData);
        return this;
    }
}
