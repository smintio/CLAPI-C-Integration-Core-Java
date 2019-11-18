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

package io.smint.clapi.consumer.integration.core.contracts;

import java.net.URL;
import java.time.OffsetDateTime;
import java.util.Locale;
import java.util.Map;

import io.smint.clapi.consumer.generated.models.LicensePurchaseTransactionStateEnum;


// CHECKSTYLE OFF: MethodCount

/**
 * Data for an asset to synchronize, as fetched from the Smin.io RESTful API.
 *
 * <p>
 * An asset is a clone/copy of an original content, copied at the event of purchase (see
 * {@link #getContentElementUuid()}). Every purchase creates such a copy - even if the same content (eg: image, video,
 * etc.) is bought again.
 * </p>
 *
 * <h2 id="two-types-of-assets">Two types of assets</h2>
 * <p>
 * On Smint.io an asset is a unique collection - with a unique ID {@link #getLicensePurchaseTransactionUuid()} - if its
 * meta data (eg: name, description, licenses, restrictions, etc.) and the attached binary data (eg: picture), where
 * each binary data has its own additional <em>Binary ID</em> (see {@link ISmintIoBinary#getUuid()}).
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
 *
 * @see io.smint.clapi.consumer.integration.core.target.ISyncAsset
 * @see io.smint.clapi.consumer.integration.core.target.ISyncBinaryAsset
 * @see io.smint.clapi.consumer.integration.core.target.ISyncCompoundAsset
 */
public interface ISmintIoAsset {

    /**
     * Provides the UUID of the content element this asset is a copy of.
     *
     * <p>
     * Assets are copies of content elements. As a source of the asset, content elements form the link to the upstream
     * content provider. Although much its data is copied to the asset, it still is a necessary link to the original
     * source. The asset is copied from the original source by a purchase transaction on the Smint.io platform.
     * </p>
     *
     * @return the Smint.io ID of the original source this asset is a copy of or {@code null}.
     */
    String getContentElementUuid();


    /**
     * This is a Smint.io internal ID of the asset.
     *
     * <p>
     * With a purchase you actually buy a license to use the asset, hence the name of this value contains "license" and
     * "purchase transaction".
     * </p>
     *
     * @return the ID of the license purchase transaction, meaning this is the ID of the asset.
     */
    String getLicensePurchaseTransactionUuid();


    /**
     * This is a Smint.io internal ID of the asset.
     *
     * <p>
     * Implementing functions need to just call {@link #getLicensePurchaseTransactionUuid()}.
     * </p>
     *
     * @return the return value of {@link #getLicensePurchaseTransactionUuid()}.
     */
    String getUuid();


    /**
     * Provides the unique Smint.io platform ID of the purchase cart this asset was part of.
     *
     * <p>
     * Every purchase consists of a single <em>cart</em>, that collects all assets to pay for in a single transaction.
     * At least there is 1 asset in a cart but the amount is unrestricted. An asset can only be part of a single cart
     * but the cart may contain multiple assets.
     * </p>
     *
     * @return the ID of the purchase or {@code null}.
     */
    String getCartPurchaseTransactionUuid();


    /**
     * Provides the current state of the purchase of the license.
     *
     * <p>
     * Currently supported values are (but might change in the future):
     * </p>
     * <ol>
     * <li>{@code PURCHASE_PHASE_1}</li>
     * <li>{@code PURCHASE_PHASE_2}</li>
     * <li>{@code DOWNLOADING}</li>
     * <li>{@code FINALIZING}</li>
     * <li>{@code COMPLETED}</li>
     * <li>{@code ERROR}</li>
     * <li>{@code CANCELLED}</li>
     * </ol>
     *
     * <p>
     * Such a situation might happen in case the asset has been already purchased in the past and therefore is managed
     * by the sync target. Nevertheless the maximum number of usage/downloads have been reached and more must be
     * purchased. If the purchase is being cancelled for some reason, it will be indicated by this flag.
     * </p>
     *
     * @return the ID of the license purchase transaction.
     */
    LicensePurchaseTransactionStateEnum getState();


    /**
     * Return an key, denoting the content provider.
     *
     * <p>
     * The enumeration key is a simple reading text, which has been defined for each provider as soon as it has been
     * added to the list of supported providers by Smint.io. Its value is arbitrary and uses mixed cases. Non-exhaustive
     * example:
     * </p>
     * <ul>
     * <li>{@code getty}</li>
     * <li>{@code shutterstock}</li>
     * <li>{@code adobestock}</li>
     * <li>{@code istock}</li>
     * <li>{@code panthermedia}</li>
     * </ul>
     *
     * <p>
     * Although a known set of values are used, some might be changed and more be added in the near future without any
     * further notice. So implementing classes must support an arbitrary value.
     * </p>
     *
     * @return an enumeration key or {@code null} if none has been set.
     * @see io.smint.clapi.consumer.integration.core.target.ISyncAsset#setContentProvider(String)
     */
    String getContentProvider();


