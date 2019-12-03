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

import java.util.Locale;
import java.util.Map;

import io.smint.clapi.consumer.integration.core.target.ISyncLicenseOption;


/**
 * Defines values for custom licensing options as created and provided by upstream content providers.
 */
public class SyncLicenseOptionJsonImpl extends BaseSyncDataTypeJson implements ISyncLicenseOption {

    public static final String JSON_KEY__NAME = "name";
    public static final String JSON_KEY__LICENSE_TEXT = "licenseText";


    @Override
    public ISyncLicenseOption setName(final Map<Locale, String> name) {
        this.putMetaDataValue(JSON_KEY__NAME, name);
        return this;
    }


    @Override
    public ISyncLicenseOption setLicenseText(final Map<Locale, String> licenseText) {
        this.putMetaDataValue(JSON_KEY__LICENSE_TEXT, licenseText);
        return this;
    }
}
