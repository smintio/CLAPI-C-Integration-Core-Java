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

package io.smint.clapi.consumer.integration.core.jobs.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.time.OffsetDateTime;
import java.util.Locale;
import java.util.Map;

import javax.inject.Provider;

import io.smint.clapi.consumer.integration.core.exceptions.SmintIoAuthenticatorException;
import io.smint.clapi.consumer.integration.core.target.ISyncBinaryAsset;
import io.smint.clapi.consumer.integration.core.target.ISyncDownloadConstraints;
import io.smint.clapi.consumer.integration.core.target.ISyncLicenseOption;
import io.smint.clapi.consumer.integration.core.target.ISyncLicenseTerm;
import io.smint.clapi.consumer.integration.core.target.ISyncReleaseDetails;


// CHECKSTYLE OFF: MethodCount

/**
 * A wrapper for {@link ISyncBinaryAsset} instances to enrich the base implementation with some caching and values.
 */
class WrapperSyncBinaryAsset extends WrapperBaseSyncAsset<ISyncBinaryAsset> implements ISyncBinaryAsset {


    private String _binaryUuid;


    public WrapperSyncBinaryAsset(final ISyncBinaryAsset assetToWrap) {
        super(assetToWrap);
    }


    @Override
    public boolean isCompoundAsset() {
        return false;
    }


    @Override
    public ISyncBinaryAsset setRecommendedFileName(final String fileName) {
        this.getWrapped().setRecommendedFileName(fileName);
        return this;
    }


    @Override
    public ISyncBinaryAsset setDownloadUrl(final URL downloadURL) {
        this.getWrapped().setDownloadUrl(downloadURL);
        return this;
    }


    @Override
    public File getDownloadedFile() throws FileNotFoundException, SmintIoAuthenticatorException {
        return this.getWrapped().getDownloadedFile();
    }


    @Override
    public ISyncBinaryAsset setDownloadedFileProvider(final Provider<File> downloadFileProvider) {
        this.getWrapped().setDownloadedFileProvider(downloadFileProvider);
        return this;
    }


    @Override
    public String getBinaryUuid() {
        return this._binaryUuid;
    }


    @Override
    public ISyncBinaryAsset setBinaryUuid(final String binaryUuid) {
        this._binaryUuid = binaryUuid;
        this.getWrapped().setBinaryUuid(binaryUuid);
        return this;
    }


    @Override
    public ISyncBinaryAsset setBinaryType(final String binaryTypeKey) {
        this.getWrapped().setBinaryType(binaryTypeKey);
        return this;
    }


    @Override
    public ISyncBinaryAsset setBinaryLocale(final Locale binaryLocale) {
        this.getWrapped().setBinaryLocale(binaryLocale);
        return this;
    }


    @Override
    public ISyncBinaryAsset setBinaryVersion(final int binaryVersion) {
        this.getWrapped().setBinaryVersion(binaryVersion);
        return this;
    }


    @Override
    public ISyncBinaryAsset setBinaryUsage(final Map<Locale, String> binaryUsage) {
        this.getWrapped().setBinaryUsage(binaryUsage);
        return this;
    }

    @Override
    public ISyncBinaryAsset setContentType(final String contentTypeKey) {
        super.setContentType(contentTypeKey);
        return this;
    }

    @Override
    public ISyncBinaryAsset setTransactionUuid(final String smintIoId) {
        super.setTransactionUuid(smintIoId);
        return this;
    }

    @Override
    public ISyncBinaryAsset setTargetAssetUuid(final String targetAssetUuid) {
        super.setTargetAssetUuid(targetAssetUuid);
        return this;
    }

    @Override
    public ISyncBinaryAsset setName(final Map<Locale, String> name) {
        super.setName(name);
        return this;
    }

    @Override
    public ISyncBinaryAsset setContentElementUuid(final String contentElementUuid) {
        super.setContentElementUuid(contentElementUuid);
        return this;
    }

    @Override
    public ISyncBinaryAsset setContentProvider(final String contentProviderKey) {
        super.setContentProvider(contentProviderKey);
        return this;
    }

    @Override
    public ISyncBinaryAsset setContentCategory(final String contentCategoryKey) {
        super.setContentCategory(contentCategoryKey);
        return this;
    }

