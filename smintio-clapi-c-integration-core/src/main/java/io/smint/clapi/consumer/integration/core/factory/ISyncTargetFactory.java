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

package io.smint.clapi.consumer.integration.core.factory;

import io.smint.clapi.consumer.integration.core.configuration.IAuthTokenStorage;
import io.smint.clapi.consumer.integration.core.configuration.models.ISettingsModel;
import io.smint.clapi.consumer.integration.core.target.ISyncTarget;


/**
 * A factory to create all synchronization target specific implementation classes.
 *
 * <p>
 * In order to avoid depending on specific implementations of any dependency frameworks, implementing classes provide
 * factory functions to create all necessary instances.
 * </p>
 */
public interface ISyncTargetFactory {

    /**
     * creates an instance of the synchronization target instance.
     *
     * <p>
     * Since the synchronization target is a heavy class with a lot of cache data, it seems better to create a new
     * instance on each call. However, the implementor of the sync target need to decide whether it is necessary to
     * create a new instance on each call or not. At the moment the default implementation of
     * {@link io.smint.clapi.consumer.integration.core.jobs.ISyncJob}, which is
     * {@link io.smint.clapi.consumer.integration.core.jobs.impl.DefaultSyncJob}, reuses the target for all
     * synchronization processes as long as the job is being scheduled. As soon as the job is being rescheduled, a new
     * synchronization target will be created.
     * </p>
     *
     * @return returns a new sync target on each call.
     */
    ISyncTarget createSyncTarget();


    /**
     * creates a settings provider to deliver the settings for the synchronization process.
     *
     * <p>
     * since the settings hardly change, the same provider, delivering the same settings all the time seems to be
     * advisable.
     * </p>
     *
     * @return returns the same settings on each request.
     */
    ISettingsModel getSettings();


    /**
     * creates a token storage to deliver the settings for authentication to the Smint.IO RESTful API.
     *
     * @return returns the same authentication token storage on each request.
     */
    IAuthTokenStorage getAuthTokenStorage();

}