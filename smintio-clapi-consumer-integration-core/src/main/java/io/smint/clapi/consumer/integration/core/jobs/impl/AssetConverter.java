package io.smint.clapi.consumer.integration.core.jobs.impl;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.inject.Provider;

import io.smint.clapi.consumer.generated.models.LicensePurchaseTransactionStateEnum;
import io.smint.clapi.consumer.integration.core.contracts.ISmintIoAsset;
import io.smint.clapi.consumer.integration.core.contracts.ISmintIoBinary;
import io.smint.clapi.consumer.integration.core.contracts.ISmintIoDownloadConstraints;
import io.smint.clapi.consumer.integration.core.contracts.ISmintIoReleaseDetails;
import io.smint.clapi.consumer.integration.core.factory.ISmintIoDownloadProvider;
import io.smint.clapi.consumer.integration.core.jobs.ISyncMetadataIdMapper;
import io.smint.clapi.consumer.integration.core.target.ISyncAsset;
import io.smint.clapi.consumer.integration.core.target.ISyncBinaryAsset;
import io.smint.clapi.consumer.integration.core.target.ISyncCompoundAsset;
import io.smint.clapi.consumer.integration.core.target.ISyncDownloadConstraints;
import io.smint.clapi.consumer.integration.core.target.ISyncLicenseTerm;
import io.smint.clapi.consumer.integration.core.target.ISyncReleaseDetails;
import io.smint.clapi.consumer.integration.core.target.ISyncTarget;
import io.smint.clapi.consumer.integration.core.target.ISyncTargetDataFactory;


