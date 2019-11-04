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

import io.smint.clapi.consumer.integration.core.exceptions.SmintIoAuthenticatorException;
import io.smint.clapi.consumer.integration.core.exceptions.SmintIoSyncJobException;


/**
 * Implements a job that can be scheduled or called on-demand to drive the synchronization process.
 *
 * <p>
 * The default implementation will connect to the Smint.io platform, prepare all data to be synchronized and pass the
 * data to an instance of {@link io.smint.clapi.consumer.integration.core.target.ISyncTarget}. A default implementation
 * is part of this library. So consumers of this library should not implement their own version.
 * </p>
 */
public interface ISyncJob {

    /**
     * Perform synchronization without or without syncing any asset meta data.
     *
     * @param syncMetaData pass {@code true} to force synchronization of meta data. If you pass {@code false} then no
     *                     meta data will be synchronized. The later is useful for non-scheduled synchronizations.
     * @throws SmintIoAuthenticatorException in case authentication with Smint.io API fails.
     * @throws SmintIoSyncJobException       for any unexpected situation, like provided settings are invalid, etc.<br>
     *                                       The message should then be logged somewhere.
     */
    void synchronize(final boolean syncMetaData) throws SmintIoAuthenticatorException, SmintIoSyncJobException;
}
