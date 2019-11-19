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

import java.util.Iterator;
import java.util.ServiceLoader;

import javax.inject.Singleton;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Provides;

import okhttp3.OkHttpClient;

import io.smint.clapi.consumer.integration.core.authenticator.ISmintIoAuthenticator;
import io.smint.clapi.consumer.integration.core.authenticator.impl.SmintIoAuthenticatorImpl;
import io.smint.clapi.consumer.integration.core.factory.ISmintIoSyncFactory;
import io.smint.clapi.consumer.integration.core.factory.ISyncTargetFactory;
import io.smint.clapi.consumer.integration.core.jobs.ISyncJob;
import io.smint.clapi.consumer.integration.core.jobs.ISyncJobExecutionQueue;
import io.smint.clapi.consumer.integration.core.jobs.impl.DefaultSyncJob;
import io.smint.clapi.consumer.integration.core.jobs.impl.SyncJobExecutionQueueImpl;
import io.smint.clapi.consumer.integration.core.providers.ISmintIoApiClient;
import io.smint.clapi.consumer.integration.core.providers.impl.SmintIoApiClientImpl;
import io.smint.clapi.consumer.integration.core.services.IPlatformScheduler;
import io.smint.clapi.consumer.integration.core.services.IPlatformSchedulerProvider;
import io.smint.clapi.consumer.integration.core.services.IPushNotificationService;
import io.smint.clapi.consumer.integration.core.services.impl.NativeThreadPoolScheduler;
import io.smint.clapi.consumer.integration.core.services.impl.PusherService;


/**
 * Configures a module for <a href="github.com/google/guice">Google's Guice</a> dependency injector.
 */
public class SyncGuiceModule extends AbstractModule {

    private final ISyncTargetFactory _syncTargetFactory;
    private IPlatformScheduler _scheduler;
    private OkHttpClient _httpClient;


    /**
     * Creates a new Google's Guice configuration module.
     *
     * @param syncTargetFactory the custom sync target factory.
     */
    public SyncGuiceModule(final ISyncTargetFactory syncTargetFactory) {
        this._syncTargetFactory = syncTargetFactory;
    }


    /**
     * Create a new Google's Guice injector that makes use of the targets {@link ISyncTargetFactory}.
     *
     * @param syncTargetFactory the custom sync target factory.
     * @return a new injector - never {@code null}
     */
    public static ISmintIoSyncFactory createSmintIoSyncFactory(final ISyncTargetFactory syncTargetFactory) {
        return Guice.createInjector(
            new SyncGuiceModule(syncTargetFactory)
        ).getInstance(ISmintIoSyncFactory.class);
    }


    /**
     * Returns the value passed to the constructor.
     *
     * @return a sync factory or {@code null}.
     */
    @Provides
    public ISyncTargetFactory getSyncTargetFactory() {
        return this._syncTargetFactory;
    }


    /**
     * Returns a singleton {@link OkHttpClient} instance.
     *
     * @return a {@link OkHttpClient} or {@code null}.
     */
    @Provides
    public OkHttpClient getHttpClient() {
        if (this._httpClient == null) {
            this._httpClient = new OkHttpClient.Builder().build();
        }
        return this._httpClient;
    }


    /**
     * Provide a platform dependent scheduler.
     *
     * <p>
     * First a service implementing {@link IPlatformSchedulerProvider} is detected using {@link ServiceLoader}. If there
     * is none, then the default Java scheduler {@link NativeThreadPoolScheduler} is used.<br>
     * If there are more implementation for this service, the first one is used. It depends on the class loader to
     * determine the order of the implementation. So in reality it can not be determined and maybe totally random.
     * However, this does not seem to be a problem as there should only be a single service implementation available -
     * one for each platform.
     * </p>
     *
     * <p>
     * The scheduler is a singleton and the same instance is returned for consecutive calls.
     * </p>
     *
     * @return the first service found or {@code null} if there is none.
     */
    @Singleton
    @Provides
    public IPlatformScheduler getPlatformSchedulerProvider() {

        if (this._scheduler == null) {
            final ServiceLoader<IPlatformSchedulerProvider> loader = ServiceLoader
                .load(IPlatformSchedulerProvider.class);
            final Iterator<IPlatformSchedulerProvider> allProvidersIterator = loader != null ? loader.iterator() : null;

            final ISyncTargetFactory factory = this.getSyncTargetFactory();
            this._scheduler = allProvidersIterator != null && allProvidersIterator.hasNext()
                ? allProvidersIterator.next().setSettings(factory != null ? factory.getSettings() : null).get()
                : null;


            if (this._scheduler == null) {
                this._scheduler = new NativeThreadPoolScheduler();
            }
        }


        return this._scheduler;
    }


    @Override
    protected void configure() {
        super.configure();

        this.bind(ISyncJob.class).to(DefaultSyncJob.class);
        this.bind(ISmintIoSyncFactory.class).to(SmintIoSyncFactoryFromDI.class).in(Singleton.class);
        this.bind(IPushNotificationService.class).to(PusherService.class).in(Singleton.class);
        this.bind(ISmintIoAuthenticator.class).to(SmintIoAuthenticatorImpl.class).in(Singleton.class);
        this.bind(ISmintIoApiClient.class).to(SmintIoApiClientImpl.class);
        this.bind(ISyncJobExecutionQueue.class).to(SyncJobExecutionQueueImpl.class).in(Singleton.class);
    }
}
