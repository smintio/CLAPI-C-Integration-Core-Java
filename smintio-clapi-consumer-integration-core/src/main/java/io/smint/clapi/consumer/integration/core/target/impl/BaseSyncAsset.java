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

package io.smint.clapi.consumer.integration.core.target.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.time.OffsetDateTime;
import java.util.Locale;
import java.util.Map;

import javax.inject.Provider;

import io.smint.clapi.consumer.integration.core.exceptions.SmintIoAuthenticatorException;
import io.smint.clapi.consumer.integration.core.target.ISyncDataType;
import io.smint.clapi.consumer.integration.core.target.ISyncDownloadConstraints;
import io.smint.clapi.consumer.integration.core.target.ISyncLicenseOption;
import io.smint.clapi.consumer.integration.core.target.ISyncLicenseTerm;
import io.smint.clapi.consumer.integration.core.target.ISyncReleaseDetails;
import io.smint.clapi.consumer.integration.core.target.ISyncTarget;
import io.smint.clapi.consumer.integration.core.target.ISyncTargetDataFactory;


// CHECKSTYLE OFF: MethodCount

/**
 * Base abstract class a sync target asset instance need to inherit from.
 *
 * <p>
 * In order to make it easier for synchronization target implementation, all data for assets to make persistent are
 * directly applied to an asset instance, representing the data structure of the target system. These instances need to
 * implement the abstract functions of this base class to store the data. Then the <em>Smint.io Integration Core
 * Library</em> is able to pass all related data to it.
 * </p>
 *
 * <p>
 * Since some more transient data is needed, there are some base functions implemented by this base class, to handle
 * these transient data. Derived classes do not need to take care of this transient data and thus do not need to
 * override these functions. In order to avoid unintended overriding, these functions are marked as <em>final</em>.
 * </p>
 *
 * <p>
 * Instances derived from this base class are created utilizing a factory function like
 * {@link ISyncTargetDataFactory#createSyncBinaryAsset()} or {@link ISyncTargetDataFactory#createSyncCompoundAsset()}.
 * Although this single base class handles <em>Binary Assets</em> and <em>Compound Assets</em> simultaneously, there
 * might be requirements to use different implementations on the sync target. It is totally up to the sync target. Since
 * synchronizing is performed one-way, at the moment, these instances are never used to read data. So only the abstract
 * setters must be implemented.
 * </p>
 *
 * <h2 id="two-types-of-assets">Two types of assets</h2>
 * <p>
 * On Smint.io an asset is a unique collection - with a unique ID {@link #setTransactionUuid(String)} - of its meta data
 * (eg: name, description, licenses, restrictions, etc.) and the attached binary data (eg: picture), where each binary
 * data has its own additional <em>Binary ID</em> (see {@link #setBinaryUuid(String)}).<br>
 * With synchronization targets, the digital binary data is in the center of storing data, as this is what most
 * <em>DAM</em> are using. So synchronization creates a direct mapping between a binary data on the target platform to
 * the binary data on the Smint.io platform, although this is not straight forward as Smint.io uses a different notion
 * of data as its base.
 * </p>
 *
 * <p>
 * There are two type of assets in use with Smint.io. This base class is intended to handle both type of assets but the
 * kind of asset is determined by {@link #isCompoundAsset()}.
 * </p>
 * <ol>
 * <li><em>Binary Asset</em> ({@link BaseSyncAsset}) - assets with a single binary attached.</li>
 * <li><em>Compound Asset</em> ({@link }) - assets that contain multiple binaries, each being a variant. eg: pictures
 * might be available in various resolutions and dimensions or maybe even localized. Nevertheless all picture have the
 * same image, scene, etc.</li>
 * </ol>
 *
 * <p>
 * An asset is a clone/copy of an original content, copied at the event of purchase (see
 * {@link #setContentElementUuid(String)}). Every purchase creates such a copy - even if the same content (eg: image,
 * video, etc.) is bought again.
 * </p>
 *
 * <h3>Storage of <em>Binary Asset</em></h3>
 * <p>
 * <em>Single Asset</em> can be stored straight forward. Although it might seem unnecessary, it is strongly advised to
 * store the <em>Binary ID</em> (see {@link #setBinaryUuid(String)}) with each binary, too.
 * </p>
 *
 * <h3>Storage of <em>Compound Asset</em></h3>
 * <p>
 * <em>Compound Asset</em> is a container for a list of multiple binaries and maintains a strong connection between
 * these. It is so strong, that these binaries are never to be split-up. Because of this strong connection, all binaries
 * use the same asset ID (see {@link #setTransactionUuid(String)}). Nevertheless each binary has its <em>Binary ID</em>
 * (see {@link #setBinaryUuid(String)}). The binaries that are part of this <em>Compound Asset</em> can be retrieved by
 * calling {@link #getAssetParts()}. In case the synchronization target support the same concept of a compound item, a
 * single asset should be created on the target, too. Nevertheless each item in the list of asset parts may contain meta
 * data additional to that of the asset.
 * </p>
 *
 * <p>
 * Unfortunately the concept of <em>Compound Asset</em> seems not very commonly used with <em>Digital Asset
 * Management</em> systems. Usually such system focus on a single digital, binary data, using it as the base for data
 * storage. Therefore chances are high, that the synchronization target will not support compound assets. Fortunately
 * most of these systems have some form of <em>Virtual Asset</em>, that can be used instead to simulate a <em>Compound
 * Asset</em>. The <em>Virtual Asset</em> usually do not show up in the UI but can be used to store some data. In this
 * case, to store the list of IDs of the binaries that are part of this <em>Compound Asset</em> (see
 * {@link #getAssetParts()}). For that case, all asset parts (a.k.a. binaries) contain a complete set of meta data, as
 * they are stored as a separate entity with the synchronization target. As these binaries share the same asset ID,
 * their unique identifier is the combination of the asset ID and the binary ID.
 * </p>
 *
 * <h2>Mapping asset IDs between sync target and Smint.io</h2>
 * <p>
 * Each asset contains a unique ID on the synchronization target ({@link #setTargetAssetUuid(String)}). Nevertheless the
 * Smint.io platform ID ({@link #setTargetAssetUuid(String)}) does not address a single binary, and thus each asset on
 * the sync target must store a composite Smint.io key, consisting of <em>Asset ID
 * ({@link #setTransactionUuid(String)})</em> and <em>Binary ID ({@link #setBinaryUuid(String)})</em>. Only this
 * composite key allows a proper mapping of synchronization target IDs and Smint.io binaries. However, <em>Virtual
 * Assets</em> do not have any binary directly attached. So these have to be identified on the target by some target
 * dependent type identifier.
 * </p>
 *
 * <pre>
 * targetAssetUuid == uuid + binaryUuid
 * targetVirtualAssetUuid == uuid + targetTypeIdentifier
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
 * <h2>Displaying localized texts</h2>
 * <p>
 * All texts of this asset are localized (see {@link #setName(Map)}. However, not all texts are available in all
 * languages. To display such a text need to take this into consideration. In case a text is not available in the target
 * language, a default language should be used and if the text is not available with it, the first language in the list
 * should be used instead.
 * </p>
 *
 */
