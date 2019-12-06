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

import java.util.Collections;
import java.util.Locale;
import java.util.Map;

import io.smint.clapi.consumer.integration.core.contracts.ISmintIoMetadataElement;


/**
 * POJO element of meta data used with Smint.io assets.
 *
 * <p>
 * Each meta data element is a structured element, containing various properties. In effect it is like a JSON object
 * with a lot of properties ({@link #getValues()}), saved to the storage with the key {@link #getKey()}.
 * </p>
 */
public class SmintIoMetadataElementImpl implements ISmintIoMetadataElement {

    private String _targetUuid = null;
    private String _key = null;
    private Map<Locale, String> _values = null;


    @Override
    public String getKey() {
        return this._key;
    }


    /**
     * Sets a new key to be related to the values from {@link #getValues()}.
     *
     * @param newKey the new key to set - should not be {@code null} because consumers of this POJO class might fail.
     *               Nevertheless the key is stored as-is.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    public SmintIoMetadataElementImpl setKey(final String newKey) {
        this._key = newKey;
        return this;
    }


    @Override
    public Map<Locale, String> getValues() {
        return this._values != null ? Collections.unmodifiableMap(this._values) : null;
    }


    /**
     * Sets new localized values to be related to the key from {@link #getKey()}.
     *
     * @param newValues the values to set, or {@code null}. The parameter is stored internally as-is.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    public SmintIoMetadataElementImpl setValues(final Map<Locale, String> newValues) {
        this._values = newValues;
        return this;
    }


    @Override
    public String getTargetMetadataUuid() {
        return this._targetUuid;
    }


    @Override
    public ISmintIoMetadataElement setTargetMetadataUuid(final String targetMetadataUuid) {
        this._targetUuid = targetMetadataUuid;
        return this;
    }


}
