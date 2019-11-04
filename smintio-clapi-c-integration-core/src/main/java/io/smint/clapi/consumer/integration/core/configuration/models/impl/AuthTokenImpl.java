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

package io.smint.clapi.consumer.integration.core.configuration.models.impl;

import java.time.OffsetDateTime;

import javax.inject.Inject;

import io.smint.clapi.consumer.integration.core.configuration.models.IAuthTokenModel;


/**
 * Default implementation of OAuth data to access the Smint.io RESTful API.
 */
public class AuthTokenImpl implements IAuthTokenModel {

    // Note: Although Smint.io style guide requires private names to be prefixed with "_", this is
    // not the case here, in order to get clean JSON property names without any annotation.
    // Avoiding any JSON library annotation helps keeping this class independent from any JSON library.

    private boolean isSuccess = false;
    private String accessToken;
    private String refreshToken;
    private String identityToken;
    private OffsetDateTime expirationDate;


    /**
     * Create a new empty instance.
     */
    @Inject
    public AuthTokenImpl() {
        // default
    }


    /**
     * Copy all data from another instance.
     */
    public AuthTokenImpl(final IAuthTokenModel otherAuthToken) {
        this();
        if (otherAuthToken != null) {
            this
                .setAccessToken(otherAuthToken.getAccessToken())
                .setIdentityToken(otherAuthToken.getIdentityToken())
                .setRefreshToken(otherAuthToken.getRefreshToken())
                .setIsSuccess(otherAuthToken.isSuccess())
                .setExpiration(otherAuthToken.getExpiration());
        }
    }


    @Override
    public boolean isSuccess() {
        return this.isSuccess;
    }


    /**
     * Set a new value for {@link #isSuccess()} and provide Fluent Interface
     *
     * @param isSuccess
     * @return {@code this} for <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    public AuthTokenImpl setIsSuccess(final boolean isSuccess) {
        this.isSuccess = isSuccess;
        return this;
    }


    @Override
    public String getAccessToken() {
        return this.accessToken;
    }


    /**
     * Set a new value for {@link #getAccessToken()} and provide Fluent Interface
     *
     * @param newAccessToken
     * @return {@code this} for <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    public AuthTokenImpl setAccessToken(final String newAccessToken) {
        this.accessToken = newAccessToken;
        return this;
    }


    @Override
    public String getRefreshToken() {
        return this.refreshToken;
    }


    /**
     * Set a new value for {@link #getRefreshToken()} and provide Fluent Interface
     *
     * @param newRefreshToken
     * @return {@code this} for <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    public AuthTokenImpl setRefreshToken(final String newRefreshToken) {
        this.refreshToken = newRefreshToken;
        return this;
    }


    @Override
    public String getIdentityToken() {
        return this.identityToken;
    }


    /**
     * Set a new value for {@link #getIdentityToken()} and provide Fluent Interface
     *
     * @param newIdentityToken
     * @return {@code this} for <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    public AuthTokenImpl setIdentityToken(final String newIdentityToken) {
        this.identityToken = newIdentityToken;
        return this;
    }


    @Override
    public OffsetDateTime getExpiration() {
        return this.expirationDate;
    }


    /**
     * Set a new value for {@link #getExpiration()} and provide Fluent Interface
     *
     * @param newExpireDate
     * @return {@code this} for <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    public AuthTokenImpl setExpiration(final OffsetDateTime newExpireDate) {
        this.expirationDate = newExpireDate;
        return this;
    }
}
