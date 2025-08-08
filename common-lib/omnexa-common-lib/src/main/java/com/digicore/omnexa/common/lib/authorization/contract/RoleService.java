/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.common.lib.authorization.contract;

import com.digicore.omnexa.common.lib.util.PaginatedResponse;

/**
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jul-21(Mon)-2025
 */
public interface RoleService extends AuthorizationService {
  void createRole(AuthorizationRequest request);

  default void deleteRole(String roleName) {}

  default void enableRole(String roleName) {}

  default void disableRole(String roleName) {}

  default void editRole(AuthorizationRequest request) {}

  default AuthorizationResponse retrieveRole(String roleName, String identifier) {
    return null;
  }

  default PaginatedResponse<AuthorizationResponse> retrieveAllRole(
      Integer pageNumber, Integer pageSize, String identifier) {
    return null;
  }
}
