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

import java.io.File;
import java.net.URL;
import java.util.Locale;
import java.util.Map;


public interface ISmintIoBinary {

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


    // TODO: javadoc
    String getContentType();

    // TODO: javadoc
    String getBinaryType();


    /**
     * Get the name of the binary asset in various languages.
     *
     * @return a single line name of the asset, translated to various languages.
     */
    Map<Locale, String> getName();


    /**
     * Provide a multi-line description of the binary asset in various languages.
     *
     * @return the multi-line description of the asset, translated to various languages.
     */
    Map<Locale, String> getDescription();


    // TODO: javadoc
    Map<Locale, String> getUsage();


    /**
     * Return the download URL to access the content on the Smint.io platform.
     *
     * @return an URL to download the asset.
     */
    URL getDownloadUrl();


    /**
     * Downloads the binary from Smint.io platform to a temporary file and provides this file.
     *
     * <p>
     * The URL to download from is taken from {@code #getDownloadUrl()} and all content stored to a temporary file.
     * </p>
     *
     * @return an already opened {@link java.io.InputStream} to read bytes from. If the content type is a textual type,
     *         the content is encoded with UTF-8.
     */
    File getDownloadedFile();


    /**
     * Denotes the recommended file name for the locally stored asset.
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

