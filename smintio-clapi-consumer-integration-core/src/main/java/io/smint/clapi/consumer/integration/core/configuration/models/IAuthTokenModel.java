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

import java.time.OffsetDateTime;


/**
 * Data required by OAuth to access the Smint.io RESTful API.
 *
 * <h2>Authorization for Smint.io RESTful API</h2>
 * <p>
 * Smint.io RESTful API requires <a href="https://en.wikipedia.org/wiki/OAuth">OAuth 2.0</a> authorization. For a
 * detailed explanation about the steps needed see <a href="https://aaronparecki.com/oauth-2-simplified/">OAuth 2
 * Simplified</a>. Usually some UI need to perform these steps as the flow is heavily based on user interaction.<br>
 * In order to refresh access tokens with OAuth, that got invalid because of time-out, the client application ID
 * ({@link ISettingsModel#getOAuthClientId()}) and the companion client secret
 * ({@link ISettingsModel#getOAuthClientSecret()}) are needed.
 * </p>
 *
 * <p>
 * The implementing application making use of this library is responsible for driving the OAuth authorization work flow
 * and acquire the necessary tokens. These tokens are required to be made available by providing an instance
 * implementing this interface.
 * </p>
 */
public interface IAuthTokenModel {

    /**
     * Indicates whether fetching OAuth access token was successful.
     *
     * <p>
     * In case {@code false} is returned, no other check is performed and the synchronization process stops right at the
     * start with an exception. Synchronizing with Smint.io requires a valid access token!<br>
     * This function will help to increase performance as no token action is performed in case the token has not been
     * retrieved successfully before hand.
     * </p>
     *
     * <p>
     * No check is performed, whether the {@link #getAccessToken()} is still valid. The server will tell anyway and a
     * refresh is tried with {@link #getRefreshToken()}. If that fails, too, an exception will abort the synchronization
     * process.
     * </p>
     *
     * @return {@code true} for valid access tokens, of {@code false} in case acquiring an access token via OAuth has
     *         not been finished successfully (eg: aborted, failed).
     */
    boolean isSuccess();


    /**
     * Returns the access token as returned by Smint.io OAuth authorization token server.
     *
     * <p>
     * After authorization with the Smint.io OAuth server, an access token is issued and returned with the server
     * response. This token is the main indicator for authorization - it is like a combination of user name and
     * password. All the authorization is based on this token. Hence it must be provided here.
     * </p>
     *
     * <p>
     * In case the access token has expired, a refresh is tried utilizing the {@code #getRefreshToken()}. If that is
     * successful, then the new token is being stored for next use.
     *
     * </p>
     *
     * @return the identity token {@code id_token} that is part of the
     *         <a href="https://openid.net/developers/specs/">OpenID specification</a> as sent by the Smint.io server.
     *         This token may be sent by the the Smint.io token server but not necessarily. If omitted by the server
     *         this function returns just {@code null}.
     */
    String getAccessToken();


    /**
     * Returns the refresh token as (optionally) returned by Smint.io OAuth authorization server.
     *
     * <p>
     * The Smint.io OAuth server supports <a href="https://openid.net/developers/specs/">OpenID</a> as well. The token
     * ID is part of this specification. At the moment it is not required to authorize but in case the Smint.io server
     * returns such a value, it is required to provide it here.
     * </p>
     *
     * @return the identity token {@code id_token} that is part of the
     *         <a href="https://openid.net/developers/specs/">OpenID specification</a> as sent by the Smint.io server.
     *         This token may be sent by the the Smint.io token server but not necessarily. If omitted by the server
     *         this function returns just {@code null}.
     */
    String getRefreshToken();


    /**
     * returns the identity token as (optionally) returned by Smint.io OAuth authorization server.
     *
     * <p>
     * The Smint.io OAuth server supports <a href="https://openid.net/developers/specs/">OpenID</a> as well. The token
     * ID is part of this specification. At the moment it is not required to authorize but in case the Smint.io server
     * returns such a value, it is required to provide it here.
     * </p>
     *
     * @return the identity token {@code id_token} that is part of the
     *         <a href="https://openid.net/developers/specs/">OpenID specification</a> as sent by the Smint.io server.
     *         This token may be sent by the the Smint.io token server but not necessarily. If omitted by the server
     *         this function returns just {@code null}.
     */
    String getIdentityToken();


    /**
     * The expiration time of the access token.
     *
     * <p>
     * Every access token has an expiration time. Usually it is just a couple of hours or days. After the expiration
     * time the token must be refreshed. The expiration time is transmitted along the access token with the token-server
     * answer.
     * </p>
     *
     * <p>
     * The expiration time is used to avoid initial access with an expired token as it would definitely result in an
     * authorization error. However, even if the access token has not yet expired, the server still may regard the token
     * as invalid and may require a refresh of the token.
     * </p>
     *
     * @return the expiration time of the access token. If {@code null} then the access token does not expire.
     */
    OffsetDateTime getExpiration();


    /**
     * Checks whether this token data has expired.
     *
     * <p>
     * The token data has expired if there is a expiration date returned by {@link #getExpiration()} and the date lies
     * before {@link OffsetDateTime#now()};
     * </p>
     *
     * @return {@true} in case the token data has already expired.
     */
    default boolean hasExpired() {
        return this.getExpiration() != null && this.getExpiration().isBefore(OffsetDateTime.now());
    }


    /**
     * Checks whether this token data is valid, meaning it has been successfully created and not yet expired.
     *
     * <p>
     * For validity, the following checks are performed:
     * </p>
     * <ol>
     * <li>The token data has been successfully created and thus {@link #isSuccess()} returns {@ode true}</li>
     * <li>The token data contains an access token, fetched with {@link #getAccessToken()}</li>
     * <li>The token data has not yet expired</li>
     * </ol>
     *
     * @return {@true} in case the token data is valid.
     */
    default boolean isValid() {
        return this.isSuccess()
            && !this.hasExpired()
            && this.getAccessToken() != null && !this.getAccessToken().trim().isEmpty();
    }
}
