/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.backoffice.modules.user.authorization.helper;

import static com.digicore.omnexa.common.lib.constant.message.MessageConstant.*;
import static com.digicore.omnexa.common.lib.constant.message.MessageConstant.INVALID;
import static com.digicore.omnexa.common.lib.constant.message.MessagePlaceHolderConstant.ROLE_NAME;
import static com.digicore.omnexa.common.lib.constant.system.SystemConstant.*;

import com.digicore.omnexa.backoffice.modules.user.authorization.data.model.BackOfficeUserPermission;
import com.digicore.omnexa.backoffice.modules.user.authorization.data.model.BackOfficeUserRole;
import com.digicore.omnexa.backoffice.modules.user.authorization.data.repository.BackOfficeUserPermissionRepository;
import com.digicore.omnexa.backoffice.modules.user.authorization.data.repository.BackOfficeUserRoleRepository;
import com.digicore.omnexa.common.lib.exception.OmnexaException;
import com.digicore.omnexa.common.lib.properties.MessagePropertyConfig;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.digicore.omnexa.common.lib.util.RequestUtil;
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

  private final BackOfficeUserRoleRepository backOfficeUserRoleRepository;
  private final MessagePropertyConfig messagePropertyConfig;

  /**
   * Set of system reserved role names that cannot be modified or deleted.
   */
  private static final Set<String> SYSTEM_RESERVED_ROLES = Set.of(
          SYSTEM_MERCHANT_ROLE_NAME,
          SYSTEM_AUTHORIZER_ROLE_NAME,
          SYSTEM_INITIATOR_ROLE_NAME
  );

  /**
   * Finds and validates that a role exists and can be operated on.
   *
   * @param roleId the ID of the role to find
   * @return the found role
   * @throws OmnexaException if role is not found
   */
  public BackOfficeUserRole findRoleById(Long roleId) {
    log.debug("Finding role with ID: {}", roleId);

    return backOfficeUserRoleRepository.findById(roleId)
            .orElseThrow(() -> {
              String errorMessage = messagePropertyConfig
                      .getRoleMessage(NOT_FOUND, SYSTEM_DEFAULT_NOT_FOUND_ERROR)
                      .replace(ROLE_NAME, "Role with ID: " + roleId);
              log.error("Role not found with ID: {}", roleId);
              return new OmnexaException(errorMessage, HttpStatus.NOT_FOUND);
            });
  }

  /**
   * Validates that a role can be modified (not a system reserved role).
   *
   * @param role the role to validate
   * @throws OmnexaException if the role is system reserved
   */
  public void validateRoleCanBeModified(BackOfficeUserRole role) {
    log.debug("Validating if role can be modified: {}", role.getName());

    if (isSystemReservedRole(role.getName())) {
      String errorMessage = messagePropertyConfig
              .getRoleMessage(CONFLICT, SYSTEM_DEFAULT_CONFLICT_ERROR)
              .replace(ROLE_NAME, role.getName() + " (System reserved role cannot be modified)");
      log.error("Attempted to modify system reserved role: {}", role.getName());
      throw new OmnexaException(errorMessage, HttpStatus.BAD_REQUEST);
    }

    log.debug("Role {} can be modified", role.getName());
  }

  /**
   * Validates that a role can be deleted.
   *
   * <p>A role can be deleted if:
   * - It's not a system reserved role
   * - It's not currently assigned to any users (future enhancement)
   *
   * @param role the role to validate for deletion
   * @throws OmnexaException if the role cannot be deleted
   */
  public void validateRoleCanBeDeleted(BackOfficeUserRole role) {
    validateRoleCanBeModified(role);
  }

  /**
   * Validates that a role can be activated.
   *
   * @param role the role to validate for activation
   * @throws OmnexaException if the role cannot be activated
   */
  public void validateRoleCanBeActivated(BackOfficeUserRole role) {
    log.debug("Validating if role can be activated: {}", role.getName());

    if (role.isActive()) {
      String errorMessage = messagePropertyConfig
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

    // System reserved roles cannot be deactivated
    validateRoleCanBeModified(role);

    if (!role.isActive()) {
      String errorMessage = messagePropertyConfig
              .getRoleMessage(CONFLICT, SYSTEM_DEFAULT_CONFLICT_ERROR)
              .replace(ROLE_NAME, role.getName() + " (Role is already inactive)");
      log.error("Attempted to deactivate already inactive role: {}", role.getName());
      throw new OmnexaException(errorMessage, HttpStatus.BAD_REQUEST);
    }

    log.debug("Role {} can be deactivated", role.getName());
  }

  /**
   * Validates that a role name can be updated (checking for duplicates excluding current role).
   *
   * @param newRoleName the new role name
   * @param currentRoleId the ID of the role being updated
   * @throws OmnexaException if the new role name already exists
   */
  public void validateRoleNameForUpdate(String newRoleName, Long currentRoleId) {
    log.debug("Validating role name for update: {} (excluding ID: {})", newRoleName, currentRoleId);

    backOfficeUserRoleRepository.findFirstByName(newRoleName.trim())
            .filter(existingRole -> !existingRole.getId().equals(currentRoleId))
            .ifPresent(existingRole -> {
              String errorMessage = messagePropertyConfig
                      .getRoleMessage(DUPLICATE, SYSTEM_DEFAULT_DUPLICATE_ERROR)
                      .replace(ROLE_NAME, newRoleName);
              log.error("Role name already exists: {}", newRoleName);
              throw new OmnexaException(errorMessage, HttpStatus.BAD_REQUEST);
            });

    log.debug("Role name {} is unique for update", newRoleName);
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

  /**
   * Validates that the role ID is valid (not null and positive).
   *
   * @param roleId the role ID to validate
   * @throws OmnexaException if the role ID is invalid
   */
  public void validateRoleId(Long roleId) {
    if (roleId == null || roleId <= 0) {
      String errorMessage = messagePropertyConfig
              .getRoleMessage(INVALID, SYSTEM_DEFAULT_INVALID_REQUEST_ERROR)
              .replace(ROLE_NAME, "Role ID");
      log.error("Invalid role ID provided: {}", roleId);
      throw new OmnexaException(errorMessage, HttpStatus.BAD_REQUEST);
    }
  }



  /**
   * Validates a role name for creation against all validation rules.
   *
   * <p>This method performs comprehensive role name validation for creation including:
   * - Null/empty checks
   * - System reserved role conflicts
   * - Duplicate role name checks
   *
   * @param roleName the role name to validate
   * @throws OmnexaException if validation fails
   */
  public void validateRoleName(String roleName) {
    log.debug("Starting role name validation for creation: {}", roleName);

    validateRoleNameNotEmpty(roleName);
    validateNotSystemReservedRole(roleName);
    validateRoleUniqueness(roleName);

    log.debug("Role name validation successful for creation: {}", roleName);
  }

  /**
   * Validates that the role name is not null or empty.
   *
   * @param roleName the role name to validate
   * @throws OmnexaException if the role name is null or empty
   */
  private void validateRoleNameNotEmpty(String roleName) {
    if (RequestUtil.nullOrEmpty(roleName)) {
      String errorMessage = messagePropertyConfig
              .getRoleMessage(REQUIRED, SYSTEM_DEFAULT_REQUIRED_FIELD_MISSING_ERROR)
              .replace(ROLE_NAME, "Role name");
      log.error("Role name validation failed: Role name is required");
      throw new OmnexaException(errorMessage, HttpStatus.BAD_REQUEST);
    }

    String trimmedRoleName = roleName.trim();
    if (trimmedRoleName.isEmpty()) {
      String errorMessage = messagePropertyConfig
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
      String errorMessage = messagePropertyConfig
              .getRoleMessage(CONFLICT, SYSTEM_DEFAULT_CONFLICT_ERROR)
              .replace(ROLE_NAME, roleName + " (System reserved role)");
      log.error("Role name validation failed: {} is a system reserved role", roleName);
      throw new OmnexaException(errorMessage, HttpStatus.BAD_REQUEST);
    }

    log.debug("Role name {} is not a system reserved role", roleName);
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
      String errorMessage = messagePropertyConfig
              .getRoleMessage(DUPLICATE, SYSTEM_DEFAULT_DUPLICATE_ERROR)
              .replace(ROLE_NAME, roleName);
      log.error("Role uniqueness validation failed: Role {} already exists", roleName);
      throw new OmnexaException(errorMessage, HttpStatus.BAD_REQUEST);
    }

    log.debug("Role name {} is unique", roleName);
  }



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
