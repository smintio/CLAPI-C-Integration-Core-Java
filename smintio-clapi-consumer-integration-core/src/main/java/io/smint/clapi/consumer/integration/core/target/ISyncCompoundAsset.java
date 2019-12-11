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

package io.smint.clapi.consumer.integration.core.target;

import java.net.URL;
import java.time.OffsetDateTime;
import java.util.Locale;
import java.util.Map;


// CHECKSTYLE OFF: MethodCount

/**
 * Implementing classes represent the notion of <em>Compound Asset</em>, which combines variants of the same content.
 *
 * <p>
 * Instances implementing this interface are created utilizing a factory function
 * {@link ISyncTargetDataFactory#createSyncCompoundAsset()}.
 * </p>
 *
 * <h2 id="two-types-of-assets">Two types of assets</h2>
 * <p>
 * On Smint.io an asset is a unique collection - with a unique ID {@link #getUuid()} - if its meta data (eg: name,
 * description, licenses, restrictions, etc.) and the attached binary data (eg: picture), where each binary data has its
 * own additional <em>Binary ID</em> (see {@link ISyncBinaryAsset#getBinaryUuid()}).<br>
 * With synchronization targets, the digital binary data is in the center of storing data, as this is what most
 * <em>DAM</em> are using. So synchronization creates a direct mapping between a binary data on the target platform to
 * the binary data on the Smint.io platform, although this is not straight forward as Smint.io uses a different notion
 * of data as its base.
 * </p>
 *
 * <p>
 * There are two type of assets in use with Smint.io.
 * </p>
 * <ol>
 * <li><em>Simple Asset</em> - assets with a single binary attached.</li>
 * <li><em>Compound Asset</em> - assets that contain multiple binaries, each being a variant. eg: pictures might be
 * available in various resolutions and dimensions or maybe even localized. Nevertheless all picture have the same
 * image, scene, etc.</li>
 * </ol>
 *
 * <h3>Storage of <em>Compound Asset</em></h3>
 * <p>
 * <em>Compound Asset</em> is a container for a list of multiple binaries and maintains a strong connection between
 * these. It is so strong, that these binaries are never to be split-up. Because of this strong connection, all binaries
 * use the same asset ID (see {@link #getUuid()}). Nevertheless each binary has its <em>Binary ID</em> (see
 * {@link ISyncBinaryAsset#getBinaryUuid()}). The binaries that are part of this <em>Compound Asset</em> can be
 * retrieved by calling {@link ISyncCompoundAsset#getAssetParts()}. In case the synchronization target support the same
 * concept of a compound item, a single asset should be created on the target, too. Nevertheless each item in the list
 * of asset parts may contain meta data additional to that of the asset.
 * </p>
 *
 * <p>
 * Unfortunately the concept of <em>Compound Asset</em> seems not very commonly used with <em>Digital Asset
 * Management</em> systems. Usually such system focus on a single digital, binary data, using it as the base for data
 * storage. Therefore chances are high, that the synchronization target will not support compound assets. Fortunately
 * most of these systems have some form of <em>Virtual Asset</em>, that can be used instead to simulate a <em>Compound
 * Asset</em>. The <em>Virtual Asset</em> usually do not show up in the UI but can be used to store some data. In this
 * case, to store the list of IDs of the binaries that are part of this <em>Compound Asset</em> (see
 * {@link ISyncCompoundAsset#getAssetParts()}). For that case, all asset parts (a.k.a. binaries) contain a complete set
 * of meta data, as they are stored as a separate entity with the synchronization target. As these binaries share the
 * same asset ID, their unique identifier is the combination of the asset ID and the binary ID.
 * </p>
 *
 * <h2>Mapping asset IDs between sync target and Smint.io</h2>
 * <p>
 * Each asset contains a unique ID on the synchronization target ({@link #getTargetAssetUuid()}). Nevertheless the
 * Smint.io platform ID ({@link #getUuid()}) does not address a single binary, and thus each asset on the sync target
 * must store a composite Smint.io key, consisting of <em>Asset ID ({@link #getUuid()})</em> and <em>Binary ID
 * ({@link ISyncBinaryAsset#getBinaryUuid()})</em>. Only this composite key allows a proper mapping of synchronization
 * target IDs and Smint.io binaries. However, <em>Virtual Assets</em> do not have any binary directly attached. So these
 * have to be identified on the target by some target dependent type identifier.
 * </p>
 *
 * <pre>
 * targetAssetUuid == uuid + binaryUuid
 * targetVirtualAssetUuid == uuid + targetTypeIdentifier
 * </pre>
 *
 * @see ISyncAsset
 */
