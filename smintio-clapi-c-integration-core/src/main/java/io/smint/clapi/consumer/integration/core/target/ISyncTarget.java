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

import io.smint.clapi.consumer.integration.core.contracts.ISmintIoMetadataElement;
import io.smint.clapi.consumer.integration.core.exceptions.SmintIoAuthenticatorException;
import io.smint.clapi.consumer.integration.core.exceptions.SmintIoSyncJobException;


// CHECKSTYLE OFF: MethodCount

/**
 * Interface to implement for each synchronization target.
 *
 * <p>
 * This is the main interface to implement for each target to synchronize with. All other interfaces are provided by
 * this library. So usually integrators do not need to implement other interfaces than this one.
 * </p>
 *
 * <h2>Transmit assets from Smint.io to any DAM (one-way)</h2>
 * <p>
 * The purpose of instances of this interface is to store assets, bought on <a href="https://www.smint.io">Smint.io</a>
 * to the target <em>downstream</em> <a href="https://en.wikipedia.org/wiki/Digital_asset_management">Asset Management
 * System (DAM)</a>. Smint.io is a purchase platform to easy acquiring of digital assets. These assets then can be
 * stored with a local DAM. Instances implementing this interface support automatically storing of assets from Smint.io
 * to a target DAM - in a one-way process, from Smint.io to DAM (Smint.io --&gt;&gt; DAM).
 * </p>
 *
 * <h2>Two-phased synchronization process</h2>
 * <p>
 * At first, structured meta data is synchronized, containing all kinds of meta data types, asset types, licenses, etc.
 * (see {@link #beforeGenericMetadataSync()}, {@link #afterGenericMetadataSync()}). Then binary assets are synchronized
 * (see {@link #beforeAssetsSync()}, {@link #afterAssetsSync()}). This will ensure, all required meta data types and
 * values are available on the target before the binaries are transmitted.
 * </p>
 *
 * <h2>Event driven notification for start and end of sync phases</h2>
 * <p>
 * The synchronization process is driven outside of instances of this interface. However, these are notified of some
 * events, eg: as soon as the overall sync process starts (see {@link #beforeSync()}) or ends (see
 * {@link #afterSync()}). The same applies to each phase. At every start of a phase (see
 * {@link #beforeGenericMetadataSync()}, {@link #beforeAssetsSync()}) instances may abort the synchronization process
 * immediately. At the end of every phase (see {@link #afterGenericMetadataSync()}, {@link #afterAssetsSync()})
 * instances may perform some clean-up.
 * </p>
 *
 * <p>
 * At the moment, cleaning of the meta data cache is requested by calling {@link #clearGenericMetadataCaches()}. This is
 * executed at the end of the sync process. However, the meta data cache might be called at any time. The asset sync
 * phase must not rely on an available and filled cache as a special sync task might be started on demand to synchronize
 * assets only - without any meta data.
 * </p>
 *
 * <h2>Two kinds of events for initiating an synchronization process</h2>
 * <p>
 * There are two kinds of events initiating a synchronization, as outlined in
 * {@link io.smint.clapi.consumer.integration.core.ISmintIoSynchronization}. The scheduled synchronization will sync
 * meta data, whereas the pushed kind of sync in case of a purchase will not.
 * </p>
 *
 * <h2>Parallel execution of imports</h2>
 * <p>
 * When implementing a class, beware that all independent meta data may be imported in parallel by different threads.
 * Please do not impose blocking of multiple threads to the implementing class. Instead support calling different
 * imports by different threads at the same time and synchronize on the data level.<br>
 * eg: calling {@link #importBinaryTypes(ISmintIoMetadataElement[])} and
 * {@link #importContentTypes(ISmintIoMetadataElement[])} could be parallelized as these handle distinct, independent
 * content.
 * </p>
 *
 * <h2 id="import-meta-information">Importing meta data information</h2>
 * <p>
 * Meta data is imported in categories, whereas each category will receive a list of Smint.io values as instances of
 * {@link ISmintIoMetadataElement}. The {@link ISmintIoMetadataElement#getKey()} of the list item will return the
 * Smint.io API key for that item and the {@link ISmintIoMetadataElement#getValues()} will hold a list of localized
 * names for that item. The localized name can be used with the UI. The list is not sorted in any way. See an example
 * with JSON.
 * </p>
 *
 * <p>
 * For each Smint.io key (see {@link ISmintIoMetadataElement#getKey()}) a sync target ID must be created and a mapping
 * must be maintained. The sync target ID will be used with assets to import or update. Its value will be retrieved for
 * every Smint.io key! (eg: see {@link #getContentProviderKey(String)}). In case the Smint.io key already exists, just
 * update/overwrite the localized names retrieved by {@link ISmintIoMetadataElement#getValues()}. Implementing classes
 * must not create new sync target ID. It would lead to big mess with the meta data, as the strictly 1:1 relation
 * between these keys would be broken.
 * </p>
 *
 * <p>
 * Whenever meta data is synchronized, ALL data will be written to the sync target. There is no detection of already
 * existing meta data on the sync target, as the metadata items are so limited in list size that additional checks would
 * impose more performance penalty than any improvements. So, upon importing meta data, expect to some across already
 * existing Smint.io key. This is perfectly natural and expected behavior. However, implementors must not delete keys on
 * sync target that are not passed to the importing functions. These keys might still be used with assets. Deleting them
 * without any checks would break their meta information.
 * </p>
 *
 */
