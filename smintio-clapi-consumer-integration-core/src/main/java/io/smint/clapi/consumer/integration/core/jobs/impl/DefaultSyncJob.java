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

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.inject.Provider;

import io.smint.clapi.consumer.integration.core.configuration.IAuthTokenStorage;
import io.smint.clapi.consumer.integration.core.configuration.ISyncJobDataStorage;
import io.smint.clapi.consumer.integration.core.configuration.models.IAuthTokenModel;
import io.smint.clapi.consumer.integration.core.configuration.models.ISettingsModel;
import io.smint.clapi.consumer.integration.core.configuration.models.ISyncJobDataModel;
import io.smint.clapi.consumer.integration.core.configuration.models.impl.SyncJobDataModelImpl;
import io.smint.clapi.consumer.integration.core.contracts.ISmintIoAsset;
import io.smint.clapi.consumer.integration.core.contracts.ISmintIoGenericMetadata;
import io.smint.clapi.consumer.integration.core.contracts.ISmintIoMetadataElement;
import io.smint.clapi.consumer.integration.core.exceptions.SmintIoAuthenticatorException;
import io.smint.clapi.consumer.integration.core.exceptions.SmintIoAuthenticatorException.AuthenticatorError;
import io.smint.clapi.consumer.integration.core.exceptions.SmintIoSyncJobException;
import io.smint.clapi.consumer.integration.core.exceptions.SmintIoSyncJobException.SyncJobError;
import io.smint.clapi.consumer.integration.core.factory.ISmintIoDownloadProvider;
import io.smint.clapi.consumer.integration.core.jobs.ISyncJob;
import io.smint.clapi.consumer.integration.core.jobs.ISyncMetadataIdMapper;
import io.smint.clapi.consumer.integration.core.providers.ISmintIoApiClient;
import io.smint.clapi.consumer.integration.core.providers.ISmintIoApiDataWithContinuation;
import io.smint.clapi.consumer.integration.core.target.ISyncTarget;
import io.smint.clapi.consumer.integration.core.target.ISyncTargetCapabilities;
import io.smint.clapi.consumer.integration.core.target.ISyncTargetDataFactory;
import io.smint.clapi.consumer.integration.core.target.impl.BaseSyncAsset;


/**
 * Implements a job that can be scheduled or called on-demand to drive the synchronization process.
 *
 * <p>
 * This default implementation will connect to the Smint.io platform, prepare all data to be synchronized and pass the
 * data to an instance of {@link io.smint.clapi.consumer.integration.core.target.ISyncTarget}.
 * </p>
 *
 * <p>
 * No synchronization between jobs are performed. No job knows any other job. The creator must take care of it.
 * </p>
 *
 * <p>
 * Semaphores are used to guarantee, that only a single synchronization job is running per tenant at a time. New tasks
 * are waiting for the currently running task to complete and will be added to a waiting queue. As soon as the queue is
 * full, the next sync task to wait for previous ones is cancelled with an
 * {@link java.util.concurrent.CancellationException} exception. However, at the moment the waiting queue only consists
 * of a single waiting slot for each kind of sync job. The type of sync job is determined by the value of the
 * {@code boolean} parameter to {@link #synchronize(boolean)}. Hence at maximum two sync jobs may be waiting for the a
 * third currently running job to finish.
 * </p>
 */
public class DefaultSyncJob implements ISyncJob {

    private static final Logger LOG = Logger.getLogger(DefaultSyncJob.class.getName());


    private final Provider<ISettingsModel> _settingsProvider;
    private final IAuthTokenStorage _tokenStorage;
    private final ISyncJobDataStorage _syncDataStorage;
    private final ISmintIoApiClient _smintIoClient;
    private final ISyncTarget _syncTarget;
    private final ISyncTargetDataFactory _syncTargetDataFactory;
    private final ISmintIoDownloadProvider _downloadProvider;
    private final ISyncMetadataIdMapper _idMapper;


