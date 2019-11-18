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
 * Defines values for custom licensing options as created and provided by upstream content providers.
 *
 * <p>
 * Any Custom license options, that do not fit any existing license data structure, are reflected with these text.
 * Usually customers at Smint.io platform have closed a special contract with upstream content provider. So these
 * contracts might be a source of custom license options.
 * </p>
 */
public interface ISyncLicenseOption {

    /**
     * Set the localized name for this licensing option.
     *
     * @param name the localized name or {@code null}.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    ISyncLicenseOption setName(final Map<Locale, String> name);


    /**
     * Set an arbitrary localized licensing text for this licensing option.
     *
     * <p>
     * An custom licensing text, to describe additional license terms that are only available in written text but not in
     * a structured data. These texts are created and provided by upstream content providers.
     * </p>
     *
     * @param licenseText the localized text or {@code null}.
     * @return {@code this} to support <a href="https://en.wikipedia.org/wiki/Fluent_interface">Fluent Interface</a>
     */
    ISyncLicenseOption setLicenseText(Map<Locale, String> licenseText);
}