    /**
     * Provides the content type of this asset.
     *
     * <p>
     * The type value is a string representation of an enumerated value. Although a fixes set of values are used, more
     * might be added in the near future. So implementing classes must support an arbitrary value. Possible values are
     * at the moment:
     * </p>
     * <ul>
     * <li>{@code image}</li>
     * <li>{@code video}</li>
     * <li>{@code audio}</li>
     * <li>{@code document}</li>
     * <li>{@code 3d} - a 3D video</li>
     * <li>{@code template}</li>
     * </ul>
     *
     * @return an enumeration key or {@code null} if none has been set.
     * @see io.smint.clapi.consumer.integration.core.target.ISyncAsset#setContentType(String)
     */
    String getContentType();


    /**
     * Provides the ID for the content category.
     *
     * <p>
     * The ID is a simple reading text. Its value is arbitrary and uses mixed cases. Non-exhaustive example:
     * </p>
     * <ul>
     * <li>{@code creative}</li>
     * <li>{@code editorial}</li>
     * </ul>
     *
     * <p>
     * Although a known set of values are used, some might be changed and more be added in the near future without any
     * further notice. So implementing classes must support an arbitrary value.
     * </p>
     *
     * @return a key of the category of this content or {@code null} if unknown.
     * @see io.smint.clapi.consumer.integration.core.target.ISyncAsset#setContentCategory(String)
     */
    String getContentCategory();


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
     * Provides the localized description for the asset, translated to multiple languages.
     *
     * <p>
     * The possible translations available are defined by the value of
     * {@link io.smint.clapi.consumer.integration.core.configuration.models.ISettingsModel#getImportLanguages()}.
     * However, not all languages might have a valid translation. Languages without a translation might be omitted from
     * the map. Especially if this is a persistent asset and a new language has been added to the list of languages.
     * Then the new language might have not yet been stored to this persistent asset.
     * </p>
     *
     * @return the multi-language description of the asset or {@code null} if none have been set yet.
     */
    Map<Locale, String> getDescription();


    /**
     * Provides a list of localized keywords to be used for keyword searches.
     *
     * <p>
     * The list of keywords are usually provided by the upstream content provider (see {@link #getContentProvider()}).
     * These keywords can be used for keyword searches. However, not all keywords are available for every language.
     * </p>
     *
     * @return the localized list of keywords or {@code null}.
     */
    Map<Locale, String[]> getKeywords();


    /**
     * Provides some release details of this asset, regarding agreements with visible models or property owners.
     *
     * @return release details of this asset or {@code null}.
     */
    ISmintIoReleaseDetails getReleaseDetails();


    /**
     * Provides a localized list of copyright notices to indicate the copyright owner of this asset.
     *
     * <p>
     * The copyright notice usually is provided by the upstream content provider (see {@link #getContentProvider()}).
     * Not all languages may contain proper copyright notice. If the language to display does not have a notice
     * available, use the one for English or the first one available.
     * </p>
     *
     * @return copyrightNotices the localized copyright notices or {@code null}.
     */
    Map<Locale, String> getCopyrightNotices();


    /**
     * Provides the Smint.io ID of the project this asset has been purchased for.
     *
     * <p>
     * On Smint.io platform every purchase is related to a project context. It provides more insights about further
     * purchase, other collections and more. So the project ID is necessary to retrieve this context in the future. Eg.
     * the web view of the asset is composed making use of the project ID (see {@link #getSmintIoUrl()}).
     * </p>
     *
     * @return the Smint.io ID of the project context for this asset or {@code null}.
     * @see #getProjectName()
     */
    String getProjectUuid();


    /**
     * Provides the Smint.io localized name of the project this asset has been purchased for.
     *
     * <p>
     * On Smint.io platform every purchase is related to a project context. It provides more insights about further
     * purchase, other collections and more. So the project ID is necessary to retrieve this context in the future. Eg.
     * the web view of the asset is composed making use of the project ID (see {@link #getSmintIoUrl()}).
     * </p>
     *
     * @return the Smint.io name of the project context for this asset or {@code null}.
     * @see #getProjectUuid()
     */
    Map<Locale, String> getProjectName();


    /**
     * Provides the Smint.io ID of the collection this asset is part of.
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
     * @return the Smint.io collection's ID this asset is part of or {@code null}.
     * @see #getCollectionName()
     */
    String getCollectionUuid();


    /**
     * Provides the Smint.io localized name of the collection this asset is part of.
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
     * @return the Smint.io collection's name this asset is part of or {@code null}.
     * @see #getCollectionUuid()
     */
    Map<Locale, String> getCollectionName();


    /**
     * Provides a Smint.io ID for the licensee.
     *
     * <p>
     * Further information about the licensee can be retrieved from the Smint.io platform using this ID.
     * </p>
     *
     * @return the Smint.io ID of the licensee or {@code null}.
     */
    String getLicenseeUuid();


    /**
     * Provides the name of the licensee.
     *
     * <p>
     * Further information about the licensee may be stored with the Smint.io platform, but at least this name can be
     * used to display the licensee in the UI of the sync target. Usually this is the business name of the licensee.
     * </p>
     *
     * @return the name of the licensee or {@code null}.
     */
    String getLicenseeName();


    /**
     * Provides a general type ID of the kind license applying to this asset.
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
     * @return the key of the type of license or {@code null}.
     */
    String getLicenseType();


    /**
     * Provides a localized text of the license that applies to this asset.
     *
     * <p>
     * The localized text contain a textual summary of the license and maybe additional information. This text is
     * intended to be displayed in the UI along the asset.
     * </p>
     *
     * @return the localized text of the license or {@code null}.
     */
    Map<Locale, String> getLicenseText();


    /**
     * Provides a list of options for the applying licenses.
     *
     * @return the list of options for the license or {@code null}.
     */
    ISmintIoLicenseOptions[] getLicenseOptions();


    /**
     * Provides a list of license terms to apply to this asset.
     *
     * @return the list of license terms of this asset or {@code null}.
     */
    ISmintIoLicenseTerm[] getLicenseTerms();


    /**
     * Provides download constraints to apply to this asset.
     *
     * @return the download constraints of this asset or {@code null}.
     */
    ISmintIoDownloadConstraints getDownloadConstraints();


    /**
     * Defines whether the content is for editorial use only.
     *
     * <p>
     * In case no such information is available, then {@code null} is returned. This is the case if the upstream asset
     * provider did not define this value and did not provide any defaults. Hence it is impossible to determine, whether
     * the asset is for editorial use only or not. If the synchronization target does not require a value, none should
     * be set in these cases. If a valid value (decision) is required, it is up to the sync target to implement a
     * reasonable default to be on the safe side of the legal challenge.
     * </p>
     *
     * @return {@link Boolean#TRUE} in case the asset is for editorial use only and {@link Boolean#FALSE} otherwise.
     *         However, {@code null} is returned if no such information is available. Hence neither is applicable.
     */
    Boolean isEditorialUse();


    /**
     * Defines whether the content has some license restrictions applied.
     *
     * <p>
     * The kind of license restrictions applicable for this asset can be retrieved with {@link #getLicenseTerms()}.
     * </p>
     *
     * @return {@code true} in case any licensing restriction has been applied to the asset or not.
     */
    boolean hasLicenseTerms();


    /**
     * Provides the list of binaries, this asset consists of.
     *
     * <p>
     * Usually an asset contains a single binary, thus the list would consist of a single item. Nevertheless
     * occasionally, more binaries are attached, that contain the same content in different variants. eg: Images of
     * different resolution, videos with banner image, etc. Therefore, each asset may consist of more than one binary.
     * </p>
     *
     * @return a list of attached binaries or {@code null} if none are linked to this asset.
     */
    ISmintIoBinary[] getBinaries();


    /**
     * Provides the Web URL to view the asset on the Smint.io platform in the browser.
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
     * @return the URL to the web page on the Smint.io platform for this asset.
     */
    URL getSmintIoUrl();


    /**
     * Provides the date, when this asset has been purchased.
     *
     * @return the date of purchase of this asset or {@code null}.
     */
    OffsetDateTime getPurchasedAt();


    /**
     * Provides the date, when this asset has been created.
     *
     * @return the date of creation of this asset or {@code null}.
     */
    OffsetDateTime getCreatedAt();


    /**
     * Provides the date, when this asset has been updated the last time.
     *
     * <p>
     * The last update time is not used to compare for updated content. Times seem to be too prune to different settings
     * on servers of Smint.io and sync target. Hence all meta data of assets are updated, once Smint.io detects a
     * change. Binary data uses a simple, increasing counter as version number (see {@link ISmintIoBinary#getVersion()})
     * which can be used to see, which binary is the newest one. Only Smint.io platform will update the binary data,
     * though. Without a single source, increasing counter would not work as they are not unique then.
     * </p>
     *
     * @return the date of last update of this asset or {@code null}.
     */
    OffsetDateTime getLastUpdatedAt();
}

// CHECKSTYLE ON: MethodCount
