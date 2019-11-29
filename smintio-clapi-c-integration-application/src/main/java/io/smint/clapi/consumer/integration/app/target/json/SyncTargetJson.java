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

package io.smint.clapi.consumer.integration.app.target.json;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.common.io.Files;

import io.smint.clapi.consumer.integration.core.contracts.ISmintIoMetadataElement;
import io.smint.clapi.consumer.integration.core.exceptions.SmintIoAuthenticatorException;
import io.smint.clapi.consumer.integration.core.exceptions.SmintIoSyncJobException;
import io.smint.clapi.consumer.integration.core.target.ISyncAsset;
import io.smint.clapi.consumer.integration.core.target.ISyncBinaryAsset;
import io.smint.clapi.consumer.integration.core.target.ISyncCompoundAsset;
import io.smint.clapi.consumer.integration.core.target.ISyncDownloadConstraints;
import io.smint.clapi.consumer.integration.core.target.ISyncLicenseOption;
import io.smint.clapi.consumer.integration.core.target.ISyncLicenseTerm;
import io.smint.clapi.consumer.integration.core.target.ISyncReleaseDetails;
import io.smint.clapi.consumer.integration.core.target.ISyncTarget;
import io.smint.clapi.consumer.integration.core.target.ISyncTargetCapabilities;


// CHECKSTYLE OFF: MethodCount

/**
 * Interface to implement for each synchronization target.
 *
 * <p>
 * Contains a memory-only list of stored/updates assets and meta data. All data is lost as soon as this class is subject
 * to garbage collecting.
 * </p>
 */
public class SyncTargetJson implements ISyncTarget {

    private static final Logger LOG = Logger.getLogger(SyncTargetJson.class.getName());


    private long _nextId = 1;
    private final Map<String, String> _mapSmintIoIdToMyId = new Hashtable<>();
    private final Map<String, Object> _allData = new Hashtable<>();
    private transient final Map<String, ISyncAsset> _binaryAssets = new Hashtable<>();
    private transient final Map<String, ISyncAsset> _compoundAssets = new Hashtable<>();

    private final File _assetBinariesDir;

    public SyncTargetJson(final File assetBinaryDownloadsDirectory) {
        this._allData.put("binaryAssets", this._binaryAssets);
        this._allData.put("compoundAssets", this._compoundAssets);

        this._assetBinariesDir = assetBinaryDownloadsDirectory;
        if (assetBinaryDownloadsDirectory != null) {
            assetBinaryDownloadsDirectory.mkdirs();
        }
    }


    /**
     * Provides all the internally collected data.
     *
     * @return a map of all collected data.
     */
    public Map<String, Object> getAllData() {
        return this._allData;
    }


    @Override
    public ISyncTargetCapabilities getCapabilities() {
        return new SyncTargetCapabilitiesJson();
    }

    @Override
    public boolean beforeSync() {
        return true;
    }

    @Override
    public boolean beforeGenericMetadataSync() {
        return true;
    }

    @Override
    public void importContentProviders(final ISmintIoMetadataElement[] contentProviders) {
        this.storeSmintIoMetaData("contentProviders", contentProviders);
    }

    @Override
    public String getContentProviderKey(final String smintIoProviderId) {
        return this.getSmintIoMetaDataKey("contentProviders", smintIoProviderId);
    }

    @Override
    public void importContentTypes(final ISmintIoMetadataElement[] contentTypes) {
        this.storeSmintIoMetaData("contentTypes", contentTypes);
    }

    @Override
    public String getContentTypeKey(final String smintIoContentTypeId) {
        return this.getSmintIoMetaDataKey("contentTypes", smintIoContentTypeId);
    }

    @Override
    public void importBinaryTypes(final ISmintIoMetadataElement[] binaryTypes) {
        this.storeSmintIoMetaData("binaryTypes", binaryTypes);
    }

    @Override
    public String getBinaryTypeKey(final String smintIoId) {
        return this.getSmintIoMetaDataKey("binaryTypes", smintIoId);
    }

    @Override
    public void importContentCategories(final ISmintIoMetadataElement[] contentCategories) {
        this.storeSmintIoMetaData("contentCategories", contentCategories);
    }

    @Override
    public String getContentCategoryKey(final String smintIoContentCategoryId) {
        return this.getSmintIoMetaDataKey("contentCategories", smintIoContentCategoryId);
    }

    @Override
    public void importLicenseTypes(final ISmintIoMetadataElement[] licenseTypes) {
        this.storeSmintIoMetaData("licenseTypes", licenseTypes);
    }

    @Override
    public String getLicenseTypeKey(final String smintIoLicenseTypeId) {
        return this.getSmintIoMetaDataKey("licenseTypes", smintIoLicenseTypeId);
    }

    @Override
    public void importReleaseStates(final ISmintIoMetadataElement[] releaseStates) {
        this.storeSmintIoMetaData("releaseStates", releaseStates);
    }

