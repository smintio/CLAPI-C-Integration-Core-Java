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

import java.util.Hashtable;
import java.util.Map;
import java.util.Objects;

import io.smint.clapi.consumer.integration.core.contracts.ISmintIoMetadataElement;
import io.smint.clapi.consumer.integration.core.jobs.ISyncMetadataIdMapper;


/**
 * Manages various in-memory caches (hash tables) for mapping meta data's Smint.io API id to sync target ID - one-way!
 *
 * <p>
 * This instance should be easily converted to and from JSON to make the cache persistent. No instances are stored, only
 * IDs of type {@code String}.
 * </p>
 */
public class DefaultSyncMetadataIdMapperImpl implements ISyncMetadataIdMapper {


    private final Map<String, String> _contentProviderIDs = new Hashtable<>();
    private final Map<String, String> _contentCategoryIDs = new Hashtable<>();
    private final Map<String, String> _contentTypeIDs = new Hashtable<>();
    private final Map<String, String> _contentBinaryTypeIDs = new Hashtable<>();
    private final Map<String, String> _licenseTypeIDs = new Hashtable<>();
    private final Map<String, String> _licenseDistributionIDs = new Hashtable<>();
    private final Map<String, String> _licenseGeographyIDs = new Hashtable<>();
    private final Map<String, String> _licenseIndustryIDs = new Hashtable<>();
    private final Map<String, String> _licenseExclusivityIDs = new Hashtable<>();
    private final Map<String, String> _licenseLanguageIDs = new Hashtable<>();
    private final Map<String, String> _licensePlacementIDs = new Hashtable<>();
    private final Map<String, String> _licenseSizesIDs = new Hashtable<>();
    private final Map<String, String> _licenseUsagesIDs = new Hashtable<>();
    private final Map<String, String> _licenseUsageLimitIDs = new Hashtable<>();
    private final Map<String, String> _licenseReleaseStateIDs = new Hashtable<>();


    @Override
    public ISyncMetadataIdMapper clearMapping() {

        this._contentProviderIDs.clear();
        this._contentCategoryIDs.clear();
        this._contentTypeIDs.clear();
        this._contentBinaryTypeIDs.clear();
        this._licenseTypeIDs.clear();
        this._licenseDistributionIDs.clear();
        this._licenseGeographyIDs.clear();
        this._licenseIndustryIDs.clear();
        this._licenseExclusivityIDs.clear();
        this._licenseLanguageIDs.clear();
        this._licensePlacementIDs.clear();
        this._licenseSizesIDs.clear();
        this._licenseUsagesIDs.clear();
        this._licenseUsageLimitIDs.clear();
        this._licenseReleaseStateIDs.clear();

        return this;
    }


    @Override
    public boolean isEmpty() {
        return this._contentProviderIDs.isEmpty()
            && this._contentCategoryIDs.isEmpty()
            && this._contentTypeIDs.isEmpty()
            && this._contentBinaryTypeIDs.isEmpty()
            && this._licenseTypeIDs.isEmpty()
            && this._licenseDistributionIDs.isEmpty()
            && this._licenseGeographyIDs.isEmpty()
            && this._licenseIndustryIDs.isEmpty()
            && this._licenseExclusivityIDs.isEmpty()
            && this._licenseLanguageIDs.isEmpty()
            && this._licensePlacementIDs.isEmpty()
            && this._licenseSizesIDs.isEmpty()
            && this._licenseUsagesIDs.isEmpty()
            && this._licenseUsageLimitIDs.isEmpty()
            && this._licenseReleaseStateIDs.isEmpty();
    }


    @Override
    public ISyncMetadataIdMapper addMappingOfContentProviders(final ISmintIoMetadataElement[] metaDataElements) {
        return this.addMapping(this._contentProviderIDs, metaDataElements);
    }


    @Override
    public String getContentProviderId(final String smintIoId) {
        return this.getTargetId(this._contentProviderIDs, smintIoId);
    }


    @Override
    public ISyncMetadataIdMapper addMappingOfContentTypes(final ISmintIoMetadataElement[] metaDataElements) {
        return this.addMapping(this._contentTypeIDs, metaDataElements);
    }


    @Override
    public String getContentTypeId(final String smintIoId) {
        return this.getTargetId(this._contentTypeIDs, smintIoId);
    }


    @Override
    public ISyncMetadataIdMapper addMappingOfBinaryTypes(final ISmintIoMetadataElement[] metaDataElements) {
        return this.addMapping(this._contentBinaryTypeIDs, metaDataElements);
    }


    @Override
    public String getBinaryTypeId(final String smintIoId) {
        return this.getTargetId(this._contentBinaryTypeIDs, smintIoId);
    }


    @Override
    public ISyncMetadataIdMapper addMappingOfContentCategories(final ISmintIoMetadataElement[] metaDataElements) {
        return this.addMapping(this._contentCategoryIDs, metaDataElements);
    }


    @Override
    public String getContentCategoryId(final String smintIoId) {
        return this.getTargetId(this._contentCategoryIDs, smintIoId);
    }


