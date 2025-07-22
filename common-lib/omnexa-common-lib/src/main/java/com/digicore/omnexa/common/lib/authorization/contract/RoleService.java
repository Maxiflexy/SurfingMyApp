/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.common.lib.authorization.contract;

import com.digicore.omnexa.common.lib.authorization.dto.request.RoleCreationDTO;

/**
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jul-21(Mon)-2025
 */
public interface RoleService extends AuthorizationService {
  void createRole(RoleCreationDTO request);
}
