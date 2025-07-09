/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.backoffice.modules.user.dto.response;

import com.digicore.omnexa.backoffice.modules.user.data.enums.BackOfficeUserStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

/**
 * DTO for user summary in list responses.
 *
 * @author Onyekachi Ejemba
 * @createdOn Jul-09(Wed)-2025
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "User summary information")
public class UserSummary {

    @Schema(description = "Unique user identifier")
    private String userId;

    @Schema(description = "User's full name")
    private String username;

    @Schema(description = "User's email address")
    private String email;

    @Schema(description = "Date when user was onboarded")
    private LocalDateTime onboard_date;

    @Schema(description = "Current user status")
    private BackOfficeUserStatus user_status;
}