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

import java.util.Locale;
import java.util.Map;

import io.smint.clapi.consumer.integration.core.contracts.ISmintIoLicenseOptions;


/**
 * Provides informations for licenses on assets.
 */
public class SmintIoLicenseOptionsImpl implements ISmintIoLicenseOptions {

    private Map<Locale, String> _optionName = null;
    private Map<Locale, String> _licenceText = null;


    @Override
    public Map<Locale, String> getOptionName() {
        return this._optionName;
    }


    /**
     * Sets a new value to option name.
     *
     * @param newOptionName a new value to set
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    public SmintIoLicenseOptionsImpl setOptionName(final Map<Locale, String> newOptionName) {
        this._optionName = newOptionName;
        return this;
    }


    @Override
    public Map<Locale, String> getLicenseText() {
        return this._licenceText;
    }


    /**
     * Sets a new value to license text.
     *
     * @param newLicenseText a new value to set.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    public SmintIoLicenseOptionsImpl setLicenseText(final Map<Locale, String> newLicenseText) {
        this._licenceText = newLicenseText;
        return this;
    }
}

