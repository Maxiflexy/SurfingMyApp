/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.backoffice.modules.user.management.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Base class for pagination requests.
 *
 * @author Onyekachi Ejemba
 * @createdOn Jul-29(Tue)-2025
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPaginationRequest {

    @Schema(description = "Page number (1-based, default: 1)", example = "1")
    private Integer pageNumber;

    @Schema(description = "Page size (max 16, default: 16)", example = "16")
    private Integer pageSize;
}