    @Override
    public ISyncBinaryAsset setDescription(final Map<Locale, String> description) {
        super.setDescription(description);
        return this;
    }

    @Override
    public ISyncBinaryAsset setSmintIoUrl(final URL smintIoUrl) {
        super.setSmintIoUrl(smintIoUrl);
        return this;
    }


    @Override
    public ISyncBinaryAsset setCreatedAt(final OffsetDateTime createdAt) {
        super.setCreatedAt(createdAt);
        return this;
    }


    @Override
    public ISyncBinaryAsset setLastUpdatedAt(final OffsetDateTime lastUpdatedAt) {
        super.setLastUpdatedAt(lastUpdatedAt);
        return this;
    }


    @Override
    public ISyncBinaryAsset setPurchasedAt(final OffsetDateTime purchasedAt) {
        super.setPurchasedAt(purchasedAt);
        return this;
    }


    @Override
    public ISyncBinaryAsset setCartPurchaseTransactionUuid(final String cartPurchaseTransactionUuid) {
        super.setCartPurchaseTransactionUuid(cartPurchaseTransactionUuid);
        return this;
    }


    @Override
    public ISyncBinaryAsset setHasBeenCancelled(final boolean hasBeenCancelled) {
        super.setHasBeenCancelled(hasBeenCancelled);
        return this;
    }


    @Override
    public ISyncBinaryAsset setProjectUuid(final String projectUuid) {
        super.setProjectUuid(projectUuid);
        return this;
    }


    @Override
    public ISyncBinaryAsset setProjectName(final Map<Locale, String> projectName) {
        super.setProjectName(projectName);
        return this;
    }


    @Override
    public ISyncBinaryAsset setCollectionUuid(final String collectionUuid) {
        super.setCollectionUuid(collectionUuid);
        return this;
    }


    @Override
    public ISyncBinaryAsset setCollectionName(final Map<Locale, String> collectionName) {
        super.setCollectionName(collectionName);
        return this;
    }


    @Override
    public ISyncBinaryAsset setKeywords(final Map<Locale, String[]> keywords) {
        super.setKeywords(keywords);
        return this;
    }


    @Override
    public ISyncBinaryAsset setCopyrightNotices(final Map<Locale, String> copyrightNotices) {
        super.setCopyrightNotices(copyrightNotices);
        return this;
    }


    @Override
    public ISyncBinaryAsset setIsEditorialUse(final Boolean isEditorialUse) {
        super.setIsEditorialUse(isEditorialUse);
        return this;
    }


    @Override
    public ISyncBinaryAsset setHasLicenseTerms(final boolean hasLicenseTerms) {
        super.setHasLicenseTerms(hasLicenseTerms);
        return this;
    }


    @Override
    public ISyncBinaryAsset setLicenseType(final String licenseTypeKey) {
        super.setLicenseType(licenseTypeKey);
        return this;
    }


    @Override
    public ISyncBinaryAsset setLicenseeUuid(final String licenseeUuid) {
        super.setLicenseeUuid(licenseeUuid);
        return this;
    }


    @Override
    public ISyncBinaryAsset setLicenseeName(final String licenseeName) {
        super.setLicenseeName(licenseeName);
        return this;
    }


    @Override
    public ISyncBinaryAsset setLicenseText(final Map<Locale, String> licenseText) {
        super.setLicenseText(licenseText);
        return this;
    }


    @Override
    public ISyncBinaryAsset setLicenseOptions(final ISyncLicenseOption[] licenseOptions) {
        super.setLicenseOptions(licenseOptions);
        return this;
    }


    @Override
    public ISyncBinaryAsset setLicenseTerms(final ISyncLicenseTerm[] licenseTerms) {
        super.setLicenseTerms(licenseTerms);
        return this;
    }


    @Override
    public ISyncBinaryAsset setDownloadConstraints(final ISyncDownloadConstraints downloadConstraints) {
        super.setDownloadConstraints(downloadConstraints);
        return this;
    }


    @Override
    public ISyncBinaryAsset setReleaseDetails(final ISyncReleaseDetails releaseDetails) {
        super.setReleaseDetails(releaseDetails);
        return this;
    }
}

// CHECKSTYLE ON: MethodCount
