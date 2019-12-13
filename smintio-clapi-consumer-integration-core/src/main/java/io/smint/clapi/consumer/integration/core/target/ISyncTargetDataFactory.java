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
 * Factory to create target specific data instances to write meta data to.
 *
 * <p>
 * Meta data is structured into various classes (interfaces). Target specific instances are used to store these meta
 * data. Implementors of this factory are delivering such instances, related to the DAM target.
 * </p>
 *
 */
public interface ISyncTargetDataFactory {

    /**
     * Factory function to create a new instance representing target's data structure for a <em>Binary Assets</em>.
     *
     * <p>
     * The newly created instance MUST NOT be stored automatically on the sync target prior to returning it. For storing
     * this instance, it will be passed to this sync target via
     * {@link ISyncTarget#importNewTargetAssets(SyncAsset[])}.
     * </p>
     *
     * @return must return a fresh instance for every call and must not ever return {@code null}.
     * @see SyncAsset
     */
    SyncAsset createSyncBinaryAsset();


    /**
     * Factory function to create a new instance representing target's compound assets.
     *
     * <p>
     * The newly created instance MUST NOT be stored automatically on the sync target prior to returning it. For storing
     * this instance, it will be passed to this sync target via
     * {@link ISyncTarget#importNewTargetCompoundAssets(SyncAsset[])}.
     * </p>
     *
     * @return must return a fresh instance for every call and must not ever return {@code null}.
     * @see SyncAsset
     */
    SyncAsset createSyncCompoundAsset();


    /**
     * Factory function to create a new instance representing target's license option data structure.
     *
     * <p>
     * The newly created instance SHOULD NOT be stored automatically on the sync target prior to returning it. For
     * storing this instance, it will be attached to an asset via
     * {@link SyncAsset#setLicenseOptions(ISyncLicenseOption[])} and thus passed to this sync target via one of
     * {@link ISyncTarget#importNewTargetAssets(SyncAsset[])},
     * {@link ISyncTarget#importNewTargetCompoundAssets(SyncAsset[])},
     * {@link ISyncTarget#updateTargetAssets(SyncAsset[])},
     * {@link ISyncTarget#updateTargetCompoundAssets(SyncAsset[])}
     * </p>
     *
     * @return must return a fresh instance for every call and must not ever return {@code null}.
     */
    ISyncLicenseOption createSyncLicenseOption();


    /**
     * Factory function to create a new instance representing target's lincense terms data structure.
     *
     * <p>
     * The newly created instance SHOULD NOT be stored automatically on the sync target prior to returning it. For
     * storing this instance, it will be attached to an asset via {@link SyncAsset#setLicenseTerms(ISyncLicenseTerm[])}
     * and thus passed to this sync target via one of {@link ISyncTarget#importNewTargetAssets(SyncAsset[])},
     * {@link ISyncTarget#importNewTargetCompoundAssets(SyncAsset[])},
     * {@link ISyncTarget#updateTargetAssets(SyncAsset[])},
     * {@link ISyncTarget#updateTargetCompoundAssets(SyncAsset[])}
     * </p>
     *
     * @return must return a fresh instance for every call and must not ever return {@code null}.
     */
    ISyncLicenseTerm createSyncLicenseTerm();


    /**
     * Factory function to create a new instance representing target's release details structure.
     *
     * <p>
     * The newly created instance SHOULD NOT be stored automatically on the sync target prior to returning it. For
     * storing this instance, it will be attached to an asset via
     * {@link SyncAsset#setReleaseDetails(ISyncReleaseDetails)} and thus passed to this sync target via one of
     * {@link ISyncTarget#importNewTargetAssets(SyncAsset[])},
     * {@link ISyncTarget#importNewTargetCompoundAssets(SyncAsset[])},
     * {@link ISyncTarget#updateTargetAssets(SyncAsset[])},
     * {@link ISyncTarget#updateTargetCompoundAssets(SyncAsset[])}
     * </p>
     *
     * @return must return a fresh instance for every call and must not ever return {@code null}.
     */
    ISyncReleaseDetails createSyncReleaseDetails();


    /**
     * Factory function to create a new instance representing target's downloads constraints data structure.
     *
     * <p>
     * The newly created instance SHOULD NOT be stored automatically on the sync target prior to returning it. For
     * storing this instance, it will be attached to an asset via
     * {@link SyncAsset#setDownloadConstraints(ISyncDownloadConstraints)} and thus passed to this sync target via one
     * of {@link ISyncTarget#importNewTargetAssets(SyncAsset[])},
     * {@link ISyncTarget#importNewTargetCompoundAssets(SyncAsset[])},
     * {@link ISyncTarget#updateTargetAssets(SyncAsset[])},
     * {@link ISyncTarget#updateTargetCompoundAssets(SyncAsset[])}
     * </p>
     *
     * @return must return a fresh instance for every call and must not ever return {@code null}.
     */
    ISyncDownloadConstraints createSyncDownloadConstraints();
}

// CHECKSTYLE OFF: MethodCount
