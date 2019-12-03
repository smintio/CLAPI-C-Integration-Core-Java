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

import io.smint.clapi.consumer.integration.core.contracts.ISmintIoGenericMetadata;
import io.smint.clapi.consumer.integration.core.contracts.ISmintIoMetadataElement;


/**
 * Pojo for generic meta data of assets, fetched from Smint.io API.
 */
public class SmintIoGenericMetadataImpl implements ISmintIoGenericMetadata {

    private ISmintIoMetadataElement[] _contentProviders = null;
    private ISmintIoMetadataElement[] _contentTypes = null;
    private ISmintIoMetadataElement[] _binaryTypes = null;
    private ISmintIoMetadataElement[] _contentCategories = null;
    private ISmintIoMetadataElement[] _licenseTypes = null;
    private ISmintIoMetadataElement[] _releaseStates = null;
    private ISmintIoMetadataElement[] _licenseExclusivities = null;
    private ISmintIoMetadataElement[] _licenseUsages = null;
    private ISmintIoMetadataElement[] _licenseSizes = null;
    private ISmintIoMetadataElement[] _licensePlacements = null;
    private ISmintIoMetadataElement[] _licenseDistributions = null;
    private ISmintIoMetadataElement[] _licenseGeographies = null;
    private ISmintIoMetadataElement[] _licenseIndustries = null;
    private ISmintIoMetadataElement[] _licenseLanguages = null;
    private ISmintIoMetadataElement[] _licenseUsageLimits = null;


    @Override
    public ISmintIoMetadataElement[] getContentProviders() {
        return this._contentProviders;
    }


