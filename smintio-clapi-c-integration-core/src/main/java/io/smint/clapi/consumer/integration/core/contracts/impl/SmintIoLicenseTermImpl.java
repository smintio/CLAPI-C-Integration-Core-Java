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

import java.time.OffsetDateTime;
import java.util.Locale;
import java.util.Map;

import io.smint.clapi.consumer.integration.core.contracts.ISmintIoLicenseTerm;


// CHECKSTYLE OFF: MethodCount

/**
 * Provides informations for licenses on assets.
 */
public class SmintIoLicenseTermImpl implements ISmintIoLicenseTerm {


    private int _sequenceNumber = 0;
    private Map<Locale, String> _name = null;
    private String[] _exclusivities = null;
    private String[] _allowedUsages = null;
    private String[] _restrictedUsages = null;
    private String[] _allowedSizes = null;
    private String[] _restrictedSizes = null;
    private String[] _allowedPlacements = null;
    private String[] _restrictedPlacements = null;
    private String[] _allowedDistributions = null;
    private String[] _restrictedDistributions = null;
    private String[] _allowedGeographies = null;
    private String[] _restrictedGeographies = null;
    private String[] _allowedIndustries = null;
    private String[] _restrictedIndustries = null;
    private String[] _allowedLanguages = null;
    private String[] _restrictedLanguages = null;
    private String[] _usageLimits = null;
    private OffsetDateTime _validFrom = null;
    private OffsetDateTime _validUntil = null;
    private OffsetDateTime _toBeUsedUntil = null;

    // editorial use is more restricted, hence this is the default!
    private boolean _isEditorialUse = true;


    @Override
    public int getSequenceNumber() {
        return this._sequenceNumber;
    }


