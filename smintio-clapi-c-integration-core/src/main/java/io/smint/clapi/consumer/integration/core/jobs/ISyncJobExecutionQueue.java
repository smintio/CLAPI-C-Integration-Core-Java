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

package io.smint.clapi.consumer.integration.core.jobs;

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
public interface ISyncJobExecutionQueue extends Runnable {

    /**
     * Adds a scheduled job or put it in the waiting queue if a slot is available.
     *
     * <p>
     * If another scheduled job is already waiting, this job is being silently ignored and no action is taken. If just a
     * <em>push-event-job</em> is waiting, this job will be added as a second waiting job to a special slot.
     * </p>
     *
     * @param job the job to run.
     * @return {@code this}
     */
    ISyncJobExecutionQueue addJobForScheduleEvent(final Runnable job);


    /**
     * Adds a job because of a push event or add it to the waiting queue.
     *
     * <p>
     * If another job is already waiting, this job is being silently ignored and no action is taken.
     * </p>
     *
     * @param job the job to run.
     * @return {@code this}
     */
    ISyncJobExecutionQueue addJobForPushEvent(final Runnable job);


    /**
     * Adds a job.
     *
     * <p>
     * Calls {@link #addJobForPushEvent(Runnable)} if parameter {@code isPushEventJob} is {@code true} and calls
     * {@link #addJobForScheduleEvent(Runnable)} otherwise.
     * </p>
     *
     * @param isPushEventJob whether this is job, that has been triggered by a push event, or not.
     * @param job            the job to run.
     * @return {@code this}
     */
    ISyncJobExecutionQueue addJob(final boolean isPushEventJob, final Runnable job);


    /**
     * Execute the next waiting job on the same thread if no other job is running.
     *
     * <p>
     * The function returns immediately without executing anything, if a job is already running.
     * </p>
     * {@inheritDoc}
     */
    @Override
    void run();


    /**
     * Checks whether a job has already been added to the waiting queue.
     *
     * <p>
     * The call is non-blocking.
     * </p>
     *
     * @return {@code true} if at least one job is waiting or {@code false}.
     */
    boolean hasWaitingJob();


    /**
     * Checks whether a job is currently being executed.
     *
     * <p>
     * The call is non-blocking.
     * </p>
     *
     * @return {@code true} if at least one job is waiting or {@code false}.
     */
    boolean isRunning();


    /**
     * Waits for the current running job to terminate.
     *
     * <p>
     * The call is blocking. If no job is currently running, the function will return immediately.
     * </p>
     *
     * @return {@code this}.
     * @throws InterruptedException see {@link java.lang.Object#wait()}
     */
    ISyncJobExecutionQueue waitForJob() throws InterruptedException;
}
