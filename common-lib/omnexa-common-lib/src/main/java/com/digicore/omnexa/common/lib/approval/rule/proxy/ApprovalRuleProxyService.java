/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.common.lib.approval.rule.proxy;

/*
 * @author Oluwatobi Ogunwuyi
 * @createdOn Feb-10(Mon)-2025
 */
public interface ApprovalRuleProxyService {
  default Object createApprovalRule(Object initialData, Object updateData, Object... files) {
    return null;
  }
}
