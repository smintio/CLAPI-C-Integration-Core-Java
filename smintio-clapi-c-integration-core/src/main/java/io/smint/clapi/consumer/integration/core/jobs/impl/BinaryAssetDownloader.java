package io.smint.clapi.consumer.integration.core.jobs.impl;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;

import javax.inject.Provider;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import io.smint.clapi.consumer.integration.core.configuration.IAuthTokenStorage;
import io.smint.clapi.consumer.integration.core.configuration.models.IAuthTokenModel;


/**
 * Downloads a file from Smint.io API, stores it inside a temporary file and provides this file.
 *
 * <p>
 * The download is performed via HTTPs.
 * </p>
 */
public class BinaryAssetDownloader implements Provider<File> {

    private OkHttpClient _httpClient;
    private final File _targetFile;
    private final URL _sourceURL;
    private final IAuthTokenStorage _authTokenStorage;

    public BinaryAssetDownloader(
        final IAuthTokenStorage authTokenStorage,
        final OkHttpClient httpClient,
        final URL sourceURL,
        final File targetFile
    ) {
        this._authTokenStorage = authTokenStorage;

        Objects.requireNonNull(this._authTokenStorage, "No Smint.io authentication storage has been provided!");
        Objects.requireNonNull(
            this._authTokenStorage.getAuthData(),
            "No Smint.io authentication data available from authentication storage!"
        );

        this._httpClient = httpClient;
        this._sourceURL = sourceURL;
        this._targetFile = targetFile;
    }


    @Override
    public File get() {

        if (this._sourceURL == null || this._targetFile == null) {
            return null;
        }

        if (this._httpClient == null) {
            this._httpClient = new OkHttpClient();
        }


        final IAuthTokenModel authData = this._authTokenStorage != null ? this._authTokenStorage.getAuthData() : null;
        final String accessToken = authData != null ? authData.getAccessToken() : null;
        Objects.requireNonNull(
            authData,
            "No Smint.io authentication data available from authentication storage!"
        );


        final URL url = this._sourceURL;
        final Request downloadRequest = new Request.Builder()
            .addHeader(
                accessToken == null ? "X-Smint.io-Auth-dummy" : "Authorization",
                accessToken == null ? "dummy" : "Bearer " + this._authTokenStorage.getAuthData().getAccessToken()
            )
            .url(url)
            .build();

        final Call call = this._httpClient.newCall(downloadRequest);
        Response response = null;
        try {
            response = call.execute();
        } catch (final IOException ignore) {
            throw new RuntimeException("failed to download binary file!", ignore);
        }


        if (response.code() == HttpURLConnection.HTTP_UNAUTHORIZED
            || response.code() == HttpURLConnection.HTTP_FORBIDDEN) {

            throw new RuntimeException(
                "Authorization failed for downloading the binary file from "
                    + url
            );

        } else if (response.isSuccessful() || response.code() == HttpURLConnection.HTTP_OK) {

            try (
                final OutputStream out = new BufferedOutputStream(new FileOutputStream(this._targetFile));
                final InputStream in = response.body().byteStream();) {

                final int chr = in.read();
                while (chr >= 0) {
                    out.write(chr);
                }

                return this._targetFile;

            } catch (final IOException excp) {

                throw new RuntimeException(
                    "Failed to write download to the temporary file from URL "
                        + url
                );

            }

        } else {
            throw new RuntimeException("failed to download binary file from " + url);
        }
    }


    public OkHttpClient getHttpClient() {
        return this._httpClient;
    }

    public BinaryAssetDownloader setHttpClient(final OkHttpClient httpClient) {
        this._httpClient = httpClient;
        return this;
    }
}
