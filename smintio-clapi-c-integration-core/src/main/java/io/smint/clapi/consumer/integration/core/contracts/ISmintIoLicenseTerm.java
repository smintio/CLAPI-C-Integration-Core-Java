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

import java.time.OffsetDateTime;
import java.util.Locale;
import java.util.Map;


/**
 * Provides informations for licenses on assets.
 */
public interface ISmintIoLicenseTerm {

    int getSequenceNumber();

    /**
     * Get the name of this license in multiple languages.
     *
     * @return the name of the license in multiple languages.
     */
    Map<Locale, String> getName();

    String[] getExclusivities();

    String[] getAllowedUsages();

    String[] getRestrictedUsages();

    String[] getAllowedSizes();

    String[] getRestrictedSizes();

    String[] getAllowedPlacements();

    String[] getRestrictedPlacements();

    String[] getAllowedDistributions();

    String[] getRestrictedDistributions();

    String[] getAllowedGeographies();

    String[] getRestrictedGeographies();

    String[] getAllowedIndustries();

    String[] getRestrictedIndustries();

    String[] getAllowedLanguages();

    String[] getRestrictedLanguages();

    String[] getUsageLimits();

    OffsetDateTime getValidFrom();

    OffsetDateTime getValidUntil();

    OffsetDateTime getToBeUsedUntil();


    /**
     * Provides a flag that this term restricts the use of the related asset to editorial usage.
     *
     * <p>
     * Until a value has been passed to this function, it can not be determined whether the asset is for editorial use
     * only or not. There might be some content provider that do not provide this information on some assets. It simply
     * is impossible to tell. The default assumption and its derived legal action depends on the synchronization target.
     * Smint.io can not give any advise how to handle such content.
     * </p>
     *
     * @return {@link Boolean#TRUE} in case the term restricts for <em>Editorial Use</em> only and {@link Boolean#FALSE}
     *         otherwise. If this can not be determined, than {@code null} is being returned.
     */
    Boolean isEditorialUse();
}

