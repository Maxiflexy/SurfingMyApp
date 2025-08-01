/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.backoffice.config;

import static com.digicore.omnexa.common.lib.constant.system.SystemConstant.*;

import com.digicore.omnexa.backoffice.modules.user.authentication.data.model.BackOfficeUserAuthProfile;
import com.digicore.omnexa.backoffice.modules.user.authentication.data.repository.BackOfficeUserAuthProfileRepository;
import com.digicore.omnexa.backoffice.modules.user.authorization.data.model.BackOfficeUserPermission;
import com.digicore.omnexa.backoffice.modules.user.authorization.data.model.BackOfficeUserRole;
import com.digicore.omnexa.backoffice.modules.user.authorization.data.repository.BackOfficeUserPermissionRepository;
import com.digicore.omnexa.backoffice.modules.user.authorization.data.repository.BackOfficeUserRoleRepository;
import com.digicore.omnexa.backoffice.modules.user.profile.data.model.BackOfficeUserProfile;
import com.digicore.omnexa.backoffice.modules.user.profile.data.repository.BackOfficeUserProfileRepository;
import com.digicore.omnexa.common.lib.authorization.contract.AuthorizationRequest;
import com.digicore.omnexa.common.lib.authorization.contract.PermissionService;
import com.digicore.omnexa.common.lib.authorization.dto.request.PermissionCreationDTO;
import com.digicore.omnexa.common.lib.backgound.startup.StartupService;
import com.digicore.omnexa.common.lib.enums.ProfileStatus;
import com.digicore.omnexa.common.lib.enums.ProfileVerificationStatus;
import com.digicore.omnexa.common.lib.security.SecurityPropertyConfig;
import com.digicore.omnexa.common.lib.util.RequestUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import jakarta.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jul-18(Fri)-2025
 */
@Configuration
@RequiredArgsConstructor
@Slf4j
public class BackOfficeStartUpService implements StartupService {
  private final SecurityPropertyConfig securityPropertyConfig;
  private final PermissionService backOfficeUserPermissionService;
  private final BackOfficeUserRoleRepository backOfficeUserRoleRepository;
  private final BackOfficeUserPermissionRepository backOfficeUserPermissionRepository;
  private final BackOfficeUserProfileRepository backOfficeUserProfileRepository;
  private final BackOfficeUserAuthProfileRepository backOfficeUserAuthProfileRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  @EventListener(ContextRefreshedEvent.class)
  @Order(1)
  public void updateSystemPermissions() {
    File file = getSystemFile(securityPropertyConfig.getSystemDefinedPermissions());
    Set<PermissionCreationDTO> newAuthorities;
    try {
      newAuthorities = RequestUtil.getObjectMapper().readValue(file, new TypeReference<>() {});

      backOfficeUserPermissionService.createPermission(
          newAuthorities.stream().map(p -> (AuthorizationRequest) p).collect(Collectors.toSet()));
    } catch (IOException ignored) {
      log.trace("<<< no update required >>>");
    }
  }

  @Override
  @EventListener(ContextRefreshedEvent.class)
  @Order(2)
  public void updateSystemRoles() {
    Permissions permissions = getPermissions();

    BackOfficeUserRole initiatorRole =
        buildRole(
            SYSTEM_INITIATOR_ROLE_NAME,
            SYSTEM_INITIATOR_ROLE_DESCRIPTION,
            permissions.makerPermissions());

    BackOfficeUserRole authorizerRole =
        buildRole(
            SYSTEM_AUTHORIZER_ROLE_NAME,
            SYSTEM_AUTHORIZER_ROLE_DESCRIPTION,
            permissions.checkerPermissions());

    backOfficeUserRoleRepository.saveAll(List.of(initiatorRole, authorizerRole));
  }