public interface ISyncTarget {

    /**
     * Provides information about features this sync target supports and thus is capable of.
     *
     * @return Must not return {@code null} but always a valid instance!
     */
    ISyncTargetCapabilities getCapabilities();


    /**
     * A hook to be called before any synchronization is started in this turn.
     *
     * <p>
     * This hook is called right before any synchronization takes place. If some preparations or are to be made in
     * advance, a task must be returned. If synchronization does not need to be prepared or any status checked or
     * altered, then simply return an empty task, doing nothing.
     * </p>
     *
     * <p>
     * Although this task is executed in an asynchronous way, synchronization is only started after this tasks ends
     * successfully. In case the tasks aborts, crashes or ends unsuccessfully, synchronization is terminated immediately
     * with proper logging. Such ending usually is indicated by throwing an exception.
     * </p>
     *
     * <p>
     * Any exception thrown will indicate a faulty ending, immediately aborting the overall synchronization. So make
     * sure all exceptions to be ignored are caught within the task.
     * </p>
     *
     * @return A boolean value, indicating whether to continue synchronization or not. {@code true} means,
     *         synchronization will continue, whereas {@code false} aborts the sync task without further notice.
     */
    boolean beforeSync();


    /**
     * Is called right before the generic meta data is about to be synced but after {@link #beforeSync()}.
     *
     * <p>
     * This hook is only called in case the generic meta data is to be synchronized. Usually this is done on a regular
     * and scheduled basis. Additionally to a re-scheduled background task for synchronization asset content is synced
     * on demand - without any generic meta data.
     * </p>
     *
     * <p>
     * Although this task is executed in an asynchronous way, synchronization is only started after this tasks ends
     * successfully. In case the tasks returns {@code false} synchronization of generic meta data is aborted
     * immediately. In case the task crashes with an exception, overall synchronization is terminated without syncing
     * any asset.
     * </p>
     *
     * <p>
     * Any exception thrown will indicate a faulty ending, immediately aborting the overall synchronization. So make
     * sure all exceptions to be ignored are caught within the task.
     * </p>
     *
     * @return A boolean value, indicating whether to continue meta data synchronization or not. {@code true} means,
     *         synchronization will continue, whereas {@code false} aborts the sync task without further notice and
     *         without calling {@link #afterGenericMetadataSync()}.
     */
    boolean beforeGenericMetadataSync();


    /**
     * Import the provided list of content providers.
     *
     * <p>
     * For general information about meta data import, see <a href="#import-meta-information">Import Meta Data
     * Information</a> in the heading section above. Example data for this meta data category is:
     * </p>
     *
     * <pre>
     * [
     *     {
     *         "getKey": "getty",
     *         "getValues": {
     *             "en": "Getty Images",
     *             "de": "Getty Images"
     *         }
     *     },
     *     {
     *         "getKey": "istock",
     *         "getValues": {
     *             "en": "iStock",
     *             "de": "iStock"
     *         }
     *     },
     *     {
     *         "getKey": "shutterstock",
     *         "getValues": {
     *             "en": "Shutterstock",
     *             "de": "Shutterstock"
     *         }
     *     },
     *     {
     *         "getKey": "adobestock",
     *         "getValues": {
     *             "en": "Adobe Stock",
     *             "de": "Adobe Stock"
     *         }
     *     },
     *     {
     *         "getKey": "panthermedia",
     *         "getValues()": {
     *             "en": "PantherMedia",
     *             "de": "PantherMedia"
     *         }
     *     }
     * ]
     * </pre>
     *
     * @param contentProviders the list of content providers used with Smint.io.
     */
    void importContentProviders(ISmintIoMetadataElement[] contentProviders);


    /**
     * Maps the Smint.io ID of a <em>Content Provider</em> to a synchronization target ID/key.
     *
     * <p>
     * Each available content provider in the Smint.io platform needs a corresponding representation of the target side.
     * This is necessary to track the origin of assets. Hence a one-to-one mapping is required. Therefore a direct
     * translation of the IDs on both platforms (Smint.io + sync target) is necessary.
     * </p>
     *
     * <p>
     * Implementation of {@link io.smint.clapi.consumer.integration.core.jobs.ISyncJob} will make use of this target ID
     * to provide prepared assets that can be easily compared to the data already available on the sync target.
     * </p>
     *
     * <p>
     * The Smin.io ID to map is retrieved from
     * {@link io.smint.clapi.consumer.integration.core.contracts.ISmintIoAsset#getContentProvider()}.
     * </p>
     *
     * @param smintIoProviderId the ID on the Smint.io platform.
     * @return the key on the synchronization target, or {@code null} in case the item does not exist yet on the target
     *         or if parameter the is {@code null}.
     * @see #getContentProviderKey(String)
     * @see io.smint.clapi.consumer.integration.core.contracts.ISmintIoAsset#getContentProvider()
     */
    String getContentProviderKey(String smintIoProviderId);


