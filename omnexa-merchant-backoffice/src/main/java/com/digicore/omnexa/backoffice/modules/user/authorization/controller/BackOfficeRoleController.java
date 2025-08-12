/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.backoffice.modules.user.authorization.controller;

import static com.digicore.omnexa.common.lib.api.ApiVersion.API_V1;
import static com.digicore.omnexa.common.lib.constant.system.SystemConstant.SYSTEM_REQUET_SUBMITTED_MESSAGE;
import static com.digicore.omnexa.common.lib.swagger.constant.CommonEndpointSwaggerDoc.*;
import static com.digicore.omnexa.common.lib.swagger.constant.authorization.AuthorizationSwaggerDocConstant.*;

import com.digicore.omnexa.backoffice.modules.user.authorization.approval.validator.BackOfficeUserRoleValidatorService;
import com.digicore.omnexa.common.lib.api.ControllerResponse;
import com.digicore.omnexa.common.lib.authorization.contract.RoleService;
import com.digicore.omnexa.common.lib.authorization.dto.request.RoleCreationDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for back office role management operations.
 *
 * <p>Provides comprehensive endpoints for role management operations following SOLID principles.
 * This controller handles role CRUD operations with proper validation and security measures.
 *
 * @author Onyekachi Ejemba
 * @createdOn Aug-04(Mon)-2025
 */
@RestController
@RequestMapping(API_V1 + ROLES_API)
@RequiredArgsConstructor
@Tag(name = ROLE_MANAGEMENT_CONTROLLER_TITLE, description = ROLE_MANAGEMENT_CONTROLLER_DESCRIPTION)
public class BackOfficeRoleController {

  private final RoleService backOfficeUserRoleService;
  private final BackOfficeUserRoleValidatorService backOfficeUserRoleValidatorService;

  /**
   * Retrieves a specific role by ID.
   *
   * @param roleName the ID of the role to retrieve
   * @return ResponseEntity containing the role details and HTTP status 200
   */
  @GetMapping("/{roleName}")
  @PreAuthorize("hasAuthority('view-backoffice-roles')")
  @Operation(summary = GET_ROLE_BY_ID_TITLE, description = GET_ROLE_BY_ID_DESCRIPTION)
  public ResponseEntity<Object> getRole(
      @Parameter(
              description = ROLE_NAME_PARAMETER_DESCRIPTION,
              example = ROLE_NAME_EXAMPLE,
              required = true)
          @PathVariable
          String roleName) {

    return ControllerResponse.buildSuccessResponse(
        backOfficeUserRoleService.retrieveRole(roleName, null), "Role retrieved successfully");
  }

  /**
   * Retrieves all roles with pagination support.
   *
   * @param pageNumber the page number (1-based, optional, default: 1)
   * @param pageSize the number of roles per page (optional, max 15, default: 15)
   * @return ResponseEntity containing the paginated list of roles and HTTP status 200
   */
  @GetMapping()
  @PreAuthorize("hasAuthority('view-backoffice-roles')")
  @Operation(summary = GET_ALL_ROLES_TITLE, description = GET_ALL_ROLES_DESCRIPTION)
  public ResponseEntity<Object> getAllRoles(
      @Parameter(description = PAGE_NUMBER_DESCRIPTION, example = PAGE_NUMBER)
          @RequestParam(required = false)
          Integer pageNumber,
      @Parameter(description = PAGE_SIZE_DESCRIPTION, example = PAGE_SIZE)
          @RequestParam(required = false)
          Integer pageSize) {

    return ControllerResponse.buildSuccessResponse(
        backOfficeUserRoleService.retrieveAllRole(pageNumber, pageSize, null),
        "Roles retrieved successfully");
  }

  /**
   * Creates a new role in the back office system.
   *
   * @param roleCreationDTO the role creation request containing role details and permissions
   * @return ResponseEntity containing success message and HTTP status 201
   */
  @PostMapping()
  @PreAuthorize("hasAuthority('create-backoffice-roles')")
  @Operation(summary = CREATE_ROLE_TITLE, description = CREATE_ROLE_DESCRIPTION)
  public ResponseEntity<Object> createRole(@Valid @RequestBody RoleCreationDTO roleCreationDTO) {
    backOfficeUserRoleValidatorService.createRole(roleCreationDTO);
    return ControllerResponse.buildCreateSuccessResponse(SYSTEM_REQUET_SUBMITTED_MESSAGE);
  }

  /**
   * Updates an existing role in the back office system.
   *
   * @param roleCreationDTO the role update request containing updated details and permissions
   * @return ResponseEntity containing success message and HTTP status 200
   */
  @PatchMapping()
  @PreAuthorize("hasAuthority('edit-backoffice-role')")
  @Operation(summary = UPDATE_ROLE_TITLE, description = UPDATE_ROLE_DESCRIPTION)
  public ResponseEntity<Object> updateRole(@Valid @RequestBody RoleCreationDTO roleCreationDTO) {

    backOfficeUserRoleValidatorService.editRole(roleCreationDTO);
    return ControllerResponse.buildSuccessResponse("Role updated successfully");
  }

  /**
   * Activates a role in the back office system.
   *
   * @param roleName the ID of the role to activate
   * @return ResponseEntity containing success message and HTTP status 200
   */
  @PatchMapping("/{roleName}/activate")
  @PreAuthorize("hasAuthority('enable-backoffice-role')")
  @Operation(summary = ACTIVATE_ROLE_TITLE, description = ACTIVATE_ROLE_DESCRIPTION)
  public ResponseEntity<Object> activateRole(
      @Parameter(description = ROLE_NAME_PARAMETER_DESCRIPTION + " to activate", required = true)
          @PathVariable
          String roleName) {

    backOfficeUserRoleValidatorService.enableRole(roleName);
    return ControllerResponse.buildSuccessResponse("Role activated successfully");
  }

  /**
   * Deactivates a role in the back office system.
   *
   * @param roleName the ID of the role to deactivate
   * @return ResponseEntity containing success message and HTTP status 200
   */
  @PatchMapping("/{roleName}/deactivate")
  @PreAuthorize("hasAuthority('disable-backoffice-role')")
  @Operation(summary = DEACTIVATE_ROLE_TITLE, description = DEACTIVATE_ROLE_DESCRIPTION)
  public ResponseEntity<Object> deactivateRole(
      @Parameter(description = ROLE_NAME_PARAMETER_DESCRIPTION + " to deactivate", required = true)
          @PathVariable
          String roleName) {

    backOfficeUserRoleValidatorService.disableRole(roleName);
    return ControllerResponse.buildSuccessResponse("Role deactivated successfully");
  }

  /**
   * Deletes a role from the back office system.
   *
   * @param roleName the ID of the role to delete
   * @return ResponseEntity containing success message and HTTP status 200
   */
  @DeleteMapping("/{roleName}")
  @PreAuthorize("hasAuthority('delete-backoffice-role')")
  @Operation(summary = DELETE_ROLE_TITLE, description = DELETE_ROLE_DESCRIPTION)
  public ResponseEntity<Object> deleteRole(
      @Parameter(description = ROLE_NAME_PARAMETER_DESCRIPTION + " to delete", required = true)
          @PathVariable
          String roleName) {

    backOfficeUserRoleValidatorService.deleteRole(roleName);
    return ControllerResponse.buildSuccessResponse("Role deleted successfully");
  }
}