  @Override
  @EventListener(ContextRefreshedEvent.class)
  @Order(3)
  @Transactional(rollbackOn = Exception.class)
  public void updateSystemUsers() {
    BackOfficeUserProfile initiatorProfile =
        buildUserProfile(
            SYSTEM_INITIATOR_EMAIL_NAME, "Initiator", "Initiator", SYSTEM_INITIATOR_ROLE_NAME);

    BackOfficeUserProfile authorizerProfile =
        buildUserProfile(
            SYSTEM_AUTHORIZER_EMAIL_NAME, "Authorizer", "Authorizer", SYSTEM_AUTHORIZER_ROLE_NAME);

    buildUserAuthProfile(initiatorProfile, SYSTEM_INITIATOR_DEFAULT_PASSWORD);
    buildUserAuthProfile(authorizerProfile, SYSTEM_AUTHORIZER_DEFAULT_PASSWORD);
  }

  private File getSystemFile(String filePath) {
    Path path = Paths.get(filePath);
    return path.toFile();
  }

  private BackOfficeUserRole buildRole(
      String name, String description, Set<BackOfficeUserPermission> permissions) {
    BackOfficeUserRole role =
        backOfficeUserRoleRepository.findFirstByName(name).orElse(new BackOfficeUserRole());

    role.setActive(true);
    role.setName(name);
    role.setDescription(description);
    if (role.getId() == null) backOfficeUserRoleRepository.save(role);

    role.setPermissions(new HashSet<>(permissions));

    return role;
  }

  private BackOfficeUserProfile buildUserProfile(
      String email, String firstName, String lastName, String role) {
    BackOfficeUserProfile profile =
        backOfficeUserProfileRepository.findFirstByEmail(email).orElse(new BackOfficeUserProfile());

    profile.setEmail(email);
    profile.setFirstName(firstName);
    profile.setLastName(lastName);
    profile.setProfileStatus(ProfileStatus.ACTIVE);
    profile.setProfileVerificationStatus(ProfileVerificationStatus.VERIFIED);
    profile.setRole(role);

    return backOfficeUserProfileRepository.save(profile);
  }

  private void buildUserAuthProfile(BackOfficeUserProfile backOfficeUserProfile, String password) {
    BackOfficeUserAuthProfile profile =
        backOfficeUserAuthProfileRepository
            .findFirstByUsername(backOfficeUserProfile.getEmail())
            .orElse(new BackOfficeUserAuthProfile());

    profile.setUsername(backOfficeUserProfile.getEmail());
    profile.setPassword(passwordEncoder.encode(password));
    profile.setBackOfficeUserProfile(backOfficeUserProfile);

    backOfficeUserAuthProfileRepository.save(profile);
  }

  private record Permissions(
      Set<BackOfficeUserPermission> makerPermissions,
      Set<BackOfficeUserPermission> checkerPermissions) {}

  private Permissions getPermissions() {
    List<BackOfficeUserPermission> allPermissions = backOfficeUserPermissionRepository.findAll();

    Set<BackOfficeUserPermission> viewerPermissions =
        allPermissions.stream()
            .filter(p -> p.getName().startsWith(VIEW_PERMISSION_PREFIX))
            .collect(Collectors.toSet());

    Set<BackOfficeUserPermission> makerPermissions =
        allPermissions.stream()
            .filter(p -> !p.getName().startsWith(APPROVE_PERMISSION_PREFIX))
            .filter(p -> !SYSTEM_TREAT_REQUEST_PERMISSION.equals(p.getName()))
            .collect(Collectors.toSet());

    Set<BackOfficeUserPermission> checkerPermissions =
        allPermissions.stream()
            .filter(
                p ->
                    p.getName().startsWith(APPROVE_PERMISSION_PREFIX)
                        || SYSTEM_TREAT_REQUEST_PERMISSION.equals(p.getName()))
            .collect(Collectors.toSet());
    checkerPermissions.addAll(viewerPermissions);

    return new Permissions(makerPermissions, checkerPermissions);
  }
}
