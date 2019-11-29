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

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;

import io.smint.clapi.consumer.integration.core.authenticator.ISmintIoAuthenticator;
import io.smint.clapi.consumer.integration.core.authenticator.ISmintIoOAuthAuthorizer;
import io.smint.clapi.consumer.integration.core.configuration.IAuthTokenStorage;
import io.smint.clapi.consumer.integration.core.configuration.models.ISettingsModel;
import io.smint.clapi.consumer.integration.core.exceptions.SmintIoAuthenticatorException;
import io.smint.clapi.consumer.integration.core.exceptions.SmintIoAuthenticatorException.AuthenticatorError;


/**
 * Authenticator to initially create an OAuth 2.0 authorization token with Smint.io API.
 *
 * <p>
 * This authenticator opens the browser and starts the <a href="https://en.wikipedia.org/wiki/OAuth#OAuth_2.0_2">OAuth
 * 2.0</a> authorization with Smint.io. At the end of it, Smint.io API redirects to a localhost url (). This
 * authenticator creates a HTTP service to listen to that URL and will receive the OAuth 2.0 token through this URL.
 * Afterwards the token will be written so the {@link IAuthTokenStorage} and the HTTP service will be terminated.
 * </p>
 *
 */
public class SystemBrowserAuthenticator implements ISmintIoAuthenticator {

    private final ISmintIoOAuthAuthorizer _authorizer;

    /**
     * The OAuth 2.0 (OpenID) URL to the Smint.io authority, which has to be called for initiating OAuth authorization.
     *
     * <p>
     * The placeholder <code>{0}</code> will be replaced with the tenant ID, fetched from
     * {@link ISettingsModel#getTenantId()}.
     * </p>
     *
     * <pre>
     * {@code SMINTIO_OPEN_IO_AUTHORITY_URL =}{@value #SMINTIO_OPEN_IO_AUTHORITY_URL}
     * </pre>
     */
    public static final String SMINTIO_OPEN_IO_AUTHORITY_URL = "https://{0}.smint.io/.well-known/openid-configuration";


    private static final Logger LOG = Logger.getLogger(SystemBrowserAuthenticator.class.getName());
    static {
        Logger.getGlobal().setLevel(Level.ALL);
    }


    /**
     * Creates a new instance that will use the provided authorizer to drive the OAuth authorization process.
     *
     * @param authorizer a valid authorizer - must not be {@code null}.
     */
    @Inject
    public SystemBrowserAuthenticator(final ISmintIoOAuthAuthorizer authorizer) {
        this._authorizer = authorizer;

        Objects.requireNonNull(authorizer, "No OAuth authorizer with Amint.io has been provided");
    }


    @Override
    public SystemBrowserAuthenticator refreshSmintIoToken(
        final ISettingsModel settings, final IAuthTokenStorage authTokenStorage
    ) throws SmintIoAuthenticatorException {

        Objects.requireNonNull(settings, "no settings have been provided!");
        Objects.requireNonNull(authTokenStorage, "auth token storage has been provided!");

        // us the authorizers refresh capability, if token data is already available
        try {
            LOG.finer("Refreshing existing OAuth authentication data.");
            this._authorizer.refreshSmintIoToken(settings, authTokenStorage);
            return this;

        } catch (final SmintIoAuthenticatorException | RuntimeException ignore) {
            // ignore, as full OAuth process will be tried instead.
        }


        LOG.finer("Creating new OAuth authentication data.");
        this.validateForAuthenticator(settings);

        // create and start web server
        final Exception[] authorizerFailedException = new Exception[1];
        final Object semaphore = new Object();
        final AuthenticatorHttpServerCompanion webServer = new AuthenticatorHttpServerCompanion(
            settings,
            (urlParams) -> {
                try {
                    this._authorizer.analyzeReceivedAuthorizationData(urlParams);
                } catch (final SmintIoAuthenticatorException | RuntimeException excp) {
                    authorizerFailedException[0] = excp;
                } finally {
                    synchronized (semaphore) {
                        semaphore.notifyAll();
                    }
                }
            }
        );

        try {
            webServer.start();

        } catch (final IOException excp) {

            throw new SmintIoAuthenticatorException(
                AuthenticatorError.CannotAcquireSmintIoToken,
                "Internal web Server to receive OAuth data failed to start!",
                excp
            );
        }


        try {
            final URL url = this._authorizer.createAuthorizationUrl();

            LOG.finer(() -> "opening system browser to URL: " + url);
            this.openSystemBrowser(url);

        } catch (IOException | URISyntaxException excp) {
            throw new SmintIoAuthenticatorException(
                AuthenticatorError.CannotAcquireSmintIoToken,
                "URL to open in browser can not be created!",
                excp
            );
        }


        // wait for web server to terminate
        try {
            synchronized (semaphore) {
                semaphore.wait(120000);
            }
        } catch (final InterruptedException ignore) {
            LOG.log(Level.WARNING, "waiting for web server to terminate has been interrupted!", ignore);
        }

        try {
            // give another 2 seconds to serve the success page
            Thread.sleep(2000);
        } catch (final InterruptedException ignore) {
            LOG.log(Level.WARNING, "waiting for web server to serve success page has been interrupted!", ignore);
        }


        if (webServer.isAlive()) {
            LOG.finer("Terminating companion web server.");
            webServer.stop();
        }

        if (authorizerFailedException[0] != null) {
            throw new SmintIoAuthenticatorException(
                AuthenticatorError.CannotAcquireSmintIoToken,
                "Failed to acquire Smint.io OAuth tokens.",
                authorizerFailedException[0]
            );
        }

        if (authTokenStorage.getAuthData() == null || !authTokenStorage.getAuthData().isSuccess()) {
            throw new SmintIoAuthenticatorException(
                AuthenticatorError.CannotAcquireSmintIoToken,
                "Failed to acquire Smint.io OAuth tokens - is still <null> or unseccssful."
            );
        }

        return this;
    }


