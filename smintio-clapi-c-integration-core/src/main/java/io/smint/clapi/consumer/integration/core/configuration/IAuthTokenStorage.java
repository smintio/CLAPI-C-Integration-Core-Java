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
 *
 * <p>
 * All instances in a need for this data might decide and are encouraged to call {@link Object#wait()} on the instances
 * implementing this interface, to get notified of available new token data. Hence the store function must call
 * {@link Object#notifyAll()} whenever it is being called - no matter whether valid data is being passed to be stored.
 * Even for invalid data, a notification should be sent, as it is quite uncertain, whether a second try to acquire OAuth
 * data is on its way. So waiting instances need to get informed about each try - even failed ones.
 * </p>
 *
 * <p>
 * Especially this is required right at the beginning of the sync process, when no OAuth token data is available yet.
 * Then there is a need to wait for the heavily asynchronous process that involves external systems such as a user's
 * browser and the user's manual intervention. As soon as the user is involved, there is no fixed time to wait for, so a
 * wait/notify scheme must be used.
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
     * <p>
     * Calls {@link Object#notifyAll()} on {@code this} for every call of this function - not matter what data has been
     * provided in parameter {@code newAuthTokenData}.
     * </p>
     *
     * @param newAuthTokenData new OAuth token data for authorization with Smint.io API server, that need to be made
     *                         persistent.
     * @return {@code this} to support Fluent Interface.
     */
    IAuthTokenStorage storeAuthData(final IAuthTokenModel newAuthTokenData);
}
