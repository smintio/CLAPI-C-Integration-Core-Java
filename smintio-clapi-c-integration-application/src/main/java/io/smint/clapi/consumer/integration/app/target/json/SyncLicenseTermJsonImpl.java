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

import java.time.OffsetDateTime;
import java.util.Locale;
import java.util.Map;

import io.smint.clapi.consumer.integration.core.target.ISyncLicenseTerm;


/**
 * Defines a license term composing restrictions and allowances.
 */
public class SyncLicenseTermJsonImpl extends BaseSyncDataTypeJson implements ISyncLicenseTerm {

    public static final String JSON_KEY__SEQUENCE_NUMBER = "sequenceNumber";
    public static final String JSON_KEY__NAME = "name";
    public static final String JSON_KEY__EXCLUSIVITIES = "exclusivities";
    public static final String JSON_KEY__ALLOWED_USAGES = "allowedUsages";
    public static final String JSON_KEY__RESTRICTED_USAGES = "restrictedUsages";
    public static final String JSON_KEY__ALLOWED_SIZES = "allowedSizes";
    public static final String JSON_KEY__RESTRICTED_SIZES = "restrictedSizes";
    public static final String JSON_KEY__ALLOWED_PLACEMENTS = "allowedPlacements";
    public static final String JSON_KEY__RESTRICTED_PLACEMENTS = "restrictedPlacements";
    public static final String JSON_KEY__ALLOWED_DISTRIBUTION = "allowedDistributions";
    public static final String JSON_KEY__RESTRICTED_DISTRIBUTION = "restrictedDistributions";
    public static final String JSON_KEY__ALLOWED_GEOGRAPHIES = "allowedGeographies";
    public static final String JSON_KEY__RESTRICTED_GEOGRAPHIES = "restrictedGeographies";
    public static final String JSON_KEY__ALLOWED_INDUSTRIES = "allowedIndustries";
    public static final String JSON_KEY__RESTRICTED_INDUSTRIES = "restrictedIndustries";
    public static final String JSON_KEY__ALLOWED_LANGUAGES = "allowedLanguages";
    public static final String JSON_KEY__RESTRICTED_LANGUAGES = "restrictedLanguages";
    public static final String JSON_KEY__USAGE_LIMITS = "usageLimits";
    public static final String JSON_KEY__VALID_FROM = "validFrom";
    public static final String JSON_KEY__VALID_UNTIL = "validUntil";
    public static final String JSON_KEY__TO_BE_USED_UNTIL = "toBeUsedUntil";
    public static final String JSON_KEY__EDITORIAL_USE = "isEditorialUse";


    @Override
    public ISyncLicenseTerm setSequenceNumber(final int sequenceNumber) {
        this.putMetaDataValue(JSON_KEY__SEQUENCE_NUMBER, sequenceNumber);
        return this;
    }


    @Override
    public ISyncLicenseTerm setName(final Map<Locale, String> name) {
        this.putMetaDataValue(JSON_KEY__NAME, name);
        return this;
    }


    @Override
    public ISyncLicenseTerm setExclusivities(final String[] exclusivityKeys) {
        this.putMetaDataValue(JSON_KEY__EXCLUSIVITIES, exclusivityKeys);
        return this;
    }


    @Override
    public ISyncLicenseTerm setAllowedUsages(final String[] usageKeys) {
        this.putMetaDataValue(JSON_KEY__ALLOWED_USAGES, usageKeys);
        return this;
    }


    @Override
    public ISyncLicenseTerm setRestrictedUsages(final String[] usageKeys) {
        this.putMetaDataValue(JSON_KEY__RESTRICTED_USAGES, usageKeys);
        return this;
    }


    @Override
    public ISyncLicenseTerm setAllowedSizes(final String[] sizeKeys) {
        this.putMetaDataValue(JSON_KEY__ALLOWED_SIZES, sizeKeys);
        return this;
    }


    @Override
    public ISyncLicenseTerm setRestrictedSizes(final String[] sizeKeys) {
        this.putMetaDataValue(JSON_KEY__RESTRICTED_SIZES, sizeKeys);
        return this;
    }


    @Override
    public ISyncLicenseTerm setAllowedPlacements(final String[] placementKeys) {
        this.putMetaDataValue(JSON_KEY__ALLOWED_PLACEMENTS, placementKeys);
        return this;
    }


    @Override
    public ISyncLicenseTerm setRestrictedPlacements(final String[] placementKeys) {
        this.putMetaDataValue(JSON_KEY__RESTRICTED_PLACEMENTS, placementKeys);
        return this;
    }


    @Override
    public ISyncLicenseTerm setAllowedDistributions(final String[] distributionKeys) {
        this.putMetaDataValue(JSON_KEY__ALLOWED_DISTRIBUTION, distributionKeys);
        return this;
    }


    @Override
    public ISyncLicenseTerm setRestrictedDistributions(final String[] distributionKeys) {
        this.putMetaDataValue(JSON_KEY__RESTRICTED_DISTRIBUTION, distributionKeys);
        return this;
    }


    @Override
    public ISyncLicenseTerm setAllowedGeographies(final String[] geographyKeys) {
        this.putMetaDataValue(JSON_KEY__ALLOWED_GEOGRAPHIES, geographyKeys);
        return this;
    }


    @Override
    public ISyncLicenseTerm setRestrictedGeographies(final String[] geographyKeys) {
        this.putMetaDataValue(JSON_KEY__RESTRICTED_GEOGRAPHIES, geographyKeys);
        return this;
    }


    @Override
    public ISyncLicenseTerm setAllowedIndustries(final String[] industryKeys) {
        this.putMetaDataValue(JSON_KEY__ALLOWED_INDUSTRIES, industryKeys);
        return this;
    }


    @Override
    public ISyncLicenseTerm setRestrictedIndustries(final String[] industryKeys) {
        this.putMetaDataValue(JSON_KEY__RESTRICTED_INDUSTRIES, industryKeys);
        return this;
    }


    @Override
    public ISyncLicenseTerm setAllowedLanguages(final String[] languages) {
        this.putMetaDataValue(JSON_KEY__ALLOWED_LANGUAGES, languages);
        return this;
    }


    @Override
    public ISyncLicenseTerm setRestrictedLanguages(final String[] languages) {
        this.putMetaDataValue(JSON_KEY__RESTRICTED_LANGUAGES, languages);
        return this;
    }


    @Override
    public ISyncLicenseTerm setUsageLimits(final String[] usageLimitKeys) {
        this.putMetaDataValue(JSON_KEY__USAGE_LIMITS, usageLimitKeys);
        return this;
    }


    @Override
    public ISyncLicenseTerm setValidFrom(final OffsetDateTime validFrom) {
        this.putMetaDataValue(JSON_KEY__VALID_FROM, validFrom);
        return this;
    }


    @Override
    public ISyncLicenseTerm setValidUntil(final OffsetDateTime validUntil) {
        this.putMetaDataValue(JSON_KEY__VALID_UNTIL, validUntil);
        return this;
    }


    @Override
    public ISyncLicenseTerm setToBeUsedUntil(final OffsetDateTime toBeUsedUntil) {
        this.putMetaDataValue(JSON_KEY__TO_BE_USED_UNTIL, toBeUsedUntil);
        return this;
    }


    @Override
    public ISyncLicenseTerm setIsEditorialUse(final Boolean isEditorialUse) {
        this.putMetaDataValue(JSON_KEY__EDITORIAL_USE, isEditorialUse);
        return this;
    }
}
