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

import io.smint.clapi.consumer.integration.core.target.ISyncReleaseDetails;


/**
 * Defines some asset release details.
 */
public class SyncReleaseDetailsJsonImpl extends BaseSyncDataTypeJson implements ISyncReleaseDetails {


    public static final String JSON_KEY__MODEL_RELEASE_STATE = "modelReleaseState";
    public static final String JSON_KEY__PROPERTY_RELEASE_STATE = "propertyReleaseState";
    public static final String JSON_KEY__PROVIDER_ALLOWED_USE_COMMENT = "providerAllowedUseComment";
    public static final String JSON_KEY__PROVIDER_RELEASE_COMMENT = "providerReleaseComment";
    public static final String JSON_KEY__PROVIDER_USAGE_CONSTRAINT = "providerUsageConstraints";

    @Override
    public ISyncReleaseDetails setModelReleaseState(final String modelReleaseStateKey) {
        this.putMetaDataValue(JSON_KEY__MODEL_RELEASE_STATE, modelReleaseStateKey);
        return this;
    }


    @Override
    public ISyncReleaseDetails setPropertyReleaseState(final String propertyReleaseStateKey) {
        this.putMetaDataValue(JSON_KEY__PROPERTY_RELEASE_STATE, propertyReleaseStateKey);
        return this;
    }


    @Override
    public ISyncReleaseDetails setProviderAllowedUseComment(final Map<Locale, String> providerAllowedUseComment) {
        this.putMetaDataValue(JSON_KEY__PROVIDER_ALLOWED_USE_COMMENT, providerAllowedUseComment);
        return this;
    }


    @Override
    public ISyncReleaseDetails setProviderReleaseComment(final Map<Locale, String> providerReleaseComment) {
        this.putMetaDataValue(JSON_KEY__PROVIDER_RELEASE_COMMENT, providerReleaseComment);
        return this;
    }


    @Override
    public ISyncReleaseDetails setProviderUsageConstraints(final Map<Locale, String> providerUsageConstraints) {
        this.putMetaDataValue(JSON_KEY__PROVIDER_USAGE_CONSTRAINT, providerUsageConstraints);
        return this;
    }
}
