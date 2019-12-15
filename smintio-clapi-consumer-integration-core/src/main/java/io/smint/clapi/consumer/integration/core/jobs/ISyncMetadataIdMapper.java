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

package io.smint.clapi.consumer.integration.core.jobs;

import io.smint.clapi.consumer.integration.core.contracts.ISmintIoMetadataElement;


/**
 * Implementing classes manage a cache for mapping meta data's Smint.io API id to sync target ID - one-way, though.
 *
 * <p>
 * All assets ({@link io.smint.clapi.consumer.integration.core.target.impl.BaseSyncAsset}) have some structured meta data.
 * Ideally the applied meta data are a bunch of references to the meta data definitions, stored elsewhere. Each storage
 * uses IDs to enable references to it. Since we have two storage system to keep in sync, there are two regimes of IDs,
 * the Smint.io API IDs and the sync target IDs. A mapping is required and this is what this cache is about. During
 * synchronizing of all meta data, this cache is filled and then kept available until the next meta data
 * synchronization, which rebuilds it anyway.
 * </p>
 *
 * <p>
 * The mapping is one-way only, from Smint.io API to sync target ID - no reverse mapping.
 * </p>
 */
public interface ISyncMetadataIdMapper {

    /**
     * Clears all the cache, removing all mapping information.
     *
     * @return {@code this}.
     */
    ISyncMetadataIdMapper clearMapping();


    /**
     * Provides information if any mapping exists.
     *
     * @return {@code true} if no mapping exists yet.
     */
    boolean isEmpty();


    /**
     * Adds mapping of Smint.io API IDs to sync target IDs for content providers.
     *
     * <p>
     * The Smint.io API ID is read from {@link ISmintIoMetadataElement#getKey()}, whereas the synchronization target ID
     * is read from {@link ISmintIoMetadataElement#getTargetMetadataUuid()}. Both must not be {@code null}. An exception
     * is thrown in case any meta data element lacks one of these values.
     * </p>
     *
     * <p>
     * In case one of the IDs already exist, it will override the previously existing mapping.
     * </p>
     *
     * @param metaDataElements the list of meta data elements to add to the mapping.
     * @return {@code this}.
     * @throws NullPointerException in case {@link ISmintIoMetadataElement#getKey()} or
     *                              {@link ISmintIoMetadataElement#getTargetMetadataUuid()} is {@code null} for any item
     *                              in the provided list.
     */
    ISyncMetadataIdMapper addMappingOfContentProviders(final ISmintIoMetadataElement[] metaDataElements);


    /**
     * Maps the Smint.io ID of a <em>Content Provider</em> to a synchronization target ID/key.
     *
     * <p>
     * Each available content provider in the Smint.io platform needs a corresponding representation of the target side.
     * This is necessary to track the origin of assets. Hence a one-to-one mapping is required. Therefore a direct
     * translation of the IDs on both platforms (Smint.io + sync target) is necessary.
     * </p>
     *
     * <p>
     * Implementation of {@link io.smint.clapi.consumer.integration.core.jobs.ISyncJob} will make use of this target ID
     * to provide prepared assets that can be easily compared to the data already available on the sync target.
     * </p>
     *
     * <p>
     * The Smint.io ID to map is similar to the value from
     * {@link io.smint.clapi.consumer.integration.core.contracts.ISmintIoAsset#getContentProvider()}.
     * </p>
     *
     * @param smintIoApiId the ID on the Smint.io platform.
     * @return the key on the synchronization target, or {@code null} in case the item does not exist yet on the target
     *         or if parameter the is {@code null}.
     * @see io.smint.clapi.consumer.integration.core.contracts.ISmintIoAsset#getContentProvider()
     */
    String getContentProviderId(String smintIoApiId);


