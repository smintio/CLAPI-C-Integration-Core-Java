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

import java.net.URL;
import java.util.Objects;

import javax.inject.Inject;

import io.smint.clapi.consumer.integration.core.configuration.models.ISettingsModel;


/**
 * Holds values as defined by its base interface.
 */
public class SettingsModelImpl implements ISettingsModel {

    private String _clientSecret;
    private String _clientID;
    private URL _redirectUrl;
    private int _channelID;
    private String _tenantID;
    private String[] _importLanguages;


    @Inject
    public SettingsModelImpl() {
        // default
    }


    /**
     * Copy all data from another instance.
     *
     * @param copyFrom the source of all data to copy from.
     */
    public SettingsModelImpl(final ISettingsModel copyFrom) {
        this();

        Objects.requireNonNull(copyFrom, "invalid object to copy from has been provided");
        this
            .setTenantId(copyFrom.getTenantId())
            .setOAuthClientId(copyFrom.getOAuthClientId())
            .setOAuthClientSecret(copyFrom.getOAuthClientSecret())
            .setOAuthLocalUrlReceivingAccessData(copyFrom.getOAuthLocalUrlReceivingAccessData())
            .setChannelId(copyFrom.getChannelId())
            .setImportLanguages(copyFrom.getImportLanguages());
    }


    @Override
    public String getTenantId() {
        return this._tenantID;
    }


    /**
     * sets a new tenant ID and returns {@code this} for Fluent Interface.
     *
     * @param newTenantId the new tenant ID of Smint.io platform, whose assets should be synchronized to the
     *                    synrhonization target.
     * @return {@code this}
     */
    public SettingsModelImpl setTenantId(final String newTenantId) {
        this._tenantID = newTenantId;
        return this;
    }


    @Override
    public int getChannelId() {
        return this._channelID;
    }


    /**
     * sets a new channel ID and returns {@code this} for Fluent Interface.
     *
     * @param newChannelId the Pusher.com channel ID to subscript to its notification. This is specific to a Smint.io
     *                     tenant.
     * @return {@code this}
     */
    public SettingsModelImpl setChannelId(final int newChannelId) {
        this._channelID = newChannelId;
        return this;
    }


    @Override
    public String getOAuthClientId() {
        return this._clientID;
    }


    /**
     * sets a new client ID and returns {@code this} for Fluent Interface.
     *
     * @param newClientId the client ID the access and refresh tokens of OAuth are related to.
     * @return {@code this}
     */
    public SettingsModelImpl setOAuthClientId(final String newClientId) {
        this._clientID = newClientId;
        return this;
    }


    @Override
    public String getOAuthClientSecret() {
        return this._clientSecret;
    }


    /**
     * sets a new client secret used for OAuth and returns {@code this} for Fluent Interface.
     *
     * @param newClientSecret a new client secret to use with OAuth access token refresh.
     * @return {@code this}
     */
    public SettingsModelImpl setOAuthClientSecret(final String newClientSecret) {
        this._clientSecret = newClientSecret;
        return this;
    }


    @Override
    public URL getOAuthLocalUrlReceivingAccessData() {
        return this._redirectUrl;
    }


    /**
     * sets a local URL to receive OAuth access data from server by user's browser redirect.
     *
     * @param newRedirectUrl a new URL, must not be {@code null} or initiating OAuth authorization won't work.
     * @return {@code this}
     */
    public SettingsModelImpl setOAuthLocalUrlReceivingAccessData(final URL newRedirectUrl) {
        this._redirectUrl = newRedirectUrl;
        return this;
    }


    @Override
    public String[] getImportLanguages() {
        return this._importLanguages;
    }


    /**
     * sets a new list of import languages and returns {@code this} for Fluent Interface.
     *
     * @param newLanguagesToImport new list of languages to import. must not be {@code null} or empty.
     * @return {@code this}
     */
    public SettingsModelImpl setImportLanguages(final String[] newLanguagesToImport) {
        this._importLanguages = newLanguagesToImport;
        return this;
    }
}
