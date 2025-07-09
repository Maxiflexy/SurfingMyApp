/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omxena.backoffice.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * Request DTO for back office user signup.
 *
 * <p>Contains all required information for a user to complete
 * their registration after receiving an invitation.
 *
 * @author Onyekachi Ejemba
 * @createdOn Jul-08(Tue)-2025
 */
@Getter
@Setter
@Schema(description = "Back office user signup request")
public class SignupRequest {

    @NotBlank(message = "{backoffice.firstName.required}")
    @Schema(description = "First name of the user", example = "John")
    private String firstName;

    @NotBlank(message = "{backoffice.lastName.required}")
    @Schema(description = "Last name of the user", example = "Doe")
    private String lastName;

    @NotBlank(message = "{backoffice.email.required}")
    @Email(message = "{backoffice.email.invalid}")
    @Schema(description = "Email address of the user", example = "john.doe@example.com")
    private String email;

    @NotBlank(message = "{backoffice.password.required}")
    @Size(min = 8, message = "{backoffice.password.minLength}")
    @Schema(description = "Password (min 8 chars, 1 uppercase, 1 number, 1 special char)", example = "SecureP@ss123")
    private String password;

    @NotBlank(message = "{backoffice.confirmPassword.required}")
    @Schema(description = "Password confirmation", example = "SecureP@ss123")
    private String confirmPassword;
}