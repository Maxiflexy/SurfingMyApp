/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.common.lib.approval.rule.service;

/*
 * @author Oluwatobi Ogunwuyi
 * @createdOn Mar-06(Thu)-2025
 */

public interface ApprovalRuleNotificationService<T> {
  void sendNotification(T notificationRequest);
}
