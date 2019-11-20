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

package io.smint.clapi.consumer.integration.app.target.json;

import java.util.Hashtable;
import java.util.Map;


/**
 * Base class to store all data inside a big map.
 *
 * <p>
 * Quite some synchronization targets store meta data in big maps/dictionaries, like JSON. This implementation does the
 * same. It is just a proof of concept and nothing more useful.
 * </p>
 */
public class BaseSyncDataTypeJson {

    private Map<String, Object> data = new Hashtable<>();


    /**
     * Provides the reference to the internal meta data storage.
     *
     * @return the metadata storage - never {@code null}
     */
    public Map<String, Object> getMetaData() {
        return this.data;
    }


    /**
     * Sets a new meta to be used internally.
     *
     * @param newMetaData the new data to use - its reference will be used directly.
     * @return {@code this}
     */
    public BaseSyncDataTypeJson setMetaData(final Map<String, Object> newMetaData) {

        if (newMetaData != null) {
            this.data = newMetaData;
        } else {
            this.data.clear();
        }
        return this;
    }


    /**
     * Sets a new meta data value.
     *
     * @param key      the metadata key to use - putting a value will be ignored if this is {@code null}.
     * @param newValue the value to use
     * @return {@code this}
     */
    public BaseSyncDataTypeJson putMetaDataValue(final String key, final Object newValue) {

        if (key != null && !key.isEmpty()) {
            final Map<String, Object> metaData = this.getMetaData();
            if (newValue == null) {
                metaData.remove(key);
            } else {
                metaData.put(key, newValue);
            }
        }

        return this;
    }
}