/**
 * Converts an instance from {@link ISmintIoAsset} to {@link ISyncAsset}.
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
class AssetConverter extends BaseSyncDataConverter<ISmintIoAsset, ISyncAsset> {

    private static final Logger LOG = Logger.getLogger(AssetConverter.class.getName());

    private final ISyncTargetDataFactory _syncTargetDataFactory;
    private final ISyncMetadataIdMapper _idMapper;
    private final ISmintIoDownloadProvider _downloadProvider;
    private final File _temporaryDownloadFolder;

    /**
     * Initializes a new converter, using the {@code syncTarget} to map to sync target keys.
     *
     * @param syncTargetDataFactory   the sync target data factory to create the data instance. Must not be
     *                                {@code null}!
     * @param idMapper                the utility to map the keys from Smint.io API ID to sync target ID. Must not be
     *                                {@code null}!
     * @param downloadProvider        an instance to create the file downloader.
     * @param temporaryDownloadFolder the temporary folder where to put all downloads.
     * @throws NullPointerException if {@code syncTarget} is {@code null}
     */
    @Inject
    public AssetConverter(
        final ISyncTargetDataFactory syncTargetDataFactory,
        final ISyncMetadataIdMapper idMapper,
        final ISmintIoDownloadProvider downloadProvider,
        final File temporaryDownloadFolder
    ) {
        super(ISyncAsset.class);
        this._idMapper = idMapper;
        this._downloadProvider = downloadProvider;
        this._temporaryDownloadFolder = temporaryDownloadFolder;
        this._syncTargetDataFactory = syncTargetDataFactory;

        Objects.requireNonNull(idMapper, "Provided ID mapper is invalid <null>");
        Objects.requireNonNull(downloadProvider, "No creator of binary asset downloader has been provided.");
        Objects.requireNonNull(temporaryDownloadFolder, "Temporary download folder is invalid <null>");
        Objects.requireNonNull(syncTargetDataFactory, "Provided sync target data factory is invalid <null>!");
    }


    @Override
    public ISyncAsset[] convert(final ISmintIoAsset rawAsset) {

        final ISmintIoBinary[] binaries = rawAsset != null ? rawAsset.getBinaries() : null;
        if (rawAsset == null || binaries == null || binaries.length == 0) {
            return null;
        }

        final List<ISyncAsset> assets = new ArrayList<>();


        LOG.info(() -> "Transforming Smint.io LPT " + rawAsset.getLicensePurchaseTransactionUuid() + " ...");


        final List<ISyncBinaryAsset> assetPartAssets = new ArrayList<>();
        for (final ISmintIoBinary binary : binaries) {

            final URL downloadUrl = binary.getDownloadUrl();
            final String recommendedFileName = binary.getRecommendedFileName();

            final ISyncBinaryAsset targetAsset = new WrapperSyncBinaryAsset(
                this._syncTargetDataFactory.createSyncBinaryAsset()
            );

            targetAsset
                .setTransactionUuid(rawAsset.getLicensePurchaseTransactionUuid())
                .setRecommendedFileName(recommendedFileName);

            this.setContentMetadata(targetAsset, rawAsset, binary, this._idMapper);
            this.setLicenseMetadata(targetAsset, rawAsset, this._idMapper, this._syncTargetDataFactory);

            targetAsset
                .setDownloadedFileProvider(
                    this._downloadProvider.createDownloaderForSmintIoUrl(
                        downloadUrl,
                        new File(
                            this._temporaryDownloadFolder,
                            targetAsset.getTransactionUuid() + "_" + targetAsset.getBinaryUuid() + "_"
                                + recommendedFileName
                        )
                    )
                );


            assetPartAssets.add(targetAsset);
            assets.add(targetAsset);
        }


        if (assetPartAssets.size() > 1) {
            // we have a compound asset, consisting of more than one asset part

            final ISyncCompoundAsset targetCompoundAsset = new WrapperSyncCompoundAsset(
                this._syncTargetDataFactory.createSyncCompoundAsset()
            );

            targetCompoundAsset
                .setAssetParts(assetPartAssets.toArray(new ISyncBinaryAsset[assetPartAssets.size()]))
                .setTransactionUuid(rawAsset.getLicensePurchaseTransactionUuid());


            this.setContentMetadata(targetCompoundAsset, rawAsset, null, this._idMapper);
            this.setLicenseMetadata(targetCompoundAsset, rawAsset, this._idMapper, this._syncTargetDataFactory);

            assets.add(targetCompoundAsset);
        }

        LOG.info(() -> "Transformed Smint.io LPT " + rawAsset.getLicensePurchaseTransactionUuid());

        return assets.toArray(new ISyncAsset[assets.size()]);
    }


    public void setContentMetadata(
        final ISyncAsset targetAsset,
        final ISmintIoAsset rawAsset,
        final ISmintIoBinary binary,
        final ISyncMetadataIdMapper idMapper
    ) {

        final String contentTypeString = this.isNullOrEmpty(binary != null ? binary.getContentType() : null)
            ? binary.getContentType()
            : rawAsset.getContentType();

        targetAsset
            .setContentElementUuid(rawAsset.getContentElementUuid())
            .setContentProvider(
                this.convertId(
                    rawAsset.getContentProvider(),
                    (id) -> idMapper.getContentProviderId(id),
                    (id) -> "No sync target ID found for content provider ID: " + id
                )
            )
            .setContentType(
                this.convertId(
                    contentTypeString,
                    (id) -> idMapper.getContentTypeId(id),
                    (id) -> "No sync target ID found for content type ID: " + id
                )
            )
            .setContentCategory(
                this.convertId(
                    rawAsset.getContentCategory(),
                    (id) -> idMapper.getContentCategoryId(id),
                    (id) -> "No sync target ID found for content category ID: " + id
                )
            )
            .setSmintIoUrl(rawAsset.getSmintIoUrl())
            .setPurchasedAt(rawAsset.getPurchasedAt())
            .setCreatedAt(rawAsset.getCreatedAt())
            .setLastUpdatedAt(rawAsset.getLastUpdatedAt())
            .setCartPurchaseTransactionUuid(rawAsset.getCartPurchaseTransactionUuid())
            .setTransactionUuid(rawAsset.getLicensePurchaseTransactionUuid())
            .setHasBeenCancelled(rawAsset.getState() == LicensePurchaseTransactionStateEnum.CANCELLED);


        if (binary != null && !this.isNullOrEmpty(binary.getName())) {
            targetAsset.setName(binary.getName());

        } else if (!this.isNullOrEmpty(rawAsset.getName())) {
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
                binaryTargetAsset.setBinaryType(
                    this.convertId(
                        binary.getBinaryType(),
                        (id) -> idMapper.getBinaryTypeId(id),
                        (id) -> "No sync target ID found for binary asset type ID " + id
                    )
                );
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
        final ISyncMetadataIdMapper idMapper,
        final ISyncTargetDataFactory syncTargetDataFactory
    ) {

        targetAsset
            .setLicenseType(
                this.convertId(
                    rawAsset.getLicenseType(),
                    (id) -> idMapper.getLicenseTypeId(id),
                    (id) -> "No sync target ID found for license type ID " + id
                )
            )
            .setLicenseeUuid(rawAsset.getLicenseeUuid())
            .setLicenseeName(rawAsset.getLicenseeName());


        if (rawAsset.getLicenseText() != null && !rawAsset.getLicenseText().isEmpty()) {
            targetAsset.setLicenseText(rawAsset.getLicenseText());
        }

        if (rawAsset.getLicenseOptions() != null && rawAsset.getLicenseOptions().length > 0) {
            targetAsset.setLicenseOptions(
                new LicenseOptionsConverter(syncTargetDataFactory).convertAll(rawAsset.getLicenseOptions())
            );
        }

        if (rawAsset.getLicenseTerms() != null && rawAsset.getLicenseTerms().length > 0) {

            targetAsset.setLicenseTerms(
                new LicenseTermConverter(syncTargetDataFactory, idMapper).convertAll(rawAsset.getLicenseTerms())
            );

        }

        if (targetAsset instanceof ISyncBinaryAsset && rawAsset.getDownloadConstraints() != null) {

            final ISmintIoDownloadConstraints rawDownloadConstraints = rawAsset.getDownloadConstraints();

            final ISyncDownloadConstraints targetDownloadConstraints = syncTargetDataFactory
                .createSyncDownloadConstraints();
            Objects.requireNonNull(
                targetDownloadConstraints,
                "ISyncTarget did not create an enitity with 'createSyncDownloadConstraints'"
            );


            targetDownloadConstraints
                .setMaxUsers(rawDownloadConstraints.getMaxUsers())
                .setMaxDownloads(rawDownloadConstraints.getMaxDownloads())
                .setMaxReuses(rawDownloadConstraints.getMaxReuses());

            final ISyncBinaryAsset binaryTargetAsset = (ISyncBinaryAsset) targetAsset;
            binaryTargetAsset.setDownloadConstraints(targetDownloadConstraints);
        }


        if (rawAsset.getReleaseDetails() != null) {
            final ISmintIoReleaseDetails rawReleaseDetails = rawAsset.getReleaseDetails();

            final ISyncReleaseDetails targetReleaseDetails = syncTargetDataFactory.createSyncReleaseDetails();
            Objects.requireNonNull(
                targetReleaseDetails,
                "ISyncTarget did not create an enitity with 'createSyncReleaseDetails'"
            );

            if (!this.isNullOrEmpty(rawReleaseDetails.getModelReleaseState())) {
                final String modelReleaseState = idMapper
                    .getReleaseStateId(rawReleaseDetails.getModelReleaseState());

                if (!this.isNullOrEmpty(modelReleaseState)) {
                    targetReleaseDetails.setModelReleaseState(modelReleaseState);
                }
            }

            if (!this.isNullOrEmpty(rawReleaseDetails.getPropertyReleaseState())) {
                final String propertyReleaseState = idMapper
                    .getReleaseStateId(rawReleaseDetails.getPropertyReleaseState());

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


    private String convertId(
        final String id,
        final Function<String, String> converter,
        final Function<String, String> errorMessageProvider
    ) {

        if (!this.isNullOrEmpty(id)) {
            return this.checkIdOrThrow(converter.apply(id), () -> errorMessageProvider.apply(id));
        }

        return null;
    }


    private String checkIdOrThrow(final String id, final Provider<String> messageProvider) {
        if (this.isNullOrEmpty(id)) {
            throw new NullPointerException(messageProvider.get());
        }

        return id;
    }
}