    /**
     * Import the provided list of content types.
     *
     * <p>
     * For general information about meta data import, see <a href="#import-meta-information">Import Meta Data
     * Information</a> in the heading section above. Example data for this meta data category is:
     * </p>
     *
     * <pre>
     * [
     *     {
     *         "getKey": "image",
     *         "getValues": {
     *             "en": "Image",
     *             "de": "Bild"
     *         }
     *     },
     *     {
     *         "getKey": "video",
     *         "getValues": {
     *             "en": "Video",
     *             "de": "Video"
     *         }
     *     },
     *     {
     *         "getKey": "audio",
     *         "getValues": {
     *             "en": "Audio",
     *             "de": "Audio"
     *         }
     *     },
     *     {
     *         "getKey": "document",
     *         "getValues": {
     *             "en": "Text",
     *             "de": "Text"
     *         }
     *     },
     *     {
     *         "getKey": "template",
     *         "getValues": {
     *             "en": "Template",
     *             "de": "Vorlage"
     *         }
     *     },
     *     {
     *         "getKey": "3d",
     *         "getValues()": {
     *             "en": "3D",
     *             "de": "3D"
     *         }
     *     }
     * ]
     * </pre>
     *
     * @param contentTypes the list of content types used with Smint.io.
     */
    void importContentTypes(ISmintIoMetadataElement[] contentTypes);


    /**
     * Maps the Smint.io ID of a <em>Content Type</em> to a synchronization target ID/key.
     *
     * <p>
     * The Smin.io ID to map is retrieved from
     * {@link io.smint.clapi.consumer.integration.core.contracts.ISmintIoBinary#getContentType()}.
     * </p>
     *
     * @param smintIoContentTypeId the ID on the Smint.io platform.
     * @return the key on the synchronization target, or {@code null} in case the item does not exist yet on the target
     *         or if parameter the is {@code null}.
     * @see #getContentProviderKey(String)
     * @see io.smint.clapi.consumer.integration.core.contracts.ISmintIoBinary#getContentType()
     */
    String getContentTypeKey(String smintIoContentTypeId);


    void importBinaryTypes(ISmintIoMetadataElement[] binaryTypes);


    /**
     * Maps the Smint.io ID of a <em>Binary Type</em> to a synchronization target ID/key.
     *
     * <p>
     * The Smin.io ID to map is retrieved from
     * {@link io.smint.clapi.consumer.integration.core.contracts.ISmintIoBinary#getBinaryType()}.
     * </p>
     *
     * @param smintIoId the ID on the Smint.io platform.
     * @return the key on the synchronization target, or {@code null} in case the item does not exist yet on the target
     *         or if parameter the is {@code null}.
     * @see #getContentProviderKey(String)
     * @see io.smint.clapi.consumer.integration.core.contracts.ISmintIoBinary#getBinaryType()
     */
    String getBinaryTypeKey(String smintIoId);

    void importContentCategories(ISmintIoMetadataElement[] contentCategories);


    /**
     * Maps the Smint.io ID of a <em>Content Category</em> to a synchronization target ID/key.
     *
     * @param smintIoContentCategoryId the ID on the Smint.io platform.
     * @return the key on the synchronization target, or {@code null} in case the item does not exist yet on the target.
     * @throws NullPointerException if parameter {@code smintIoContentCategoryId} is {@code null}.
     * @see #getContentProviderKey(String)
     */
    String getContentCategoryKey(String smintIoContentCategoryId);


    void importLicenseTypes(ISmintIoMetadataElement[] licenseTypes);


    /**
     * Maps the Smint.io ID of a <em>License Type</em> to a synchronization target ID/key.
     *
     * @param smintIoLicenseTypeId the ID on the Smint.io platform.
     * @return the key on the synchronization target, or {@code null} in case the item does not exist yet on the target.
     * @throws NullPointerException if parameter is {@code null}.
     * @see #getContentProviderKey(String)
     */
    String getLicenseTypeKey(String smintIoLicenseTypeId);


    void importReleaseStates(ISmintIoMetadataElement[] releaseStates);


