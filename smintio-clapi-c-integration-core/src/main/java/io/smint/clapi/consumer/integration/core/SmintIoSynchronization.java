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

import java.util.Objects;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;

import io.smint.clapi.consumer.integration.core.exceptions.SmintIoAuthenticatorException;
import io.smint.clapi.consumer.integration.core.exceptions.SmintIoSyncJobException;
import io.smint.clapi.consumer.integration.core.factory.ISmintIoSyncFactory;
import io.smint.clapi.consumer.integration.core.factory.ISyncTargetFactory;
import io.smint.clapi.consumer.integration.core.jobs.ISyncJob;


/**
 * Provides initializing functions for the Smint.io synchronization process and handles the synchronization.
 *
 * <h2>Constant rate schedule</h2>
 * <p>
 * This class will manage creating a synchronization job and will schedule it in function {@link #start()}. The schedule
 * is set to constant rate of every 60 minutes. However, in case synchronization takes longer, the next scheduled task
 * is put into a waiting queue. The waiting queue consists of a single slot only. So any additional tasks to execute
 * because of the schedule is ignored and discarded. The still waiting task will handle its sync assets, too.
 * </p>
 */
public class SmintIoSynchronization implements ISmintIoSynchronization {

    /**
     * The rate (period) to execute the standard synchronization job, including meta-data synchronization.
     */
    public final static long JOB_SCHEDULE_PERIOD_MILLISEC = 3600000L;


    private final static Logger LOG = Logger.getLogger(SmintIoSynchronization.class.getName());


    private ISmintIoSyncFactory _factory;
    private final ISyncTargetFactory _syncTargetfactory;

    private String _scheduledJobKey;
    private IPlatformScheduler _scheduler;


    /**
     * Create a new Smint.io synchronization progress.
     *
     * <p>
     * {@link #init()} will try to load Google's Guice as Dependency Injection framework in order to create an instance
     * of {@link ISmintIoSyncFactory}.
     * </p>
     *
     * @param syncTargetfactory the user factory helping to create all target specific instances.
     */
    public SmintIoSynchronization(final ISyncTargetFactory syncTargetfactory) throws Exception {

        Objects.requireNonNull(syncTargetfactory, "The provided sync target factory is null!");

        this._syncTargetfactory = syncTargetfactory;
        this._scheduledJobKey = null;
        this.init();
    }


    /**
     * Create a new Smint.io synchronization progress and injects required factory.
     *
     * <p>
     * This should be used by external dependency injection system. In case none is in place,
     * {@link #SmintIoSynchronization(ISyncTargetFactory)} might be more convenient.
     * </p>
     *
     * <p>
     * In case the provided factory is {@code null}, then {@link #init()} will try to load Google's Guice as Dependency
     * Injection framework.
     * </p>
     *
     * @param factory the Smint.io synchronization factory.
     */
    @Inject
    public SmintIoSynchronization(final ISmintIoSyncFactory factory) throws Exception {

        Objects.requireNonNull(factory, "Dependency Injector did not provide a valid factory!");
        this._factory = factory;

        this._syncTargetfactory = null;
        this._scheduledJobKey = null;
        this.init();
    }


    /**
     * Return the used factory.
     *
     * @return the currently used factory.
     */
    public ISyncTargetFactory getFactory() {
        return this._syncTargetfactory != null ? this._syncTargetfactory : this._factory.getSyncTargetFactory();
    }


    @Override
    public void start() {

        if (this._scheduledJobKey == null) {
            this._scheduledJobKey = this._scheduler.scheduleAtFixedRate(
                this.createNewJob(true), JOB_SCHEDULE_PERIOD_MILLISEC
            );
        }
    }


    @Override
    public void stop() {
        if (this._scheduledJobKey != null) {
            this._scheduler.stopSchedule(this._scheduledJobKey);
            this._scheduledJobKey = null;
        }
    }


    @Override
    public Future<Void> doSync() {
        return this.doSync(false);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Future<Void> doSync(final boolean syncMetaData) {

        final Runnable syncJob = this.createNewJob(false);

        final FutureTask<Void> task = new FutureTask<>(syncJob, null);
        final Thread workerThread = new Thread(task);
        workerThread.start();

        return task;
    }


    /**
     * Initializes the Smint.io platform synchronization.
     *
     * <p>
     * Checks whether an instance of {@link ISmintIoSyncFactory} has been properly injected into the private field by
     * any injection system. An external dependency injection may be in place, to fulfill this task.
     * </p>
     *
     * @return the first service found or {@code null} if there is none.
     * @throws NullPointerException
     */
    public SmintIoSynchronization init() {

        Objects.requireNonNull(this._factory, "No synchronization factory available!");
        Objects.requireNonNull(this._factory.getSyncTargetFactory(), "Failed to acquire sync target factory!");
        Objects.requireNonNull(this._factory.createSyncJob(), "Failed to create a sync target job!");

        this._scheduler = this._factory.getPlatformScheduler();
        Objects.requireNonNull(this._scheduler, "Failed to get platform dependent scheduler from factory!");

        return this;
    }


    private Runnable createNewJob(final boolean syncMetadata) {

        final ISyncJob job = this._factory.createSyncJob();
        return () -> {
            try {
                job.synchronize(syncMetadata);
            } catch (SmintIoAuthenticatorException | SmintIoSyncJobException excp) {
                LOG.log(Level.SEVERE, "Failed to execute synchronization job with Smint.io platform!", excp);
            }
        };
    }
}
