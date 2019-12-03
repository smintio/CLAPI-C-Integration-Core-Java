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

package io.smint.clapi.consumer.integration.core.factory.impl;

import java.io.File;
import java.net.URL;
import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Provider;

import okhttp3.OkHttpClient;

import io.smint.clapi.consumer.integration.core.configuration.IAuthTokenStorage;
import io.smint.clapi.consumer.integration.core.factory.ISmintIoDownloadProvider;
import io.smint.clapi.consumer.integration.core.jobs.impl.BinaryAssetDownloader;


/**
 * Creates a downloader that will download binary data from Smint.io API to a file.
 *
 * <p>
 * The downloader uses the OAuth 2 data from {@code IAuthTokenStorage} to authorize the download with Smint.io.
 * </p>
 */
public class SmintIoDownloadProviderImpl implements ISmintIoDownloadProvider {

    private final OkHttpClient _httpClient;
    private final IAuthTokenStorage _authTokenStorage;

    @Inject
    public SmintIoDownloadProviderImpl(
        final IAuthTokenStorage authTokenStorage,
        final OkHttpClient httpClient
    ) {
        this._authTokenStorage = authTokenStorage;

        Objects.requireNonNull(this._authTokenStorage, "No Smint.io authentication storage has been provided!");
        Objects.requireNonNull(
            this._authTokenStorage.getAuthData(),
            "No Smint.io authentication data available from authentication storage!"
        );

        this._httpClient = httpClient;
    }


    @Override
    public Provider<File> createDownloaderForSmintIoUrl(final URL downloadFileUrl, final File targetFile) {
        return new BinaryAssetDownloader(this._authTokenStorage, this._httpClient, downloadFileUrl, targetFile);
    }
}
