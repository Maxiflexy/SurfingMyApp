/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.common.lib.onboarding.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * Request DTO for user signup.
 *
 * <p>Contains all required information for a user to complete their registration after receiving an
 * invitation.
 *
 * @author Onyekachi Ejemba
 * @createdOn Jul-08(Tue)-2025
 */
@Getter
@Setter
@Schema(description = "User signup request")
public class SignupRequest extends UserInviteRequest {
  @NotBlank(message = "{user.lastName.required}")
  @Schema(description = "Last name of the user", example = "Doe")
  private String lastName;

  @NotBlank(message = "{user.password.required}")
  @Size(min = 8, message = "{user.password.minLength}")
  @Schema(description = "Password", example = "C00k1ng@0723")
  private String password;
}
