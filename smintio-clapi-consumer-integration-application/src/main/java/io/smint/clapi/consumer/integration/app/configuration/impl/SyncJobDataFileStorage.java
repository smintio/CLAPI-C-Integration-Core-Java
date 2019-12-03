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

package io.smint.clapi.consumer.integration.app.configuration.impl;

import java.io.File;

import javax.inject.Inject;
import javax.inject.Named;

import io.smint.clapi.consumer.integration.core.configuration.ISyncJobDataStorage;
import io.smint.clapi.consumer.integration.core.configuration.models.ISyncJobDataModel;
import io.smint.clapi.consumer.integration.core.configuration.models.impl.SyncJobDataJsonConverter;


/**
 * Provides storage layer for some process data and stores it in file system.
 *
 */
public class SyncJobDataFileStorage extends FileModelStorage<ISyncJobDataModel> implements ISyncJobDataStorage {

    /**
     * Create a new facade to another token provider, caching its result for improved performance.
     *
     * @param tokenConverter the provider to wrap and query for data - must not be {@code null}.
     * @param fileStorage    the file to store the token to.
     */
    @Inject
    public SyncJobDataFileStorage(
        final SyncJobDataJsonConverter tokenConverter,
        @Named("smint.io-job-data-file") final File fileStorage
    ) {
        super(tokenConverter, fileStorage);
    }


    @Override
    public ISyncJobDataModel getSyncProcessData() {
        return this.get();
    }


    @Override
    public SyncJobDataFileStorage storeSyncProcessData(final ISyncJobDataModel newJobData) {
        this.store(newJobData);
        return this;
    }
}
