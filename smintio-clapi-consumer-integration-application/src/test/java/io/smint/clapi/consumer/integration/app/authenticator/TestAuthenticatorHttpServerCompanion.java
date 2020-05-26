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

package io.smint.clapi.consumer.integration.app.authenticator;

import java.net.URL;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;


// CHECKSTYLE.OFF: MultipleStringLiterals
// CHECKSTYLE.OFF: MagicNumber


@DisplayName("Test abstract scheduler: AuthenticatorHttpServerCompanion")
public class TestAuthenticatorHttpServerCompanion {


    @Test
    @DisplayName("Extracting default port from URL if no port has been specified.")
    public void getPortFromUrl_DefaultPort() throws Exception {
        URL url = new URL("http://www.smint.io/");
        int port = AuthenticatorHttpServerCompanion.getPortFromUrl(url);
        Assertions.assertEquals(
            Integer.valueOf(80),
            port,
            "Failed to acquire port 80 from 'getPortFromUrl' for URL '" + url.toExternalForm() + "'!"
        );


        url = new URL("https://www.smint.io/");
        port = AuthenticatorHttpServerCompanion.getPortFromUrl(url);
        Assertions.assertEquals(
            443,
            port,
            "Failed to acquire port 443 from 'getPortFromUrl' for URL '" + url.toExternalForm() + "'!"
        );
    }

    @Test
    @DisplayName("Extracting specified port from URL if a port has been specified.")
    public void getPortFromUrl_SpecificPort() throws Exception {
        URL url = new URL("http://www.smint.io:40443/");
        int port = AuthenticatorHttpServerCompanion.getPortFromUrl(url);

        Assertions.assertEquals(
            40443,
            port,
            "Acquire port from 'getPortFromUrl' for URL '" + url.toExternalForm() + " is unexpected'!"
        );


        url = new URL("https://www.smint.io:9843/");
        port = AuthenticatorHttpServerCompanion.getPortFromUrl(url);
        Assertions.assertEquals(
            9843,
            port,
            "Acquire port from 'getPortFromUrl' for URL '" + url.toExternalForm() + " is unexpected'!"
        );
    }

    @Test
    @DisplayName("Failing extracting of port from invalid URL.")
    public void getPortFromUrl_Fail() throws Exception {
        Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> AuthenticatorHttpServerCompanion.getPortFromUrl(null),
            "'getPortFromUrl' did not fail for invalid <null> URL!"
        );
    }

    @Test
    @DisplayName("Failing extracting of port from invalid URL.")
    public void getPortFromUrl_ZeroForInvalidUrl() throws Exception {
        final URL url = new URL("file://www.smint.io/");
        final int port = AuthenticatorHttpServerCompanion.getPortFromUrl(url);
        Assertions.assertEquals(
            0,
            port,
            "Failed to acquire port 0 from 'getPortFromUrl' for URL '" + url.toExternalForm() + "'!"
        );
    }
}

// CHECKSTYLE.ON: MultipleStringLiterals
// CHECKSTYLE.ON: MagicNumber
