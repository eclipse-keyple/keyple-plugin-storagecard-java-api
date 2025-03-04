/* **************************************************************************************
 * Copyright (c) 2025 Calypso Networks Association https://calypsonet.org/
 *
 * See the NOTICE file(s) distributed with this work for additional information
 * regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License 2.0 which is available at http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
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
