package io.smint.clapi.consumer.integration.core.providers.impl;

import io.smint.clapi.consumer.integration.core.providers.ISmintIoApiDataWithContinuation;


/**
 * Combines Smint.io API query results with a continuation UUID, which is used to fetch next chunk of results.
 *
 * @param <T> defines the type this container will hold as result.
 */
public class SmintIoApiDataWithContinuationImpl<T> implements ISmintIoApiDataWithContinuation<T> {

    private T _result = null;
    private boolean _hasAssets = false;
    private String _continuationUuid = null;

    @Override
    public T getResult() {
        return this._result;
    }


    /**
     * Sets a new value to the stored result.
     *
     * @param newResult the new value to set.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    public SmintIoApiDataWithContinuationImpl<T> setResult(final T newResult) {
        this._result = newResult;
        return this;
    }


    @Override
    public String getContinuationUuid() {
        return this._continuationUuid;
    }


    /**
     * Sets a new value to the stored continuation UUID.
     *
     * @param newUuid the new value to set.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    public SmintIoApiDataWithContinuationImpl<T> setContinuationUuid(final String newUuid) {
        this._continuationUuid = newUuid;
        return this;
    }


    @Override
    public boolean hasAssets() {
        return this._hasAssets;
    }


    /**
     * Sets a new flag to indicate the sync job must continue with next batch/chunk.
     *
     * @param newUuid the new value to set.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    public SmintIoApiDataWithContinuationImpl<T> setHasAssets(final boolean hasAssets) {
        this._hasAssets = hasAssets;
        return this;
    }
}
