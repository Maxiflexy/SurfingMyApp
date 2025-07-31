/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.merchant.modules.authorization.helper;

import static com.digicore.omnexa.common.lib.constant.message.MessageConstant.CONFLICT;
import static com.digicore.omnexa.common.lib.constant.message.MessagePlaceHolderConstant.ROLE_NAME;
import static com.digicore.omnexa.common.lib.util.RequestUtil.getValueFromAccessToken;

import com.digicore.omnexa.common.lib.exception.OmnexaException;
import com.digicore.omnexa.common.lib.properties.MessagePropertyConfig;
import com.digicore.omnexa.merchant.modules.authorization.data.model.MerchantUserPermission;
import com.digicore.omnexa.merchant.modules.authorization.data.repository.MerchantUserPermissionRepository;
import com.digicore.omnexa.merchant.modules.authorization.data.repository.MerchantUserRoleRepository;
import com.digicore.omnexa.merchant.modules.profile.data.model.MerchantProfile;
import com.digicore.omnexa.merchant.modules.profile.data.repository.MerchantProfileRepository;
import jakarta.persistence.EntityManager;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jul-22(Tue)-2025
 */
@Component
@RequiredArgsConstructor
public class AuthorizationHelper {
  private static final Logger log = LoggerFactory.getLogger(AuthorizationHelper.class);

  @Getter private final MerchantUserPermissionRepository permissionRepository;
  @Getter private final MerchantUserRoleRepository merchantUserRoleRepository;
  @Getter private final MerchantProfileRepository merchantProfileRepository;
  @Getter private final MessagePropertyConfig messagePropertyConfig;
  private final EntityManager entityManager;
  private static final String MERCHANT_ID = "merchantId";
  public static final String MERCHANT_ROLE = "CUSTODIAN";

  public Set<MerchantUserPermission> retrieveSelectedPermissions(Set<String> selectedPermissions) {
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

    List<MerchantUserPermission> permissions =
        permissionRepository.findByNameIn(effectivePermissions);

    return new HashSet<>(permissions);
  }

  public MerchantProfile getMerchantProfileByReference() {
    Long merchantProfileId =
        merchantProfileRepository.findIdByMerchantId(retrieveMerchantId()).orElseThrow();
    return entityManager.getReference(MerchantProfile.class, merchantProfileId);
  }

  public void validateRoleName(String roleName) {
    String conflictMessage = getConflictMessage(roleName);

    if (MERCHANT_ROLE.equalsIgnoreCase(roleName)) {
      log.warn(conflictMessage);
      throw new OmnexaException(conflictMessage, HttpStatus.CONFLICT);
    }

    boolean roleExists =
        merchantUserRoleRepository.existsByNameAndDeletedAndMerchantProfileMerchantId(
            roleName, false, retrieveMerchantId());

    if (roleExists) {
      log.warn(conflictMessage);
      throw new OmnexaException(conflictMessage, HttpStatus.CONFLICT);
    }
  }

  private String getConflictMessage(String roleName) {
    return messagePropertyConfig.getRoleMessage(CONFLICT).replace(ROLE_NAME, roleName);
  }

  public static String retrieveMerchantId() {
    return getValueFromAccessToken(MERCHANT_ID);
  }
}
