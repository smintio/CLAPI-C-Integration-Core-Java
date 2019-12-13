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

import java.net.URL;
import java.util.Locale;
import java.util.Map;


/**
 * Represents a single binary for an asset data.
 *
 * <h2>Override of values if part of a <em>Compound Asset</em> (see
 * {@link io.smint.clapi.consumer.integration.core.target.SyncAsset})</h2>
 * <p>
 * If this asset is part of a <em>Compound Asset</em> and therefore has been retrieved as part of the list from
 * {@link io.smint.clapi.consumer.integration.core.target.SyncAsset#getAssetParts()}, its values will override the
 * values of it base compound asset. So, if both received values with something like
 * {@link io.smint.clapi.consumer.integration.core.target.SyncAsset#setContentCategory(String)}, the asset part's value
 * win and should be selected to be stored with the sync target's asset representation.
 * </p>
 *
 * @see io.smint.clapi.consumer.integration.core.target.SyncAsset
 */
public interface ISmintIoBinary extends ISmintIoDataType {

    /**
     * Get the unique universal identifier of this asset within the Smint.io platform.
     *
     * <p>
     * The {@code UUID} will not change, once created for an asset. It is the value to identify the asset with the
     * Smint.io platform and with any synchronization target.
     * </p>
     *
     * @return an invariant {@code UUID} created by the Smint.io platform.
     */
    String getUuid();


    /**
     * Provides the content type of this asset.
     *
     * <p>
     * The passed value is a string representation of an enumerated value. Although a fixes set of values are used, more
     * might be added in the near future. So implementing classes must support an arbitrary value. Possible values are
     * at the moment:
     * </p>
     * <ul>
     * <li>{@code image}</li>
     * <li>{@code video}</li>
     * <li>{@code audio}</li>
     * <li>{@code document}</li>
     * <li>{@code 3d} - a 3D video</li>
     * <li>{@code template}</li>
     * </ul>
     *
     * @return the Smint.io enumeration name or {@code null}.
     */
    String getContentType();


    /**
     * Provides the type of the binary as an enumeration key.
     *
     * <p>
     * A value is a simple reading text, valid with the Smint.io platform. Its value is arbitrary and uses mixed cases.
     * </p>
     *
     * <p>
     * Non-exhaustive example:
     * </p>
     * <ul>
     * <li>{@code standard}</li>
     * </ul>
     *
     * <p>
     * Although a known set of values are used, some might be changed and more be added in the near future without any
     * further notice. So implementing classes must support an arbitrary value.
     * </p>
     *
     * @return the Smint.io enumeration name or {@code null}.
     */
    String getBinaryType();


    /**
     * Get the name of the binary asset in various languages.
     *
     * @return a single line name of the asset, translated to various languages.
     */
    Map<Locale, String> getName();


    /**
     * Provide a localized description of the binary asset.
     *
     * @return the localized description of the asset, translated to various languages.
     */
    Map<Locale, String> getDescription();


    /**
     * Provide a localized description and explanation of the allowed and restricted usage of the binary.
     *
     * @return a localized text.
     */
    Map<Locale, String> getUsage();


    /**
     * Return the download URL to access the content on the Smint.io platform.
     *
     * @return a valid URL to download the binary from the Smint.io platform or {@code null}.
     */
    URL getDownloadUrl();


    /**
     * Retrieve the recommended file name to use on the synchronization target.
     *
     * <p>
     * Usually the available character set to file names are restricted by the file system storage. So it is not useful
     * to re-use the name of locally download files to determine the name of the binary on the target system. These
     * local file names may contain hashes/IDs and other mechanism to uniquely identify the downloaded asset before
     * uploading it to the target system. Hence the name provided by this function should be used as a base for the file
     * name on the target.
     * </p>
     *
     * <p>
     * This is an optionally suggestion for the file name. This name is not mandatory, but consumers are strongly
     * advised to use this name.
     * </p>
     *
     * @return a suggestion for a file.
     */
    String getRecommendedFileName();


    /**
     * Get the locale this assemble is valid for.
     *
     * <p>
     * If this binary asset does not depend on any locale (including language and country) {@code null} may be returned.
     * </p>
     *
     * @return a locale this asset is valid for or {@code null} if not restricted to any locale.
     */
    Locale getLocale();


    /**
     * Get a version number for this binary asset that can be used for updating the asset.
     *
     * <p>
     * Comparing the version number of binary assets enables
     * {@link io.smint.clapi.consumer.integration.core.target.ISyncTarget} to detect updates to the binary data. The
     * higher the version number, the more recent the binary data is.
     * </p>
     *
     * @return a number greater than {@code 0}, which is increased by {@code 1} on every update to the binary asset.
     */
    int getVersion();
}

