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
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.common.io.Files;

import io.smint.clapi.consumer.integration.core.contracts.ISmintIoMetadataElement;
import io.smint.clapi.consumer.integration.core.exceptions.SmintIoAuthenticatorException;
import io.smint.clapi.consumer.integration.core.exceptions.SmintIoSyncJobException;
import io.smint.clapi.consumer.integration.core.target.ISyncTarget;
import io.smint.clapi.consumer.integration.core.target.ISyncTargetCapabilities;
import io.smint.clapi.consumer.integration.core.target.SyncAsset;
import io.smint.clapi.consumer.integration.core.target.SyncTargetCapabilitiesEnum;


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
    private transient final Map<String, SyncAsset> _binaryAssets = new Hashtable<>();
    private transient final Map<String, SyncAsset> _compoundAssets = new Hashtable<>();

    private final File _assetBinariesDir;

    private Consumer<Void> _afterSyncCallback;


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
        final SyncTargetCapabilitiesEnum[] capabilities = new SyncTargetCapabilitiesEnum[] {
            SyncTargetCapabilitiesEnum.BinaryUpdatesEnum,
            SyncTargetCapabilitiesEnum.MultiLanguageEnum,
        };
        return () -> capabilities;
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
    public void importContentTypes(final ISmintIoMetadataElement[] contentTypes) {
        this.storeSmintIoMetaData("contentTypes", contentTypes);
    }

    @Override
    public void importBinaryTypes(final ISmintIoMetadataElement[] binaryTypes) {
        this.storeSmintIoMetaData("binaryTypes", binaryTypes);
    }

    @Override
    public void importContentCategories(final ISmintIoMetadataElement[] contentCategories) {
        this.storeSmintIoMetaData("contentCategories", contentCategories);
    }

    @Override
    public void importLicenseTypes(final ISmintIoMetadataElement[] licenseTypes) {
        this.storeSmintIoMetaData("licenseTypes", licenseTypes);
    }

    @Override
    public void importReleaseStates(final ISmintIoMetadataElement[] releaseStates) {
        this.storeSmintIoMetaData("releaseStates", releaseStates);
    }

    @Override
    public void importLicenseExclusivities(final ISmintIoMetadataElement[] licenseExclusivities) {
        this.storeSmintIoMetaData("licenseExclusivities", licenseExclusivities);
    }

    @Override
    public void importLicenseUsages(final ISmintIoMetadataElement[] licenseUsages) {
        this.storeSmintIoMetaData("licenseUsages", licenseUsages);
    }

    @Override
    public void importLicenseSizes(final ISmintIoMetadataElement[] licenseSizes) {
        this.storeSmintIoMetaData("licenseSizes", licenseSizes);
    }

    @Override
    public void importLicensePlacements(final ISmintIoMetadataElement[] licensePlacements) {
        this.storeSmintIoMetaData("licensePlacements", licensePlacements);
    }

    @Override
    public void importLicenseDistributions(final ISmintIoMetadataElement[] licenseDistributions) {
        this.storeSmintIoMetaData("licenseDistribution", licenseDistributions);
    }

    @Override
    public void importLicenseGeographies(final ISmintIoMetadataElement[] licenseGeographies) {
        this.storeSmintIoMetaData("licenseGeographies", licenseGeographies);
    }

    @Override
    public void importLicenseIndustries(final ISmintIoMetadataElement[] licenseIndustries) {
        this.storeSmintIoMetaData("licenseIndustries", licenseIndustries);
    }

    @Override
    public void importLicenseLanguages(final ISmintIoMetadataElement[] licenseLanguages) {
        this.storeSmintIoMetaData("licenseLanguages", licenseLanguages);
    }

    @Override
    public void importLicenseUsageLimits(final ISmintIoMetadataElement[] licenseUsageLimits) {
        this.storeSmintIoMetaData("licenseUsageLimit", licenseUsageLimits);
    }

    @Override
    public void afterGenericMetadataSync() {
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
    public String getTargetAssetBinaryUuid(final String assetUuid, final String binaryUuid) {
        return this._mapSmintIoIdToMyId.get("asset-" + assetUuid + "_" + binaryUuid);
    }

    @Override
    public void importNewTargetAssets(final SyncAsset[] newTargetAssets) {

        LOG.finer("Importing new asset!");


        if (newTargetAssets != null) {
            for (final SyncAsset asset : newTargetAssets) {

                if (!(asset instanceof SyncAssetJsonImpl)) {
                    LOG.log(Level.WARNING, "invalid asset instance type found: " + asset.getClass().getName());
                    continue;
                }

                final SyncAssetJsonImpl newAsset = (SyncAssetJsonImpl) asset;


                LOG.finer(
                    "Importing new asset " + newAsset.getTransactionUuid() + ":" + newAsset.getBinaryUuid() + "!"
                );

                final String id = this.getNextId();
                this._binaryAssets.put(id, newAsset);
                this._mapSmintIoIdToMyId
                    .put("asset-" + newAsset.getTransactionUuid() + "_" + newAsset.getBinaryUuid(), id);

                // download file
                try {
                    newAsset.getDownloadedFile();

                } catch (FileNotFoundException | SmintIoAuthenticatorException excp) {
                    LOG.log(Level.SEVERE, "Failed to download asset to file!", excp);
                }
            }
        }
    }

    @Override
    public void updateTargetAssets(final SyncAsset[] updatedTargetAssets) {
        if (updatedTargetAssets != null) {
            for (final SyncAsset asset : updatedTargetAssets) {

                if (!(asset instanceof SyncAssetJsonImpl)) {
                    LOG.log(Level.WARNING, "invalid asset instance type found: " + asset.getClass().getName());
                    continue;
                }

                final SyncAssetJsonImpl newAsset = (SyncAssetJsonImpl) asset;
                final String id = this._mapSmintIoIdToMyId
                    .get("asset-" + newAsset.getTransactionUuid() + "_" + newAsset.getBinaryUuid());
                this._binaryAssets.put(id, asset);
            }
        }
    }

    @Override
    public void importNewTargetCompoundAssets(final SyncAsset[] newTargetCompoundAssets) {
        if (newTargetCompoundAssets != null) {
            for (final SyncAsset asset : newTargetCompoundAssets) {

                final SyncAssetJsonImpl newAsset = (SyncAssetJsonImpl) asset;
                final String id = this.getNextId();
                this._compoundAssets.put(id, newAsset);
                this._mapSmintIoIdToMyId.put("asset-" + newAsset.getTransactionUuid() + "_compound", id);
            }
        }
    }

    @Override
    public void updateTargetCompoundAssets(final SyncAsset[] updatedTargetCompoundAssets) {
        if (updatedTargetCompoundAssets != null) {
            for (final SyncAsset asset : updatedTargetCompoundAssets) {

                final SyncAssetJsonImpl newAsset = (SyncAssetJsonImpl) asset;
                final String id = this._mapSmintIoIdToMyId.get("asset-" + newAsset.getTransactionUuid() + "_compound");
                this._binaryAssets.put(id, newAsset);
            }
        }
    }

    @Override
    public void afterAssetsSync() {

        // move every file to target output directory
        final File assetsDir = this._assetBinariesDir;
        assetsDir.mkdirs();

        for (final SyncAssetJsonImpl asset : this.getAllBinaryAssets()) {
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
        if (this._afterSyncCallback != null) {
            this._afterSyncCallback.accept(null);
        }
    }

    @Override
    public void afterSync() {
        if (this._afterSyncCallback != null) {
            this._afterSyncCallback.accept(null);
        }
    }


    public SyncAssetJsonImpl[] getAllBinaryAssets() {
        return this._binaryAssets.values()
            .stream()
            .map((i) -> (SyncAssetJsonImpl) i)
            .toArray(SyncAssetJsonImpl[]::new);
    }


    public SyncAssetJsonImpl[] getAllCompoundAssets() {
        return this._compoundAssets.values()
            .stream()
            .map((i) -> (SyncAssetJsonImpl) i)
            .toArray(SyncAssetJsonImpl[]::new);
    }


    public Consumer<Void> getAfterAssetSyncCallback() {
        return this._afterSyncCallback;
    }


    public SyncTargetJson setAfterSyncCallback(final Consumer<Void> afterSyncCallback) {
        this._afterSyncCallback = afterSyncCallback;
        return this;
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
                item.setTargetMetadataUuid(id);
            }
        }
    }

}

// CHECKSTYLE OFF: MethodCount
