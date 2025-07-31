/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.common.lib.authentication.contract;

import com.digicore.omnexa.common.lib.exception.OmnexaException;

/**
 * Service interface for password reset workflows.
 *
 * <p>This interface defines the contract for handling password reset operations, including
 * initiating a reset and completing it with a new password.
 *
 * <p>Implementations of this interface should manage the logic for validating reset tokens,
 * updating passwords, and providing appropriate responses or exceptions in case of errors.
 *
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jun-24(Tue)-2025
 */
public interface PasswordResetService {

  /**
   * Completes the password reset process using a new password and reset token.
   *
   * <p>This method validates the provided reset request and updates the user's password if the
   * reset token is valid. If the token is invalid or expired, an {@link OmnexaException} is thrown.
   *
   * @param request The password reset request containing the reset token and new password.
   * @return A {@link PasswordResetResponse} containing the status or message of the reset
   *     operation.
   * @throws OmnexaException If the reset token is invalid or expired.
   */
  PasswordResetResponse resetPassword(PasswordResetRequest request);
}
