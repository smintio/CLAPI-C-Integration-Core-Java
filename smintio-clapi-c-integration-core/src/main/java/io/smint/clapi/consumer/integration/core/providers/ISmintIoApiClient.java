package io.smint.clapi.consumer.integration.core.providers;

import io.smint.clapi.consumer.integration.core.contracts.ISmintIoGenericMetadata;
import io.smint.clapi.consumer.integration.core.contracts.ISmintIoAsset;


/**
 * Implementing classes utilize the Smint.io CLAPI consumer core library to access Smint.io API and provide sync data.
 *
 * <p>
 * The data to be synchronized by {@link io.smint.clapi.consumer.integration.core.jobs.ISyncJob} need to be fetched from
 * the Smint.io RESTful API. This is done by implementing classes - usually utilizing the available <em>Smint.io Client
 * API Consumer Library</em>.
 * </p>
 */
public interface ISmintIoApiClient {

    /**
     * Retrieves all meta data from the Smint.io API server that is to be synchronized to the target.
     *
     * @return Might be {@code null} in case no data can be retrieved from the server.
     */
    ISmintIoGenericMetadata getGenericMetadata();


    /**
     * Retrieves all assets from the Smint.io API server that are to be synchronized to the target.
     *
     * <p>
     * The data is retrieved in chunks, because the binary assets might become very big. Callers need to provide the
     * continuation UUID, that has been returned by the previous call, to the next call. Hence continuation is
     * "chained".
     * </p>
     *
     * @param continuationUuid     the continuation UUID to send to the server to keep track on already transmitted
     *                             data. Reading all data in chunks is much more responsive and much faster than reading
     *                             all at once.
     * @param includeCoundAssets   set to {@code true} in case compound assets are supported by the synchronization
     *                             target and thus should be included in the list of assets to be synchronized.
     * @param includeBinaryUpdates set to {@code true} in case updating binary assets is supported with the
     *                             synchronization target and thus updates should be included in the list of assets to
     *                             be synced.
     * @return {@code null} in case no data has been retrieved from the server.
     */
    ISmintIoApiDataWithContinuation<ISmintIoAsset[]> getAssets(
        final String continuationUuid, final boolean includeCoundAssets, final boolean includeBinaryUpdates
    );
}
