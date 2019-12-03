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
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO
// THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
// TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
// SOFTWARE.
//
// SPDX-License-Identifier: MIT

package io.smint.clapi.consumer.integration.core.exceptions;

/**
 * Exception wrapper for all kind of exceptions occurred during authentication against Smint.io API.
 *
 * <p>
 * A lot of different kind of exceptions may occur when authenticating against the Smint.io API. These exceptions are
 * wrapped into this class in order to preserve the full stack and provide best possible debug information.
 * </p>
 */
public class SmintIoAuthenticatorException extends RuntimeException {


    private static final long serialVersionUID = 6271619852259353633L;


    /**
     * provides more information about the kind of error this exception covers.
     */
    public enum AuthenticatorError {
        /**
         * The current state does not permit any authorization with the Smint.io API.
         *
         * <p>
         * Probably because settings are wrong.
         * </p>
         */
        SmintIoIntegrationWrongState,

        /**
         * Acquiring the OAuth access token for Smint.io API failed.
         */
        CannotAcquireSmintIoToken,

        /**
         * Refreshing the OAuth access token for Smint.io API with the refresh token failed.
         */
        CannotRefreshSmintIoToken,

        /**
         * Some unknown, generic error.
         */
        Generic
    }


    private final AuthenticatorError _errorType;


    /**
     * same as {@link java.lang.Exception#Exception(String, Throwable)} with error type set to
     * {@link AuthenticatorError#Generic}.
     *
     * @param message the exception message.
     * @param cause   another exception that is the cause for this one.
     */
    public SmintIoAuthenticatorException(final String message, final Throwable cause) {
        this(AuthenticatorError.Generic, message, cause);
    }


    /**
     * same as {@link java.lang.Exception#Exception(String)} with error type set to {@link AuthenticatorError#Generic}.
     *
     * @param message the exception message.
     */
    public SmintIoAuthenticatorException(final String message) {
        this(AuthenticatorError.Generic, message);
    }


    /**
     * same as {@link java.lang.Exception#Exception(Throwable)} with error type set to
     * {@link AuthenticatorError#Generic}.
     *
     * @param cause another exception that is the cause for this one.
     */
    public SmintIoAuthenticatorException(final Throwable cause) {
        this(AuthenticatorError.Generic, cause);
    }


    /**
     * same as {@link #SmintIoAuthenticatorException(String, Throwable)} with additional setting of error type.
     *
     * @param errorType a type of this error, which makes it easier to check for various errors of the same class but
     *                  with slight differences
     * @param message   the exception message.
     * @param cause     another exception that is the cause for this one.
     */
    public SmintIoAuthenticatorException(
        final AuthenticatorError errorType, final String message, final Throwable cause
    ) {
        super(message, cause);
        this._errorType = errorType;
    }


    /**
     * same as {@link #SmintIoAuthenticatorException(String)} with additional setting of error type.
     *
     * @param errorType a type of this error, which makes it easier to check for various errors of the same class but
     *                  with slight differences
     * @param message   the exception message.
     */
    public SmintIoAuthenticatorException(final AuthenticatorError errorType, final String message) {
        super(message);
        this._errorType = errorType;
    }


    /**
     * same as {@link #SmintIoAuthenticatorException(Throwable)} with additional setting of error type.
     *
     * @param errorType a type of this error, which makes it easier to check for various errors of the same class but
     *                  with slight differences
     * @param cause     another exception that is the cause for this one.
     */
    public SmintIoAuthenticatorException(final AuthenticatorError errorType, final Throwable cause) {
        super(cause);
        this._errorType = errorType;
    }


    /**
     * Indicates the error type this exception covers.
     *
     * @return the error type of this exception or {@link AuthenticatorError#Generic}.
     */
    public AuthenticatorError getErrorType() {
        return this._errorType;
    }
}