public abstract class BaseSyncAsset implements ISyncDataType {

    private BaseSyncAsset[] _binaryAssets;
    private boolean _isCompondAsset = false;
    private String _recommendedFileName;
    private String _targetAssetUuid;

    private Provider<File> _downloadFileProvider;
    private File _downloadedFile = null;


    /**
     * Sets a synchronization target's ID for this asset.
     *
     * <p>
     * Only assets that have already made persistent to the synchronization target have a <em>Target Asset UUID</em>.
     * This value is set by {@link io.smint.clapi.consumer.integration.core.jobs.ISyncJob} as it asks
     * {@link ISyncTarget#getTargetAssetBinaryUuid(String, String)} for the target asset ID for each asset, based on the
     * Smint.io <em>License Purchase Transaction UUID</em> ({@link #setTransactionUuid(String)}), which is the effective
     * Smint.io platform UUID for an asset. Assets that have never been made persistent with the sync target will not
     * receive an sync target asset UUid.
     * </p>
     *
     * @param targetAssetUuid the sync target's ID for this asset or {@code null}.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    public final BaseSyncAsset setTargetAssetUuid(final String targetAssetUuid) {
        this._targetAssetUuid = targetAssetUuid;
        return this;
    }


    /**
     * provides the a synchronization target's ID for this asset as set with {@link #setTargetAssetUuid(String)}.
     *
     * @return the target asset uuid or {@code null}.
     */
    public final String getTargetAssetUuid() {
        return this._targetAssetUuid;
    }


