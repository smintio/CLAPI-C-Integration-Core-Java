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
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

import com.google.gson.Gson;

import org.junit.jupiter.api.DisplayName;

import io.smint.clapi.consumer.integration.core.LocaleUtility;
import io.smint.clapi.consumer.integration.core.configuration.impl.AuthTokenMemoryStorage;
import io.smint.clapi.consumer.integration.core.configuration.models.ISettingsModel;
import io.smint.clapi.consumer.integration.core.factory.impl.SmintIoGsonProvider;


// CHECKSTYLE.OFF: MultipleStringLiterals
// CHECKSTYLE.OFF: MagicNumber

@DisplayName("Test Smint.io API provider: SmintIoApiClientImpl")
public abstract class TestSminIoApiClientBase {


    public Method getPrivateFunction(
        final SmintIoApiClientImpl clientApi,
        final String functionName,
        final Class<?>... parameterTypes
    ) throws Exception {

        Objects.requireNonNull(clientApi, "client api parameter must not be null!");
        final Method func = clientApi.getClass().getDeclaredMethod(
            functionName,
            parameterTypes
        );
        func.setAccessible(true);
        return func;
    }


    public SmintIoApiClientImpl createApiClient(final String[] importLanguages) {

        final ISettingsModel mySettings = this.createSettings(importLanguages);
        return new SmintIoApiClientImpl(
            () -> mySettings,
            new AuthTokenMemoryStorage(),
            (settings, authTokenStorage) -> null,
            null,
            null,
            null,
            null
        );
    }


    public ISettingsModel createSettings(final String[] importLanguages) {
        return new ISettingsModel() {

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

        };
    }


    public String toJson(final Object any) {
        final Gson gson = new SmintIoGsonProvider().get();
        return gson.toJson(any);
    }


    public Object fromJson(final String json, final Class<?> type) {
        final Gson gson = new SmintIoGsonProvider().get();
        return gson.fromJson(json, type);
    }


    public List<Locale> convertImportLanguages(final String[] importLanguages) {
        return Arrays.asList(importLanguages)
            .stream()
            .map((lang) -> LocaleUtility.covertToISO2Locale(new Locale(lang)))
            .collect(Collectors.toList());

    }
}

// CHECKSTYLE.ON: MultipleStringLiterals
// CHECKSTYLE.ON: MagicNumber
