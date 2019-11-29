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
import javax.inject.Singleton;

import io.smint.clapi.consumer.integration.core.configuration.IAuthTokenStorage;
import io.smint.clapi.consumer.integration.core.configuration.ISyncJobDataStorage;
import io.smint.clapi.consumer.integration.core.configuration.models.ISettingsModel;
import io.smint.clapi.consumer.integration.core.factory.ISyncTargetFactory;
import io.smint.clapi.consumer.integration.core.target.ISyncTarget;


/**
 * A factory to hold all synchronization target specific implementation classes as pre-created instances.
 *
 * <p>
 * This class can be used with Java dependency injection to provide the required classes. Any dependency injection
 * available in the implementation application is sufficient. Just add proper configuration to your DI. Nevertheless an
 * instance does not need to be created utilizing dependency injection. It can be used as is and all necessary instances
 * passed to the constructor.
 * </p>
 *
 * <h2>Example without any dependency injection</h2>
 * <p>
 * Even without any dependency injection available, it is easy to create an instance holding all necessary pre-created
 * instances. The instance for {@link ISyncTarget} is created by a lambda function to ensure a new instance is created
 * all the time.
 * </p>
 *
 * <pre>
 * final ISyncFactory syncFactory = new SyncFactoryFromDI(
 *     new MyAuthTokenProviderImplementation(),
 *     new MySettingsProviderImplementation(),
 *     () -&gt; new MySyncTargetImplementation()
 * );
 * </pre>
 */
@Singleton
public class SyncTargetFactoryFromDI implements ISyncTargetFactory {

    private final IAuthTokenStorage _authTokenProvider;
    private final ISyncJobDataStorage _jobDataStorage;
    private final Provider<ISettingsModel> _settingsProvider;
    private final Provider<ISyncTarget> _syncTargetProvider;


    /**
     * create a new sync factory with pre-created values.
     *
     * @param authTokenProvider  already created and available authentication token provider
     * @param settings           already available settings provider
     * @param syncTargetProvider a sync target provider
     */
    @Inject
    public SyncTargetFactoryFromDI(
        final IAuthTokenStorage authTokenProvider,
        final Provider<ISettingsModel> settings,
        final Provider<ISyncTarget> syncTargetProvider
    ) {
        this(authTokenProvider, settings, syncTargetProvider, null);
    }


    /**
     * create a new sync factory with pre-created values.
     *
     * @param authTokenProvider  already created and available authentication token provider
     * @param settings           already available settings provider
     * @param syncTargetProvider a sync target provider
     * @param syncJobDataStorage a storage to read from and write job data to
     */
    @Inject
    public SyncTargetFactoryFromDI(
        final IAuthTokenStorage authTokenProvider,
        final Provider<ISettingsModel> settings,
        final Provider<ISyncTarget> syncTargetProvider,
        final ISyncJobDataStorage syncJobDataStorage
    ) {
        this._authTokenProvider = authTokenProvider;
        this._settingsProvider = settings;
        this._syncTargetProvider = syncTargetProvider;
        this._jobDataStorage = syncJobDataStorage;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public ISyncTarget createSyncTarget() {
        return this._syncTargetProvider.get();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public ISettingsModel getSettings() {
        return this._settingsProvider.get();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public IAuthTokenStorage getAuthTokenStorage() {
        return this._authTokenProvider;
    }


    @Override
    public ISyncJobDataStorage getJobDataStorage() {
        return this._jobDataStorage;
    }
}
