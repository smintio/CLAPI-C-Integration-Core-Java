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

package io.smint.clapi.consumer.integration.core.services;

/**
 * Implements a platform dependent scheduler to schedule a synchronization job or handle on-demand sync.
 *
 * <p>
 * The implementation must handle adding or removing a new schedule.
 * </p>
 */
public interface IPlatformScheduler {

    /**
     * Schedule a single job for immediate background execution.
     *
     * <p>
     * The job is started on a separate timer thread immediately, but only once.
     * </p>
     *
     * @param job the runnable to to execute. In case it is {@code null}, the schedule is ignored.
     * @return {@code this}.
     */
    IPlatformScheduler scheduleForImmediateExecution(final Runnable job);


    /**
     * Schedule a re-occurring job with a timer at a fixed timing rate.
     *
     * <p>
     * Starting from this very millisecond, the job is executed after every {@code period} milliseconds have passed.
     * {@code period} is also used as initial delay. So in case it is set to one hour (= {@code 3600000} milliseconds),
     * the job is first executed in an hour and then every hour afterwards.
     * </p>
     *
     * <p>
     * There is no check whether the same instance is added again.
     * </p>
     *
     * @param job    the runnable to to execute. In case it is {@code null}, the schedule is ignored.
     * @param period the period in milliseconds to execute the job.
     * @return a key to the scheduled job, that can be used to remove the schedule later with
     *         {@link #stopSchedule(String)}.
     */
    String scheduleAtFixedRate(final Runnable job, long period);


    /**
     * Stop the scheduled job, identified with a key.
     *
     * @param jobKey As returned by {@code #schedule(Runnable)}
     * @return {@code this} to support Fluent Interface.
     */
    IPlatformScheduler stopSchedule(final String jobKey);


    /**
     * Cancel all queued scheduled jobs and terminate the timer threads as soon as possible.
     *
     * <p>
     * The currently running job is not terminated. Only the queue is cleared and all schedules cancelled.
     * </p>
     *
     * @return {@code this} to support Fluent Interface.
     */
    IPlatformScheduler cancel();
}
