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
 * Represents a <em>Simple Asset</em> containing a single binary asset data.
 */
public class SyncBinaryAssetJsonImpl extends SyncAssetJsonImpl implements ISyncBinaryAsset {


    public static final String JSON_KEY__RECOMMENDED_FILE_NAME = "recommendedFileName";
    public static final String JSON_KEY__DOWNLOAD_URL = "downloadUrl";

    public static final String JSON_KEY__BINARY_UUID = "binaryUuid";
    public static final String JSON_KEY__BINARY_TYPE = "binaryType";
    public static final String JSON_KEY__BINARY_LOCALE = "binaryLocale";
    public static final String JSON_KEY__BINARY_VERSION = "binaryVersion";
    public static final String JSON_KEY__BINARY_USAGE = "binaryUsage";

    private Provider<File> _downloadProvider = null;
    private File _downloadedFile = null;

    @Override
    public String getRecommendedFileName() {
        return (String) this.getMetaData().get(JSON_KEY__RECOMMENDED_FILE_NAME);
    }


    @Override
    public ISyncBinaryAsset setRecommendedFileName(final String fileName) {
        this.putMetaDataValue(JSON_KEY__RECOMMENDED_FILE_NAME, fileName);
        return this;
    }


    @Override
    public URL getDownloadUrl() {
        return (URL) this.getMetaData().get(JSON_KEY__DOWNLOAD_URL);
    }


    @Override
    public ISyncBinaryAsset setDownloadUrl(final URL downloadURL) {
        this.putMetaDataValue(JSON_KEY__DOWNLOAD_URL, downloadURL);
        return this;
    }


    @Override
    public File getDownloadedFile() throws FileNotFoundException, SmintIoAuthenticatorException {
        if (this._downloadedFile == null && this._downloadProvider != null) {
            this._downloadedFile = this._downloadProvider.get();
        }
        return this._downloadedFile;
    }


    @Override
    public ISyncBinaryAsset setDownloadedFileProvider(final Provider<File> downloadFileProvider) {
        this._downloadProvider = downloadFileProvider;
        return this;
    }


    @Override
    public String getBinaryUuid() {
        return (String) this.getMetaData().get(JSON_KEY__BINARY_UUID);
    }


    @Override
    public ISyncBinaryAsset setBinaryUuid(final String binaryUuid) {
        this.putMetaDataValue(JSON_KEY__BINARY_UUID, binaryUuid);
        return this;
    }


    @Override
    public ISyncBinaryAsset setBinaryType(final String binaryTypeKey) {
        this.putMetaDataValue(JSON_KEY__BINARY_TYPE, binaryTypeKey);
        return this;
    }


    @Override
    public ISyncBinaryAsset setBinaryLocale(final Locale binaryLocale) {
        this.putMetaDataValue(JSON_KEY__BINARY_LOCALE, binaryLocale);
        return this;
    }


    @Override
    public int getBinaryVersion() {
        final Object version = this.getMetaData().get(JSON_KEY__BINARY_VERSION);
        return version == null ? 0 : ((Integer) version).intValue();
    }


    @Override
    public ISyncBinaryAsset setBinaryVersion(final int binaryVersion) {
        this.putMetaDataValue(JSON_KEY__BINARY_UUID, new Integer(binaryVersion));
        return this;
    }


    @SuppressWarnings("unchecked")
    @Override
    public Map<Locale, String> getBinaryUsage() {
        return (Map<Locale, String>) this.getMetaData().get(JSON_KEY__BINARY_USAGE);
    }


    @Override
    public ISyncBinaryAsset setBinaryUsage(final Map<Locale, String> binaryUsage) {
        this.putMetaDataValue(JSON_KEY__BINARY_USAGE, binaryUsage);
        return this;
    }


    @Override
    public boolean isCompoundAsset() {
        return false;
    }


    @Override
    public ISyncBinaryAsset setContentType(final String contentTypeKey) {
        super.setContentType(contentTypeKey);
        return this;
    }

    @Override
    public ISyncBinaryAsset setUuid(String smintIoId) {
        super.setUuid(smintIoId);
        return this;
    }

    @Override
    public ISyncBinaryAsset setTargetAssetUuid(String targetAssetUuid) {
        super.setTargetAssetUuid(targetAssetUuid);
        return this;
    }

    @Override
    public ISyncBinaryAsset setName(Map<Locale, String> name) {
        super.setName(name);
        return this;
    }

    @Override
    public ISyncBinaryAsset setContentElementUuid(String contentElementUuid) {
        super.setContentElementUuid(contentElementUuid);
        return this;
    }

