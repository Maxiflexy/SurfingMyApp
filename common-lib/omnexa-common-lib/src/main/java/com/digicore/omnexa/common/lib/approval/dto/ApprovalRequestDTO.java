/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.common.lib.approval.dto;

import com.digicore.omnexa.common.lib.approval.enums.ApprovalRequestStatus;
import java.time.LocalDateTime;
import lombok.*;

/*
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jan-26(Sun)-2025
 */

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApprovalRequestDTO {
  private Long id;
  private String requesterName;
  private String approverName;
  private String description;
  private String dataToUpdate;
  private String initialData;
  private String requesterUsername;
  private String approvalUsername;
  private boolean approved;
  private ApprovalRequestStatus status;
  private String approvalRequestType;
  private LocalDateTime createdOn;
  private LocalDateTime treatDate;
  private String errorTrace;
  private Object[] documentsUploaded;
  private String cardProgramName = "N/A";
}
