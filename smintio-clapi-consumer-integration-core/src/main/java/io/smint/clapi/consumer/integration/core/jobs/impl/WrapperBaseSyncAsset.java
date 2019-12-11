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

import java.net.URL;
import java.time.OffsetDateTime;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import io.smint.clapi.consumer.integration.core.target.ISyncAsset;
import io.smint.clapi.consumer.integration.core.target.ISyncDownloadConstraints;
import io.smint.clapi.consumer.integration.core.target.ISyncLicenseOption;
import io.smint.clapi.consumer.integration.core.target.ISyncLicenseTerm;
import io.smint.clapi.consumer.integration.core.target.ISyncReleaseDetails;


// CHECKSTYLE OFF: MethodCount

/**
 * A wrapper for {@link ISyncAsset} instances to enrich the base implementation with some caching and values.
 *
 * <p>
 * In order to make it easier for synchronization target implementation, all data for assets are directly applied to an
 * asset instance, representing the data structure of the target system. These instances need to implement this
 * interface, too. Then the <em>Smint.io Integration Core Library</em> is able to pass all related data to it.
 * </p>
 *
 */
abstract class WrapperBaseSyncAsset<T extends ISyncAsset> implements ISyncAsset {


    private final T _wrapped;
    private String _uuid;
    private String _targetAssetUuid;
    private Map<Locale, String> _name;


    public WrapperBaseSyncAsset(final T assetToWrap) {
        this._wrapped = assetToWrap;

        Objects.requireNonNull(assetToWrap, "Invalid asset instance to wrap!");
    }


    public T getWrapped() {
        return this._wrapped;
    }


    /**
     * Provides the Smint.io platform ID for the asset.
     *
     * @return the Smint.io platform ID or {@code null} in case none has been set yet.
     */
    public String getUuid() {
        return this.getTransactionUuid();
    }


    /**
     * Sets a Smint.io platform ID for the asset.
     *
     * @param smintIoId the Smint.io platform ID to set for the asset.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     * @throws NullPointerException if parameter {@code smintIoId} is {@code null}.
     */
    public ISyncAsset setUuid(final String smintIoId) throws NullPointerException {
        this.setTransactionUuid(smintIoId);
        return this;
    }


    @Override
    public ISyncAsset setTransactionUuid(final String smintIoId) {
        this._uuid = smintIoId;
        this.getWrapped().setTransactionUuid(smintIoId);
        return this;
    }


    @Override
    public String getTransactionUuid() {
        if (this._uuid == null || this._uuid.isEmpty()) {
            this._uuid = this.getTransactionUuid();
        }
        return this._uuid;
    }


    @Override
    public ISyncAsset setTargetAssetUuid(final String targetAssetUuid) {
        this._targetAssetUuid = targetAssetUuid;
        this.getWrapped().setTargetAssetUuid(targetAssetUuid);
        return this;
    }


    /**
     * Provides the synchronization target's ID for this asset.
     *
     * <p>
     * Only assets that have already made persistent to the synchronization target have a <em>Target Asset UUID</em>.
     * This value is used by {@link io.smint.clapi.consumer.integration.core.jobs.ISyncJob} to determine, whether the
     * assets has ever been made persistent with the sync target or not. So newly created instances must not provide a
     * UUID unless they have already been persisted.
     * </p>
     *
     * @return the sync target's ID for this asset or {@code null} in case none has been set yet. It must be
     *         {@code null} if the asset has not been made persistent yet.
     */
    public String getTargetAssetUuid() {
        return this._targetAssetUuid;
    }


    /**
     * Provides the name for the asset, translated to multiple languages.
     *
     * <p>
     * The possible translations available are defined by the value of
     * {@link io.smint.clapi.consumer.integration.core.configuration.models.ISettingsModel#getImportLanguages()}.
     * However, not all languages might have a valid translation. Languages without a translation might be omitted from
     * the map. Especially if this is a persistent asset and a new language has been added to the list of languages.
     * Then the new language might have not yet been stored to this persistent asset.
     * </p>
     *
     * @return the multi-language name of the asset or {@code null} if none have been set yet.
     */
    public Map<Locale, String> getName() {
        return this._name;
    }


    @Override
    public ISyncAsset setName(final Map<Locale, String> name) {
        this._name = name;
        this.getWrapped().setName(name);
        return this;
    }


    @Override
    public boolean isCompoundAsset() {
        return this.getWrapped().isCompoundAsset();
    }


    @Override
    public ISyncAsset setContentElementUuid(final String contentElementUuid) {
        this.getWrapped().setContentElementUuid(contentElementUuid);
        return this;
    }


    @Override
    public ISyncAsset setContentType(final String contentTypeKey) {
        this.getWrapped().setContentType(contentTypeKey);
        return this;
    }


