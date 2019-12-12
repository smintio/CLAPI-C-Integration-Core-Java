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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.google.gson.Gson;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.smint.clapi.consumer.generated.models.LocalizedMetadataElement;
import io.smint.clapi.consumer.generated.models.LocalizedString;
import io.smint.clapi.consumer.generated.models.SyncLicensePurchaseTransaction;
import io.smint.clapi.consumer.integration.core.contracts.ISmintIoReleaseDetails;
import io.smint.clapi.consumer.integration.core.factory.impl.SmintIoGsonProvider;


// CHECKSTYLE.OFF: MultipleStringLiterals
// CHECKSTYLE.OFF: MagicNumber

@DisplayName("Test asset loading of Smint.io API provider: SmintIoApiClientImpl")
public class TestSminIoApiClientLoadAsset extends TestSminIoApiClientBase {


    @Test
    @DisplayName("converting localized string, like asset name.")
    public void testValuesForImportLanguages() throws Exception {

        final String[] importLanguages = new String[] { "en", "de" };
        final Gson gson = new SmintIoGsonProvider().get();
        final LocalizedString[] localizedStrings = gson.fromJson(
            "[\n" +
                "    {\n" +
                "      \"culture\": \"en\",\n" +
                "      \"value\": \"little mouse hidingher muzzle\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"culture\": \"de\",\n" +
                "      \"value\": \"Kleine Maus die ihre Schnautze versteckt\"\n" +
                "    },\n" +
                "]\n" +
                "",
            LocalizedString[].class
        );
        Assertions.assertNotNull(localizedStrings, "Failed read test data from JSON!");


        final SmintIoApiClientImpl clientApi = this.createApiClient(importLanguages);
        final Method getValuesForImportLanguages = this.getPrivateFunction(
            clientApi, "getValuesForImportLanguages", List.class, List.class
        );
        @SuppressWarnings("unchecked")
        final Map<Locale, String> convertedTexts = (Map<Locale, String>) getValuesForImportLanguages
            .invoke(clientApi, this.convertImportLanguages(importLanguages), Arrays.asList(localizedStrings));

        Assertions.assertNotNull(convertedTexts, "Failed to convert API localized Strings to sync language texts!");
        Assertions.assertEquals(
            importLanguages.length,
            convertedTexts.size(),
            "Failed to acquire a value for each import language!"
        );

        Assertions.assertEquals(
            "{\n" +
                "  \"de\": \"Kleine Maus die ihre Schnautze versteckt\",\n" +
                "  \"en\": \"little mouse hidingher muzzle\"\n" +
                "}",
            gson.toJson(convertedTexts),
            "conversion lead to unexpected result!"
        );
    }


    @Test
    @DisplayName("converting keywords to import languages.")
    public void testGroupedValuesForImportLanguages() throws Exception {

        final String[] importLanguages = new String[] { "en" };
        final Gson gson = new SmintIoGsonProvider().get();
        final LocalizedMetadataElement[] localizedStrings = gson.fromJson(
            "[\n" +
                "        {\n" +
                "          \"culture\": \"en\",\n" +
                "          \"metadata_element\": {\n" +
                "            \"name\": \"rat\"\n" +
                "          }\n" +
                "        },\n" +
                "        {\n" +
                "          \"culture\": \"en\",\n" +
                "          \"metadata_element\": {\n" +
                "            \"name\": \"mouse\"\n" +
                "          }\n" +
                "        },\n" +
                "        {\n" +
                "          \"culture\": \"en\",\n" +
                "          \"metadata_element\": {\n" +
                "            \"name\": \"animal\"\n" +
                "          }\n" +
                "        },\n" +
                "        {\n" +
                "          \"culture\": \"en\",\n" +
                "          \"metadata_element\": {\n" +
                "            \"name\": \"white background\"\n" +
                "          }\n" +
                "        },\n" +
                "        {\n" +
                "          \"culture\": \"en\",\n" +
                "          \"metadata_element\": {\n" +
                "            \"name\": \"pest\"\n" +
                "          }\n" +
                "        },\n" +
                "        {\n" +
                "          \"culture\": \"en\",\n" +
                "          \"metadata_element\": {\n" +
                "            \"name\": \"hide\"\n" +
                "          }\n" +
                "        }\n" +
                "      ]",
            LocalizedMetadataElement[].class
        );
        Assertions.assertNotNull(localizedStrings, "Failed read test data from JSON!");


        final SmintIoApiClientImpl clientApi = this.createApiClient(importLanguages);
        final Method getGroupedValuesForImportLanguages = this.getPrivateFunction(
            clientApi, "getGroupedValuesForImportLanguages", List.class, List.class
        );
        @SuppressWarnings("unchecked")
        final Map<Locale, String[]> convertedTexts = (Map<Locale, String[]>) getGroupedValuesForImportLanguages
            .invoke(clientApi, this.convertImportLanguages(importLanguages), Arrays.asList(localizedStrings));


        Assertions.assertNotNull(convertedTexts, "Failed to convert API localized Strings to sync language texts!");
        Assertions.assertEquals(
            importLanguages.length,
            convertedTexts.size(),
            "Failed to acquire a value for each import language!"
        );


        Assertions.assertEquals(
            "{\n" +
                "  \"en\": [\n" +
                "    \"rat\",\n" +
                "    \"mouse\",\n" +
                "    \"animal\",\n" +
                "    \"white background\",\n" +
                "    \"pest\",\n" +
                "    \"hide\"\n" +
                "  ]\n" +
                "}",
            gson.toJson(convertedTexts),
            "conversion lead to unexpected result!"
        );
    }


