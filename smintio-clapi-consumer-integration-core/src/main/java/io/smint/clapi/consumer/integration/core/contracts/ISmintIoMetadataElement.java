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

import java.util.Locale;
import java.util.Map;


/**
 * Element of meta data used with Smint.io assets.
 *
 * <p>
 * Each meta data element is a structured element, containing various properties. In effect it is like a JSON object
 * with a lot of properties ({@link #getValues()}), saved to the storage with the key {@link #getKey()}.
 * </p>
 *
 * <p>
 * In order to speed up synchronization, provide a secure mapping and ease the work load on the sync target DAM
 * abstraction layer {@link io.smint.clapi.consumer.integration.core.target.ISyncTarget}, the meta data items are cached
 * within the synchronization job. So instances are probably long living.
 * </p>
 */
public interface ISmintIoMetadataElement {

    /**
     * The Smint.io API key/id of this meta data element.
     *
     * @return the key used as ID.
     */
    String getKey();


    /**
     * Provides the ID, this meta data element uses on the sync target side.
     *
     * <p>
     * This value provides a 1:1 mapping from the Smint.io API key ({@link #getKey()}) to the key on the sync target
     * side. This is the value that is used when settings meta data to
     * {@link io.smint.clapi.consumer.integration.core.target.SyncAsset}s
     * </p>
     *
     * @return the ID used on the sync target side.
     */
    String getTargetMetadataUuid();


    /**
     * Set the ID, this meta data element uses on the sync target side.
     *
     * <p>
     * The value is retrieved from an instance of {@link io.smint.clapi.consumer.integration.core.target.ISyncTarget}
     * during meta data import and then set on this instance for later use.
     * </p>
     *
     * @param targetMetadataUuid the ID of this meta data element on the sync target side.
     * @return {@code this}
     */
    ISmintIoMetadataElement setTargetMetadataUuid(final String targetMetadataUuid);


    /**
     * Retrieve all the properties of the meta data element object.
     *
     * @return The properties as a flat list of values.
     */
    Map<Locale, String> getValues();
}
