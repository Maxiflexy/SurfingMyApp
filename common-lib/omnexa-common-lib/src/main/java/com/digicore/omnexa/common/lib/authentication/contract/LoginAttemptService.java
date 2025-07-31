/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.common.lib.authentication.contract;

import com.digicore.omnexa.common.lib.authentication.dto.LoginAttemptDTO;

/**
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jul-29(Tue)-2025
 */
public interface LoginAttemptService {
  default void verifyLoginAccess(LoginAttemptDTO loginAttemptDTO, boolean credentialMatches) {}
}