    /**
     * Sets a new value to SequenceNumber.
     *
     * @param newSequenceNumber new value to set.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    public SmintIoLicenseTermImpl setSequenceNumber(final int newSequenceNumber) {
        this._sequenceNumber = newSequenceNumber;
        return this;
    }

    @Override
    public Map<Locale, String> getName() {
        return this._name;
    }


    /**
     * Sets a new value to name.
     *
     * @param newName the new values to use.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    public SmintIoLicenseTermImpl setName(final Map<Locale, String> newName) {
        this._name = newName;
        return this;
    }

    @Override
    public String[] getExclusivities() {
        return this._exclusivities;
    }


    /**
     * Sets a new value to Exclusivities.
     *
     * @param newExclusivities new value to set.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    public SmintIoLicenseTermImpl setExclusivities(final String[] newExclusivities) {
        this._exclusivities = newExclusivities;
        return this;
    }


    @Override
    public String[] getAllowedUsages() {
        return this._allowedUsages;
    }


    /**
     * Sets a new value to AllowedUsages.
     *
     * @param newAllowedUsages new value to set.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    public SmintIoLicenseTermImpl setAllowedUsages(final String[] newAllowedUsages) {
        this._allowedUsages = newAllowedUsages;
        return this;
    }


    @Override
    public String[] getRestrictedUsages() {
        return this._restrictedUsages;
    }


    /**
     * Sets a new value to RestrictedUsages.
     *
     * @param newRestrictedUsages new value to set.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    public SmintIoLicenseTermImpl setRestrictedUsages(final String[] newRestrictedUsages) {
        this._restrictedUsages = newRestrictedUsages;
        return this;
    }


    @Override
    public String[] getAllowedSizes() {
        return this._allowedSizes;
    }


    /**
     * Sets a new value to AllowedSizes.
     *
     * @param newAllowedSizes new value to set.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    public SmintIoLicenseTermImpl setAllowedSizes(final String[] newAllowedSizes) {
        this._allowedSizes = newAllowedSizes;
        return this;
    }


    @Override
    public String[] getRestrictedSizes() {
        return this._restrictedSizes;
    }


    /**
     * Sets a new value to RestrictedSizes.
     *
     * @param newRestrictedSizes new value to set.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    public SmintIoLicenseTermImpl setRestrictedSizes(final String[] newRestrictedSizes) {
        this._restrictedSizes = newRestrictedSizes;
        return this;
    }


    @Override
    public String[] getAllowedPlacements() {
        return this._allowedPlacements;
    }


    /**
     * Sets a new value to AllowedPlacements.
     *
     * @param newAllowedPlacements new value to set.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    public SmintIoLicenseTermImpl setAllowedPlacements(final String[] newAllowedPlacements) {
        this._allowedPlacements = newAllowedPlacements;
        return this;
    }


    @Override
    public String[] getRestrictedPlacements() {
        return this._restrictedPlacements;
    }


    /**
     * Sets a new value to RestrictedPlacements.
     *
     * @param newRestrictedPlacements new value to set.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    public SmintIoLicenseTermImpl setRestrictedPlacements(final String[] newRestrictedPlacements) {
        this._restrictedPlacements = newRestrictedPlacements;
        return this;
    }


    @Override
    public String[] getAllowedDistributions() {
        return this._allowedDistributions;
    }


    /**
     * Sets a new value to AllowedDistributions.
     *
     * @param newAllowedDistributions new value to set.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    public SmintIoLicenseTermImpl setAllowedDistributions(final String[] newAllowedDistributions) {
        this._allowedDistributions = newAllowedDistributions;
        return this;
    }


    @Override
    public String[] getRestrictedDistributions() {
        return this._restrictedDistributions;
    }


    /**
     * Sets a new value to RestrictedDistributions.
     *
     * @param newRestrictedDistributions new value to set.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    public SmintIoLicenseTermImpl setRestrictedDistributions(final String[] newRestrictedDistributions) {
        this._restrictedDistributions = newRestrictedDistributions;
        return this;
    }


    @Override
    public String[] getAllowedGeographies() {
        return this._allowedGeographies;
    }


    /**
     * Sets a new value to AllowedGeographies.
     *
     * @param newAllowedGeographies new value to set.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    public SmintIoLicenseTermImpl setAllowedGeographies(final String[] newAllowedGeographies) {
        this._allowedGeographies = newAllowedGeographies;
        return this;
    }


    @Override
    public String[] getRestrictedGeographies() {
        return this._restrictedGeographies;
    }


    /**
     * Sets a new value to RestrictedGeographies.
     *
     * @param newRestrictedGeographies new value to set.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    public SmintIoLicenseTermImpl setRestrictedGeographies(final String[] newRestrictedGeographies) {
        this._restrictedGeographies = newRestrictedGeographies;
        return this;
    }


    @Override
    public String[] getAllowedIndustries() {
        return this._allowedIndustries;
    }


    /**
     * Sets a new value to AllowedIndustries.
     *
     * @param newAllowedIndustries new value to set.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    public SmintIoLicenseTermImpl setAllowedIndustries(final String[] newAllowedIndustries) {
        this._allowedIndustries = newAllowedIndustries;
        return this;
    }


    @Override
    public String[] getRestrictedIndustries() {
        return this._restrictedIndustries;
    }


    /**
     * Sets a new value to RestrictedIndustries.
     *
     * @param newRestrictedIndustries new value to set.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    public SmintIoLicenseTermImpl setRestrictedIndustries(final String[] newRestrictedIndustries) {
        this._restrictedIndustries = newRestrictedIndustries;
        return this;
    }


    @Override
    public String[] getAllowedLanguages() {
        return this._allowedLanguages;
    }


    /**
     * Sets a new value to AllowedLanguages.
     *
     * @param newAllowedLanguages new value to set.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    public SmintIoLicenseTermImpl setAllowedLanguages(final String[] newAllowedLanguages) {
        this._allowedLanguages = newAllowedLanguages;
        return this;
    }


    @Override
    public String[] getRestrictedLanguages() {
        return this._restrictedLanguages;
    }


    /**
     * Sets a new value to RestrictedLanguages.
     *
     * @param newRestrictedLanguages new value to set.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    public SmintIoLicenseTermImpl setRestrictedLanguages(final String[] newRestrictedLanguages) {
        this._restrictedLanguages = newRestrictedLanguages;
        return this;
    }


    @Override
    public String[] getUsageLimits() {
        return this._usageLimits;
    }


    /**
     * Sets a new value to UsageLimits.
     *
     * @param newUsageLimits new value to set.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    public SmintIoLicenseTermImpl setUsageLimits(final String[] newUsageLimits) {
        this._usageLimits = newUsageLimits;
        return this;
    }


    @Override
    public OffsetDateTime getValidFrom() {
        return this._validFrom;
    }

    /**
     * Sets a new value to ValidFrom.
     *
     * @param newValidFrom new value to set.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    public SmintIoLicenseTermImpl setValidFrom(final OffsetDateTime newValidFrom) {
        this._validFrom = newValidFrom;
        return this;
    }


    @Override
    public OffsetDateTime getValidUntil() {
        return this._validUntil;
    }


    /**
     * Sets a new value to ValidUntil.
     *
     * @param newValidUntil new value to set.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    public SmintIoLicenseTermImpl setValidUntil(final OffsetDateTime newValidUntil) {
        this._validUntil = newValidUntil;
        return this;
    }


    @Override
    public OffsetDateTime getToBeUsedUntil() {
        return this._toBeUsedUntil;
    }


    /**
     * Sets a new value to ToBeUsedUntil.
     *
     * @param newToBeUsedUntil new value to set.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    public SmintIoLicenseTermImpl setToBeUsedUntil(final OffsetDateTime newToBeUsedUntil) {
        this._toBeUsedUntil = newToBeUsedUntil;
        return this;
    }


    @Override
    public boolean isEditorialUse() {
        return this._isEditorialUse;
    }


    /**
     * Sets whether this asset is for editorial use only.
     *
     * <p>
     * In case no value has been set, {@code true} is the default when retrieved with {@code #isEditorialUse()}.
     * </p>
     *
     * @param newIsEditorialUse new value to set.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    public SmintIoLicenseTermImpl setIsEditorialUse(final boolean newIsEditorialUse) {
        this._isEditorialUse = newIsEditorialUse;
        return this;
    }

}

// CHECKSTYLE ON: MethodCount
