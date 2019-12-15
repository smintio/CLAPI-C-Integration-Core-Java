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
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO
// THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
// TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
// SOFTWARE.
//
// SPDX-License-Identifier: MIT

package io.smint.clapi.consumer.integration.core.target;

import java.util.Arrays;


/**
 * Synchronization targets indicate their capabilities by providing an instance of this interface.
 *
 * <p>
 * Based on the supported capabilities, the preparation of data to be synchronized is adapted in order to match the
 * supported features. Eg: in case the synchronization target does not support multiple languages but only a single one,
 * the meta data to write into the target downstream <em>Digital Asset Management</em> system is reduced to a single
 * language. The language to import can be configured using the interface ...
 * </p>
 */
public interface ISyncTargetCapabilities {

    /**
     * Provides a list if capabilities enumeration values indicating supported features of the sync target.
     *
     * <p>
     * Every synchronization target is different and relies on the capabilities of the underlying DAM infrastructure.
     * The list of enumerations provide a means to query the capabilities and restrictions of this sync target.
     * </p>
     *
     * <p>
     * The support flag {@link SyncTargetCapabilitiesEnum#MultiLanguageEnum} indicating multi-language support is
     * evaluated very early in the synchronization process. Right at the start the sync configuration is checked,
     * whether it matches the capabilities regarding multi-language support. If they do not comply, the synchronization
     * task is not started but aborted immediately.
     * </p>
     *
     * @return The array of capability enumeration indicates all features the sync target is capable of supporting. If
     *         none of the enumerable features are supported, either return {@code null} value.
     */
    SyncTargetCapabilitiesEnum[] getCapabilities();


    /**
     * Indicates whether multi languages are supported by this sync target implementation.
     *
     * <p>
     * The value should be calculated based on the provided {@link #getCapabilities()}.
     * </p>
     *
     * @return {@code true} in case the target system is capable of handling translations into various languages.
     */
    default boolean isMultiLanguageSupported() {
        final SyncTargetCapabilitiesEnum[] capabilities = this.getCapabilities();
        if (capabilities == null || capabilities.length == 0) {
            return false;
        }

        return Arrays.asList(capabilities).contains(SyncTargetCapabilitiesEnum.MultiLanguageEnum);
    }


    /**
     * Indicates whether compound assets are supported by this sync target implementation.
     *
     * <p>
     * The value should be calculated based on the provided {@link #getCapabilities()}.
     * </p>
     *
     * @return {@code true} in case the target system is capable of handling compound assets.
     */
    default boolean isCompoundAssetsSupported() {
        final SyncTargetCapabilitiesEnum[] capabilities = this.getCapabilities();
        if (capabilities == null || capabilities.length == 0) {
            return false;
        }

        return Arrays.asList(capabilities).contains(SyncTargetCapabilitiesEnum.CompoundAssetsEnum);
    }


    /**
     * Indicates whether binary updates are supported by this sync target implementation.
     *
     * <p>
     * The value should be calculated based on the provided {@link #getCapabilities()}.
     * </p>
     *
     * @return {@code true} in case the target system is capable of updating assets (binaries) besides its meta data.
     */
    default boolean isBinaryUpdatesSupported() {
        final SyncTargetCapabilitiesEnum[] capabilities = this.getCapabilities();
        if (capabilities == null || capabilities.length == 0) {
            return false;
        }

        return Arrays.asList(capabilities).contains(SyncTargetCapabilitiesEnum.BinaryUpdatesEnum);
    }

}
