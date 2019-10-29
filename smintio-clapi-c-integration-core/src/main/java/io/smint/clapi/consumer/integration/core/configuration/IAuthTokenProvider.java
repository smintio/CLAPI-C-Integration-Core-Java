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

import java.util.concurrent.Future;

import io.smint.clapi.consumer.integration.core.configuration.models.IAuthTokenModel;


/**
 * Provides OAuth access token data to authenticate to the Smint.io RESTful API.
 */
public interface IAuthTokenProvider {

    /**
     * Provides the OAuth authentication token data that is needed for accessing Smint.io API.
     *
     * @return a {@code Future} that may complete in an asynchronous way.
     */
    Future<IAuthTokenModel> getAuthTokenModelAsync();


    /**
     * Sets set of OAuth authentication data to be made persistent and retrieved on next call to
     * {@link #getSettingsModelAsync()}.
     *
     * <p>
     * The access token should be persisted to the underlaying storage. In case this storage takes a lot of IO and time
     * (eg: database), please make use of {@link java.util.concurrent.CompletableFuture#runAsync(Runnable)} and then
     * return {@code this} on completion.
     * </p>
     *
     * @param newAuthTokenData new OAuth token data for authorization with Smint.io API server, that need to be made
     *                         persistent.
     * @return a {@code Future} that may complete in an asynchronous way. Its value will return {@code this} instance.
     */
    Future<IAuthTokenProvider> setAuthModelAsync(final IAuthTokenModel newAuthTokenData);
}
