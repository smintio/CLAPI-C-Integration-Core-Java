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

/**
 * Denotes capabilities and features the synchronization "downstream" target supports.
 *
 * <p>
 * Such a synchronization target might be a DAM (Digital Asset Management) and there are a lot of them. So implementing
 * a synchronization feature to cover them all is a challenging task. Hence a lot of information about the target is
 * needed to get that right.
 * </p>
 */
public enum SyncTargetCapabilitiesEnum {

    /**
     * The target supports multiple languages, so provide all languages configured in the settings.
     *
     * <p>
     * the configuration data, which languages to import, is still respected. Only the configured languages are passed
     * to the synchronization functions. So, indicating support for multiple languages is only a matter of checking the
     * configuration setting. In case multiple languages are not supported but are configured, an exception is thrown at
     * the start of the synchronization process.
     * </p>
     */
    MultiLanguageEnum,

    /**
     * Some assets might contain various parts and thus form a so called {@code compound asset}.
     *
     * <p>
     * Example of such a compound assets might be a video and its textual transcript. At the moment, only a few DAM
     * support compound assets, so this is vital to know.
     * </p>
     */
    CompoundAssetsEnum,

    /**
     * indicates that the target supports updates of binaries.
     *
     * <p>
     * Usually binary updates are avoided. But sometimes there is a need for update the binary files (images, videos,
     * audio files, texts) in case the upstream provider has updated the file. In case the sync target does not support
     * updates of binary files, an error is indicated during the sync process once such an event happens and a binary
     * needs update.
     * </p>
     */
    BinaryUpdatesEnum
}

