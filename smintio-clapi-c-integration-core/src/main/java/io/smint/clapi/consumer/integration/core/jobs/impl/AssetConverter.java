package io.smint.clapi.consumer.integration.core.jobs.impl;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

import javax.inject.Inject;

import io.smint.clapi.consumer.generated.models.LicensePurchaseTransactionStateEnum;
import io.smint.clapi.consumer.integration.core.contracts.ISmintIoAsset;
import io.smint.clapi.consumer.integration.core.contracts.ISmintIoBinary;
import io.smint.clapi.consumer.integration.core.contracts.ISmintIoDownloadConstraints;
import io.smint.clapi.consumer.integration.core.contracts.ISmintIoReleaseDetails;
import io.smint.clapi.consumer.integration.core.factory.ISmintIoDownloadProvider;
import io.smint.clapi.consumer.integration.core.target.ISyncAsset;
import io.smint.clapi.consumer.integration.core.target.ISyncBinaryAsset;
import io.smint.clapi.consumer.integration.core.target.ISyncCompoundAsset;
import io.smint.clapi.consumer.integration.core.target.ISyncDownloadConstraints;
import io.smint.clapi.consumer.integration.core.target.ISyncLicenseTerm;
import io.smint.clapi.consumer.integration.core.target.ISyncReleaseDetails;
import io.smint.clapi.consumer.integration.core.target.ISyncTarget;