    /**
     * Maps the Smint.io ID of a <em>Release State</em> to a synchronization target ID/key.
     *
     * <p>
     * The Smin.io ID to map is retrieved from
     * {@link io.smint.clapi.consumer.integration.core.contracts.ISmintIoReleaseDetails#getModelReleaseState()}.
     * </p>
     *
     * @param smintIoReleaseStateId the ID on the Smint.io platform.
     * @return the key on the synchronization target, or {@code null} in case the item does not exist yet on the target
     *         or if parameter the is {@code null}.
     * @see #getContentProviderKey(String)
     * @see io.smint.clapi.consumer.integration.core.contracts.ISmintIoReleaseDetails#getModelReleaseState()
     */
    String getReleaseStateKey(String smintIoReleaseStateId);


    void importLicenseExclusivities(ISmintIoMetadataElement[] licenseExclusivities);


    /**
     * Maps the Smint.io ID of a license <em>Exclusivity</em> to a synchronization target ID/key.
     *
     * <p>
     * The Smin.io ID to map is retrieved from
     * {@link io.smint.clapi.consumer.integration.core.contracts.ISmintIoLicenseTerm#getExclusivities()}.
     * </p>
     *
     * @param smintIoId the ID on the Smint.io platform or {@code null}.
     * @return the key on the synchronization target, or {@code null} in case the item does not exist yet on the target
     *         or the parameter is {@code null}.
     * @see #getContentProviderKey(String)
     * @see io.smint.clapi.consumer.integration.core.contracts.ISmintIoLicenseTerm#getExclusivities()
     */
    String getLicenseExclusivityKey(final String smintIoId);


    void importLicenseUsages(ISmintIoMetadataElement[] licenseUsages);


    /**
     * Maps the Smint.io ID of a <em>License Usage</em> to a synchronization target ID/key.
     *
     * <p>
     * The Smin.io ID to map is retrieved from
     * {@link io.smint.clapi.consumer.integration.core.contracts.ISmintIoLicenseTerm#getAllowedUsages()}.
     * </p>
     *
     * @param smintIoId the ID on the Smint.io platform.
     * @return the key on the synchronization target, or {@code null} in case the item does not exist yet on the target
     *         or if parameter the is {@code null}.
     * @see #getContentProviderKey(String)
     * @see io.smint.clapi.consumer.integration.core.contracts.ISmintIoLicenseTerm#getAllowedUsages()
     */
    String getLicenseUsageKey(String smintIoId);


    void importLicenseSizes(ISmintIoMetadataElement[] licenseSizes);


    /**
     * Maps the Smint.io ID of a <em>License Size</em> to a synchronization target ID/key.
     *
     * <p>
     * The Smin.io ID to map is retrieved from
     * {@link io.smint.clapi.consumer.integration.core.contracts.ISmintIoLicenseTerm#getAllowedSizes()}.
     * </p>
     *
     * @param smintIoId the ID on the Smint.io platform.
     * @return the key on the synchronization target, or {@code null} in case the item does not exist yet on the target
     *         or if parameter the is {@code null}.
     * @see #getContentProviderKey(String)
     * @see io.smint.clapi.consumer.integration.core.contracts.ISmintIoLicenseTerm#getAllowedSizes()
     */
    String getLicenseSizeKey(String smintIoId);


    void importLicensePlacements(ISmintIoMetadataElement[] licensePlacements);


    /**
     * Maps the Smint.io ID of a <em>License Placement</em> to a synchronization target ID/key.
     *
     * <p>
     * The Smin.io ID to map is retrieved from
     * {@link io.smint.clapi.consumer.integration.core.contracts.ISmintIoLicenseTerm#getAllowedPlacements()}.
     * </p>
     *
     * @param smintIoId the ID on the Smint.io platform.
     * @return the key on the synchronization target, or {@code null} in case the item does not exist yet on the target
     *         or if parameter the is {@code null}.
     * @see #getContentProviderKey(String)
     * @see io.smint.clapi.consumer.integration.core.contracts.ISmintIoLicenseTerm#getAllowedPlacements()
     */
    String getLicensePlacementKey(String smintIoId);


    void importLicenseDistributions(ISmintIoMetadataElement[] licenseDistributions);


    /**
     * Maps the Smint.io ID of a <em>License Distribution</em> to a synchronization target ID/key.
     *
     * <p>
     * The Smin.io ID to map is retrieved from
     * {@link io.smint.clapi.consumer.integration.core.contracts.ISmintIoLicenseTerm#getAllowedDistributions()}.
     * </p>
     *
     * @param smintIoId the ID on the Smint.io platform.
     * @return the key on the synchronization target, or {@code null} in case the item does not exist yet on the target
     *         or if parameter the is {@code null}.
     * @see #getContentProviderKey(String)
     * @see io.smint.clapi.consumer.integration.core.contracts.ISmintIoLicenseTerm#getAllowedDistributions()
     */
    String getLicenseDistributionKey(String smintIoId);


    void importLicenseGeographies(ISmintIoMetadataElement[] licenseGeographies);