public interface ISyncCompoundAsset extends ISyncAsset {

    /**
     * Sets a list of parts for this compound asset - only applicable if {@code this} is a compound asset.
     *
     * @param compoundParts the parts that form the compound asset. If {@code null} or an empty list, this asset is not
     *                      regarded as a <em>Compound Asset</em> anymore.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     * @see #getAssetParts()
     */
    ISyncCompoundAsset setAssetParts(final ISyncBinaryAsset[] compoundParts);


    @Override
    ISyncCompoundAsset setTransactionUuid(String smintIoId);

    @Override
    ISyncCompoundAsset setTargetAssetUuid(String targetAssetUuid);

    @Override
    ISyncCompoundAsset setName(Map<Locale, String> name);

    @Override
    ISyncCompoundAsset setContentElementUuid(String contentElementUuid);

    @Override
    ISyncCompoundAsset setContentType(String contentTypeKey);

    @Override
    ISyncCompoundAsset setContentProvider(String contentProviderKey);

    @Override
    ISyncCompoundAsset setContentCategory(String contentCategoryKey);

    @Override
    ISyncCompoundAsset setDescription(Map<Locale, String> description);

    @Override
    ISyncCompoundAsset setSmintIoUrl(URL smintIoUrl);


    @Override
    ISyncCompoundAsset setCreatedAt(OffsetDateTime createdAt);


    @Override
    ISyncCompoundAsset setLastUpdatedAt(OffsetDateTime lastUpdatedAt);


    @Override
    ISyncCompoundAsset setPurchasedAt(OffsetDateTime purchasedAt);


    @Override
    ISyncCompoundAsset setCartPurchaseTransactionUuid(String cartPurchaseTransactionUuid);


    @Override
    ISyncCompoundAsset setHasBeenCancelled(boolean hasBeenCancelled);


    @Override
    ISyncCompoundAsset setProjectUuid(String projectUuid);


    @Override
    ISyncCompoundAsset setProjectName(Map<Locale, String> projectName);


    @Override
    ISyncCompoundAsset setCollectionUuid(String collectionUuid);


    @Override
    ISyncCompoundAsset setCollectionName(Map<Locale, String> collectionName);


    @Override
    ISyncCompoundAsset setKeywords(Map<Locale, String[]> keywords);


    @Override
    ISyncCompoundAsset setCopyrightNotices(Map<Locale, String> copyrightNotices);


    @Override
    ISyncCompoundAsset setIsEditorialUse(Boolean isEditorialUse);


    @Override
    ISyncCompoundAsset setHasLicenseTerms(boolean hasLicenseTerms);


    @Override
    ISyncCompoundAsset setLicenseType(String licenseTypeKey);


    @Override
    ISyncCompoundAsset setLicenseeUuid(String licenseeUuid);


    @Override
    ISyncCompoundAsset setLicenseeName(String licenseeName);


    @Override
    ISyncCompoundAsset setLicenseText(Map<Locale, String> licenseText);


    @Override
    ISyncCompoundAsset setLicenseOptions(ISyncLicenseOption[] licenseOptions);


    @Override
    ISyncCompoundAsset setLicenseTerms(ISyncLicenseTerm[] licenseTerms);


    @Override
    ISyncCompoundAsset setDownloadConstraints(ISyncDownloadConstraints downloadConstraints);


    @Override
    ISyncCompoundAsset setReleaseDetails(ISyncReleaseDetails releaseDetails);

}

// CHECKSTYLE ON: MethodCount
