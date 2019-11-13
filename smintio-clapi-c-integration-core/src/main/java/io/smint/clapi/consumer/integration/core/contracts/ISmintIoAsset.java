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
import java.util.Map;

import io.smint.clapi.consumer.generated.models.LicensePurchaseTransactionStateEnum;


/**
 * Data for an asset to synchronize.
 */
public interface ISmintIoAsset {

    String getContentElementUuid();

    String getLicensePurchaseTransactionUuid();

    String getCartPurchaseTransactionUuid();

    LicensePurchaseTransactionStateEnum getState();

    String getProvider();

    String getContentType();

    Map<String, String> getName();

    Map<String, String> getDescription();

    Map<String, String[]> getKeywords();

    String getCategory();

    ISmintIoReleaseDetails getReleaseDetails();

    Map<String, String> getCopyrightNotices();

    String getProjectUuid();

    Map<String, String> getProjectName();

    String getCollectionUuid();

    Map<String, String> getCollectionName();

    String getLicenseeUuid();

    String getLicenseeName();

    String getLicenseType();

    Map<String, String> getLicenseText();

    ISmintIoLicenseOptions[] getLicenseOptions();

    ISmintIoLicenseTerm[] getLicenseTerms();

    ISmintIoDownloadConstraints getDownloadConstraints();

    /**
     * Defines whether the content is for editorial use only.
     *
     * <p>
     * In case no such information is available, then {@code null} is returned. This is the case if the upstream asset
     * provider did not define this value and did not provide any defaults. Hence it is impossible to determine, whether
     * the asset is for editorial use only or not. If the synchronization target does not require a value, none should
     * be set in these cases. If a valid value (decision) is required, it is up to the sync target to implement a
     * reasonable default to be on the safe side of the legal challenge.
     * </p>
     *
     * @return {@link Boolean#TRUE} in case the asset is for editorial use only and {@link Boolean#FALSE} otherwise.
     *         However, {@code null} is returned if no such information is available. Hence neither is applicable.
     */
    Boolean isEditorialUse();


    /**
     * Defines whether the content has some license restrictions applied.
     *
     * <p>
     * The kind of license restrictions applicable for this asset can be retrieved with {@link #getLicenseTerms()}.
     * </p>
     *
     * @return {@code true} in case any licensing restriction has been applied to the asset or not.
     */
    boolean hasLicenseTerms();


    ISmintIoBinary[] getBinaries();

    String getSmintIoUrl();

    OffsetDateTime getPurchasedAt();

    OffsetDateTime getCreatedAt();

    OffsetDateTime getLastUpdatedAt();
}