    /**
     * Maps the Smint.io ID of a <em>License Geography</em> to a synchronization target ID/key.
     *
     * <p>
     * The Smin.io ID to map is retrieved from
     * {@link io.smint.clapi.consumer.integration.core.contracts.ISmintIoLicenseTerm#getAllowedGeographies()}.
     * </p>
     *
     * @param smintIoId the ID on the Smint.io platform.
     * @return the key on the synchronization target, or {@code null} in case the item does not exist yet on the target
     *         or if parameter the is {@code null}.
     * @see #getContentProviderKey(String)
     * @see io.smint.clapi.consumer.integration.core.contracts.ISmintIoLicenseTerm#getAllowedGeographies()
     */
    String getLicenseGeographyKey(String smintIoId);


    void importLicenseIndustries(ISmintIoMetadataElement[] licenseIndustries);


    /**
     * Maps the Smint.io ID of a <em>License Industry</em> to a synchronization target ID/key.
     *
     * <p>
     * The Smin.io ID to map is retrieved from
     * {@link io.smint.clapi.consumer.integration.core.contracts.ISmintIoLicenseTerm#getAllowedIndustries()}.
     * </p>
     *
     * @param smintIoId the ID on the Smint.io platform.
     * @return the key on the synchronization target, or {@code null} in case the item does not exist yet on the target
     *         or if parameter the is {@code null}.
     * @see #getContentProviderKey(String)
     * @see io.smint.clapi.consumer.integration.core.contracts.ISmintIoLicenseTerm#getAllowedIndustries()
     */
    String getLicenseIndustryKey(String smintIoId);


    /**
     * Import the provided list of languages a license for an asset is restricted to.
     *
     * <p>
     * The usage of assets may be limited to certain languages or certain languages may be excluded. Smint.io keeps a
     * list of all languages ever used within licensing on the platform as meta data. This list is provided here and
     * linked to if such a restriction is applicable.
     * </p>
     *
     * <p>
     * For general information about meta data import, see <a href="#import-meta-information">Import Meta Data
     * Information</a> in the heading section above. Example data for this meta data category is:
     * </p>
     *
     * <pre>
     * [
     *     {
     *         "Key": "ara",
     *         "Values": {
     *             "de": "Arabisch",
     *             "en": "Arabic"
     *         }
     *     },
     *     {
     *         "Key": "rus",
     *         "Values": {
     *             "de": "Russisch",
     *             "en": "Russian"
     *         }
     *     },
     *     {
     *         "Key": "ben",
     *         "Values": {
     *             "de": "Bengali",
     *             "en": "Bengali"
     *         }
     *     },
     *     {
     *         "Key": "eng",
     *         "Values": {
     *             "de": "Englisch",
     *             "en": "English"
     *         }
     *     },
     *     {
     *         "Key": "spa",
     *         "Values": {
     *             "de": "Spanisch",
     *             "en": "Spanish"
     *         }
     *     },
     *     {
     *         "Key": "hns",
     *         "Values": {
     *             "de": "Hindi",
     *             "en": "Hindustani"
     *         }
     *     },
     *     {
     *         "Key": "fra",
     *         "Values": {
     *             "de": "Französisch",
     *             "en": "French"
     *         }
     *     },
     *     {
     *         "Key": "ger",
     *         "Values": {
     *             "de": "Deutsch",
     *             "en": "German"
     *         }
     *     },
     *     {
     *         "Key": "cmn",
     *         "Values": {
     *             "de": "Mandarin",
     *             "en": "Mandarin"
     *         }
     *     },
     *     {
     *         "Key": "any",
     *         "Values": {
     *             "de": "Alle",
     *             "en": "Any"
     *         }
     *     },
     *     {
     *         "Key": "por",
     *         "Values": {
     *             "de": "Portugiesisch",
     *             "en": "Portuguese"
     *         }
     *     },
     *     {
     *         "Key": "msa",
     *         "Values": {
     *             "de": "Malaysisch",
     *             "en": "Malay"
     *         }
     *     }
     * ]
     * </pre>
     *
     * <p>
     * Each {@link ISmintIoMetadataElement#getKey()} will return a valid
     * <a href="https://en.wikipedia.org/wiki/ISO_639-3">ISO 639-3 code</a>. Smint.io uses other codes internally with
     * its RESTful API but these codes are translated to their ISO counterparts by this library.<br>
     * Although this library is delivering the same localized text as used with Smint.io web page, it would be better to
     * use the name, available to your system - if there is some. So to get the proper localized name from "Mandarin" by
     * using something like {@code new Locale("cmn").getDisplayLanguage()}. However there might not be translations for
     * all languages available with your Java VM. So either use the values provided here, or use an external library for
     * such cases.
     * </p>
     *
     * @param licenseLanguages the list of languages a license may restrict the usage of assets to.
     */
    void importLicenseLanguages(ISmintIoMetadataElement[] licenseLanguages);