    /**
     * Sets a new value as list of content providers.
     *
     * @param newContentProviders the value to set.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    public SmintIoGenericMetadataImpl setContentProviders(final ISmintIoMetadataElement[] newContentProviders) {
        this._contentProviders = newContentProviders;
        return this;
    }


    @Override
    public ISmintIoMetadataElement[] getContentTypes() {
        return this._contentTypes;
    }


    /**
     * Sets a new value to ContentTypes.
     *
     * @param newContentTypes a new list of values to set.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    public SmintIoGenericMetadataImpl setContentTypes(final ISmintIoMetadataElement[] newContentTypes) {
        this._contentTypes = newContentTypes;
        return this;
    }

    @Override
    public ISmintIoMetadataElement[] getBinaryTypes() {
        return this._binaryTypes;
    }


    /**
     * Sets a new value to BinaryTypes.
     *
     * @param newBinaryTypes a new list of values to set.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    public SmintIoGenericMetadataImpl setBinaryTypes(final ISmintIoMetadataElement[] newBinaryTypes) {
        this._binaryTypes = newBinaryTypes;
        return this;
    }

    @Override
    public ISmintIoMetadataElement[] getContentCategories() {
        return this._contentCategories;
    }


    /**
     * Sets a new value to ContentCategories.
     *
     * @param newContentCategories a new list of values to set.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    public SmintIoGenericMetadataImpl setContentCategories(final ISmintIoMetadataElement[] newContentCategories) {
        this._contentCategories = newContentCategories;
        return this;
    }

    @Override
    public ISmintIoMetadataElement[] getLicenseTypes() {
        return this._licenseTypes;
    }


    /**
     * Sets a new value to LicenseTypes.
     *
     * @param newLicenseTypes a new list of values to set.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    public SmintIoGenericMetadataImpl setLicenseTypes(final ISmintIoMetadataElement[] newLicenseTypes) {
        this._licenseTypes = newLicenseTypes;
        return this;
    }

    @Override
    public ISmintIoMetadataElement[] getReleaseStates() {
        return this._releaseStates;
    }


    /**
     * Sets a new value to ReleaseStates.
     *
     * @param newReleaseStates a new list of values to set.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    public SmintIoGenericMetadataImpl setReleaseStates(final ISmintIoMetadataElement[] newReleaseStates) {
        this._releaseStates = newReleaseStates;
        return this;
    }

    @Override
    public ISmintIoMetadataElement[] getLicenseExclusivities() {
        return this._licenseExclusivities;
    }


    /**
     * Sets a new value to LicenseExclusivities.
     *
     * @param newLicenseExclusivities a new list of values to set.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    public SmintIoGenericMetadataImpl setLicenseExclusivities(final ISmintIoMetadataElement[] newLicenseExclusivities) {
        this._licenseExclusivities = newLicenseExclusivities;
        return this;
    }

    @Override
    public ISmintIoMetadataElement[] getLicenseUsages() {
        return this._licenseUsages;
    }


    /**
     * Sets a new value to LicenseUsages.
     *
     * @param newLicenseUsages a new list of values to set.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    public SmintIoGenericMetadataImpl setLicenseUsages(final ISmintIoMetadataElement[] newLicenseUsages) {
        this._licenseUsages = newLicenseUsages;
        return this;
    }

    @Override
    public ISmintIoMetadataElement[] getLicenseSizes() {
        return this._licenseSizes;
    }


    /**
     * Sets a new value to LicenseSizes.
     *
     * @param newLicenseSizes a new list of values to set.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    public SmintIoGenericMetadataImpl setLicenseSizes(final ISmintIoMetadataElement[] newLicenseSizes) {
        this._licenseSizes = newLicenseSizes;
        return this;
    }

    @Override
    public ISmintIoMetadataElement[] getLicensePlacements() {
        return this._licensePlacements;
    }


    /**
     * Sets a new value to LicensePlacements.
     *
     * @param newLicensePlacements a new list of values to set.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    public SmintIoGenericMetadataImpl setLicensePlacements(final ISmintIoMetadataElement[] newLicensePlacements) {
        this._licensePlacements = newLicensePlacements;
        return this;
    }

    @Override
    public ISmintIoMetadataElement[] getLicenseDistributions() {
        return this._licenseDistributions;
    }


    /**
     * Sets a new value to LicenseDistributions.
     *
     * @param newLicenseDistributions a new list of values to set.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    public SmintIoGenericMetadataImpl setLicenseDistributions(final ISmintIoMetadataElement[] newLicenseDistributions) {
        this._licenseDistributions = newLicenseDistributions;
        return this;
    }

    @Override
    public ISmintIoMetadataElement[] getLicenseGeographies() {
        return this._licenseGeographies;
    }


    /**
     * Sets a new value to LicenseGeographies.
     *
     * @param newLicenseGeographies a new list of values to set.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    public SmintIoGenericMetadataImpl setLicenseGeographies(final ISmintIoMetadataElement[] newLicenseGeographies) {
        this._licenseGeographies = newLicenseGeographies;
        return this;
    }

    @Override
    public ISmintIoMetadataElement[] getLicenseIndustries() {
        return this._licenseIndustries;
    }


    /**
     * Sets a new value to LicenseIndustries.
     *
     * @param newLicenseIndustries a new list of values to set.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    public SmintIoGenericMetadataImpl setLicenseIndustries(final ISmintIoMetadataElement[] newLicenseIndustries) {
        this._licenseIndustries = newLicenseIndustries;
        return this;
    }

    @Override
    public ISmintIoMetadataElement[] getLicenseLanguages() {
        return this._licenseLanguages;
    }


    /**
     * Sets a new value to LicenseLanguages.
     *
     * @param newLicenseLanguages a new list of values to set.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    public SmintIoGenericMetadataImpl setLicenseLanguages(final ISmintIoMetadataElement[] newLicenseLanguages) {
        this._licenseLanguages = newLicenseLanguages;
        return this;
    }

    @Override
    public ISmintIoMetadataElement[] getLicenseUsageLimits() {
        return this._licenseUsageLimits;
    }


    /**
     * Sets a new value to LicenseUsageLimits.
     *
     * @param newLicenseUsageLimits a new list of values to set.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    public SmintIoGenericMetadataImpl setLicenseUsageLimits(final ISmintIoMetadataElement[] newLicenseUsageLimits) {
        this._licenseUsageLimits = newLicenseUsageLimits;
        return this;
    }
}
