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

package io.smint.clapi.consumer.integration.core.contracts.impl;

import io.smint.clapi.consumer.integration.core.contracts.ISmintIoDownloadConstraints;


/**
 * POJO to hold basic download constraints for assets.
 */
public class SmintIoDownloadConstraintsImpl implements ISmintIoDownloadConstraints {

    private int _maxDownloads = UNLIMITED;
    private int _maxUsers = UNLIMITED;
    private int _maxReuses = UNLIMITED;


    @Override
    public int getMaxDownloads() {
        return this._maxDownloads;
    }


    /**
     * Sets a new value to max downloads.
     *
     * @param newMaxDownloads the new maximum downloads to allow.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    public SmintIoDownloadConstraintsImpl setMaxDownloads(final int newMaxDownloads) {
        this._maxDownloads = newMaxDownloads;
        return this;
    }


    @Override
    public int getMaxUsers() {
        return this._maxUsers;
    }


    /**
     * Sets a new value to max users.
     *
     * @param newMaxUsers the new maximum users to allow.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    public SmintIoDownloadConstraintsImpl setMaxUsers(final int newMaxUsers) {
        this._maxUsers = newMaxUsers;
        return this;
    }


    @Override
    public int getMaxReuses() {
        return this._maxReuses;
    }


    /**
     * Sets a new value to max re-uses.
     *
     * @param newMaxReuses the new maximum re-use to allow.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    public SmintIoDownloadConstraintsImpl setMaxReuses(final int newMaxReuses) {
        this._maxReuses = newMaxReuses;
        return this;
    }
}

