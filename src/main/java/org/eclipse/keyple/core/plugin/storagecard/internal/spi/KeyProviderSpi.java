/* **************************************************************************************
 * Copyright (c) 2025 Calypso Networks Association https://calypsonet.org/
 *
 * See the NOTICE file(s) distributed with this work for additional information
 * regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the terms of the
 * MIT License which is available at https://opensource.org/licenses/MIT
 *
 * SPDX-License-Identifier: MIT
 ************************************************************************************** */
package org.eclipse.keyple.core.plugin.storagecard.internal.spi;

/**
 * Interface for providing authentication keys dynamically to plugins during card operations.
 *
 * <p>This interface enables the "External Vault" security pattern, where cryptographic keys are
 * stored securely by the application (e.g., in a Hardware Security Module, KeyStore, secure cloud
 * service, or encrypted file) and provided to the plugin strictly on-demand, rather than being
 * pre-loaded into reader memory.
 *
 * <p>This approach offers several security benefits:
 *
 * <ul>
 *   <li>Keys remain in secure storage and are only retrieved when needed for authentication
 *   <li>Reduces the attack surface by minimizing the time keys spend in plugin/reader memory
 *   <li>Allows centralized key management across multiple readers and plugins
 *   <li>Supports dynamic key rotation and revocation
 * </ul>
 *
 * <p>The plugin will typically call this interface when performing authentication operations (e.g.,
 * via {@code generalAuthenticate}) if a key has not been previously loaded into the reader's memory
 * via {@code loadKey}.
 *
 * <p><strong>Security Consideration:</strong> The returned key byte array should be cleared (zeroed
 * out) by the plugin after use to prevent keys from lingering in memory.
 *
 * <p>To be implemented by the application.
 *
 * @since 1.1.0
 */
@FunctionalInterface
public interface KeyProviderSpi {

  /**
   * Retrieves the authentication key associated with the specified key index.
   *
   * <p>This method is called by the plugin when a key is required for authentication but has not
   * been previously loaded into the reader's memory. The key index corresponds to the logical key
   * number used in authentication commands.
   *
   * <p>The key structure and length depend on the card type and authentication algorithm.
   *
   * <p>If the requested key is not available or cannot be retrieved, this method should return
   * {@code null}. The plugin will then fail the authentication operation with an appropriate error.
   *
   * @param keyIndex The index of the key requested by the authentication operation. The meaning and
   *     valid range of key indices depend on the specific plugin and card type implementation.
   * @return A byte array containing the raw key bytes, or {@code null} if the key is not available,
   *     not found, or access is denied.
   * @since 1.1.0
   */
  byte[] getKey(int keyIndex);
}
