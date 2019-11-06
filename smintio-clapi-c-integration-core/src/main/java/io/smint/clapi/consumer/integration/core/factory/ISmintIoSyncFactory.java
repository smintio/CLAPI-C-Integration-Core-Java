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

import io.smint.clapi.consumer.integration.core.IPlatformScheduler;
import io.smint.clapi.consumer.integration.core.jobs.ISyncJob;


/**
 * A factory that will create all necessary instances, necessary for the synchronization jobs.
 *
 * <p>
 * This factory is used by {@link io.smint.clapi.consumer.integration.core.SmintIoSynchronization} to drive the setup of
 * the synchronization job. So {@code SmintIoSynchronization} is responsible for creating such a factory. Hence it will
 * hand over the instance of {@link ISyncTargetFactory} as provided by the consumer of the library.
 * </p>
 */
public interface ISmintIoSyncFactory {

    /**
     * Creates an instance of the synchronization job, probably not a singleton.
     *
     * @return returns a new sync target on each call.
     */
    ISyncJob createSyncJob();


    /**
     * Provide the synchronization target factory that has been passed to
     * {@link io.smint.clapi.consumer.integration.core.SmintIoSynchronization}.
     *
     * <p>
     * since the settings hardly change, the same provider, delivering the same settings all the time seems to be
     * advisable.
     * </p>
     *
     * @return returns the same settings on each request.
     */
    ISyncTargetFactory getSyncTargetFactory();


    /**
     * Detects and creates the scheduler for the current runtime platform.
     *
     * @return the scheduler but never {@code null}
     */
    IPlatformScheduler getPlatformScheduler();
}
