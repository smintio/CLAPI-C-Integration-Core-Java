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

package io.smint.clapi.consumer.integration.core.providers.impl;

import java.lang.reflect.Method;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.smint.clapi.consumer.generated.models.LocalizedMetadataElement;
import io.smint.clapi.consumer.integration.core.configuration.impl.AuthTokenMemoryStorage;
import io.smint.clapi.consumer.integration.core.configuration.models.ISettingsModel;
import io.smint.clapi.consumer.integration.core.contracts.ISmintIoMetadataElement;


// CHECKSTYLE.OFF: MultipleStringLiterals
// CHECKSTYLE.OFF: MagicNumber

@DisplayName("Test Smint.io API provider: SmintIoApiClientImpl")
public class TestSminIoApiClient {


    @Test
    @DisplayName("converting API data to grouped meta data elements.")
    public void testGroupedMetadataElementsForImportLanguages() throws Exception {

        final String[] importLanguages = new String[] { "en", "de" };
        final Gson gson = new GsonBuilder().setPrettyPrinting().create();
        final LocalizedMetadataElement[] metaData = gson.fromJson(
            "[\n" +
                "        {\n" +
                "            \"culture\": \"en\",\n" +
                "            \"metadata_element\": {\n" +
                "                \"key\": \"getty\",\n" +
                "                \"name\": \"Getty Images EN\"\n" +
                "            }\n" +
                "        },\n" +
                "        {\n" +
                "            \"culture\": \"de\",\n" +
                "            \"metadata_element\": {\n" +
                "                \"key\": \"getty\",\n" +
                "                \"name\": \"Getty Images DE\"\n" +
                "            }\n" +
                "        },\n" +
                "        {\n" +
                "            \"culture\": \"en\",\n" +
                "            \"metadata_element\": {\n" +
                "                \"key\": \"istock\",\n" +
                "                \"name\": \"iStock\"\n" +
                "            }\n" +
                "        },\n" +
                "        {\n" +
                "            \"culture\": \"de\",\n" +
                "            \"metadata_element\": {\n" +
                "                \"key\": \"istock\",\n" +
                "                \"name\": \"iStock\"\n" +
                "            }\n" +
                "        },\n" +
                "        {\n" +
                "            \"culture\": \"en\",\n" +
                "            \"metadata_element\": {\n" +
                "                \"key\": \"shutterstock\",\n" +
                "                \"name\": \"Shutterstock\"\n" +
                "            }\n" +
                "        },\n" +
                "        {\n" +
                "            \"culture\": \"de\",\n" +
                "            \"metadata_element\": {\n" +
                "                \"key\": \"shutterstock\",\n" +
                "                \"name\": \"Shutterstock\"\n" +
                "            }\n" +
                "        },\n" +
                "        {\n" +
                "            \"culture\": \"en\",\n" +
                "            \"metadata_element\": {\n" +
                "                \"key\": \"adobestock\",\n" +
                "                \"name\": \"Adobe Stock\"\n" +
                "            }\n" +
                "        },\n" +
                "        {\n" +
                "            \"culture\": \"de\",\n" +
                "            \"metadata_element\": {\n" +
                "                \"key\": \"adobestock\",\n" +
                "                \"name\": \"Adobe Stock\"\n" +
                "            }\n" +
                "        },\n" +
                "        {\n" +
                "            \"culture\": \"en\",\n" +
                "            \"metadata_element\": {\n" +
                "                \"key\": \"panthermedia\",\n" +
                "                \"name\": \"PantherMedia\"\n" +
                "            }\n" +
                "        },\n" +
                "        {\n" +
                "            \"culture\": \"de\",\n" +
                "            \"metadata_element\": {\n" +
                "                \"key\": \"panthermedia\",\n" +
                "                \"name\": \"PantherMedia\"\n" +
                "            }\n" +
                "        }\n" +
                "    ]",
            LocalizedMetadataElement[].class
        );
        Assertions.assertNotNull(metaData, "Failed read test data from JSON!");


        final String expectedResult = "[\n" +
            "  {\n" +
            "    \"_key\": \"de\",\n" +
            "    \"_values\": {\n" +
            "      \"getty\": \"Getty Images DE\",\n" +
            "      \"shutterstock\": \"Shutterstock\",\n" +
            "      \"istock\": \"iStock\",\n" +
            "      \"panthermedia\": \"PantherMedia\",\n" +
            "      \"adobestock\": \"Adobe Stock\"\n" +
            "    }\n" +
            "  },\n" +
            "  {\n" +
            "    \"_key\": \"en\",\n" +
            "    \"_values\": {\n" +
            "      \"getty\": \"Getty Images EN\",\n" +
            "      \"shutterstock\": \"Shutterstock\",\n" +
            "      \"istock\": \"iStock\",\n" +
            "      \"panthermedia\": \"PantherMedia\",\n" +
            "      \"adobestock\": \"Adobe Stock\"\n" +
            "    }\n" +
            "  }\n" +
            "]";

        final ISmintIoMetadataElement[] groupedElements = this
            .getGroupedMetadataElementsForImportLanguages(importLanguages, Arrays.asList(metaData));

        Assertions.assertNotNull(groupedElements, "Failed to convert API meta data to internal sync data!");
        Assertions.assertEquals(
            importLanguages.length,
            groupedElements.length,
            "Failed to acquire a value for each import language!"
        );

        Assertions.assertEquals(
            expectedResult,
            gson.toJson(groupedElements),
            "conversion lead to unexpected result!"
        );

    }


