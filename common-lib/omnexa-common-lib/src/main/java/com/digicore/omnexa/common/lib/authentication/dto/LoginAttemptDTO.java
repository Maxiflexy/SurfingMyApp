/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.common.lib.authentication.dto;

import com.digicore.omnexa.common.lib.enums.AuthenticationType;
import java.time.ZonedDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jul-29(Tue)-2025
 */
@Getter
@Setter
@Builder
public class LoginAttemptDTO {
  private String username;
  private String name;
  private String role;
  private ZonedDateTime automatedUnlockTime;
  private ZonedDateTime lastLoginAt;
  private ZonedDateTime lastFailedLoginAt;
  private int failedLoginAttempts;
  private String lastLoginIp;
  private String deviceInfo;
  private boolean loginLocked;
  private AuthenticationType authenticationType;
}
