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

package io.smint.clapi.consumer.integration.core.authenticator;

import java.net.URL;
import java.util.Map;

import io.smint.clapi.consumer.integration.core.configuration.models.ISettingsModel;
import io.smint.clapi.consumer.integration.core.exceptions.SmintIoAuthenticatorException;


/**
 * Authenticator to create OAuth authorization token with Smint.io API.
 *
 * <p>
 * A generic authenticator to authorize access to the Smint.io API, which uses OAuth 2.0. Smint.io API uses
 * <a href="https://identityserver.io/">Identity Server</a> as server part to provide OAuth 2.0 authorization scheme.
 * </p>
 *
 * <p>
 * Smint.io provides an OpenID auto-detection configuration URL for each tenant installation, which can be used to read
 * the OAuth 2.0 end points. This URL can be used to detect the end points and configure the authorizer.
 * </p>
 *
 * <pre>
 *     https://{tenantID}.smint.io/.well-known/openid-configuration
 *     https://demo.smint.io/.well-known/openid-configuration
 * </pre>
 *
 * <p>
 * OAuth 2.0 authorization involves user interaction. So, only the initial call to the Smint.io API can be made by the
 * authorizer, to request authorization. The OAuth service then will provide an URL the user must open in the browser in
 * order to authorize this library manually. Then the server will redirect to a local URL, where a listener receives
 * authorization data as URL parameters, which need to be passed to the function
 * {@link #analyzeReceivedAuthorizationData(Map)}. The data will then be evaluated and a request token fetched from the
 * Smint.io API to finish OAuth authorization cycle.
 * </p>
 *
 * <p>
 * The local URL the OAuth service need to redirect to - in order to pass the OAuth authorization data - is retrieved
 * from {@link ISettingsModel#getOAuthLocalUrlReceivingAccessData()}
 * </p>
 *
 * <p>
 * A listener on an address reachable by the user's browser, need to be in place. This listener must read all URL data
 * and pass these to {@link #analyzeReceivedAuthorizationData(Map)}. Utilizing a servlet, the parameters are available
 * calling <a href="https://javaee.github.io/javaee-spec/javadocs/javax/servlet/ServletRequest.html#getParameterMap--"
 * >{@code javax.servlet.http.HttpServletRequest#getParameterMap()}</a>. Hence such a listener might look like this,
 * fetching the Smint.io instances from global scope, somehow.
 * </p>
 *
 * <pre>
 * package test;
 *
 * import java.io.IOException;
 *
 * import javax.servlet.ServletException;
 * import javax.servlet.http.HttpServlet;
 * import javax.servlet.http.HttpServletRequest;
 * import javax.servlet.http.HttpServletResponse;
 *
 * import test.Global;
 *
 *
 * public class ReceiveOAuthDataFromServiceViaBrowser extends HttpServlet {
 *
 *     protected void doGet(HttpServletRequest request, HttpServletResponse response)
 *         throws ServletException, IOException {
 *
 *         final Map&lt;String, String[]&gt; authParameters = request.getParameterMap();
 *
 *         boolean isAuthDataConsumed = false;
 *         for (final ISmintIoOAuthAuthorizer authorizer : Global.getSmintIoAuthorizers()) {
 *             if (authorizer.isTargetOfReceivedAuthorizationData(authParameters)) {
 *                 isAuthDataConsumed = true;
 *                 authorizer.analyzeAuthorizationData(authParameters);
 *                 break;
 *             }
 *         }
 *
 *         if (!isAuthDataConsumed) {
 *             throw new SmintIoAuthenticatorException("Invalid URL parameters - no authenticator target found!");
 *         }
 *     }
 *
 * }
 * </pre>
 *
 * <p>
 * Beware, that the same instance of {@code ISmintIoOAuthAuthorizer} must be used to analyze the received URL
 * parameters, as the instance needs to cached some data, that is necessary to fulfill the authorization process.
 * Therefore, the same instance need to be passed somehow to the servlet, receiving the URL data.
 * </p>
 */
public interface ISmintIoOAuthAuthorizer extends ISmintIoAuthenticator {

    /**
     * The scope to use with OAuth to authorize for synchronization with Smint.io API.
     *
     * <pre>
     * {@code SMINTIO_OAUTH_SCOPE = }{@value #SMINTIO_OAUTH_SCOPE}
     * </pre>
     */
    String SMINTIO_OAUTH_SCOPE = "smintio.full openid profile offline_access";