    @Override
    public ISyncBinaryAsset setContentProvider(String contentProviderKey) {
        super.setContentProvider(contentProviderKey);
        return this;
    }

    @Override
    public ISyncBinaryAsset setContentCategory(String contentCategoryKey) {
        super.setContentCategory(contentCategoryKey);
        return this;
    }

    @Override
    public ISyncBinaryAsset setDescription(Map<Locale, String> description) {
        super.setDescription(description);
        return this;
    }

    @Override
    public ISyncBinaryAsset setSmintIoUrl(URL smintIoUrl) {
        super.setSmintIoUrl(smintIoUrl);
        return this;
    }


    @Override
    public ISyncBinaryAsset setCreatedAt(OffsetDateTime createdAt) {
        super.setCreatedAt(createdAt);
        return this;
    }


    @Override
    public ISyncBinaryAsset setLastUpdatedAt(OffsetDateTime lastUpdatedAt) {
        super.setLastUpdatedAt(lastUpdatedAt);
        return this;
    }


    @Override
    public ISyncBinaryAsset setPurchasedAt(OffsetDateTime purchasedAt) {
        super.setPurchasedAt(purchasedAt);
        return this;
    }


    @Override
    public ISyncBinaryAsset setCartPurchaseTransactionUuid(String cartPurchaseTransactionUuid) {
        super.setCartPurchaseTransactionUuid(cartPurchaseTransactionUuid);
        return this;
    }


    @Override
    public ISyncBinaryAsset setLicensePurchaseTransactionUuid(String licensePurchaseTransactionUuid) {
        super.setLicensePurchaseTransactionUuid(licensePurchaseTransactionUuid);
        return this;
    }


    @Override
    public ISyncBinaryAsset setHasBeenCancelled(boolean hasBeenCancelled) {
        super.setHasBeenCancelled(hasBeenCancelled);
        return this;
    }


    @Override
    public ISyncBinaryAsset setProjectUuid(String projectUuid) {
        super.setProjectUuid(projectUuid);
        return this;
    }


    @Override
    public ISyncBinaryAsset setProjectName(Map<Locale, String> projectName) {
        super.setProjectName(projectName);
        return this;
    }


    @Override
    public ISyncBinaryAsset setCollectionUuid(String collectionUuid) {
        super.setCollectionUuid(collectionUuid);
        return this;
    }


    @Override
    public ISyncBinaryAsset setCollectionName(Map<Locale, String> collectionName) {
        super.setCollectionName(collectionName);
        return this;
    }


    @Override
    public ISyncBinaryAsset setKeywords(Map<Locale, String[]> keywords) {
        super.setKeywords(keywords);
        return this;
    }


    @Override
    public ISyncBinaryAsset setCopyrightNotices(Map<Locale, String> copyrightNotices) {
        super.setCopyrightNotices(copyrightNotices);
        return this;
    }


    @Override
    public ISyncBinaryAsset setIsEditorialUse(Boolean isEditorialUse) {
        super.setIsEditorialUse(isEditorialUse);
        return this;
    }


    @Override
    public ISyncBinaryAsset setHasLicenseTerms(boolean hasLicenseTerms) {
        super.setHasLicenseTerms(hasLicenseTerms);
        return this;
    }


    @Override
    public ISyncBinaryAsset setLicenseType(String licenseTypeKey) {
        super.setLicenseType(licenseTypeKey);
        return this;
    }


    @Override
    public ISyncBinaryAsset setLicenseeUuid(String licenseeUuid) {
        super.setLicenseeUuid(licenseeUuid);
        return this;
    }


    @Override
    public ISyncBinaryAsset setLicenseeName(String licenseeName) {
        super.setLicenseeName(licenseeName);
        return this;
    }


    @Override
    public ISyncBinaryAsset setLicenseText(Map<Locale, String> licenseText) {
        super.setLicenseText(licenseText);
        return this;
    }


    @Override
    public ISyncBinaryAsset setLicenseOptions(ISyncLicenseOption[] licenseOptions) {
        super.setLicenseOptions(licenseOptions);
        return this;
    }


    @Override
    public ISyncBinaryAsset setLicenseTerms(ISyncLicenseTerm[] licenseTerms) {
        super.setLicenseTerms(licenseTerms);
        return this;
    }


    @Override
    public ISyncBinaryAsset setDownloadConstraints(ISyncDownloadConstraints downloadConstraints) {
        super.setDownloadConstraints(downloadConstraints);
        return this;
    }


    @Override
    public ISyncBinaryAsset setReleaseDetails(ISyncReleaseDetails releaseDetails) {
        super.setReleaseDetails(releaseDetails);
        return this;
    }

}

// CHECKSTYLE ON: MethodCount
