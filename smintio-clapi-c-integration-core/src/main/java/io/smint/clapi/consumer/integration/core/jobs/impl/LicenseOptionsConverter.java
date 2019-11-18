package io.smint.clapi.consumer.integration.core.jobs.impl;

import java.util.Objects;

import javax.inject.Inject;

import io.smint.clapi.consumer.integration.core.contracts.ISmintIoLicenseOptions;
import io.smint.clapi.consumer.integration.core.target.ISyncLicenseOption;
import io.smint.clapi.consumer.integration.core.target.ISyncLicenseTerm;
import io.smint.clapi.consumer.integration.core.target.ISyncTarget;


/**
 * Converts an instance from {@link ISmintIoLicenseOptions} to {@link ISyncLicenseOption}.
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
public class LicenseOptionsConverter extends BaseSyncDataConverter<ISmintIoLicenseOptions, ISyncLicenseOption> {


    private final ISyncTarget _syncTarget;

    /**
     * Initializes a new converter, using the {@code syncTarget} to map to sync target keys.
     *
     * @param syncTarget the sync target implementation that is used to map the keys and create the resulting instance.
     *                   Must not be {@code null}!
     * @throws NullPointerException if {@code syncTarget} is {@code null}
     */
    @Inject
    public LicenseOptionsConverter(final ISyncTarget syncTarget) {
        super(ISyncLicenseOption.class);
        this._syncTarget = syncTarget;

        Objects.requireNonNull(syncTarget, "Provided sync target is invalid <null>");
    }


    @Override
    public ISyncLicenseOption[] convert(final ISmintIoLicenseOptions rawLicenseOption) {

        if (rawLicenseOption == null) {
            return null;
        }

        return new ISyncLicenseOption[] {
            this._syncTarget.createSyncLicenseOption()
                .setName(rawLicenseOption.getOptionName())
                .setLicenseText(rawLicenseOption.getLicenseText())
        };
    }
}
