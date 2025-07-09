/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.backoffice.modules.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * Request DTO for getting paginated user list.
 *
 * @author Onyekachi Ejemba
 * @createdOn Jul-09(Wed)-2025
 */
@Getter
@Setter
@Schema(description = "User list request parameters")
public class UserListRequest {

    @Schema(description = "Page number (1-based, default: 1)", example = "1")
    private Integer pageNumber = 1;

    @Schema(description = "Page size (max 16, default: 16)", example = "16")
    private Integer pageSize = 16;

    @Schema(description = "Search term to filter users by name or email", example = "john.doe")
    private String search;

    @Schema(description = "Filter by user status", example = "ACTIVE")
    private String status;
}