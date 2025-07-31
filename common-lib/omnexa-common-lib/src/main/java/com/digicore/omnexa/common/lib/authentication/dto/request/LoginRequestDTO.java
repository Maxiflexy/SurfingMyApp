/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.common.lib.authentication.dto.request;

import com.digicore.omnexa.common.lib.authentication.contract.AuthenticationRequest;
import com.digicore.omnexa.common.lib.enums.AuthenticationType;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jul-29(Tue)-2025
 */
@Getter
@Setter
public class LoginRequestDTO implements AuthenticationRequest {
  private String username;
  private String password;
  private AuthenticationType authenticationType;
}