    /**
     * Create new sync job and provide all necessary parameters via parameters.
     *
     * @param settings              the settings to read tenant ID etc. from
     * @param authTokenStorage      OAuth token for authorization to connect to Smint.io API
     * @param smintIoClient         Smint.IO API wrapper instance
     * @param syncTarget            the target to synchronize to
     * @param syncTargetDataFactory the sync target data factory to create the data instance. Must not be {@code null}!
     * @param syncDataStorage       storage to save some data between synchronization steps. Used for fetching the list
     *                              of assets in chunks, as the list could be very long.
     * @param downloadProvider      an instance to create file downloader for binary asset files.
     * @param idMapper              a utility class to map from meta data Smint.io API IDs to sync target IDs.
     */
    // CHECKSTYLE OFF: ParameterNumber
    @Inject
    public DefaultSyncJob(
        final Provider<ISettingsModel> settings,
        final IAuthTokenStorage authTokenStorage,
        final ISmintIoApiClient smintIoClient,
        final ISyncTarget syncTarget,
        final ISyncTargetDataFactory syncTargetDataFactory,
        final ISyncJobDataStorage syncDataStorage,
        final ISmintIoDownloadProvider downloadProvider,
        final ISyncMetadataIdMapper idMapper
    ) {
        this._settingsProvider = settings;
        this._tokenStorage = authTokenStorage;
        this._syncDataStorage = syncDataStorage;
        this._smintIoClient = smintIoClient;
        this._syncTarget = syncTarget;
        this._syncTargetDataFactory = syncTargetDataFactory;
        this._downloadProvider = downloadProvider;
        this._idMapper = idMapper;


        Objects.requireNonNull(this._syncTarget, "Synchronization target has not been provided!");
        Objects.requireNonNull(this._syncDataStorage, "job data storage is missing!");
        Objects.requireNonNull(this._tokenStorage, "OAuth token provider is missing!");
        Objects.requireNonNull(this._smintIoClient, "Missing Smint.io API client!");
        Objects.requireNonNull(this._settingsProvider, "Settings must not be null!");
        Objects.requireNonNull(this._settingsProvider.get(), "Settings must not be null!");
        Objects.requireNonNull(this._settingsProvider.get().getTenantId(), "Settings must provide a tenent ID!");
        Objects.requireNonNull(this._idMapper, "ID mapper utility is missing!");
    }
    // CHECKSTYLE ON: ParameterNumber


    @Override
    public void synchronize(final boolean syncMetaData) throws SmintIoAuthenticatorException, SmintIoSyncJobException {

        final ISettingsModel settings = this.validateSettingsForSync(this._settingsProvider.get());

        final ISyncTargetCapabilities capabilites = this._syncTarget.getCapabilities();
        if (settings.getImportLanguages().length > 1
            && (capabilites == null || !capabilites.isMultiLanguageSupported())) {

            throw new SmintIoSyncJobException(
                SyncJobError.Generic,
                "SyncTarget supports only one language but multiple language are set to be synced!"
            );
        }


        try {
            final IAuthTokenModel authData = this._tokenStorage.getAuthData();
            this.validateAuthTokenForSync(authData);


            final boolean cancelTask = !this._syncTarget.beforeSync();
            if (cancelTask) {
                LOG.info("'BeforeSync' task terminated with 'false', indicating to abort sync.");
                return;
            }


            if (syncMetaData || this._idMapper.isEmpty()) {
                // synchronizing meta data is enforced in case the ID map does not contain any mappings.
                // The ID mappings are needed, so we MUST sync meta data in this case.
                if (!syncMetaData) {
                    LOG.log(
                        Level.WARNING,
                        "syncing meta data is enforced as the meta data ID mapping is empty and must be filled."
                    );
                }

                this.synchronizeGenericMetadata(this._smintIoClient.getGenericMetadata(), this._syncTarget);
            }

            this.synchronizeAssets(
                settings.getTenantId(),
                this._syncTarget,
                this._syncDataStorage,
                this._smintIoClient
            );

            LOG.fine("calling afterSync() on sync target");
            this._syncTarget.afterSync();

        } catch (final SmintIoAuthenticatorException authExcp) {
            LOG.log(Level.SEVERE, "Authentication error in sync job", authExcp);
            this._syncTarget.handleAuthenticatorException(authExcp);

        } catch (final SmintIoSyncJobException jobExcp) {
            LOG.log(Level.SEVERE, "General synchronization error in sync job", jobExcp);
            this._syncTarget.handleSyncJobException(jobExcp);

            // CHECKSTYLE OFF: IllegalCatch
        } catch (final Exception excp) {
            LOG.log(Level.SEVERE, "Arbitrary error in sync job", excp);
            this._syncTarget.handleSyncJobException(
                new SmintIoSyncJobException(SmintIoSyncJobException.SyncJobError.Generic, excp.getMessage(), excp)
            );
            // CHECKSTYLE ON: IllegalCatch
        }
    }