    @Override
    public ISyncAsset setContentProvider(final String contentProviderKey) {
        this.getWrapped().setContentProvider(contentProviderKey);
        return this;
    }


    @Override
    public ISyncAsset setContentCategory(final String contentCategoryKey) {
        this.getWrapped().setContentCategory(contentCategoryKey);
        return this;
    }


    @Override
    public ISyncAsset setDescription(final Map<Locale, String> description) {
        this.getWrapped().setDescription(description);
        return this;
    }


    @Override
    public ISyncAsset setSmintIoUrl(final URL smintIoUrl) {
        this.getWrapped().setSmintIoUrl(smintIoUrl);
        return this;
    }


    @Override
    public ISyncAsset setCreatedAt(final OffsetDateTime createdAt) {
        this.getWrapped().setCreatedAt(createdAt);
        return this;

    }


    @Override
    public ISyncAsset setLastUpdatedAt(final OffsetDateTime lastUpdatedAt) {
        this.getWrapped().setLastUpdatedAt(lastUpdatedAt);
        return this;
    }


    @Override
    public ISyncAsset setPurchasedAt(final OffsetDateTime purchasedAt) {
        this.getWrapped().setPurchasedAt(purchasedAt);
        return this;
    }


    @Override
    public ISyncAsset setCartPurchaseTransactionUuid(final String cartPurchaseTransactionUuid) {
        this.getWrapped().setCartPurchaseTransactionUuid(cartPurchaseTransactionUuid);
        return this;
    }


    @Override
    public ISyncAsset setHasBeenCancelled(final boolean hasBeenCancelled) {
        this.getWrapped().setHasBeenCancelled(hasBeenCancelled);
        return this;
    }


    @Override
    public ISyncAsset setProjectUuid(final String projectUuid) {
        this.getWrapped().setProjectUuid(projectUuid);
        return this;
    }


    @Override
    public ISyncAsset setProjectName(final Map<Locale, String> projectName) {
        this.getWrapped().setProjectName(projectName);
        return this;
    }


    @Override
    public ISyncAsset setCollectionUuid(final String collectionUuid) {
        this.getWrapped().setCollectionUuid(collectionUuid);
        return this;
    }


    @Override
    public ISyncAsset setCollectionName(final Map<Locale, String> collectionName) {
        this.getWrapped().setCollectionName(collectionName);
        return this;
    }


    @Override
    public ISyncAsset setKeywords(final Map<Locale, String[]> keywords) {
        this.getWrapped().setKeywords(keywords);
        return this;
    }


    @Override
    public ISyncAsset setCopyrightNotices(final Map<Locale, String> copyrightNotices) {
        this.getWrapped().setCopyrightNotices(copyrightNotices);
        return this;
    }


    @Override
    public ISyncAsset setIsEditorialUse(final Boolean isEditorialUse) {
        this.getWrapped().setIsEditorialUse(isEditorialUse);
        return this;
    }


    @Override
    public ISyncAsset setHasLicenseTerms(final boolean hasLicenseTerms) {
        this.getWrapped().setHasLicenseTerms(hasLicenseTerms);
        return this;
    }


    @Override
    public ISyncAsset setLicenseType(final String licenseTypeKey) {
        this.getWrapped().setLicenseType(licenseTypeKey);
        return this;
    }


    @Override
    public ISyncAsset setLicenseeUuid(final String licenseeUuid) {
        this.getWrapped().setLicenseeUuid(licenseeUuid);
        return this;
    }


    @Override
    public ISyncAsset setLicenseeName(final String licenseeName) {
        this.getWrapped().setLicenseeName(licenseeName);
        return this;
    }


    @Override
    public ISyncAsset setLicenseText(final Map<Locale, String> licenseText) {
        this.getWrapped().setLicenseText(licenseText);
        return this;
    }


    @Override
    public ISyncAsset setLicenseOptions(final ISyncLicenseOption[] licenseOptions) {
        this.getWrapped().setLicenseOptions(licenseOptions);
        return this;
    }


    @Override
    public ISyncAsset setLicenseTerms(final ISyncLicenseTerm[] licenseTerms) {
        this.getWrapped().setLicenseTerms(licenseTerms);
        return this;
    }


    @Override
    public ISyncAsset setDownloadConstraints(final ISyncDownloadConstraints downloadConstraints) {
        this.getWrapped().setDownloadConstraints(downloadConstraints);
        return this;
    }


    @Override
    public ISyncAsset setReleaseDetails(final ISyncReleaseDetails releaseDetails) {
        this.getWrapped().setReleaseDetails(releaseDetails);
        return this;
    }

}

// CHECKSTYLE ON: MethodCount
