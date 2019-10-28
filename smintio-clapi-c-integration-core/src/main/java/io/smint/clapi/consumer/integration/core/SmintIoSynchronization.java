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

package io.smint.clapi.consumer.integration.core;

import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

import javax.inject.Inject;

import io.smint.clapi.consumer.integration.core.factory.ISyncFactory;


/**
 * Provides initializing functions for the Smint.io synchronization process and handles the synchronization.
 *
 * <p>
 * This class will create a synchronization job and will schedule it in function {@link #start()}. The schedule is set
 * to constant time between synchronization jobs of 5 Minutes.
 * </p>
 */
public class SmintIoSynchronization implements ISmintIoSynchronization {


    private final ISyncFactory _factory;


    /**
     * Create a new Smint.io synchronization progress.
     *
     * @param factory the user factory helping to create all target specific instances.
     */
    @Inject
    public SmintIoSynchronization(final ISyncFactory factory) {
        this._factory = factory;
    }


    /**
     * Return the used factory.
     *
     * @return the currently used factory.
     */
    public ISyncFactory getFactory() {
        return this._factory;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void start() {
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void stop() {
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Future<Void> doSync() {
        return this.doSync(false);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Future<Void> doSync(final boolean syncMetaData) {
        return new FutureTask<>(() -> null);
    }
}
