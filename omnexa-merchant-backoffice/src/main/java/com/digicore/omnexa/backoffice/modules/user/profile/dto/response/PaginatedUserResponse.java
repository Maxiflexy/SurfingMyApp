package com.digicore.omnexa.backoffice.modules.user.profile.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Paginated user list response")
/**
 * @author Onyekachi Ejemba
 * @createdOn Jul-23(Wed)-2025
 */
public class PaginatedUserResponse {

    @Schema(description = "List of users in current page")
    private List<BackOfficeUserProfileDTO> content;

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