    /**
     * Maps the Smint.io ID of a <em>License Language</em> to a synchronization target ID/key.
     *
     * <p>
     * The Smin.io ID to map is retrieved from
     * {@link io.smint.clapi.consumer.integration.core.contracts.ISmintIoLicenseTerm#getAllowedLanguages()}.
     * </p>
     *
     * <p>
     * Beware that the value "{@code any}" is used in addition to the ISO 639-3 codes, in order to denote <em>any</em>
     * language. There is not ISO code for that case.
     * </p>
     *
     * @param smintIoId the ID on the Smint.io platform.
     * @return the key on the synchronization target, or {@code null} in case the item does not exist yet on the target
     *         or if parameter the is {@code null}.
     * @see #getContentProviderKey(String)
     * @see io.smint.clapi.consumer.integration.core.contracts.ISmintIoLicenseTerm#getAllowedLanguages()
     */
    String getLicenseLanguageKey(String smintIoId);


    void importLicenseUsageLimits(ISmintIoMetadataElement[] licenseUsageLimits);


    /**
     * Maps the Smint.io ID of a <em>License Usage Limit</em> to a synchronization target ID/key.
     *
     * <p>
     * The Smin.io ID to map is retrieved from
     * {@link io.smint.clapi.consumer.integration.core.contracts.ISmintIoLicenseTerm#getUsageLimits()}.
     * </p>
     *
     * @param smintIoId the ID on the Smint.io platform.
     * @return the key on the synchronization target, or {@code null} in case the item does not exist yet on the target
     *         or if parameter the is {@code null}.
     * @see #getContentProviderKey(String)
     * @see io.smint.clapi.consumer.integration.core.contracts.ISmintIoLicenseTerm#getUsageLimits()
     */
    String getLicenseUsageLimitKey(String smintIoId);


    /**
     * After the synchronization of all generic meta data this is called, before any syncing of assets.
     *
     * <p>
     * This hook is only called in case the generic meta data has been synchronized without any exception.
     * </p>
     *
     * <p>
     * Although this task is executed in an asynchronous way, synchronization of assets only starts after this tasks
     * ends successfully. Any aborts, crashes or unsuccessfully endings are aborting the overall synchronization process
     * immediately - without syncing assets. Such ending usually is indicated by throwing an exception.
     * </p>
     *
     * <p>
     * Any exception thrown will indicate a faulty ending, immediately aborting the overall synchronization. So make
     * sure all exceptions to be ignored are caught within the task.
     * </p>
     */
    void afterGenericMetadataSync();


    /**
     * Factory function to create a new instance representing target's data structure for a <em>Binary Assets</em>.
     *
     * <p>
     * The newly created instance MUST NOT be stored automatically on the sync target prior to returning it. For storing
     * this instance, it will be passed to this sync target via {@link #importNewTargetAssets(ISyncBinaryAsset[])}.
     * </p>
     *
     * @return must return a fresh instance for every call and must not ever return {@code null}.
     * @see ISyncAsset
     */
    ISyncBinaryAsset createSyncBinaryAsset();


    /**
     * Factory function to create a new instance representing target's compound assets.
     *
     * <p>
     * The newly created instance MUST NOT be stored automatically on the sync target prior to returning it. For storing
     * this instance, it will be passed to this sync target via
     * {@link #importNewTargetCompoundAssets(ISyncCompoundAsset[])}.
     * </p>
     *
     * @return must return a fresh instance for every call and must not ever return {@code null}.
     * @see ISyncAsset
     */
    ISyncCompoundAsset createSyncCompoundAsset();


    /**
     * Factory function to create a new instance representing target's lincense option data structure.
     *
     * <p>
     * The newly created instance SHOULD NOT be stored automatically on the sync target prior to returning it. For
     * storing this instance, it will be attached to an asset via
     * {@link ISyncAsset#setLicenseOptions(ISyncLicenseOption[])} and thus passed to this sync target via one of
     * {@link #importNewTargetAssets(ISyncBinaryAsset[])}, {@link #importNewTargetCompoundAssets(ISyncCompoundAsset[])},
     * {@link #updateTargetAssets(ISyncBinaryAsset[])}, {@link #updateTargetCompoundAssets(ISyncCompoundAsset[])}
     * </p>
     *
     * @return must return a fresh instance for every call and must not ever return {@code null}.
     */
    ISyncLicenseOption createSyncLicenseOption();


    /**
     * Factory function to create a new instance representing target's lincense terms data structure.
     *
     * <p>
     * The newly created instance SHOULD NOT be stored automatically on the sync target prior to returning it. For
     * storing this instance, it will be attached to an asset via {@link ISyncAsset#setLicenseTerms(ISyncLicenseTerm[])}
     * and thus passed to this sync target via one of {@link #importNewTargetAssets(ISyncBinaryAsset[])},
     * {@link #importNewTargetCompoundAssets(ISyncCompoundAsset[])}, {@link #updateTargetAssets(ISyncBinaryAsset[])},
     * {@link #updateTargetCompoundAssets(ISyncCompoundAsset[])}
     * </p>
     *
     * @return must return a fresh instance for every call and must not ever return {@code null}.
     */
    ISyncLicenseTerm createSyncLicenseTerm();


