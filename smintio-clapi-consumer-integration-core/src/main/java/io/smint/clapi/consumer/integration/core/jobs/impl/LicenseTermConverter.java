package io.smint.clapi.consumer.integration.core.jobs.impl;

import java.util.Objects;
import java.util.function.Function;

import javax.inject.Inject;

import io.smint.clapi.consumer.integration.core.contracts.ISmintIoLicenseTerm;
import io.smint.clapi.consumer.integration.core.jobs.ISyncMetadataIdMapper;
import io.smint.clapi.consumer.integration.core.target.ISyncLicenseTerm;
import io.smint.clapi.consumer.integration.core.target.ISyncTarget;
import io.smint.clapi.consumer.integration.core.target.ISyncTargetDataFactory;


/**
 * Converts an instance from {@link ISmintIoLicenseTerm} to {@link ISyncLicenseTerm}.
 *
 * <p>
 * Conversion involve the replacement of Smint.io specific ID with synchronization target keys. Hence this
 * implementation makes use of an instance of {@link ISyncTarget} to do translation of the IDs.
 * </p>
 *
 * <p>
 * New instances to {@link ISyncLicenseTerm} are created utilizing
 * {@link ISyncTargetDataFactory#createSyncLicenseTerm()}.
 * </p>
 */
class LicenseTermConverter extends BaseSyncDataConverter<ISmintIoLicenseTerm, ISyncLicenseTerm> {


    private final ISyncTargetDataFactory _syncTargetDataFactory;
    private final ISyncMetadataIdMapper _idMapper;

    /**
     * Initializes a new converter, using the {@code syncTarget} to map to sync target keys.
     *
     * @param syncTargetDataFactory the sync target data factory implementation that is used to create the resulting
     *                              data instances. Must not be {@code null}!
     * @param idMapper              the utility to map the keys from Smint.io API ID to sync target ID. Must not be
     *                              {@code null}!
     * @throws NullPointerException if {@code syncTarget} or {@code syncTargetDataFactory} are {@code null}
     */
    @Inject
    public LicenseTermConverter(
        final ISyncTargetDataFactory syncTargetDataFactory,
        final ISyncMetadataIdMapper idMapper
    ) {
        super(ISyncLicenseTerm.class);
        this._syncTargetDataFactory = syncTargetDataFactory;
        this._idMapper = idMapper;

        Objects.requireNonNull(idMapper, "Provided ID mapper is invalid <null>");
        Objects.requireNonNull(syncTargetDataFactory, "Provided sync target data factory is invalid <null>");
    }