/**
 * Converts an instance from {@link ISmintIoAsset} to {@link ISyncAsset}.
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
public class AssetConverter extends BaseSyncDataConverter<ISmintIoAsset, ISyncAsset> {

    private static final Logger LOG = Logger.getLogger(AssetConverter.class.getName());

    private final ISyncTarget _syncTarget;
    private final ISmintIoDownloadProvider _downloadProvider;
    private final File _temporaryDownloadFolder;

    /**
     * Initializes a new converter, using the {@code syncTarget} to map to sync target keys.
     *
     * @param syncTarget the sync target implementation that is used to map the keys and create the resulting instance.
     *                   Must not be {@code null}!
     * @throws NullPointerException if {@code syncTarget} is {@code null}
     */
    @Inject
    public AssetConverter(
        final ISyncTarget syncTarget,
        final ISmintIoDownloadProvider downloadProvider,
        final File temporaryDownloadFolder
    ) {
        super(ISyncAsset.class);
        this._syncTarget = syncTarget;
        this._downloadProvider = downloadProvider;
        this._temporaryDownloadFolder = temporaryDownloadFolder;

        Objects.requireNonNull(syncTarget, "Provided sync target is invalid <null>");
        Objects.requireNonNull(downloadProvider, "No creator of binary asset downloader has been provided.");
        Objects.requireNonNull(temporaryDownloadFolder, "Temporary download folder is invalid <null>");
    }


    @Override
    public ISyncAsset[] convert(final ISmintIoAsset rawAsset) {

        if (rawAsset == null) {
            return null;
        }

        final List<ISyncAsset> assets = new ArrayList<>();


        LOG.info(() -> "Transforming Smint.io LPT " + rawAsset.getLicensePurchaseTransactionUuid() + " ...");

        final ISmintIoBinary[] binaries = rawAsset.getBinaries();

        final List<ISyncBinaryAsset> assetPartAssets = new ArrayList<>();
        for (final ISmintIoBinary binary : binaries) {

            final URL downloadUrl = binary.getDownloadUrl();
            final String recommendedFileName = binary.getRecommendedFileName();

            final ISyncBinaryAsset targetAsset = this._syncTarget.createSyncBinaryAsset();

            targetAsset
                .setUuid(rawAsset.getLicensePurchaseTransactionUuid())
                .setRecommendedFileName(recommendedFileName)
                .setDownloadUrl(downloadUrl);

            this.setContentMetadata(targetAsset, rawAsset, binary, this._syncTarget);
            this.setLicenseMetadata(targetAsset, rawAsset, this._syncTarget);

            targetAsset
                .setDownloadedFileProvider(
                    this._downloadProvider.createDownloaderForSmintIoUrl(
                        downloadUrl,
                        new File(
                            this._temporaryDownloadFolder,
                            targetAsset.getUuid() + "_" + targetAsset.getBinaryUuid() + "_"
                                + targetAsset.getRecommendedFileName()
                        )
                    )
                );


            assetPartAssets.add(targetAsset);
            assets.add(targetAsset);
        }


        if (assetPartAssets.size() > 1) {
            // we have a compound asset, consisting of more than one asset part

            final ISyncCompoundAsset targetCompoundAsset = this._syncTarget.createSyncCompoundAsset();

            targetCompoundAsset
                .setAssetParts(assetPartAssets.toArray(new ISyncBinaryAsset[assetPartAssets.size()]))
                .setUuid(rawAsset.getLicensePurchaseTransactionUuid());


            this.setContentMetadata(targetCompoundAsset, rawAsset, null, this._syncTarget);
            this.setLicenseMetadata(targetCompoundAsset, rawAsset, this._syncTarget);

            assets.add(targetCompoundAsset);
        }

        LOG.info(() -> "Transformed Smint.io LPT " + rawAsset.getLicensePurchaseTransactionUuid());

        return assets.toArray(new ISyncAsset[assets.size()]);
    }


    public void setContentMetadata(
        final ISyncAsset targetAsset,
        final ISmintIoAsset rawAsset,
        final ISmintIoBinary binary,
        final ISyncTarget syncTarget
    ) {

        final String contentTypeString = this.isNullOrEmpty(binary != null ? binary.getContentType() : null)
            ? binary.getContentType()
            : rawAsset.getContentType();

        targetAsset
            .setContentElementUuid(rawAsset.getContentElementUuid())
            .setContentProvider(syncTarget.getContentProviderKey(rawAsset.getContentProvider()))
            .setContentType(syncTarget.getContentTypeKey(contentTypeString))
            .setContentCategory(syncTarget.getContentCategoryKey(rawAsset.getContentCategory()))
            .setSmintIoUrl(rawAsset.getSmintIoUrl())
            .setPurchasedAt(rawAsset.getPurchasedAt())
            .setCreatedAt(rawAsset.getCreatedAt())
            .setLastUpdatedAt(rawAsset.getLastUpdatedAt())
            .setCartPurchaseTransactionUuid(rawAsset.getCartPurchaseTransactionUuid())
            .setLicensePurchaseTransactionUuid(rawAsset.getLicensePurchaseTransactionUuid())
            .setHasBeenCancelled(rawAsset.getState() == LicensePurchaseTransactionStateEnum.CANCELLED);


        if (binary != null && !this.isNullOrEmpty(binary.getName())) {
            targetAsset.setName(binary.getName());

        } else if (!this.isNullOrEmpty(targetAsset.getName())) {
            targetAsset.setName(rawAsset.getName());
        }


        if (binary != null && !this.isNullOrEmpty(binary.getDescription())) {
            targetAsset.setDescription(binary.getDescription());

        } else if (!this.isNullOrEmpty(rawAsset.getDescription())) {
            targetAsset.setDescription(rawAsset.getDescription());
        }


        if (!this.isNullOrEmpty(rawAsset.getProjectUuid())) {
            targetAsset.setProjectUuid(rawAsset.getProjectUuid());
        }

        if (!this.isNullOrEmpty(rawAsset.getProjectName())) {
            targetAsset.setProjectName(rawAsset.getProjectName());
        }

        if (!this.isNullOrEmpty(rawAsset.getCollectionUuid())) {
            targetAsset.setCollectionUuid(rawAsset.getCollectionUuid());
        }

        if (!this.isNullOrEmpty(rawAsset.getCollectionName())) {
            targetAsset.setCollectionName(rawAsset.getCollectionName());
        }

        if (!this.isNullOrEmpty(rawAsset.getKeywords())) {
            targetAsset.setKeywords(rawAsset.getKeywords());
        }

        if (!this.isNullOrEmpty(rawAsset.getCopyrightNotices())) {
            targetAsset.setCopyrightNotices(rawAsset.getCopyrightNotices());
        }


        if (binary != null && targetAsset instanceof ISyncBinaryAsset) {
            final ISyncBinaryAsset binaryTargetAsset = (ISyncBinaryAsset) targetAsset;

            if (!this.isNullOrEmpty(binary.getBinaryType())) {
                binaryTargetAsset.setBinaryType(syncTarget.getBinaryTypeKey(binary.getBinaryType()));
            }


            binaryTargetAsset
                .setBinaryUuid(binary.getUuid())
                .setBinaryVersion(binary.getVersion());

            if (!this.isNullOrEmpty(binary.getUsage())) {
                binaryTargetAsset.setBinaryUsage(binary.getUsage());
            }

            final Locale binaryCultureLocale = binary.getLocale();
            if (binaryCultureLocale != null) {
                binaryTargetAsset.setBinaryLocale(binaryCultureLocale);
            }
        }
    }

    public void setLicenseMetadata(
        final ISyncAsset targetAsset,
        final ISmintIoAsset rawAsset,
        final ISyncTarget syncTarget
    ) {

        targetAsset
            .setLicenseType(syncTarget.getLicenseTypeKey(rawAsset.getLicenseType()))
            .setLicenseeUuid(rawAsset.getLicenseeUuid())
            .setLicenseeName(rawAsset.getLicenseeName());


        if (rawAsset.getLicenseText() != null && !rawAsset.getLicenseText().isEmpty()) {
            targetAsset.setLicenseText(rawAsset.getLicenseText());
        }

        if (rawAsset.getLicenseOptions() != null && rawAsset.getLicenseOptions().length > 0) {
            targetAsset.setLicenseOptions(
                new LicenseOptionsConverter(syncTarget).convertAll(rawAsset.getLicenseOptions())
            );
        }

        if (rawAsset.getLicenseTerms() != null && rawAsset.getLicenseTerms().length > 0) {

            targetAsset.setLicenseTerms(
                new LicenseTermConverter(syncTarget).convertAll(rawAsset.getLicenseTerms())
            );

        }

        if (targetAsset instanceof ISyncBinaryAsset && rawAsset.getDownloadConstraints() != null) {

            final ISmintIoDownloadConstraints rawDownloadConstraints = rawAsset.getDownloadConstraints();

            final ISyncDownloadConstraints targetDownloadConstraints = syncTarget.createSyncDownloadConstraints();
            Objects.requireNonNull(
                targetDownloadConstraints,
                "ISyncTarget did not create an enitity with 'createSyncDownloadConstraints'"
            );


            targetDownloadConstraints
                .setMaxDownloads(rawDownloadConstraints.getMaxDownloads())
                .setMaxReuses(rawDownloadConstraints.getMaxReuses());

            final ISyncBinaryAsset binaryTargetAsset = (ISyncBinaryAsset) targetAsset;
            binaryTargetAsset.setDownloadConstraints(targetDownloadConstraints);
        }


        if (rawAsset.getReleaseDetails() != null) {
            final ISmintIoReleaseDetails rawReleaseDetails = rawAsset.getReleaseDetails();

            final ISyncReleaseDetails targetReleaseDetails = syncTarget.createSyncReleaseDetails();
            Objects.requireNonNull(
                targetReleaseDetails,
                "ISyncTarget did not create an enitity with 'createSyncReleaseDetails'"
            );

            if (!this.isNullOrEmpty(rawReleaseDetails.getModelReleaseState())) {
                final String modelReleaseState = syncTarget
                    .getReleaseStateKey(rawReleaseDetails.getModelReleaseState());

                if (!this.isNullOrEmpty(modelReleaseState)) {
                    targetReleaseDetails.setModelReleaseState(modelReleaseState);
                }
            }

            if (!this.isNullOrEmpty(rawReleaseDetails.getPropertyReleaseState())) {
                final String propertyReleaseState = syncTarget
                    .getReleaseStateKey(rawReleaseDetails.getPropertyReleaseState());

                if (!this.isNullOrEmpty(propertyReleaseState)) {
                    targetReleaseDetails.setModelReleaseState(propertyReleaseState);
                }
            }


            if (rawReleaseDetails.getProviderAllowedUseComment() != null
                && !rawReleaseDetails.getProviderAllowedUseComment().isEmpty()) {
                targetReleaseDetails.setProviderAllowedUseComment(rawReleaseDetails.getProviderAllowedUseComment());
            }

            if (rawReleaseDetails.getProviderReleaseComment() != null
                && !rawReleaseDetails.getProviderReleaseComment().isEmpty()) {
                targetReleaseDetails.setProviderReleaseComment(rawReleaseDetails.getProviderReleaseComment());
            }

            if (rawReleaseDetails.getProviderUsageConstraints() != null
                && !rawReleaseDetails.getProviderUsageConstraints().isEmpty()) {
                targetReleaseDetails.setProviderUsageConstraints(rawReleaseDetails.getProviderUsageConstraints());
            }

            targetAsset.setReleaseDetails(targetReleaseDetails);
        }

        if (rawAsset.isEditorialUse() != null) {
            targetAsset.setIsEditorialUse(rawAsset.isEditorialUse());
        }

        targetAsset.setHasLicenseTerms(rawAsset.hasLicenseTerms());
    }


    private boolean isNullOrEmpty(final String value) {
        return value == null || value.isEmpty() || value.matches("^\\s*$");
    }

    private boolean isNullOrEmpty(final Map<?, ?> value) {
        return value == null || value.isEmpty();
    }
}
