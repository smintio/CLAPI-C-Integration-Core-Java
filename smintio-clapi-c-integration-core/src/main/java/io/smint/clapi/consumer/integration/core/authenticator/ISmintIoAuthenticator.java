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

import io.smint.clapi.consumer.integration.core.configuration.IAuthTokenStorage;
import io.smint.clapi.consumer.integration.core.configuration.models.ISettingsModel;
import io.smint.clapi.consumer.integration.core.exceptions.SmintIoAuthenticatorException;


/**
 * Authenticator to refresh authorization with Smint.io API.
 *
 * <p>
 * This authenticator uses the refresh token to renew the validity of the access token.
 * </p>
 *
 * <h2><a href="https://en.wikipedia.org/wiki/OAuth#OAuth_2.0_2">OAuth 2.0</a> authorization</h2>
 * <p>
 * Smint.io API utilizes OAuth to authorize access. The first authorization must be performed manually, because it
 * requires user interaction. This initial authorization creates an <em>Access Token</em>, that must be provided to this
 * library in an instance of {@link io.smint.clapi.consumer.integration.core.configuration.models.IAuthTokenModel} to
 * acquired from {@link IAuthTokenStorage}. The access token is used to authorize access to the API. The OAuth protocol
 * is used to ensure, that the created token is transmitted securely to the consumer and the access token expires after
 * a small amount of time.
 * </p>
 *
 * <p>
 * Along the access token, a <em>refresh token</em> is being created. The refresh token's validity duration usually is
 * much longer than that of the access token. It can be used to refresh the validity of the access token. So its
 * security is very important.
 * </p>
 */
public interface ISmintIoAuthenticator {

    /**
     * Renew the validity of the OAuth access token utilizing the refresh token.
     *
     * @param settings         The settings that contains the OAuth redirection URL and other invariant configuration
     *                         data that might be necessary.
     * @param authTokenStorage a storage provider to read the current OAuth access token and refresh token and then make
     *                         new tokens persistent for next use.
     * @throws SmintIoAuthenticatorException in case no refresh token is available, invalid, has expired or the Smint.io
     *                                       API rejected the refresh token.
     */
    ISmintIoAuthenticator refreshSmintIoToken(final ISettingsModel settings, final IAuthTokenStorage authTokenStorage)
        throws SmintIoAuthenticatorException;
}
