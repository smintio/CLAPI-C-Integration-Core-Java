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

import java.net.URL;
import java.time.OffsetDateTime;
import java.util.Map;

import io.smint.clapi.consumer.generated.models.LicensePurchaseTransactionStateEnum;
import io.smint.clapi.consumer.integration.core.contracts.ISmintIoAsset;
import io.smint.clapi.consumer.integration.core.contracts.ISmintIoBinary;
import io.smint.clapi.consumer.integration.core.contracts.ISmintIoDownloadConstraints;
import io.smint.clapi.consumer.integration.core.contracts.ISmintIoLicenseOptions;
import io.smint.clapi.consumer.integration.core.contracts.ISmintIoLicenseTerm;
import io.smint.clapi.consumer.integration.core.contracts.ISmintIoReleaseDetails;


// CHECKSTYLE OFF: MethodCount

/**
 * Data for an asset to synchronize.
 */
public class SmintIoAssetImpl implements ISmintIoAsset {


    private String _contentElementUuid = null;
    private String _licensePurchaseTransactionUuid = null;
    private String _cartPurchaseTransactionUuid = null;
    private LicensePurchaseTransactionStateEnum _state = null;
    private String _provider = null;
    private String _contentType = null;
    private Map<String, String> _name = null;
    private Map<String, String> _description = null;
    private Map<String, String[]> _keywords = null;
    private String _category = null;
    private ISmintIoReleaseDetails _releaseDetails = null;
    private Map<String, String> _copyrightNotices = null;
    private String _projectUuid = null;
    private Map<String, String> _projectName = null;
    private String _collectionUuid = null;
    private Map<String, String> _collectionName = null;
    private String _licenseeUuid = null;
    private String _licenseeName = null;
    private String _licenseType = null;
    private Map<String, String> _licenseText = null;
    private ISmintIoLicenseOptions[] _licenseOptions = null;
    private ISmintIoLicenseTerm[] _licenseTerms = null;
    private ISmintIoDownloadConstraints _downloadConstraints = null;
    private Boolean _isEditorialUse = null;
    private boolean _hasLicenceTerms = false;
    private ISmintIoBinary[] _binaries = null;
    private URL _smintIoUrl = null;
    private OffsetDateTime _purchasedAt = null;
    private OffsetDateTime _createdAt = null;
    private OffsetDateTime _lastUpdatedAt = null;


    @Override
    public String getContentElementUuid() {
        return this._contentElementUuid;
    }


