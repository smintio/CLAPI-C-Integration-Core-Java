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

import io.smint.clapi.consumer.integration.core.target.ISyncAsset;
import io.smint.clapi.consumer.integration.core.target.ISyncDownloadConstraints;
import io.smint.clapi.consumer.integration.core.target.ISyncLicenseOption;
import io.smint.clapi.consumer.integration.core.target.ISyncLicenseTerm;
import io.smint.clapi.consumer.integration.core.target.ISyncReleaseDetails;


// CHECKSTYLE OFF: MethodCount

/**
 * JSON based container for asset data.
 */
public abstract class SyncAssetJsonImpl extends BaseSyncDataTypeJson implements ISyncAsset {


    public static final String JSON_KEY__UUID = "uuid";
    public static final String JSON_KEY__TARGET_ASSET_UUID = "propertyReleaseState";
    public static final String JSON_KEY__ASSET_NAME = "name";
    public static final String JSON_KEY__DESCRIPTION = "description";
    public static final String JSON_KEY__CONTENT_ELEMENT_UUID = "contentElementUuid";
    public static final String JSON_KEY__CONTENT_TYPE = "contentType";
    public static final String JSON_KEY__CONTENT_PROVIDER = "contentProvider";
    public static final String JSON_KEY__CONTENT_CATEGORY = "category";
    public static final String JSON_KEY__SMINT_IO_URL = "smintIoUrl";
    public static final String JSON_KEY__CREATE_AT = "createdAt";
    public static final String JSON_KEY__LAST_UPDATED_AT = "lastUpdatedAt";
    public static final String JSON_KEY__PURCHASED_AT = "purchasedAt";
    public static final String JSON_KEY__CART_PURCHASE_TRANSACTION_UUID = "cartPurchaseTransactionUuid";
    public static final String JSON_KEY__HAS_BEEN_CANCELLED = "hasBeenCancelled";

    public static final String JSON_KEY__PROJECT_UUID = "projectUuid";
    public static final String JSON_KEY__PROJECT_NAME = "projectName";
    public static final String JSON_KEY__COLLECTION_UUID = "collectionUuid";
    public static final String JSON_KEY__COLLECTION_NAME = "collectionName";

    public static final String JSON_KEY__KEYWORDS = "keywords";
    public static final String JSON_KEY__COPYRIGHT_NOTICE = "copyrightNotices";
    public static final String JSON_KEY__IS_EDITORIAL_USE = "isEditorialUse";
    public static final String JSON_KEY__HAS_LICENSE_TERM = "hasLicenseTerms";
    public static final String JSON_KEY__LICENSE_TYPE = "licenseType";
    public static final String JSON_KEY__LICENSEE_UUID = "licenseeUuid";
    public static final String JSON_KEY__LICENSEE_NAME = "licenseeName";

    public static final String JSON_KEY__LICENSE_TEXT = "licenseText";
    public static final String JSON_KEY__LICENSE_OPTION = "licenseOptions";
    public static final String JSON_KEY__LICENSE_TERMS = "licenseTerms";
    public static final String JSON_KEY__DOWNLOAD_CONSTRAINTS = "downloadConstraints";
    public static final String JSON_KEY__RELEASE_DETAILS = "releaseDetails";


    @Override
    public String getTransactionUuid() {
        return (String) this.getMetaData().get(JSON_KEY__UUID);
    }


    @Override
    public ISyncAsset setTransactionUuid(final String smintIoId) throws NullPointerException {
        this.putMetaDataValue(JSON_KEY__UUID, smintIoId);
        return this;
    }


    public String getTargetAssetUuid() {
        return (String) this.getMetaData().get(JSON_KEY__TARGET_ASSET_UUID);
    }


    @Override
    public ISyncAsset setTargetAssetUuid(final String targetAssetUuid) {
        this.putMetaDataValue(JSON_KEY__TARGET_ASSET_UUID, targetAssetUuid);
        return this;
    }


    @Override
    public ISyncAsset setName(final Map<Locale, String> name) {
        this.putMetaDataValue(JSON_KEY__ASSET_NAME, name);
        return this;
    }


    @Override
    public ISyncAsset setContentElementUuid(final String contentElementUuid) {
        this.putMetaDataValue(JSON_KEY__CONTENT_ELEMENT_UUID, contentElementUuid);
        return this;
    }


    @Override
    public ISyncAsset setContentType(final String contentTypeKey) {
        this.putMetaDataValue(JSON_KEY__CONTENT_TYPE, contentTypeKey);
        return this;
    }


    @Override
    public ISyncAsset setContentProvider(final String contentProviderKey) {
        this.putMetaDataValue(JSON_KEY__CONTENT_PROVIDER, contentProviderKey);
        return this;
    }


    @Override
    public ISyncAsset setContentCategory(final String contentCategoryKey) {
        this.putMetaDataValue(JSON_KEY__CONTENT_CATEGORY, contentCategoryKey);
        return this;
    }


    @Override
    public ISyncAsset setDescription(final Map<Locale, String> description) {
        this.putMetaDataValue(JSON_KEY__DESCRIPTION, description);
        return this;
    }


