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

import java.lang.reflect.Method;
import java.net.URL;
import java.util.Objects;

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

        final Method getPortFromUrl = getPrivateFunction(
            AuthenticatorHttpServerCompanion.class, "getPortFromUrl", URL.class
        );


        Assertions.assertNotNull(getPortFromUrl, "Failed to find static method 'getPortFromUrl' !");

        URL url = new URL("http://www.smint.io/");
        Object port = getPortFromUrl.invoke(null, url);

        Assertions.assertNotNull(
            port,
            "Failed to acquire port 80 from 'getPortFromUrl' for URL '" + url.toExternalForm() + "'!"
        );
        Assertions.assertEquals(
            Integer.valueOf(80),
            port,
            "Failed to acquire port 80 from 'getPortFromUrl' for URL '" + url.toExternalForm() + "'!"
        );


        url = new URL("https://www.smint.io/");
        port = getPortFromUrl.invoke(null, url);

        Assertions.assertNotNull(
            port,
            "Failed to acquire port 443 from 'getPortFromUrl' for URL '" + url.toExternalForm() + "'!"
        );
        Assertions.assertEquals(
            Integer.valueOf(443),
            port,
            "Failed to acquire port 443 from 'getPortFromUrl' for URL '" + url.toExternalForm() + "'!"
        );
    }

    @Test
    @DisplayName("Extracting specified port from URL if a port has been specified.")
    public void getPortFromUrl_SpecificPort() throws Exception {

        final Method getPortFromUrl = getPrivateFunction(
            AuthenticatorHttpServerCompanion.class, "getPortFromUrl", URL.class
        );


        Assertions.assertNotNull(getPortFromUrl, "Failed to find static method 'getPortFromUrl' !");

        URL url = new URL("http://www.smint.io:40443/");
        Object port = getPortFromUrl.invoke(null, url);

        Assertions.assertNotNull(
            port,
            "Failed to acquire port 40443 from 'getPortFromUrl' for URL '" + url.toExternalForm() + "'!"
        );
        Assertions.assertEquals(
            Integer.valueOf(40443),
            port,
            "Acquire port from 'getPortFromUrl' for URL '" + url.toExternalForm() + " is unexpected'!"
        );


        url = new URL("https://www.smint.io:9843/");
        port = getPortFromUrl.invoke(null, url);

        Assertions.assertNotNull(
            port,
            "Failed to acquire port 9843 from 'getPortFromUrl' for URL '" + url.toExternalForm() + "'!"
        );
        Assertions.assertEquals(
            Integer.valueOf(9843),
            port,
            "Acquire port from 'getPortFromUrl' for URL '" + url.toExternalForm() + " is unexpected'!"
        );
    }

    @Test
    @DisplayName("Failing extracting of port from invalid URL.")
    public void getPortFromUrl_Fail() throws Exception {

        final Method getPortFromUrl = getPrivateFunction(
            AuthenticatorHttpServerCompanion.class, "getPortFromUrl", URL.class
        );


        Assertions.assertNotNull(getPortFromUrl, "Failed to find static method 'getPortFromUrl' !");

        Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> getPortFromUrl.invoke(null, null),
            "'getPortFromUrl' did not fail for invalid <null> URL!"
        );
    }

    @Test
    @DisplayName("Failing extracting of port from invalid URL.")
    public void getPortFromUrl_ZeroForInvalidUrl() throws Exception {

        final Method getPortFromUrl = getPrivateFunction(
            AuthenticatorHttpServerCompanion.class, "getPortFromUrl", URL.class
        );


        Assertions.assertNotNull(getPortFromUrl, "Failed to find static method 'getPortFromUrl' !");

        final URL url = new URL("file://www.smint.io/");
        final Object port = getPortFromUrl.invoke(null, url);

        Assertions.assertNotNull(
            port,
            "Failed to acquire port 0 from 'getPortFromUrl' for URL '" + url.toExternalForm() + "'!"
        );
        Assertions.assertEquals(
            Integer.valueOf(0),
            port,
            "Failed to acquire port 0 from 'getPortFromUrl' for URL '" + url.toExternalForm() + "'!"
        );
    }


    /**
     * Extracts a named private function from the instance and sets its accessible value to {@code true}.
     *
     * @param clazz          the class to access its private member function.
     * @param functionName   the function name to access
     * @param parameterTypes the types of the list of parameters to pass to the function
     * @return the Method handle to access the private function.
     * @throws Exception in case of any exception or access is denied.
     */
    private static <T> Method getPrivateFunction(
        final Class<T> clazz,
        final String functionName,
        final Class<?>... parameterTypes
    ) throws Exception {

        Objects.requireNonNull(clazz, "clazz parameter must not be null!");
        final Method func = clazz.getDeclaredMethod(
            functionName,
            parameterTypes
        );
        func.setAccessible(true);
        return func;
    }

}

// CHECKSTYLE.ON: MultipleStringLiterals
// CHECKSTYLE.ON: MagicNumber