    /**
     * Sets a new value to ContentElementUuid.
     *
     * @param newContentElementUuid the new value to set.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    public SmintIoAssetImpl setContentElementUuid(final String newContentElementUuid) {
        this._contentElementUuid = newContentElementUuid;
        return this;
    }


    @Override
    public String getLicensePurchaseTransactionUuid() {
        return this._licensePurchaseTransactionUuid;
    }


    /**
     * Sets a new value to LicensePurchaseTransactionUuid.
     *
     * @param newLicensePurchaseTransactionUuid the new value to set.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    public SmintIoAssetImpl setLicensePurchaseTransactionUuid(final String newLicensePurchaseTransactionUuid) {
        this._licensePurchaseTransactionUuid = newLicensePurchaseTransactionUuid;
        return this;
    }


    @Override
    public String getCartPurchaseTransactionUuid() {
        return this._cartPurchaseTransactionUuid;
    }


    /**
     * Sets a new value to CartPurchaseTransactionUuid.
     *
     * @param newCartPurchaseTransactionUuid the new value to set.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    public SmintIoAssetImpl setCartPurchaseTransactionUuid(final String newCartPurchaseTransactionUuid) {
        this._cartPurchaseTransactionUuid = newCartPurchaseTransactionUuid;
        return this;
    }


    @Override
    public LicensePurchaseTransactionStateEnum getState() {
        return this._state;
    }


    /**
     * Sets a new value to State.
     *
     * @param newState the new value to set.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    public SmintIoAssetImpl setState(final LicensePurchaseTransactionStateEnum newState) {
        this._state = newState;
        return this;
    }


    @Override
    public String getProvider() {
        return this._provider;
    }


    /**
     * Sets a new value to Provider.
     *
     * @param newProvider the new value to set.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    public SmintIoAssetImpl setProvider(final String newProvider) {
        this._provider = newProvider;
        return this;
    }


    @Override
    public String getContentType() {
        return this._contentType;
    }


    /**
     * Sets a new value to ContentType.
     *
     * @param newContentType the new value to set.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    public SmintIoAssetImpl setContentType(final String newContentType) {
        this._contentType = newContentType;
        return this;
    }


    @Override
    public Map<String, String> getName() {
        return this._name;
    }


    /**
     * Sets a new value to Name.
     *
     * @param newName the new value to set.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    public SmintIoAssetImpl setName(final Map<String, String> newName) {
        this._name = newName;
        return this;
    }


    @Override
    public Map<String, String> getDescription() {
        return this._description;
    }


    /**
     * Sets a new value to Description.
     *
     * @param newDescription the new value to set.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    public SmintIoAssetImpl setDescription(final Map<String, String> newDescription) {
        this._description = newDescription;
        return this;
    }


    @Override
    public Map<String, String[]> getKeywords() {
        return this._keywords;
    }


    /**
     * Sets a new value to Keywords.
     *
     * @param newKeywords the new value to set.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    public SmintIoAssetImpl setKeywords(final Map<String, String[]> newKeywords) {
        this._keywords = newKeywords;
        return this;
    }


    @Override
    public String getCategory() {
        return this._category;
    }


    /**
     * Sets a new value to Category.
     *
     * @param newCategory the new value to set.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    public SmintIoAssetImpl setCategory(final String newCategory) {
        this._category = newCategory;
        return this;
    }


    @Override
    public ISmintIoReleaseDetails getReleaseDetails() {
        return this._releaseDetails;
    }


    /**
     * Sets a new value to ReleaseDetails.
     *
     * @param newReleaseDetails the new value to set.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    public SmintIoAssetImpl setReleaseDetails(final ISmintIoReleaseDetails newReleaseDetails) {
        this._releaseDetails = newReleaseDetails;
        return this;
    }


    @Override
    public Map<String, String> getCopyrightNotices() {
        return this._copyrightNotices;
    }


    /**
     * Sets a new value to CopyrightNotices.
     *
     * @param newCopyrightNotices the new value to set.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    public SmintIoAssetImpl setCopyrightNotices(final Map<String, String> newCopyrightNotices) {
        this._copyrightNotices = newCopyrightNotices;
        return this;
    }


    @Override
    public String getProjectUuid() {
        return this._projectUuid;
    }


    /**
     * Sets a new value to ProjectUuid.
     *
     * @param newProjectUuid the new value to set.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    public SmintIoAssetImpl setProjectUuid(final String newProjectUuid) {
        this._projectUuid = newProjectUuid;
        return this;
    }


    @Override
    public Map<String, String> getProjectName() {
        return this._projectName;
    }


    /**
     * Sets a new value to ProjectName.
     *
     * @param newProjectName the new value to set.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    public SmintIoAssetImpl setProjectName(final Map<String, String> newProjectName) {
        this._projectName = newProjectName;
        return this;
    }


    @Override
    public String getCollectionUuid() {
        return this._collectionUuid;
    }


    /**
     * Sets a new value to CollectionUuid.
     *
     * @param newCollectionUuid the new value to set.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    public SmintIoAssetImpl setCollectionUuid(final String newCollectionUuid) {
        this._collectionUuid = newCollectionUuid;
        return this;
    }


    @Override
    public Map<String, String> getCollectionName() {
        return this._collectionName;
    }


    /**
     * Sets a new value to CollectionName.
     *
     * @param newCollectionName the new value to set.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    public SmintIoAssetImpl setCollectionName(final Map<String, String> newCollectionName) {
        this._collectionName = newCollectionName;
        return this;
    }


    @Override
    public String getLicenseeUuid() {
        return this._licenseeUuid;
    }


    /**
     * Sets a new value to LicenseeUuid.
     *
     * @param newLicenseeUuid the new value to set.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    public SmintIoAssetImpl setLicenseeUuid(final String newLicenseeUuid) {
        this._licenseeUuid = newLicenseeUuid;
        return this;
    }


    @Override
    public String getLicenseeName() {
        return this._licenseeName;
    }


    /**
     * Sets a new value to LicenseeName.
     *
     * @param newLicenseeName the new value to set.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    public SmintIoAssetImpl setLicenseeName(final String newLicenseeName) {
        this._licenseeName = newLicenseeName;
        return this;
    }


    @Override
    public String getLicenseType() {
        return this._licenseType;
    }


    /**
     * Sets a new value to LicenseType.
     *
     * @param newLicenseType the new value to set.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    public SmintIoAssetImpl setLicenseType(final String newLicenseType) {
        this._licenseType = newLicenseType;
        return this;
    }


    @Override
    public Map<String, String> getLicenseText() {
        return this._licenseText;
    }


    /**
     * Sets a new value to LicenseText.
     *
     * @param newLicenseText the new value to set.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    public SmintIoAssetImpl setLicenseText(final Map<String, String> newLicenseText) {
        this._licenseText = newLicenseText;
        return this;
    }


    @Override
    public ISmintIoLicenseOptions[] getLicenseOptions() {
        return this._licenseOptions;
    }


    /**
     * Sets a new value to LicenseOptions.
     *
     * @param newLicenseOptions the new value to set.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    public SmintIoAssetImpl setLicenseOptions(final ISmintIoLicenseOptions[] newLicenseOptions) {
        this._licenseOptions = newLicenseOptions;
        return this;
    }


    @Override
    public ISmintIoLicenseTerm[] getLicenseTerms() {
        return this._licenseTerms;
    }


    /**
     * Sets a new value to LicenseTerms.
     *
     * @param newLicenseTerms the new value to set.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    public SmintIoAssetImpl setLicenseTerms(final ISmintIoLicenseTerm[] newLicenseTerms) {
        this._licenseTerms = newLicenseTerms;
        return this;
    }


    @Override
    public ISmintIoDownloadConstraints getDownloadConstraints() {
        return this._downloadConstraints;
    }


    /**
     * Sets a new value to DownloadConstraints.
     *
     * @param newDownloadConstraints the new value to set.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    public SmintIoAssetImpl setDownloadConstraints(final ISmintIoDownloadConstraints newDownloadConstraints) {
        this._downloadConstraints = newDownloadConstraints;
        return this;
    }


    @Override
    public Boolean isEditorialUse() {
        return this._isEditorialUse;
    }

    /**
     * Sets a new value to isEditorialUse.
     *
     * @param isEditorialUse the new value to set. In case no such information is available, then use {@code null}.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     * @see #isEditorialUse()
     */
    public SmintIoAssetImpl setIsEditorialUse(final Boolean isEditorialUse) {
        this._isEditorialUse = isEditorialUse;
        return this;
    }


