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

package io.smint.clapi.consumer.integration.app;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

import io.smint.clapi.consumer.integration.app.configuration.impl.AuthTokenFileStorage;
import io.smint.clapi.consumer.integration.app.target.json.SyncTargetJson;
import io.smint.clapi.consumer.integration.core.ISmintIoSynchronization;
import io.smint.clapi.consumer.integration.core.SmintIoSynchronization;
import io.smint.clapi.consumer.integration.core.configuration.models.ISettingsModel;
import io.smint.clapi.consumer.integration.core.configuration.models.impl.AuthTokenJsonConverter;
import io.smint.clapi.consumer.integration.core.factory.impl.SmintIoGsonProvider;
import io.smint.clapi.consumer.integration.core.factory.impl.SyncTargetFactoryFromDI;


/**
 * Example Java console application to fetch data from the Smint.io API for "demo".
 *
 * <p>
 * All data synchronized from "demo" is being stored inside a {@link Map}, which is converted to JSON at the end. The
 * JSON is then written to the console.
 * </p>
 *
 * <p>
 * Necessary settings and authorization tokens are loaded and stored to the file {@code appsettings.local.json}. The
 * format is much the same as that one of the C# library. Hence these can be used interchangeably. The file must consist
 * of a map, where the only value to be read has key {@code "SmintIo"}. All other values are ignored.<br>
 * An example file content would be:
 * </p>
 *
 * <pre>
 * {
 *   "SmintIo": {
 *     "App": {
 *       "TenantId": "test",
 *       "ChannelId": 3982
 *     },
 *     "Auth": {
 *       "ClientId": "test",
 *       "ClientSecret": "324poi3lknv234lkj324834"
 *     }
 *   },
 * }
 * </pre>
 */
public class ExampleApplication {

    /**
     * The file name to load settings and authorization data from and store to.
     *
     * <pre>
     *     {@code FILE_NAME_APPSETTINGS_LOCAL = }{@value #FILE_NAME_APPSETTINGS_LOCAL}
     * </pre>
     */
    public static final String FILE_NAME_APPSETTINGS_LOCAL = "appsettings.local.json";

    /**
     * The file name to load default settings and authorization data from.
     *
     * <p>
     * This file is not being written.
     * </p>
     *
     * <pre>
     *     {@code FILE_NAME_APPSETTINGS = }{@value #FILE_NAME_APPSETTINGS}
     * </pre>
     */
    public static final String FILE_NAME_APPSETTINGS = "appsettings.json";


    private Map<String, Object> _localAppSettingsData;
    private Map<String, Object> _appSettingsData;

    public static void main(final String[] args) throws Exception {
        new ExampleApplication().run();
    }


    public void run() throws Exception {

        final SyncTargetJson syncTarget = new SyncTargetJson();
        final ISmintIoSynchronization smintIoSync = new SmintIoSynchronization(
            new SyncTargetFactoryFromDI(
                new AuthTokenFileStorage(
                    new AuthTokenJsonConverter(new Gson()),
                    new File("./auth-token.json")
                ),
                this.loadSettingsFromConfig(),
                () -> syncTarget
            )
        );


        smintIoSync.initialSync(true);

        // sync a second time to detect errors to updated assets
        smintIoSync.initialSync(true);


        // now print out the collected JSON
        final Gson gson = this.createGson();
        System.out.println(gson.toJson(syncTarget.getAllData()));
    }


    /**
     * Load the settings from the JSON file, that can be used with the C# version, too.
     *
     * @param jsonFile
     * @return
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    private ISettingsModel loadSettingsFromConfig() throws IOException {

        if (this._appSettingsData == null) {
            this._appSettingsData = this.loadAppSettingsFile("./" + FILE_NAME_APPSETTINGS);
            this.initAppDataSettings(this._appSettingsData);
        }

        if (this._localAppSettingsData == null) {
            this._localAppSettingsData = this.loadAppSettingsFile("./" + FILE_NAME_APPSETTINGS_LOCAL);
            this.initAppDataSettings(this._localAppSettingsData);
        }

        final Map<String, Object> smintIoData = (Map<String, Object>) this._appSettingsData.get("SmintIo");
        final Map<String, Object> settings = (Map<String, Object>) smintIoData.get("App");
        final Map<String, String> auth = (Map<String, String>) smintIoData.get("Auth");

        final Map<String, Object> localSmintIoData = (Map<String, Object>) this._localAppSettingsData.get("SmintIo");
        final Map<String, Object> localSettings = (Map<String, Object>) localSmintIoData.get("App");
        final Map<String, String> localAuth = (Map<String, String>) localSmintIoData.get("Auth");


        final String tenantId = (String) (localSettings.get("TenantId") != null ? localSettings.get("TenantId")
            : settings.get("TenantId"));
        final Double channelIdValue = (Double) (localSettings.get("ChannelId") != null ? localSettings.get("ChannelId")
            : settings.get("ChannelId"));
        final int channelId = channelIdValue != null ? Math.round(channelIdValue.floatValue()) : -1;

        final String clientId = localAuth.get("ClientId") != null ? localAuth.get("ClientId") : auth.get("ClientId");
        final String clientSecret = localAuth.get("ClientSecret") != null ? localAuth.get("ClientSecret")
            : auth.get("ClientSecret");
        final String[] importLanguages = ((List<String>) (localSettings.get("ImportLanguages") != null
            ? localSettings.get("ImportLanguages")
            : settings.get("ImportLanguages"))).toArray(new String[0]);


        return new ISettingsModel() {

            @Override
            public String getTenantId() {
                return tenantId;
            }

            @Override
            public int getChannelId() {
                return channelId;
            }

            @Override
            public String getClientId() {
                return clientId;
            }

            @Override
            public String getClientSecret() {
                return clientSecret;
            }

            @Override
            public String[] getImportLanguages() {
                return importLanguages != null ? importLanguages : new String[] { "en", "de" };
            }
        };

    }


    @SuppressWarnings("unchecked")
    private Map<String, Object> loadAppSettingsFile(final String fileName) throws IOException {
        final Gson gson = this.createGson();

        System.out.println("File: " + new File(fileName).getAbsolutePath());
        try (final Reader jsonIn = new FileReader(new File(fileName))) {
            return gson.fromJson(jsonIn, Hashtable.class);
        }
    }

    private Gson createGson() {
        return new SmintIoGsonProvider().get();
    }


    private void initAppDataSettings(final Map<String, Object> appDataSettings) {

        if (appDataSettings != null) {

            if (!appDataSettings.containsKey("SmintIo")) {
                appDataSettings.put("SmintIo", new HashMap<String, Object>());
            }

            appDataSettings.get("SmintIo");
            if (!appDataSettings.containsKey("App")) {
                appDataSettings.put("App", new HashMap<String, Object>());
            }
            if (!appDataSettings.containsKey("Auth")) {
                appDataSettings.put("Auth", new HashMap<String, Object>());
            }
        }
    }
}
