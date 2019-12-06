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

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.time.OffsetDateTime;
import java.util.Locale;
import java.util.Map;

import javax.inject.Provider;

import io.smint.clapi.consumer.integration.core.exceptions.SmintIoAuthenticatorException;


// CHECKSTYLE OFF: MethodCount

/**
 * Represents a <em>Simple Asset</em> containing a single binary asset data.
 *
 * <p>
 * Instances implementing this interface are created utilizing one for the factory functions
 * {@link ISyncTargetDataFactory#createSyncCompoundAsset()} or {@link ISyncTargetDataFactory#createSyncBinaryAsset()}.
 * </p>
 *
 * <h2 id="two-types-of-assets">Two types of assets</h2>
 * <p>
 * On Smint.io an asset is a unique collection - with a unique ID {@link #getUuid()} - if its meta data (eg: name,
 * description, licenses, restrictions, etc.) and the attached binary data (eg: picture), where each binary data has its
 * own additional <em>Binary ID</em> (see {@link #getBinaryUuid()}).<br>
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
 * <li><em>Binary Asset</em> - assets with a single binary attached.</li>
 * <li><em>Compound Asset</em> - assets that contain multiple binaries, each being a variant. eg: pictures might be
 * available in various resolutions and dimensions or maybe even localized. Nevertheless all picture have the same
 * image, scene, etc.</li>
 * </ol>
 *
 * <h3>Storage of <em>Binary Asset</em></h3>
 * <p>
 * <em>Single Asset</em> can be stored straight forward. Although it might seem unnecessary, it is strongly advised to
 * store the <em>Binary ID</em> (see {@link #getBinaryUuid()}) with each binary, too.
 * </p>
 *
 * <h2>Mapping asset IDs between sync target and Smint.io</h2>
 * <p>
 * Each asset contains a unique ID on the synchronization target ({@link #getTargetAssetUuid()}). Nevertheless the
 * Smint.io platform ID ({@link #getUuid()}) does not address a single binary, and thus each asset on the sync target
 * must store a composite Smint.io key, consisting of <em>Asset ID ({@link #getUuid()})</em> and <em>Binary ID
 * ({@link #getBinaryUuid()})</em>. Only this composite key allows a proper mapping of synchronization target IDs and
 * Smint.io binaries.
 * </p>
 *
 * <pre>
 * targetAssetUuid == uuid + binaryUuid
 * </pre>
 *
 * <h2>Localized texts with translations</h2>
 * <p>
 * The texts with translations, the possibly available translations are defined by the value of
 * {@link io.smint.clapi.consumer.integration.core.configuration.models.ISettingsModel#getImportLanguages()}. However,
 * not all languages might have a valid translation. Languages without a translation might be omitted. Especially if
 * this is a persistent asset and a new language has been added to the list of languages after storing the asset to the
 * synchronization target, some existing translations might be missing. Such cases are not detected by the
 * synchronization job and unchanged assets will not be updated. Only changes on the Smint.io platform are detected. All
 * changes on the synchronization target are discarded.
 * </p>
 *
 * <h2>Override of values if part of a <em>Compound Asset</em> (see {@link ISyncCompoundAsset})</h2>
 * <p>
 * If this asset is part of a <em>Compound Asset</em> and therefore has been retrieved as part of the list from
 * {@link ISyncCompoundAsset#getAssetParts()}, its values will override the values of it base compound asset. So, if
 * both received values with something like {@link ISyncAsset#setContentCategory(String)}, the asset part's value win
 * and should be selected to be stored with the sync target's asset representation.
 * </p>
 *
 * @see ISyncAsset
 */
public interface ISyncBinaryAsset extends ISyncAsset {

    /**
     * Set the recommended file name to use on the synchronization target.
     *
     * @param fileName a valid file name or {@code null}.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    ISyncBinaryAsset setRecommendedFileName(final String fileName);


    /**
     * Sets the download URL for the binary of this asset.
     *
     * @param downloadURL a valid URL to download the binary from the Smint.io platform or {@code null}.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    ISyncBinaryAsset setDownloadUrl(final URL downloadURL);


    /**
     * Downloads and provides the binary file of this <em>Simple Asset</em>.
     *
     * <p>
     * Downloading the file happens only once, at the first call to this function. For the actual download, the provider
     * set by {@link #setDownloadedFileProvider(Provider)} is used. Further calls will return the same downloaded file
     * and bypass the provider - even if the file has been deleted meanwhile.
     * </p>
     *
     * @return a valid file or {@code null} if no download provider has been set yet.
     * @throws FileNotFoundException         if the file system failed to store the download, thus it can not be found
     *                                       after the download has started. This is also thrown if the download URL is
     *                                       wrong and the file can not be found on the Smint.io platform.
     * @throws SmintIoAuthenticatorException if authentication to the Smint.io API has failed.
     */
    File getDownloadedFile() throws FileNotFoundException, SmintIoAuthenticatorException;


    /**
     * Sets a provider for the binary file of this <em>Simple Asset</em> that will download the binary data.
     *
     * <p>
     * The provider is utilized in {@link #getDownloadedFile()} to download the binary data to a temporary file.
     * </p>
     *
     * @param downloadFileProvider a provider that will download the binary data from the Smint.io API and store it into
     *                             a temporary file. This temporary file will then be returned.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    ISyncBinaryAsset setDownloadedFileProvider(final Provider<File> downloadFileProvider);


    /**
     * Provide the Smint.io UUID to the single binary for the asset.
     *
     * @return the binary UUID of {@code null} if none has been set yet or this asset is a <em>Compound Asset</em>
     *         consisting of multiple binaries.
     */
    String getBinaryUuid();


