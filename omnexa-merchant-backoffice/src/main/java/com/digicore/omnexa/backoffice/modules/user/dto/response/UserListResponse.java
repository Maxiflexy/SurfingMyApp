/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.backoffice.modules.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

/**
 * Response DTO for paginated user list.
 *
 * @author Onyekachi Ejemba
 * @createdOn Jul-09(Wed)-2025
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Paginated user list response")
public class UserListResponse {

    @Schema(description = "List of users in current page")
    private List<UserSummary> content;

    @Schema(description = "Current page number (1-based)")
    private int currentPage;

    @Schema(description = "Total number of items")
    private long totalItems;

    @Schema(description = "Total number of pages")
    private int totalPages;

    @Schema(description = "Whether this is the first page")
    private boolean isFirstPage;

    @Schema(description = "Whether this is the last page")
    private boolean isLastPage;
}