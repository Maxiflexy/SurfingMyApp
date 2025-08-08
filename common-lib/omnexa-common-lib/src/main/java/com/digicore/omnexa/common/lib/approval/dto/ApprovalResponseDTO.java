/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.common.lib.approval.dto;

import com.digicore.omnexa.common.lib.approval.enums.ApprovalRequestStatus;
import lombok.*;

/*
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jan-26(Sun)-2025
 */

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ApprovalResponseDTO {
  private String description;
  private ApprovalRequestStatus requestStatus;
}
