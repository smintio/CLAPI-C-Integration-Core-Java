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

package io.smint.clapi.consumer.integration.app.authenticator;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Named;

import io.smint.clapi.consumer.integration.core.authenticator.ISmintIoOAuthAuthorizer;
import io.smint.clapi.consumer.integration.core.configuration.IAuthTokenStorage;
import io.smint.clapi.consumer.integration.core.configuration.models.ISettingsModel;

import fi.iki.elonen.NanoHTTPD;


/**
 * This class creates a web server that will help to fulfill the OAuth authorization process.
 *
 * <p>
 * As soon as the <a href="https://en.wikipedia.org/wiki/OAuth#OAuth_2.0_2">OAuth 2.0</a> authorization process has been
 * started with Smint.io a HTTP service is required to listen to the URL that will receive the OAuth 2.0 token through
 * via system browser. Afterwards the token will be written so the {@link IAuthTokenStorage} and the HTTP service will
 * be terminated.
 * </p>
 *
 * <p>
 * This class will create such a HTTP service listening on all interfaces of the current running machine. It serves the
 * static files from inside this jar with path {@link #WWW_BASE_RESOURCE_PATH} and will hand over all URL parameters on
 * the OAuth redirect URL to the OAuth authorizer (of class{@link ISmintIoOAuthAuthorizer}). In case the function
 * {@link ISmintIoOAuthAuthorizer#analyzeReceivedAuthorizationData(Map)} succeeds, then a redirect to page
 * {@link #WWW_SUCCESS_PAGE} is sent as response. In all error cases, the error message is sent along an HTTP error code
 * of 500.
 * </p>
 *
 * <p>
 * Calling page {@link #WWW_TERMINATE_PAGE} will terminate the web server, so the caller does not need to.
 * </p>
 */
public class AuthenticatorHttpServerCompanion extends NanoHTTPD {

    /**
     * After the received parameters have been passed to the authorizer, a {@link #stop()} is called after some time.
     *
     * <p>
     * After this time of milliseconds have elapsed, a {@link #stop()} signal is sent to the web server to terminate it.
     * It might terminate earlier, because the URL of the terminate page is called (see {@link #WWW_TERMINATE_PAGE}).
     * Nevertheless it is good to make the web service stop even if the browser has been closed.
     * </p>
     *
     * <pre>
     * {@code WAIT_AFTER_AUTHORIZER_IS_CALLED_MILLISECONDS = }{@value #WAIT_AFTER_AUTHORIZER_IS_CALLED_MILLISECONDS}
     * </pre>
     */
    public static final int WAIT_AFTER_AUTHORIZER_IS_CALLED_MILLISECONDS = 10000;


    /**
     * This is the base path within the class path to load files from, when requested by the browser.
     *
     * <p>
     * Files within this resource path can be used to create a good looking response page.
     * </p>
     *
     * <pre>
     * {@code WWW_BASE_RESOURCE_PATH = }{@value #WWW_BASE_RESOURCE_PATH}
     * </pre>
     */
    public static final String WWW_BASE_RESOURCE_PATH = "smint.io-authorizer/www";


    /**
     * The path to a page displaying a success message within {@link #WWW_BASE_RESOURCE_PATH}.
     *
     * <pre>
     * {@code WWW_SUCCESS_PAGE = }{@value #WWW_SUCCESS_PAGE}
     * </pre>
     */
    public static final String WWW_SUCCESS_PAGE = "/success.html";


    /**
     * An URL path that will terminate this HTTP server, which can be used at the end of the success page.
     *
     * <pre>
     * {@code WWW_TERMINATE_PAGE = }{@value #WWW_TERMINATE_PAGE}
     * </pre>
     */
    public static final String WWW_TERMINATE_PAGE = "/terminate";


    private static Logger LOG = Logger.getLogger(AuthenticatorHttpServerCompanion.class.getName());


    private final Consumer<Map<String, String[]>> _authorizer;
    private final String _urlPathOfAuthorize;


