/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.backoffice.modules.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * Request DTO for inviting a new back office user.
 *
 * <p>Contains the minimum information needed to send an invitation
 * to a potential back office user.
 *
 * @author Onyekachi Ejemba
 * @createdOn Jul-08(Tue)-2025
 */
@Getter
@Setter
@Schema(description = "Back office user invitation request")
public class InviteUserRequest {

    @NotBlank(message = "{backoffice.email.required}")
    @Email(message = "{backoffice.email.invalid}")
    @Schema(description = "Email address of the user to invite", example = "john.doe@example.com")
    private String email;

    @NotBlank(message = "{backoffice.firstName.required}")
    @Schema(description = "First name of the user (for email personalization)", example = "John")
    private String firstName;
}
