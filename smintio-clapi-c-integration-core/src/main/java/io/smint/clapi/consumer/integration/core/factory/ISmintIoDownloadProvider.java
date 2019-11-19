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

package io.smint.clapi.consumer.integration.core.factory;

import java.io.File;
import java.net.URL;

import javax.inject.Provider;


/**
 * Implementing classes create and provide new file downloaders on demand.
 *
 * <p>
 * Creates a file downloader to download from Smint.io API. The downloader is targeted at Smint.io API and closed to a
 * specific URL. It is not a general purpose downloader.
 * </p>
 */
public interface ISmintIoDownloadProvider {

    /**
     * Creates a new Smint.io API downloader, targeted at the specific download URL.
     *
     * <p>
     * The created downloader must return {@code null} in case either of the parameters are {@code null}. For any
     * download error, an exception must be thrown.
     * </p>
     *
     * @param downloadFileUrl the download URL to use to download the binary data from.
     * @param targetFile      the file to store the binary data to.
     * @return a brand new downloder that will provide the downloaded file.
     */
    Provider<File> createDownloaderForSmintIoUrl(final URL downloadFileUrl, final File targetFile);
}
