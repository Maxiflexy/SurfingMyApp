/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.backoffice.modules.user.authorization.helper;

import static com.digicore.omnexa.common.lib.constant.message.MessageConstant.CONFLICT;

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

  public static Set<BackOfficeUserPermission> retrieveSelectedPermissions(
      Set<String> selectedPermissions,
      MessagePropertyConfig messagePropertyConfig,
      BackOfficeUserPermissionRepository permissionRepository) {
    Set<String> effectivePermissions = new HashSet<>(selectedPermissions);
    boolean treatRequestAdded = false;

    for (String permission : selectedPermissions) {
      if (permission.startsWith("approve-")) {
        String basePermission = permission.substring("approve-".length());

        if (selectedPermissions.contains(basePermission)) {
          throw new OmnexaException(
              messagePropertyConfig.getRoleMessage(CONFLICT), HttpStatus.BAD_REQUEST);
        }

        if (!selectedPermissions.contains("treat-requests") && !treatRequestAdded) {
          effectivePermissions.add("treat-requests");
          treatRequestAdded = true;
        }
      }
    }

    List<BackOfficeUserPermission> permissions =
        permissionRepository.findByNameIn(effectivePermissions);

    return new HashSet<>(permissions);
  }
}
