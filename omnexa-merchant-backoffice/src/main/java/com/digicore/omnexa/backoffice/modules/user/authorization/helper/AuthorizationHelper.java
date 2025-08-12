/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.backoffice.modules.user.authorization.helper;

import static com.digicore.omnexa.common.lib.constant.message.MessageConstant.*;
import static com.digicore.omnexa.common.lib.constant.message.MessagePlaceHolderConstant.ROLE_NAME;
import static com.digicore.omnexa.common.lib.constant.system.SystemConstant.*;

import com.digicore.omnexa.backoffice.modules.user.authorization.data.model.BackOfficeUserPermission;
import com.digicore.omnexa.backoffice.modules.user.authorization.data.model.BackOfficeUserRole;
import com.digicore.omnexa.backoffice.modules.user.authorization.data.repository.BackOfficeUserPermissionRepository;
import com.digicore.omnexa.backoffice.modules.user.authorization.data.repository.BackOfficeUserRoleRepository;
import com.digicore.omnexa.common.lib.authorization.dto.response.PermissionDTO;
import com.digicore.omnexa.common.lib.authorization.dto.response.RoleDTO;
import com.digicore.omnexa.common.lib.exception.OmnexaException;
import com.digicore.omnexa.common.lib.properties.MessagePropertyConfig;
import com.digicore.omnexa.common.lib.util.BeanUtilWrapper;
import com.digicore.omnexa.common.lib.util.RequestUtil;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jul-22(Tue)-2025
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class AuthorizationHelper {
  @Getter private final BackOfficeUserRoleRepository backOfficeUserRoleRepository;
  @Getter private final BackOfficeUserPermissionRepository backOfficeUserPermissionRepository;
  @Getter private final MessagePropertyConfig messagePropertyConfig;

  /** Set of system reserved role names that cannot be modified or deleted. */
  private static final Set<String> SYSTEM_RESERVED_ROLES =
      Set.of(SYSTEM_MERCHANT_ROLE_NAME, SYSTEM_AUTHORIZER_ROLE_NAME, SYSTEM_INITIATOR_ROLE_NAME);

  /**
   * Finds and validates that a role exists and can be operated on.
   *
   * @param roleId the ID of the role to find
   * @return the found role
   * @throws OmnexaException if role is not found
   */
  //  public BackOfficeUserRole findRoleById(Long roleId) {
  //    log.debug("Finding role with ID: {}", roleId);
  //
  //    return backOfficeUserRoleRepository
  //        .findById(roleId)
  //        .orElseThrow(
  //            () -> {
  //              String errorMessage =
  //                  messagePropertyConfig
  //                      .getRoleMessage(NOT_FOUND, SYSTEM_DEFAULT_NOT_FOUND_ERROR)
  //                      .replace(ROLE_NAME, "Role with ID: " + roleId);
  //              log.error("Role not found with ID: {}", roleId);
  //              return new OmnexaException(errorMessage, HttpStatus.NOT_FOUND);
  //            });
  //  }

  /**
   * Validates that a role can be activated.
   *
   * @param role the role to validate for activation
   * @throws OmnexaException if the role cannot be activated
   */
  public void validateRoleCanBeActivated(BackOfficeUserRole role) {
    log.debug("Validating if role can be activated: {}", role.getName());

    if (role.isActive()) {
      String errorMessage =
          messagePropertyConfig
              .getRoleMessage(CONFLICT, SYSTEM_DEFAULT_CONFLICT_ERROR)
              .replace(ROLE_NAME, role.getName() + " (Role is already active)");
      log.error("Attempted to activate already active role: {}", role.getName());
      throw new OmnexaException(errorMessage, HttpStatus.BAD_REQUEST);
    }

    log.debug("Role {} can be activated", role.getName());
  }

  /**
   * Validates that a role can be deactivated.
   *
   * @param role the role to validate for deactivation
   * @throws OmnexaException if the role cannot be deactivated
   */
  public void validateRoleCanBeDeactivated(BackOfficeUserRole role) {
    log.debug("Validating if role can be deactivated: {}", role.getName());

    if (!role.isActive()) {
      String errorMessage =
          messagePropertyConfig
              .getRoleMessage(CONFLICT, SYSTEM_DEFAULT_CONFLICT_ERROR)
              .replace(ROLE_NAME, role.getName() + " (Role is already inactive)");
      log.error("Attempted to deactivate already inactive role: {}", role.getName());
      throw new OmnexaException(errorMessage, HttpStatus.BAD_REQUEST);
    }

    log.debug("Role {} can be deactivated", role.getName());
  }

  /**
   * Checks if a role name is a system reserved role.
   *
   * @param roleName the role name to check
   * @return true if the role name is system reserved, false otherwise
   */
  public boolean isSystemReservedRole(String roleName) {
    if (roleName == null || roleName.trim().isEmpty()) {
      return false;
    }

    String normalizedRoleName = roleName.trim().toUpperCase();
    return SYSTEM_RESERVED_ROLES.contains(normalizedRoleName);
  }

  /**
   * Gets the set of system reserved role names.
   *
   * @return immutable set of system reserved role names
   */
  public Set<String> getSystemReservedRoles() {
    return Set.copyOf(SYSTEM_RESERVED_ROLES);
  }

  public void validateRolePermissions(Set<String> permissions) {
    // Fetch valid permission names from DB
    List<String> validPermissions =
        backOfficeUserPermissionRepository.findByNameIn(permissions).stream()
            .map(BackOfficeUserPermission::getName)
            .toList();

    // Identify invalid ones
    List<String> invalidPermissions =
        permissions.stream().filter(p -> !validPermissions.contains(p)).toList();

    if (!invalidPermissions.isEmpty()) {
      String errorMessage = "Invalid permission(s): " + String.join(", ", invalidPermissions);
      throw new OmnexaException(errorMessage, HttpStatus.BAD_REQUEST);
    }
  }

  /**
   * Validates a role name for creation against all validation rules.
   *
   * <p>This method performs comprehensive role name validation for creation including: - Null/empty
   * checks - System reserved role conflicts - Duplicate role name checks
   *
   * @param roleName the role name to validate
   * @throws OmnexaException if validation fails
   */
  public void validateRoleName(String roleName) {
    validateRoleNameNotEmpty(roleName);
    validateNotSystemReservedRole(roleName);
    validateRoleUniqueness(roleName);
  }

  public void validateRoleNameForUpdate(String roleName) {
    validateRoleNameNotEmpty(roleName);
    validateNotSystemReservedRole(roleName);
  }

  /**
   * Validates that the role name is not null or empty.
   *
   * @param roleName the role name to validate
   * @throws OmnexaException if the role name is null or empty
   */
  private void validateRoleNameNotEmpty(String roleName) {
    if (RequestUtil.nullOrEmpty(roleName)) {
      String errorMessage =
          messagePropertyConfig
              .getRoleMessage(REQUIRED, SYSTEM_DEFAULT_REQUIRED_FIELD_MISSING_ERROR)
              .replace(ROLE_NAME, "Role name");
      log.error("Role name validation failed: Role name is required");
      throw new OmnexaException(errorMessage, HttpStatus.BAD_REQUEST);
    }

    String trimmedRoleName = roleName.trim();
    if (trimmedRoleName.isEmpty()) {
      String errorMessage =
          messagePropertyConfig
              .getRoleMessage(REQUIRED, SYSTEM_DEFAULT_REQUIRED_FIELD_MISSING_ERROR)
              .replace(ROLE_NAME, "Role name");
      log.error("Role name validation failed: Role name cannot be empty after trimming");
      throw new OmnexaException(errorMessage, HttpStatus.BAD_REQUEST);
    }
  }

  /**
   * Validates that the role name is not a system reserved role.
   *
   * @param roleName the role name to validate
   * @throws OmnexaException if the role name is reserved
   */
  private void validateNotSystemReservedRole(String roleName) {
    if (isSystemReservedRole(roleName)) {
      String errorMessage =
          messagePropertyConfig
              .getRoleMessage(CONFLICT, SYSTEM_DEFAULT_CONFLICT_ERROR)
              .replace(ROLE_NAME, roleName + " (System reserved role)");
      log.error("Role name validation failed: {} is a system reserved role", roleName);
      throw new OmnexaException(errorMessage, HttpStatus.BAD_REQUEST);
    }
  }

  /**
   * Validates that the role name doesn't already exist in the database.
   *
   * @param roleName the role name to check for uniqueness
   * @throws OmnexaException if the role already exists
   */
  private void validateRoleUniqueness(String roleName) {
    boolean roleExists = backOfficeUserRoleRepository.findFirstByName(roleName.trim()).isPresent();
    if (roleExists) {
      String errorMessage =
          messagePropertyConfig
              .getRoleMessage(DUPLICATE, SYSTEM_DEFAULT_DUPLICATE_ERROR)
              .replace(ROLE_NAME, roleName);
      log.error("Role uniqueness validation failed: Role {} already exists", roleName);
      throw new OmnexaException(errorMessage, HttpStatus.BAD_REQUEST);
    }
  }

  public RoleDTO retrieveRole(String roleName) {
    BackOfficeUserRole userRole =
        backOfficeUserRoleRepository
            .findFirstByName(roleName)
            .orElseThrow(
                () ->
                    new OmnexaException(
                        messagePropertyConfig
                            .getRoleMessage(NOT_FOUND, SYSTEM_DEFAULT_NOT_FOUND_ERROR)
                            .replace(ROLE_NAME, roleName)));
    return new RoleDTO(
        userRole.getName(),
        userRole.getDescription(),
        userRole.isActive(),
        userRole.getPermissions().stream()
            .map(
                merchantUserPermission -> {
                  PermissionDTO permissionDTO = new PermissionDTO();
                  BeanUtilWrapper.copyNonNullProperties(merchantUserPermission, permissionDTO);
                  return permissionDTO;
                })
            .collect(Collectors.toSet()));
  }

  public BackOfficeUserRole retrieveRoleEnity(String roleName) {
    return backOfficeUserRoleRepository
        .findFirstByName(roleName)
        .orElseThrow(
            () ->
                new OmnexaException(
                    messagePropertyConfig
                        .getRoleMessage(NOT_FOUND, SYSTEM_DEFAULT_NOT_FOUND_ERROR)
                        .replace(ROLE_NAME, roleName)));
  }

  public Set<BackOfficeUserPermission> retrieveSelectedPermissions(
      Set<String> selectedPermissions) {
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
        backOfficeUserPermissionRepository.findByNameIn(effectivePermissions);

    return new HashSet<>(permissions);
  }
}