    /**
     * Adds mapping of Smint.io API IDs to sync target IDs for content providers.
     *
     * <p>
     * The Smint.io API ID is read from {@link ISmintIoMetadataElement#getKey()}, whereas the synchronization target ID
     * is read from {@link ISmintIoMetadataElement#getTargetMetadataUuid()}. Both must not be {@code null}. An exception
     * is thrown in case any meta data element lacks one of these values.
     * </p>
     *
     * <p>
     * In case one of the IDs already exist, it will override the previously existing mapping.
     * </p>
     *
     * @param metaDataElements the list of meta data elements to add to the mapping.
     * @return {@code this}.
     * @throws NullPointerException in case {@link ISmintIoMetadataElement#getKey()} or
     *                              {@link ISmintIoMetadataElement#getTargetMetadataUuid()} is {@code null} for any item
     *                              in the provided list.
     */
    ISyncMetadataIdMapper addMappingOfContentTypes(final ISmintIoMetadataElement[] metaDataElements);


    /**
     * Maps the Smint.io ID of a <em>Content Type</em> to a synchronization target ID/key.
     *
     * <p>
     * The Smint.io ID to map is similar to the value from
     * {@link io.smint.clapi.consumer.integration.core.contracts.ISmintIoBinary#getContentType()}.
     * </p>
     *
     * @param smintIoContentTypeId the ID on the Smint.io platform.
     * @return the key on the synchronization target, or {@code null} in case the item does not exist yet on the target
     *         or if parameter the is {@code null}.
     * @see io.smint.clapi.consumer.integration.core.contracts.ISmintIoBinary#getContentType()
     */
    String getContentTypeId(final String smintIoContentTypeId);


    /**
     * Adds mapping of Smint.io API IDs to sync target IDs for content providers.
     *
     * <p>
     * The Smint.io API ID is read from {@link ISmintIoMetadataElement#getKey()}, whereas the synchronization target ID
     * is read from {@link ISmintIoMetadataElement#getTargetMetadataUuid()}. Both must not be {@code null}. An exception
     * is thrown in case any meta data element lacks one of these values.
     * </p>
     *
     * <p>
     * In case one of the IDs already exist, it will override the previously existing mapping.
     * </p>
     *
     * @param metaDataElements the list of meta data elements to add to the mapping.
     * @return {@code this}.
     * @throws NullPointerException in case {@link ISmintIoMetadataElement#getKey()} or
     *                              {@link ISmintIoMetadataElement#getTargetMetadataUuid()} is {@code null} for any item
     *                              in the provided list.
     */
    ISyncMetadataIdMapper addMappingOfBinaryTypes(final ISmintIoMetadataElement[] metaDataElements);


    /**
     * Maps the Smint.io ID of a <em>Binary Type</em> to a synchronization target ID/key.
     *
     * <p>
     * The Smint.io ID to map is similar to the value from
     * {@link io.smint.clapi.consumer.integration.core.contracts.ISmintIoBinary#getBinaryType()}.
     * </p>
     *
     * @param smintIoId the ID on the Smint.io platform.
     * @return the key on the synchronization target, or {@code null} in case the item does not exist yet on the target
     *         or if parameter the is {@code null}.
     * @see io.smint.clapi.consumer.integration.core.contracts.ISmintIoBinary#getBinaryType()
     */
    String getBinaryTypeId(final String smintIoId);


    /**
     * Adds mapping of Smint.io API IDs to sync target IDs for content providers.
     *
     * <p>
     * The Smint.io API ID is read from {@link ISmintIoMetadataElement#getKey()}, whereas the synchronization target ID
     * is read from {@link ISmintIoMetadataElement#getTargetMetadataUuid()}. Both must not be {@code null}. An exception
     * is thrown in case any meta data element lacks one of these values.
     * </p>
     *
     * <p>
     * In case one of the IDs already exist, it will override the previously existing mapping.
     * </p>
     *
     * @param metaDataElements the list of meta data elements to add to the mapping.
     * @return {@code this}.
     * @throws NullPointerException in case {@link ISmintIoMetadataElement#getKey()} or
     *                              {@link ISmintIoMetadataElement#getTargetMetadataUuid()} is {@code null} for any item
     *                              in the provided list.
     */
    ISyncMetadataIdMapper addMappingOfContentCategories(final ISmintIoMetadataElement[] metaDataElements);


