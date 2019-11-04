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
import java.util.Map;


public interface ISmintIoBinary {

    String getUuid();

    String getContentType();

    String getBinaryType();

    Map<String, String> getName();

    Map<String, String> getDescription();

    Map<String, String> getUsage();



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

    String getCulture();

    int getVersion();
}

