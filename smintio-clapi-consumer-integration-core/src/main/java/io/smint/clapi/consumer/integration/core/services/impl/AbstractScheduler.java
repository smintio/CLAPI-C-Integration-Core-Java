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

package io.smint.clapi.consumer.integration.core.services.impl;

import java.nio.charset.StandardCharsets;
import java.util.Hashtable;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.logging.Logger;

import javax.inject.Singleton;

import io.smint.clapi.consumer.integration.core.services.IPlatformScheduler;


// CHECKSTYLE.OFF: MultipleStringLiterals

/**
 * Implements an abstract base class for schedulers with some useful utilities.
 *
 * <p>
 * The provided utilities are optional. No implementors of schedulers need to derive from this class. Nevertheless it is
 * beneficial.
 * </p>
 *
 * <p>
 * A map is managed to create a relation between artificial job keys and job informations.
 * </p>
 *
 * @param <T> The type of elements that are stored in a map to maintain relation between key and job.
 */
@Singleton
public abstract class AbstractScheduler<T> implements IPlatformScheduler {

    /**
     * The minimal milliseconds a schedule must use for its re-occurring period.
     *
     * <p>
     * Schedules lower than this value will be ignored. Lower values impose a risk of workload overloading.
     * </p>
     *
     * <pre>
     * {@code MINIMAL_PERIOD_MILLISECONDS} = {@value #MINIMAL_PERIOD_MILLISECONDS}
     * </pre>
     */
    public static final long MINIMAL_PERIOD_MILLISECONDS = 10;


    private static final Logger LOG = Logger.getLogger(AbstractScheduler.class.getName());

    private static final int KEY_BYTE_LEN = 20;
    private static final int MAX_RETRIES_FOR_UNIQUENESS = 1000;

    private final Map<String, T> _scheduledJobs = new Hashtable<>();


    /**
     * Stores the job information and assigns a unique ID/key for the data.
     *
     * <p>
     * A unique is created with {@link #createUniqueRandomKey()} and used to store the job information in a map,
     * maintaining a relation between the job information data and the key.
     * </p>
     *
     * @param jobInfo the information of the job to store.
     * @return A unique key for the job.
     * @throws IllegalStateException in case no unique key can be created after {@link #MAX_RETRIES_FOR_UNIQUENESS}
     *                               tries.
     */
    public String putJob(final T jobInfo) {

        // ignore invalid job
        if (jobInfo == null) {
            return null;
        }

        final String jobKey = this.createUniqueRandomKey();
        this._scheduledJobs.put(jobKey, jobInfo);
        return jobKey;
    }


    /**
     * Retrieves previously stored job information.
     *
     * @param jobKey the key of the job information data as created previously with {@link #putJob(Object)}
     * @return The information of the job as stored previously with {@link #putJob(Object)} or {@code null} if none has
     *         been stored for that key.
     */
    public T getJob(final String jobKey) {
        if (jobKey != null) {
            return this._scheduledJobs.get(jobKey);
        }
        return null;
    }


    /**
     * Retrieves previously stored job information.
     * <p>
     * The bare memory reference of the job information is compared unless the type implements interface
     * {@link java.lang.Comparable}.
     * </p>
     *
     * @param jobInfo the information of the job that has been stored previously.
     * @return The unique key for the previously stored job or {@code null} in case the job has not been stored yet.
     */
    @SuppressWarnings("unchecked")
    public String getKey(final T jobInfo) {

        if (jobInfo == null) {
            return null;
        }

        for (final Entry<String, T> entry : this._scheduledJobs.entrySet()) {

            final T value = entry.getValue();
            if (value != null) {
                if (jobInfo == value
                    || value instanceof Comparable && jobInfo instanceof Comparable
                        && ((Comparable<T>) value).compareTo(jobInfo) == 0) {
                    return entry.getKey();
                }
            }
        }
        return null;
    }


    /**
     * Remove a previously stored job information.
     *
     * @param jobKey the key of the job information data as created previously with {@link #putJob(Object)}
     * @return The information of the job as stored previously with {@link #putJob(Object)} or {@code null} if none has
     *         been stored for that key.
     */
    public T removeJob(final String jobKey) {
        if (jobKey != null) {
            return this._scheduledJobs.remove(jobKey);
        }
        return null;
    }


    /**
     * Get an array of all active job keys.
     *
     * @return An array of all keys of active jobs or {@code String[0]} if no job information is available. No
     *         {@code null} is returned.
     */
    public String[] getAllJobKey() {
        if (this._scheduledJobs.size() == 0) {
            return new String[0];
        }
        return this._scheduledJobs.keySet().toArray(new String[this._scheduledJobs.size()]);
    }


    /**
     * Create a unique random key to be used for a job.
     *
     * <p>
     * Various tries are performed to get hold on a unique key. Random keys are created and checked with the map of
     * internally stored jobs. In case the key is not used, it is unique and returned.<br>
     * If a unique key can not be determined after {@link #MAX_RETRIES_FOR_UNIQUENESS}, then an exception is thrown.
     * </p>
     *
     * @return A unique key for the job.
     * @throws IllegalStateException in case no unique key can be created after {@link #MAX_RETRIES_FOR_UNIQUENESS}
     *                               tries.
     */
    protected String createUniqueRandomKey() {

        String key = this.createRandomKey();
        int max = MAX_RETRIES_FOR_UNIQUENESS;
        while (--max > 0 && this._scheduledJobs.containsKey(key)) {

            LOG.finer("New job key >>" + key + "<< is not unique - trying again (" + max + " attempts left)!");
            key = this.createRandomKey();
        }
        final String jobKey = key;

        if (this._scheduledJobs.containsKey(jobKey)) {
            final IllegalStateException excp = new IllegalStateException(
                "Failed to create a unique key to add the job to the queue!"
            );

            LOG.finer(() -> "New job key >>" + jobKey + "<< is not unique - NOT trying again!");
            LOG.throwing(this.getClass().getName(), "scheduleAtFixedRate", excp);
            throw excp;
        }

        return jobKey;
    }


    /**
     * Create a random key.
     *
     * @return A random key derived from bytes of length {@link #KEY_BYTE_LEN}.
     */
    protected String createRandomKey() {
        final byte[] array = new byte[KEY_BYTE_LEN];
        new Random().nextBytes(array);
        final String key = new String(array, StandardCharsets.UTF_8);

        LOG.finer(() -> "Created new job key >>" + key + "<< - test for uniqueness is pending!");

        return key;
    }
}

// CHECKSTYLE.ON: MultipleStringLiterals
