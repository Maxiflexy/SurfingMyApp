/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.backoffice.modules.user.authorization.mapper;

import com.digicore.omnexa.backoffice.modules.user.authorization.data.model.BackOfficeUserRole;
import com.digicore.omnexa.common.lib.authorization.contract.AuthorizationResponse;
import com.digicore.omnexa.common.lib.authorization.dto.response.PermissionDTO;
import com.digicore.omnexa.common.lib.authorization.dto.response.RoleDTO;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

/**
 * Mapper class for converting role entities to response DTOs.
 *
 * <p>This mapper handles the transformation of role entities and related data into appropriate
 * response DTOs using the existing common lib DTOs to maintain consistency across the application.
 *
 * @author Onyekachi Ejemba
 * @createdOn Aug-04(Mon)-2025
 */
@Component
public class RoleResponseMapper {

  private RoleResponseMapper() {}

  /**
   * Maps a BackOfficeUserRole entity to RoleDTO using existing common lib DTO.
   *
   * @param role the role entity to map
   * @return the mapped RoleDTO
   */
  public static RoleDTO mapEntityToRoleDTO(BackOfficeUserRole role) {
    if (role == null) {
      return null;
    }

    // Map permissions using existing mapper
    Set<PermissionDTO> permissionDTOs =
        role.getPermissions().stream()
            .map(AuthorizationMapper::mapEntityToDTO)
            .collect(Collectors.toSet());

    // Create RoleDTO using the existing common lib DTO
    RoleDTO roleDTO =
        new RoleDTO(role.getName(), role.getDescription(), role.isActive(), permissionDTOs);

    // Set additional fields from BaseRoleDTO
    roleDTO.setSystemInitiated(isSystemRole(role.getName()));

    return roleDTO;
  }

  /**
   * Maps a list of BackOfficeUserRole entities to RoleDTO list.
   *
   * @param roles the list of role entities to map
   * @return the list of mapped RoleDTO objects
   */
  public static List<AuthorizationResponse> mapEntitiesToRoleDTOs(List<BackOfficeUserRole> roles) {
    if (roles == null) {
      return List.of();
    }

    return roles.stream().map(RoleResponseMapper::mapEntityToRoleDTO).collect(Collectors.toList());
  }

  /**
   * Validates and maps role entity with null safety.
   *
   * @param role the role entity to map
   * @return the mapped DTO or null if input is null
   */
  public static RoleDTO safeMapEntityToRoleDTO(BackOfficeUserRole role) {
    try {
      return mapEntityToRoleDTO(role);
    } catch (Exception e) {
      // Log the error in a real implementation
      return null;
    }
  }

  /**
   * Determines if a role is a system-initiated role based on its name.
   *
   * @param roleName the name of the role
   * @return true if the role is system-initiated, false otherwise
   */
  private static boolean isSystemRole(String roleName) {
    if (roleName == null) {
      return false;
    }

    String normalizedName = roleName.trim().toUpperCase();
    return normalizedName.equals("CUSTODIAN")
        || normalizedName.equals("AUTHORIZER")
        || normalizedName.equals("INITIATOR");
  }
}
