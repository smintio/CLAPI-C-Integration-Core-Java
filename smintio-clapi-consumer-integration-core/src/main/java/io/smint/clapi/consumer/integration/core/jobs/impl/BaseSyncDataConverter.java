package io.smint.clapi.consumer.integration.core.jobs.impl;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import io.smint.clapi.consumer.integration.core.contracts.ISmintIoDataType;
import io.smint.clapi.consumer.integration.core.target.ISyncDataType;
import io.smint.clapi.consumer.integration.core.target.ISyncTarget;


/**
 * Abstract base for converters that convert instances of {@code <FromTypeT>} to {@code <ToTypeT>}.
 *
 * <p>
 * Conversion involve the replacement of Smint.io specific ID with synchronization target keys. Hence instances probably
 * make use of a reference to an instance of {@link ISyncTarget} to do translation of the IDs.
 * </p>
 *
 * <p>
 * New instances to should be created utilizing {@link ISyncTarget}'s factory functions.
 * </p>
 *
 * @param <FromTypeT> the type from package {@link io.smint.clapi.consumer.integration.core.contracts} to convert from,
 *                    copying over all data and mapping Smtint.io keys to sync target keys.
 * @param <ToTypeT>   the new type from package {@link io.smint.clapi.consumer.integration.core.target} that will hold
 *                    all data prepared for the sync target.
 */
abstract class BaseSyncDataConverter<FromTypeT extends ISmintIoDataType, ToTypeT extends ISyncDataType> {


    private final Class<ToTypeT> _targetType;

    /**
     * Initializes a new converter, using the {@code syncTarget} to map to sync target keys.
     *
     * @param targetType the sync target type that is used to convert to. Must not be {@code null}!
     * @throws NullPointerException if {@code syncTarget} is {@code null}
     */
    protected BaseSyncDataConverter(final Class<ToTypeT> targetType) {
        this._targetType = targetType;

        Objects.requireNonNull(targetType, "Provided target type is invalid <null>");
    }


    /**
     * Converts a single raw instance of {@code FromType} to a new instance of {@code ToType}.
     *
     * <p>
     * Usually a one-to-one conversion is performed, so the result should be an array with just one element. But
     * sometimes, a direct mapping is impossible (eg: converting assets). Then more than a single element will be
     * returned in the resulting array.
     * </p>
     *
     * @param source the source instance to convert.
     * @return a list if new instance with converted data or {@code null} if {@code source} is {@code null}.
     */
    public abstract ToTypeT[] convert(final FromTypeT source);


    /**
     * Converts each item in the list, using {@link #convert(ISmintIoDataType)}.
     *
     * @param sources the list of original sources to convert.
     * @return a converted list or {@code null} if {@code sources} is {@code null}
     */
    @SuppressWarnings("unchecked")
    public ToTypeT[] convertAll(final FromTypeT[] sources) {
        if (sources == null) {
            return null;
        }

        if (sources.length == 0) {
            return (ToTypeT[]) Array.newInstance(this._targetType, 0);
        }

        final List<ToTypeT> convertedItems = new ArrayList<>();
        for (final FromTypeT source : sources) {

            final ToTypeT[] converted = this.convert(source);
            if (converted != null) {
                convertedItems.addAll(Arrays.asList(converted));
            }
        }

        return convertedItems.toArray((ToTypeT[]) Array.newInstance(this._targetType, convertedItems.size()));
    }


    /**
     * Maps a Smint.io list of keys to a list of keys on the sync target.
     *
     * <p>
     * The mapping is performed by a function, that will receive a single Smint.io key and map it to a synchronization
     * target key. The mapping function should return {@code null} for any parameter of value {@code null}. However, it
     * must raise an exception, in case the Smint.io key can not be found on the sync target, rather than returning
     * {@code null} in this case, too. The exception will abort the current sync process and requires further manual
     * checks and SysAdmin assistance. Since all possible meta data is synchronized before any assets, a missing license
     * meta data is a severe condition and hence must be checked manually!
     * </p>
     *
     * @param smintIoKeys               the Smint.io keys to map.
     * @param getTargetKeyForSmintIoKey the mapping function for the key, does the actual workload.
     * @return a list of mapped keys of the same size as the parameter.
     */
    protected String[] convertKeys(
        final String[] smintIoKeys, final Function<String, String> getTargetKeyForSmintIoKey
    ) {

        if (smintIoKeys == null || smintIoKeys.length == 0) {
            return smintIoKeys;
        }


        final String[] targetKeys = new String[smintIoKeys.length];
        for (int i = 0; i < smintIoKeys.length; i++) {
            final String smintIoKey = smintIoKeys[i];
            targetKeys[i] = getTargetKeyForSmintIoKey.apply(smintIoKey);

            if (smintIoKey != null && !smintIoKey.isEmpty()) {
                Objects
                    .requireNonNull(targetKeys[i], "Failed to get sync target key for Smint.io API key " + smintIoKey);
            }
        }

        return targetKeys;
    }
}
