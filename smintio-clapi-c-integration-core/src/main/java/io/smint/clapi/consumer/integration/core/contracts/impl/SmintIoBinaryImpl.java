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

package io.smint.clapi.consumer.integration.core.contracts.impl;

import java.io.File;
import java.net.URL;
import java.util.Locale;
import java.util.Map;

import javax.inject.Provider;

import io.smint.clapi.consumer.integration.core.contracts.ISmintIoBinary;


/**
 * POJO to hold data for binaries.
 *
 * <p>
 * In order to provide a downloaded {@link File} in {@link #getDownloadedFile()}, a {@link Provider} is being set to
 * this POJO. Using such a provider enables the creator to download the file on first usage only, without the need to
 * download all files in advance although some (all) might not be needed. This POJO implementation does not make use of
 * any cache. So every call of {@link #getDownloadedFile()} is directly passed to {@link Provider#get()}.
 * </p>
 *
 * <pre>
 * {@code
 *     final ISmintIoBinary binaryData = new SmintIoBinaryImpl()
 *         .setUuid(UUID)
 *         .setDownloadFile(() -> {
 *             // NOW CHECK FILE EXISTENCE AND DOWNLOAD IF NECESSARY
 *             return downloadedFile;
 *         })
 * }
 * </pre>
 *
 */
public class SmintIoBinaryImpl implements ISmintIoBinary {


    private String _uuid = null;
    private String _contentType = null;
    private String _binaryType = null;
    private Map<Locale, String> _name = null;
    private Map<Locale, String> _description = null;
    private Map<Locale, String> _usage = null;
    private URL _downloadUrl = null;
    private Provider<File> _downloadFileProvider = null;
    private String _recommendedFileName = null;
    private Locale _locale = null;
    private int _version = -1;


    @Override
    public String getUuid() {
        return this._uuid;
    }


    /**
     * Sets a new value to uuid.
     *
     * @param newUuid the new value to set.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    public SmintIoBinaryImpl setUuid(final String newUuid) {
        this._uuid = newUuid;
        return this;
    }


    @Override
    public String getContentType() {
        return this._contentType;
    }


    /**
     * Sets a new value to content type.
     *
     * @param newContentType the new value to set.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    public SmintIoBinaryImpl setContentType(final String newContentType) {
        this._contentType = newContentType;
        return this;
    }


    @Override
    public String getBinaryType() {
        return this._binaryType;
    }


    /**
     * Sets a new value to binary type.
     *
     * @param newBinaryType the new value to set.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    public SmintIoBinaryImpl setBinaryType(final String newBinaryType) {
        this._binaryType = newBinaryType;
        return this;
    }


    @Override
    public Map<Locale, String> getName() {
        return this._name;
    }


    /**
     * Sets a new value to the translated version of names.
     *
     * @param newName the new value to set.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    public SmintIoBinaryImpl setName(final Map<Locale, String> newName) {
        this._name = newName;
        return this;
    }


    @Override
    public Map<Locale, String> getDescription() {
        return this._description;
    }


    /**
     * Sets a new value to the translated version of descriptions.
     *
     * @param newDescription the new value to set.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    public SmintIoBinaryImpl setDescription(final Map<Locale, String> newDescription) {
        this._description = newDescription;
        return this;
    }


    @Override
    public Map<Locale, String> getUsage() {
        return this._usage;
    }


    /**
     * Sets a new value to the translated version of usage description.
     *
     * @param newUsage the new value to set.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    public SmintIoBinaryImpl setUsage(final Map<Locale, String> newUsage) {
        this._usage = newUsage;
        return this;
    }


    @Override
    public URL getDownloadUrl() {
        return this._downloadUrl;
    }


    /**
     * Sets a new value to the download URL.
     *
     * @param newDownloadUrl the new value to set.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    public SmintIoBinaryImpl setDownloadUrl(final URL newDownloadUrl) {
        this._downloadUrl = newDownloadUrl;
        return this;
    }


    @Override
    public File getDownloadedFile() {
        return this._downloadFileProvider != null ? this._downloadFileProvider.get() : null;
    }


    /**
     * Sets a new value to the download file.
     *
     * @param newFileProvider the new value to set. The file does not need to be existent. The next call to
     *                        {@code #getDownloadedFile()} will download the file from the Smint.io platform in case the
     *                        file does not exist yet. At least the path of the target file must be writable.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    public SmintIoBinaryImpl setDownloadFile(final Provider<File> newFileProvider) {
        this._downloadFileProvider = newFileProvider;
        return this;
    }


    @Override
    public String getRecommendedFileName() {
        return this._recommendedFileName;
    }


    /**
     * Sets a new value to the recommended file name.
     *
     * @param newRecommendedFileName the new value to set.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    public SmintIoBinaryImpl setRecommendedFileName(final String newRecommendedFileName) {
        this._recommendedFileName = newRecommendedFileName;
        return this;
    }


    @Override
    public Locale getLocale() {
        return this._locale;
    }


    /**
     * Sets a new value to the locale of this asset.
     *
     * @param newLocale the new value to set.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    public SmintIoBinaryImpl setLocale(final Locale newLocale) {
        this._locale = newLocale;
        return this;
    }


    @Override
    public int getVersion() {
        return this._version;
    }


    /**
     * Sets a new value to the version.
     *
     * @param newVersion the new value to set.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    public SmintIoBinaryImpl setVersion(final int newVersion) {
        this._version = newVersion;
        return this;
    }
}

