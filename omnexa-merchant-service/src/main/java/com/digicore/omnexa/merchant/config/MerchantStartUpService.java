/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.merchant.config;

import static com.digicore.omnexa.common.lib.constant.system.SystemConstant.SYSTEM_MERCHANT_ROLE_DESCRIPTION;
import static com.digicore.omnexa.common.lib.constant.system.SystemConstant.SYSTEM_MERCHANT_ROLE_NAME;

import com.digicore.omnexa.common.lib.authorization.contract.AuthorizationRequest;
import com.digicore.omnexa.common.lib.authorization.contract.PermissionService;
import com.digicore.omnexa.common.lib.authorization.dto.request.PermissionCreationDTO;
import com.digicore.omnexa.common.lib.backgound.startup.StartupService;
import com.digicore.omnexa.common.lib.security.SecurityPropertyConfig;
import com.digicore.omnexa.common.lib.util.RequestUtil;
import com.digicore.omnexa.merchant.modules.authorization.data.model.MerchantUserRole;
import com.digicore.omnexa.merchant.modules.authorization.data.repository.MerchantUserPermissionRepository;
import com.digicore.omnexa.merchant.modules.authorization.data.repository.MerchantUserRoleRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jul-18(Fri)-2025
 */
@Configuration
@RequiredArgsConstructor
@Slf4j
public class MerchantStartUpService implements StartupService {
  private final SecurityPropertyConfig securityPropertyConfig;
  private final PermissionService merchantUserPermissionService;
  private final MerchantUserRoleRepository merchantUserRoleRepository;
  private final MerchantUserPermissionRepository merchantUserPermissionRepository;

  @Override
  @EventListener(ContextRefreshedEvent.class)
  @Order(1)
  public void updateSystemPermissions() {
    File file = getSystemFile(securityPropertyConfig.getSystemDefinedPermissions());
    Set<PermissionCreationDTO> newAuthorities;
    try {
      newAuthorities = RequestUtil.getObjectMapper().readValue(file, new TypeReference<>() {});

      merchantUserPermissionService.createPermission(
          newAuthorities.stream().map(p -> (AuthorizationRequest) p).collect(Collectors.toSet()));
    } catch (IOException ignored) {
      log.trace("<<< no update required >>>");
    }
  }

  @Override
  @EventListener(ContextRefreshedEvent.class)
  @Order(2)
  @Transactional
  public void updateSystemRoles() {
    MerchantUserRole userRole =
        merchantUserRoleRepository
            .findFirstByName(SYSTEM_MERCHANT_ROLE_NAME)
            .orElse(new MerchantUserRole());
    userRole.setActive(true);
    userRole.setName(SYSTEM_MERCHANT_ROLE_NAME);
    userRole.setDescription(SYSTEM_MERCHANT_ROLE_DESCRIPTION);
    userRole.setPermissions(new HashSet<>(merchantUserPermissionRepository.findAll()));
    merchantUserRoleRepository.save(userRole);
  }

  private File getSystemFile(String filePath) {
    Path path = Paths.get(filePath);
    return path.toFile();
  }
}
