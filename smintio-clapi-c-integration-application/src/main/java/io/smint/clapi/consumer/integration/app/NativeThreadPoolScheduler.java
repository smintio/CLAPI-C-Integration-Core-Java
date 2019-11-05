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

package io.smint.clapi.consumer.integration.app;

import java.nio.charset.StandardCharsets;
import java.util.Hashtable;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Singleton;

import io.smint.clapi.consumer.integration.core.IPlatformScheduler;


// CHECKSTYLE.OFF: MultipleStringLiterals

/**
 * Implements a Java {@link ScheduledThreadPoolExecutor} based scheduler to schedule a synchronization job.
 *
 * <p>
 * for every job, an independent, new event creating {@link Runnable} is added to the queue. This new {@code Runnable}
 * does create a new {@link java.lang.Thread} and makes it execute the scheduled job. So for every job execution, a new
 * thread is created. This helps to avoid blocking the timer queue by long running threads. A crashing thread will not
 * affect the timer, too.
 * </p>
 */
@Singleton
public class NativeThreadPoolScheduler implements IPlatformScheduler {

    private static final Logger LOG = Logger.getLogger(NativeThreadPoolScheduler.class.getName());
    private static final int KEY_CHARACTER_LEN = 20;

    private ScheduledThreadPoolExecutor _executor;
    private final Map<String, ScheduledFuture<?>> _scheduledJobs = new Hashtable<>();

    @Override
    public String scheduleAtFixedRate(final Runnable job, final long period) {
        LOG.entering(this.getClass().getName(), "scheduleAtFixedRate", new Object[] { job, new Long(period) });

        if (this._executor == null) {
            this._executor = new ScheduledThreadPoolExecutor(2);
        }

        Objects.requireNonNull(this._executor, "Failed to create a scheduled executor pool!");
        this._executor.setRemoveOnCancelPolicy(true);


        // create a key for the job
        String key = this.createRandomKey();
        // CHECKSTYLE.OFF: MagicNumber
        int max = 1000;
        // CHECKSTYLE.ON: MagicNumber
        while (--max > 0 && this._scheduledJobs.containsKey(key)) {

            LOG.finer("New job key >>" + key + "<< is not unique - trying again (" + max + " attempts left)!");
            key = this.createRandomKey();
        }
        final String jobKey = key;

        if (this._scheduledJobs.containsKey(jobKey)) {
            final IllegalStateException excp = new IllegalStateException(
                "Failed to create a unique key to add the job to the queue!"
            );

            LOG.finer(() -> "New job key >>" + jobKey + "<< is not unique - NOT trying again!");
            LOG.throwing(this.getClass().getName(), "scheduleAtFixedRate", excp);
            throw excp;
        }

        LOG.finer(() -> "Scheduling a new job with key >>" + jobKey + "<<.");
        final ScheduledFuture<?> scheduledJob = this._executor
            .scheduleAtFixedRate(
                () -> {
                    // CHECKSTYLE.OFF: IllegalCatch
                    try {
                        LOG.finer(() -> "Executing job with key >>" + jobKey + "<< in new thread.");
                        new Thread(job).start();
                    } catch (final Exception ignore) {
                        LOG.log(Level.SEVERE, "Executing a timed job has failed!", ignore);
                    }
                    // CHECKSTYLE.ON: IllegalCatch
                },
                period,
                period,
                TimeUnit.MILLISECONDS
            );

        this._scheduledJobs.put(jobKey, scheduledJob);

        LOG.exiting(this.getClass().getName(), "scheduleAtFixedRate", jobKey);
        return jobKey;
    }


    @Override
    public IPlatformScheduler stopSchedule(final String jobKey) {
        LOG.entering(this.getClass().getName(), "stopSchedule", new Object[] { jobKey });

        if (this._executor != null && jobKey != null && !jobKey.isEmpty()) {

            final ScheduledFuture<?> scheduledJob = this._scheduledJobs.get(jobKey);
            if (scheduledJob != null) {

                LOG.finer(() -> "Stopping job with key >>" + jobKey + "<<.");
                scheduledJob.cancel(false);
                this._scheduledJobs.remove(jobKey);

            } else {
                LOG.warning(() -> "No job with key >>" + jobKey + "<< has been found.");
            }

        } else if (this._executor == null) {
            LOG.warning(() -> "No job executor has been initialized so no job can be stopped.");

        } else {
            LOG.finer(() -> "Job key to find job to stop is invalid: " + jobKey);
        }

        LOG.exiting(this.getClass().getName(), "stopSchedule", this);
        return this;
    }


    @Override
    public IPlatformScheduler cancel() {
        LOG.entering(this.getClass().getName(), "cancel");

        if (this._executor != null) {
            LOG.finer(() -> "Shutting down thread pool executer for timer!");
            this._executor.shutdownNow();
            this._executor = null;
        }

        LOG.exiting(this.getClass().getName(), "cancel", this);
        return this;
    }


    private String createRandomKey() {
        final byte[] array = new byte[KEY_CHARACTER_LEN];
        new Random().nextBytes(array);
        final String key = new String(array, StandardCharsets.UTF_8);

        LOG.finer(() -> "Created new job key >>" + key + "<< - test for uniqueness is pending!");

        return key;
    }
}

// CHECKSTYLE.ON: MultipleStringLiterals
