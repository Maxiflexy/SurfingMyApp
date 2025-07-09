/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.backoffice.user.dto.response;

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
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Back office user invitation response")
public class InviteUserResponse {

    @Schema(description = "Email address of the invited user")
    private String email;

    @Schema(description = "User ID generated for the invited user")
    private String userId;

    @Schema(description = "Status message of the invitation")
    private String message;
}