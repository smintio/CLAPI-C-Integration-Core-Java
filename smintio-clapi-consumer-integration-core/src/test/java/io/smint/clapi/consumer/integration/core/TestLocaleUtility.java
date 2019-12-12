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

import java.util.Arrays;
import java.util.Locale;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


// CHECKSTYLE.OFF: MultipleStringLiterals
// CHECKSTYLE.OFF: MagicNumber

@DisplayName("Test Locale utility")
public class TestLocaleUtility {


    @Test
    @DisplayName("test Locale handling ISO 639-3 language.")
    public void testLocaleIso3Language() throws Exception {

        Assertions.assertEquals(
            Locale.ENGLISH.toString(),
            "en",
            "English does not use ISO 639-3 code in toString()!"
        );

        Assertions.assertEquals(
            Locale.ENGLISH,
            new Locale("en"),
            "English differs when created with 'en'!"
        );

        Assertions.assertEquals(
            Locale.ENGLISH.getISO3Language(),
            new Locale("eng").getISO3Language(),
            "English language ISO 639-3 code differs when created with 'eng'!"
        );

        Assertions.assertNotEquals(
            Locale.ENGLISH,
            new Locale("eng"),
            "Finally JVM does not make any difference anymore between English created with 'en' and 'eng'!"
        );
    }


    @Test
    @DisplayName("test Locale conversion to ISO 639-2 language.")
    public void testLocaleIso3LanguageConversion() throws Exception {

        Assertions.assertEquals(
            Locale.ENGLISH,
            LocaleUtility.covertToISO2Locale(new Locale("eng")),
            "English differs when created with 'eng' despite conversion!"
        );

        Assertions.assertEquals(
            Locale.GERMAN,
            LocaleUtility.covertToISO2Locale(new Locale("deu")),
            "German differs when created with 'deu' despite conversion!"
        );

        Assertions.assertEquals(
            Locale.CHINESE,
            LocaleUtility.covertToISO2Locale(new Locale("zho")),
            "Chinese differs when created with 'zho' despite conversion!"
        );

        Assertions.assertEquals(
            Locale.KOREAN,
            LocaleUtility.covertToISO2Locale(new Locale("kor")),
            "Korean differs when created with 'kor' despite conversion!"
        );

        Arrays.stream(Locale.getAvailableLocales())
            .sorted((a, b) -> a.getISO3Language().compareTo(b.getISO3Language()))
            .forEach((language) -> System.out.println(language.getISO3Language() + " -> " + language.toString()));

    }
}

// CHECKSTYLE.ON: MultipleStringLiterals
// CHECKSTYLE.ON: MagicNumber
