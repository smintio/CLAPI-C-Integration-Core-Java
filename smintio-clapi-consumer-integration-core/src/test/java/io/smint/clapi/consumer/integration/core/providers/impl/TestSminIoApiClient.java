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
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.smint.clapi.consumer.generated.models.LocalizedMetadataElement;
import io.smint.clapi.consumer.integration.core.LocaleUtility;
import io.smint.clapi.consumer.integration.core.contracts.ISmintIoMetadataElement;


// CHECKSTYLE.OFF: MultipleStringLiterals
// CHECKSTYLE.OFF: MagicNumber

@DisplayName("Test SmintIoApiClientImpl: getGroupedMetadataElementsForImportLanguages")
public class TestSminIoApiClient extends TestSminIoApiClientBase {


    @Test
    @DisplayName("converting to grouped meta data elements.")
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
            "    \"_key\": \"adobestock\",\n" +
            "    \"_values\": {\n" +
            "      \"de\": \"Adobe Stock\",\n" +
            "      \"en\": \"Adobe Stock\"\n" +
            "    }\n" +
            "  },\n" +
            "  {\n" +
            "    \"_key\": \"getty\",\n" +
            "    \"_values\": {\n" +
            "      \"de\": \"Getty Images DE\",\n" +
            "      \"en\": \"Getty Images EN\"\n" +
            "    }\n" +
            "  },\n" +
            "  {\n" +
            "    \"_key\": \"istock\",\n" +
            "    \"_values\": {\n" +
            "      \"de\": \"iStock\",\n" +
            "      \"en\": \"iStock\"\n" +
            "    }\n" +
            "  },\n" +
            "  {\n" +
            "    \"_key\": \"panthermedia\",\n" +
            "    \"_values\": {\n" +
            "      \"de\": \"PantherMedia\",\n" +
            "      \"en\": \"PantherMedia\"\n" +
            "    }\n" +
            "  },\n" +
            "  {\n" +
            "    \"_key\": \"shutterstock\",\n" +
            "    \"_values\": {\n" +
            "      \"de\": \"Shutterstock\",\n" +
            "      \"en\": \"Shutterstock\"\n" +
            "    }\n" +
            "  }\n" +
            "]";

        final ISmintIoMetadataElement[] groupedElements = this
            .getGroupedMetadataElementsForImportLanguages(importLanguages, Arrays.asList(metaData));

        Assertions.assertNotNull(groupedElements, "Failed to convert API meta data to internal sync data!");
        Assertions.assertEquals(
            importLanguages.length,
            groupedElements[0].getValues().size(),
            "Failed to acquire a value for each import language!"
        );

