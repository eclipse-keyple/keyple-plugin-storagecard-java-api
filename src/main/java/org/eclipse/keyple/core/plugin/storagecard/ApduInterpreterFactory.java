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
package org.eclipse.keyple.core.plugin.storagecard;

/**
 * Factory interface for creating instances of APDU interpreters.
 *
 * <p>The purpose of this interface is to provide an APDU interpreter factory designed to be
 * supplied to plugins capable of handling storage-type smart card processing functionalities.
 *
 * <p>This interface is designed to be implemented by card APDU interpreter libraries, enabling the
 * creation of specific APDU interpreter instances tailored to handle APDU commands and their
 * processing requirements.
 *
 * <p>Implementations must be instances of {@link
 * org.eclipse.keyple.core.plugin.storagecard.internal.spi.ApduInterpreterFactorySpi} providing the
 * logic for a concrete APDU interpreter.
 *
 * @since 1.0.0
 */
public interface ApduInterpreterFactory {}