    /**
     * Maps the Smint.io ID of a <em>Content Category</em> to a synchronization target ID/key.
     *
     * @param smintIoId the ID on the Smint.io platform.
     * @return the key on the synchronization target, or {@code null} in case the item does not exist yet on the target
     *         or if parameter the is {@code null}.
     */
    String getContentCategoryId(final String smintIoId);


    /**
     * Adds mapping of Smint.io API IDs to sync target IDs for content providers.
     *
     * <p>
     * The Smint.io API ID is read from {@link ISmintIoMetadataElement#getKey()}, whereas the synchronization target ID
     * is read from {@link ISmintIoMetadataElement#getTargetMetadataUuid()}. Both must not be {@code null}. An exception
     * is thrown in case any meta data element lacks one of these values.
     * </p>
     *
     * <p>
     * In case one of the IDs already exist, it will override the previously existing mapping.
     * </p>
     *
     * @param metaDataElements the list of meta data elements to add to the mapping.
     * @return {@code this}.
     * @throws NullPointerException in case {@link ISmintIoMetadataElement#getKey()} or
     *                              {@link ISmintIoMetadataElement#getTargetMetadataUuid()} is {@code null} for any item
     *                              in the provided list.
     */
    ISyncMetadataIdMapper addMappingOfLicenseTypes(final ISmintIoMetadataElement[] metaDataElements);


    /**
     * Maps the Smint.io ID of a <em>License Type</em> to a synchronization target ID/key.
     *
     * @param smintIoId the ID on the Smint.io platform.
     * @return the key on the synchronization target, or {@code null} in case the item does not exist yet on the target
     *         or if parameter the is {@code null}.
     */
    String getLicenseTypeId(final String smintIoId);


    /**
     * Adds mapping of Smint.io API IDs to sync target IDs for content providers.
     *
     * <p>
     * The Smint.io API ID is read from {@link ISmintIoMetadataElement#getKey()}, whereas the synchronization target ID
     * is read from {@link ISmintIoMetadataElement#getTargetMetadataUuid()}. Both must not be {@code null}. An exception
     * is thrown in case any meta data element lacks one of these values.
     * </p>
     *
     * <p>
     * In case one of the IDs already exist, it will override the previously existing mapping.
     * </p>
     *
     * @param metaDataElements the list of meta data elements to add to the mapping.
     * @return {@code this}.
     * @throws NullPointerException in case {@link ISmintIoMetadataElement#getKey()} or
     *                              {@link ISmintIoMetadataElement#getTargetMetadataUuid()} is {@code null} for any item
     *                              in the provided list.
     */
    ISyncMetadataIdMapper addMappingOfReleaseStates(final ISmintIoMetadataElement[] metaDataElements);


    /**
     * Maps the Smint.io ID of a <em>Release State</em> to a synchronization target ID/key.
     *
     * <p>
     * The Smint.io ID to map is similar to the value from
     * {@link io.smint.clapi.consumer.integration.core.contracts.ISmintIoReleaseDetails#getModelReleaseState()}.
     * </p>
     *
     * @param smintIoId the ID on the Smint.io platform.
     * @return the key on the synchronization target, or {@code null} in case the item does not exist yet on the target
     *         or if parameter the is {@code null}.
     * @see io.smint.clapi.consumer.integration.core.contracts.ISmintIoReleaseDetails#getModelReleaseState()
     */
    String getReleaseStateId(final String smintIoId);


