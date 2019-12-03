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

package io.smint.clapi.consumer.integration.core.target;

/**
 * Defines some download constraints on an asset.
 *
 * <p>
 * These constrains need to be stored with the sync target. There is no need to read these values, as they are NOT
 * compared to the values at Smint.io. Smint.io platform is the master source of these values and thus during
 * synchronization these value are only being set and not retrieved.
 * </p>
 *
 * <p>
 * Every instance is being created with {@link ISyncTarget#createSyncDownloadConstraints()}.
 * </p>
 */
public interface ISyncDownloadConstraints extends ISyncDataType {

    /**
     * Sets the maximum amount of users that are allowed to use the related asset.
     *
     * <p>
     * The default value is {@code 0} (meaning <em>"unrestricted"</em>), which is different from <em>unknown</em>. so if
     * this function is never called on the implementing instance, {@code null} is the value to store on the sync target
     * for the related asset as the constraint's value is simply not known.
     * </p>
     *
     * @param maxUsers the maximum amount of users as a positive integer value. if value is below or equal {@code 0},
     *                 then there are no restrictions on the maximum amount of users. Values below {@code 0} should be
     *                 stored as {@code null}, which means <em>unknown</em>.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    ISyncDownloadConstraints setMaxUsers(Integer maxUsers);


    /**
     * Sets the maximum amount of downloads of the related asset, that are allowed.
     *
     * <p>
     * The default value is {@code 0} (meaning <em>"unrestricted"</em>), which is different from <em>unknown</em>. so if
     * this function is never called on the implementing instance, {@code null} is the value to store on the sync target
     * for the related asset as the constraint's value is simply not known.
     * </p>
     *
     * @param maxDownloads the maximum amount of downloads as a positive integer value. if value is below or equal 0,
     *                     then there are no restrictions on the maximum downloads. Values below {@code 0} should be
     *                     stored as {@code null}, which means <em>unknown</em>.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    ISyncDownloadConstraints setMaxDownloads(Integer maxDownloads);


    /**
     * Sets the maximum amount of re-uses of the related asset, that are allowed.
     *
     * <p>
     * The default value is {@code 0} (meaning <em>"unrestricted"</em>), which is different from <em>unknown</em>. so if
     * this function is never called on the implementing instance, {@code null} is the value to store on the sync target
     * for the related asset as the constraint's value is simply not known.
     * </p>
     *
     * @param maxReuses the maximum re-uses as a positive integer value. if value is below or equal 0, then there are no
     *                  restrictions on the maximum downloads. Values below {@code 0} should be stored as {@code null},
     *                  which means <em>unknown</em>.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    ISyncDownloadConstraints setMaxReuses(Integer maxReuses);
}
