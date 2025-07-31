/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.common.lib.authentication.dto.response;

import com.digicore.omnexa.common.lib.authentication.contract.AuthenticationResponse;
import java.util.Map;
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
public class LoginResponseDTO implements AuthenticationResponse {
  private String accessToken;
  private Map<String, Object> additionalInformation;
}