    /**
     * Adds mapping of Smint.io API IDs to sync target IDs for content providers.
     *
     * <p>
     * The Smint.io API ID is read from {@link ISmintIoMetadataElement#getKey()}, whereas the synchronization target ID
     * is read from {@link ISmintIoMetadataElement#getTargetMetadataUuid()}. Both must not be {@code null}. An exception
     * is thrown in case any meta data element lacks one of these values.
     * </p>
     *
     * <p>
     * In case one of the IDs already exist, it will override the previously existing mapping.
     * </p>
     *
     * @param metaDataElements the list of meta data elements to add to the mapping.
     * @return {@code this}.
     * @throws NullPointerException in case {@link ISmintIoMetadataElement#getKey()} or
     *                              {@link ISmintIoMetadataElement#getTargetMetadataUuid()} is {@code null} for any item
     *                              in the provided list.
     */
    ISyncMetadataIdMapper addMappingOfLicenseExclusivities(final ISmintIoMetadataElement[] metaDataElements);


    /**
     * Maps the Smint.io ID of a license <em>Exclusivity</em> to a synchronization target ID/key.
     *
     * <p>
     * The Smint.io ID to map is similar to the value from
     * {@link io.smint.clapi.consumer.integration.core.contracts.ISmintIoLicenseTerm#getExclusivities()}.
     * </p>
     *
     * @param smintIoId the ID on the Smint.io platform or {@code null}.
     * @return the key on the synchronization target, or {@code null} in case the item does not exist yet on the target
     *         or the parameter is {@code null}.
     * @see io.smint.clapi.consumer.integration.core.contracts.ISmintIoLicenseTerm#getExclusivities()
     */
    String getLicenseExclusivityId(final String smintIoId);


    /**
     * Adds mapping of Smint.io API IDs to sync target IDs for content providers.
     *
     * <p>
     * The Smint.io API ID is read from {@link ISmintIoMetadataElement#getKey()}, whereas the synchronization target ID
     * is read from {@link ISmintIoMetadataElement#getTargetMetadataUuid()}. Both must not be {@code null}. An exception
     * is thrown in case any meta data element lacks one of these values.
     * </p>
     *
     * <p>
     * In case one of the IDs already exist, it will override the previously existing mapping.
     * </p>
     *
     * @param metaDataElements the list of meta data elements to add to the mapping.
     * @return {@code this}.
     * @throws NullPointerException in case {@link ISmintIoMetadataElement#getKey()} or
     *                              {@link ISmintIoMetadataElement#getTargetMetadataUuid()} is {@code null} for any item
     *                              in the provided list.
     */
    ISyncMetadataIdMapper addMappingOfLicenseUsages(final ISmintIoMetadataElement[] metaDataElements);


    /**
     * Maps the Smint.io ID of a <em>License Usage</em> to a synchronization target ID/key.
     *
     * <p>
     * The Smint.io ID to map is similar to the value from
     * {@link io.smint.clapi.consumer.integration.core.contracts.ISmintIoLicenseTerm#getAllowedUsages()}.
     * </p>
     *
     * @param smintIoId the ID on the Smint.io platform.
     * @return the key on the synchronization target, or {@code null} in case the item does not exist yet on the target
     *         or if parameter the is {@code null}.
     * @see io.smint.clapi.consumer.integration.core.contracts.ISmintIoLicenseTerm#getAllowedUsages()
     */
    String getLicenseUsageId(final String smintIoId);


    /**
     * Adds mapping of Smint.io API IDs to sync target IDs for content providers.
     *
     * <p>
     * The Smint.io API ID is read from {@link ISmintIoMetadataElement#getKey()}, whereas the synchronization target ID
     * is read from {@link ISmintIoMetadataElement#getTargetMetadataUuid()}. Both must not be {@code null}. An exception
     * is thrown in case any meta data element lacks one of these values.
     * </p>
     *
     * <p>
     * In case one of the IDs already exist, it will override the previously existing mapping.
     * </p>
     *
     * @param metaDataElements the list of meta data elements to add to the mapping.
     * @return {@code this}.
     * @throws NullPointerException in case {@link ISmintIoMetadataElement#getKey()} or
     *                              {@link ISmintIoMetadataElement#getTargetMetadataUuid()} is {@code null} for any item
     *                              in the provided list.
     */
    ISyncMetadataIdMapper addMappingOfLicenseSizes(final ISmintIoMetadataElement[] metaDataElements);