    /**
     * Validates the provided settings data for validity to be used with the synchronization job.
     *
     * <p>
     * Currently checks that the tenant ID has been set (see {@link ISettingsModel#getTenantId()}) and that valid import
     * languages are available.
     * </p>
     *
     * @param settings the settings data to check.
     * @return a validate version of the settings, to be used for a single run only. The import languages might have
     *         been changed.
     * @throws SmintIoAuthenticatorException in case an invalid settings value has been provided.
     * @throws NullPointerException          if parameter {@code settings} is {@code null}
     */
    public ISettingsModel validateSettingsForSync(final ISettingsModel settings)
        throws SmintIoAuthenticatorException, NullPointerException {

        final String tenantId = settings != null ? settings.getTenantId() : null;
        if (tenantId == null || tenantId.isEmpty()) {
            throw new SmintIoAuthenticatorException(
                AuthenticatorError.SmintIoIntegrationWrongState,
                "The tenant ID is missing!"
            );
        }


        final String[] languages = settings.getImportLanguages();
        if (languages == null || languages.length == 0) {
            throw new SmintIoAuthenticatorException(
                AuthenticatorError.SmintIoIntegrationWrongState,
                "The import languages are missing!"
            );
        }

        // validate import languages
        for (final String language : languages) {
            final Locale localeForLanguage = Locale.forLanguageTag(language);
            if (localeForLanguage == null || localeForLanguage.getISO3Language() == null) {
                throw new SmintIoAuthenticatorException(
                    AuthenticatorError.SmintIoIntegrationWrongState,
                    "The import language '" + language + "' is invalid!"
                );
            }
        }

        return settings;
    }


    /**
     * Validates the provided settings for validity to be used with authorizing to the Smint.io API.
     *
     * <p>
     * Currently checks that the following values are valid:
     * </p>
     * <ul>
     * <li>{@link ISettingsModel#getTenantId()}</li>
     * <li>{@link ISettingsModel#getOAuthClientId()}</li>
     * <li>{@link ISettingsModel#getOAuthClientSecret()}</li>
     * </ul>
     *
     * @param settings the settings data to check.
     * @throws SmintIoAuthenticatorException in case an invalid settings value has been provided.
     * @throws NullPointerException          if parameter {@code settings} is {@code null}
     */
    public void validateSettingsForAuthenticator(final ISettingsModel settings)
        throws SmintIoAuthenticatorException, NullPointerException {

        Objects.requireNonNull(settings, "provided settings to check is null - non existent");

        if (this.isNullOrEmpty(settings.getTenantId())) {
            throw new SmintIoAuthenticatorException(
                AuthenticatorError.SmintIoIntegrationWrongState, "The tenant ID is missing"
            );
        }

        if (this.isNullOrEmpty(settings.getOAuthClientId())) {
            throw new SmintIoAuthenticatorException(
                AuthenticatorError.SmintIoIntegrationWrongState,
                "The client ID is missing"
            );
        }


        if (this.isNullOrEmpty(settings.getOAuthClientSecret())) {
            throw new SmintIoAuthenticatorException(
                AuthenticatorError.SmintIoIntegrationWrongState,
                "The client secret is missing"
            );
        }
    }


