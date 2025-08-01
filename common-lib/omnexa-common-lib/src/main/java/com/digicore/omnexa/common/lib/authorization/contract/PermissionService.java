/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.common.lib.authorization.contract;

import java.util.Set;

/**
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jul-21(Mon)-2025
 */
public interface PermissionService extends AuthorizationService {
  void createPermission(Set<AuthorizationRequest> newPermissions);

  Set<AuthorizationResponse> getAllPermissions();
}
