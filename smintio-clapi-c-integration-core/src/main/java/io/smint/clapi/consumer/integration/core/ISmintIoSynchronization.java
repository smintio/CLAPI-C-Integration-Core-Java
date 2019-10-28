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

import java.util.concurrent.Future;


/**
 * Provides methods to handle the sync process from assets at Smint.io to a downstream synchronization target.
 */
public interface ISmintIoSynchronization {

    /**
     * Start a scheduled synchronization, register with channel listener and store a reference with the scheduler.
     *
     * <p>
     * The scheduling is maintained by the implementing class. It will schedule only one process, no matter how often
     * this method is executed. Only a new schedule is created but no sync process started.
     * </p>
     *
     * <p>
     * Along the scheduled job, the job is registered with the the <a href="https://pusher.com/docs/channels">channel
     * API</a> of <a href="https://www.pusher.com">Pusher.com</a> to receive push notification in case of any purchase.
     * </p>
     *
     * <p>
     * Beware: a reference to this instance is stored with the scheduler. So as long as there is an active schedule
     * garbage collection won't apply.
     * </p>
     */
    void start();


    /**
     * Stop the scheduled synchronization process.
     *
     * <p>
     * Only the schedule is stopped (removed) and the <a href="https://pusher.com/docs/channels">channel API</a> of
     * <a href="https://www.pusher.com">Pusher.com</a> is disconnected. Any currently running synchronization process is
     * not terminated.
     * </p>
     */
    void stop();


    /**
     * Perform an on-demand synchronization without syncing any asset meta data.
     *
     * @return a {@code Future} that will return {@code null} in its {@link Future#get()} function.
     */
    Future<Void> doSync();


    /**
     * Perform an on-demand synchronization without or without syncing any asset meta data.
     *
     * @param syncMetaData pass {@code true} to force synchronization of meta data. If you pass {@code false} then no
     *                     meta data will be synchronized. This is useful for non-scheduled synchronizations.
     * @return a {@code Future} that will return {@code null} in its {@link Future#get()} function.
     */
    Future<Void> doSync(final boolean syncMetaData);
}