    /**
     * Set the recommended file name to use on the file system storage.
     *
     * @param fileName a valid file name or {@code null}.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    public BaseSyncAsset setRecommendedFileName(final String fileName) {
        this._recommendedFileName = fileName;
        return this;
    }


    /**
     * Provides the recommended file name to use on the file system storage.
     *
     * @return a valid file name or {@code null}.
     */
    public final String getRecommendedFileName() {
        return this._recommendedFileName;
    }


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
    public BaseSyncAsset setDownloadedFileProvider(final Provider<File> downloadFileProvider) {
        this._downloadFileProvider = downloadFileProvider;
        return this;
    }


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
    public final File getDownloadedFile() throws FileNotFoundException, SmintIoAuthenticatorException {
        if (this._downloadedFile == null) {

            if (this._downloadFileProvider == null) {
                throw new FileNotFoundException("No downloader has been set to download the file from Smint.io API.");
            }

            this._downloadedFile = this._downloadFileProvider.get();
        }
        return this._downloadedFile;
    }


    /**
     * Sets a list of parts for this compound asset - only applicable if {@code this} is a compound asset.
     *
     * <p>
     * Calling this function marks this asset as a compound asset - no matter whether asset parts are being set or not.
     * </p>
     *
     * @param compoundParts the parts that form the compound asset. If {@code null} or an empty list, this asset is not
     *                      regarded as a <em>Compound Asset</em> anymore.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     * @see #getAssetParts()
     */
    public final BaseSyncAsset setAssetParts(final BaseSyncAsset[] compoundParts) {
        this._binaryAssets = compoundParts;
        this._isCompondAsset = true;
        return this;
    }


    /**
     * provides the list of parts for this compound asset - only applicable if {@code this} is a compound asset.
     *
     * @return the parts that form the compound asset. If {@code null} or an empty list, this asset is not regarded as a
     *         <em>Compound Asset</em> anymore.
     */
    public final BaseSyncAsset[] getAssetParts() {
        return this._binaryAssets;
    }


    /**
     * Sets the <em>License Purchase Transaction UUID</em>, the effective Smint.io platform ID, for the asset.
     *
     * <p>
     * On Smint.io platform each purchased asset is related to the actual purchase context, which is called "license
     * purchase transaction". Only this transaction contains the complete license context. Different license
     * requirements may impose different costs. So the license heavily depends on the purchase context.
     * </p>
     *
     * @param smintIoId the Smint.io platform ID to set for the asset.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    public abstract BaseSyncAsset setTransactionUuid(final String smintIoId);


    /**
     * Sets a name for the asset, translated to multiple languages.
     *
     * <p>
     * The possible translations available are defined by the value of
     * {@link io.smint.clapi.consumer.integration.core.configuration.models.ISettingsModel#getImportLanguages()}.
     * However, not all languages might have a valid translation. Languages without a translation might be omitted from
     * the map. Especially if this is a persistent asset and a new language has been added to the list of languages.
     * Then the new language might have not yet been stored to this persistent asset.
     * </p>
     *
     * @param name the multi-language name of the asset.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    public abstract BaseSyncAsset setName(final Map<Locale, String> name);


    /**
     * Determines whether this asset is a <em>Compound Asset</em> or an <em>Binary Asset</em>.
     *
     * <p>
     * The implementation of this function will check for the existence of asset parts, retrieved with
     * {@link #getAssetParts()}. If a valid list with at least 1 asset part is available, then this is a <em>Compound
     * Asset</em>
     * </p>
     *
     *
     * @return {@code true} if {@link #setAssetParts(BaseSyncAsset[])} has already been called.
     * @see <a href="two-types-of-assets">Two types of assets</a>
     */
    public boolean isCompoundAsset() {
        return this._isCompondAsset;
    }


