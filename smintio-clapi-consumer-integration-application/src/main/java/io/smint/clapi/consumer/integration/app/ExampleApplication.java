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
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.net.URL;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gson.Gson;

import io.smint.clapi.consumer.integration.app.authenticator.SystemBrowserAuthenticator;
import io.smint.clapi.consumer.integration.app.configuration.impl.AuthTokenFileStorage;
import io.smint.clapi.consumer.integration.app.target.json.SyncTargetDataFactory;
import io.smint.clapi.consumer.integration.app.target.json.SyncTargetJson;
import io.smint.clapi.consumer.integration.core.ISmintIoSynchronization;
import io.smint.clapi.consumer.integration.core.SmintIoSynchronization;
import io.smint.clapi.consumer.integration.core.authenticator.impl.SmintIoOAuthAuthorizer;
import io.smint.clapi.consumer.integration.core.configuration.IAuthTokenStorage;
import io.smint.clapi.consumer.integration.core.configuration.models.IAuthTokenModel;
import io.smint.clapi.consumer.integration.core.configuration.models.ISettingsModel;
import io.smint.clapi.consumer.integration.core.configuration.models.impl.AuthTokenJsonConverter;
import io.smint.clapi.consumer.integration.core.factory.impl.DefaultSyncTargetFactory;
import io.smint.clapi.consumer.integration.core.factory.impl.SmintIoGsonProvider;


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


    private static final Logger LOG = Logger.getLogger(ExampleApplication.class.getName());


    private Map<String, Object> _localAppSettingsData;
    private Map<String, Object> _appSettingsData;

    public static void main(final String[] args) throws Exception {

        final Logger root = Logger.getGlobal();
        root.setLevel(Level.ALL);
        for (final Handler handler : root.getHandlers()) {
            handler.setLevel(Level.ALL);
        }


        LOG.finer("Starting application");
        new ExampleApplication().run();
    }


    public void run() throws Exception {

        LOG.finer("Reading configuration data.");
        final ISettingsModel settings = this.loadSettingsFromConfig();

        final IAuthTokenStorage tokenStorage = new AuthTokenFileStorage(
            new AuthTokenJsonConverter(new SmintIoGsonProvider().get()),
            new File("./auth-token.json")
        );

        // check if auth tokens are available. If not, create some
        final IAuthTokenModel authTokens = tokenStorage.getAuthData();
        if (authTokens == null || !authTokens.isSuccess()) {
            LOG.finer("Creating new OAuth token data.");

            final SystemBrowserAuthenticator browserAuthenticator = new SystemBrowserAuthenticator(
                new SmintIoOAuthAuthorizer(
                    () -> settings,
                    tokenStorage
                )
            );

            browserAuthenticator.refreshSmintIoToken(settings, tokenStorage);
        }

        LOG.finer("Creating new sync Job.");

        final File assetsDir = new File(".", "downloaded-assets");
        final SyncTargetJson syncTarget = new SyncTargetJson(assetsDir);
        final ISmintIoSynchronization smintIoSync = new SmintIoSynchronization(
            new DefaultSyncTargetFactory()
                .setAuthTokenStorage(tokenStorage)
                .setSettingsProvider(() -> settings)
                .setSyncTargetProvider(() -> syncTarget)
                .setDataFactory(new SyncTargetDataFactory())
        );

        final int[] syncCounter = new int[1];
        syncCounter[0] = 0;

        // set a callback to be executed after all asset sync
        syncTarget.setAfterSyncCallback((i) -> {

            syncCounter[0]++;
            if (syncCounter[0] < 2) {
                return;
            }

            // stop schedule
            smintIoSync.stop();

            // now print out the collected JSON
            final Gson gson = this.createGson();

            try (final FileWriter out = new FileWriter(new File(".", "out-result.json"))) {
                out.append(gson.toJson(syncTarget.getAllData()));
            } catch (final IOException ignore) {
                ignore.printStackTrace();
            }
            System.out.println("DONE DONE: " + new File(".", "out-result.json").getAbsolutePath());
            System.out.println("asset downloaded to: " + assetsDir.getAbsolutePath());
        });


        // now run everything
        smintIoSync.start();
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


        final String redirectUri = localAuth.get("RedirectUri") != null ? localAuth.get("RedirectUri")
            : auth.get("RedirectUri");
        final URL oAuthUrl = redirectUri != null && !redirectUri.isEmpty() ? new URL(redirectUri) : null;

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
            public String getOAuthClientId() {
                return clientId;
            }

            @Override
            public String getOAuthClientSecret() {
                return clientSecret;
            }

            @Override
            public String[] getImportLanguages() {
                return importLanguages != null ? importLanguages : new String[] { "en", "de" };
            }

            @Override
            public URL getOAuthLocalUrlReceivingAccessData() {
                return oAuthUrl;
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
