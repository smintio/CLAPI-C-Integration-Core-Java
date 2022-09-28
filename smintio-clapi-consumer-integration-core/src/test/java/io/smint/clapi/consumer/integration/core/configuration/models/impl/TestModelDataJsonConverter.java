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

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.smint.clapi.consumer.integration.core.configuration.models.IAuthTokenModel;
import io.smint.clapi.consumer.integration.core.factory.impl.SmintIoGsonProvider;


// CHECKSTYLE.OFF: MultipleStringLiterals
// CHECKSTYLE.OFF: MagicNumber


@DisplayName("Test ModelDataJsonConverter")
public class TestModelDataJsonConverter {


    @Test
    @DisplayName("Get class information of JSON target implementation model.")
    public void getClassOfModel_isSameAsInConstructor() throws Exception {

        final MyAuthTokenJsonConverter converter = new MyAuthTokenJsonConverter();
        Assertions.assertNotNull(converter, "Failed to create converter!");
        Assertions.assertEquals(
            AuthTokenImpl.class,
            converter.getClassOfModel(),
            "Failed to convert JSON to token model - access token unexpected!"
        );
    }


    @Test
    @DisplayName("Decode valid auth token JSON")
    public void decode_ValidJson() throws Exception {

        final MyAuthTokenJsonConverter converter = new MyAuthTokenJsonConverter();
        final IAuthTokenModel expectedTokenData = new AuthTokenImpl()
            .setAccessToken(
                "eyJhbGciOiJSUzI1NiIsImtpZCI6IjM3QUZDM0YwODc0RUQ1MkI2Q0IzQ0RDNzQ4NkJENEQ3M0ZCNUZBQTgiLCJ0eXAiOiJhdCt" +
                    "qd3QiLCJ4NXQiOiJONl9EOElkTzFTdHNzODNIU0d2VTF6LTEtcWcifQ.eyJuYmYiOjE1OTA0ODI0MzAsImV4cCI6MTU5MDQ" +
                    "4NjAzMCwiaXNzIjoiaHR0cHM6Ly9wb3J0YWwuc21pbnQuaW8vIiwiYXVkIjoic21pbnRpby5mdWxsIiwiY2xpZW50X2lkIj" +
                    "oicmV3ZWdyb3VwIiwiY2xpZW50X2RldmVsb3Blcl91dWlkIjoiMiIsInN1YiI6ImQxMjdmYTkzLTc5ZjEtNDEwNS04MGY2L" +
                    "WY4YjUyZmIxNzgwNiIsImF1dGhfdGltZSI6MTU5MDQ3NjAyNSwiaWRwIjoibG9jYWwiLCJqdGkiOiJYNGRVQy02dlZUcGpy" +
                    "cktQZzQzRkJnIiwic2NvcGUiOlsib3BlbmlkIiwicHJvZmlsZSIsInNtaW50aW8uZnVsbCIsIm9mZmxpbmVfYWNjZXNzIl0" +
                    "sImFtciI6WyJwd2QiXX0.rbzvdFlpHlIKoiOch76znIv-GXsapXQ5ZC_W2NPy7k_V5bHW3a5QUIvaCbOvBETwYCSl-rtJkB" +
                    "gLqDp5J2qQ5SVMuH9WU8ExIipZZKHZfV3ZKYdieBG-IhcN9Csp8PBKkbVa2LOOjfVUzpb8JgluMiEs-bMCH8yM-rJ98Cnm_" +
                    "CfWMq8pucZIxghvmX3YTllZJ6LUghRY50nwszi1LpsjExPLHG3sdR7e7DY1bEr2Hpc8y7A22XcXovfO9X8hCKC1MtekZLdz" +
                    "oROMqWFDkg1BgAkBZGrWB7n4DT0xSu_BuD4w9gA1JzwXR_5mLcauFbKYQwB5tVlLXzaVpKqynfww3w"
            ).setIdentityToken(
                "eyJhbGciOiJSUzI1NiIsImtpZCI6IjM3QUZDM0YwODc0RUQ1MkI2Q0IzQ0RDNzQ4NkJENEQ3M0ZCNUZBQTgiLCJ0eXAiOiJKV1Q" +
                    "iLCJ4NXQiOiJONl9EOElkTzFTdHNzODNIU0d2VTF6LTEtcWcifQ.eyJuYmYiOjE1OTA0ODI0MzAsImV4cCI6MTU5MDQ4Mjc" +
                    "zMCwiaXNzIjoiaHR0cHM6Ly9wb3J0YWwuc21pbnQuaW8vIiwiYXVkIjoicmV3ZWdyb3VwIiwiaWF0IjoxNTkwNDgyNDMwLC" +
                    "JhdF9oYXNoIjoiREFOeDBkNFRadk95Y2taOFp1X01CUSIsInNfaGFzaCI6Im12ZFpIQ2lNR2V6VHlhMVZ3TnVBdFEiLCJza" +
                    "WQiOiJKWExoNzdUVjduaGMtY1MxWGRIQVpnIiwic3ViIjoiZDEyN2ZhOTMtNzlmMS00MTA1LTgwZjYtZjhiNTJmYjE3ODA2" +
                    "IiwiYXV0aF90aW1lIjoxNTkwNDc2MDI1LCJpZHAiOiJsb2NhbCIsImFtciI6WyJwd2QiXX0.IkG3pU92k32naDqfIvlhZqq" +
                    "aDDZJl04VQahBjuFlHVQ6RvNoo9jNnFJdE4Yur0Uje5XkKXJiezBkmn-rTdQhVTEeqBwUpoX01YCZrDLGowcRl0KF13AN0H" +
                    "Al4myziZb5LgKWBNZTeB4tEJxilFynwO0PCgMmKUDf-xAYbGpzLksQLALHN5ZhRejTp6zOM-Q4ixSjW0RydKLbnhcFq3WzP" +
                    "3jyY9K69oX28Tv8dpW99MOfFyfeVc1b9GIwYM83KvPS_X1TRms5RAAzjhR0ZUihaskYBmuCbGA2Ib6PvWevEMdI31ZCSOzk" +
                    "VhMuAgyx3WDmzVI1_UMQ6OtBwMPq67qKCg"
            ).setRefreshToken("No7ejw0T5y7RcENcRFqWcRcDeNtYZfToT_lM-rgJKWU")
            .setExpiration(OffsetDateTime.now().plus(600, ChronoUnit.SECONDS));

        final OffsetDateTime expiresAt = expectedTokenData.getExpiration();
        final String json = "{\n" +
            "    \"_identityToken\": \"" + expectedTokenData.getIdentityToken() + "\",\n" +
            "    \"_accessToken\": \"" + expectedTokenData.getAccessToken() + "\",\n" +
            "    \"token_type\": \"Bearer\",\n" +
            "    \"_refreshToken\": \"" + expectedTokenData.getRefreshToken() + "\",\n" +
            "    \"scope\": \"openid profile smintio.full offline_access\",\n" +
            "    \"_expirationDate\": {\n" +
            "        \"dateTime\": {\n" +
            "          \"date\": {\n" +
            "            \"year\": " + expiresAt.getYear() + ",\n" +
            "            \"month\": " + expiresAt.getMonthValue() + ",\n" +
            "            \"day\": " + expiresAt.getDayOfMonth() + "\n" +
            "          },\n" +
            "          \"time\": {\n" +
            "            \"hour\": " + expiresAt.getHour() + ",\n" +
            "            \"minute\": " + expiresAt.getMinute() + ",\n" +
            "            \"second\": " + expiresAt.getSecond() + ",\n" +
            "            \"nano\": " + expiresAt.getNano() + "\n" +
            "          }\n" +
            "        },\n" +
            "        \"offset\": {\n" +
            "          \"totalSeconds\": " + expiresAt.getOffset().getTotalSeconds() + "\n" +
            "        }\n" +
            "    }" +
            "}";

        IAuthTokenModel authTokenData = converter.decode(json);

        Assertions.assertNotNull(authTokenData, "Failed to convert JSON to token model!");
        Assertions.assertEquals(
            expectedTokenData.getIdentityToken(),
            authTokenData.getIdentityToken(),
            "Failed to convert JSON to token model - identity token unexpected!"
        );
        Assertions.assertEquals(
            expectedTokenData.getAccessToken(),
            authTokenData.getAccessToken(),
            "Failed to convert JSON to token model - access token unexpected!"
        );
        Assertions.assertEquals(
            expectedTokenData.getRefreshToken(),
            authTokenData.getRefreshToken(),
            "Failed to convert JSON to token model - access token unexpected!"
        );
        Assertions.assertNotNull(
            expectedTokenData.getExpiration(),
            "Failed to convert JSON to token model - expire time is missing!"
        );
        Assertions.assertEquals(
            expectedTokenData.getExpiration().toEpochSecond(),
            authTokenData.getExpiration().toEpochSecond(),
            "Failed to convert JSON to token model - expire time do not match!"
        );


        authTokenData = converter.decode(
            "{\n" +
                "    \"identityToken\": \"" + expectedTokenData.getIdentityToken() + "\",\n" +
                "    \"accessToken\": \"" + expectedTokenData.getAccessToken() + "\",\n" +
                "    \"token_type\": \"Bearer\",\n" +
                "    \"refreshToken\": \"" + expectedTokenData.getRefreshToken() + "\",\n" +
                "    \"scope\": \"openid profile smintio.full offline_access\"\n" +
                "}"
        );

        Assertions.assertNotNull(authTokenData, "Failed to convert JSON(2) to token model!");
        Assertions.assertEquals(
            expectedTokenData.getIdentityToken(),
            authTokenData.getIdentityToken(),
            "Failed to convert JSON(2) to token model - identity token unexpected!"
        );
        Assertions.assertEquals(
            expectedTokenData.getAccessToken(),
            authTokenData.getAccessToken(),
            "Failed to convert JSON(2) to token model - access token unexpected!"
        );
        Assertions.assertEquals(
            expectedTokenData.getRefreshToken(),
            authTokenData.getRefreshToken(),
            "Failed to convert JSON(2) to token model - access token unexpected!"
        );
    }


