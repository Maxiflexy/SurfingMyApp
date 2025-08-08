/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.common.lib.approval.converter;

import com.digicore.omnexa.common.lib.approval.enums.ApprovalRequestStatus;
import com.digicore.omnexa.common.lib.converter.StringEnumConverter;
import jakarta.persistence.Converter;

/*
 * @author Oluwatobi Ogunwuyi
 * @createdOn Mar-03(Mon)-2025
 */

@Converter
public class ApprovalRequestStatusConverter extends StringEnumConverter<ApprovalRequestStatus> {
  public ApprovalRequestStatusConverter() {
    super(ApprovalRequestStatus.class);
  }
}
