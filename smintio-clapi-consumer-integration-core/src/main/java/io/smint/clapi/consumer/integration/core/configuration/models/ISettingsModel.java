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

package io.smint.clapi.consumer.integration.core.configuration.models;

import java.net.URL;


/**
 * Define invariant settings for this synchronization library.
 *
 * <p>
 * The settings are vital to drive the synchronization process. Their values usually are manually defined by the
 * Smint.io web platform and need to be stored within some storage system of the runtime environment. This could be a
 * file based (eg: JSON) system or a database system.
 * </p>
 *
 * <h2>Data needed</h2>
 * <p>
 * The settings cover some authorization data that is needed to run <a href="https://en.wikipedia.org/wiki/OAuth">OAuth
 * 2.0</a> (see <a href="https://aaronparecki.com/oauth-2-simplified/">OAuth 2 Simplified</a>) authorization with
 * Smint.io. Additionally the scope of assets is required, defined by the user's group, known as <em>tenant</em> to the
 * Smint.io platform.
 * </p>
 * <ul>
 * <li>tenant (company) - the owner of all assets to sync ({@link #getTenantId()})</li>
 * <li>client application ID - the application authorized to use OAuth ({@link #getOAuthClientId()})</li>
 * <li>client/application secret to use with OAuth 2.0 ({@link #getOAuthClientSecret()})</li>
 * <li>URL that will receive OAuth 2.0 access data ({@link #getOAuthLocalUrlReceivingAccessData()})</li>
 * <li>channel ID for push notification ({@link #getChannelId()})</li>
 * </ul>
 * <p>
 * The local URL to receive OAuth access data during the OAuth authorization process, can be {@code null} in case access
 * data is created outside this library and provided as {@link IAuthTokenModel}. There is an optional companion library
 * available (called {@code smintio-clapi-c-integration-authorizer}) that will help with creating OAuth access data.
 * </p>
 *
 * <h2>Settings are static</h2>
 * <p>
 * Usually these settings are not dynamic, they change not during runtime of the application. Most of the time these
 * settings even do not change over time but are defined up-front. Only in rare cases any change is needed. Eg.: The
 * OAuth access token might need a refresh after its timeout. On Smint.io the timeout of the token is usually set to 1
 * year.
 * </p>
 *
 * <h2>Two kinds of events for initiating an synchronization process</h2>
 * <p>
 * There are two kinds of events initiating a synchronization, as outlined in
 * {@link io.smint.clapi.consumer.integration.core.ISmintIoSynchronization}
 * </p>
 *
 * <h2>Authorization for Smint.io RESTful API</h2>
 * <p>
 * Smint.io RESTful API requires <a href="https://en.wikipedia.org/wiki/OAuth">OAuth 2.0</a> authorization. For a
 * detailed explanation about the steps needed see <a href="https://aaronparecki.com/oauth-2-simplified/">OAuth 2
 * Simplified</a>. Usually some UI need to perform these steps as the flow is heavily based on user interaction.
 * However, in case the library is used on a command line (<em>CLI</em>), it is able to provide such a user interaction
 * by opening a browser and providing the necessary redirect URL for receiving authorization token.<br>
 * In order to refresh access tokens with OAuth, that got invalid because of time-out, the client application ID
 * ({@link #getOAuthClientId()}) and the companion client secret ({@link #getOAuthClientSecret()}) are needed.
 * </p>
 *
 * <p>
 * All OAuth 2.0 access data is stored with {@link IAuthTokenModel}. The application that makes use of this library is
 * responsible for getting these access tokens via OAuth.
 * </p>
 */
public interface ISettingsModel {

    /**
     * The Smint.io tenant ID to use for synchronize all its assets.
     *
     * <p>
     * The tenant is like a use group. By definition of Smint.io all assets bought for a tenant is visible to all users
     * of the same tenant. Hence assets are assigned to the tenant rather than the user. Therefore the synchronization
     * covers all assets of the tenant. The tenant ID defines the sets of assets to synchronize.
     * </p>
     *
     * @return The tenant ID defining the group/sets of assets to sync.
     */
    String getTenantId();


    /**
     * An ID of a channel for receiving notifications from Smint.io platform of newly bought assets to be synced.
     *
     * <p>
     * In order to synchronize newly bought assets immediately, a notification is sent via a
     * <a href="https://pusher.com">Pusher.com</a> <a href="https://pusher.com/docs/channels">channel API</a>. So the ID
     * of this channel is necessary to register a listener and receive notification. The channel ID is assigned and
     * managed by the Smint.io platform and should be visible in the tenant's settings. If not, please ask your Smint.io
     * team to provide such a channel ID.
     * </p>
     *
     * <p>
     * For more information about the two kinds of events initiating a synchronization process, see
     * {@link io.smint.clapi.consumer.integration.core.ISmintIoSynchronization}
     * </p>
     *
     * @return the <a href="https://pusher.com">Pusher.com</a> <a href="https://pusher.com/docs/channels">channel API
     *         ID</a> to use for push notifications of newly bought assets.
     */
    int getChannelId();


    /**
     * The client application ID to use for authorization with Smint.io OAuth.
     *
     * <p>
     * The client application ID is used within the OAuth 2.0 authorization flow.
     * </p>
     *
     * @return the client application ID for OAuth.
     */
    String getOAuthClientId();


    /**
     * The Smint.io OAuth 2.0 secret token for the client application ID.
     *
     * <p>
     * Used to refresh the access token to the Smint.io RESTful API server, along with the client application ID
     * ({@link #getOAuthClientId()}.
     * </p>
     *
     * @return the OAuth 2.0 secret client token to use for authorization and access token refresh.
     */
    String getOAuthClientSecret();


    /**
     * The local URL the Smint.io OAuth service need to redirect to, after successful authorization.
     *
     * <p>
     * The URL need to be accessible from the user's browser, that is performing the manually authorization steps. This
     * URL will receive the required OAuth authorization data, which can be used to retrieve the OAuth access token.
     * Then this access token data need to be stored with
     * {@link io.smint.clapi.consumer.integration.core.configuration.IAuthTokenStorage#storeAuthData(IAuthTokenModel)}
     * </p>
     *
     * @return the local URL to receive OAuth access data during the OAuth authorization process. can be {@code null} in
     *         case access data is created outside this library, eg: if there is an external OAuth authorizer driving
     *         OAuth authorization with Smint.io API.
     */
    URL getOAuthLocalUrlReceivingAccessData();


    /**
     * The languages to use for importing meta data and asset descriptions.
     *
     * <p>
     * Smint.io platform supports indefinite number of languages, only restricted by the languages upstream content
     * providers (eg.: stock image sellers) are using with their assets. Nevertheless not all target downstream
     * synchronization targets support multiple languages. Very often it is not necessary as the target audience of such
     * systems can be precisely defined and thus a single, variable language may be sufficient. Additionally the
     * synchronization target is not necessarily interested in many languages, even if it is supported. So the language
     * to synchronize are defined with configuration settings. Which language are of importance the the target asset
     * management system?
     * </p>
     *
     * @return an array of languages to import.
     */
    String[] getImportLanguages();
}