    @Override
    public String getReleaseStateKey(final String smintIoReleaseStateId) {
        return this.getSmintIoMetaDataKey("releaseStates", smintIoReleaseStateId);
    }

    @Override
    public void importLicenseExclusivities(final ISmintIoMetadataElement[] licenseExclusivities) {
        this.storeSmintIoMetaData("licenseExclusivities", licenseExclusivities);
    }

    @Override
    public String getLicenseExclusivityKey(final String smintIoId) {
        return this.getSmintIoMetaDataKey("licenseExclusivities", smintIoId);
    }

    @Override
    public void importLicenseUsages(final ISmintIoMetadataElement[] licenseUsages) {
        this.storeSmintIoMetaData("licenseUsages", licenseUsages);
    }

    @Override
    public String getLicenseUsageKey(final String smintIoId) {
        return this.getSmintIoMetaDataKey("licenseUsages", smintIoId);
    }

    @Override
    public void importLicenseSizes(final ISmintIoMetadataElement[] licenseSizes) {
        this.storeSmintIoMetaData("licenseSizes", licenseSizes);
    }

    @Override
    public String getLicenseSizeKey(final String smintIoId) {
        return this.getSmintIoMetaDataKey("licenseSizes", smintIoId);
    }

    @Override
    public void importLicensePlacements(final ISmintIoMetadataElement[] licensePlacements) {
        this.storeSmintIoMetaData("licensePlacements", licensePlacements);
    }

    @Override
    public String getLicensePlacementKey(final String smintIoId) {
        return this.getSmintIoMetaDataKey("licensePlacements", smintIoId);
    }

    @Override
    public void importLicenseDistributions(final ISmintIoMetadataElement[] licenseDistributions) {
        this.storeSmintIoMetaData("licenseDistribution", licenseDistributions);
    }

    @Override
    public String getLicenseDistributionKey(final String smintIoId) {
        return this.getSmintIoMetaDataKey("licenseDistribution", smintIoId);
    }

    @Override
    public void importLicenseGeographies(final ISmintIoMetadataElement[] licenseGeographies) {
        this.storeSmintIoMetaData("licenseGeographies", licenseGeographies);
    }

    @Override
    public String getLicenseGeographyKey(final String smintIoId) {
        return this.getSmintIoMetaDataKey("licenseGeographies", smintIoId);
    }

    @Override
    public void importLicenseIndustries(final ISmintIoMetadataElement[] licenseIndustries) {
        this.storeSmintIoMetaData("licenseIndustries", licenseIndustries);
    }

    @Override
    public String getLicenseIndustryKey(final String smintIoId) {
        return this.getSmintIoMetaDataKey("licenseIndustries", smintIoId);
    }

    @Override
    public void importLicenseLanguages(final ISmintIoMetadataElement[] licenseLanguages) {
        this.storeSmintIoMetaData("licenseLanguages", licenseLanguages);
    }

    @Override
    public String getLicenseLanguageKey(final String smintIoId) {
        return this.getSmintIoMetaDataKey("licenseLanguages", smintIoId);
    }

    @Override
    public void importLicenseUsageLimits(final ISmintIoMetadataElement[] licenseUsageLimits) {
        this.storeSmintIoMetaData("licenseUsageLimit", licenseUsageLimits);
    }

    @Override
    public String getLicenseUsageLimitKey(final String smintIoId) {
        return this.getSmintIoMetaDataKey("licenseUsageLimit", smintIoId);
    }

    @Override
    public void afterGenericMetadataSync() {
    }

    @Override
    public ISyncBinaryAsset createSyncBinaryAsset() {
        return new SyncBinaryAssetJsonImpl();
    }

    @Override
    public ISyncCompoundAsset createSyncCompoundAsset() {
        return new SyncCompoundAssetJsonImpl();
    }

    @Override
    public ISyncLicenseOption createSyncLicenseOption() {
        return new SyncLicenseOptionJsonImpl();
    }

    @Override
    public ISyncLicenseTerm createSyncLicenseTerm() {
        return new SyncLicenseTermJsonImpl();
    }

    @Override
    public ISyncReleaseDetails createSyncReleaseDetails() {
        return new SyncReleaseDetailsJsonImpl();
    }

    @Override
    public ISyncDownloadConstraints createSyncDownloadConstraints() {
        return new SyncDownloadConstraintsJsonImpl();
    }

    @Override
    public boolean beforeAssetsSync() {
        return true;
    }

    @Override
    public String getTargetCompoundAssetUuid(final String assetUuid) {
        return this._mapSmintIoIdToMyId.get("asset-" + assetUuid + "_compound");
    }

    @Override
    public String getTargetBinaryAssetUuid(final String assetUuid, final String binaryUuid) {
        return this._mapSmintIoIdToMyId.get("asset-" + assetUuid + "_" + binaryUuid);
    }

