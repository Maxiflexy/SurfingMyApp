/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.merchant.backoffice.modules.user.dto.response;

import com.digicore.omnexa.merchant.backoffice.modules.user.data.enums.BackOfficeUserStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Response DTO for user signup operation.
 *
 * @author System
 * @createdOn Jan-26(Sun)-2025
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Back office user signup response")
public class SignupResponse {

    @Schema(description = "Unique user identifier")
    private String userId;

    @Schema(description = "User email address")
    private String email;

    @Schema(description = "User full name")
    private String fullName;

    @Schema(description = "Account status")
    private BackOfficeUserStatus status;

    @Schema(description = "Success message")
    private String message;
}
