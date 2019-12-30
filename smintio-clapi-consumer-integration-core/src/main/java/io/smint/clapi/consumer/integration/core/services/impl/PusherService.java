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

package io.smint.clapi.consumer.integration.core.services.impl;

import java.text.MessageFormat;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.pusher.client.Authorizer;
import com.pusher.client.Pusher;
import com.pusher.client.PusherOptions;
import com.pusher.client.channel.Channel;
import com.pusher.client.channel.PrivateChannelEventListener;
import com.pusher.client.channel.PusherEvent;
import com.pusher.client.connection.ConnectionEventListener;
import com.pusher.client.connection.ConnectionState;
import com.pusher.client.connection.ConnectionStateChange;
import com.pusher.client.util.HttpAuthorizer;

import io.smint.clapi.consumer.integration.core.configuration.IAuthTokenStorage;
import io.smint.clapi.consumer.integration.core.configuration.models.IAuthTokenModel;
import io.smint.clapi.consumer.integration.core.configuration.models.ISettingsModel;
import io.smint.clapi.consumer.integration.core.exceptions.SmintIoAuthenticatorException;
import io.smint.clapi.consumer.integration.core.exceptions.SmintIoAuthenticatorException.AuthenticatorError;
import io.smint.clapi.consumer.integration.core.services.IPushNotificationService;


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


    private static final Logger LOG = Logger.getLogger(PusherService.class.getName());


    private final ISettingsModel _settings;
    private final IAuthTokenStorage _tokenStorage;
    private Pusher _pusher;
    private final List<Runnable> _jobsToNotify = new Vector<>();
    private String _customApplicationKey;
    private Authorizer _customAuthorizer;


    @Inject
    public PusherService(final ISettingsModel settings, final IAuthTokenStorage authTokenStorage) {
        this._settings = settings;
        this._tokenStorage = authTokenStorage;
    }


    public PusherService(
        final Authorizer customPusherAuthorizer,
        final String customApplicationKey,
        final ISettingsModel settings,
        final IAuthTokenStorage authTokenStorage
    ) {
        this(settings, authTokenStorage);
        this._customApplicationKey = customApplicationKey;
        this._customAuthorizer = customPusherAuthorizer;
    }


    @Override
    public Future<IPushNotificationService> startNotificationService(final Runnable job) {

        if (job != null) {
            this._jobsToNotify.add(job);
        }


        if (this._pusher == null) {
            this._pusher = this.createPusherService(
                this._settings,
                this._tokenStorage != null ? this._tokenStorage.get() : null
            );
            this.subscribeToPusherChannel(this._settings.getChannelId());

            final Pusher pusher = this._pusher;
            final PusherService pusherService = this;
            final CompletableFuture<IPushNotificationService> result = new CompletableFuture<>();
            final ConnectionEventListener[] eventListener = new ConnectionEventListener[1];

            eventListener[0] = new ConnectionEventListener() {

                @Override
                public void onConnectionStateChange(final ConnectionStateChange change) {
                    pusher.getConnection().unbind(ConnectionState.CONNECTED, eventListener[0]);
                    result.complete(pusherService);
                }

                @Override
                public void onError(final String arg0, final String arg1, final Exception excp) {
                    pusher.getConnection().unbind(ConnectionState.CONNECTED, eventListener[0]);
                    result.completeExceptionally(excp);
                }
            };
            this._pusher.getConnection().bind(ConnectionState.CONNECTED, eventListener[0]);

            this._pusher.connect(this);
            return result;
        } else {
            return CompletableFuture.completedFuture(this);
        }
    }


    @Override
    public PusherService stopNotificationService() {

        if (this._pusher != null) {

            final String channelName = MessageFormat.format(PUSHER__CHANNEL, this._settings.getChannelId());
            this._pusher.unsubscribe(channelName);

            try {
                // wait for the final processing of all events.
                Thread.sleep(1);
            } catch (final InterruptedException ignore) {
                // ignore
            }
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
        LOG.log(Level.WARNING, "An Pusher exception occured: " + message + " [" + code + "]", excp);
    }


    private PusherService subscribeToPusherChannel(final int channelId) {

        final String channelName = MessageFormat.format(PUSHER__CHANNEL, this._settings.getChannelId());
        LOG.info(() -> "Pusher: subscribing to channel '" + channelName + "'");


        final List<Runnable> jobs = this._jobsToNotify;
        final Runnable notifyAllJobs = () -> {

            LOG.info(() -> "Pusher['" + channelName + "'] notifying all jobs: " + jobs.size());

            final Runnable[] allJobs = jobs.toArray(new Runnable[jobs.size()]);
            for (int i = 0; i < allJobs.length; i++) {
                final Runnable job = allJobs[i];
                try {
                    LOG.info("Pusher['" + channelName + "'] executing job " + i);
                    job.run();
                } catch (final RuntimeException ignore) {
                    // ignore
                }
            }
        };


        final Channel channel = this._pusher.subscribePrivate(channelName);
        channel.bind(PUSHER__EVENT_NAME, new PrivateChannelEventListener() {

            @Override
            public void onSubscriptionSucceeded(final String arg0) {
                LOG.info(() -> "Pusher: subscribing to channel '" + channelName + "' succeeded!");
            }

            @Override
            public void onEvent(final PusherEvent arg0) {
                // call all jobs
                notifyAllJobs.run();
            }

            @Override
            public void onAuthenticationFailure(final String message, final Exception excp) {
                LOG.log(
                    Level.WARNING,
                    excp,
                    () -> "Pusher: subscribing to channel '" + channelName + "' failed! " + message
                );
            }

        });

        return this;
    }


    private Pusher createPusherService(final ISettingsModel settings, final IAuthTokenModel authToken) {

        this.validateSettings(settings);
        this.validateAuthToken(authToken);

        final String pusherAuthEndpoint = MessageFormat.format(
            PUSHER__OAUTH_SMINTIO_ENDPOINT, settings.getTenantId()
        );

        Authorizer authorizer = this._customAuthorizer;
        if (authorizer == null) {
            authorizer = new HttpAuthorizer(pusherAuthEndpoint);

            final String accessToken = authToken.getAccessToken();
            if (accessToken != null && !accessToken.isEmpty()) {

                final Map<String, String> authorizationHeaders = new Hashtable<>();
                authorizationHeaders.put("Authorization", "Bearer " + accessToken);
                ((HttpAuthorizer) authorizer).setHeaders(authorizationHeaders);
            }
        }

        return new Pusher(
            this._customApplicationKey != null && !this._customApplicationKey.isEmpty()
                ? this._customApplicationKey
                : PUSHER__APPLICATION_KEY,
            new PusherOptions()
                .setCluster(PUSHER__CLUSTER)
                .setAuthorizer(authorizer)
        );
    }


    private void validateSettings(final ISettingsModel settings) {

        if (settings == null) {
            throw new SmintIoAuthenticatorException(
                AuthenticatorError.SmintIoIntegrationWrongState,
                "No settings are available"
            );
        }

        if (settings.getTenantId() == null || settings.getTenantId().isEmpty()) {
            throw new SmintIoAuthenticatorException(
                AuthenticatorError.SmintIoIntegrationWrongState,
                "The tenant ID is missing"
            );
        }

        if (settings.getChannelId() <= 0) {
            throw new SmintIoAuthenticatorException(
                AuthenticatorError.SmintIoIntegrationWrongState,
                "The channel ID is invalid: " + settings.getChannelId()
            );
        }
    }


    private void validateAuthToken(final IAuthTokenModel authToken) {

        if (authToken == null) {
            throw new SmintIoAuthenticatorException(
                AuthenticatorError.SmintIoIntegrationWrongState,
                "No access token for Smint.io are available"
            );
        }


        if (!authToken.isSuccess() || authToken.getAccessToken() == null || authToken.getAccessToken().isEmpty()) {
            throw new SmintIoAuthenticatorException(
                AuthenticatorError.SmintIoIntegrationWrongState,
                "The Smint.io access token is missing"
            );
        }
    }

}
