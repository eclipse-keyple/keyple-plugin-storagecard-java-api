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

/**
 * Interface defining an APDU interpreter for processing commands sent to a card.
 *
 * <p>Its implementation is provided by {@link ApduInterpreterFactorySpi}.
 *
 * <p>Upon calling {@code processApdu}, implementations determine how to invoke the appropriate
 * methods of {@link CommandProcessorApi} based on the card type.
 *
 * <p>For standard ISO 7816-4 APDUs, the interpreter directly calls {@link
 * CommandProcessorApi#transmitIsoApdu(byte[])}. For storage-type cards, the interpreter translates
 * the APDU into specific read/write operations using {@link CommandProcessorApi#getUID()}, {@link
 * CommandProcessorApi#readBlock(int, int)} and {@link CommandProcessorApi#writeBlock(int, byte[])}.
 *
 * <h3>Status Words</h3>
 *
 * <p>While this method declares {@code throws Exception} for interface compatibility,
 * implementations are <strong>recommended</strong> to convert all errors to ISO 7816-4 compliant
 * status words for APDU protocol conformity:
 *
 * <table border="1">
 *   <tr><th>Status Word</th><th>Code</th><th>Usage</th></tr>
 *   <tr><td>Success</td><td>0x9000</td><td>Successful execution</td></tr>
 *   <tr><td>Wrong length</td><td>0x6700</td><td>Invalid data length</td></tr>
 *   <tr><td>Incorrect P1-P2</td><td>0x6A86</td><td>Invalid parameters</td></tr>
 *   <tr><td>Conditions not satisfied</td><td>0x6985</td><td>Preconditions not met</td></tr>
 *   <tr><td>Key type not known</td><td>0x6986</td><td>Invalid key type (PC/SC)</td></tr>
 *   <tr><td>Key number not valid</td><td>0x6988</td><td>Invalid key number (PC/SC)</td></tr>
 *   <tr><td>Authentication failed</td><td>0x6300</td><td>Wrong authentication key</td></tr>
 *   <tr><td>Technical problem</td><td>0x6581</td><td>Hardware/communication error</td></tr>
 *   <tr><td>INS not supported</td><td>0x6D00</td><td>Unknown instruction</td></tr>
 * </table>
 *
 * <p>Implementations that convert exceptions to status words should log the full exception details
 * (including stack traces) for diagnostic purposes while returning appropriate status words to the
 * client.
 *
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
