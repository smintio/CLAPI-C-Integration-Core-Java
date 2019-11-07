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

package io.smint.clapi.consumer.integration.app.configuration.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Provider;

import io.smint.clapi.consumer.integration.core.configuration.models.IModelStringConverter;


/**
 * Stores a configuration model from {@link io.smint.clapi.consumer.integration.core.configuration} to a file and reads
 * the token from the file.
 *
 * <p>
 * The format of the file is defined by the instance of {@link IModelStringConverter} that is provided to this instance.
 * </p>
 *
 * <p>
 * No caching is performed. The token is read from the file with every call to {@link #get()} over and over again.
 * </p>
 *
 * @param <T> the type (interface) that is being stored to and loaded from the file.
 */
public class FileModelStorage<T> implements Provider<T> {

    private static final Logger LOG = Logger.getLogger(FileModelStorage.class.getName());

    private final File _fileStorage;
    private final IModelStringConverter<T> _tokenConverter;


    /**
     * Create a new facade to another token provider, caching its result for improved performance.
     *
     * @param tokenConverter The provider to wrap and query for data - must not be {@code null}.
     * @param file           The location of the file to read from and write to.
     */
    public FileModelStorage(final IModelStringConverter<T> tokenConverter, final File file) {
        this._tokenConverter = tokenConverter;
        this._fileStorage = file;

        Objects.requireNonNull(tokenConverter, "The token converter is invalid (null)!");
        Objects.requireNonNull(file, "The file storage location is invalid (null)!");
        if (file.isDirectory()) {
            throw new IllegalArgumentException(
                "Provided path is not a file but a directory. Can not write auth info to it. "
                    + file.getAbsolutePath()
            );
        }
    }


    @Override
    public T get() {

        if (this._fileStorage == null) {
            LOG.warning("File to read authentication token from does not exist");
            return null;
        }

        final StringBuffer json = new StringBuffer();
        try (final FileReader in = new FileReader(this._fileStorage)) {


            final int chr = in.read();
            while (chr >= 0) {
                json.append((char) chr);
            }

            return json.length() > 0 ? this._tokenConverter.decode(json.toString()) : null;

        } catch (final ParseException excp) {

            LOG.log(
                Level.WARNING,
                excp,
                () -> "Can not convert invalid JSON to Java data: " + json.toString()
            );

        } catch (final FileNotFoundException excp) {

            LOG.log(
                Level.WARNING,
                excp,
                () -> "File does not exist to read authentication token from"
                    + this._fileStorage.getAbsolutePath()
            );

        } catch (final IOException excp) {

            LOG.log(
                Level.WARNING,
                excp,
                () -> "Failed to read the file with authentication data: "
                    + this._fileStorage.getAbsolutePath()
            );
        }

        return null;
    }


    public FileModelStorage<T> store(final T newModelData) {

        Objects.requireNonNull(this._fileStorage, "The file storage has not been provided and is null!");

        String json = null;

        try {
            json = this._tokenConverter.encode(newModelData);
        } catch (final ParseException excp) {
            LOG.log(Level.WARNING, "Failed convert Java sync model data to JSON", excp);
        }

        if (json == null || json.isEmpty()) {
            this._fileStorage.delete();

        } else {

            try (final FileWriter out = new FileWriter(this._fileStorage)) {
                out.write(json);

            } catch (final IOException excp) {

                LOG.log(
                    Level.WARNING,
                    excp,
                    () -> "Failed to read the file with authentication information: "
                        + (this._fileStorage != null ? this._fileStorage.getAbsolutePath() : "null")
                );
            }
        }


        return this;
    }
}
