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
   * Reads data starting from a specified block address.
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
   * @param blockAddress The address of the first block to be read.
   * @param length The address of bytes to read, should be a multiple of the card's page size and
   *     must not exceed the maximum readable size for the specific card type.
   * @return A byte array containing the block data.
   * @throws IllegalArgumentException if {@code blockAddress} is out of range or {@code length} is
   *     negative, not compatible with the card's page structure, or exceeds the maximum readable
   *     size.
   * @throws Exception if the read operation fails or communication error occurs
   * @since 1.0.0
   */
  byte[] readBlock(int blockAddress, int length) throws Exception;

  /**
   * Writes data to a specified memory block on the card.
   *
   * <p>This method handles writing memory blocks according to the card's memory page structure. The
   * data length must exactly match the exchange size for the specific card type.
   *
   * @param blockAddress The address of the first block to be written.
   * @param data The byte array containing the data to write. The length must match exactly the
   *     card's page size or exchange size.
   * @throws IllegalArgumentException if {@code blockAddress} is out of range or if {@code data}
   *     length does not match the required exchange size for the card type.
   * @throws Exception if the write operation fails or communication error occurs
   * @since 1.0.0
   */
  void writeBlock(int blockAddress, byte[] data) throws Exception;

  /**
   * Loads a card-specific authentication key into the reader's memory.
   *
   * <p>This method stores a card key in either volatile (RAM) or non-volatile (EEPROM) memory of
   * the card reader. The key can be subsequently used by the {@link #generalAuthenticate(int, int,
   * int)} method to authenticate to the card. This command can be used for all kinds of contactless
   * cards.
   *
   * <p>Volatile memory provides temporary storage that is cleared when the reader loses power,
   * while non-volatile memory persists across power cycles. The availability of non-volatile memory
   * depends on the specific reader hardware.
   *
   * <p>The key structure and length depend on the card type. For example:
   *
   * <ul>
   *   <li>Mifare cards: 6-byte keys (Type A or Type B)
   *   <li>Other contactless cards: card-specific key formats
   * </ul>
   *
   * <p>The key number parameter identifies the storage location in the reader's memory. The valid
   * range and meaning of key numbers depend on the reader implementation and whether volatile or
   * non-volatile memory is used. Consult the reader documentation for specific key number
   * assignments.
   *
   * <p>Note that keys stored in memory cannot be read back for security reasons. Once loaded, they
   * can only be used for authentication operations.
   *
   * @param isVolatileMemory {@code true} to store the key in volatile memory (RAM), {@code false}
   *     to store in non-volatile memory (EEPROM).
   * @param keyNumber The key index identifying the storage location. Valid ranges depend on the
   *     reader implementation and memory type.
   * @param key A byte array containing the card-specific key value. Must not be null. The required
   *     length depends on the card type and authentication algorithm.
   * @throws IllegalArgumentException if {@code key} is null, if the key length is not valid for the
   *     card type, or if {@code keyNumber} is not in the valid range for the reader and memory
   *     type.
   * @throws Exception if the load operation fails, if the specified memory type is not available on
   *     the reader hardware, or if a communication error occurs.
   * @see #generalAuthenticate(int, int, int)
   * @since 1.1.0
   */
  void loadKey(boolean isVolatileMemory, int keyNumber, byte[] key) throws Exception;

  /**
   * Performs authentication to a contactless card using a previously loaded key.
   *
   * <p>This method authenticates to a specific memory location on the card using a key that was
   * previously loaded into the reader's memory via the {@link #loadKey(boolean, int, byte[])}
   * method. Successful authentication is typically required before performing read or write
   * operations on protected memory areas.
   *
   * <p>The authentication process establishes a secure session between the reader and the card. The
   * block address represents the block number or starting byte number of the card to be
   * authenticated, depending on the card type.
   *
   * <p>The key type parameter is card-specific and indicates which type of key to use for
   * authentication. Examples:
   *
   * <ul>
   *   <li>Mifare cards: 0x60 (KEY_A) or 0x61 (KEY_B)
   *   <li>Other contactless cards: card-specific key type values
   * </ul>
   *
   * <p>The key number parameter references a key previously loaded via {@link #loadKey(boolean,
   * int, byte[])} and identifies which stored key to use for this authentication operation.
   *
   * @param blockAddress The block number or starting byte number on the card where authentication
   *     is to be performed. Valid range depends on the card type and memory structure.
   * @param keyType The type of key to use for authentication. The valid values are card-specific
   *     (e.g., 0x60 or 0x61 for Mifare cards).
   * @param keyNumber The index of the previously loaded key to use for authentication. Must
   *     reference a key that was loaded via {@link #loadKey(boolean, int, byte[])}.
   * @throws IllegalArgumentException if {@code blockAddress} is out of valid range for the card
   *     type, if {@code keyType} is not supported by the card, or if {@code keyNumber} does not
   *     reference a valid loaded key.
   * @throws Exception if the authentication fails (incorrect key or access denied), if the
   *     referenced key was not previously loaded, if the card does not support authentication, or
   *     if a communication error occurs.
   * @see #loadKey(boolean, int, byte[])
   * @since 1.1.0
   */
  void generalAuthenticate(int blockAddress, int keyType, int keyNumber) throws Exception;
}
