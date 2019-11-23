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

package io.smint.clapi.consumer.integration.core.configuration;

import javax.inject.Provider;

import io.smint.clapi.consumer.integration.core.configuration.models.IAuthTokenModel;


/**
 * Provides OAuth access token data to authenticate to the Smint.io RESTful API.
 *
 * <p>
 * It is vital for the synchronization job to have proper OAuth data available. This is not the case right at the
 * beginning and OAuth access token must be created with user interaction. In the end new authorization data will be
 * stored utilizing the store function {@link #storeAuthData(IAuthTokenModel)}. But how does the rest of the system get
 * notified about it? It is highly asynchronous as it involves external systems (user browser) to accept the
 * authorization request.
 * </p>
 */
public interface IAuthTokenStorage extends Provider<IAuthTokenModel> {

    /**
     * Provides the OAuth authentication data to authorize access to Smint.io API.
     *
     * @return Currently valid OAuth data or {@code null} if none is available.
     */
    IAuthTokenModel getAuthData();


    /**
     * Stores a new set of OAuth authentication data to be made persistent and retrieved on next call to
     * {@link #getAuthData()}.
     *
     * <p>
     * The access token should be persisted to the underlying storage. In case this storage takes a lot of IO and time
     * (eg: database), please make use of {@link java.util.concurrent.CompletableFuture#runAsync(Runnable)} and then
     * return {@code this} on completion.
     * </p>
     *
     * @param newAuthTokenData new OAuth token data for authorization with Smint.io API server, that need to be made
     *                         persistent.
     * @return {@code this} to support Fluent Interface.
     */
    IAuthTokenStorage storeAuthData(final IAuthTokenModel newAuthTokenData);
}