    /**
     * Sets the UUID of the content element this asset is a copy of.
     *
     * <p>
     * Assets are copies of content elements. As a source of the asset, content elements form the link to the upstream
     * content provider. Although much its data is copied to the asset, it still is a necessary link to the original
     * source. The asset is copied from the original source by a purchase transaction on the Smint.io platform.
     * </p>
     *
     * @param contentElementUuid the Smint.io ID of the original source this asset is a copy of or {@code null}.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    public abstract BaseSyncAsset setContentElementUuid(final String contentElementUuid);


    /**
     * Sets the content type of this binary of the related asset.
     *
     * <p>
     * The value is a sync target platform specific ID, mapped from Smint.io key.
     * </p>
     *
     * @param contentTypeKey the sync target ID or {@code null}.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     * @see io.smint.clapi.consumer.integration.core.contracts.ISmintIoAsset#getContentType()
     */
    public abstract BaseSyncAsset setContentType(final String contentTypeKey);


    /**
     * Save the content provider with the asset.
     *
     * <p>
     * The value is a sync target platform specific ID, mapped from Smint.io key.
     * </p>
     *
     * @param contentProviderKey the sync target ID or {@code null}.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     * @see io.smint.clapi.consumer.integration.core.contracts.ISmintIoAsset#getContentProvider()
     */
    public abstract BaseSyncAsset setContentProvider(final String contentProviderKey);


    /**
     * Sets the ID for the content category.
     *
     * <p>
     * The value is a sync target platform specific ID, mapped from Smint.io key.
     * </p>
     *
     * @param contentCategoryKey the sync target ID or {@code null}.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     * @see io.smint.clapi.consumer.integration.core.contracts.ISmintIoAsset#getContentProvider()
     */
    public abstract BaseSyncAsset setContentCategory(final String contentCategoryKey);


