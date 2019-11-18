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
import io.smint.clapi.consumer.integration.core.factory.impl.SyncGuiceModule;
import io.smint.clapi.consumer.integration.core.jobs.ISyncJob;
import io.smint.clapi.consumer.integration.core.jobs.ISyncJobExecutionQueue;
import io.smint.clapi.consumer.integration.core.services.IPlatformScheduler;
import io.smint.clapi.consumer.integration.core.services.IPushNotificationService;


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
 *
 * <h2>Dependency Injection</h2>
 * <p>
 * Either this class is created using any dependency injection, or <a href="https://github.com/google/guice"> Google's
 * Guice</a> is used internally. The implementation tries to avoid to apply its own injector if there already is some in
 * place. Only if the required instance of {@link ISmintIoSyncFactory} is not injected via Field Injection, Guice is to
 * create these instances.
 * </p>
 *
 * <h2>Synchronizing job runs</h2>
 * <p>
 * Synchronization jobs are not allowed to run simultaneously. It simple does not make sense. So new events to trigger a
 * new job run add the new run to a waiting queue in case a job is currently running. Hence an instance of
 * {@link ISyncJobExecutionQueue} is used to detect and handle collisions.
 * </p>
 */
public class SmintIoSynchronization implements ISmintIoSynchronization {

    /**
     * The rate (period) to execute the standard synchronization job, including meta-data synchronization.
     */
    public static final long JOB_SCHEDULE_PERIOD_MILLISEC = 3600000L;


    private static final Logger LOG = Logger.getLogger(SmintIoSynchronization.class.getName());


    private ISmintIoSyncFactory _factory;

    private String _scheduledJobKey;
    private IPlatformScheduler _scheduler;
    private ISyncJobExecutionQueue _executionQueue;

    /**
     * Create a new Smint.io synchronization progress.
     *
     * <p>
     * {@link #init(ISyncTargetFactory)} will try to load Google's Guice as Dependency Injection framework in order to
     * create an instance of {@link ISmintIoSyncFactory}.
     * </p>
     *
     * @param syncTargetFactory the user factory helping to create all target specific instances.
     */
    @Inject
    public SmintIoSynchronization(final ISyncTargetFactory syncTargetFactory) {
        this.init(syncTargetFactory);
        this._scheduledJobKey = null;
    }


    /**
     * Return the used factory.
     *
     * @return the currently used factory.
     */
    public ISyncTargetFactory getFactory() {
        return this._factory.getSyncTargetFactory();
    }


    @Override
    public void start() {

        if (this._scheduledJobKey == null) {
            this._scheduledJobKey = this._scheduler.scheduleAtFixedRate(
                this.createNewSynchonizedJob(true), JOB_SCHEDULE_PERIOD_MILLISEC
            );

            final IPushNotificationService pushService = this._factory.getNotificationService();
            if (pushService != null) {
                pushService.startNotificationService(this.createNewSynchonizedJob(false));
            }
        }
    }


    @Override
    public void stop() {
        if (this._scheduledJobKey != null) {

            final IPushNotificationService pushService = this._factory.getNotificationService();
            if (pushService != null) {
                pushService.stopNotificationService();
            }

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
     * An instance of <a href="https://github.com/google/guice"> Google's Guice</a> is created as Dependency Injection
     * system and used to create this required instance of {@link ISmintIoSyncFactory}. The Guice injector is created by
     * calling {@link SyncGuiceModule#createSmintIoSyncFactory(ISyncTargetFactory)}.
     * </p>
     *
     * @param syncTargetFactory sync target provided factory to use with this library.
     * @return the first service found or {@code null} if there is none.
     * @throws NullPointerException in case the created factory is {@code null} or any of its factory-function return
     *                              {@code null}.
     */
    public SmintIoSynchronization init(final ISyncTargetFactory syncTargetFactory) {

        Objects.requireNonNull(syncTargetFactory, "The provided sync target factory is null!");
        this._factory = SyncGuiceModule.createSmintIoSyncFactory(syncTargetFactory);

        Objects.requireNonNull(this._factory, "No synchronization factory available!");
        Objects.requireNonNull(this._factory.getSyncTargetFactory(), "Failed to acquire sync target factory!");
        Objects.requireNonNull(this._factory.createSyncJob(), "Failed to create a sync target job!");

        this._scheduler = this._factory.getPlatformScheduler();
        Objects.requireNonNull(this._scheduler, "Failed to get platform dependent scheduler from factory!");

        this._executionQueue = this._factory.getJobExecutionQueue();
        Objects.requireNonNull(this._executionQueue, "Failed to get job execution queue from factory!");

        return this;
    }


    private Runnable createNewJob(final boolean syncMetadata) {

        final ISyncJob job = this._factory.createSyncJob();
        return () -> {
            try {
                job.synchronize(syncMetadata);
            } catch (final SmintIoAuthenticatorException | SmintIoSyncJobException excp) {
                LOG.log(Level.SEVERE, "Failed to execute synchronization job with Smint.io platform!", excp);
            }
        };
    }


    private Runnable createNewSynchonizedJob(final boolean syncMetadata) {

        final boolean isPushEventJob = !syncMetadata;
        final Runnable checkedJob = this.createNewJob(syncMetadata);

        // first add the job to the queue, then execute the next item in the queue if any is waiting.
        return () -> {
            this._executionQueue.addJob(isPushEventJob, checkedJob);
            this._executionQueue.run();
        };
    }
}
