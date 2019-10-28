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

import io.smint.clapi.consumer.integration.core.configuration.models.ISyncProcessModel;


/**
 * Provides data that a sync process need to store for its next run and must be made persistent.
 *
 * <p>
 * There is some data that is created in one sync run that must be made available to the next sync run, even if the JVM
 * has been restarted. Such data is used to speed-up synchronization. The kind of data is defined by
 * {@link ISyncProcessModel}. Eg: {@link ISyncProcessModel#getContinuationUuid()} will help to continue with a failed or
 * otherwise unexpectedly stopped synchronization process, because the assets that have already been synchronized
 * successfully are marked on the Smint.io server side. These can be skipped and only new assets need to be synced.
 * Especially with a vast amount of assets this will improve synchronization speed by magnitudes.
 * </p>
 *
 * <p>
 * Usually the same instance as passed to {@code #setSyncProcessModelAsync(ISyncProcessModel)} will be kept in memory
 * and returns with {@code #getSyncProcessModelAsync()}. However, it must be persisted to a storage system in order to
 * restore it once the JVM has been stopped and/or restarted. A base class
 * </p>
 */
public interface ISyncProcessProvider {

    /**
     * Returns the data that has previously been set by the sync process to be made persistent.
     *
     * @return a {@code Future} that may complete in an asynchronous way. Its value will return an
     *         {@link ISyncProcessModel} copy of data that has been previously been passed to
     *         {@link #setSyncProcessModelAsync(ISyncProcessModel)}. Implementing classes must not return {@code null}.
     */
    Future<ISyncProcessModel> getSyncProcessModelAsync();

    /**
     * Sets a new set of process data that need to be made persistent and made available to the next run.
     *
     * @return a {@code Future} that may complete in an asynchronous way. Its value will return an
     *         {@link ISyncProcessModel} instance. Implementing classes must not return {@code null}.
     */
    Future<ISyncProcessProvider> setSyncProcessModelAsync(final ISyncProcessModel newProcessData);
}
