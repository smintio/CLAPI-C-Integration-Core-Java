package io.smint.clapi.consumer.integration.core.jobs.impl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;
import java.util.logging.Logger;

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

    private static final Logger LOG = Logger.getLogger(BinaryAssetDownloader.class.getName());


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


        // return already downloaded files
        if (this._targetFile.exists() && this._targetFile.isFile() && this._targetFile.length() > 0) {
            return this._targetFile;
        }


        if (this._httpClient == null) {
            this._httpClient = new OkHttpClient();
        }


        final URL url = this._sourceURL;
        LOG.finer(() -> "Downloading asset binary from URL " + url);

        final IAuthTokenModel authData = this._authTokenStorage != null ? this._authTokenStorage.getAuthData() : null;
        String accessToken = authData != null ? authData.getAccessToken() : null;
        if (url == null || !url.getHost().endsWith(".smint.io")) {
            accessToken = null;
        }

        final String token = accessToken;
        LOG.finer(() -> "Downloading asset binary with access token " + token + " from URL " + url);

        final Request downloadRequest = new Request.Builder()
            .addHeader(
                accessToken == null ? "X-Smint.io-Auth-dummy" : "Authorization",
                accessToken == null ? "dummy" : "Bearer " + this._authTokenStorage.getAuthData().getAccessToken()
            )
            .url(url)
            .build();

        LOG.finer(() -> "Creating HTTP client call to URL " + url);
        final Call call = this._httpClient.newCall(downloadRequest);

        Response resp = null;
        try {
            LOG.finer(() -> "Executing HTTP client call, receiving response from URL " + url);
            resp = call.execute();
        } catch (final IOException ignore) {
            throw new RuntimeException("failed to download binary file!", ignore);
        }


        final Response response = resp;
        LOG.finer(() -> "Receiving response with code " + response.code() + " from URL " + url);

        if (response.code() == HttpURLConnection.HTTP_UNAUTHORIZED
            || response.code() == HttpURLConnection.HTTP_FORBIDDEN) {

            LOG.finer(() -> "Authorization failed for URL " + url);

            response.close();
            throw new RuntimeException(
                "Authorization failed for downloading the binary file from "
                    + url
            );

        } else if (response.isSuccessful() || response.code() == HttpURLConnection.HTTP_OK) {

            LOG.finer(
                () -> "Writing binary data of successful request for URL " + url + " to file "
                    + this._targetFile.getAbsolutePath()
            );

            try (
                final OutputStream out = new BufferedOutputStream(new FileOutputStream(this._targetFile));
                final InputStream in = new BufferedInputStream(response.body().byteStream());

            ) {

                int chr = in.read();
                while (chr >= 0) {
                    out.write(chr);
                    chr = in.read();
                }

                LOG.finer(
                    () -> "Successfully downloaded to target file " + this._targetFile.getAbsolutePath() + " from URL "
                        + url
                );

                return this._targetFile;

            } catch (final IOException excp) {

                throw new RuntimeException(
                    "Failed to write download to the temporary file from URL "
                        + url,
                    excp
                );

            } finally {
                response.close();
            }

        } else {
            response.close();
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
