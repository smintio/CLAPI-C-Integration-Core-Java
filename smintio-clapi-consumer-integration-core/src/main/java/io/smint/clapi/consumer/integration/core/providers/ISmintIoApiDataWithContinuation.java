package io.smint.clapi.consumer.integration.core.providers;

/**
 * Combines Smint.io API query results with a continuation UUID, which is used to fetch next chunk of results.
 *
 * <p>
 * Querying a RESTful API can be a time consuming task. If a lot of data is to be transmitted, collecting the data on
 * the server side takes quite some time but also the transmission can be very slow. In order to speed up
 * synchronization, the data fetched from the Smin.io platform is parted into chunks. While waiting for the next chunk,
 * the previous chunk an be processed and synchronized to the target system. However, in order to avoid missing some
 * data, the Smint.io API server keeps track of all data in the previous chunks and sends only new data with the next
 * chunk. Since RESTful API is state agnostic, a continuation UUID is provided to the API server - much like a session
 * ID for users. This continuation ID is linked and related to all the data in the previous chunks. This makes it easy
 * to avoid duplicate transmission.
 * </p>
 *
 * @param <T> defines the type this container will hold as result.
 */
public interface ISmintIoApiDataWithContinuation<T> {

    /**
     * Provides the result that was fetched from the Smint.io API server.
     *
     * @return a result of type {@code T} or {@code null}.
     */
    T getResult();


    /**
     * Provides the next continuation UUID to be sent to the Smint.io API server on next fetch.
     *
     * @return a continuation UUID as sent by the server or {@code null} if nothing more is to be fetched.
     */
    String getContinuationUuid();


    /**
     * Indicates that the current chunk/batch covers assets, although none are subject synchronization.
     *
     * <p>
     * There are assets that exist at the Smint.io platform but that must not be synchronized to any local asset
     * management system. Maybe the purchase is not yet finished or whatever. In case a batch/chunk loaded from the
     * Smint.io server covers only such assets, there will be no assets to sync. However, that must not be an abort
     * criterion as more may be available. Therefore this flag will indicate that the current list of assets is covering
     * some real assets and more are to be covered by next chunk/batch.
     * </p>
     *
     * @return {@code true} in case this chunk/batch covers some assets and the sync job must continue with next
     *         batch/chunk.
     */
    boolean hasAssets();
}
