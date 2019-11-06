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

package io.smint.clapi.consumer.integration.core;

import javax.inject.Provider;

import io.smint.clapi.consumer.integration.core.configuration.models.ISettingsModel;


/**
 * Implements a service to provide a platform dependent scheduler of type {@link IPlatformScheduler}.
 *
 * <p>
 * Schedulers depend on the runtime platform, yet they might need some configuration data for initialization. So it is
 * not feasible to use {@link java.util.ServiceLoader} directly with the scheduler, but an intermediate service class is
 * used. This service class will create a scheduler for the current runtime platform and pass the required configuration
 * data to the constructor.
 * </p>
 */
public interface IPlatformSchedulerProvider extends Provider<IPlatformScheduler> {

    /**
     * Creates a new platform scheduler or reuses the previously created one.
     *
     * @return A new or reused scheduler or {@code null} in case none can be created.
     * @throws IllegalStateException if settings data is required but no data has been set via
     *                               {@link #setSettings(ISettingsModel)}
     */
    @Override
    IPlatformScheduler get() throws IllegalStateException;


    /**
     * Sets initialization data for creating new schedulers.
     *
     * @return {@code this} for Fluent Interface
     */
    IPlatformSchedulerProvider setSettings(final ISettingsModel settings);


    /**
     * Sets initialization data for creating new schedulers.
     *
     * @return the currently settings as set by {@link #setSettings(ISettingsModel)}
     */
    ISettingsModel getSettings();
}