    /**
     * Creates a new instance that will use the provided authorizer to drive the authorization.
     *
     * <p>
     * The Web server is not yet started.
     * </p>
     *
     * <pre>
     * final AuthenticatorHttpServerCompanion webServer = new AuthenticatorHttpServerCompanion(settings, tokenStorage);
     * webServer.start(6000, false);
     * </pre>
     *
     * @param settings              the settings to read the port to listen to.
     *                              {@link ISettingsModel#getOAuthLocalUrlReceivingAccessData()} is fetched and its
     *                              {@link URL#} port will be used.
     * @param consumerOfOAuthParams a consumer to receive the URL parameters containing all OAuth data - must not be
     *                              {@code null}. if injected by name, the name is set to
     *                              {@code @Named("authorizer-url-params-consumer")}.
     * @throwns NullPointerException in case any parameter is {@code null}.
     */
    @Inject
    public AuthenticatorHttpServerCompanion(
        final ISettingsModel settings,
        @Named("authorizer-url-params-consumer") final Consumer<Map<String, String[]>> consumerOfOAuthParams
    ) {

        super(settings.getOAuthLocalUrlReceivingAccessData().getPort());

        this._authorizer = consumerOfOAuthParams;
        Objects.requireNonNull(consumerOfOAuthParams, "No OAuth data consumer with Smint.io has been provided");


        String pathOfAuthroizer = settings.getOAuthLocalUrlReceivingAccessData().getPath();
        if (pathOfAuthroizer == null || pathOfAuthroizer.isEmpty()) {
            pathOfAuthroizer = "/";
        }

        this._urlPathOfAuthorize = pathOfAuthroizer;
    }


    @Override
    public Response serve(final IHTTPSession session) {

        LOG.info(() -> "serving URI " + session.getUri());


        if (this._urlPathOfAuthorize.equalsIgnoreCase(session.getUri())) {

            final Map<String, String[]> parms = session.getParameters()
                .entrySet()
                .stream()
                .collect(
                    Collectors.toMap(
                        entry -> entry.getKey(),
                        entry -> entry.getValue().toArray(new String[entry.getValue().size()])
                    )
                );

            Response response = null;
            try {
                this._authorizer.accept(parms);

                response = newFixedLengthResponse(
                    Response.Status.REDIRECT_SEE_OTHER, MIME_PLAINTEXT, null
                );
                response.addHeader("Location", WWW_SUCCESS_PAGE);

            } catch (final RuntimeException excp) {
                response = newFixedLengthResponse(Response.Status.INTERNAL_ERROR, MIME_PLAINTEXT, excp.toString());
            }


            // this service should terminate really soon, now.
            new Thread(() -> {
                try {
                    Thread.sleep(WAIT_AFTER_AUTHORIZER_IS_CALLED_MILLISECONDS);
                } catch (final InterruptedException ignore) {
                    // ignore
                }
                if (this.isAlive()) {
                    this.stop();
                }
            });


            return response;

        } else if (WWW_TERMINATE_PAGE.equalsIgnoreCase(session.getUri())) {

            LOG.info(() -> "terminating web service because of " + session.getUri());
            this.stop();
            return newFixedLengthResponse(Response.Status.OK, MIME_PLAINTEXT, "Terminating ...");
        }


        // serve all other URL paths as files from the resource
        String filePath = session.getUri();
        if (filePath == null || filePath.isEmpty()) {
            filePath = "/index.html";

        } else if (filePath.endsWith("/")) {
            filePath += "index.html";
        }

        // sanity checks
        filePath = filePath
            .replaceAll("\\.\\./", "")
            .replaceAll("\\.\\.", "")
            .replaceAll("\\.(class|java)$", "");


        // detect mime type
        String mimeType = NanoHTTPD.getMimeTypeForFile(filePath);

        // try for unknown mime types
        if (mimeType == null || mimeType.isEmpty() || mimeType.contains("octet-stream")) {
            mimeType = URLConnection.guessContentTypeFromName(filePath);
        }

        final String pathOfFile = filePath;
        LOG.info(() -> "serving static file from resource path " + WWW_BASE_RESOURCE_PATH + pathOfFile);


        // find the file in the resource bundles
        final InputStream in = this.getClass().getClassLoader().getResourceAsStream(WWW_BASE_RESOURCE_PATH + filePath);
        if (in == null) {
            return newFixedLengthResponse(Response.Status.NOT_FOUND, "", "");

        } else {
            return newChunkedResponse(Response.Status.OK, mimeType, in);
        }
    }


    /**
     * Starts up the web server in an extra thread, so this function will only block to check for proper startup.
     *
     * <p>
     * As soon as the web server is running, this function will return. There is no way to access the main thread of the
     * web server directly.
     * </p>
     */
    @Override
    public void start() throws IOException {
        LOG.info("Staring up web server!");
        this.start(SOCKET_READ_TIMEOUT, false);
    }


    @Override
    public void stop() {
        LOG.info("STOPPING web server!");
        super.stop();
    }
}
