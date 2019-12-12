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

package io.smint.clapi.consumer.integration.core;

import java.util.Locale;


/**
 * Implements utility tools to handle Java Locale peculiarities.
 */
public abstract class LocaleUtility {

    /**
     * Converts the provided locale to an instance with ISO 639-1 two letter code - if possible.
     *
     * <p>
     * Internally in Java Locale the language codes are not converted. So creating a locale for English with language
     * code "en" is different from creating a locale with language code "eng", although both codes denote the same
     * language. This would not be a problem if only the ISO 639-3 language codes {@link Locale#getISO3Language()} are
     * compared. Unfortunately this is not the case with {@link Locale#equals(Object)}. So creating a locale with
     * language code "eng" is not "equal" to {@link Locale#ENGLISH}, as the latter uses a two letter language code "en".
     * So all locales, that have been created with ISO 639-3 codes, should be converted to their two letter codes (ISO
     * 639-1) variants to make them comparable with system locales.
     * </p>
     *
     * <p>
     * Conversion is done by listing all available locales and then comparing the ISO 639-3 codes with the provided
     * parameter. If they match, then the matching system locale is returned. If none match or the provided locale
     * already uses two letter language code, then the provided locale is returned unchanged.
     * </p>
     *
     * @return a converted locale if an ISO 639-1 two letter code exists.
     */
    public static Locale covertToISO2Locale(final Locale locale) {
        if (locale == null) {
            return locale;
        }

        final String language = locale.getLanguage();
        if (language == null || language.isEmpty() || language.length() == 2) {
            return locale;
        }

        final String languageIso3 = locale.getISO3Language();
        if (languageIso3 == null || languageIso3.isEmpty() || languageIso3.length() != 3) {
            return locale;
        }


        // English and German are first citizen targets
        if (languageIso3.equals(Locale.ENGLISH.getISO3Language())) {
            return Locale.ENGLISH;

        } else if (languageIso3.equals(Locale.GERMAN.getISO3Language())) {
            return Locale.GERMAN;

        }


        // get all available locales but at first try without country
        for (final Locale sysLocale : Locale.getAvailableLocales()) {

            final String country = sysLocale.getCountry();
            if ((country == null || country.isEmpty()) && languageIso3.equals(sysLocale.getISO3Language())) {
                return sysLocale;
            }
        }


        // Now try with country code
        for (final Locale sysLocale : Locale.getAvailableLocales()) {
            if (languageIso3.equals(sysLocale.getISO3Language())) {
                return sysLocale;
            }
        }

        return locale;
    }
}
