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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;

import java.text.MessageFormat;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import com.pusher.rest.Pusher;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import io.smint.clapi.consumer.integration.core.configuration.IAuthTokenStorage;
import io.smint.clapi.consumer.integration.core.configuration.models.IAuthTokenModel;
import io.smint.clapi.consumer.integration.core.configuration.models.ISettingsModel;


// CHECKSTYLE.OFF: MultipleStringLiterals
// CHECKSTYLE.OFF: MagicNumber

@ExtendWith(MockitoExtension.class)
@DisplayName("Test PusherService: Integration test")
public class TestPusherService {

    private static final String PUSHER_APP_ID = "922007";
    private static final String PUSHER_APP_KEY = "9eb796d4bcb1709af830";
    private static final String PUSHER_APP_SECRET = "f1e00f88148a71212225";
    private static final int PUSHER_CHANNEL_ID = 16;


    private Pusher pusher = null;


    @BeforeEach
    public void initPusher() {
        this.pusher = new Pusher(PUSHER_APP_ID, PUSHER_APP_KEY, PUSHER_APP_SECRET);
        this.pusher.setCluster(PusherService.PUSHER__CLUSTER);
        this.pusher.setEncrypted(true);
    }


    @Test
    @DisplayName("Receive Pusher notification and execute the job.")
    public void testReceiveNotificationExecuteJob(
        @Mock final ISettingsModel settings,
        @Mock final IAuthTokenStorage tokenStorage,
        @Mock final IAuthTokenModel authData
    ) throws Exception {

        final IAuthTokenModel[] authDataStorage = new IAuthTokenModel[1];
        authDataStorage[0] = authData;

        lenient().when(tokenStorage.get()).then((invocation) -> authDataStorage[0]);
        lenient().when(tokenStorage.getAuthData()).then((invocation) -> authDataStorage[0]);
        lenient().when(tokenStorage.storeAuthData((IAuthTokenModel) any()))
            .then((invocation) -> {
                final IAuthTokenModel[] customDataStorage = authDataStorage;
                customDataStorage[0] = invocation.getArgument(0);
                return invocation.getMock();
            });

        lenient().when(settings.getTenantId()).thenReturn("test");
        lenient().when(settings.getChannelId()).thenReturn(PUSHER_CHANNEL_ID);
        lenient().when(settings.getOAuthClientId()).thenReturn("test");
        lenient().when(settings.getOAuthClientSecret()).thenReturn("test");

        lenient().when(authData.getAccessToken()).thenReturn("test");
        lenient().when(authData.getRefreshToken()).thenReturn("test");
        lenient().when(authData.isSuccess()).thenReturn(true);


        final int[] jobExecutionCounter = new int[1];
        jobExecutionCounter[0] = 0;


        @SuppressWarnings("unchecked")
        final CompletableFuture<Integer>[] waitForExecution = new CompletableFuture[1];
        final Runnable job = () -> {
            jobExecutionCounter[0]++;
            waitForExecution[0].complete(jobExecutionCounter[0]);
            System.out.println("Executing TEST job: " + jobExecutionCounter[0]);
        };

        final PusherService service = new PusherService(
            (channel, socketId) -> this.pusher.authenticate(socketId, channel),
            PUSHER_APP_KEY,
            settings,
            tokenStorage
        );

        try {
            service.startNotificationService(job).get(2000, TimeUnit.MILLISECONDS);


            waitForExecution[0] = new CompletableFuture<>();
            this.sendPushNotification();
            Assertions.assertEquals(
                1,
                waitForExecution[0].get(2000, TimeUnit.MILLISECONDS),
                "job has not been executed just once!"
            );


            this.sendPushNotification();
            waitForExecution[0] = new CompletableFuture<>();
            Assertions.assertEquals(
                2,
                waitForExecution[0].get(2000, TimeUnit.MILLISECONDS),
                "job has not been executed twice!"
            );

        } finally {
            service.stopNotificationService();
        }
    }


    public void sendPushNotification() throws Exception {
        final String channelName = MessageFormat.format(PusherService.PUSHER__CHANNEL, PUSHER_CHANNEL_ID);

        System.out.println("PUSHER: Sending message to channel " + channelName);
        this.pusher.trigger(
            channelName,
            PusherService.PUSHER__EVENT_NAME,
            "TEST"
        );
    }

}

// CHECKSTYLE.ON: MultipleStringLiterals
// CHECKSTYLE.ON: MagicNumber
