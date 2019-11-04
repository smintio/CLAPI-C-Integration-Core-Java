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

import java.util.Objects;

import javax.inject.Inject;

import com.google.gson.Gson;

import io.smint.clapi.consumer.integration.core.configuration.models.IAuthTokenModel;
import io.smint.clapi.consumer.integration.core.configuration.models.IModelStringConverter;


/**
 * Convert {@link IAuthTokenModel} to and from {@link java.lang.String} in JSON format.
 *
 * <p>
 * This class uses <a href="https://github.com/google/gson">Google's Gson library</a> to convert to and from JSON.
 * </p>
 *
 * @param <T> The interface of the model to convert to and from.
 * @param <I> A class implementing the interface {@code T}, which is necessary for Gson to create proper instances.
 */
public class ModelDataJsonConverter<T, I extends T> implements IModelStringConverter<T> {

    private final Gson _gson;
    private Class<I> _modelClass;


    /**
     * Create a new model JSON converter utilizing Google's Gson library.
     *
     * @param gson the Gson instance to use.
     * @see <a href="https://github.com/google/gson">https://github.com/google/gson</a>
     */
    @Inject
    public ModelDataJsonConverter(final Gson gson) {
        this._gson = gson;
        Objects.requireNonNull(gson, "JSON utility 'Gson' is empty!");
    }


    @Override
    public T decode(final String encodedToken) throws RuntimeException {
        if (encodedToken == null || encodedToken.isEmpty()) {
            return null;
        }

        return this._gson.fromJson(encodedToken, this.getClassOfModel());
    }


    @Override
    public String encode(final T newAuthTokenData) throws RuntimeException {

        if (newAuthTokenData == null) {
            return null;
        }

        return this._gson.toJson(newAuthTokenData);
    }


    /**
     * Return the class of the data model to be serialized - used for deserialization.
     *
     * @return the class instance of the model to deserialize - must not return {@code null}.
     */
    public Class<I> getClassOfModel() {
        return this._modelClass;
    }


    /**
     * Return the class of the data model to be serialized - used for deserialization.
     *
     * @return the class instance of the model to deserialize - must not return {@code null}.
     */
    protected ModelDataJsonConverter<T, I> setClassOfModel(final Class<I> newModelClass) {
        this._modelClass = newModelClass;
        return this;
    }
}
