package io.smint.clapi.consumer.integration.core.jobs.impl;

import java.util.Objects;
import java.util.function.Function;

import javax.inject.Inject;

import io.smint.clapi.consumer.integration.core.contracts.ISmintIoLicenseTerm;
import io.smint.clapi.consumer.integration.core.target.ISyncLicenseTerm;
import io.smint.clapi.consumer.integration.core.target.ISyncTarget;


/**
 * Converts an instance from {@link ISmintIoLicenseTerm} to {@link ISyncLicenseTerm}.
 *
 * <p>
 * Conversion involve the replacement of Smint.io specific ID with synchronization target keys. Hence this
 * implementation makes use of an instance of {@link ISyncTarget} to do translation of the IDs.
 * </p>
 *
 * <p>
 * New instances to {@link ISyncLicenseTerm} are created utilizing {@link ISyncTarget#createSyncLicenseTerm()}.
 * </p>
 */
public class LicenseTermConverter extends BaseSyncDataConverter<ISmintIoLicenseTerm, ISyncLicenseTerm> {


    private final ISyncTarget _syncTarget;

    /**
     * Initializes a new converter, using the {@code syncTarget} to map to sync target keys.
     *
     * @param syncTarget the sync target implementation that is used to map the keys and create the resulting instance.
     *                   Must not be {@code null}!
     * @throws NullPointerException if {@code syncTarget} is {@code null}
     */
    @Inject
    public LicenseTermConverter(final ISyncTarget syncTarget) {
        super(ISyncLicenseTerm.class);
        this._syncTarget = syncTarget;

        Objects.requireNonNull(syncTarget, "Provided sync target is invalid <null>");
    }


    @Override
    public ISyncLicenseTerm[] convert(final ISmintIoLicenseTerm rawLicenseTerm) {

        if (rawLicenseTerm == null) {
            return null;
        }

        final ISyncLicenseTerm targetLicenseTerm = this._syncTarget.createSyncLicenseTerm();


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
     *         {@link ISyncTarget#getLicenseExclusivityKey(String)}
     */
    public String[] getLicenseExclusivitiesKeys(final String[] smintIoKeys) {
        return this.convertKeys(smintIoKeys, this._syncTarget::getLicenseExclusivityKey);
    }


    /**
     * Maps license usages keys.
     *
     * @param smintIoKeys the list of Smint.io keys.
     * @return the result of calling {@link #convertKeys(String[], Function)} with providing the mapping function
     *         {@link ISyncTarget#getLicenseUsageKey(String)}
     */
    public String[] getLicenseUsagesKeys(final String[] smintIoKeys) {
        return this.convertKeys(smintIoKeys, this._syncTarget::getLicenseUsageKey);
    }


    /**
     * Maps license size keys.
     *
     * @param smintIoKeys the list of Smint.io keys.
     * @return the result of calling {@link #convertKeys(String[], Function)} with providing the mapping function
     *         {@link ISyncTarget#getLicenseSizeKey(String)}
     */
    public String[] getLicenseSizesKeys(final String[] smintIoKeys) {
        return this.convertKeys(smintIoKeys, this._syncTarget::getLicenseSizeKey);
    }


    /**
     * Maps license placement keys.
     *
     * @param smintIoKeys the list of Smint.io keys.
     * @return the result of calling {@link #convertKeys(String[], Function)} with providing the mapping function
     *         {@link ISyncTarget#getLicensePlacementKey(String)}
     */
    public String[] getLicensePlacementsKeys(final String[] smintIoKeys) {
        return this.convertKeys(smintIoKeys, this._syncTarget::getLicensePlacementKey);
    }


    /**
     * Maps license distribution keys.
     *
     * @param smintIoKeys the list of Smint.io keys.
     * @return the result of calling {@link #convertKeys(String[], Function)} with providing the mapping function
     *         {@link ISyncTarget#getLicenseDistributionKey(String)}
     */
    public String[] getLicenseDistributionsKeys(final String[] smintIoKeys) {
        return this.convertKeys(smintIoKeys, this._syncTarget::getLicenseDistributionKey);
    }


    /**
     * Maps license geographical distribution keys.
     *
     * @param smintIoKeys the list of Smint.io keys.
     * @return the result of calling {@link #convertKeys(String[], Function)} with providing the mapping function
     *         {@link ISyncTarget#getLicenseGeographyKey(String)}
     */
    public String[] getLicenseGeographiesKeys(final String[] smintIoKeys) {
        return this.convertKeys(smintIoKeys, this._syncTarget::getLicenseGeographyKey);
    }


    /**
     * Maps license industry keys.
     *
     * @param smintIoKeys the list of Smint.io keys.
     * @return the result of calling {@link #convertKeys(String[], Function)} with providing the mapping function
     *         {@link ISyncTarget#getLicenseIndustryKey(String)}
     */
    public String[] getLicenseIndustriesKeys(final String[] smintIoKeys) {
        return this.convertKeys(smintIoKeys, this._syncTarget::getLicenseIndustryKey);
    }


    /**
     * Maps license language keys.
     *
     * @param smintIoKeys the list of Smint.io keys.
     * @return the result of calling {@link #convertKeys(String[], Function)} with providing the mapping function
     *         {@link ISyncTarget#getLicenseLanguageKey(String)}
     */
    public String[] getLicenseLanguagesKeys(final String[] smintIoKeys) {
        return this.convertKeys(smintIoKeys, this._syncTarget::getLicenseLanguageKey);
    }


    /**
     * Maps license usage keys.
     *
     * @param smintIoKeys the list of Smint.io keys.
     * @return the result of calling {@link #convertKeys(String[], Function)} with providing the mapping function
     *         {@link ISyncTarget#getLicenseUsageKey(String)}
     */
    public String[] getLicenseUsageLimitsKeys(final String[] smintIoKeys) {
        return this.convertKeys(smintIoKeys, this._syncTarget::getLicenseUsageKey);
    }
}