    @Override
    public ISyncMetadataIdMapper addMappingOfLicenseTypes(final ISmintIoMetadataElement[] metaDataElements) {
        return this.addMapping(this._licenseTypeIDs, metaDataElements);
    }


    @Override
    public String getLicenseTypeId(final String smintIoId) {
        return this.getTargetId(this._licenseTypeIDs, smintIoId);
    }


    @Override
    public ISyncMetadataIdMapper addMappingOfReleaseStates(final ISmintIoMetadataElement[] metaDataElements) {
        return this.addMapping(this._licenseReleaseStateIDs, metaDataElements);
    }


    @Override
    public String getReleaseStateId(final String smintIoId) {
        return this.getTargetId(this._licenseReleaseStateIDs, smintIoId);
    }


    @Override
    public ISyncMetadataIdMapper addMappingOfLicenseExclusivities(final ISmintIoMetadataElement[] metaDataElements) {
        return this.addMapping(this._licenseExclusivityIDs, metaDataElements);
    }


    @Override
    public String getLicenseExclusivityId(final String smintIoId) {
        return this.getTargetId(this._licenseExclusivityIDs, smintIoId);
    }


    @Override
    public ISyncMetadataIdMapper addMappingOfLicenseUsages(final ISmintIoMetadataElement[] metaDataElements) {
        return this.addMapping(this._licenseUsagesIDs, metaDataElements);
    }


    @Override
    public String getLicenseUsageId(final String smintIoId) {
        return this.getTargetId(this._licenseUsagesIDs, smintIoId);
    }


    @Override
    public ISyncMetadataIdMapper addMappingOfLicenseSizes(final ISmintIoMetadataElement[] metaDataElements) {
        return this.addMapping(this._licenseSizesIDs, metaDataElements);
    }


    @Override
    public String getLicenseSizeId(final String smintIoId) {
        return this.getTargetId(this._licenseSizesIDs, smintIoId);
    }


    @Override
    public ISyncMetadataIdMapper addMappingOfLicensePlacements(final ISmintIoMetadataElement[] metaDataElements) {
        return this.addMapping(this._licensePlacementIDs, metaDataElements);
    }


    @Override
    public String getLicensePlacementId(final String smintIoId) {
        return this.getTargetId(this._licensePlacementIDs, smintIoId);
    }


    @Override
    public ISyncMetadataIdMapper addMappingOfLicenseDistributions(final ISmintIoMetadataElement[] metaDataElements) {
        return this.addMapping(this._licenseDistributionIDs, metaDataElements);
    }


    @Override
    public String getLicenseDistributionId(final String smintIoId) {
        return this.getTargetId(this._licenseDistributionIDs, smintIoId);
    }


    @Override
    public ISyncMetadataIdMapper addMappingOfLicenseGeographies(final ISmintIoMetadataElement[] metaDataElements) {
        return this.addMapping(this._licenseGeographyIDs, metaDataElements);
    }


    @Override
    public String getLicenseGeographyId(final String smintIoId) {
        return this.getTargetId(this._licenseGeographyIDs, smintIoId);
    }


    @Override
    public ISyncMetadataIdMapper addMappingOfLicenseIndustries(final ISmintIoMetadataElement[] metaDataElements) {
        return this.addMapping(this._licenseIndustryIDs, metaDataElements);
    }


    @Override
    public String getLicenseIndustryId(final String smintIoId) {
        return this.getTargetId(this._licenseIndustryIDs, smintIoId);
    }


    @Override
    public ISyncMetadataIdMapper addMappingOfLicenseLanguages(final ISmintIoMetadataElement[] metaDataElements) {
        return this.addMapping(this._licenseLanguageIDs, metaDataElements);
    }


    @Override
    public String getLicenseLanguageId(final String smintIoId) {
        return this.getTargetId(this._licenseLanguageIDs, smintIoId);
    }


    @Override
    public ISyncMetadataIdMapper addMappingOfLicenseUsageLimits(final ISmintIoMetadataElement[] metaDataElements) {
        return this.addMapping(this._licenseUsageLimitIDs, metaDataElements);
    }


    @Override
    public String getLicenseUsageLimitId(final String smintIoId) {
        return this.getTargetId(this._licenseUsageLimitIDs, smintIoId);
    }


    private ISyncMetadataIdMapper addMapping(
        final Map<String, String> cache, final ISmintIoMetadataElement[] metaDataElements
    ) {

        Objects.requireNonNull(cache, "No mapping cache has been provided!");

        if (metaDataElements != null) {
            for (final ISmintIoMetadataElement element : metaDataElements) {

                if (element == null) {
                    continue;
                }

                Objects.requireNonNull(element.getKey(), "Meta data element does not contain a Smint.io API ID!");
                Objects.requireNonNull(
                    element.getTargetMetadataUuid(), "Meta data element does not contain a sync target ID!"
                );

                cache.put(element.getKey(), element.getTargetMetadataUuid());
            }
        }

        return this;
    }


    private String getTargetId(final Map<String, String> cache, final String smintIoApiId) {
        Objects.requireNonNull(cache, "No mapping cache has been provided!");
        return smintIoApiId != null ? cache.get(smintIoApiId) : null;
    }
}