    /**
     * Maps the Smint.io ID of a <em>License Size</em> to a synchronization target ID/key.
     *
     * <p>
     * The Smint.io ID to map is similar to the value from
     * {@link io.smint.clapi.consumer.integration.core.contracts.ISmintIoLicenseTerm#getAllowedSizes()}.
     * </p>
     *
     * @param smintIoId the ID on the Smint.io platform.
     * @return the key on the synchronization target, or {@code null} in case the item does not exist yet on the target
     *         or if parameter the is {@code null}.
     * @see io.smint.clapi.consumer.integration.core.contracts.ISmintIoLicenseTerm#getAllowedSizes()
     */
    String getLicenseSizeId(final String smintIoId);


    /**
     * Adds mapping of Smint.io API IDs to sync target IDs for content providers.
     *
     * <p>
     * The Smint.io API ID is read from {@link ISmintIoMetadataElement#getKey()}, whereas the synchronization target ID
     * is read from {@link ISmintIoMetadataElement#getTargetMetadataUuid()}. Both must not be {@code null}. An exception
     * is thrown in case any meta data element lacks one of these values.
     * </p>
     *
     * <p>
     * In case one of the IDs already exist, it will override the previously existing mapping.
     * </p>
     *
     * @param metaDataElements the list of meta data elements to add to the mapping.
     * @return {@code this}.
     * @throws NullPointerException in case {@link ISmintIoMetadataElement#getKey()} or
     *                              {@link ISmintIoMetadataElement#getTargetMetadataUuid()} is {@code null} for any item
     *                              in the provided list.
     */
    ISyncMetadataIdMapper addMappingOfLicensePlacements(final ISmintIoMetadataElement[] metaDataElements);


    /**
     * Maps the Smint.io ID of a <em>License Placement</em> to a synchronization target ID/key.
     *
     * <p>
     * The Smint.io ID to map is similar to the value from
     * {@link io.smint.clapi.consumer.integration.core.contracts.ISmintIoLicenseTerm#getAllowedPlacements()}.
     * </p>
     *
     * @param smintIoId the ID on the Smint.io platform.
     * @return the key on the synchronization target, or {@code null} in case the item does not exist yet on the target
     *         or if parameter the is {@code null}.
     * @see io.smint.clapi.consumer.integration.core.contracts.ISmintIoLicenseTerm#getAllowedPlacements()
     */
    String getLicensePlacementId(final String smintIoId);


    /**
     * Adds mapping of Smint.io API IDs to sync target IDs for content providers.
     *
     * <p>
     * The Smint.io API ID is read from {@link ISmintIoMetadataElement#getKey()}, whereas the synchronization target ID
     * is read from {@link ISmintIoMetadataElement#getTargetMetadataUuid()}. Both must not be {@code null}. An exception
     * is thrown in case any meta data element lacks one of these values.
     * </p>
     *
     * <p>
     * In case one of the IDs already exist, it will override the previously existing mapping.
     * </p>
     *
     * @param metaDataElements the list of meta data elements to add to the mapping.
     * @return {@code this}.
     * @throws NullPointerException in case {@link ISmintIoMetadataElement#getKey()} or
     *                              {@link ISmintIoMetadataElement#getTargetMetadataUuid()} is {@code null} for any item
     *                              in the provided list.
     */
    ISyncMetadataIdMapper addMappingOfLicenseDistributions(final ISmintIoMetadataElement[] metaDataElements);


