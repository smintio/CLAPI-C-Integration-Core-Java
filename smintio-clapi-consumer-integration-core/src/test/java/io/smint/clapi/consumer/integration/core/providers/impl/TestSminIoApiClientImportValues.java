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
import java.util.Map;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.smint.clapi.consumer.generated.models.LocalizedString;
import io.smint.clapi.consumer.integration.core.LocaleUtility;


// CHECKSTYLE.OFF: MultipleStringLiterals
// CHECKSTYLE.OFF: MagicNumber

@DisplayName("Test SmintIoApiClientImpl: getValuesForImportLanguages")
public class TestSminIoApiClientImportValues extends TestSminIoApiClientBase {

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Test
    @DisplayName("converting to localized name.")
    public void testlocalizedNamesForImportLanguages() throws Exception {

        final String[] importLanguages = new String[] { "en", "de" };
        final String expectedResult = "{\n"
            + "  \"de\": \"mouse DE\",\n"
            + "  \"en\": \"mouse EN\"\n"
            + "}";

        final Map<Locale, String> localizedValues = this
            .getValuesForImportLanguages(importLanguages, this.getTestData());

        Assertions.assertNotNull(localizedValues, "Failed to convert API meta data to internal sync data!");
        Assertions.assertEquals(
            importLanguages.length,
            localizedValues.size(),
            "Failed to acquire a value for each import language!"
        );

        Assertions.assertEquals(
            expectedResult,
            this.gson.toJson(localizedValues),
            "conversion lead to unexpected result!"
        );
    }


    @Test
    @DisplayName("converting to localized text for multiple languages with language fallback.")
    public void testlocalizedValuesForImportLanguages_WithFallback() throws Exception {

        final String[] importLanguages = new String[] { "zh", "de", "fr" };
        final String expectedResult = "{\n"
            + "  \"de\": \"mouse DE\",\n"
            + "  \"zh\": \"mouse ZH\",\n"
            + "  \"fr\": \"mouse EN\"\n"
            + "}";

        final Map<Locale, String> localizedValues = this
            .getValuesForImportLanguages(importLanguages, this.getTestData());

        Assertions.assertNotNull(localizedValues, "Failed to convert API meta data to internal sync data!");
        Assertions.assertEquals(
            importLanguages.length,
            localizedValues.size(),
            "Failed to acquire a value for each import language!"
        );

        Assertions.assertEquals(
            expectedResult,
            this.gson.toJson(localizedValues),
            "conversion lead to unexpected result!"
        );
    }


    @Test
    @DisplayName("converting to localized text for single language with language fallback.")
    public void testlocalizedValuesForImportLanguages_SingleLanguageWithFallback() throws Exception {

        final String[] importLanguages = new String[] { "fr" };
        final String expectedResult = "{\n"
            + "  \"fr\": \"mouse EN\"\n"
            + "}";

        final Map<Locale, String> localizedValues = this
            .getValuesForImportLanguages(importLanguages, this.getTestData());

        Assertions.assertNotNull(localizedValues, "Failed to convert API meta data to internal sync data!");
        Assertions.assertEquals(
            importLanguages.length,
            localizedValues.size(),
            "Failed to acquire a value for each import language!"
        );

        Assertions.assertEquals(
            expectedResult,
            this.gson.toJson(localizedValues),
            "conversion lead to unexpected result!"
        );
    }


    @Test
    @DisplayName("converting to glocalized text for single language.")
    public void testlocalizedValuesForImportLanguages_SingleLanguage() throws Exception {

        final String[] importLanguages = new String[] { "ita" };
        final String expectedResult = "{\n"
            + "  \"it\": \"mouse IT\"\n"
            + "}";

        final Map<Locale, String> localizedValues = this
            .getValuesForImportLanguages(importLanguages, this.getTestData());

        Assertions.assertNotNull(localizedValues, "Failed to convert API meta data to internal sync data!");
        Assertions.assertEquals(
            importLanguages.length,
            localizedValues.size(),
            "Failed to acquire a value for each import language!"
        );

        Assertions.assertEquals(
            expectedResult,
            this.gson.toJson(localizedValues),
            "conversion lead to unexpected result!"
        );
    }


    @SuppressWarnings("unchecked")
    public Map<Locale, String> getValuesForImportLanguages(
        final String[] importLanguages, final List<LocalizedString> elements
    ) throws Exception {

        final SmintIoApiClientImpl apiClient = this.createApiClient(importLanguages);
        final Method getGrouped = this.getPrivateFunction(
            apiClient,
            "getValuesForImportLanguages",
            List.class, List.class
        );

        final List<Locale> languages = Arrays.asList(importLanguages)
            .stream()
            .map((lang) -> LocaleUtility.covertToISO2Locale(new Locale(lang)))
            .collect(Collectors.toList());

        return (Map<Locale, String>) getGrouped.invoke(apiClient, languages, elements);
    }


    private List<LocalizedString> getTestData() {

        final LocalizedString[] metaData = this.gson.fromJson(
            "[\n" +
                "        {\n" +
                "          \"culture\": \"en\",\n" +
                "          \"value\": \"mouse EN\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"culture\": \"de\",\n" +
                "          \"value\": \"mouse DE\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"culture\": \"zh\",\n" +
                "          \"value\": \"mouse ZH\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"culture\": \"it\",\n" +
                "          \"value\": \"mouse IT\"\n" +
                "        },\n" +
                "]",
            LocalizedString[].class
        );
        Assertions.assertNotNull(metaData, "Failed read test data from JSON!");
        return Arrays.asList(metaData);
    }
}

// CHECKSTYLE.ON: MultipleStringLiterals
// CHECKSTYLE.ON: MagicNumber