        Arrays.sort(groupedElements, (a, b) -> a.getKey().compareTo(b.getKey()));
        Assertions.assertEquals(
            expectedResult,
            gson.toJson(groupedElements),
            "conversion lead to unexpected result!"
        );

    }


    @Test
    @DisplayName("converting to grouped meta data elements without fallback but missing language.")
    public void testGroupedMetadataElements_WithMissingLanguageWithoutFallback() throws Exception {

        final String[] importLanguages = new String[] { "en", "de", "zh" };
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
            "    \"_key\": \"adobestock\",\n" +
            "    \"_values\": {\n" +
            "      \"de\": \"Adobe Stock\",\n" +
            "      \"en\": \"Adobe Stock\"\n" +
            "    }\n" +
            "  },\n" +
            "  {\n" +
            "    \"_key\": \"getty\",\n" +
            "    \"_values\": {\n" +
            "      \"de\": \"Getty Images DE\",\n" +
            "      \"en\": \"Getty Images EN\"\n" +
            "    }\n" +
            "  },\n" +
            "  {\n" +
            "    \"_key\": \"istock\",\n" +
            "    \"_values\": {\n" +
            "      \"de\": \"iStock\",\n" +
            "      \"en\": \"iStock\"\n" +
            "    }\n" +
            "  },\n" +
            "  {\n" +
            "    \"_key\": \"panthermedia\",\n" +
            "    \"_values\": {\n" +
            "      \"de\": \"PantherMedia\",\n" +
            "      \"en\": \"PantherMedia\"\n" +
            "    }\n" +
            "  },\n" +
            "  {\n" +
            "    \"_key\": \"shutterstock\",\n" +
            "    \"_values\": {\n" +
            "      \"de\": \"Shutterstock\",\n" +
            "      \"en\": \"Shutterstock\"\n" +
            "    }\n" +
            "  }\n" +
            "]";

        final ISmintIoMetadataElement[] groupedElements = this
            .getGroupedMetadataElementsForImportLanguages(importLanguages, Arrays.asList(metaData));

        Assertions.assertNotNull(groupedElements, "Failed to convert API meta data to internal sync data!");
        Assertions.assertEquals(
            importLanguages.length - 1,
            groupedElements[0].getValues().size(),
            "Failed to acquire a value for each import language!"
        );

        Assertions.assertNotEquals(
            importLanguages.length,
            groupedElements[0].getValues().size(),
            "Additional language received a transation although fallback should be disabled!"
        );

        Arrays.sort(groupedElements, (a, b) -> a.getKey().compareTo(b.getKey()));
        Assertions.assertEquals(
            expectedResult,
            gson.toJson(groupedElements),
            "conversion lead to unexpected result!"
        );

    }


    @Test
    @DisplayName("converting to grouped meta data elements without fallback but missing language.")
    public void testGroupedMetadataElements_WithMissingLanguageWithFallback() throws Exception {

        final String[] importLanguages = new String[] { "de", "zh" };
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
            "    \"_key\": \"adobestock\",\n" +
            "    \"_values\": {\n" +
            "      \"de\": \"Adobe Stock\",\n" +
            "      \"zh\": \"Adobe Stock\"\n" +
            "    }\n" +
            "  },\n" +
            "  {\n" +
            "    \"_key\": \"getty\",\n" +
            "    \"_values\": {\n" +
            "      \"de\": \"Getty Images DE\",\n" +
            "      \"zh\": \"Getty Images EN\"\n" +
            "    }\n" +
            "  },\n" +
            "  {\n" +
            "    \"_key\": \"istock\",\n" +
            "    \"_values\": {\n" +
            "      \"de\": \"iStock\",\n" +
            "      \"zh\": \"iStock\"\n" +
            "    }\n" +
            "  },\n" +
            "  {\n" +
            "    \"_key\": \"panthermedia\",\n" +
            "    \"_values\": {\n" +
            "      \"de\": \"PantherMedia\",\n" +
            "      \"zh\": \"PantherMedia\"\n" +
            "    }\n" +
            "  },\n" +
            "  {\n" +
            "    \"_key\": \"shutterstock\",\n" +
            "    \"_values\": {\n" +
            "      \"de\": \"Shutterstock\",\n" +
            "      \"zh\": \"Shutterstock\"\n" +
            "    }\n" +
            "  }\n" +
            "]";

        final ISmintIoMetadataElement[] groupedElements = this
            .getGroupedMetadataElementsForImportLanguages(importLanguages, Arrays.asList(metaData));

        Assertions.assertNotNull(groupedElements, "Failed to convert API meta data to internal sync data!");
        Assertions.assertEquals(
            importLanguages.length,
            groupedElements[0].getValues().size(),
            "Failed to acquire a value for each import language!"
        );

        Arrays.sort(groupedElements, (a, b) -> a.getKey().compareTo(b.getKey()));
        Assertions.assertEquals(
            expectedResult,
            gson.toJson(groupedElements),
            "conversion lead to unexpected result!"
        );
    }


    @Test
    @DisplayName("converting to grouped meta data elements ISO 639-3 language.")
    public void testGroupedMetadataElementsForImportLanguagesWithIso3() throws Exception {

        final String[] importLanguages = new String[] { "en", "deu" };
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
            "    \"_key\": \"adobestock\",\n" +
            "    \"_values\": {\n" +
            "      \"de\": \"Adobe Stock\",\n" +
            "      \"en\": \"Adobe Stock\"\n" +
            "    }\n" +
            "  },\n" +
            "  {\n" +
            "    \"_key\": \"getty\",\n" +
            "    \"_values\": {\n" +
            "      \"de\": \"Getty Images DE\",\n" +
            "      \"en\": \"Getty Images EN\"\n" +
            "    }\n" +
            "  },\n" +
            "  {\n" +
            "    \"_key\": \"istock\",\n" +
            "    \"_values\": {\n" +
            "      \"de\": \"iStock\",\n" +
            "      \"en\": \"iStock\"\n" +
            "    }\n" +
            "  },\n" +
            "  {\n" +
            "    \"_key\": \"panthermedia\",\n" +
            "    \"_values\": {\n" +
            "      \"de\": \"PantherMedia\",\n" +
            "      \"en\": \"PantherMedia\"\n" +
            "    }\n" +
            "  },\n" +
            "  {\n" +
            "    \"_key\": \"shutterstock\",\n" +
            "    \"_values\": {\n" +
            "      \"de\": \"Shutterstock\",\n" +
            "      \"en\": \"Shutterstock\"\n" +
            "    }\n" +
            "  }\n" +
            "]";

        final ISmintIoMetadataElement[] groupedElements = this
            .getGroupedMetadataElementsForImportLanguages(importLanguages, Arrays.asList(metaData));

        Assertions.assertNotNull(groupedElements, "Failed to convert API meta data to internal sync data!");
        Assertions.assertEquals(
            importLanguages.length,
            groupedElements[0].getValues().size(),
            "Failed to acquire a value for each import language!"
        );

        Arrays.sort(groupedElements, (a, b) -> a.getKey().compareTo(b.getKey()));
        Assertions.assertEquals(
            expectedResult,
            gson.toJson(groupedElements),
            "conversion lead to unexpected result!"
        );

    }


    @Test
    @DisplayName("converting to grouped meta data elements for single language.")
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
            "    \"_key\": \"adobestock\",\n" +
            "    \"_values\": {\n" +
            "      \"de\": \"Adobe Stock\"\n" +
            "    }\n" +
            "  },\n" +
            "  {\n" +
            "    \"_key\": \"getty\",\n" +
            "    \"_values\": {\n" +
            "      \"de\": \"Getty Images DE\"\n" +
            "    }\n" +
            "  },\n" +
            "  {\n" +
            "    \"_key\": \"istock\",\n" +
            "    \"_values\": {\n" +
            "      \"de\": \"iStock\"\n" +
            "    }\n" +
            "  },\n" +
            "  {\n" +
            "    \"_key\": \"panthermedia\",\n" +
            "    \"_values\": {\n" +
            "      \"de\": \"PantherMedia\"\n" +
            "    }\n" +
            "  },\n" +
            "  {\n" +
            "    \"_key\": \"shutterstock\",\n" +
            "    \"_values\": {\n" +
            "      \"de\": \"Shutterstock\"\n" +
            "    }\n" +
            "  }\n" +
            "]";

        final ISmintIoMetadataElement[] groupedElements = this
            .getGroupedMetadataElementsForImportLanguages(importLanguages, Arrays.asList(metaData));

        Assertions.assertNotNull(groupedElements, "Failed to convert API meta data to internal sync data!");
        Assertions.assertEquals(
            importLanguages.length,
            groupedElements[0].getValues().size(),
            "Failed to acquire a value for each import language!"
        );

        Arrays.sort(groupedElements, (a, b) -> a.getKey().compareTo(b.getKey()));
        Assertions.assertEquals(
            expectedResult,
            gson.toJson(groupedElements),
            "conversion lead to unexpected result!"
        );

    }


    @Test
    @DisplayName("converting to grouped meta data with single language fallback.")
    public void testGroupedMetadataElementsForSingleLanguageWithFallback() throws Exception {

        final String[] importLanguages = new String[] { "it" };
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
            "    \"_key\": \"adobestock\",\n" +
            "    \"_values\": {\n" +
            "      \"it\": \"Adobe Stock\"\n" +
            "    }\n" +
            "  },\n" +
            "  {\n" +
            "    \"_key\": \"getty\",\n" +
            "    \"_values\": {\n" +
            "      \"it\": \"Getty Images EN\"\n" +
            "    }\n" +
            "  },\n" +
            "  {\n" +
            "    \"_key\": \"istock\",\n" +
            "    \"_values\": {\n" +
            "      \"it\": \"iStock\"\n" +
            "    }\n" +
            "  },\n" +
            "  {\n" +
            "    \"_key\": \"panthermedia\",\n" +
            "    \"_values\": {\n" +
            "      \"it\": \"PantherMedia\"\n" +
            "    }\n" +
            "  },\n" +
            "  {\n" +
            "    \"_key\": \"shutterstock\",\n" +
            "    \"_values\": {\n" +
            "      \"it\": \"Shutterstock\"\n" +
            "    }\n" +
            "  }\n" +
            "]";

        final ISmintIoMetadataElement[] groupedElements = this
            .getGroupedMetadataElementsForImportLanguages(importLanguages, Arrays.asList(metaData));

        Assertions.assertNotNull(groupedElements, "Failed to convert API meta data to internal sync data!");
        Assertions.assertEquals(
            importLanguages.length,
            groupedElements[0].getValues().size(),
            "Failed to acquire a value for each import language!"
        );

        Arrays.sort(groupedElements, (a, b) -> a.getKey().compareTo(b.getKey()));
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
        final Method getGrouped = this.getPrivateFunction(
            apiClient,
            "getGroupedMetadataElementsForImportLanguages",
            List.class, List.class
        );

        final List<Locale> languages = Arrays.asList(importLanguages)
            .stream()
            .map((lang) -> LocaleUtility.covertToISO2Locale(new Locale(lang)))
            .collect(Collectors.toList());

        return (ISmintIoMetadataElement[]) getGrouped.invoke(apiClient, languages, elements);
    }

}

// CHECKSTYLE.ON: MultipleStringLiterals
// CHECKSTYLE.ON: MagicNumber