    /**
     * Validates the provided OAuth authorization token data for validity to be used with the synchronization job.
     *
     * <p>
     * Currently checks that the following values are valid:
     * </p>
     * <ul>
     * <li>{@link IAuthTokenModel#getAccessToken()}</li>
     * </ul>
     *
     * @param authData authentication data to check whether every data for OAuth authorization is available.
     * @throws SmintIoAuthenticatorException in case an invalid settings value has been provided.
     * @throws NullPointerException          if parameter {@code authData} is {@code null}
     */
    public void validateAuthTokenForSync(final IAuthTokenModel authData)
        throws SmintIoAuthenticatorException, NullPointerException {

        Objects.requireNonNull(authData, "provided authentication data to check is null - non existent");

        if (this.isNullOrEmpty(authData.getAccessToken())) {
            throw new SmintIoAuthenticatorException(
                AuthenticatorError.SmintIoIntegrationWrongState,
                "The access token is missing"
            );
        }
    }


    /**
     * Synchronizes the passed-in generic meta data.
     *
     * <p>
     * the meta data is split up into pieces and passed to the sync target one-by-one
     * </p>
     *
     * @param metaData   the meta data to sync.
     * @param syncTarget the target to sync with.
     * @throws Exception any exception if thrown by the sync target functions
     */
    public void synchronizeGenericMetadata(final ISmintIoGenericMetadata metaData, final ISyncTarget syncTarget)
        throws Exception {

        if (metaData == null) {
            LOG.warning("No meta data to sync!");
            return;
        }

        Objects.requireNonNull(syncTarget, "Provided sync target is null!");


        LOG.info("Starting Smint.io generic metadata synchronization...");

        final boolean cancelMetadataSync = !this._syncTarget.beforeGenericMetadataSync();
        if (cancelMetadataSync && !this._idMapper.isEmpty()) {
            LOG.warning("'beforeGenericMetadataSync' task aborted meta data sync");
            return;

        } else if (cancelMetadataSync) {
            // synchronizing meta data is enforced in case the ID map does not contain any mappings.
            // The ID mappings are needed, so we MUST sync meta data in this case.
            LOG.warning(
                "'beforeGenericMetadataSync' task indicated to stop meta data sync but it is ignored. "
                    + "Meta data ID map is missing and must be filled."
            );
        }


        this._idMapper.clearMapping();


        ISmintIoMetadataElement[] items = metaData.getContentProviders();
        syncTarget.importContentProviders(items);
        this._idMapper.addMappingOfContentProviders(items);

        items = metaData.getContentTypes();
        syncTarget.importContentTypes(items);
        this._idMapper.addMappingOfContentTypes(items);

        items = metaData.getBinaryTypes();
        syncTarget.importBinaryTypes(items);
        this._idMapper.addMappingOfBinaryTypes(items);

        items = metaData.getContentCategories();
        syncTarget.importContentCategories(items);
        this._idMapper.addMappingOfContentCategories(items);


        items = metaData.getLicenseTypes();
        syncTarget.importLicenseTypes(items);
        this._idMapper.addMappingOfLicenseTypes(items);

        items = metaData.getReleaseStates();
        syncTarget.importReleaseStates(items);
        this._idMapper.addMappingOfReleaseStates(items);


        items = metaData.getLicenseExclusivities();
        syncTarget.importLicenseExclusivities(items);
        this._idMapper.addMappingOfLicenseExclusivities(items);

        items = metaData.getLicenseUsages();
        syncTarget.importLicenseUsages(items);
        this._idMapper.addMappingOfLicenseUsages(items);

        items = metaData.getLicenseSizes();
        syncTarget.importLicenseSizes(items);
        this._idMapper.addMappingOfLicenseSizes(items);

        items = metaData.getLicensePlacements();
        syncTarget.importLicensePlacements(items);
        this._idMapper.addMappingOfLicensePlacements(items);

        items = metaData.getLicenseDistributions();
        syncTarget.importLicenseDistributions(items);
        this._idMapper.addMappingOfLicenseDistributions(items);

        items = metaData.getLicenseGeographies();
        syncTarget.importLicenseGeographies(items);
        this._idMapper.addMappingOfLicenseGeographies(items);

        items = metaData.getLicenseIndustries();
        syncTarget.importLicenseIndustries(items);
        this._idMapper.addMappingOfLicenseIndustries(items);

        items = metaData.getLicenseLanguages();
        syncTarget.importLicenseLanguages(items);
        this._idMapper.addMappingOfLicenseLanguages(items);

        items = metaData.getLicenseUsageLimits();
        syncTarget.importLicenseUsageLimits(items);
        this._idMapper.addMappingOfLicenseUsageLimits(items);


        syncTarget.afterGenericMetadataSync();

        LOG.info("Finished Smint.io generic metadata synchronization");
    }


