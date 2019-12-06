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

package io.smint.clapi.consumer.integration.core;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.smint.clapi.consumer.integration.core.services.IPlatformScheduler;
import io.smint.clapi.consumer.integration.core.services.impl.AbstractScheduler;


// CHECKSTYLE.OFF: MultipleStringLiterals
// CHECKSTYLE.OFF: MagicNumber

@DisplayName("Test abstract scheduler: AbstractScheduler")
public class TestAbstractScheduler {


    @Test
    @DisplayName("Adding an empty job info item fails silently.")
    public void putEmptyJobInfo() throws Exception {

        final DummyScheduler scheduler = new DummyScheduler();
        final Map<String, String> jobInfos = scheduler.getJobInfoMap();

        Assertions.assertNotNull(jobInfos, "Failed to get map for job infos!");
        Assertions.assertEquals(0, jobInfos.size(), "Map of job informations starts with an invalid entry!");

        scheduler.putJob(null);
        Assertions.assertEquals(0, jobInfos.size(), "Some job info has been added despite the null value!");
    }


    @Test
    @DisplayName("Adding job info.")
    public void putJobInfo() throws Exception {

        final DummyScheduler scheduler = new DummyScheduler();
        final Map<String, String> jobInfos = scheduler.getJobInfoMap();

        final String key = scheduler.putJob("TEST");
        Assertions.assertNotNull(key, "No job key has been returned!");

        Assertions.assertEquals(1, jobInfos.size(), "Failed to enter the job information to the information map!");
        Assertions.assertTrue(jobInfos.containsKey(key), "Map of job infos does not contain the created key!");
    }


    @Test
    @DisplayName("Adding duplicate job info is accepted.")
    public void putDuplicateJobInfo() throws Exception {

        final DummyScheduler scheduler = new DummyScheduler();
        final Map<String, String> jobInfos = scheduler.getJobInfoMap();

        scheduler.putJob("TEST");
        scheduler.putJob("TEST");
        Assertions.assertEquals(
            2, jobInfos.size(),
            "Failed to enter a duplicate job information to the information map!"
        );
    }


    @Test
    @DisplayName("Retrieving identical reference to job info for job key.")
    public void retrieveJobInfoReference() throws Exception {

        final DummyScheduler scheduler = new DummyScheduler();

        final String testValue = new String("TestValue");
        final String key = scheduler.putJob(testValue);
        final String storedValue = scheduler.getJob(key);

        Assertions.assertTrue(
            testValue == storedValue,
            "Retrieving failed to deliver the identical reference to the job info!"
        );
    }


    @Test
    @DisplayName("Find the job key for the job info with Comparable values.")
    public void getKeyForJobInfo() throws Exception {

        final DummyScheduler scheduler = new DummyScheduler();

        final String testValue = new String("TestValue2");
        final String key = scheduler.putJob(testValue);
        final String keyFound = scheduler.getKey(testValue);

        Assertions.assertEquals(
            key,
            keyFound,
            "Finding a key for a job info failed with Comparable job info!"
        );
    }


    @Test
    @DisplayName("Find the job key for the job info with Comparable values within multiple values.")
    public void getKeyForJobInfoWithinMultiple() throws Exception {

        final DummyScheduler scheduler = new DummyScheduler();


        scheduler.putJob("TestValue-1");
        scheduler.putJob("TestValue-2");
        scheduler.putJob("TestValue-3");
        scheduler.putJob("TestValue-4");

        final String testValue = new String("TestValue-5");
        final String key = scheduler.putJob(testValue);

        scheduler.putJob("TestValue-6");
        scheduler.putJob("TestValue-7");
        scheduler.putJob("TestValue-8");

        final String keyFound = scheduler.getKey(testValue);
        Assertions.assertEquals(
            key,
            keyFound,
            "Finding a key for a job info failed with Comparable job info!"
        );
    }


    @Test
    @DisplayName("Find the job key for the job info by reference.")
    public void getKeyForJobInfoByReference() throws Exception {

        final DummyObjectScheduler scheduler = new DummyObjectScheduler();

        final Object testValue = new Object();
        final String key = scheduler.putJob(testValue);
        final String keyFound = scheduler.getKey(testValue);

        Assertions.assertEquals(
            key,
            keyFound,
            "Finding a key for a job info failed comparing by reference!"
        );
    }


