/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.common.lib.onboarding.dto.request;

import com.digicore.omnexa.common.lib.onboarding.contract.OnboardingRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * Request DTO for inviting a new user.
 *
 * <p>Contains the minimum information needed to send an invitation to a potential user.
 *
 * @author Onyekachi Ejemba
 * @createdOn Jul-08(Tue)-2025
 */
@Getter
@Setter
@Schema(description = "User invitation request")
public class UserInviteRequest implements OnboardingRequest {

  @NotBlank(message = "{user.email.required}")
  @Email(message = "{user.email.invalid}")
  @Schema(description = "Email address of the user to invite", example = "john.doe@example.com")
  private String email;

  @NotBlank(message = "{user.firstName.required}")
  @Schema(description = "First name of the user (for email personalization)", example = "John")
  private String firstName;

  @NotBlank(message = "{user.role.required}")
  @Schema(description = "Role of the user", example = "ADMIN")
  private String role;
}