    @Override
    public ISyncAsset setSmintIoUrl(final URL smintIoUrl) {
        this.putMetaDataValue(JSON_KEY__SMINT_IO_URL, smintIoUrl);
        return this;
    }


    @Override
    public ISyncAsset setCreatedAt(final OffsetDateTime createdAt) {
        this.putMetaDataValue(JSON_KEY__CREATE_AT, createdAt);
        return this;
    }


    @Override
    public ISyncAsset setLastUpdatedAt(final OffsetDateTime lastUpdatedAt) {
        this.putMetaDataValue(JSON_KEY__LAST_UPDATED_AT, lastUpdatedAt);
        return this;
    }


    @Override
    public ISyncAsset setPurchasedAt(final OffsetDateTime purchasedAt) {
        this.putMetaDataValue(JSON_KEY__PURCHASED_AT, purchasedAt);
        return this;
    }


    @Override
    public ISyncAsset setCartPurchaseTransactionUuid(final String cartPurchaseTransactionUuid) {
        this.putMetaDataValue(JSON_KEY__CART_PURCHASE_TRANSACTION_UUID, cartPurchaseTransactionUuid);
        return this;
    }


    @Override
    public ISyncAsset setHasBeenCancelled(final boolean hasBeenCancelled) {
        this.putMetaDataValue(JSON_KEY__HAS_BEEN_CANCELLED, hasBeenCancelled);
        return this;
    }


    @Override
    public ISyncAsset setProjectUuid(final String projectUuid) {
        this.putMetaDataValue(JSON_KEY__PROJECT_UUID, projectUuid);
        return this;
    }


    @Override
    public ISyncAsset setProjectName(final Map<Locale, String> projectName) {
        this.putMetaDataValue(JSON_KEY__PROJECT_NAME, projectName);
        return this;
    }


    @Override
    public ISyncAsset setCollectionUuid(final String collectionUuid) {
        this.putMetaDataValue(JSON_KEY__COLLECTION_UUID, collectionUuid);
        return this;
    }


    @Override
    public ISyncAsset setCollectionName(final Map<Locale, String> collectionName) {
        this.putMetaDataValue(JSON_KEY__COLLECTION_NAME, collectionName);
        return this;
    }


    @Override
    public ISyncAsset setKeywords(final Map<Locale, String[]> keywords) {
        this.putMetaDataValue(JSON_KEY__KEYWORDS, keywords);
        return this;
    }


    @Override
    public ISyncAsset setCopyrightNotices(final Map<Locale, String> copyrightNotices) {
        this.putMetaDataValue(JSON_KEY__COPYRIGHT_NOTICE, copyrightNotices);
        return this;
    }


    @Override
    public ISyncAsset setIsEditorialUse(final Boolean isEditorialUse) {
        this.putMetaDataValue(JSON_KEY__IS_EDITORIAL_USE, isEditorialUse);
        return this;
    }


    @Override
    public ISyncAsset setHasLicenseTerms(final boolean hasLicenseTerms) {
        this.putMetaDataValue(JSON_KEY__HAS_LICENSE_TERM, hasLicenseTerms);
        return this;
    }


    @Override
    public ISyncAsset setLicenseType(final String licenseTypeKey) {
        this.putMetaDataValue(JSON_KEY__LICENSE_TYPE, licenseTypeKey);
        return this;
    }


    @Override
    public ISyncAsset setLicenseeUuid(final String licenseeUuid) {
        this.putMetaDataValue(JSON_KEY__LICENSEE_UUID, licenseeUuid);
        return this;
    }


    @Override
    public ISyncAsset setLicenseeName(final String licenseeName) {
        this.putMetaDataValue(JSON_KEY__LICENSEE_NAME, licenseeName);
        return this;
    }


    @Override
    public ISyncAsset setLicenseText(final Map<Locale, String> licenseText) {
        this.putMetaDataValue(JSON_KEY__LICENSE_TEXT, licenseText);
        return this;
    }


    @Override
    public ISyncAsset setLicenseOptions(final ISyncLicenseOption[] licenseOptions) {
        this.putMetaDataValue(JSON_KEY__LICENSE_OPTION, licenseOptions);
        return this;
    }


    @Override
    public ISyncAsset setLicenseTerms(final ISyncLicenseTerm[] licenseTerms) {
        this.putMetaDataValue(JSON_KEY__LICENSE_TERMS, licenseTerms);
        return this;
    }


    @Override
    public ISyncAsset setDownloadConstraints(final ISyncDownloadConstraints downloadConstraints) {
        this.putMetaDataValue(JSON_KEY__DOWNLOAD_CONSTRAINTS, downloadConstraints);
        return this;
    }


    @Override
    public ISyncAsset setReleaseDetails(final ISyncReleaseDetails releaseDetails) {
        this.putMetaDataValue(JSON_KEY__RELEASE_DETAILS, releaseDetails);
        return this;
    }


}

// CHECKSTYLE ON: MethodCount
