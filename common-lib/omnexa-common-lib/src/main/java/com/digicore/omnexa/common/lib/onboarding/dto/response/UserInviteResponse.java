/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.common.lib.onboarding.dto.response;

import com.digicore.omnexa.common.lib.onboarding.contract.OnboardingResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * Response DTO for user invitation operation.
 *
 * @author Onyekachi Ejemba
 * @createdOn Jul-08(Tue)-2025
 */
@Getter
@Setter
@Schema(description = "User invitation response")
public class UserInviteResponse implements OnboardingResponse {
  private String firstName;
  private String email;
}
