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

package io.smint.clapi.consumer.integration.core.services;

import java.text.MessageFormat;
import java.util.Hashtable;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.pusher.client.Pusher;
import com.pusher.client.PusherOptions;
import com.pusher.client.channel.Channel;
import com.pusher.client.connection.ConnectionEventListener;
import com.pusher.client.connection.ConnectionState;
import com.pusher.client.connection.ConnectionStateChange;
import com.pusher.client.util.HttpAuthorizer;

import io.smint.clapi.consumer.integration.core.configuration.models.IAuthTokenModel;
import io.smint.clapi.consumer.integration.core.configuration.models.ISettingsModel;


/**
 * Connects to <a href="https://pusher.com">Pusher.com</a> to receive notifications about newly purchased assets.
 *
 * <p>
 * The pusher service instance maintains a connection to the channel for the synchronization tenant and executes the
 * provided job on every received notification.
 * </p>
 */
@Singleton
public class PusherService implements IPushNotificationService, ConnectionEventListener {

    /**
     * The cluster used with the pusher.com service.
     *
     * <pre>
     *    {@code PUSHER__CLUSTER} = {@value #PUSHER__CLUSTER}
     * </pre>
     */
    public static final String PUSHER__CLUSTER = "eu";


    /**
     * The cluster used with the pusher.com service.
     *
     * <p>
     * The value is formated with {@link java.text.MessageFormat#format(String, Object...)} in order to fill the
     * placeholder with the tenant ID, taken from {@link ISettingsModel#getTenantId()}.
     * </p>
     *
     * <pre>
     *    {@code PUSHER__OAUTH_SMINTIO_ENDPOINT} = {@value #PUSHER__OAUTH_SMINTIO_ENDPOINT}
     * </pre>
     */
    public static final String PUSHER__OAUTH_SMINTIO_ENDPOINT = "https://{0}-clapi.smint.io/consumer/v1/notifications/pusher/auth";


    /**
     * The name of the event to listen to on the pusher service.
     *
     * <pre>
     *    {@code PUSHER__EVENT_NAME} = {@value #PUSHER__EVENT_NAME}
     * </pre>
     */
    public static final String PUSHER__EVENT_NAME = "global-transaction-history-update";


    /**
     * The name of the event to listen to on the pusher service.
     *
     * <p>
     * The value is formated with {@link java.text.MessageFormat#format(String, Object...)} in order to fill the // *
     * placeholder with the channel ID, taken from {@link ISettingsModel#getChannelId()}.
     * </p>
     *
     * <pre>
     *    {@code PUSHER__EVENT_NAME} = {@value #PUSHER__EVENT_NAME}
     * </pre>
     */
    public static final String PUSHER__CHANNEL = "private-2-{0}";


    /**
     * The application key used with authorizing Pusher.com service.
     */
    private static final String PUSHER__APPLICATION_KEY = "32f31c26a83e09dc401b";


    private final static Logger LOG = Logger.getLogger(PusherService.class.getName());


    private final ISettingsModel _settings;
    private final IAuthTokenModel _authToken;
    private Pusher _pusher;

    @Inject
    public PusherService(final ISettingsModel settings, final IAuthTokenModel authToken) {
        this._settings = settings;
        this._authToken = authToken;
    }


    @Override
    public PusherService startNotificationService(final Runnable job) {

        if (this._pusher == null) {
            this.validateSettings(this._settings);
            this.validateAuthToken(this._authToken);

            final String pusherAuthEndpoint = MessageFormat.format(
                PUSHER__OAUTH_SMINTIO_ENDPOINT, this._settings.getTenantId()
            );

            final HttpAuthorizer authorizer = new HttpAuthorizer(pusherAuthEndpoint);

            final String accessToken = this._authToken.getAccessToken();
            if (accessToken != null && !accessToken.isEmpty()) {

                final Map<String, String> authorizationHeaders = new Hashtable<>();
                authorizationHeaders.put("Authorization", "Bearer " + accessToken);
                authorizer.setHeaders(authorizationHeaders);
            }

            this._pusher = new Pusher(
                PUSHER__APPLICATION_KEY,
                new PusherOptions().setCluster(PUSHER__CLUSTER).setAuthorizer(authorizer)
            );

            this._pusher.connect(this);

        } else {
            this._pusher.connect();
        }

        if (this._pusher.getConnection().getState() == ConnectionState.CONNECTED) {
            this.subscribeToPusherChannel(this._settings.getChannelId(), job);
        }

        return this;
    }


    @Override
    public PusherService stopNotificationService() {

        if (this._pusher != null) {

            final String channelName = MessageFormat.format(PUSHER__CHANNEL, this._settings.getChannelId());
            this._pusher.unsubscribe(channelName);
            this._pusher.disconnect();
        }

        return this;
    }


    @Override
    public void onConnectionStateChange(final ConnectionStateChange change) {
        LOG.info(() -> "Pusher connection state changed to " + change.getCurrentState());
    }


    @Override
    public void onError(final String message, final String code, final Exception excp) {
        LOG.log(Level.WARNING, "An Pusher exception occured", excp);
    }


    private PusherService subscribeToPusherChannel(final int channelId, final Runnable job) {

        Objects.requireNonNull(job, "Invalid job (null) provided to be executed for notification events");

        final String channelName = MessageFormat.format(PUSHER__CHANNEL, this._settings.getChannelId());
        final Channel channel = this._pusher.subscribePrivate(channelName);
        channel.bind(PUSHER__EVENT_NAME, event -> new Thread(job));

        return this;
    }


    private void validateSettings(final ISettingsModel settings) {

    }

    private void validateAuthToken(final IAuthTokenModel authToken) {

    }


}