    /**
     * Opens the system browser on the machine running this code and makes the browser open the URL.
     *
     * <p>
     * The browser is opened in a separate process.
     * </p>
     *
     * <p>
     * On every operating system supported, there are helper commands available to actually find and select the system
     * browser. These helper tools will open the browser and pass the URL to it. There might be more operating systems
     * with such helpers, that are still unsupported. In that case, send an email to the developer of this code. The
     * currently supported platforms are:
     * </p>
     * <ul>
     * <li>Windows - helper {@code start}</li>
     * <li>MacOS - helper {@code open}</li>
     * <li>Linux with a Desktop - helper {@code xdg-open}</li>
     * </ul>
     *
     * @param url the browser will open this URL initially. if {@code null}, no browser will be opened.
     * @return {@code this}
     * @throws IOException        see {@link Runtime#exec(String)}
     * @throws URISyntaxException if conversion to {@link URI} failed with {@link URL#toURI()}.
     */
    public SystemBrowserAuthenticator openSystemBrowser(final URL url) throws IOException, URISyntaxException {

        if (url == null) {
            return this;
        }

        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            Desktop.getDesktop().browse(url.toURI());
            return this;
        }


        // use plain runtime to start the browser somehow
        final Runtime rt = Runtime.getRuntime();

        final String os = System.getProperty("os.name").toLowerCase();
        if (os.indexOf("win") >= 0) {
            // rt.exec("rundll32 url.dll,FileProtocolHandler \"" + url.toExternalForm() + "\"");
            rt.exec("start \"" + url.toExternalForm() + "\"");

        } else if (os.indexOf("mac") >= 0) {
            rt.exec("open '" + url.toExternalForm() + "'");

        } else if (os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0) {
            rt.exec("xdg-open '" + url.toExternalForm() + "'");
        }

        return this;
    }


    private void validateForAuthenticator(final ISettingsModel settings) throws SmintIoAuthenticatorException {
        if (settings.getTenantId() == null || settings.getTenantId().isEmpty()) {
            throw new SmintIoAuthenticatorException(
                AuthenticatorError.SmintIoIntegrationWrongState, "The tenant ID is missing"
            );
        }

        if (settings.getOAuthClientId() == null || settings.getOAuthClientId().isEmpty()) {
            throw new SmintIoAuthenticatorException(
                AuthenticatorError.SmintIoIntegrationWrongState,
                "The client ID is missing"
            );
        }


        if (settings.getOAuthClientSecret() == null || settings.getOAuthClientSecret().isEmpty()) {
            throw new SmintIoAuthenticatorException(
                AuthenticatorError.SmintIoIntegrationWrongState,
                "The client secret is missing"
            );
        }


        if (settings.getOAuthLocalUrlReceivingAccessData() == null) {
            throw new SmintIoAuthenticatorException(
                AuthenticatorError.SmintIoIntegrationWrongState,
                "The oAuth target redirect URI is missing"
            );
        }
    }
}
