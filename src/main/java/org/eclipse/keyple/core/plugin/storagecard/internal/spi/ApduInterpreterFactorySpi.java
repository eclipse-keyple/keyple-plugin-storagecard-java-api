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

import org.eclipse.keyple.core.plugin.storagecard.ApduInterpreterFactory;

/**
 * Factory interface for creating instances of {@link ApduInterpreterSpi}.
 *
 * <p>To be implemented by the storage card APDU interpreter.
 *
 * @since 1.0.0
 */
public interface ApduInterpreterFactorySpi extends ApduInterpreterFactory {

  /**
   * Creates a new instance of an APDU interpreter.
   *
   * @return A new instance of {@link ApduInterpreterSpi}.
   * @since 1.0.0
   */
  ApduInterpreterSpi createApduInterpreter();
}
