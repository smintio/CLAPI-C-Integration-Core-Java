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

package io.smint.clapi.consumer.integration.core.configuration.models;

import java.text.ParseException;


/**
 * Implementing classes convert model {@code T} to and from {@link java.lang.String}.
 *
 * <p>
 * The representation within {@code String} is totally up to the implementation, whether it is something like XML or
 * JSON or any other form of textual representation.
 * </p>
 */
public interface IModelStringConverter<T> {

    /**
     * Parses the provided {@link String} data and converts it to an instance of {@code T}.
     *
     * @param encodedData the encoded model data to parse.
     * @return The extracted model data or {@code null}. In case parsing fail, then an exception of
     *         {@link ParseException} is thrown. In case the parameter {@code encodedToken} is an empty string or
     *         {@code null}, then {@code null} is returned.
     * @throws ParseException in case the provided text can not be converted to model data and all characters consumed.
     *                        Empty {@code String} values or {@code null} values are ignored and not parsed and thus no
     *                        exception is being thrown.
     */
    T decode(final String encodedData) throws RuntimeException;


    /**
     * Encodes an model to a {@code String} value.
     *
     * @param newModelData new model data for sync with Smint.io API server, that need to be encoded.
     * @return An encoded version of the token or {@code null} in case the provided parameter is {@code null}.
     * @throws ParseException in case the provided model data can not be converted to a {@code String} value.
     *                        Nevertheless {@code null} values are ignored and not converted and thus no exception is
     *                        being thrown.
     */
    String encode(final T newModelData) throws RuntimeException;
}