    @Test
    @DisplayName("Decode valid auth token JSON from HTTP OAuth response")
    public void decode_ValidJsonOfHttpResponse() throws Exception {

        final MyAuthTokenJsonConverter converter = new MyAuthTokenJsonConverter();
        final IAuthTokenModel expectedTokenData = new AuthTokenImpl()
            .setAccessToken(
                "eyJhbGciOiJSUzI1NiIsImtpZCI6IjM3QUZDM0YwODc0RUQ1MkI2Q0IzQ0RDNzQ4NkJENEQ3M0ZCNUZBQTgiLCJ0eXAiOiJhdCt" +
                    "qd3QiLCJ4NXQiOiJONl9EOElkTzFTdHNzODNIU0d2VTF6LTEtcWcifQ.eyJuYmYiOjE1OTA0ODI0MzAsImV4cCI6MTU5MDQ" +
                    "4NjAzMCwiaXNzIjoiaHR0cHM6Ly9wb3J0YWwuc21pbnQuaW8vIiwiYXVkIjoic21pbnRpby5mdWxsIiwiY2xpZW50X2lkIj" +
                    "oicmV3ZWdyb3VwIiwiY2xpZW50X2RldmVsb3Blcl91dWlkIjoiMiIsInN1YiI6ImQxMjdmYTkzLTc5ZjEtNDEwNS04MGY2L" +
                    "WY4YjUyZmIxNzgwNiIsImF1dGhfdGltZSI6MTU5MDQ3NjAyNSwiaWRwIjoibG9jYWwiLCJqdGkiOiJYNGRVQy02dlZUcGpy" +
                    "cktQZzQzRkJnIiwic2NvcGUiOlsib3BlbmlkIiwicHJvZmlsZSIsInNtaW50aW8uZnVsbCIsIm9mZmxpbmVfYWNjZXNzIl0" +
                    "sImFtciI6WyJwd2QiXX0.rbzvdFlpHlIKoiOch76znIv-GXsapXQ5ZC_W2NPy7k_V5bHW3a5QUIvaCbOvBETwYCSl-rtJkB" +
                    "gLqDp5J2qQ5SVMuH9WU8ExIipZZKHZfV3ZKYdieBG-IhcN9Csp8PBKkbVa2LOOjfVUzpb8JgluMiEs-bMCH8yM-rJ98Cnm_" +
                    "CfWMq8pucZIxghvmX3YTllZJ6LUghRY50nwszi1LpsjExPLHG3sdR7e7DY1bEr2Hpc8y7A22XcXovfO9X8hCKC1MtekZLdz" +
                    "oROMqWFDkg1BgAkBZGrWB7n4DT0xSu_BuD4w9gA1JzwXR_5mLcauFbKYQwB5tVlLXzaVpKqynfww3w"
            ).setIdentityToken(
                "eyJhbGciOiJSUzI1NiIsImtpZCI6IjM3QUZDM0YwODc0RUQ1MkI2Q0IzQ0RDNzQ4NkJENEQ3M0ZCNUZBQTgiLCJ0eXAiOiJKV1Q" +
                    "iLCJ4NXQiOiJONl9EOElkTzFTdHNzODNIU0d2VTF6LTEtcWcifQ.eyJuYmYiOjE1OTA0ODI0MzAsImV4cCI6MTU5MDQ4Mjc" +
                    "zMCwiaXNzIjoiaHR0cHM6Ly9wb3J0YWwuc21pbnQuaW8vIiwiYXVkIjoicmV3ZWdyb3VwIiwiaWF0IjoxNTkwNDgyNDMwLC" +
                    "JhdF9oYXNoIjoiREFOeDBkNFRadk95Y2taOFp1X01CUSIsInNfaGFzaCI6Im12ZFpIQ2lNR2V6VHlhMVZ3TnVBdFEiLCJza" +
                    "WQiOiJKWExoNzdUVjduaGMtY1MxWGRIQVpnIiwic3ViIjoiZDEyN2ZhOTMtNzlmMS00MTA1LTgwZjYtZjhiNTJmYjE3ODA2" +
                    "IiwiYXV0aF90aW1lIjoxNTkwNDc2MDI1LCJpZHAiOiJsb2NhbCIsImFtciI6WyJwd2QiXX0.IkG3pU92k32naDqfIvlhZqq" +
                    "aDDZJl04VQahBjuFlHVQ6RvNoo9jNnFJdE4Yur0Uje5XkKXJiezBkmn-rTdQhVTEeqBwUpoX01YCZrDLGowcRl0KF13AN0H" +
                    "Al4myziZb5LgKWBNZTeB4tEJxilFynwO0PCgMmKUDf-xAYbGpzLksQLALHN5ZhRejTp6zOM-Q4ixSjW0RydKLbnhcFq3WzP" +
                    "3jyY9K69oX28Tv8dpW99MOfFyfeVc1b9GIwYM83KvPS_X1TRms5RAAzjhR0ZUihaskYBmuCbGA2Ib6PvWevEMdI31ZCSOzk" +
                    "VhMuAgyx3WDmzVI1_UMQ6OtBwMPq67qKCg"
            ).setRefreshToken("No7ejw0T5y7RcENcRFqWcRcDeNtYZfToT_lM-rgJKWU");
        final IAuthTokenModel authTokenData = converter.decode(
            "{\n" +
                "    \"id_token\": \"" + expectedTokenData.getIdentityToken() + "\",\n" +
                "    \"access_token\": \"" + expectedTokenData.getAccessToken() + "\",\n" +
                "    \"token_type\": \"Bearer\",\n" +
                "    \"expires_in\": 3600,\n" +
                "    \"refresh_token\": \"" + expectedTokenData.getRefreshToken() + "\",\n" +
                "    \"scope\": \"openid profile smintio.full offline_access\"\n" +
                "}"
        );

        Assertions.assertNotNull(authTokenData, "Failed to convert JSON to token model!");
        Assertions.assertEquals(
            expectedTokenData.getIdentityToken(),
            authTokenData.getIdentityToken(),
            "Failed to convert JSON to token model - identity token unexpected!"
        );
        Assertions.assertEquals(
            expectedTokenData.getAccessToken(),
            authTokenData.getAccessToken(),
            "Failed to convert JSON to token model - access token unexpected!"
        );
        Assertions.assertEquals(
            expectedTokenData.getRefreshToken(),
            authTokenData.getRefreshToken(),
            "Failed to convert JSON to token model - access token unexpected!"
        );
        Assertions.assertEquals(
            true,
            authTokenData.isSuccess(),
            "Failed to convert JSON to token model - \"success\" is invalid!"
        );
    }