    /**
     * Maps the Smint.io ID of a <em>License Distribution</em> to a synchronization target ID/key.
     *
     * <p>
     * The Smint.io ID to map is similar to the value from
     * {@link io.smint.clapi.consumer.integration.core.contracts.ISmintIoLicenseTerm#getAllowedDistributions()}.
     * </p>
     *
     * @param smintIoId the ID on the Smint.io platform.
     * @return the key on the synchronization target, or {@code null} in case the item does not exist yet on the target
     *         or if parameter the is {@code null}.
     * @see io.smint.clapi.consumer.integration.core.contracts.ISmintIoLicenseTerm#getAllowedDistributions()
     */
    String getLicenseDistributionId(final String smintIoId);


    /**
     * Adds mapping of Smint.io API IDs to sync target IDs for content providers.
     *
     * <p>
     * The Smint.io API ID is read from {@link ISmintIoMetadataElement#getKey()}, whereas the synchronization target ID
     * is read from {@link ISmintIoMetadataElement#getTargetMetadataUuid()}. Both must not be {@code null}. An exception
     * is thrown in case any meta data element lacks one of these values.
     * </p>
     *
     * <p>
     * In case one of the IDs already exist, it will override the previously existing mapping.
     * </p>
     *
     * @param metaDataElements the list of meta data elements to add to the mapping.
     * @return {@code this}.
     * @throws NullPointerException in case {@link ISmintIoMetadataElement#getKey()} or
     *                              {@link ISmintIoMetadataElement#getTargetMetadataUuid()} is {@code null} for any item
     *                              in the provided list.
     */
    ISyncMetadataIdMapper addMappingOfLicenseGeographies(final ISmintIoMetadataElement[] metaDataElements);


    /**
     * Maps the Smint.io ID of a <em>License Geography</em> to a synchronization target ID/key.
     *
     * <p>
     * The Smint.io ID to map is similar to the value from
     * {@link io.smint.clapi.consumer.integration.core.contracts.ISmintIoLicenseTerm#getAllowedGeographies()}.
     * </p>
     *
     * @param smintIoId the ID on the Smint.io platform.
     * @return the key on the synchronization target, or {@code null} in case the item does not exist yet on the target
     *         or if parameter the is {@code null}.
     * @see io.smint.clapi.consumer.integration.core.contracts.ISmintIoLicenseTerm#getAllowedGeographies()
     */
    String getLicenseGeographyId(final String smintIoId);


    /**
     * Adds mapping of Smint.io API IDs to sync target IDs for content providers.
     *
     * <p>
     * The Smint.io API ID is read from {@link ISmintIoMetadataElement#getKey()}, whereas the synchronization target ID
     * is read from {@link ISmintIoMetadataElement#getTargetMetadataUuid()}. Both must not be {@code null}. An exception
     * is thrown in case any meta data element lacks one of these values.
     * </p>
     *
     * <p>
     * In case one of the IDs already exist, it will override the previously existing mapping.
     * </p>
     *
     * @param metaDataElements the list of meta data elements to add to the mapping.
     * @return {@code this}.
     * @throws NullPointerException in case {@link ISmintIoMetadataElement#getKey()} or
     *                              {@link ISmintIoMetadataElement#getTargetMetadataUuid()} is {@code null} for any item
     *                              in the provided list.
     */
    ISyncMetadataIdMapper addMappingOfLicenseIndustries(final ISmintIoMetadataElement[] metaDataElements);


    /**
     * Maps the Smint.io ID of a <em>License Industry</em> to a synchronization target ID/key.
     *
     * <p>
     * The Smint.io ID to map is similar to the value from
     * {@link io.smint.clapi.consumer.integration.core.contracts.ISmintIoLicenseTerm#getAllowedIndustries()}.
     * </p>
     *
     * @param smintIoId the ID on the Smint.io platform.
     * @return the key on the synchronization target, or {@code null} in case the item does not exist yet on the target
     *         or if parameter the is {@code null}.
     * @see io.smint.clapi.consumer.integration.core.contracts.ISmintIoLicenseTerm#getAllowedIndustries()
     */
    String getLicenseIndustryId(final String smintIoId);