    @Override
    public ISyncLicenseTerm[] convert(final ISmintIoLicenseTerm rawLicenseTerm) {

        if (rawLicenseTerm == null) {
            return null;
        }

        final ISyncLicenseTerm targetLicenseTerm = this._syncTargetDataFactory.createSyncLicenseTerm();


        if (rawLicenseTerm.getSequenceNumber() > 0) {
            targetLicenseTerm.setSequenceNumber(rawLicenseTerm.getSequenceNumber());
        }

        if (rawLicenseTerm.getName() != null && !rawLicenseTerm.getName().isEmpty()) {
            targetLicenseTerm.setName(rawLicenseTerm.getName());
        }


        final String[] exclusivities = this.getLicenseExclusivitiesKeys(rawLicenseTerm.getExclusivities());
        if (exclusivities != null && exclusivities.length > 0) {
            targetLicenseTerm.setExclusivities(exclusivities);
        }

        final String[] allowedUsages = this.getLicenseUsagesKeys(rawLicenseTerm.getAllowedUsages());
        if (allowedUsages != null && allowedUsages.length > 0) {
            targetLicenseTerm.setAllowedUsages(allowedUsages);
        }

        final String[] restrictedUsages = this.getLicenseUsagesKeys(rawLicenseTerm.getRestrictedUsages());
        if (restrictedUsages != null && restrictedUsages.length > 0) {
            targetLicenseTerm.setRestrictedUsages(restrictedUsages);
        }

        final String[] allowedSizes = this.getLicenseSizesKeys(rawLicenseTerm.getAllowedSizes());
        if (allowedSizes != null && allowedSizes.length > 0) {
            targetLicenseTerm.setAllowedSizes(allowedSizes);
        }

        final String[] restrictedSizes = this.getLicenseSizesKeys(rawLicenseTerm.getRestrictedSizes());
        if (restrictedSizes != null && restrictedSizes.length > 0) {
            targetLicenseTerm.setRestrictedSizes(restrictedSizes);
        }

        final String[] allowedPlacements = this.getLicensePlacementsKeys(rawLicenseTerm.getAllowedPlacements());
        if (allowedPlacements != null && allowedPlacements.length > 0) {
            targetLicenseTerm.setAllowedPlacements(allowedPlacements);
        }

        final String[] restrictedPlacements = this.getLicensePlacementsKeys(rawLicenseTerm.getRestrictedPlacements());
        if (restrictedPlacements != null && restrictedPlacements.length > 0) {
            targetLicenseTerm.setRestrictedPlacements(restrictedPlacements);
        }

        final String[] allowedDistributions = this
            .getLicenseDistributionsKeys(rawLicenseTerm.getAllowedDistributions());
        if (allowedDistributions != null && allowedDistributions.length > 0) {
            targetLicenseTerm.setAllowedDistributions(allowedDistributions);
        }

        final String[] restrictedDistributions = this
            .getLicenseDistributionsKeys(rawLicenseTerm.getRestrictedDistributions());
        if (restrictedDistributions != null && restrictedDistributions.length > 0) {
            targetLicenseTerm.setRestrictedDistributions(restrictedDistributions);
        }

        final String[] allowedGeographies = this.getLicenseGeographiesKeys(rawLicenseTerm.getAllowedGeographies());
        if (allowedGeographies != null && allowedGeographies.length > 0) {
            targetLicenseTerm.setAllowedGeographies(allowedGeographies);
        }

        final String[] restrictedGeographies = this
            .getLicenseGeographiesKeys(rawLicenseTerm.getRestrictedGeographies());
        if (restrictedGeographies != null && restrictedGeographies.length > 0) {
            targetLicenseTerm.setRestrictedGeographies(restrictedGeographies);
        }

        final String[] allowedIndustries = this.getLicenseIndustriesKeys(rawLicenseTerm.getAllowedIndustries());
        if (allowedIndustries != null && allowedIndustries.length > 0) {
            targetLicenseTerm.setAllowedIndustries(allowedIndustries);
        }

        final String[] restrictedIndustries = this.getLicenseIndustriesKeys(rawLicenseTerm.getRestrictedIndustries());
        if (restrictedIndustries != null && restrictedIndustries.length > 0) {
            targetLicenseTerm.setRestrictedIndustries(restrictedIndustries);
        }

        final String[] allowedLanguages = this.getLicenseLanguagesKeys(rawLicenseTerm.getAllowedLanguages());
        if (allowedLanguages != null && allowedLanguages.length > 0) {
            targetLicenseTerm.setAllowedLanguages(allowedLanguages);
        }

        final String[] restrictedLanguages = this.getLicenseLanguagesKeys(rawLicenseTerm.getRestrictedLanguages());
        if (restrictedLanguages != null && restrictedLanguages.length > 0) {
            targetLicenseTerm.setRestrictedLanguages(restrictedLanguages);
        }

        final String[] usageLimits = this.getLicenseUsageLimitsKeys(rawLicenseTerm.getUsageLimits());
        if (usageLimits != null && usageLimits.length > 0) {
            targetLicenseTerm.setUsageLimits(usageLimits);
        }

        if (rawLicenseTerm.getValidFrom() != null) {
            targetLicenseTerm.setValidFrom(rawLicenseTerm.getValidFrom());
        }

        if (rawLicenseTerm.getValidUntil() != null) {
            targetLicenseTerm.setValidUntil(rawLicenseTerm.getValidUntil());
        }

        if (rawLicenseTerm.getToBeUsedUntil() != null) {
            targetLicenseTerm.setToBeUsedUntil(rawLicenseTerm.getToBeUsedUntil());
        }

        if (rawLicenseTerm.isEditorialUse() != null) {
            targetLicenseTerm.setIsEditorialUse(rawLicenseTerm.isEditorialUse());
        }

        return new ISyncLicenseTerm[] { targetLicenseTerm, };
    }


