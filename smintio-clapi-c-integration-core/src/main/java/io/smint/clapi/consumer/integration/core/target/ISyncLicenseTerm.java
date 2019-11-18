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

package io.smint.clapi.consumer.integration.core.target;

import java.time.OffsetDateTime;
import java.util.Locale;
import java.util.Map;


/**
 * Defines a license term composing restrictions and allowances.
 *
 * <p>
 * These constrains need to be stored with the sync target. There is no need to read these values, as they are NOT
 * compared to the values at Smint.io. Smint.io platform is the master source of these values and thus during
 * synchronization these value are only being set and not retrieved.
 * </p>
 *
 * <p>
 * Every instance is being created with {@link ISyncTarget#createSyncLicenseTerm()}.
 * </p>
 *
 * @see io.smint.clapi.consumer.integration.core.contracts.ISmintIoLicenseTerm
 */
public interface ISyncLicenseTerm extends ISyncDataType {

    /**
     * Sets the sequence number of this license term, which is unique only for the current tenant.
     *
     * <p>
     * License terms use a Smint.io sequence number to be unique for a single tenant. License that become invalid (see
     * {@link #setValidUntil(OffsetDateTime)}) are not deleted but kept for archiving reasons. New license terms are
     * created with a next available sequence number. There is no ID for license terms which is unique with Smint.io
     * platform.
     * </p>
     *
     * <p>
     * A sequence number is a positive value. All values below {@code 0} are considered invalid and must not be stored
     * to the sync target. Such values may be set for temporary use.
     * </p>
     *
     * @param sequenceNumber the new sequence number or equal to below {@code 0}.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    ISyncLicenseTerm setSequenceNumber(final int sequenceNumber);


    /**
     * Sets a localized name for this license term.
     *
     * @param name the new localized name or {@code null}.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    ISyncLicenseTerm setName(final Map<Locale, String> name);


    /**
     * Sets a list of exclusive rights that are granted and on that the restrictions in this instance applies to.
     *
     * <p>
     * Each value is a sync target platform specific ID, mapped from Smint.io key by
     * {@link ISyncTarget#getLicenseExclusivityKey(String)}.
     * </p>
     *
     * @param exclusivityKeys the sync target ID of exclusivity granted or {@code null}.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    ISyncLicenseTerm setExclusivities(final String[] exclusivityKeys);


    /**
     * Sets a list of allowed usages of the related asset.
     *
     * <p>
     * Each value is a sync target platform specific ID, mapped from Smint.io key by
     * {@link ISyncTarget#getLicenseUsageKey(String)}.
     * </p>
     *
     * @param usageKeys the sync target key to store or {@code null}.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    ISyncLicenseTerm setAllowedUsages(final String[] usageKeys);


    /**
     * Sets a list of restricted/denied usages of the related asset.
     *
     * <p>
     * Each value is a sync target platform specific ID, mapped from Smint.io key by
     * {@link ISyncTarget#getLicenseUsageKey(String)}.
     * </p>
     *
     * @param usageKeys the sync target key to store or {@code null}.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    ISyncLicenseTerm setRestrictedUsages(final String[] usageKeys);


    /**
     * Sets a list of allowed sizes for the related asset.
     *
     * <p>
     * Each value is a sync target platform specific ID, mapped from Smint.io key by
     * {@link ISyncTarget#getLicenseSizeKey(String)}.
     * </p>
     *
     * @param sizeKeys the sync target key to store or {@code null}.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    ISyncLicenseTerm setAllowedSizes(final String[] sizeKeys);


    /**
     * Sets a list of restricted sizes for the related asset.
     *
     * <p>
     * Each value is a sync target platform specific ID, mapped from Smint.io key by
     * {@link ISyncTarget#getLicenseSizeKey(String)}.
     * </p>
     *
     * @param sizeKeys the sync target key to store or {@code null}.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    ISyncLicenseTerm setRestrictedSizes(final String[] sizeKeys);


    /**
     * Sets a list of allowed placements for the related asset.
     *
     * <p>
     * Each value is a sync target platform specific ID, mapped from Smint.io key by
     * {@link ISyncTarget#getLicensePlacementKey(String)}.
     * </p>
     *
     * @param placementKeys the sync target key to store or {@code null}.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    ISyncLicenseTerm setAllowedPlacements(final String[] placementKeys);


    /**
     * Sets a list of restricted placements for the related asset.
     *
     * <p>
     * Each value is a sync target platform specific ID, mapped from Smint.io key by
     * {@link ISyncTarget#getLicensePlacementKey(String)}.
     * </p>
     *
     * @param placementKeys the sync target key to store or {@code null}.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    ISyncLicenseTerm setRestrictedPlacements(final String[] placementKeys);


    /**
     * Sets a list of allowed kind of distribution for the related asset.
     *
     * <p>
     * Each value is a sync target platform specific ID, mapped from Smint.io key by
     * {@link ISyncTarget#getLicenseDistributionKey(String)}.
     * </p>
     *
     * @param distributionKeys the sync target key to store or {@code null}.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    ISyncLicenseTerm setAllowedDistributions(final String[] distributionKeys);


    /**
     * Sets a list of restricted kind of distribution for the related asset.
     *
     * <p>
     * Each value is a sync target platform specific ID, mapped from Smint.io key by
     * {@link ISyncTarget#getLicenseDistributionKey(String)}.
     * </p>
     *
     * @param distributionKeys the sync target key to store or {@code null}.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    ISyncLicenseTerm setRestrictedDistributions(final String[] distributionKeys);


    /**
     * Sets a list of allowed geographic regions to use the related asset within.
     *
     * <p>
     * Each value is a sync target platform specific ID, mapped from Smint.io key by
     * {@link ISyncTarget#getLicenseGeographyKey(String)}.
     * </p>
     *
     * @param geographyKeys the sync target key to store or {@code null}.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    ISyncLicenseTerm setAllowedGeographies(final String[] geographyKeys);


    /**
     * Sets a list of restricted geographic regions for the related asset.
     *
     * <p>
     * Each value is a sync target platform specific ID, mapped from Smint.io key by
     * {@link ISyncTarget#getLicenseGeographyKey(String)}.
     * </p>
     *
     * @param geographyKeys the sync target key to store or {@code null}.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    ISyncLicenseTerm setRestrictedGeographies(final String[] geographyKeys);


    /**
     * Sets a list of allowed industries to use the related asset within.
     *
     * <p>
     * Each value is a sync target platform specific ID, mapped from Smint.io key by
     * {@link ISyncTarget#getLicenseIndustryKey(String)}.
     * </p>
     *
     * @param industryKeys the sync target key to store or {@code null}.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    ISyncLicenseTerm setAllowedIndustries(final String[] industryKeys);


    /**
     * Sets a list of restricted industries to use the related asset with.
     *
     * <p>
     * Each value is a sync target platform specific ID, mapped from Smint.io key by
     * {@link ISyncTarget#getLicenseIndustryKey(String)}.
     * </p>
     *
     * @param industryKeys the sync target key to store or {@code null}.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    ISyncLicenseTerm setRestrictedIndustries(final String[] industryKeys);


    /**
     * Sets a list of allowed languages to use the related asset with.
     *
     * <p>
     * Each value is a sync target platform specific ID, mapped from Smint.io key by
     * {@link ISyncTarget#getLicenseLanguageKey(String)}.
     * </p>
     *
     * @param languages the sync target key to store or {@code null}.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    ISyncLicenseTerm setAllowedLanguages(final String[] languages);


    /**
     * Sets a list of restricted language to use the related asset with.
     *
     * <p>
     * Each value is a sync target platform specific ID, mapped from Smint.io key by
     * {@link ISyncTarget#getLicenseLanguageKey(String)}.
     * </p>
     *
     * @param languages the sync target key to store or {@code null}.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    ISyncLicenseTerm setRestrictedLanguages(final String[] languages);


    /**
     * Sets a list of usage limits to apply to the usage of the related asset.
     *
     * <p>
     * Each value is a sync target platform specific ID, mapped from Smint.io key by
     * {@link ISyncTarget#getLicenseUsageLimitKey(String)}.
     * </p>
     *
     * @param usageLimitKeys the sync target key to store or {@code null}.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    ISyncLicenseTerm setUsageLimits(final String[] usageLimitKeys);


    /**
     * Sets the date when making use of the related asset can be started.
     *
     * @param validFrom the date this asset is allowed to be used after or {@code null} if no restriction apply.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    ISyncLicenseTerm setValidFrom(final OffsetDateTime validFrom);


    /**
     * Sets the date until making use of the related asset must be stopped.
     *
     * @param validUntil the date before this asset is allowed to be used but not after. {@code null} if no restrictions
     *                   apply.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    ISyncLicenseTerm setValidUntil(final OffsetDateTime validUntil);


    /**
     * Sets the date until making use of the related asset for the first time must be started.
     *
     * <p>
     * Some content providers prohibit <em>sharding</em>, meaning buying a lot of exclusive content but not making use
     * of it. This behavior would block others from exclusive content without effectively making use of the content
     * itself. Blocking other is the main achievement. So a licensor might require the licensee to make use of the asset
     * before a specific date.
     * </p>
     *
     * @param toBeUsedUntil the date before this asset must be used for the first time. {@code null} if no restrictions
     *                      apply.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    ISyncLicenseTerm setToBeUsedUntil(final OffsetDateTime toBeUsedUntil);


    /**
     * Sets a flag that this term restricts the use of the related asset to editorial usage.
     *
     * <p>
     * Until a value has been passed to this function, it can not be determined whether the asset is for editorial use
     * only or not. There might be some content provider that do not provide this information on some assets. It simply
     * is impossible to tell. The default assumption and its derived legal action depends on the synchronization target.
     * Smint.io can not give any advise how to handle such content.
     * </p>
     *
     * @param isEditorialUse {@link Boolean#TRUE} in case the term restricts for <em>Editorial Use</em> only and
     *                       {@link Boolean#FALSE} otherwise. If this can not be determined, than {@code null} is being
     *                       set.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    ISyncLicenseTerm setIsEditorialUse(final Boolean isEditorialUse);
}
