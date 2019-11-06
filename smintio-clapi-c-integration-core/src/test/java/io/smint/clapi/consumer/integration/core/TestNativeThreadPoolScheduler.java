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

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.smint.clapi.consumer.integration.core.impl.NativeThreadPoolScheduler;


// CHECKSTYLE.OFF: MultipleStringLiterals
// CHECKSTYLE.OFF: MagicNumber

@DisplayName("Test Java native sheduler: NativeThreadPoolScheduler.class")
public class TestNativeThreadPoolScheduler {


    @Test
    @DisplayName("Stopping scheduler without jobs does not fail.")
    public void stopEmptyScheduler() {
        final NativeThreadPoolScheduler scheduler = new NativeThreadPoolScheduler();
        scheduler.stopSchedule(null);
    }


    @Test
    @DisplayName("Scheduling 'null' job does not fail.")
    public void scheduleEmptyJob() {
        final NativeThreadPoolScheduler scheduler = new NativeThreadPoolScheduler();
        scheduler.scheduleAtFixedRate(null, 1000);
    }


    @Test
    @DisplayName("New jobs are not executed immediately.")
    public void scheduleJobWithoutImmediateExecution() throws InterruptedException {

        final NativeThreadPoolScheduler scheduler = new NativeThreadPoolScheduler();
        final int period = 100;

        final int[] wasCalled = new int[] { 0 };
        scheduler.scheduleAtFixedRate(() -> wasCalled[0]++, period);

        TimeUnit.MILLISECONDS.sleep(period - 10);
        Assertions.assertEquals(0, wasCalled[0], "Scheduled job was executed immediately!");

        scheduler.cancel();
    }


    @Test
    @DisplayName("Jobs are executed after first period.")
    public void scheduleJob() throws InterruptedException {

        final NativeThreadPoolScheduler scheduler = new NativeThreadPoolScheduler();
        final int period = 100;

        final int[] wasCalled = new int[] { 0 };
        scheduler.scheduleAtFixedRate(() -> wasCalled[0]++, period);

        TimeUnit.MILLISECONDS.sleep(period + 10);
        Assertions.assertEquals(1, wasCalled[0], "Scheduled job was not executed after first period!");

        scheduler.cancel();
    }


    @Test
    @DisplayName("Jobs are executed multiple times after fixed period.")
    public void scheduleJobCheckAfterFixedPeriod() throws InterruptedException {

        final NativeThreadPoolScheduler scheduler = new NativeThreadPoolScheduler();
        final int period = 100;

        final int[] wasCalled = new int[] { 0 };
        scheduler.scheduleAtFixedRate(() -> wasCalled[0]++, period);

        TimeUnit.MILLISECONDS.sleep(5);

        for (int i = 1; i <= 10; i++) {
            TimeUnit.MILLISECONDS.sleep(period);
            Assertions.assertEquals(i, wasCalled[0], "Scheduled job was not executed after period nr " + i + "!");
        }
        scheduler.cancel();
    }


    @Test
    @DisplayName("Jobs are executed multiple times but not after stopping.")
    public void scheduleJobCheckAfterFixedPeriodButNotAfterCancel() throws InterruptedException {

        final NativeThreadPoolScheduler scheduler = new NativeThreadPoolScheduler();
        final int period = 100;

        final int[] wasCalled = new int[] { 0 };
        final String jobKey = scheduler.scheduleAtFixedRate(() -> wasCalled[0]++, period);

        TimeUnit.MILLISECONDS.sleep(5);

        for (int i = 1; i <= 5; i++) {
            TimeUnit.MILLISECONDS.sleep(period);
            Assertions.assertEquals(i, wasCalled[0], "Scheduled job was not executed after period nr " + i + "!");
        }

        final int currentCall = wasCalled[0];

        scheduler.stopSchedule(jobKey);
        TimeUnit.MILLISECONDS.sleep(period);
        Assertions
            .assertEquals(currentCall, wasCalled[0], "Scheduled job was executed although it has been cancelled!");
    }
}

// CHECKSTYLE.ON: MultipleStringLiterals
// CHECKSTYLE.ON: MagicNumber