    @Test
    @DisplayName("converting API language to ISO 639-3.")
    public void testConvertApiLanguages() throws Exception {

        final String[] importLanguages = new String[] { "en" };
        final List<String> apiLanguages = new ArrayList<>();

        apiLanguages.add("language_english");
        apiLanguages.add("language_german");
        apiLanguages.add("language_french");
        apiLanguages.add("language_spanish");
        apiLanguages.add("language_mandarin_chinese");
        apiLanguages.add("language_hindustani");
        apiLanguages.add("language_arabic");
        apiLanguages.add("language_malay");
        apiLanguages.add("language_russian");
        apiLanguages.add("language_bengali");
        apiLanguages.add("language_portuguese");


        final SmintIoApiClientImpl clientApi = this.createApiClient(importLanguages);
        final Method convertApiLanguages = this.getPrivateFunction(
            clientApi, "convertApiLanguages", List.class
        );
        final String[] convertedLanguages = (String[]) convertApiLanguages
            .invoke(clientApi, apiLanguages);


        Assertions.assertNotNull(convertedLanguages, "Failed to convert API languages to ISO 639-3 codes!");
        Assertions.assertEquals(
            apiLanguages.size(),
            convertedLanguages.length,
            "Failed to acquire a value for each API language!"
        );

        for (final String isoLang : convertedLanguages) {

            final Locale locale = new Locale(isoLang);
            Assertions.assertNotNull(locale.getISO3Language(), "failed to convert API language to ISO 639-3 code.");
            Assertions.assertEquals(
                isoLang, locale.getISO3Language(), "invalid conversion from API language to ISO 639-3 code."
            );
        }
    }


    @Test
    @DisplayName("extracting release detail from API asset.")
    public void testReleaseDetails() throws Exception {

        final String[] importLanguages = new String[] { "en", "de" };


        final SmintIoApiClientImpl clientApi = this.createApiClient(importLanguages);
        final Method getReleaseDetails = this.getPrivateFunction(
            clientApi, "getReleaseDetails", List.class, SyncLicensePurchaseTransaction.class
        );
        final ISmintIoReleaseDetails releaseDetail = (ISmintIoReleaseDetails) getReleaseDetails
            .invoke(clientApi, this.convertImportLanguages(importLanguages), this.loadTestAssetData());


        Assertions.assertNull(releaseDetail, "Failed to extract release detail form API asset!");
// System.out.println(this.toJson(releaseDetail));
    }


