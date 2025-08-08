/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.common.lib.approval.dto;

import com.digicore.omnexa.common.lib.approval.enums.ApprovalStatus;
import lombok.*;

/*
 * @author Oluwatobi Ogunwuyi
 * @createdOn Feb-10(Mon)-2025
 */

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApprovalDecisionDTO {
  private String requestId;
  private ApprovalStatus status;
  private String reason;
  private String requestType;
  private String authenticationKey;
}