    @Test
    @DisplayName("converting API data to grouped meta data elements for single language.")
    public void testGroupedMetadataElementsForSingleLanguage() throws Exception {

        final String[] importLanguages = new String[] { "de" };
        final Gson gson = new GsonBuilder().setPrettyPrinting().create();
        final LocalizedMetadataElement[] metaData = gson.fromJson(
            "[\n" +
                "        {\n" +
                "            \"culture\": \"en\",\n" +
                "            \"metadata_element\": {\n" +
                "                \"key\": \"getty\",\n" +
                "                \"name\": \"Getty Images EN\"\n" +
                "            }\n" +
                "        },\n" +
                "        {\n" +
                "            \"culture\": \"de\",\n" +
                "            \"metadata_element\": {\n" +
                "                \"key\": \"getty\",\n" +
                "                \"name\": \"Getty Images DE\"\n" +
                "            }\n" +
                "        },\n" +
                "        {\n" +
                "            \"culture\": \"en\",\n" +
                "            \"metadata_element\": {\n" +
                "                \"key\": \"istock\",\n" +
                "                \"name\": \"iStock\"\n" +
                "            }\n" +
                "        },\n" +
                "        {\n" +
                "            \"culture\": \"de\",\n" +
                "            \"metadata_element\": {\n" +
                "                \"key\": \"istock\",\n" +
                "                \"name\": \"iStock\"\n" +
                "            }\n" +
                "        },\n" +
                "        {\n" +
                "            \"culture\": \"en\",\n" +
                "            \"metadata_element\": {\n" +
                "                \"key\": \"shutterstock\",\n" +
                "                \"name\": \"Shutterstock\"\n" +
                "            }\n" +
                "        },\n" +
                "        {\n" +
                "            \"culture\": \"de\",\n" +
                "            \"metadata_element\": {\n" +
                "                \"key\": \"shutterstock\",\n" +
                "                \"name\": \"Shutterstock\"\n" +
                "            }\n" +
                "        },\n" +
                "        {\n" +
                "            \"culture\": \"en\",\n" +
                "            \"metadata_element\": {\n" +
                "                \"key\": \"adobestock\",\n" +
                "                \"name\": \"Adobe Stock\"\n" +
                "            }\n" +
                "        },\n" +
                "        {\n" +
                "            \"culture\": \"de\",\n" +
                "            \"metadata_element\": {\n" +
                "                \"key\": \"adobestock\",\n" +
                "                \"name\": \"Adobe Stock\"\n" +
                "            }\n" +
                "        },\n" +
                "        {\n" +
                "            \"culture\": \"en\",\n" +
                "            \"metadata_element\": {\n" +
                "                \"key\": \"panthermedia\",\n" +
                "                \"name\": \"PantherMedia\"\n" +
                "            }\n" +
                "        },\n" +
                "        {\n" +
                "            \"culture\": \"de\",\n" +
                "            \"metadata_element\": {\n" +
                "                \"key\": \"panthermedia\",\n" +
                "                \"name\": \"PantherMedia\"\n" +
                "            }\n" +
                "        }\n" +
                "    ]",
            LocalizedMetadataElement[].class
        );
        Assertions.assertNotNull(metaData, "Failed read test data from JSON!");


        final String expectedResult = "[\n" +
            "  {\n" +
            "    \"_key\": \"de\",\n" +
            "    \"_values\": {\n" +
            "      \"getty\": \"Getty Images DE\",\n" +
            "      \"shutterstock\": \"Shutterstock\",\n" +
            "      \"istock\": \"iStock\",\n" +
            "      \"panthermedia\": \"PantherMedia\",\n" +
            "      \"adobestock\": \"Adobe Stock\"\n" +
            "    }\n" +
            "  }\n" +
            "]";

        final ISmintIoMetadataElement[] groupedElements = this
            .getGroupedMetadataElementsForImportLanguages(importLanguages, Arrays.asList(metaData));

        Assertions.assertNotNull(groupedElements, "Failed to convert API meta data to internal sync data!");
        Assertions.assertEquals(
            importLanguages.length,
            groupedElements.length,
            "Failed to acquire a value for each import language!"
        );

        Assertions.assertEquals(
            expectedResult,
            gson.toJson(groupedElements),
            "conversion lead to unexpected result!"
        );

    }


    public ISmintIoMetadataElement[] getGroupedMetadataElementsForImportLanguages(
        final String[] importLanguages, final List<LocalizedMetadataElement> elements
    ) throws Exception {

        final SmintIoApiClientImpl apiClient = this.createApiClient(importLanguages);
        final Method getGrouped = apiClient.getClass().getDeclaredMethod(
            "getGroupedMetadataElementsForImportLanguages",
            String[].class, List.class
        );
        getGrouped.setAccessible(true);
        return (ISmintIoMetadataElement[]) getGrouped.invoke(apiClient, importLanguages, elements);
    }


    private SmintIoApiClientImpl createApiClient(final String[] importLanguages) {
        return new SmintIoApiClientImpl(
            new ISettingsModel() {

                @Override
                public String getTenantId() {
                    return "test";
                }

                @Override
                public int getChannelId() {
                    return 0;
                }

                @Override
                public String getOAuthClientId() {
                    return "test";
                }

                @Override
                public String getOAuthClientSecret() {
                    return "test";
                }

                @Override
                public URL getOAuthLocalUrlReceivingAccessData() {
                    return null;
                }

                @Override
                public String[] getImportLanguages() {
                    return importLanguages;
                }

            },
            new AuthTokenMemoryStorage(),
            (settings, authTokenStorage) -> null,
            null,
            null,
            null
        );
    }
}

// CHECKSTYLE.ON: MultipleStringLiterals
// CHECKSTYLE.ON: MagicNumber