    public SyncLicensePurchaseTransaction loadTestAssetData() {

        final Gson gson = new SmintIoGsonProvider().get();
        return gson.fromJson(
            "{\n" +
                "    \"uuid\": \"79468\",\n" +
                "    \"cart_purchase_transaction_uuid\": \"3525\",\n" +
                "    \"state\": \"completed\",\n" +
                "    \"project_uuid\": \"227\",\n" +
                "    \"project_name\": [\n" +
                "      {\n" +
                "        \"culture\": \"en\",\n" +
                "        \"value\": \"Testprojekt\"\n" +
                "      }\n" +
                "    ],\n" +
                "    \"collection_uuid\": \"1339\",\n" +
                "    \"collection_name\": [\n" +
                "      {\n" +
                "        \"culture\": \"en\",\n" +
                "        \"value\": \"Cart\"\n" +
                "      }\n" +
                "    ],\n" +
                "    \"collection_content_element_uuid\": \"9748\",\n" +
                "    \"collection_content_element_created_at\": {\n" +
                "      \"dateTime\": {\n" +
                "        \"date\": {\n" +
                "          \"year\": 2019,\n" +
                "          \"month\": 7,\n" +
                "          \"day\": 1\n" +
                "        },\n" +
                "        \"time\": {\n" +
                "          \"hour\": 5,\n" +
                "          \"minute\": 17,\n" +
                "          \"second\": 30,\n" +
                "          \"nano\": 577286000\n" +
                "        }\n" +
                "      },\n" +
                "      \"offset\": {\n" +
                "        \"totalSeconds\": 0\n" +
                "      }\n" +
                "    },\n" +
                "    \"collection_content_element_created_by_user_uuid\": \"b999a063-2025-40ae-8a0b-eae83828c079\",\n" +
                "    \"collection_content_element_created_by_user_name\": \"Linda Gratzer\",\n" +
                "    \"total_price_eur\": 0.0,\n" +
                "    \"total_price_usd\": 0.0,\n" +
                "    \"content_element\": {\n" +
                "      \"uuid\": \"2:0:61019726\",\n" +
                "      \"provider\": \"adobestock\",\n" +
                "      \"content_type\": \"image\",\n" +
                "      \"content_category\": \"creative\",\n" +
                "      \"editorial_segments\": [],\n" +
                "      \"name\": [\n" +
                "        {\n" +
                "          \"culture\": \"en\",\n" +
                "          \"value\": \"little mouse hidingher muzzle\"\n" +
                "        }\n" +
                "      ],\n" +
                "      \"description\": [],\n" +
                "      \"image_details\": {\n" +
                "        \"type\": \"photo\",\n" +
                "        \"aspect_ratio\": 1.2302857142857142857142857143,\n" +
                "        \"orientation\": \"horizontal\"\n" +
                "      },\n" +
                "      \"events\": [],\n" +
                "      \"contributors\": [\n" +
                "        {\n" +
                "          \"culture\": \"en\",\n" +
                "          \"metadata_element\": {\n" +
                "            \"key\": \"2:201135494\",\n" +
                "            \"name\": \"Vera Kuttelvaserova\"\n" +
                "          }\n" +
                "        }\n" +
                "      ],\n" +
                "      \"copyright_notices\": [\n" +
                "        {\n" +
                "          \"culture\": \"en\",\n" +
                "          \"value\": \"Vera Kuttelvaserova / www.adobestock.com\"\n" +
                "        }\n" +
                "      ],\n" +
                "      \"collections\": [],\n" +
                "      \"is_premium\": false,\n" +
                "      \"is_editorial_use\": false,\n" +
                "      \"is_editorial\": false,\n" +
                "      \"created_at\": {\n" +
                "        \"dateTime\": {\n" +
                "          \"date\": {\n" +
                "            \"year\": 2014,\n" +
                "            \"month\": 2,\n" +
                "            \"day\": 3\n" +
                "          },\n" +
                "          \"time\": {\n" +
                "            \"hour\": 11,\n" +
                "            \"minute\": 2,\n" +
                "            \"second\": 20,\n" +
                "            \"nano\": 414628000\n" +
                "          }\n" +
                "        },\n" +
                "        \"offset\": {\n" +
                "          \"totalSeconds\": 0\n" +
                "        }\n" +
                "      },\n" +
                "      \"provider_urls\": [\n" +
                "        {\n" +
                "          \"culture\": \"en\",\n" +
                "          \"metadata_element\": {\n" +
                "            \"key\": \"adobestock\",\n" +
                "            \"name\": \"Adobe Stock\",\n" +
                "            \"url\": \"https://stock.adobe.com/61019726\"\n" +
                "          }\n" +
                "        }\n" +
                "      ],\n" +
                "      \"keywords\": [\n" +
                "        {\n" +
                "          \"culture\": \"en\",\n" +
                "          \"metadata_element\": {\n" +
                "            \"name\": \"rat\"\n" +
                "          }\n" +
                "        },\n" +
                "        {\n" +
                "          \"culture\": \"en\",\n" +
                "          \"metadata_element\": {\n" +
                "            \"name\": \"mouse\"\n" +
                "          }\n" +
                "        },\n" +
                "        {\n" +
                "          \"culture\": \"en\",\n" +
                "          \"metadata_element\": {\n" +
                "            \"name\": \"animal\"\n" +
                "          }\n" +
                "        },\n" +
                "        {\n" +
                "          \"culture\": \"en\",\n" +
                "          \"metadata_element\": {\n" +
                "            \"name\": \"white background\"\n" +
                "          }\n" +
                "        },\n" +
                "        {\n" +
                "          \"culture\": \"en\",\n" +
                "          \"metadata_element\": {\n" +
                "            \"name\": \"pest\"\n" +
                "          }\n" +
                "        },\n" +
                "        {\n" +
                "          \"culture\": \"en\",\n" +
                "          \"metadata_element\": {\n" +
                "            \"name\": \"rodent\"\n" +
                "          }\n" +
                "        },\n" +
                "        {\n" +
                "          \"culture\": \"en\",\n" +
                "          \"metadata_element\": {\n" +
                "            \"name\": \"isolated\"\n" +
                "          }\n" +
                "        },\n" +
                "        {\n" +
                "          \"culture\": \"en\",\n" +
                "          \"metadata_element\": {\n" +
                "            \"name\": \"warfare\"\n" +
                "          }\n" +
                "        },\n" +
                "        {\n" +
                "          \"culture\": \"en\",\n" +
                "          \"metadata_element\": {\n" +
                "            \"name\": \"bait\"\n" +
                "          }\n" +
                "        },\n" +
                "        {\n" +
                "          \"culture\": \"en\",\n" +
                "          \"metadata_element\": {\n" +
                "            \"name\": \"hairy\"\n" +
                "          }\n" +
                "        },\n" +
                "        {\n" +
                "          \"culture\": \"en\",\n" +
                "          \"metadata_element\": {\n" +
                "            \"name\": \"mammal\"\n" +
                "          }\n" +
                "        },\n" +
                "        {\n" +
                "          \"culture\": \"en\",\n" +
                "          \"metadata_element\": {\n" +
                "            \"name\": \"paw\"\n" +
                "          }\n" +
                "        },\n" +
                "        {\n" +
                "          \"culture\": \"en\",\n" +
                "          \"metadata_element\": {\n" +
                "            \"name\": \"tail\"\n" +
                "          }\n" +
                "        },\n" +
                "        {\n" +
                "          \"culture\": \"en\",\n" +
                "          \"metadata_element\": {\n" +
                "            \"name\": \"pollution\"\n" +
                "          }\n" +
                "        },\n" +
                "        {\n" +
                "          \"culture\": \"en\",\n" +
                "          \"metadata_element\": {\n" +
                "            \"name\": \"sniff\"\n" +
                "          }\n" +
                "        },\n" +
                "        {\n" +
                "          \"culture\": \"en\",\n" +
                "          \"metadata_element\": {\n" +
                "            \"name\": \"shot\"\n" +
                "          }\n" +
                "        },\n" +
                "        {\n" +
                "          \"culture\": \"en\",\n" +
                "          \"metadata_element\": {\n" +
                "            \"name\": \"isolation\"\n" +
                "          }\n" +
                "        },\n" +
                "        {\n" +
                "          \"culture\": \"en\",\n" +
                "          \"metadata_element\": {\n" +
                "            \"name\": \"head\"\n" +
                "          }\n" +
                "        },\n" +
                "        {\n" +
                "          \"culture\": \"en\",\n" +
                "          \"metadata_element\": {\n" +
                "            \"name\": \"studio\"\n" +
                "          }\n" +
                "        },\n" +
                "        {\n" +
                "          \"culture\": \"en\",\n" +
                "          \"metadata_element\": {\n" +
                "            \"name\": \"infection\"\n" +
                "          }\n" +
                "        },\n" +
                "        {\n" +
                "          \"culture\": \"en\",\n" +
                "          \"metadata_element\": {\n" +
                "            \"name\": \"grey\"\n" +
                "          }\n" +
                "        },\n" +
                "        {\n" +
                "          \"culture\": \"en\",\n" +
                "          \"metadata_element\": {\n" +
                "            \"name\": \"portrait\"\n" +
                "          }\n" +
                "        },\n" +
                "        {\n" +
                "          \"culture\": \"en\",\n" +
                "          \"metadata_element\": {\n" +
                "            \"name\": \"cute\"\n" +
                "          }\n" +
                "        },\n" +
                "        {\n" +
                "          \"culture\": \"en\",\n" +
                "          \"metadata_element\": {\n" +
                "            \"name\": \"grey\"\n" +
                "          }\n" +
                "        },\n" +
                "        {\n" +
                "          \"culture\": \"en\",\n" +
                "          \"metadata_element\": {\n" +
                "            \"name\": \"small\"\n" +
                "          }\n" +
                "        },\n" +
                "        {\n" +
                "          \"culture\": \"en\",\n" +
                "          \"metadata_element\": {\n" +
                "            \"name\": \"funny\"\n" +
                "          }\n" +
                "        },\n" +
                "        {\n" +
                "          \"culture\": \"en\",\n" +
                "          \"metadata_element\": {\n" +
                "            \"name\": \"up\"\n" +
                "          }\n" +
                "        },\n" +
                "        {\n" +
                "          \"culture\": \"en\",\n" +
                "          \"metadata_element\": {\n" +
                "            \"name\": \"look\"\n" +
                "          }\n" +
                "        },\n" +
                "        {\n" +
                "          \"culture\": \"en\",\n" +
                "          \"metadata_element\": {\n" +
                "            \"name\": \"close\"\n" +
                "          }\n" +
                "        },\n" +
                "        {\n" +
                "          \"culture\": \"en\",\n" +
                "          \"metadata_element\": {\n" +
                "            \"name\": \"ugly\"\n" +
                "          }\n" +
                "        },\n" +
                "        {\n" +
                "          \"culture\": \"en\",\n" +
                "          \"metadata_element\": {\n" +
                "            \"name\": \"lovely\"\n" +
                "          }\n" +
                "        },\n" +
                "        {\n" +
                "          \"culture\": \"en\",\n" +
                "          \"metadata_element\": {\n" +
                "            \"name\": \"furry\"\n" +
                "          }\n" +
                "        },\n" +
                "        {\n" +
                "          \"culture\": \"en\",\n" +
                "          \"metadata_element\": {\n" +
                "            \"name\": \"background\"\n" +
                "          }\n" +
                "        },\n" +
                "        {\n" +
                "          \"culture\": \"en\",\n" +
                "          \"metadata_element\": {\n" +
                "            \"name\": \"back\"\n" +
                "          }\n" +
                "        },\n" +
                "        {\n" +
                "          \"culture\": \"en\",\n" +
                "          \"metadata_element\": {\n" +
                "            \"name\": \"domestic\"\n" +
                "          }\n" +
                "        },\n" +
                "        {\n" +
                "          \"culture\": \"en\",\n" +
                "          \"metadata_element\": {\n" +
                "            \"name\": \"bread\"\n" +
                "          }\n" +
                "        },\n" +
                "        {\n" +
                "          \"culture\": \"en\",\n" +
                "          \"metadata_element\": {\n" +
                "            \"name\": \"smell\"\n" +
                "          }\n" +
                "        },\n" +
                "        {\n" +
                "          \"culture\": \"en\",\n" +
                "          \"metadata_element\": {\n" +
                "            \"name\": \"pet\"\n" +
                "          }\n" +
                "        },\n" +
                "        {\n" +
                "          \"culture\": \"en\",\n" +
                "          \"metadata_element\": {\n" +
                "            \"name\": \"food\"\n" +
                "          }\n" +
                "        },\n" +
                "        {\n" +
                "          \"culture\": \"en\",\n" +
                "          \"metadata_element\": {\n" +
                "            \"name\": \"nose\"\n" +
                "          }\n" +
                "        },\n" +
                "        {\n" +
                "          \"culture\": \"en\",\n" +
                "          \"metadata_element\": {\n" +
                "            \"name\": \"disease\"\n" +
                "          }\n" +
                "        },\n" +
                "        {\n" +
                "          \"culture\": \"en\",\n" +
                "          \"metadata_element\": {\n" +
                "            \"name\": \"virus\"\n" +
                "          }\n" +
                "        },\n" +
                "        {\n" +
                "          \"culture\": \"en\",\n" +
                "          \"metadata_element\": {\n" +
                "            \"name\": \"muzzle\"\n" +
                "          }\n" +
                "        },\n" +
                "        {\n" +
                "          \"culture\": \"en\",\n" +
                "          \"metadata_element\": {\n" +
                "            \"name\": \"shy\"\n" +
                "          }\n" +
                "        },\n" +
                "        {\n" +
                "          \"culture\": \"en\",\n" +
                "          \"metadata_element\": {\n" +
                "            \"name\": \"hide\"\n" +
                "          }\n" +
                "        }\n" +
                "      ],\n" +
                "      \"categories\": [\n" +
                "        {\n" +
                "          \"culture\": \"en\",\n" +
                "          \"metadata_element\": {\n" +
                "            \"key\": \"2:61\",\n" +
                "            \"name\": \"Other Pets\"\n" +
                "          }\n" +
                "        }\n" +
                "      ],\n" +
                "      \"previews\": [\n" +
                "        {\n" +
                "          \"culture\": \"en\",\n" +
                "          \"preview\": {\n" +
                "            \"thumbnail\": {\n" +
                "              \"width\": 295,\n" +
                "              \"height\": 240,\n" +
                "              \"media_type\": \"image/jpeg\",\n" +
                "              \"url\": \"https://t4.ftcdn.net/jpg/00/61/01/97/240_F_61019726_rHnt8FcdfOoNL8lO2FhrPn1NemsdptnX.jpg\"\n"
                +
                "            },\n" +
                "            \"preview\": {\n" +
                "              \"width\": 1000,\n" +
                "              \"height\": 813,\n" +
                "              \"media_type\": \"image/jpeg\",\n" +
                "              \"url\": \"https://as2.ftcdn.net/jpg/00/61/01/97/1000_F_61019726_rHnt8FcdfOoNL8lO2FhrPn1NemsdptnX.jpg\"\n"
                +
                "            }\n" +
                "          }\n" +
                "        }\n" +
                "      ]\n" +
                "    },\n" +
                "    \"offering\": {\n" +
                "      \"license_uuid\": \"image_standard\",\n" +
                "      \"provider_configuration_uuid\": \"18\",\n" +
                "      \"provider_configuration_name\": [\n" +
                "        {\n" +
                "          \"culture\": \"en\",\n" +
                "          \"value\": \"Adobe Stock\"\n" +
                "        }\n" +
                "      ],\n" +
                "      \"provider_configuration_contact_person\": {},\n" +
                "      \"contract_uuid\": \"132\",\n" +
                "      \"contract_name\": [\n" +
                "        {\n" +
                "          \"culture\": \"en\",\n" +
                "          \"value\": \"Standard License\"\n" +
                "        }\n" +
                "      ],\n" +
                "      \"contract_description\": [],\n" +
                "      \"offering_uuid\": \"968\",\n" +
                "      \"offering_name\": [\n" +
                "        {\n" +
                "          \"culture\": \"en\",\n" +
                "          \"value\": \"Standard Image\"\n" +
                "        }\n" +
                "      ],\n" +
                "      \"offering_label\": [\n" +
                "        {\n" +
                "          \"culture\": \"en\",\n" +
                "          \"value\": \"Standard\"\n" +
                "        }\n" +
                "      ],\n" +
                "      \"has_options\": false,\n" +
                "      \"options\": [],\n" +
                "      \"license_type\": \"royalty_free\",\n" +
                "      \"media_type\": \"image/jpeg\",\n" +
                "      \"size\": \"large\",\n" +
                "      \"format\": \"fixed\",\n" +
                "      \"resolution\": 15071000,\n" +
                "      \"is_premium\": false,\n" +
                "      \"is_editorial_use\": false,\n" +
                "      \"is_editorial\": false,\n" +
                "      \"image_details\": {\n" +
                "        \"width\": 4306,\n" +
                "        \"height\": 3500,\n" +
                "        \"resolution_megapixels\": 15\n" +
                "      },\n" +
                "      \"total_price_eur\": 0.0,\n" +
                "      \"total_price_usd\": 0.0,\n" +
                "      \"licensee_uuid\": \"11\",\n" +
                "      \"licensee_name\": \"Smint.io Smarter Interfaces GmbH\",\n" +
                "      \"license_text\": {\n" +
                "        \"effective_text\": [\n" +
                "          {\n" +
                "            \"culture\": \"en\",\n" +
                "            \"value\": \"NUR DEMO - NICHT FÜR VERWENDUNG FREIGEGEBEN!\"\n" +
                "          }\n" +
                "        ],\n" +
                "        \"effective_text_is_safe_html\": false\n" +
                "      },\n" +
                "      \"license_terms\": [\n" +
                "        {\n" +
                "          \"sequence_number\": 1,\n" +
                "          \"name\": [\n" +
                "            {\n" +
                "              \"culture\": \"en\",\n" +
                "              \"value\": \"Default\"\n" +
                "            },\n" +
                "            {\n" +
                "              \"culture\": \"de\",\n" +
                "              \"value\": \"Standard\"\n" +
                "            }\n" +
                "          ],\n" +
                "          \"exclusivities\": [],\n" +
                "          \"allowed_usages\": [],\n" +
                "          \"restricted_usages\": [],\n" +
                "          \"allowed_sizes\": [],\n" +
                "          \"restricted_sizes\": [],\n" +
                "          \"allowed_placements\": [],\n" +
                "          \"restricted_placements\": [],\n" +
                "          \"allowed_distributions\": [],\n" +
                "          \"restricted_distributions\": [],\n" +
                "          \"allowed_geographies\": [],\n" +
                "          \"restricted_geographies\": [],\n" +
                "          \"allowed_industries\": [],\n" +
                "          \"restricted_industries\": [],\n" +
                "          \"allowed_languages\": [],\n" +
                "          \"restricted_languages\": [],\n" +
                "          \"usage_limits\": [],\n" +
                "          \"is_editorial_use\": false\n" +
                "        }\n" +
                "      ],\n" +
                "      \"license_download_constraints\": {},\n" +
                "      \"has_layout_file\": true\n" +
                "    },\n" +
                "    \"purchased_at\": {\n" +
                "      \"dateTime\": {\n" +
                "        \"date\": {\n" +
                "          \"year\": 2019,\n" +
                "          \"month\": 7,\n" +
                "          \"day\": 1\n" +
                "        },\n" +
                "        \"time\": {\n" +
                "          \"hour\": 5,\n" +
                "          \"minute\": 18,\n" +
                "          \"second\": 51,\n" +
                "          \"nano\": 397267000\n" +
                "        }\n" +
                "      },\n" +
                "      \"offset\": {\n" +
                "        \"totalSeconds\": 0\n" +
                "      }\n" +
                "    },\n" +
                "    \"completed_at\": {\n" +
                "      \"dateTime\": {\n" +
                "        \"date\": {\n" +
                "          \"year\": 2019,\n" +
                "          \"month\": 7,\n" +
                "          \"day\": 1\n" +
                "        },\n" +
                "        \"time\": {\n" +
                "          \"hour\": 5,\n" +
                "          \"minute\": 19,\n" +
                "          \"second\": 12,\n" +
                "          \"nano\": 486834000\n" +
                "        }\n" +
                "      },\n" +
                "      \"offset\": {\n" +
                "        \"totalSeconds\": 0\n" +
                "      }\n" +
                "    },\n" +
                "    \"purchased_by_user_uuid\": \"b999a063-2025-40ae-8a0b-eae83828c079\",\n" +
                "    \"purchased_by_user_name\": \"Linda Gratzer\",\n" +
                "    \"licensee_uuid\": \"11\",\n" +
                "    \"licensee_name\": \"Smint.io Smarter Interfaces GmbH\",\n" +
                "    \"license_terms\": [\n" +
                "      {\n" +
                "        \"sequence_number\": 1,\n" +
                "        \"name\": [\n" +
                "          {\n" +
                "            \"culture\": \"en\",\n" +
                "            \"value\": \"Default\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"culture\": \"de\",\n" +
                "            \"value\": \"Standard\"\n" +
                "          }\n" +
                "        ],\n" +
                "        \"exclusivities\": [],\n" +
                "        \"allowed_usages\": [],\n" +
                "        \"restricted_usages\": [],\n" +
                "        \"allowed_sizes\": [],\n" +
                "        \"restricted_sizes\": [],\n" +
                "        \"allowed_placements\": [],\n" +
                "        \"restricted_placements\": [],\n" +
                "        \"allowed_distributions\": [],\n" +
                "        \"restricted_distributions\": [],\n" +
                "        \"allowed_geographies\": [],\n" +
                "        \"restricted_geographies\": [],\n" +
                "        \"allowed_industries\": [],\n" +
                "        \"restricted_industries\": [],\n" +
                "        \"allowed_languages\": [],\n" +
                "        \"restricted_languages\": [],\n" +
                "        \"usage_limits\": [],\n" +
                "        \"valid_from\": {\n" +
                "          \"dateTime\": {\n" +
                "            \"date\": {\n" +
                "              \"year\": 2019,\n" +
                "              \"month\": 7,\n" +
                "              \"day\": 1\n" +
                "            },\n" +
                "            \"time\": {\n" +
                "              \"hour\": 5,\n" +
                "              \"minute\": 18,\n" +
                "              \"second\": 51,\n" +
                "              \"nano\": 397267000\n" +
                "            }\n" +
                "          },\n" +
                "          \"offset\": {\n" +
                "            \"totalSeconds\": 0\n" +
                "          }\n" +
                "        },\n" +
                "        \"is_editorial_use\": false\n" +
                "      }\n" +
                "    ],\n" +
                "    \"license_download_constraints\": {},\n" +
                "    \"current_downloads\": 0,\n" +
                "    \"current_users\": 0,\n" +
                "    \"current_reuses\": 0,\n" +
                "    \"can_be_synced\": true,\n" +
                "    \"version\": 4,\n" +
                "    \"created_at\": {\n" +
                "      \"dateTime\": {\n" +
                "        \"date\": {\n" +
                "          \"year\": 2019,\n" +
                "          \"month\": 7,\n" +
                "          \"day\": 1\n" +
                "        },\n" +
                "        \"time\": {\n" +
                "          \"hour\": 5,\n" +
                "          \"minute\": 18,\n" +
                "          \"second\": 51,\n" +
                "          \"nano\": 397267000\n" +
                "        }\n" +
                "      },\n" +
                "      \"offset\": {\n" +
                "        \"totalSeconds\": 0\n" +
                "      }\n" +
                "    },\n" +
                "    \"last_updated_at\": {\n" +
                "      \"dateTime\": {\n" +
                "        \"date\": {\n" +
                "          \"year\": 2019,\n" +
                "          \"month\": 7,\n" +
                "          \"day\": 1\n" +
                "        },\n" +
                "        \"time\": {\n" +
                "          \"hour\": 5,\n" +
                "          \"minute\": 19,\n" +
                "          \"second\": 12,\n" +
                "          \"nano\": 486834000\n" +
                "        }\n" +
                "      },\n" +
                "      \"offset\": {\n" +
                "        \"totalSeconds\": 0\n" +
                "      }\n" +
                "    }\n" +
                "  }",
            SyncLicensePurchaseTransaction.class
        );

    }
}

// CHECKSTYLE.ON: MultipleStringLiterals
// CHECKSTYLE.ON: MagicNumber