    /**
     * Maps licence exclusivities keys.
     *
     * @param smintIoKeys the list of Smint.io keys.
     * @return the result of calling {@link #convertKeys(String[], Function)} with providing the mapping function
     *         {@link ISyncMetadataIdMapper#getLicenseExclusivityId(String)}
     */
    public String[] getLicenseExclusivitiesKeys(final String[] smintIoKeys) {
        return this.convertKeys(smintIoKeys, this._idMapper::getLicenseExclusivityId);
    }


    /**
     * Maps license usages keys.
     *
     * @param smintIoKeys the list of Smint.io keys.
     * @return the result of calling {@link #convertKeys(String[], Function)} with providing the mapping function
     *         {@link ISyncMetadataIdMapper#getLicenseUsageId(String)}
     */
    public String[] getLicenseUsagesKeys(final String[] smintIoKeys) {
        return this.convertKeys(smintIoKeys, this._idMapper::getLicenseUsageId);
    }


    /**
     * Maps license size keys.
     *
     * @param smintIoKeys the list of Smint.io keys.
     * @return the result of calling {@link #convertKeys(String[], Function)} with providing the mapping function
     *         {@link ISyncMetadataIdMapper#getLicenseSizeId(String)}
     */
    public String[] getLicenseSizesKeys(final String[] smintIoKeys) {
        return this.convertKeys(smintIoKeys, this._idMapper::getLicenseSizeId);
    }


    /**
     * Maps license placement keys.
     *
     * @param smintIoKeys the list of Smint.io keys.
     * @return the result of calling {@link #convertKeys(String[], Function)} with providing the mapping function
     *         {@link ISyncMetadataIdMapper#getLicensePlacementId(String)}
     */
    public String[] getLicensePlacementsKeys(final String[] smintIoKeys) {
        return this.convertKeys(smintIoKeys, this._idMapper::getLicensePlacementId);
    }


    /**
     * Maps license distribution keys.
     *
     * @param smintIoKeys the list of Smint.io keys.
     * @return the result of calling {@link #convertKeys(String[], Function)} with providing the mapping function
     *         {@link ISyncMetadataIdMapper#getLicenseDistributionId(String)}
     */
    public String[] getLicenseDistributionsKeys(final String[] smintIoKeys) {
        return this.convertKeys(smintIoKeys, this._idMapper::getLicenseDistributionId);
    }


    /**
     * Maps license geographical distribution keys.
     *
     * @param smintIoKeys the list of Smint.io keys.
     * @return the result of calling {@link #convertKeys(String[], Function)} with providing the mapping function
     *         {@link ISyncMetadataIdMapper#getLicenseGeographyId(String)}
     */
    public String[] getLicenseGeographiesKeys(final String[] smintIoKeys) {
        return this.convertKeys(smintIoKeys, this._idMapper::getLicenseGeographyId);
    }


    /**
     * Maps license industry keys.
     *
     * @param smintIoKeys the list of Smint.io keys.
     * @return the result of calling {@link #convertKeys(String[], Function)} with providing the mapping function
     *         {@link ISyncMetadataIdMapper#getLicenseIndustryId(String)}
     */
    public String[] getLicenseIndustriesKeys(final String[] smintIoKeys) {
        return this.convertKeys(smintIoKeys, this._idMapper::getLicenseIndustryId);
    }


    /**
     * Maps license language keys.
     *
     * @param smintIoKeys the list of Smint.io keys.
     * @return the result of calling {@link #convertKeys(String[], Function)} with providing the mapping function
     *         {@link ISyncMetadataIdMapper#getLicenseLanguageId(String)}
     */
    public String[] getLicenseLanguagesKeys(final String[] smintIoKeys) {
        return this.convertKeys(smintIoKeys, this._idMapper::getLicenseLanguageId);
    }


    /**
     * Maps license usage keys.
     *
     * @param smintIoKeys the list of Smint.io keys.
     * @return the result of calling {@link #convertKeys(String[], Function)} with providing the mapping function
     *         {@link ISyncMetadataIdMapper#getLicenseUsageId(String)}
     */
    public String[] getLicenseUsageLimitsKeys(final String[] smintIoKeys) {
        return this.convertKeys(smintIoKeys, this._idMapper::getLicenseUsageLimitId);
    }
}
