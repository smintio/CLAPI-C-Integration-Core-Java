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
 * Interface sync target asset instances need to implement.
 *
 * <p>
 * In order to make it easier for synchronization target implementation, all data for assets are directly applied to an
 * asset instance, representing the data structure of the target system. These instances need to implement this
 * interface, too. Then the <em>Smint.io Integration Core Library</em> is able to pass all related data to it.
 * </p>
 *
 * <p>
 * Instances implementing this interface are created utilizing a factory function like
 * {@link ISyncTargetDataFactory#createSyncBinaryAsset()} or {@link ISyncTargetDataFactory#createSyncCompoundAsset()}.
 * Since synchronizing is performed one-way, at the moment, these instances are never used to read data. So only setters
 * must be made available to {@link io.smint.clapi.consumer.integration.core.jobs.ISyncJob}
 * </p>
 *
 * <h2 id="two-types-of-assets">Two types of assets</h2>
 * <p>
 * On Smint.io an asset is a unique collection - with a unique ID {@link #getUuid()} - of its meta data (eg: name,
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
 * <li><em>Binary Asset</em> ({@link ISyncBinaryAsset}) - assets with a single binary attached.</li>
 * <li><em>Compound Asset</em> ({@link ISyncCompoundAsset}) - assets that contain multiple binaries, each being a
 * variant. eg: pictures might be available in various resolutions and dimensions or maybe even localized. Nevertheless
 * all picture have the same image, scene, etc.</li>
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
 * store the <em>Binary ID</em> (see {@link ISyncBinaryAsset#getBinaryUuid()}) with each binary, too.
 * </p>
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
 * All texts of this asset are localized (see {@link #getName()}. However, not all texts are available in all languages.
 * To display such a text need to take this into consideration. In case a text is not available in the target language,
 * a default language should be used and if the text is not available with it, the first language in the list should be
 * used instead.
 * </p>
 *
 */
public interface ISyncAsset extends ISyncDataType {


    /**
     * Provides the Smint.io platform ID for the asset.
     *
     * @return the Smint.io platform ID or {@code null} in case none has been set yet.
     */
    String getUuid();


    /**
     * Sets a Smint.io platform ID for the asset.
     *
     * @param smintIoId the Smint.io platform ID to set for the asset.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     * @throws NullPointerException if parameter {@code smintIoId} is {@code null}.
     */
    ISyncAsset setUuid(final String smintIoId) throws NullPointerException;


    /**
     * Provides the synchronization target's ID for this asset.
     *
     * <p>
     * Only assets that have already made persistent to the synchronization target have a <em>Target Asset UUID</em>.
     * This value is used by {@link io.smint.clapi.consumer.integration.core.jobs.ISyncJob} to determine, whether the
     * assets has ever been made persistent with the sync target or not. So newly created instances must not provide a
     * UUID unless they have already been persisted.
     * </p>
     *
     * @return the sync target's ID for this asset or {@code null} in case none has been set yet. It must be
     *         {@code null} if the asset has not been made persistent yet.
     */
    String getTargetAssetUuid();


    /**
     * Sets a synchronization target's ID for this asset.
     *
     * @param targetAssetUuid the sync target's ID for this asset or {@code null}.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    ISyncAsset setTargetAssetUuid(final String targetAssetUuid);


    /**
     * Provides the name for the asset, translated to multiple languages.
     *
     * <p>
     * The possible translations available are defined by the value of
     * {@link io.smint.clapi.consumer.integration.core.configuration.models.ISettingsModel#getImportLanguages()}.
     * However, not all languages might have a valid translation. Languages without a translation might be omitted from
     * the map. Especially if this is a persistent asset and a new language has been added to the list of languages.
     * Then the new language might have not yet been stored to this persistent asset.
     * </p>
     *
     * @return the multi-language name of the asset or {@code null} if none have been set yet.
     */
    Map<Locale, String> getName();


    /**
     * Sets a name for the asset, translated to multiple languages.
     *
     * <p>
     * The translations available are defined by the value of
     * {@link io.smint.clapi.consumer.integration.core.configuration.models.ISettingsModel#getImportLanguages()}.
     * </p>
     *
     * @param name the multi-language name of the asset.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    ISyncAsset setName(final Map<Locale, String> name);


    /**
     * Determines whether this asset is a <em>Compound Asset</em> or an <em>Binary Asset</em>.
     *
     * <p>
     * The type is determined by the implemented interface, usually. But in case a single class is used for both type of
     * assets, the implementation of this function will check for the existence of asset parts, retrieved with
     * {@link ISyncCompoundAsset#getAssetParts()}. If a valid list with at least 1 asset part is available, then this is
     * a <em>Compound Asset</em>
     * </p>
     *
     *
     * @return {@code true} if only the interface {@link ISyncCompoundAsset} is implemented by the instance or the list
     *         of {@link ISyncCompoundAsset#getAssetParts()} contains at least 1 valid asset part, {@code false}
     *         otherwise. Also if both are missing - no asset parts and no simple binary (check with
     *         {@link ISyncBinaryAsset#getBinaryUuid()}) - {@code false} must be returned, too.
     * @see <a href="two-types-of-assets">Two types of assets</a>
     */
    boolean isCompoundAsset();


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
    ISyncAsset setContentElementUuid(final String contentElementUuid);


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
    ISyncAsset setContentType(final String contentTypeKey);


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
    ISyncAsset setContentProvider(final String contentProviderKey);


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
    ISyncAsset setContentCategory(final String contentCategoryKey);


    /**
     * Sets an arbitrary localized description for the content.
     *
     * @param description a localized descriptions for the content or {@code null} if unknown.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    ISyncAsset setDescription(final Map<Locale, String> description);


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
    ISyncAsset setSmintIoUrl(final URL smintIoUrl);


    /**
     * Sets the date, when this asset has been created.
     *
     * @param createdAt the date of creation of this asset or {@code null}.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    ISyncAsset setCreatedAt(OffsetDateTime createdAt);


    /**
     * Sets the date, when this asset has been updated the last time.
     *
     * <p>
     * The last update time is not used to compare for updated content. Times seem to be too prune to different settings
     * on servers of Smint.io and sync target. Hence all meta data of assets are updated, once Smint.io detects a
     * change. Binary data uses a simple, increasing counter as version number (see
     * {@link ISyncBinaryAsset#setBinaryVersion(int)}) which can be used to see, which binary is the newest one. Only
     * Smint.io platform will update the binary data, though. Without a single source, increasing counter would not work
     * as they are not unique then.
     * </p>
     *
     * @param lastUpdatedAt the date of last update of this asset or {@code null}.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    ISyncAsset setLastUpdatedAt(final OffsetDateTime lastUpdatedAt);


    /**
     * Sets the date, when this asset has been purchased.
     *
     * @param purchasedAt the date of purchase of this asset or {@code null}.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    ISyncAsset setPurchasedAt(final OffsetDateTime purchasedAt);


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
    ISyncAsset setCartPurchaseTransactionUuid(final String cartPurchaseTransactionUuid);


    /**
     * This is a Smint.io internal ID of the asset within the purchase.
     *
     * <p>
     * The value itself does not have any practical use for the user of the sync target, but is necessary to retrieve
     * further information about the asset from the Smint.io platform. Actually, it is the same value as
     * {@code #getUuid()} but might be changed in the future to use a different value.
     * </p>
     *
     * <p>
     * With a purchase you actually buy a license to use the asset, hence the name of this value contains "license" and
     * "purchase transaction".
     * </p>
     *
     * @param licensePurchaseTransactionUuid the ID of the license purchase transaction.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    ISyncAsset setLicensePurchaseTransactionUuid(final String licensePurchaseTransactionUuid);


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
    ISyncAsset setHasBeenCancelled(boolean hasBeenCancelled);


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
    ISyncAsset setProjectUuid(final String projectUuid);


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
    ISyncAsset setProjectName(final Map<Locale, String> projectName);


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
    ISyncAsset setCollectionUuid(final String collectionUuid);


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
    ISyncAsset setCollectionName(final Map<Locale, String> collectionName);


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
    ISyncAsset setKeywords(final Map<Locale, String[]> keywords);


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
    ISyncAsset setCopyrightNotices(final Map<Locale, String> copyrightNotices);


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
    ISyncAsset setIsEditorialUse(final Boolean isEditorialUse);


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
    ISyncAsset setHasLicenseTerms(final boolean hasLicenseTerms);


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
    ISyncAsset setLicenseType(final String licenseTypeKey);


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
    ISyncAsset setLicenseeUuid(final String licenseeUuid);


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
    ISyncAsset setLicenseeName(final String licenseeName);


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
    ISyncAsset setLicenseText(final Map<Locale, String> licenseText);


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
    ISyncAsset setLicenseOptions(final ISyncLicenseOption[] licenseOptions);


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
    ISyncAsset setLicenseTerms(final ISyncLicenseTerm[] licenseTerms);


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
    ISyncAsset setDownloadConstraints(final ISyncDownloadConstraints downloadConstraints);


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
    ISyncAsset setReleaseDetails(final ISyncReleaseDetails releaseDetails);

}

// CHECKSTYLE ON: MethodCount