    @Override
    public boolean hasLicenseTerms() {
        return this._hasLicenceTerms;
    }


    /**
     * Sets a new value to SmintIoUrl.
     *
     * @param newHasLicenceTerms the new value to set.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    public SmintIoAssetImpl setHasLicenseTerms(final boolean newHasLicenceTerms) {
        this._hasLicenceTerms = newHasLicenceTerms;
        return this;
    }


    @Override
    public ISmintIoBinary[] getBinaries() {
        return this._binaries;
    }


    /**
     * Sets a new value to Binaries.
     *
     * @param newBinaries the new value to set.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    public SmintIoAssetImpl setBinaries(final ISmintIoBinary[] newBinaries) {
        this._binaries = newBinaries;
        return this;
    }


    @Override
    public URL getSmintIoUrl() {
        return this._smintIoUrl;
    }


    /**
     * Sets a new value to SmintIoUrl.
     *
     * @param newSmintIoUrl the new value to set.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    public SmintIoAssetImpl setSmintIoUrl(final URL newSmintIoUrl) {
        this._smintIoUrl = newSmintIoUrl;
        return this;
    }


    @Override
    public OffsetDateTime getPurchasedAt() {
        return this._purchasedAt;
    }


    /**
     * Sets a new value to PurchasedAt.
     *
     * @param newPurchasedAt the new value to set.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    public SmintIoAssetImpl setPurchasedAt(final OffsetDateTime newPurchasedAt) {
        this._purchasedAt = newPurchasedAt;
        return this;
    }


    @Override
    public OffsetDateTime getCreatedAt() {
        return this._createdAt;
    }


    /**
     * Sets a new value to CreatedAt.
     *
     * @param newCreatedAt the new value to set.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    public SmintIoAssetImpl setCreatedAt(final OffsetDateTime newCreatedAt) {
        this._createdAt = newCreatedAt;
        return this;
    }


    @Override
    public OffsetDateTime getLastUpdatedAt() {
        return this._lastUpdatedAt;
    }


    /**
     * Sets a new value to LastUpdatedAt.
     *
     * @param newLastUpdatedAt the new value to set.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    public SmintIoAssetImpl setLastUpdatedAt(final OffsetDateTime newLastUpdatedAt) {
        this._lastUpdatedAt = newLastUpdatedAt;
        return this;
    }
}

// CHECKSTYLE ON: MethodCount