    /**
     * Sets an arbitrary localized description for the content.
     *
     * @param description a localized descriptions for the content or {@code null} if unknown.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    public abstract BaseSyncAsset setDescription(final Map<Locale, String> description);


    /**
     * Sets the Web URL to view the asset on the Smint.io platform in the browser.
     *
     * <p>
     * The URL to the web view of the asset on the Smint.io platform can be used by users, to view more information
     * about the asset on the Smint.io platform. Especially what part of collection it is, further discussions, ratings
     * and feedbacks on this asset. The Smint.io platform provides more data than can possibly be stored with a
     * synchronization target.<br>
     * Example:
     * </p>
     *
     * <pre>
     *     https://demo.smint.io/project/21/content-element/7:0:256245306
     * </pre>
     *
     * <p>
     * A user to access this URL still need permissions on the Smint.io platform to do so. Usually a separate user for
     * the platform is needed.
     * </p>
     *
     * @param smintIoUrl the URL to the web page on the Smint.io platform for this asset.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    public abstract BaseSyncAsset setSmintIoUrl(final URL smintIoUrl);


    /**
     * Sets the date, when this asset has been created.
     *
     * @param createdAt the date of creation of this asset or {@code null}.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    public abstract BaseSyncAsset setCreatedAt(OffsetDateTime createdAt);


    /**
     * Sets the date, when this asset has been updated the last time.
     *
     * <p>
     * The last update time is not used to compare for updated content. Times seem to be too prune to different settings
     * on servers of Smint.io and sync target. Hence all meta data of assets are updated, once Smint.io detects a
     * change. Binary data uses a simple, increasing counter as version number (see {@link #setBinaryVersion(int)})
     * which can be used to see, which binary is the newest one. Only Smint.io platform will update the binary data,
     * though. Without a single source, increasing counter would not work as they are not unique then.
     * </p>
     *
     * @param lastUpdatedAt the date of last update of this asset or {@code null}.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    public abstract BaseSyncAsset setLastUpdatedAt(final OffsetDateTime lastUpdatedAt);


    /**
     * Sets the date, when this asset has been purchased.
     *
     * @param purchasedAt the date of purchase of this asset or {@code null}.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    public abstract BaseSyncAsset setPurchasedAt(final OffsetDateTime purchasedAt);


    /**
     * Sets the unique Smint.io platform ID of the purchase cart this asset was part of.
     *
     * <p>
     * Every purchase consists of a single <em>cart</em>, that collects all assets to pay for in a single transaction.
     * At least there is 1 asset in a cart but the amount is unrestricted. An asset can only be part of a single cart
     * but the cart may contain multiple assets.
     * </p>
     *
     * @param cartPurchaseTransactionUuid the ID of the purchase or {@code null}.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    public abstract BaseSyncAsset setCartPurchaseTransactionUuid(final String cartPurchaseTransactionUuid);


    /**
     * Set to {@code true} if the purchase transaction has been cancelled prior to fulfillment of payment.
     *
     * <p>
     * Assets are regarded as being purchased until this value is being set to {@code true}. If set to {@code true},
     * then the user was strongly interested in the asset but actually did not buy it. Thus no license is available to
     * really use this asset.
     * </p>
     *
     * <p>
     * Such a situation might happen in case the asset has been already purchased in the past and therefore is managed
     * by the sync target. Nevertheless the maximum number of usage/downloads have been reached and more must be
     * purchased. If the purchase is being cancelled for some reason, it will be indicated by this flag.
     * </p>
     *
     * @param hasBeenCancelled if {@code true} the purchase transaction for new licenses has been cancelled.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    public abstract BaseSyncAsset setHasBeenCancelled(boolean hasBeenCancelled);


    /**
     * Sets the Smint.io ID of the project this asset has been purchased for.
     *
     * <p>
     * On Smint.io platform every purchase is related to a project context. It provides more insights about further
     * purchase, other collections and more. So the project ID is necessary to retrieve this context in the future. Eg.
     * the web view of the asset is composed making use of the project ID (see {@link #setSmintIoUrl(URL)}).
     * </p>
     *
     * @param projectUuid the Smint.io ID of the project context for this asset or {@code null}.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     * @see #setProjectName(Map)
     */
    public abstract BaseSyncAsset setProjectUuid(final String projectUuid);


    /**
     * Sets the Smint.io localized name of the project this asset has been purchased for.
     *
     * <p>
     * On Smint.io platform every purchase is related to a project context. It provides more insights about further
     * purchase, other collections and more. So the project ID is necessary to retrieve this context in the future. Eg.
     * the web view of the asset is composed making use of the project ID (see {@link #setSmintIoUrl(URL)}).
     * </p>
     *
     * @param projectName the Smint.io name of the project context for this asset or {@code null}.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     * @see #setProjectUuid(String)
     */
    public abstract BaseSyncAsset setProjectName(final Map<Locale, String> projectName);


    /**
     * Sets the Smint.io ID of the collection this asset is part of.
     *
     * <p>
     * On Smint.io platform every asset may be added to a collection within the project context. The collections can be
     * used to prepare purchase or collect assets for certain tasks before selecting the ones to really buy.
     * </p>
     *
     * <p>
     * If possible, the name of the project should be visible on the UI to provide a fast feedback to the user for which
     * project this asset has been acquired.
     * </p>
     *
     * @param collectionUuid the Smint.io collection's ID this asset is part of or {@code null}.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     * @see #setCollectionName(Map)
     */
    public abstract BaseSyncAsset setCollectionUuid(final String collectionUuid);


