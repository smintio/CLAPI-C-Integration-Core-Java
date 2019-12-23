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
import io.smint.clapi.consumer.integration.core.target.impl.BaseSyncAsset;


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
 * <p>
 * During synchronization of the meta data, an internal mapping of meta data's Smint.io API keys and sync target IDs are
 * created. This mapping is vital for feeding the meta data sync target UUID to the assets to reference these values.
 * This mapping is kept in memory as long as the synchronization is being scheduled. Nevertheless if this mapping is
 * missing, a meta data sync is enforced to create it.
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
 * <h2 id="import-meta-information">Importing meta data information - set sync target ID on meta data item</h2>
 * <p>
 * Meta data is imported in categories, whereas each category will receive a list of Smint.io values as instances of
 * {@link ISmintIoMetadataElement}. The {@link ISmintIoMetadataElement#getKey()} of the list item will return the
 * Smint.io API key for that item and the {@link ISmintIoMetadataElement#getValues()} will hold a list of localized
 * names for that item. The localized name can be used with the UI.
 * </p>
 *
 * <p>
 * The list is not sorted in any way. See an example with JSON.
 * </p>
 *
 * <h3>Set sync target ID on meta data item</h3>
 * <p>
 * For each Smint.io key (see {@link ISmintIoMetadataElement#getKey()}) a sync target ID must be created and stored with
 * the meta data element by setting it to {@link ISmintIoMetadataElement#setTargetMetadataUuid(String)}. This sync
 * target ID will be used with assets to import or update as references to the meta data item. In case the Smint.io key
 * already exists, just update/overwrite the localized names retrieved by {@link ISmintIoMetadataElement#getValues()}
 * and call {@link ISmintIoMetadataElement#setTargetMetadataUuid(String)}. Implementing classes must not create new sync
 * target ID. It would lead to big mess with the meta data, as the strictly 1:1 relation between these keys would be
 * broken.
 * </p>
 *
 * <p>
 * <strong>IMPORTANT!</strong> a valid ID must be set to {@link ISmintIoMetadataElement#setTargetMetadataUuid(String)}.
 * After an import of meta data, each meta data element will be checked for valid target UUID. If that is missing, the
 * synchronization will immediately abort with an exception. These target UUIDs are vital! So for every import of meta
 * data items, something like the following pseudo-code should be used (see
 * {@link #importContentProviders(ISmintIoMetadataElement[])}):
 * </p>
 *
 * <pre>
 *
 *    void importContentProviders(final ISmintIoMetadataElement[] contentProviders) {
 *
 *        if (contentProviders == null || contentProviders.length == 0) {
 *            return;
 *        }
 *
 *
 *        for (final ISmintIoMetadataElement metadataToImport : contentProviders) {
 *            if (metadataToImport == null) {
 *                continue;
 *            }
 *
 *            final String contentProviderSmintIoID = metadataToImport.getKey();
 *            final Map&lt;Locale, String&gt; localizedNameOfProviderForUIDisplay =  metadataToImport.getValues();
 *
 *            // NOW import the key and its UI translation for each content provider
 *            // store the data somewhere - the storage should return an ID to
 *            // find this value again.
 *
 *
 *            // finished storing this data ..
 *            final String myIdOfThisMetaDataElement = .....
 *            metadataToImport.setTargetMetadataUuid(myIdOfThisMetaDataElement);
 *        }
 *    }
 * </pre>
 *
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
     * In case the tasks returns {@code false} synchronization of generic meta data is aborted immediately. However, if
     * the mapping of meta data Smint.io API IDs and sync target UUIDs is missing, synchronizing meta data is enforced
     * anyway. In this case the return value is simply ignored. This happens on each first run of the synchronization.
     * Later runs make use of already existing mapping, which is cached internally.
     * </p>
     *
     * <p>
     * In case the task crashes with an exception, overall synchronization is terminated without syncing any asset.
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
     * <p>
     * The synchronizations's target IDs must be set to each meta data element with
     * {@link ISmintIoMetadataElement#setTargetMetadataUuid(String)}. Otherwise the sync job will bail out.
     * </p>
     *
     * @param contentProviders the list of content providers used with Smint.io.
     */
    void importContentProviders(ISmintIoMetadataElement[] contentProviders);


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
     * <p>
     * The synchronizations's target IDs must be set to each meta data element with
     * {@link ISmintIoMetadataElement#setTargetMetadataUuid(String)}. Otherwise the sync job will bail out.
     * </p>
     *
     * @param contentTypes the list of content types used with Smint.io.
     */
    void importContentTypes(ISmintIoMetadataElement[] contentTypes);

    void importBinaryTypes(ISmintIoMetadataElement[] binaryTypes);

    void importContentCategories(ISmintIoMetadataElement[] contentCategories);

    void importLicenseTypes(ISmintIoMetadataElement[] licenseTypes);

    void importReleaseStates(ISmintIoMetadataElement[] releaseStates);

    void importLicenseExclusivities(ISmintIoMetadataElement[] licenseExclusivities);

    void importLicenseUsages(ISmintIoMetadataElement[] licenseUsages);

    void importLicenseSizes(ISmintIoMetadataElement[] licenseSizes);

    void importLicensePlacements(ISmintIoMetadataElement[] licensePlacements);

    void importLicenseDistributions(ISmintIoMetadataElement[] licenseDistributions);

    void importLicenseGeographies(ISmintIoMetadataElement[] licenseGeographies);

    void importLicenseIndustries(ISmintIoMetadataElement[] licenseIndustries);

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

    void importLicenseUsageLimits(ISmintIoMetadataElement[] licenseUsageLimits);


    /**
     * After the synchronization of all generic meta data this is called, before any syncing of assets.
     *
     * <p>
     * This hook is only called in case the generic meta data has been synchronized without any exception.
     * </p>
     *
     * <p>
     * Any exception thrown will indicate a faulty ending, immediately aborting the overall synchronization. So make
     * sure all exceptions to be ignored are caught within the task.
     * </p>
     */
    void afterGenericMetadataSync();


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
     * @see BaseSyncAsset
     */
    String getTargetCompoundAssetUuid(String assetUuid);


    /**
     * Maps the Smint.io ID of a <em>Binary Asset</em> to a synchronization target ID/key.
     *
     * @param assetTransactionUuid the ID of the asset on the Smint.io platform.
     * @param binaryUuid           the ID of the binary data on the Smint.io platform.
     * @return the key on the synchronization target, or {@code null} in case the element does not exist yet on the
     *         target.
     * @throws NullPointerException if parameter is {@code null}.
     * @see BaseSyncAsset
     */
    String getTargetAssetBinaryUuid(String assetTransactionUuid, String binaryUuid);


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
    void importNewTargetAssets(BaseSyncAsset[] newTargetAssets);


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
    void updateTargetAssets(BaseSyncAsset[] updatedTargetAssets);


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
    void importNewTargetCompoundAssets(BaseSyncAsset[] newTargetCompoundAssets);


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
    void updateTargetCompoundAssets(BaseSyncAsset[] updatedTargetCompoundAssets);


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
     * This function is the final call after an authentication exception occurred. No recovery is possible. The idea is,
     * to perform some clean-up and notify the user or the system administrator, that an exception has occurred. After
     * that, the synchronizing is terminated immediately.
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
     * This function is the final call after any exception occurred. No recovery is possible. The idea is, to perform
     * some clean-up and notify the user or the system administrator, that an exception has occurred. After that, the
     * synchronizing is terminated immediately.
     * </p>
     *
     * <p>
     * Whenever any of the synchronization functions (like {@link #importNewTargetAssets(BaseSyncAsset[])}) throws an
     * exception, synchronization is terminated immediately. Eventually this function is called. So in case of any
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
     * After all synchronization took place, some clean-up might be necessary. This is executed after all
     * synchronization has ended.
     * </p>
     *
     * <p>
     * Any exception thrown will indicate a faulty ending, immediately aborting the overall synchronization. Since this
     * is the last task to perform at the end of the synchronization, this might be negligible.
     * </p>
     */
    void afterSync();
}

// CHECKSTYLE OFF: MethodCount
