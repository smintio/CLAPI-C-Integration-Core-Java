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

import io.smint.clapi.consumer.integration.core.factory.ISmintIoSyncFactory;
import io.smint.clapi.consumer.integration.core.factory.ISyncTargetFactory;
import io.smint.clapi.consumer.integration.core.jobs.ISyncJob;
import io.smint.clapi.consumer.integration.core.services.IPlatformScheduler;
import io.smint.clapi.consumer.integration.core.services.IPushNotificationService;


/**
 * A factory to provide all necessary instances to the sync process fetching them from an available DI.
 *
 * <p>
 * This class can be used with Java dependency injection to provide the required classes. Any dependency injection
 * available in the implementation application is sufficient. Just add proper configuration to your DI. The default
 * dependency injection framework used, if there is none available, is
 * <a href="https://github.com/google/guice">Google's Guice</a>.
 * </p>
 */
@Singleton
public class SmintIoSyncFactoryFromDI implements ISmintIoSyncFactory {


    private final ISyncTargetFactory _syncTargetFactory;
    private final Provider<ISyncJob> _jobProvider;
    private final IPlatformScheduler _scheduler;
    private final IPushNotificationService _notificationService;

    @Inject
    public SmintIoSyncFactoryFromDI(
        final ISyncTargetFactory syncTargetFactory,
        final Provider<ISyncJob> jobProvider,
        final IPlatformScheduler platformScheduler,
        final IPushNotificationService notificationService
    ) {
        this._syncTargetFactory = syncTargetFactory;
        this._jobProvider = jobProvider;
        this._scheduler = platformScheduler;
        this._notificationService = notificationService;
    }


    @Override
    public ISyncJob createSyncJob() {
        return this._jobProvider.get();
    }


    @Override
    public ISyncTargetFactory getSyncTargetFactory() {
        return this._syncTargetFactory;
    }


    @Override
    public IPlatformScheduler getPlatformScheduler() {
        return this._scheduler;
    }


    @Override
    public IPushNotificationService getNotificationService() {
        return this._notificationService;
    }

}
