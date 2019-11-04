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

import java.util.List;

import io.smint.clapi.consumer.integration.core.contracts.ISmintIoAsset;
import io.smint.clapi.consumer.integration.core.contracts.ISmintIoMetadataElement;
import io.smint.clapi.consumer.integration.core.exceptions.SmintIoAuthenticatorException;
import io.smint.clapi.consumer.integration.core.exceptions.SmintIoSyncJobException;


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
 * to a target DAM - in a one-way process, from Smint.io to DAM (Smint.io --&gt;/gt; DAM).
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
 * {@link io.smint.clapi.consumer.integration.core.target.ISmintIoSynchronization}. The scheduled synchronization will
 * sync meta data, whereas the pushed kind of sync in case of a purchase will not.
 * </p>
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


    // TODO: add documentation to these functions along example data to by synced

    void importContentProviders(ISmintIoMetadataElement[] contentProviders);

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

    void importAssets(final String folderName, final List<ISmintIoAsset> assets);

    /**
     * After the synchronization of all assets with {@link #importAssets(String, List)} this is called, but before
     * {@link #afterAssetsSync()}.
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
     * Whenever any of the synchronization functions (like {@link #importAssets(String, List)}) throws an exception,
     * synchronization is terminated immediately. Eventually this function is called but not other clean-up utility
     * function, like {@link #clearGenericMetadataCaches()} or any {@code after...} function. So in case of any
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
