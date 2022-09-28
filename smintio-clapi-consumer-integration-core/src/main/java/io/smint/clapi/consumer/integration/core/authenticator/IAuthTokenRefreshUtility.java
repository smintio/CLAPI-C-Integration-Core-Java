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

import io.smint.clapi.consumer.integration.core.configuration.models.IAuthTokenModel;
import io.smint.clapi.consumer.integration.core.exceptions.SmintIoAuthenticatorException;


/**
 * A utility to refresh the OAuth authentication token, in case it has expired.
 *
 * <p>
 * Since refreshing a once valid OAuth authentication token can be performed without user interaction, such utility can
 * be very simple. Implementing classes are NOT required to support complex first authorization, as this involves manual
 * interaction. Utilities of that kind are only intended to <i>refresh</i> authentication tokens and not to obtain such
 * token initially.
 * </p>
 */
public interface IAuthTokenRefreshUtility {
    /**
     * Refresh expired authentication token data, utilizing the stored refresh token.
     *
     * <p>
     * The refresh is performed, no matter whether the authentication token has already expired or not. It is up to the
     * caller to not call this method in case the authentication token is still valid. A refresh is attempted and a new
     * token model is returned. The caller must check the return value to see, whether the new token is valid or not,
     * thus whether refreshing the token has been successfully done or not.
     * </p>
     *
     * <h4>Return same data as passed-in in case of insufficient refresh data.</h4>
     * <p>
     * In case the refreshing fails because of insufficient refresh token data, the originally passed-in token model
     * data is returned unchanged if it has not yet expired. So token data that is valid before can be re-used. If the
     * provided token data has already expired but its {@link IAuthTokenModel#isSuccess()} flag still returns
     * {@code true}, then every data is copied to a new token model with its success flag being set to {@code false}.
     * This will provide the opportunity to reuse still valid authentication data.
     * </p>
     *
     * <h4>Return some empty data if OAuth service rejected request.</h4>
     * <p>
     * In case sufficient refresh data is available but the Smint.io OAuth service rejected the refresh request for some
     * reason, a {@link SmintIoAuthenticatorException} exception is thrown.
     * </p>
     *
     * @param expiredToken the token to refresh - must not be {@code null}. If the token model does not contain
     *                     sufficient data for refreshing the authentication token (like missing the refresh token),
     *                     then refreshing fails. In such cases, a warnin is logged and the <i>same</i> token model is
     *                     returned. So, if the passed-in token was valid, it is still, as it is returned unchanged in
     *                     such cases.
     * @return a token model instance and never {@code null}. If refreshing failed, then
     *         {@link IAuthTokenModel#isSuccess()} returns {@code false}. The reason for failure are just with a
     *         {@link java.util.logging.Level#WARNING}
     * @throws IllegalArgumentException      in case the parameter {@code expiredToken} is {@code null}.
     * @throws SmintIoAuthenticatorException if refreshing was rejected by Smint.io's OAuth service.
     */
    IAuthTokenModel refreshOAuthToken(final IAuthTokenModel expiredToken) throws SmintIoAuthenticatorException;
}
