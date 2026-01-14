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

import org.eclipse.keyple.core.plugin.storagecard.internal.CommandProcessorApi;
import org.eclipse.keyple.core.plugin.storagecard.internal.KeyStorageType;

/**
 * Interface defining an APDU interpreter for processing commands sent to storage cards.
 *
 * <p>Its implementation is provided by {@link ApduInterpreterFactorySpi}.
 *
 * <p>Upon calling {@code processApdu}, implementations determine how to invoke the appropriate
 * methods of {@link CommandProcessorApi} based on the APDU class byte (CLA) and instruction (INS).
 *
 * <h3>APDU Processing Strategy</h3>
 *
 * <p>The interpreter follows a two-pathway processing model:
 *
 * <ul>
 *   <li><strong>Standard ISO 7816-4 APDUs</strong> (CLA != 0xFF): Directly transmitted via {@link
 *       CommandProcessorApi#transmitIsoApdu(byte[])} without interpretation.
 *   <li><strong>Storage Card APDUs</strong> (CLA = 0xFF): Interpreted and translated into
 *       CommandProcessorApi method calls based on the instruction byte:
 *       <ul>
 *         <li><code>INS 0xCA</code> → {@link CommandProcessorApi#getUID()} - Get card UID
 *         <li><code>INS 0xB0</code> → {@link CommandProcessorApi#readBlock(int, int)} - Read binary
 *             data
 *         <li><code>INS 0xD6</code> → {@link CommandProcessorApi#writeBlock(int, byte[])} - Write
 *             binary data
 *         <li><code>INS 0x82</code> → {@link CommandProcessorApi#loadKey(KeyStorageType, int,
 *             byte[])} - Load authentication key
 *         <li><code>INS 0x86</code> → {@link CommandProcessorApi#generalAuthenticate(int, int,
 *             int)} - Authenticate with key
 *       </ul>
 * </ul>
 *
 * <h3>Error Handling Strategy</h3>
 *
 * <p>This interface uses a <strong>delegation-based validation</strong> approach:
 *
 * <ul>
 *   <li><strong>Validation</strong> is delegated to {@link CommandProcessorApi}, which throws typed
 *       exceptions for invalid parameters, lengths, or security violations.
 *   <li><strong>Exceptions propagate</strong> to the caller unless they represent normal protocol
 *       conditions (e.g., authentication failure, unsupported instruction).
 *   <li><strong>Status words</strong> are returned only for protocol-level conditions, not
 *       validation errors.
 * </ul>
 *
 * <h3>Status Words Usage</h3>
 *
 * <p>Implementations return ISO 7816-4 / PC/SC compliant status words for the following conditions:
 *
 * <table border="1">
 *   <caption>Status Words returned by implementations</caption>
 *   <tr><th>Status Word</th><th>Code</th><th>Usage</th></tr>
 *   <tr><td>Success</td><td>0x9000</td><td>Successful execution</td></tr>
 *   <tr><td>Security status not satisfied</td><td>0x6982</td><td>Authentication failed (PC/SC
 *   compliant)</td></tr>
 *   <tr><td>INS not supported</td><td>0x6D00</td><td>Unknown instruction code</td></tr>
 * </table>
 *
 * <h3>Exception Propagation</h3>
 *
 * <p>The following error conditions result in exceptions thrown by {@link CommandProcessorApi}:
 *
 * <ul>
 *   <li><strong>Invalid parameters</strong> (P1/P2 out of range, invalid key number)
 *   <li><strong>Invalid data length</strong> (wrong key length, APDU length mismatch)
 *   <li><strong>Invalid data format</strong> (wrong version byte, invalid key type)
 *   <li><strong>Hardware/communication errors</strong> (card not responding, transmission failure)
 * </ul>
 *
 * <p>Callers should catch and handle these exceptions appropriately for their context (e.g.,
 * converting to HTTP status codes, logging for diagnostics, or wrapping in domain-specific
 * exceptions).
 *
 * <h3>PC/SC Compliance</h3>
 *
 * <p>Storage card commands (CLA=0xFF) follow the PC/SC v2.01.09 specification:
 *
 * <ul>
 *   <li>LOAD KEY: <code>FF 82 [P1] [P2] 06 [6-byte key]</code>
 *   <li>GENERAL AUTHENTICATE: <code>FF 86 00 00 05 [version][addr-MSB][addr-LSB][key-type][key-num]
 *       </code>
 *   <li>GET DATA (UID): <code>FF CA 00 00 [Le]</code>
 *   <li>READ BINARY: <code>FF B0 [P1] [P2] [Le]</code>
 *   <li>UPDATE BINARY: <code>FF D6 [P1] [P2] [Lc] [data]</code>
 * </ul>
 *
 * @see CommandProcessorApi
 * @see ApduInterpreterFactorySpi
 * @since 1.0.0
 */
public interface ApduInterpreterSpi {

  /**
   * Sets the command processor responsible for handling APDU execution.
   *
   * @param commandProcessor The {@link CommandProcessorApi} instance used for APDU communication.
   * @since 1.0.0
   */
  void setCommandProcessor(CommandProcessorApi commandProcessor);

  /**
   * Processes an APDU command by invoking the corresponding methods of {@link CommandProcessorApi}
   * based on the card type.
   *
   * <p>If the card supports standard ISO 7816-4 APDUs, this method calls {@link
   * CommandProcessorApi#transmitIsoApdu(byte[])}. Otherwise, it interprets the APDU and executes
   * the required read or write operations through {@link CommandProcessorApi#readBlock(int, int)}
   * or {@link CommandProcessorApi#writeBlock(int, byte[])}.
   *
   * @param apdu The APDU command as a byte array.
   * @return The response as a byte array.
   * @throws IllegalArgumentException if the APDU is null or invalid.
   * @throws Exception if the transmission fails or a communication error occurs.
   * @since 1.0.0
   */
  byte[] processApdu(byte[] apdu) throws Exception;
}