    @Test
    @DisplayName("Find the job key for the job info by reference within multiple jobs.")
    public void getKeyForJobInfoByReferenceInMultiple() throws Exception {

        final DummyObjectScheduler scheduler = new DummyObjectScheduler();

        final Object testValue = new Object();

        scheduler.putJob(new Object());
        scheduler.putJob(new Object());
        scheduler.putJob(new Object());
        final String key = scheduler.putJob(testValue);
        scheduler.putJob(new Object());
        scheduler.putJob(new Object());

        final String keyFound = scheduler.getKey(testValue);
        Assertions.assertEquals(
            key,
            keyFound,
            "Finding a key for a job info failed comparing by reference!"
        );
    }


    @Test
    @DisplayName("Removing a job info from multiple values.")
    public void removingJobInfo() throws Exception {

        final DummyScheduler scheduler = new DummyScheduler();
        final Map<String, String> jobInfos = scheduler.getJobInfoMap();

        scheduler.putJob("TEST1");
        scheduler.putJob("TEST2");
        scheduler.putJob("TEST3");
        final String key = scheduler.putJob("TEST4");
        scheduler.putJob("TEST5");
        Assertions.assertEquals(5, jobInfos.size(), "Failed to enter multiple job information!");

        scheduler.removeJob(key);
        Assertions.assertEquals(4, jobInfos.size(), "Failed to remove a specific job information!");
    }


    @Test
    @DisplayName("Retrieving all available job info keys.")
    public void getAllJobInfoKeys() throws Exception {

        final DummyScheduler scheduler = new DummyScheduler();

        scheduler.putJob("TEST1");
        scheduler.putJob("TEST2");
        scheduler.putJob("TEST3");
        scheduler.putJob("TEST4");
        scheduler.putJob("TEST5");

        final String[] allKeys = scheduler.getAllJobKey();
        Assertions.assertNotNull(allKeys, "Failed to retrieve any job key!");
        Assertions.assertEquals(5, allKeys.length, "Failed to retrieve all job keys!");
    }


    @Test
    @DisplayName("An unique random key is created successfully.")
    public void createUniqueJobKey() throws Exception {

        final DummyScheduler scheduler = new DummyScheduler();
        final String jobKey = scheduler.createUniqueRandomKey();

        Assertions.assertNotNull(jobKey, "Failed to create a valid job key!");
        Assertions.assertFalse(jobKey.isEmpty(), "Create job key is empty!");
    }


    @Test
    @DisplayName("Job keys are really unique.")
    public void createRandomJobKey() throws Exception {

        final DummyScheduler scheduler = new DummyScheduler();
        final Map<String, Boolean> existingKeys = new HashMap<>();

        for (int i = 0; i < 1000; i++) {
            final String jobKey = scheduler.createRandomKey();

            Assertions.assertFalse(jobKey == null || jobKey.isEmpty(), "Job key is invalid: " + jobKey + "!");
            Assertions.assertFalse(existingKeys.containsKey(jobKey), "Job key is not unique: " + jobKey + "!");

            existingKeys.put(jobKey, Boolean.TRUE);
        }
    }


    private static class DummyScheduler extends AbstractScheduler<String> {

        @Override
        public String scheduleAtFixedRate(final Runnable job, final long period) {
            return null;
        }

        @Override
        public IPlatformScheduler stopSchedule(final String jobKey) {
            return null;
        }

        @Override
        public IPlatformScheduler cancel() {
            return null;
        }

        @Override
        public IPlatformScheduler scheduleForImmediateExecution(final Runnable job) {
            return null;
        }

        @SuppressWarnings("unchecked")
        public Map<String, String> getJobInfoMap() throws Exception {
            final Field getScheduledJobs = AbstractScheduler.class.getDeclaredField("_scheduledJobs");
            getScheduledJobs.setAccessible(true);
            return (Map<String, String>) getScheduledJobs.get(this);
        }

        @Override
        public String createUniqueRandomKey() {
            return super.createUniqueRandomKey();
        }

        @Override
        public String createRandomKey() {
            return super.createRandomKey();
        }

    }


    private static class DummyObjectScheduler extends AbstractScheduler<Object> {

        @Override
        public String scheduleAtFixedRate(final Runnable job, final long period) {
            return null;
        }

        @Override
        public IPlatformScheduler stopSchedule(final String jobKey) {
            return null;
        }

        @Override
        public IPlatformScheduler cancel() {
            return null;
        }

        @Override
        public IPlatformScheduler scheduleForImmediateExecution(final Runnable job) {
            return null;
        }
    }
}

// CHECKSTYLE.ON: MultipleStringLiterals
// CHECKSTYLE.ON: MagicNumber