    @Test
    @DisplayName("Encode valid auth token JSON")
    public void encode_ValidJson() throws Exception {

        final MyAuthTokenJsonConverter converter = new MyAuthTokenJsonConverter();
        final IAuthTokenModel originalTokenData = new AuthTokenImpl()
            .setAccessToken(
                "eyJhbGciOiJSUzI1NiIsImtpZCI6IjM3QUZDM0YwODc0RUQ1MkI2Q0IzQ0RDNzQ4NkJENEQ3M0ZCNUZBQTgiLCJ0eXAiOiJhdCt" +
                    "qd3QiLCJ4NXQiOiJONl9EOElkTzFTdHNzODNIU0d2VTF6LTEtcWcifQ.eyJuYmYiOjE1OTA0ODI0MzAsImV4cCI6MTU5MDQ" +
                    "4NjAzMCwiaXNzIjoiaHR0cHM6Ly9wb3J0YWwuc21pbnQuaW8vIiwiYXVkIjoic21pbnRpby5mdWxsIiwiY2xpZW50X2lkIj" +
                    "oicmV3ZWdyb3VwIiwiY2xpZW50X2RldmVsb3Blcl91dWlkIjoiMiIsInN1YiI6ImQxMjdmYTkzLTc5ZjEtNDEwNS04MGY2L" +
                    "WY4YjUyZmIxNzgwNiIsImF1dGhfdGltZSI6MTU5MDQ3NjAyNSwiaWRwIjoibG9jYWwiLCJqdGkiOiJYNGRVQy02dlZUcGpy" +
                    "cktQZzQzRkJnIiwic2NvcGUiOlsib3BlbmlkIiwicHJvZmlsZSIsInNtaW50aW8uZnVsbCIsIm9mZmxpbmVfYWNjZXNzIl0" +
                    "sImFtciI6WyJwd2QiXX0.rbzvdFlpHlIKoiOch76znIv-GXsapXQ5ZC_W2NPy7k_V5bHW3a5QUIvaCbOvBETwYCSl-rtJkB" +
                    "gLqDp5J2qQ5SVMuH9WU8ExIipZZKHZfV3ZKYdieBG-IhcN9Csp8PBKkbVa2LOOjfVUzpb8JgluMiEs-bMCH8yM-rJ98Cnm_" +
                    "CfWMq8pucZIxghvmX3YTllZJ6LUghRY50nwszi1LpsjExPLHG3sdR7e7DY1bEr2Hpc8y7A22XcXovfO9X8hCKC1MtekZLdz" +
                    "oROMqWFDkg1BgAkBZGrWB7n4DT0xSu_BuD4w9gA1JzwXR_5mLcauFbKYQwB5tVlLXzaVpKqynfww3w"
            ).setIdentityToken(
                "eyJhbGciOiJSUzI1NiIsImtpZCI6IjM3QUZDM0YwODc0RUQ1MkI2Q0IzQ0RDNzQ4NkJENEQ3M0ZCNUZBQTgiLCJ0eXAiOiJKV1Q" +
                    "iLCJ4NXQiOiJONl9EOElkTzFTdHNzODNIU0d2VTF6LTEtcWcifQ.eyJuYmYiOjE1OTA0ODI0MzAsImV4cCI6MTU5MDQ4Mjc" +
                    "zMCwiaXNzIjoiaHR0cHM6Ly9wb3J0YWwuc21pbnQuaW8vIiwiYXVkIjoicmV3ZWdyb3VwIiwiaWF0IjoxNTkwNDgyNDMwLC" +
                    "JhdF9oYXNoIjoiREFOeDBkNFRadk95Y2taOFp1X01CUSIsInNfaGFzaCI6Im12ZFpIQ2lNR2V6VHlhMVZ3TnVBdFEiLCJza" +
                    "WQiOiJKWExoNzdUVjduaGMtY1MxWGRIQVpnIiwic3ViIjoiZDEyN2ZhOTMtNzlmMS00MTA1LTgwZjYtZjhiNTJmYjE3ODA2" +
                    "IiwiYXV0aF90aW1lIjoxNTkwNDc2MDI1LCJpZHAiOiJsb2NhbCIsImFtciI6WyJwd2QiXX0.IkG3pU92k32naDqfIvlhZqq" +
                    "aDDZJl04VQahBjuFlHVQ6RvNoo9jNnFJdE4Yur0Uje5XkKXJiezBkmn-rTdQhVTEeqBwUpoX01YCZrDLGowcRl0KF13AN0H" +
                    "Al4myziZb5LgKWBNZTeB4tEJxilFynwO0PCgMmKUDf-xAYbGpzLksQLALHN5ZhRejTp6zOM-Q4ixSjW0RydKLbnhcFq3WzP" +
                    "3jyY9K69oX28Tv8dpW99MOfFyfeVc1b9GIwYM83KvPS_X1TRms5RAAzjhR0ZUihaskYBmuCbGA2Ib6PvWevEMdI31ZCSOzk" +
                    "VhMuAgyx3WDmzVI1_UMQ6OtBwMPq67qKCg"
            ).setRefreshToken("No7ejw0T5y7RcENcRFqWcRcDeNtYZfToT_lM-rgJKWU");

        final String expectedJSON = "{\n" +
            "  \"isSuccess\": false,\n" +
            "  \"accessToken\": \"" + originalTokenData.getAccessToken() + "\",\n" +
            "  \"refreshToken\": \"" + originalTokenData.getRefreshToken() + "\",\n" +
            "  \"identityToken\": \"" + originalTokenData.getIdentityToken() + "\"\n" +
            "}";

        final String convertedJson = converter.encode(originalTokenData);

        Assertions.assertNotNull(convertedJson, "Failed to convert JSON to token model!");
        Assertions.assertEquals(
            expectedJSON,
            convertedJson,
            "Failed to convert from token model to JSON!"
        );
    }


    private class MyAuthTokenJsonConverter extends ModelDataJsonConverter<IAuthTokenModel, AuthTokenImpl> {

        public MyAuthTokenJsonConverter() {
            super(new SmintIoGsonProvider().get());
            this.setClassOfModel(AuthTokenImpl.class);
        }
    }
}


// CHECKSTYLE.ON: MultipleStringLiterals
// CHECKSTYLE.ON: MagicNumber