    /**
     * Connects to the Smint.io API, requests authorization and returns the URL, the user must open in the browser.
     *
     * <p>
     * Implementing classes will need an instance to {@link ISettingsModel} to get all required data for OAuth
     * authorization.
     * </p>
     *
     * <p>
     * The returned URL is used to manually authorize this application with the Smint.io API by the user. It must be
     * opened in a user's browser.
     * </p>
     *
     * @return A valid URL to display to the user, which need to open it in the browser.
     * @throws SmintIoAuthenticatorException in case no client ID or client secret are available, invalid, have expired
     *                                       or the Smint.io API rejected the authorization request, or network is down
     *                                       and the Smint.io API service can not be reached.
     */
    URL createAuthorizationUrl() throws SmintIoAuthenticatorException;


    /**
     * Received and evaluates the authorization data sent from the server to a redirect URL.
     *
     * <p>
     * OAuth 2.0 authorization involves user interaction. So, only the initial call to the Smint.io API can be made by
     * the authorizer, to request authorization. The OAuth service then will provide an URL the user must open in the
     * browser in order to authorize this library manually. Then the server will redirect to a local URL, where a
     * listener receives authorization data, that need to be passed to this function. The data will then be evaluated
     * and a request token fetched from the Smint.io API.
     * </p>
     *
     * <p>
     * The URL any OAuth service will redirect to - after successful authorization - contains required URL parameters.
     * These parameters need to be passed to this function.
     * </p>
     *
     * @param urlParameters the parameters received by the redirection URL listener. Using servlets, the parameters can
     *                      be easily fetched by using <a href=
     *                      "https://javaee.github.io/javaee-spec/javadocs/javax/servlet/http/HttpServletRequest.html"
     *                      >{@code javax.servlet.http.HttpServletRequest#getParameterMap()}</a>.
     * @return A valid URL to display to the user, which need to open it in the browser.
     * @throws SmintIoAuthenticatorException in case no client ID or client secret are available, invalid, have expired
     *                                       or the Smint.io API rejected the authorization request, or network is down
     *                                       and the Smint.io API service can not be reached.
     */
    ISmintIoOAuthAuthorizer analyzeReceivedAuthorizationData(
        final Map<String, String[]> urlParameters
    ) throws SmintIoAuthenticatorException;


    /**
     * Checks the authorization data sent from the server if these are targeted to this instance.
     *
     * <p>
     * OAuth 2.0 authorization involves user interaction. So, only the initial call to the Smint.io API can be made by
     * the authorizer, to request authorization. The OAuth service then will provide an URL the user must open in the
     * browser in order to authorize this library manually. Then the server will redirect to a local URL, where a
     * listener receives authorization data, that need to be passed to this function. The data will then be evaluated
     * and a request token fetched from the Smint.io API.
     * </p>
     *
     * <p>
     * The URL any OAuth service will redirect to - after successful authorization - contains required URL parameters.
     * As there might be some more authorizations are currently in progress, this check can be used, whether the
     * received URL parameters are targeted at this instance. Each OAuth authorization progress to Smint.io relies on
     * instances of this class. So multiple instances might be used in global scope to be passed to the same servlet
     * receiving the authorization data. The servlet must use some lookup method to check, which instance is waiting for
     * the authorization data. In case no instance is the target for such data, the servlet must fail with an
     * authorization exception.
     * </p>
     *
     * @param urlParameters the parameters received by the redirection URL listener. Using servlets, the parameters can
     *                      be easily fetched by using <a href=
     *                      "https://javaee.github.io/javaee-spec/javadocs/javax/servlet/http/HttpServletRequest.html"
     *                      >{@code javax.servlet.http.HttpServletRequest#getParameterMap()}</a>.
     * @return A valid URL to display to the user, which need to open it in the browser.
     * @throws SmintIoAuthenticatorException in case no client ID or client secret are available, invalid, have expired
     *                                       or the Smint.io API rejected the authorization request, or network is down
     *                                       and the Smint.io API service can not be reached.
     */
    boolean isTargetOfReceivedAuthorizationData(final Map<String, String[]> urlParameters);
}