    /**
     * Fetches the assets from Smint.io API and synchronizes these with the target.
     *
     * <p>
     * The assets are fetched from the Smint.io RESTful API in chunks and passed to the sync target in these batches. A
     * temporary directory is created to be used for downloading assets. In the end, all files and directories in the
     * temporary directory are deleted at the end of synchronizing.
     * </p>
     *
     * @param tenantId       the tenant ID is used forming a prefix for the temporary download directory.
     * @param syncTarget     the target to sync with.
     * @param jobDataStorage data storage for job data (eg: continuation ID).
     * @param smintIoClient  the Smint.io API library.
     *
     * @throws Exception any exception thrown by the {@code syncTarget}
     */
    public void synchronizeAssets(
        final String tenantId,
        final ISyncTarget syncTarget,
        final ISyncJobDataStorage jobDataStorage,
        final ISmintIoApiClient smintIoClient
    )
        throws Exception {

        Objects.requireNonNull(tenantId, "Provided tenant ID is <null>!");
        Objects.requireNonNull(syncTarget, "Provided sync target is <null>!");
        Objects.requireNonNull(jobDataStorage, "Provided job data storage is <null>!");
        Objects.requireNonNull(smintIoClient, "Provided Smint.io API client is <null>!");

        LOG.info("Starting Smint.io asset synchronization...");

        final boolean cancelAssetsSync = !syncTarget.beforeAssetsSync();
        if (cancelAssetsSync) {
            LOG.info("'BeforeAssetsSyncAsync' task aborted assets sync");
            return;
        }

        final Path tempFolderPath = Files.createTempDirectory("smint_io-sync-" + tenantId);
        final File tempFolder = tempFolderPath.toFile();

        final ISyncJobDataModel syncDatabaseModel = jobDataStorage.getSyncProcessData();
        String continuationUuid = syncDatabaseModel != null ? syncDatabaseModel.getContinuationUuid() : null;

        try {
            final ISyncTargetCapabilities capabilities = syncTarget.getCapabilities();
            final boolean isCompoundAssetsSupported = capabilities != null ? capabilities.isCompoundAssetsSupported()
                : false;
            final boolean isBinaryUpdatesSupported = capabilities != null ? capabilities.isBinaryUpdatesSupported()
                : false;


            boolean moreChunksToLoad = true;
            while (moreChunksToLoad) {

                final ISmintIoApiDataWithContinuation<ISmintIoAsset[]> rawAssetsInfo = smintIoClient
                    .getAssets(continuationUuid, isCompoundAssetsSupported, isBinaryUpdatesSupported);

                moreChunksToLoad = rawAssetsInfo.hasAssets();

                final String newContinuationUuid = rawAssetsInfo.getContinuationUuid();
                final ISmintIoAsset[] rawAssets = rawAssetsInfo.getResult();
                continuationUuid = newContinuationUuid;

                if (rawAssets != null && rawAssets.length > 0) {

                    moreChunksToLoad = true;

                    final List<WrapperSyncAsset> newTargetAssets = new ArrayList<>();
                    final List<WrapperSyncAsset> updatedTargetAssets = new ArrayList<>();
                    final List<WrapperSyncAsset> newTargetCompoundAssets = new ArrayList<>();
                    final List<WrapperSyncAsset> updatedTargetCompoundAssets = new ArrayList<>();

                    final WrapperSyncAsset[] targetAssets = new AssetConverter(
                        this._syncTargetDataFactory,
                        this._idMapper,
                        this._downloadProvider,
                        tempFolder
                    ).convertAll(rawAssets);
                    Objects.requireNonNull(targetAssets, "Conversion of assets from Smint.io failed.");

                    for (final WrapperSyncAsset targetAsset : targetAssets) {

                        if (targetAsset.isCompoundAsset()) {

                            // check for existing asset
                            final String targetCompoundAssetUuid = this._syncTarget.getTargetCompoundAssetUuid(
                                targetAsset.getTransactionUuid()
                            );

                            if (!this.isNullOrEmpty(targetCompoundAssetUuid)) {
                                targetAsset.setTargetAssetUuid(targetCompoundAssetUuid);
                                updatedTargetCompoundAssets.add(targetAsset);

                            } else {
                                newTargetCompoundAssets.add(targetAsset);
                            }


                        } else {

                            // check for existing asset
                            final String targetAssetUuid = this._syncTarget.getTargetAssetBinaryUuid(
                                targetAsset.getTransactionUuid(),
                                targetAsset.getBinaryUuid()
                            );

                            if (!this.isNullOrEmpty(targetAssetUuid)) {
                                targetAsset.setTargetAssetUuid(targetAssetUuid);
                                updatedTargetAssets.add(targetAsset);

                            } else {
                                newTargetAssets.add(targetAsset);
                            }

                        }

                    }


                    if (!newTargetAssets.isEmpty()) {
                        syncTarget.importNewTargetAssets(
                            newTargetAssets.stream()
                                .map((asset) -> asset.getWrapped())
                                .toArray(BaseSyncAsset[]::new)
                        );
                    }

                    if (!updatedTargetAssets.isEmpty()) {
                        syncTarget.updateTargetAssets(
                            updatedTargetAssets.stream()
                                .map((asset) -> asset.getWrapped())
                                .toArray(BaseSyncAsset[]::new)
                        );
                    }

                    if (!newTargetCompoundAssets.isEmpty()) {
                        syncTarget.importNewTargetCompoundAssets(
                            newTargetCompoundAssets.stream()
                                .map((asset) -> asset.getWrapped())
                                .toArray(BaseSyncAsset[]::new)
                        );
                    }

                    if (!updatedTargetCompoundAssets.isEmpty()) {
                        syncTarget.updateTargetCompoundAssets(
                            updatedTargetCompoundAssets.stream()
                                .map((asset) -> asset.getWrapped())
                                .toArray(BaseSyncAsset[]::new)
                        );
                    }


                    // store continuation ID
                    jobDataStorage.storeSyncProcessData(
                        new SyncJobDataModelImpl().setContinuationUuid(newContinuationUuid)
                    );
                    LOG.info("Synchronized " + rawAssets.length + " Smint.io assets.");
                }
            }

            LOG.info("Finished Smint.io asset synchronization");

            syncTarget.afterAssetsSync();
        } finally {

            LOG.info(
                () -> "Deleting temporary path: " + tempFolder.getAbsolutePath()
            );

            // delete the temporary files and folder
            Files.walk(tempFolderPath)
                .map(Path::toFile)
                .sorted(Comparator.reverseOrder())
                .forEach(File::delete);
        }
    }


    private boolean isNullOrEmpty(final String value) {
        return value == null || value.isEmpty() || value.matches("^\\s*$");
    }
}
