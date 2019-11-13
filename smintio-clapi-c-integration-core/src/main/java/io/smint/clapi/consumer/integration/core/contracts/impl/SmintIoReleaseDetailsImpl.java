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

import java.util.Map;

import io.smint.clapi.consumer.integration.core.contracts.ISmintIoReleaseDetails;


/**
 * POJO to provide some release details for an asset.
 */
public class SmintIoReleaseDetailsImpl implements ISmintIoReleaseDetails {

    private String _modelReleaseState = null;
    private String _propertyReleaseState = null;
    private Map<String, String> _providerAllowedUseComment = null;
    private Map<String, String> _providerReleaseComment = null;
    private Map<String, String> _providerUsageConstraints = null;


    @Override
    public String getModelReleaseState() {
        return this._modelReleaseState;
    }


    /**
     * Sets a new value to ModelReleaseState.
     *
     * @param newModelReleaseState a new list of values to set
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    public SmintIoReleaseDetailsImpl setModelReleaseState(final String newModelReleaseState) {
        this._modelReleaseState = newModelReleaseState;
        return this;
    }


    @Override
    public String getPropertyReleaseState() {
        return this._propertyReleaseState;
    }


    /**
     * Sets a new value to PropertyReleaseState.
     *
     * @param newPropertyReleaseState a new list of values to set
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    public SmintIoReleaseDetailsImpl setPropertyReleaseState(final String newPropertyReleaseState) {
        this._propertyReleaseState = newPropertyReleaseState;
        return this;
    }


    @Override
    public Map<String, String> getProviderAllowedUseComment() {
        return this._providerAllowedUseComment;
    }


    /**
     * Sets a new value to ProviderAllowedUseComment.
     *
     * @param newProviderAllowedUseComment the new value to set.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    public SmintIoReleaseDetailsImpl setProviderAllowedUseComment(
        final Map<String, String> newProviderAllowedUseComment
    ) {
        this._providerAllowedUseComment = newProviderAllowedUseComment;
        return this;
    }


    @Override
    public Map<String, String> getProviderReleaseComment() {
        return this._providerReleaseComment;
    }


    /**
     * Sets a new value to ProviderReleaseComment.
     *
     * @param newProviderReleaseComment the new value to set.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    public SmintIoReleaseDetailsImpl setProviderReleaseComment(final Map<String, String> newProviderReleaseComment) {
        this._providerReleaseComment = newProviderReleaseComment;
        return this;
    }


    @Override
    public Map<String, String> getProviderUsageConstraints() {
        return this._providerUsageConstraints;
    }


    /**
     * Sets a new value to ProviderUsageConstraints.
     *
     * @param newProviderUsageConstraints the new value to set.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    public SmintIoReleaseDetailsImpl setProviderUsageConstraints(
        final Map<String, String> newProviderUsageConstraints
    ) {
        this._providerUsageConstraints = newProviderUsageConstraints;
        return this;
    }
}

