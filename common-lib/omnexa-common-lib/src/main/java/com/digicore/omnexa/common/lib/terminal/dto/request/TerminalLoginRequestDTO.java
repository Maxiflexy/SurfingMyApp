/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.common.lib.terminal.dto.request;

import com.digicore.omnexa.common.lib.authentication.dto.request.LoginRequestDTO;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Oluwatobi Ogunwuyi
 * @createdOn Aug-11(Mon)-2025
 */
@Getter
@Setter
public class TerminalLoginRequestDTO extends LoginRequestDTO {
  private int pin;
  private String serialId;
}
