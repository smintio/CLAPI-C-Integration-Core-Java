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

package io.smint.clapi.consumer.integration.core.contracts;

import java.util.Locale;
import java.util.Map;


/**
 * Provides some asset release details.
 *
 * <p>
 * The <em>Asset Release Details</em> list whether an agreements have been made with humans or properties visible on
 * images or in movies/pictures. Under most laws, models and and property owner of items or brand properties need to
 * agree to the release of the image or movie.
 * </p>
 */
public interface ISmintIoReleaseDetails extends ISmintIoDataType {

    /**
     * Provides the state of the release agreement with any human model visible.
     *
     * <p>
     * The value is a textual representation of an enumeration. Each value is a simple reading text, valid with the
     * Smint.io platform. Its value is arbitrary and uses mixed cases. At the moment, the following values are used -
     * but more or others might be used in future releases of the Smint.io platform.
     * </p>
     *
     * <p>
     * Non-exhaustive example:
     * </p>
     * <ul>
     * <li>{@code all}</li>
     * <li>{@code not_released}</li>
     * <li>{@code no_release_required}</li>
     * <li>{@code released}</li>
     * </ul>
     *
     * <p>
     * Although a known set of values are used, some might be changed and more be added in the near future without any
     * further notice. So implementing classes must be prepared to support an arbitrary value.
     * </p>
     *
     * @return the enumeration value of the release state or {@code null} if unknown.
     */
    String getModelReleaseState();


    /**
     * Provides the state of the release agreement with any property owner, whose property is visible.
     *
     * <p>
     * Uses the same values as {@link #getModelReleaseState()}
     * </p>
     *
     * @return the enumeration value of the release state or {@code null} if unknown.
     */
    String getPropertyReleaseState();


    /**
     * Provides a localized comment on the allowed usage of the asset, as provided by the content provider.
     *
     * <p>
     * Content providers may add additional comments, which help clarify the allowed usage of the related asset.
     * </p>
     *
     * @return the localized comment made by the content provider on the allowed usage or {@code null} if none has been
     *         made.
     */
    Map<Locale, String> getProviderAllowedUseComment();


    /**
     * Provides a localized comment on the release of the asset, as provided by the content provider.
     *
     * <p>
     * Content providers may add additional comments, which help clarify the release of the related asset. eg: why a
     * model release is not required and other information.
     * </p>
     *
     * @return the localized comment made by the content provider on the release or {@code null} if none has been made.
     */
    Map<Locale, String> getProviderReleaseComment();


    /**
     * Provides a localized comment on restrictions on the usage of the asset, as provided by the content provider.
     *
     * <p>
     * Content providers may add additional comments, which help clarify the restricted usage of the related asset.
     * </p>
     *
     * @return the localized comment made by the content provider on the restricted usage or {@code null} if none has
     *         been made.
     */
    Map<Locale, String> getProviderUsageConstraints();
}

