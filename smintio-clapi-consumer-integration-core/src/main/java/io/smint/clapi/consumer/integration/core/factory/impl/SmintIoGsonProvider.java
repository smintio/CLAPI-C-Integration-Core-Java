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

package io.smint.clapi.consumer.integration.core.factory.impl;

import javax.inject.Provider;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


/**
 * Provides an instance of {@link Gson}, that is configured to serialize Smint.io API data correctly to JSON.
 *
 * <p>
 * Nevertheless converting JSON data to instances is most often impossible, as JSON lacks type information.
 * </p>
 */
public class SmintIoGsonProvider implements Provider<Gson> {

    @Override
    public Gson get() {
        return new GsonBuilder()
            .setPrettyPrinting()
            .create();
    }
}
