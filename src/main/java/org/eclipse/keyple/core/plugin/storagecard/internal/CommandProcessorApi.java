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
package org.eclipse.keyple.core.plugin.storagecard.internal;

/**
 * Interface defining a command processor for interacting with a storage card.
 *
 * <p>To be implemented by the plugin.
 *
 * @since 1.0.0
 */
public interface CommandProcessorApi {
  /**
   * Transmits a standard APDU command to the card.
   *
   * @param apdu The APDU ISO 7816-4 command.
   * @return The response from the card including both data and status words.
   * @throws IllegalArgumentException if apdu is null or invalid
   * @throws Exception if transmission fails or communication error occurs
   * @since 1.0.0
   */
  byte[] transmitIsoApdu(byte[] apdu) throws Exception;

  /**
   * Retrieves the Unique Identifier (UID) of the card.
   *
   * <p>This method obtains the UID which uniquely identifies the card. The UID length varies
   * depending on the card type.
   *
   * <p>If the UID is not available for the current card type, an empty byte array will be returned.
   *
   * @return A byte array containing the card's UID, or an empty array if the UID is not available.
   * @throws Exception if the retrieval operation fails or communication error occurs
   * @since 1.0.0
   */
  byte[] getUID() throws Exception;

  /**
   * Reads data starting from a specified block number.
   *
   * <p>This method handles reading memory blocks according to the card's memory page structure. The
   * requested length should be a multiple of the card's page size and must not exceed the maximum
   * readable size in a single exchange with the specific card type.
   *
   * <p>For example, Mifare Ultralight cards have 4-byte pages, but each read command returns 4
   * pages (16 bytes) at once. ST25/SRT512 cards read one page at a time.
   *
   * <p>If the requested {@code length} is shorter than the actual exchange size with the card, the
   * response will be truncated accordingly.
   *
   * @param blockNumber The number of the first block to be read.
   * @param length The number of bytes to read, should be a multiple of the card's page size and
   *     must not exceed the maximum readable size for the specific card type.
   * @return A byte array containing the block data.
   * @throws IllegalArgumentException if {@code blockNumber} is out of range or {@code length} is
   *     negative, not compatible with the card's page structure, or exceeds the maximum readable
   *     size.
   * @throws Exception if the read operation fails or communication error occurs
   * @since 1.0.0
   */
  byte[] readBlock(int blockNumber, int length) throws Exception;

  /**
   * Writes data to a specified memory block on the card.
   *
   * <p>This method handles writing memory blocks according to the card's memory page structure. The
   * data length must exactly match the exchange size for the specific card type.
   *
   * @param blockNumber The number of the first block to be written.
   * @param data The byte array containing the data to write. The length must match exactly the
   *     card's page size or exchange size.
   * @throws IllegalArgumentException if {@code blockNumber} is out of range or if {@code data}
   *     length does not match the required exchange size for the card type.
   * @throws Exception if the write operation fails or communication error occurs
   * @since 1.0.0
   */
  void writeBlock(int blockNumber, byte[] data) throws Exception;
}
