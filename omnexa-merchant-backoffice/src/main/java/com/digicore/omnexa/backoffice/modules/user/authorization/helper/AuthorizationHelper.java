/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.backoffice.modules.user.authorization.helper;

import static com.digicore.omnexa.common.lib.constant.message.MessageConstant.CONFLICT;
import static com.digicore.omnexa.common.lib.constant.system.SystemConstant.*;

import com.digicore.omnexa.backoffice.modules.user.authorization.data.model.BackOfficeUserPermission;
import com.digicore.omnexa.backoffice.modules.user.authorization.data.repository.BackOfficeUserPermissionRepository;
import com.digicore.omnexa.common.lib.exception.OmnexaException;
import com.digicore.omnexa.common.lib.properties.MessagePropertyConfig;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jul-22(Tue)-2025
 */
@Component
public class AuthorizationHelper {
  private AuthorizationHelper() {}

  public static Set<BackOfficeUserPermission> retrieveSelectedPermissions(
      Set<String> selectedPermissions,
      MessagePropertyConfig messagePropertyConfig,
      BackOfficeUserPermissionRepository permissionRepository) {
    Set<String> effectivePermissions = new HashSet<>(selectedPermissions);
    boolean treatRequestAdded = false;

    for (String permission : selectedPermissions) {
      if (permission.startsWith(APPROVE_PERMISSION_PREFIX)) {
        String basePermission = permission.substring(APPROVE_PERMISSION_PREFIX.length());

        if (selectedPermissions.contains(basePermission)) {
          throw new OmnexaException(
              messagePropertyConfig.getRoleMessage(CONFLICT, SYSTEM_DEFAULT_CONFLICT_ERROR),
              HttpStatus.BAD_REQUEST);
        }

        if (!selectedPermissions.contains(SYSTEM_TREAT_REQUEST_PERMISSION) && !treatRequestAdded) {
          effectivePermissions.add(SYSTEM_TREAT_REQUEST_PERMISSION);
          treatRequestAdded = true;
        }
      }
    }

    List<BackOfficeUserPermission> permissions =
        permissionRepository.findByNameIn(effectivePermissions);

    return new HashSet<>(permissions);
  }
}