    /**
     * Factory function to create a new instance representing target's release details structure.
     *
     * <p>
     * The newly created instance SHOULD NOT be stored automatically on the sync target prior to returning it. For
     * storing this instance, it will be attached to an asset via
     * {@link ISyncAsset#setReleaseDetails(ISyncReleaseDetails)} and thus passed to this sync target via one of
     * {@link #importNewTargetAssets(ISyncBinaryAsset[])}, {@link #importNewTargetCompoundAssets(ISyncCompoundAsset[])},
     * {@link #updateTargetAssets(ISyncBinaryAsset[])}, {@link #updateTargetCompoundAssets(ISyncCompoundAsset[])}
     * </p>
     *
     * @return must return a fresh instance for every call and must not ever return {@code null}.
     */
    ISyncReleaseDetails createSyncReleaseDetails();


    /**
     * Factory function to create a new instance representing target's downloads constraints data structure.
     *
     * <p>
     * The newly created instance SHOULD NOT be stored automatically on the sync target prior to returning it. For
     * storing this instance, it will be attached to an asset via
     * {@link ISyncAsset#setDownloadConstraints(ISyncDownloadConstraints)} and thus passed to this sync target via one
     * of {@link #importNewTargetAssets(ISyncBinaryAsset[])},
     * {@link #importNewTargetCompoundAssets(ISyncCompoundAsset[])}, {@link #updateTargetAssets(ISyncBinaryAsset[])},
     * {@link #updateTargetCompoundAssets(ISyncCompoundAsset[])}
     * </p>
     *
     * @return must return a fresh instance for every call and must not ever return {@code null}.
     */
    ISyncDownloadConstraints createSyncDownloadConstraints();


    /**
     * Is called right before assets are about to be synced but after {@link #beforeSync()} and after
     * {@link #afterGenericMetadataSync()}.
     *
     * <p>
     * This hook is only called right before any assets are to be synced.
     * </p>
     *
     * <p>
     * Any exception thrown will indicate a faulty ending, immediately aborting the overall synchronization. So make
     * sure all exceptions to be ignored are caught within the task.
     * </p>
     *
     * @return A boolean value, indicating whether to continue asset synchronization or not. {@code true} means,
     *         synchronization will continue, whereas {@code false} aborts the sync task without further notice and
     *         without calling {@link #afterAssetsSync()} or {@link #afterSync()}.
     */
    boolean beforeAssetsSync();


    /**
     * Maps the Smint.io ID of a <em>Compound Asset</em> to a synchronization target ID/key.
     *
     * @param assetUuid the ID of the compound asset on the Smint.io platform.
     * @return the key on the synchronization target, or {@code null} in case the element does not exist yet on the
     *         target.
     * @throws NullPointerException if parameter is {@code null}.
     * @see #getContentProviderKey(String)
     * @see ISyncAsset
     */
    String getTargetCompoundAssetUuid(String assetUuid);


    /**
     * Maps the Smint.io ID of a <em>Binary Asset</em> to a synchronization target ID/key.
     *
     * @param assetUuid  the ID of the asset on the Smint.io platform.
     * @param binaryUuid the ID of the binary data on the Smint.io platform.
     * @return the key on the synchronization target, or {@code null} in case the element does not exist yet on the
     *         target.
     * @throws NullPointerException if parameter is {@code null}.
     * @see #getContentProviderKey(String)
     * @see ISyncAsset
     */
    String getTargetAssetBinaryUuid(String assetUuid, String binaryUuid);


    /**
     * Stores new entries for assets on the synchronization target, these must be created on the target.
     *
     * <p>
     * Creating new entries is only called for new assets available on the Smin.io platform but not yet on the sync
     * target. Implementing classes can be sure, these items do not exist yet and do not need to perform any checks for
     * duplicates.
     * </p>
     *
     * @param newTargetAssets the list of new assets to create on the sync target. Ignore in case the list is empty ore
     *                        {@code null}.
     */
    void importNewTargetAssets(ISyncBinaryAsset[] newTargetAssets);


    /**
     * Updates already existing entries for assets on the synchronization target.
     *
     * <p>
     * Updating entries is only called for already existing assets on both sides (Smint.io + sync target). However, no
     * information is available which data of an asset has changed. Hence implementing classes should check binaries for
     * changes, too - based on the version number of the binary (see
     * {@link io.smint.clapi.consumer.integration.core.contracts.ISmintIoBinary#getVersion()}).
     * </p>
     *
     * @param updatedTargetAssets the list of updates, existing assets to create on the sync target. Ignore in case the
     *                            list is empty ore {@code null}.
     */
    void updateTargetAssets(ISyncBinaryAsset[] updatedTargetAssets);