    /**
     * Set the Smint.io UUID to the single binary for the asset.
     *
     * @param binaryUuid the Smint.io platform UUID for the binary file of this asset.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    ISyncBinaryAsset setBinaryUuid(String binaryUuid);


    /**
     * Sets the type of the binary as a key.
     *
     * <p>
     * The value is a sync target platform specific ID, mapped from Smint.io key.
     * </p>
     *
     * @param binaryTypeKey the sync target ID of exclusivity granted or {@code null}.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    ISyncBinaryAsset setBinaryType(String binaryTypeKey);


    /**
     * Sets the locale (language) this binary contains.
     *
     * <p>
     * Binary data may contain language specific symbols or other data, or be a translated variant of another binary
     * data. In these cases, the language this asset is closely related and best suited for, can be set. Its value can
     * be used for filtered searches.
     * </p>
     *
     * @param binaryLocale the locale to set or {@code null} if none is applicable.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    ISyncBinaryAsset setBinaryLocale(final Locale binaryLocale);


    /**
     * Set the binary version to a new value.
     *
     * <p>
     * The binary version is used to detect changes on the binary file. Every change will increase the version number.
     * So the newest version will be the one with the highest version number. Nevertheless this one-way synchronization
     * will not sync back any changes to the Smint.io platform.
     * </p>
     *
     * @param binaryVersion the new binary version as a positive number, which must be greater or equal to 0.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     * @throws IllegalArgumentException in case the parameter is set to a negative value.
     * @see <a href="two-types-of-assets">Two types of assets</a>
     */
    ISyncBinaryAsset setBinaryVersion(int binaryVersion);


    /**
     * Sets a localized textual description of the usage definition for the binary.
     *
     * @param binaryUsage a localized description of the usage definitions for the binary or {@code null}.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     * @see <a href="two-types-of-assets">Two types of assets</a>
     */
    ISyncBinaryAsset setBinaryUsage(Map<Locale, String> binaryUsage);

    @Override
    ISyncBinaryAsset setContentType(final String contentTypeKey);

    @Override
    ISyncBinaryAsset setUuid(String smintIoId);

    @Override
    ISyncBinaryAsset setTargetAssetUuid(String targetAssetUuid);

    @Override
    ISyncBinaryAsset setName(Map<Locale, String> name);

    @Override
    ISyncBinaryAsset setContentElementUuid(String contentElementUuid);

    @Override
    ISyncBinaryAsset setContentProvider(String contentProviderKey);

    @Override
    ISyncBinaryAsset setContentCategory(String contentCategoryKey);

    @Override
    ISyncBinaryAsset setDescription(Map<Locale, String> description);

    @Override
    ISyncBinaryAsset setSmintIoUrl(URL smintIoUrl);


    @Override
    ISyncBinaryAsset setCreatedAt(OffsetDateTime createdAt);


    @Override
    ISyncBinaryAsset setLastUpdatedAt(OffsetDateTime lastUpdatedAt);


    @Override
    ISyncBinaryAsset setPurchasedAt(OffsetDateTime purchasedAt);


    @Override
    ISyncBinaryAsset setCartPurchaseTransactionUuid(String cartPurchaseTransactionUuid);


    @Override
    ISyncBinaryAsset setLicensePurchaseTransactionUuid(String licensePurchaseTransactionUuid);


    @Override
    ISyncBinaryAsset setHasBeenCancelled(boolean hasBeenCancelled);


    @Override
    ISyncBinaryAsset setProjectUuid(String projectUuid);


    @Override
    ISyncBinaryAsset setProjectName(Map<Locale, String> projectName);


    @Override
    ISyncBinaryAsset setCollectionUuid(String collectionUuid);


    @Override
    ISyncBinaryAsset setCollectionName(Map<Locale, String> collectionName);


    @Override
    ISyncBinaryAsset setKeywords(Map<Locale, String[]> keywords);


    @Override
    ISyncBinaryAsset setCopyrightNotices(Map<Locale, String> copyrightNotices);


    @Override
    ISyncBinaryAsset setIsEditorialUse(Boolean isEditorialUse);


    @Override
    ISyncBinaryAsset setHasLicenseTerms(boolean hasLicenseTerms);


    @Override
    ISyncBinaryAsset setLicenseType(String licenseTypeKey);


    @Override
    ISyncBinaryAsset setLicenseeUuid(String licenseeUuid);


    @Override
    ISyncBinaryAsset setLicenseeName(String licenseeName);


    @Override
    ISyncBinaryAsset setLicenseText(Map<Locale, String> licenseText);


    @Override
    ISyncBinaryAsset setLicenseOptions(ISyncLicenseOption[] licenseOptions);


    @Override
    ISyncBinaryAsset setLicenseTerms(ISyncLicenseTerm[] licenseTerms);


    @Override
    ISyncBinaryAsset setDownloadConstraints(ISyncDownloadConstraints downloadConstraints);


    @Override
    ISyncBinaryAsset setReleaseDetails(ISyncReleaseDetails releaseDetails);

}

// CHECKSTYLE ON: MethodCount
