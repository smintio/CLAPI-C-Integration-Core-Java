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

package io.smint.clapi.consumer.integration.core.contracts;

/**
 * Provides some download constraints.
 */
public interface ISmintIoDownloadConstraints extends ISmintIoDataType {

    /**
     * The default value - denoting an <em>unlimited</em> value - for all fields in case no value has ever been set.
     */
    int UNKNOWN = -1;


    /**
     * Get the maximum allowed downloads for the asset.
     *
     * @return the maximum amount of downloads as a positive integer value. if value is below or equal {@code 0}, then
     *         there are no restrictions on the maximum amount of users. Returns {@link #UNKNOWN} if no restrictions
     *         have been set.
     */
    int getMaxDownloads();


    /**
     * Get the maximum allowed users to make use of the asset.
     *
     * @return the maximum amount of users as a positive integer value. if value is below or equal {@code 0}, then there
     *         are no restrictions on the maximum amount of users. Returns {@link #UNKNOWN} if no restrictions have been
     *         set.
     */
    int getMaxUsers();


    /**
     * Get the maximum allowed reuses of the asset.
     *
     * @return the maximum amount of reuses as a positive integer value. if value is below or equal {@code 0}, then
     *         there are no restrictions on the maximum amount of users. Returns {@link #UNKNOWN} if no restrictions
     *         have been set.
     */
    int getMaxReuses();
}