    /**
     * Creates new entries for assets on the synchronization target.
     *
     * <p>
     * Creating new entries is only called for new assets available on the Smin.io platform but not yet on the sync
     * target. Implementing classes can be sure, these items do not exist yet and do not need to perform any checks for
     * duplicates.
     * </p>
     *
     * @param newTargetCompoundAssets the list of new compound assets to create on the sync target. Ignore in case the
     *                                list is empty ore {@code null}.
     */
    void importNewTargetCompoundAssets(ISyncCompoundAsset[] newTargetCompoundAssets);


    /**
     * Updates already existing entries for compound assets on the synchronization target.
     *
     * <p>
     * Updating entries is only called for already existing assets on both sides (Smint.io + sync target). However, no
     * information is available which data of an asset has changed. Hence implementing classes should check binaries for
     * changes, too - based on the version number of the binary (see
     * {@link io.smint.clapi.consumer.integration.core.contracts.ISmintIoBinary#getVersion()}).
     * </p>
     *
     * @param updatedTargetCompoundAssets the list of updates, existing assets to create on the sync target. Ignore in
     *                                    case the list is empty ore {@code null}.
     */
    void updateTargetCompoundAssets(ISyncCompoundAsset[] updatedTargetCompoundAssets);


    /**
     * After the synchronization of all assets this is called, but before {@link #afterAssetsSync()}.
     *
     * <p>
     * Any exception thrown will indicate a faulty ending, immediately aborting the overall synchronization without
     * calling {@link #afterSync()}. So make sure all exceptions to be ignored are caught within the task.
     * </p>
     */
    void afterAssetsSync();


    /**
     * Is called in case an Smint.io API authentication exception has occurred.
     *
     * <p>
     * This function is the final call after an authentication exception occurred. No resumption is possible. The idea
     * is, to perform some clean-up and notify the user or the system administrator, that an exception has occurred.
     * After that, the synchronizing is terminated immediately - even without calling
     * {@link #clearGenericMetadataCaches()}.
     * </p>
     *
     * <p>
     * Authentication to the Smint.io API is performed right before the start of the synchronization process. In case an
     * error occurs, authorization is tried a second time. So most of the time authentication exceptions happen here.
     * Nevertheless in theory it is possible that the authentication becomes invalid due to session timeout after a long
     * pause. Then a refresh of the authentication token is tried. In the event that re-authentication fails, this
     * function is called in the course of a running synchronization process. So you never know, when shit happens.
     * Nevertheless, since synchronization is no task with big pauses or rest periods, the authenticated session should
     * not expire easily and thus most of the time this exception will occur right at the start of the sync process.
     * </p>
     *
     * @param exception the exception that has been caught in the synchronization process. The handler might take
     *                  advantage of more information about the exception to handle.
     */
    void handleAuthenticatorException(SmintIoAuthenticatorException exception);


    /**
     * Is called in case any exception in the course of synchronizing occurs, besides an authentication exception.
     *
     * <p>
     * This function is the final call after any exception occurred. No resumption is possible. The idea is, to perform
     * some clean-up and notify the user or the system administrator, that an exception has occurred. After that, the
     * synchronizing is terminated immediately - even without calling {@link #clearGenericMetadataCaches()}.
     * </p>
     *
     * <p>
     * Whenever any of the synchronization functions (like {@link #importNewTargetAssets(ISyncBinaryAsset[])}) throws an
     * exception, synchronization is terminated immediately. Eventually this function is called but not other clean-up
     * utility function, like {@link #clearGenericMetadataCaches()} or any {@code after...} function. So in case of any
     * exception, this handler is the final chance to perform clean-up and prepare everything for the next run to work
     * better.
     * </p>
     *
     * @param exception the exception that has been caught in the synchronization process. The handler might take
     *                  advantage of more information about the exception to handle.
     */
    void handleSyncJobException(SmintIoSyncJobException exception);


    /**
     * A hook to be called after all synchronization took place.
     *
     * <p>
     * After all synchronization took place, some clean-up might be necessary, besides
     * {@link #clearGenericMetadataCaches()}. This is executed after all synchronization has ended but <i>before</i>
     * {@link #clearGenericMetadataCaches()} is called. So the running task still has access to all the created caches.
     * </p>
     *
     * <p>
     * Any exception thrown will indicate a faulty ending, immediately aborting the overall synchronization. Since this
     * is the last task to perform at the end of the synchronization, this might be negligible.
     * </p>
     */
    void afterSync();


    /**
     * Clears all cached generic meta data.
     *
     * <p>
     * During synchronization of license information, asset types, release states and content categories, a lot of meta
     * data is usually cached locally to avoid multiple remote lookups. This speeds-up the synchronization of these meta
     * data items. But before synchronization of content takes place and at the end of each synchronization process,
     * this cache is cleared. In the former case it will guarantee a fresh start and will avoid any errors due to
     * failure in meta data sync process. The latter releases all memory consumed by the cache.
     * </p>
     */
    void clearGenericMetadataCaches();
}

// CHECKSTYLE OFF: MethodCount
