/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.common.lib.util;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.*;

/**
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jul-30(Wed)-2025
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Paginated list response")
public class PaginatedResponse<T> {

  @Schema(description = "List of contents in current page")
  private List<T> content;

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
