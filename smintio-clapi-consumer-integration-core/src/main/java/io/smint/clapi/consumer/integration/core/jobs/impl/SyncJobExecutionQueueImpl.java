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

package io.smint.clapi.consumer.integration.core.jobs.impl;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

import io.smint.clapi.consumer.integration.core.jobs.ISyncJobExecutionQueue;


/**
 * Handles collision of execution of jobs to copy data from Smint.io platform to sync targets.
 *
 * <h2>Job runs</h2>
 * <p>
 * Jobs to copy from Smint.io to sync targets are not allowed to run simultaneously. It simple does not make sense. So
 * new events to trigger a new job run, add the new run to a waiting queue in case a job is currently running. More jobs
 * are just ignored, as the already waiting job will do the same task. Hence the waiting queue just consists of a single
 * slot.
 * </p>
 *
 * <p>
 * Because jobs triggered by receiving a push event from <a href="htts://www.pusher.com">Pusher.com</a> do not sync meta
 * data by default, scheduled jobs are not rejected in case the last item in the queue is a push event, but the queue
 * reserves a special, additional slot for scheduled jobs for that.
 * </p>
 */
@Singleton
public class SyncJobExecutionQueueImpl implements ISyncJobExecutionQueue {

    private static final int JOB_QUEUE_LENGTH = 1;
    private static final int JOB_QUEUE_EXTRA_SLOTS = 1;
    private static final int JOB_QUEUE_MAX_LENGTH = JOB_QUEUE_LENGTH + JOB_QUEUE_EXTRA_SLOTS;


    private final List<JobDescription> _jobWaitingQueue = new ArrayList<>();
    private final Object _runningJobSemaphore = new Object();
    private boolean _isRunning = false;

    @Override
    public SyncJobExecutionQueueImpl addJobForScheduleEvent(final Runnable job) {

        if (job == null) {
            return this;
        }

        synchronized (this._jobWaitingQueue) {
            if (this._jobWaitingQueue.size() >= JOB_QUEUE_MAX_LENGTH) {
                return this;

            } else if (this._jobWaitingQueue.size() == JOB_QUEUE_LENGTH) {

                // check for the last element to be a job triggered by push event
                final JobDescription lastJob = this._jobWaitingQueue.get(this._jobWaitingQueue.size() - 1);
                if (!lastJob.isPushEventJob()) {
                    return this;
                }
            }

            // add this job
            this._jobWaitingQueue.add(new JobDescription().setJob(job).setIsPushEventJob(false));
        }

        return this;
    }


    @Override
    public SyncJobExecutionQueueImpl addJobForPushEvent(final Runnable job) {

        if (job == null) {
            return this;
        }

        synchronized (this._jobWaitingQueue) {
            if (this._jobWaitingQueue.size() >= JOB_QUEUE_LENGTH) {
                return this;
            }

            this._jobWaitingQueue.add(new JobDescription().setJob(job).setIsPushEventJob(true));
        }

        return this;
    }


    @Override
    public ISyncJobExecutionQueue addJob(final boolean isPushEventJob, final Runnable job) {
        if (isPushEventJob) {
            return this.addJobForPushEvent(job);
        } else {
            return this.addJobForScheduleEvent(job);
        }
    }


    @Override
    public void run() {

        if (this.isRunning()) {
            return;
        }


        synchronized (this._runningJobSemaphore) {
            this._isRunning = true;
        }


        do {

            JobDescription nextJob = null;
            synchronized (this._jobWaitingQueue) {
                if (this._jobWaitingQueue.size() > 0) {
                    nextJob = this._jobWaitingQueue.remove(0);
                }
            }

            if (nextJob == null || nextJob.getJob() == null) {
                break;
            }


            try {
                nextJob.getJob().run();

            } finally {

                synchronized (nextJob) {
                    nextJob.notifyAll();
                }
            }
        } while (true);


        synchronized (this._runningJobSemaphore) {
            this._isRunning = false;
        }
    }


    @Override
    public boolean hasWaitingJob() {
        synchronized (this._jobWaitingQueue) {
            return !this._jobWaitingQueue.isEmpty();
        }
    }


    @Override
    public boolean isRunning() {
        synchronized (this._runningJobSemaphore) {
            return this._isRunning;
        }
    }


    private static class JobDescription {

        private Runnable _job = null;
        private boolean _isPushEventJob = false;

        public Runnable getJob() {
            return this._job;
        }

        public JobDescription setJob(final Runnable job) {
            this._job = job;
            return this;
        }

        public boolean isPushEventJob() {
            return this._isPushEventJob;
        }

        public JobDescription setIsPushEventJob(final boolean isPushEventJob) {
            this._isPushEventJob = isPushEventJob;
            return this;
        }
    }
}
