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
 *
 * <h2>Two kinds of events for initiating a synchronization: <em>scheduled</em> + <em>on demand pusher</em></h2>
 * <p>
 * Synchronization of purchased assets are performed on a regular/scheduled basis in the background. A timer is used to
 * start this task. Additional, synchronization can also started on demand, as soon as any new asset has been bought on
 * Smint.io. Hence the scheduled process does not need to run every minute but can be scheduled utilizing a wider
 * interval. In order to speed-up on-demand sync, the structured meta data usually is not synchronized at this step. It
 * can be assumed that syncing meta data in the scheduled process only is sufficient. So an initial synchronization is
 * required, usually performed during development time of any implementing instance.
 * </p>
 *
 * <ul>
 * <li><em>scheduled sync</em> &mdash; automatic synchronization task in the background synchronizing all data</li>
 * <li><em>on demand pusher</em> &mdash; manually/programmatically started task to synchronize binary assets only. Such
 * an on-demand event will be triggered by the Smint.io platform utilizing channel-services of
 * <a href="https://www.pusher.com">Pusher.com</a></li>
 * </ul>
 *
 * <h2>Registering with <a href="https://www.pusher.com">Pusher.com</a></h2>
 * <p>
 * To receive notifications for on-demand synchronization in case of any purchase, the synchronization job registered
 * itself to the <a href="https://pusher.com/docs/channels">channel API</a> of
 * <a href="https://www.pusher.com">Pusher.com</a>. In general, a web socket is opened to something like
 * {@code https://ws-*.pusher.com}. The web socket is used to exchange notification. In order to make that work, the
 * open socket must be kept available and a reference to it maintained. Hence the subscription to the channel is started
 * along the scheduled job in order to ensure a reference to {@code ISmintIoSynchronization} is available with its
 * internal reference to the channel socket.
 * </p>
 *
 * @see io.smint.clapi.consumer.integration.core
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
    ISmintIoSynchronization start();


    /**
     * Stop the scheduled synchronization process.
     *
     * <p>
     * Only the schedule is stopped (removed) and the <a href="https://pusher.com/docs/channels">channel API</a> of
     * <a href="https://www.pusher.com">Pusher.com</a> is disconnected. Any currently running synchronization process is
     * not terminated.
     * </p>
     */
    ISmintIoSynchronization stop();


    /**
     * Perform an initial synchronization of all meta data and assets.
     *
     * <p>
     * A new synchronization task will be executed. In case such a task is already being executed, no new one will be
     * created but the returned {@code Future} is going to wait for the termination of the existing task. Beware that
     * every {@link Future} will require the task to run in a different thread.
     * </p>
     *
     * @param waitForTermination if {@code true} the sync job is executed in the current thread and the function will
     *                           return as soon as the task has terminated. There is no need to keep a reference to the
     *                           result of this function in that case.
     * @return a {@code Future} that will return {@code null} in its {@link Future#get()} function, once synchronization
     *         job has finished.
     */
    Future<Void> initialSync(boolean waitForTermination);
}
