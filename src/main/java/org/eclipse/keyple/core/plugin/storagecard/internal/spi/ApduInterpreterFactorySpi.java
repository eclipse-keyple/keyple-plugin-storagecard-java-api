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