    @Override
    public void importNewTargetAssets(final ISyncBinaryAsset[] newTargetAssets) {

        LOG.finer("Importing new asset!");


        if (newTargetAssets != null) {
            for (final ISyncBinaryAsset asset : newTargetAssets) {

                LOG.finer("Importing new asset " + asset.getUuid() + ":" + asset.getBinaryUuid() + "!");

                final String id = this.getNextId();
                this._binaryAssets.put(id, asset);
                this._mapSmintIoIdToMyId.put("asset-" + asset.getUuid() + "_" + asset.getBinaryUuid(), id);

                LOG.info("Downloading asset file: " + asset.getRecommendedFileName());

                // download file
                try {
                    final File assetFile = asset.getDownloadedFile();
                    LOG.finer(
                        "Download asset to file (" + asset.getRecommendedFileName() + "): " + assetFile.getName()
                    );

                } catch (FileNotFoundException | SmintIoAuthenticatorException excp) {
                    LOG.log(Level.SEVERE, "Failed to download asset to file!", excp);
                }
            }
        }
    }

    @Override
    public void updateTargetAssets(final ISyncBinaryAsset[] updatedTargetAssets) {
        if (updatedTargetAssets != null) {
            for (final ISyncBinaryAsset asset : updatedTargetAssets) {
                final String id = this._mapSmintIoIdToMyId
                    .get("asset-" + asset.getUuid() + "_" + asset.getBinaryUuid());
                this._binaryAssets.put(id, asset);
            }
        }
    }

    @Override
    public void importNewTargetCompoundAssets(final ISyncCompoundAsset[] newTargetCompoundAssets) {
        if (newTargetCompoundAssets != null) {
            for (final ISyncCompoundAsset asset : newTargetCompoundAssets) {
                final String id = this.getNextId();
                this._compoundAssets.put(id, asset);
                this._mapSmintIoIdToMyId.put("asset-" + asset.getUuid() + "_compound", id);
            }
        }
    }

    @Override
    public void updateTargetCompoundAssets(final ISyncCompoundAsset[] updatedTargetCompoundAssets) {
        if (updatedTargetCompoundAssets != null) {
            for (final ISyncCompoundAsset asset : updatedTargetCompoundAssets) {
                final String id = this._mapSmintIoIdToMyId.get("asset-" + asset.getUuid() + "_compound");
                this._binaryAssets.put(id, asset);
            }
        }
    }

    @Override
    public void afterAssetsSync() {

        // move every file to target output directory
        final File assetsDir = this._assetBinariesDir;
        assetsDir.mkdirs();

        for (final SyncBinaryAssetJsonImpl asset : this.getAllBinaryAssets()) {
            try {
                final File assetFile = asset.getDownloadedFile();
                final File targetFile = new File(assetsDir, assetFile.getName());

                if (!targetFile.exists()) {
                    Files.move(assetFile, targetFile);
                }

            } catch (SmintIoAuthenticatorException | IOException excp) {
                LOG.log(Level.SEVERE, "Failed to move asset file for asset " + asset.getRecommendedFileName(), excp);
            }
        }
    }

    @Override
    public void handleAuthenticatorException(final SmintIoAuthenticatorException exception) {
        LOG.log(Level.SEVERE, "authentication to Smint.io API failed!", exception);
    }

    @Override
    public void handleSyncJobException(final SmintIoSyncJobException exception) {
        LOG.log(Level.SEVERE, "synchronization with Smint.io API failed!", exception);
    }

    @Override
    public void afterSync() {

    }


    @Override
    public void clearGenericMetadataCaches() {

    }


    public SyncBinaryAssetJsonImpl[] getAllBinaryAssets() {
        return this._binaryAssets.values()
            .stream()
            .map((i) -> (SyncBinaryAssetJsonImpl) i)
            .toArray(SyncBinaryAssetJsonImpl[]::new);
    }


    public SyncCompoundAssetJsonImpl[] getAllCompoundAssets() {
        return this._compoundAssets.values()
            .stream()
            .map((i) -> (SyncCompoundAssetJsonImpl) i)
            .toArray(SyncCompoundAssetJsonImpl[]::new);
    }


    private String getNextId() {
        return String.valueOf(this._nextId++);
    }


    private void storeSmintIoMetaData(final String storageName, final ISmintIoMetadataElement[] data) {

        @SuppressWarnings("unchecked")
        Map<String, ISmintIoMetadataElement> storage = (Map<String, ISmintIoMetadataElement>) this._allData
            .get(storageName);
        if (storage == null) {
            storage = new Hashtable<>();
            this._allData.put(storageName, storage);
        }


        if (data != null) {
            for (final ISmintIoMetadataElement item : data) {

                String id = this._mapSmintIoIdToMyId.get(storageName + "-" + item.getKey());
                if (id == null) {
                    id = this.getNextId();
                    this._mapSmintIoIdToMyId.put(storageName + "-" + item.getKey(), id);
                }

                storage.put(id, item);
            }
        }
    }

    private String getSmintIoMetaDataKey(final String storageName, final String smintIoId) {
        return this._mapSmintIoIdToMyId.get(storageName + "-" + smintIoId);
    }
}

// CHECKSTYLE OFF: MethodCount