    /**
     * Sets the Smint.io localized name of the collection this asset is part of.
     *
     * <p>
     * On Smint.io platform every asset may be added to a collection within the project context. The collections can be
     * used to prepare purchase or collect assets for certain tasks before selecting the ones to really buy.
     * </p>
     *
     * <p>
     * If possible, the name of the collection should be visible on the UI to provide a fast feedback to the user how
     * this asset has been acquired and its purchase has been prepared.
     * </p>
     *
     * @param collectionName the Smint.io collection's name this asset is part of or {@code null}.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     * @see #setCollectionUuid(String)
     */
    public abstract BaseSyncAsset setCollectionName(final Map<Locale, String> collectionName);


    /**
     * Sets a list of localized keywords to be used for keyword searches.
     *
     * <p>
     * The list of keywords are usually provided by the upstream content provider (see
     * {@link #setContentProvider(String)}). These keywords can be used for keyword searches. However, not all keywords
     * are available for every language.
     * </p>
     *
     * @param keywords the localized list of keywords or {@code null}.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    public abstract BaseSyncAsset setKeywords(final Map<Locale, String[]> keywords);


    /**
     * Sets a localized list of copyright notices to indicate the copyright owner of this asset.
     *
     * <p>
     * The copyright notice usually is provided by the upstream content provider (see
     * {@link #setContentProvider(String)}). Not all languages may contain proper copyright notice. If the language to
     * display does not have a notice available, use the one for English or the first one available.
     * </p>
     *
     * @param copyrightNotices the localized copyright notices or {@code null}.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    public abstract BaseSyncAsset setCopyrightNotices(final Map<Locale, String> copyrightNotices);


    /**
     * Sets a flag that this asset can only be used for editorial use.
     *
     * <p>
     * Until a value has been passed to this function, it can not be determined whether the asset is for editorial use
     * only or not. There might be some content provider that do not provide this information on some assets. It simply
     * is impossible to tell. The default assumption and its derived legal action depends on the synchronization target.
     * Smint.io can not give any advise how to handle such content.
     * </p>
     *
     * @param isEditorialUse {@link Boolean#TRUE} in case the asset is allowed for <em>Editorial Use</em> only and
     *                       {@link Boolean#FALSE} if the asset can be used for <em>Creative</em> content, too. If this
     *                       can not be determined, than {@code null} is being set.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    public abstract BaseSyncAsset setIsEditorialUse(final Boolean isEditorialUse);


    /**
     * Sets a flag whether this asset has license terms attached or not.
     *
     * <p>
     * With many sync targets the license information is being stored in an unstructured way, that its data can not be
     * easily analyzed. Then it is hard to tell, whether license terms are applied to this asset or not. Smint.io stores
     * all license terms in a structured way and is able to detect such terms. Hence this flag is being set by Smint.io
     * to lift this burden from sync targets.
     * </p>
     *
     * <p>
     * Until a value has been passed to this function, assume {@code false} as default.
     * </p>
     *
     * @param hasLicenseTerms {@code true} in case any license term/restriction applies to this asset.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    public abstract BaseSyncAsset setHasLicenseTerms(final boolean hasLicenseTerms);


    /**
     * Sets a general type ID of the kind license applying to this asset.
     *
     * <p>
     * The type ID is a simple reading text, which has been defined up-front by Smint.io. Its value is arbitrary and
     * uses mixed cases. Non-exhaustive example:
     * </p>
     * <ul>
     * <li>{@code all} - all is allowed, no restrictions are applied.</li>
     * <li>{@code royalty_free} - royalty free content.</li>
     * <li>{@code rights_managed} - rights managed content with more detailed license restrictions.</li>
     * <li>{@code rights_ready}</li>
     * </ul>
     *
     * <p>
     * Although a known set of values are used, some might be changed and more be added in the near future without any
     * further notice. So implementing classes must support an arbitrary value.
     * </p>
     *
     * @param licenseTypeKey the key of the type of license or {@code null}.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    public abstract BaseSyncAsset setLicenseType(final String licenseTypeKey);


    /**
     * Sets a Smint.io ID for the licensee.
     *
     * <p>
     * Further information about the licensee can be retrieved from the Smint.io platform using this ID.
     * </p>
     *
     * @param licenseeUuid the Smint.io ID of the licensee or {@code null}.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    public abstract BaseSyncAsset setLicenseeUuid(final String licenseeUuid);


    /**
     * Sets the name of the licensee.
     *
     * <p>
     * Further information about the licensee may be stored with the Smint.io platform, but at least this name can be
     * used to display the licensee in the UI of the sync target. Usually this is the business name of the licensee.
     * </p>
     *
     * @param licenseeName the name of the licensee or {@code null}.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    public abstract BaseSyncAsset setLicenseeName(final String licenseeName);


    /**
     * Sets a localized text of the license that applies to this asset.
     *
     * <p>
     * The localized text contain a textual summary of the license and maybe additional information. This text is
     * intended to be displayed in the UI along the asset.
     * </p>
     *
     * @param licenseText the localized text of the license or {@code null}.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    public abstract BaseSyncAsset setLicenseText(final Map<Locale, String> licenseText);


    /**
     * Sets a list of options for the applying licenses.
     *
     * <p>
     * Every instance part of the list has been created with {@link ISyncTargetDataFactory#createSyncLicenseOption()}.
     * </p>
     *
     * @param licenseOptions the list of options for the license or {@code null}.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    public abstract BaseSyncAsset setLicenseOptions(final ISyncLicenseOption[] licenseOptions);


    /**
     * Sets a list of license terms to apply to this asset.
     *
     * <p>
     * Every instance part of the list has been created with {@link ISyncTargetDataFactory#createSyncLicenseTerm()}.
     * </p>
     *
     * @param licenseTerms the list of license terms of this asset or {@code null}.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    public abstract BaseSyncAsset setLicenseTerms(final ISyncLicenseTerm[] licenseTerms);


    /**
     * Sets download constraints to apply to this asset.
     *
     * <p>
     * The provided instance has been created with {@link ISyncTargetDataFactory#createSyncDownloadConstraints()}.
     * </p>
     *
     * @param downloadConstraints the download constraints of this asset or {@code null}.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    public abstract BaseSyncAsset setDownloadConstraints(final ISyncDownloadConstraints downloadConstraints);


    /**
     * Sets some release details of this asset, regarding agreements with visible models or property owners.
     *
     * <p>
     * Every provided instance has been created with {@link ISyncTargetDataFactory#createSyncDownloadConstraints()}.
     * </p>
     *
     * @param releaseDetails release details of this asset or {@code null}.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    public abstract BaseSyncAsset setReleaseDetails(final ISyncReleaseDetails releaseDetails);


    /**
     * Set the Smint.io UUID to the single binary for the asset.
     *
     * @param binaryUuid the Smint.io platform UUID for the binary file of this asset.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    public abstract BaseSyncAsset setBinaryUuid(String binaryUuid);


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
    public abstract BaseSyncAsset setBinaryType(String binaryTypeKey);


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
    public abstract BaseSyncAsset setBinaryLocale(final Locale binaryLocale);


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
    public abstract BaseSyncAsset setBinaryVersion(int binaryVersion);


    /**
     * Sets a localized textual description of the usage definition for the binary.
     *
     * @param binaryUsage a localized description of the usage definitions for the binary or {@code null}.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     * @see <a href="two-types-of-assets">Two types of assets</a>
     */
    public abstract BaseSyncAsset setBinaryUsage(Map<Locale, String> binaryUsage);

}

// CHECKSTYLE ON: MethodCount