    /**
     * Adds mapping of Smint.io API IDs to sync target IDs for content providers.
     *
     * <p>
     * The Smint.io API ID is read from {@link ISmintIoMetadataElement#getKey()}, whereas the synchronization target ID
     * is read from {@link ISmintIoMetadataElement#getTargetMetadataUuid()}. Both must not be {@code null}. An exception
     * is thrown in case any meta data element lacks one of these values.
     * </p>
     *
     * <p>
     * In case one of the IDs already exist, it will override the previously existing mapping.
     * </p>
     *
     * @param metaDataElements the list of meta data elements to add to the mapping.
     * @return {@code this}.
     * @throws NullPointerException in case {@link ISmintIoMetadataElement#getKey()} or
     *                              {@link ISmintIoMetadataElement#getTargetMetadataUuid()} is {@code null} for any item
     *                              in the provided list.
     */
    ISyncMetadataIdMapper addMappingOfLicenseLanguages(final ISmintIoMetadataElement[] metaDataElements);


    /**
     * Maps the Smint.io ID of a <em>License Language</em> to a synchronization target ID/key.
     *
     * <p>
     * The Smint.io ID to map is similar to the value from
     * {@link io.smint.clapi.consumer.integration.core.contracts.ISmintIoLicenseTerm#getAllowedLanguages()}.
     * </p>
     *
     * <p>
     * Beware that the value "{@code any}" is used in addition to the ISO 639-3 codes, in order to denote <em>any</em>
     * language. There is not ISO code for that case.
     * </p>
     *
     * @param smintIoId the ID on the Smint.io platform.
     * @return the key on the synchronization target, or {@code null} in case the item does not exist yet on the target
     *         or if parameter the is {@code null}.
     * @see io.smint.clapi.consumer.integration.core.contracts.ISmintIoLicenseTerm#getAllowedLanguages()
     */
    String getLicenseLanguageId(final String smintIoId);


    /**
     * Adds mapping of Smint.io API IDs to sync target IDs for content providers.
     *
     * <p>
     * The Smint.io API ID is read from {@link ISmintIoMetadataElement#getKey()}, whereas the synchronization target ID
     * is read from {@link ISmintIoMetadataElement#getTargetMetadataUuid()}. Both must not be {@code null}. An exception
     * is thrown in case any meta data element lacks one of these values.
     * </p>
     *
     * <p>
     * In case one of the IDs already exist, it will override the previously existing mapping.
     * </p>
     *
     * @param metaDataElements the list of meta data elements to add to the mapping.
     * @return {@code this}.
     * @throws NullPointerException in case {@link ISmintIoMetadataElement#getKey()} or
     *                              {@link ISmintIoMetadataElement#getTargetMetadataUuid()} is {@code null} for any item
     *                              in the provided list.
     */
    ISyncMetadataIdMapper addMappingOfLicenseUsageLimits(final ISmintIoMetadataElement[] metaDataElements);


    /**
     * Maps the Smint.io ID of a <em>License Usage Limit</em> to a synchronization target ID/key.
     *
     * <p>
     * The Smint.io ID to map is similar to the value from
     * {@link io.smint.clapi.consumer.integration.core.contracts.ISmintIoLicenseTerm#getUsageLimits()}.
     * </p>
     *
     * @param smintIoId the ID on the Smint.io platform.
     * @return the key on the synchronization target, or {@code null} in case the item does not exist yet on the target
     *         or if parameter the is {@code null}.
     * @see io.smint.clapi.consumer.integration.core.contracts.ISmintIoLicenseTerm#getUsageLimits()
     */
    String getLicenseUsageLimitId(final String smintIoId);
}
