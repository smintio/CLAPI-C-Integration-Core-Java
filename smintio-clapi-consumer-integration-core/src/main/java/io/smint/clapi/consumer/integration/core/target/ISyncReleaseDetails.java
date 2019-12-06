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

package io.smint.clapi.consumer.integration.core.target;

import java.util.Locale;
import java.util.Map;


/**
 * Defines some asset release details.
 *
 * <p>
 * The <em>Asset Release Details</em> list whether an agreements have been made with humans or properties visible on
 * images or in movies/pictures. Under most laws, models and and property owner of items or brand properties need to
 * agree to the release of the image or movie.
 * </p>
 */
public interface ISyncReleaseDetails extends ISyncDataType {

    /**
     * Sets the state of the release agreement with any human model visible.
     *
     * <p>
     * The value is a sync target platform specific ID, mapped from Smint.io key.
     * </p>
     *
     * @param modelReleaseStateKey the sync target ID or {@code null}.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    ISyncReleaseDetails setModelReleaseState(String modelReleaseStateKey);


    /**
     * Sets the state of the release agreement with any property owner, whose property is visible.
     *
     * <p>
     * The value is a sync target platform specific ID, mapped from Smint.io key.
     * </p>
     *
     * @param propertyReleaseStateKey the sync target ID or {@code null}.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    ISyncReleaseDetails setPropertyReleaseState(String propertyReleaseStateKey);


    /**
     * Sets a localized comment on the allowed usage of the asset, as provided by the content provider.
     *
     * <p>
     * Content providers may add additional comments, which help clarify the allowed usage of the related asset.
     * </p>
     *
     * @param providerAllowedUseComment the localized comment made by the content provider on the allowed usage or
     *                                  {@code null} if none has been made.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    ISyncReleaseDetails setProviderAllowedUseComment(Map<Locale, String> providerAllowedUseComment);


    /**
     * Sets a localized comment on the release of the asset, as provided by the content provider.
     *
     * <p>
     * Content providers may add additional comments, which help clarify the release of the related asset. eg: why a
     * model release is not required and other information.
     * </p>
     *
     * @param providerReleaseComment the localized comment made by the content provider on the release or {@code null}
     *                               if none has been made.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    ISyncReleaseDetails setProviderReleaseComment(Map<Locale, String> providerReleaseComment);


    /**
     * Sets a localized comment on restrictions on the usage of the asset, as provided by the content provider.
     *
     * <p>
     * Content providers may add additional comments, which help clarify the restricted usage of the related asset.
     * </p>
     *
     * @param providerUsageConstraints the localized comment made by the content provider on the restricted usage or
     *                                 {@code null} if none has been made.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    ISyncReleaseDetails setProviderUsageConstraints(Map<Locale, String> providerUsageConstraints);
}
