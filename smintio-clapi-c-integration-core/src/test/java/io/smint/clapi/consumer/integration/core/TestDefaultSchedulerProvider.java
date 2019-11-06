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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.smint.clapi.consumer.integration.core.configuration.models.ISettingsModel;
import io.smint.clapi.consumer.integration.core.services.IPlatformScheduler;
import io.smint.clapi.consumer.integration.core.services.impl.DefaultPlatformSchedulerProvider;


@DisplayName("Test default Java sheduler provider: DefaultPlatformSchedulerProvider.class")
public class TestDefaultSchedulerProvider {


    @Test
    @DisplayName("A scheduler is provided.")
    public void getScheduler() {
        final DefaultPlatformSchedulerProvider provider = new DefaultPlatformSchedulerProvider();
        Assertions.assertNotNull(provider.get(), "Default platform scheduler provider failed to provide an instance!");
    }


    @Test
    @DisplayName("The same scheduler is provided for every call.")
    public void getSameScheduler() {
        final DefaultPlatformSchedulerProvider provider = new DefaultPlatformSchedulerProvider();

        final IPlatformScheduler scheduler = provider.get();

        Assertions.assertTrue(
            scheduler == provider.get(),
            "Default platform scheduler provider a new instance instead of a singleton!"
        );

        Assertions.assertTrue(
            scheduler == provider.get(),
            "Default platform scheduler provider a new instance instead of a singleton (2nd try)!"
        );

        Assertions.assertTrue(
            scheduler == provider.get(),
            "Default platform scheduler provider a new instance instead of a singleton (3rd try)!"
        );

        Assertions.assertTrue(
            scheduler == provider.get(),
            "Default platform scheduler provider a new instance instead of a singleton (4th try)!"
        );
    }


    @Test
    @DisplayName("Settings are stored.")
    public void setSettings() {
        final DefaultPlatformSchedulerProvider provider = new DefaultPlatformSchedulerProvider();

        final ISettingsModel settings = new ISettingsModel() {

            @Override
            public String getTenantId() {
                return null;
            }

            @Override
            public int getChannelId() {
                return 0;
            }

            @Override
            public String getClientId() {
                return null;
            }

            @Override
            public String getClientSecret() {
                return null;
            }

            @Override
            public String[] getImportLanguages() {
                return null;
            }
        };

        provider.setSettings(settings);
        Assertions.assertEquals(
            settings,
            provider.getSettings(),
            "Default platform scheduler did not provide the settings instance that was stored!"
        );
    }

}
