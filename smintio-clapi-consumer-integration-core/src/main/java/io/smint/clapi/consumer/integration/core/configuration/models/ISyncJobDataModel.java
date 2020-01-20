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

package io.smint.clapi.consumer.integration.core.configuration.models;


/**
 * Data to remember between synchronization runs.
 *
 * <p>
 * At the moment, only the continuation UUID is required to be passed to next run. This UUID is presented to the
 * Smint.io server as a sort of session ID marker. The server links this UUID to the last synchronized asset. So on a
 * new sync run, only newer assets need to be synchronized, greatly improving synchronization process. It helps to avoid
 * loops and duplicate synchronization of assets as well as helping to split a big list of assets into smaller chunks
 * and keep track of these chunks.
 * </p>
 *
 * <p>
 * If the next run should start at the beginning - synchronizing all assets from scratch - the only thing to do is, to
 * remove this <em>Continuation UUID</em>. Without this marker, the Smint.io server will start to send all assets, as if
 * none of them has ever been synchronized yet.
 * </p>
 */
public interface ISyncJobDataModel {

    /**
     * Return the continuation UUID to be used with next synchronization run.
     *
     * @return {@code null} if no UUID was stored on last run or the value as provided from last run.
     */
    String getContinuationUuid();
}
