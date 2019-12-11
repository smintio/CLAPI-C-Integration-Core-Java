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

import java.net.URL;
import java.time.OffsetDateTime;
import java.util.Locale;
import java.util.Map;

import io.smint.clapi.consumer.integration.core.target.ISyncBinaryAsset;
import io.smint.clapi.consumer.integration.core.target.ISyncCompoundAsset;
import io.smint.clapi.consumer.integration.core.target.ISyncDownloadConstraints;
import io.smint.clapi.consumer.integration.core.target.ISyncLicenseOption;
import io.smint.clapi.consumer.integration.core.target.ISyncLicenseTerm;
import io.smint.clapi.consumer.integration.core.target.ISyncReleaseDetails;


// CHECKSTYLE OFF: MethodCount

/**
 * Implementing classes represent the notion of <em>Compound Asset</em>, which combines variants of the same content.
 */
public class SyncCompoundAssetJsonImpl extends SyncAssetJsonImpl implements ISyncCompoundAsset {


    public static final String JSON_KEY__ASSET_PARTS = "assetParts";


    @Override
    public ISyncCompoundAsset setAssetParts(final ISyncBinaryAsset[] compoundParts) {
        this.putMetaDataValue(JSON_KEY__ASSET_PARTS, compoundParts);
        return this;
    }


    @Override
    public boolean isCompoundAsset() {
        return true;
    }


    @Override
    public ISyncCompoundAsset setTransactionUuid(final String smintIoId) {
        super.setTransactionUuid(smintIoId);
        return this;
    }

    @Override
    public ISyncCompoundAsset setTargetAssetUuid(final String targetAssetUuid) {
        super.setTargetAssetUuid(targetAssetUuid);
        return this;
    }

    @Override
    public ISyncCompoundAsset setName(final Map<Locale, String> name) {
        super.setName(name);
        return this;
    }

    @Override
    public ISyncCompoundAsset setContentElementUuid(final String contentElementUuid) {
        super.setContentElementUuid(contentElementUuid);
        return this;
    }

    @Override
    public ISyncCompoundAsset setContentType(final String contentTypeKey) {
        super.setContentType(contentTypeKey);
        return this;
    }

    @Override
    public ISyncCompoundAsset setContentProvider(final String contentProviderKey) {
        super.setContentProvider(contentProviderKey);
        return this;
    }

    @Override
    public ISyncCompoundAsset setContentCategory(final String contentCategoryKey) {
        super.setContentCategory(contentCategoryKey);
        return this;
    }

    @Override
    public ISyncCompoundAsset setDescription(final Map<Locale, String> description) {
        super.setDescription(description);
        return this;
    }

    @Override
    public ISyncCompoundAsset setSmintIoUrl(final URL smintIoUrl) {
        super.setSmintIoUrl(smintIoUrl);
        return this;
    }


    @Override
    public ISyncCompoundAsset setCreatedAt(final OffsetDateTime createdAt) {
        super.setCreatedAt(createdAt);
        return this;
    }


    @Override
    public ISyncCompoundAsset setLastUpdatedAt(final OffsetDateTime lastUpdatedAt) {
        super.setLastUpdatedAt(lastUpdatedAt);
        return this;
    }


    @Override
    public ISyncCompoundAsset setPurchasedAt(final OffsetDateTime purchasedAt) {
        super.setPurchasedAt(purchasedAt);
        return this;
    }


    @Override
    public ISyncCompoundAsset setCartPurchaseTransactionUuid(final String cartPurchaseTransactionUuid) {
        super.setCartPurchaseTransactionUuid(cartPurchaseTransactionUuid);
        return this;
    }


    @Override
    public ISyncCompoundAsset setHasBeenCancelled(final boolean hasBeenCancelled) {
        super.setHasBeenCancelled(hasBeenCancelled);
        return this;
    }


    @Override
    public ISyncCompoundAsset setProjectUuid(final String projectUuid) {
        super.setProjectUuid(projectUuid);
        return this;
    }


    @Override
    public ISyncCompoundAsset setProjectName(final Map<Locale, String> projectName) {
        super.setProjectName(projectName);
        return this;
    }


    @Override
    public ISyncCompoundAsset setCollectionUuid(final String collectionUuid) {
        super.setCollectionUuid(collectionUuid);
        return this;
    }


    @Override
    public ISyncCompoundAsset setCollectionName(final Map<Locale, String> collectionName) {
        super.setCollectionName(collectionName);
        return this;
    }


    @Override
    public ISyncCompoundAsset setKeywords(final Map<Locale, String[]> keywords) {
        super.setKeywords(keywords);
        return this;
    }


    @Override
    public ISyncCompoundAsset setCopyrightNotices(final Map<Locale, String> copyrightNotices) {
        super.setCopyrightNotices(copyrightNotices);
        return this;
    }


    @Override
    public ISyncCompoundAsset setIsEditorialUse(final Boolean isEditorialUse) {
        super.setIsEditorialUse(isEditorialUse);
        return this;
    }


    @Override
    public ISyncCompoundAsset setHasLicenseTerms(final boolean hasLicenseTerms) {
        super.setHasLicenseTerms(hasLicenseTerms);
        return this;
    }


    @Override
    public ISyncCompoundAsset setLicenseType(final String licenseTypeKey) {
        super.setLicenseType(licenseTypeKey);
        return this;
    }


    @Override
    public ISyncCompoundAsset setLicenseeUuid(final String licenseeUuid) {
        super.setLicenseeUuid(licenseeUuid);
        return this;
    }


    @Override
    public ISyncCompoundAsset setLicenseeName(final String licenseeName) {
        super.setLicenseeName(licenseeName);
        return this;
    }


    @Override
    public ISyncCompoundAsset setLicenseText(final Map<Locale, String> licenseText) {
        super.setLicenseText(licenseText);
        return this;
    }


    @Override
    public ISyncCompoundAsset setLicenseOptions(final ISyncLicenseOption[] licenseOptions) {
        super.setLicenseOptions(licenseOptions);
        return this;
    }


    @Override
    public ISyncCompoundAsset setLicenseTerms(final ISyncLicenseTerm[] licenseTerms) {
        super.setLicenseTerms(licenseTerms);
        return this;
    }


    @Override
    public ISyncCompoundAsset setDownloadConstraints(final ISyncDownloadConstraints downloadConstraints) {
        super.setDownloadConstraints(downloadConstraints);
        return this;
    }


    @Override
    public ISyncCompoundAsset setReleaseDetails(final ISyncReleaseDetails releaseDetails) {
        super.setReleaseDetails(releaseDetails);
        return this;
    }
}

// CHECKSTYLE ON: MethodCount
