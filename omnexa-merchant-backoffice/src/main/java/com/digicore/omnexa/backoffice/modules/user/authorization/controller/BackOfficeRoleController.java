/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.backoffice.modules.user.authorization.controller;

import static com.digicore.omnexa.common.lib.api.ApiVersion.API_V1;
import static com.digicore.omnexa.common.lib.swagger.constant.AuthorizationSwaggerDocConstant.*;

import com.digicore.omnexa.backoffice.modules.user.authorization.service.BackOfficeUserRoleService;
import com.digicore.omnexa.common.lib.api.ControllerResponse;
import com.digicore.omnexa.common.lib.authorization.dto.request.RoleCreationDTO;
import com.digicore.omnexa.common.lib.authorization.dto.response.RoleDTO;
import com.digicore.omnexa.common.lib.util.PaginatedResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

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

    private final BackOfficeUserRoleService backOfficeUserRoleService;

    /**
     * Retrieves a specific role by ID.
     *
     * @param roleId the ID of the role to retrieve
     * @return ResponseEntity containing the role details and HTTP status 200
     */
    @GetMapping("/{roleId}")
    @Operation(
            summary = GET_ROLE_BY_ID_TITLE,
            description = GET_ROLE_BY_ID_DESCRIPTION)
    public ResponseEntity<Object> getRoleById(
            @Parameter(description = ROLE_ID_PARAMETER_DESCRIPTION + " to retrieve",
                    example = ROLE_ID_EXAMPLE, required = true)
            @PathVariable Long roleId) {

        Optional<RoleDTO> result = backOfficeUserRoleService.getRoleById(roleId);
        return ControllerResponse.buildSuccessResponse(result, "Role retrieved successfully");
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
    @Operation(
            summary = GET_ALL_ROLES_TITLE,
            description = GET_ALL_ROLES_DESCRIPTION)
    public ResponseEntity<Object> getAllRoles(
            @Parameter(description = PAGE_NUMBER_PARAMETER_DESCRIPTION, example = PAGE_NUMBER_EXAMPLE)
            @RequestParam(required = false)
            Integer pageNumber,
            @Parameter(description = PAGE_SIZE_PARAMETER_DESCRIPTION, example = PAGE_SIZE_EXAMPLE)
            @RequestParam(required = false)
            Integer pageSize) {

        PaginatedResponse<RoleDTO> result = backOfficeUserRoleService.getAllRoles(pageNumber, pageSize);
        return ControllerResponse.buildSuccessResponse(result, "Roles retrieved successfully");
    }

    /**
     * Creates a new role in the back office system.
     *
     * @param roleCreationDTO the role creation request containing role details and permissions
     * @return ResponseEntity containing success message and HTTP status 201
     */
    @PostMapping()
    @PreAuthorize("hasAuthority('create-backoffice-roles')")
    @Operation(
            summary = CREATE_ROLE_TITLE,
            description = CREATE_ROLE_DESCRIPTION)
    public ResponseEntity<Object> createRole(@Valid @RequestBody RoleCreationDTO roleCreationDTO) {
        backOfficeUserRoleService.createRole(roleCreationDTO);
        return ControllerResponse.buildCreateSuccessResponse("Role created successfully");
    }

    /**
     * Updates an existing role in the back office system.
     *
     * @param roleId the ID of the role to update
     * @param roleCreationDTO the role update request containing updated details and permissions
     * @return ResponseEntity containing success message and HTTP status 200
     */
    @PutMapping("/{roleId}")
    @PreAuthorize("hasAuthority('edit-backoffice-role')")
    @Operation(
            summary = UPDATE_ROLE_TITLE,
            description = UPDATE_ROLE_DESCRIPTION)
    public ResponseEntity<Object> updateRole(
            @Parameter(description = ROLE_ID_PARAMETER_DESCRIPTION + " to update",
                    example = ROLE_ID_EXAMPLE, required = true)
            @PathVariable Long roleId,
            @Valid @RequestBody RoleCreationDTO roleCreationDTO) {

        Optional<String> result = backOfficeUserRoleService.updateRole(roleId, roleCreationDTO);
        return ControllerResponse.buildSuccessResponse(result, "Role updated successfully");
    }

    /**
     * Activates a role in the back office system.
     *
     * @param roleId the ID of the role to activate
     * @return ResponseEntity containing success message and HTTP status 200
     */
    @PatchMapping("/{roleId}/activate")
    @PreAuthorize("hasAuthority('approve-create-backoffice-roles')")
    @Operation(
            summary = ACTIVATE_ROLE_TITLE,
            description = ACTIVATE_ROLE_DESCRIPTION)
    public ResponseEntity<Object> activateRole(
            @Parameter(description = ROLE_ID_PARAMETER_DESCRIPTION + " to activate",
                    example = ROLE_ID_EXAMPLE, required = true)
            @PathVariable Long roleId) {

        Optional<String> result = backOfficeUserRoleService.activateRole(roleId);
        return ControllerResponse.buildSuccessResponse(result, "Role activated successfully");
    }

    /**
     * Deactivates a role in the back office system.
     *
     * @param roleId the ID of the role to deactivate
     * @return ResponseEntity containing success message and HTTP status 200
     */
    @PatchMapping("/{roleId}/deactivate")
    @PreAuthorize("hasAuthority('disable-backoffice-role')")
    @Operation(
            summary = DEACTIVATE_ROLE_TITLE,
            description = DEACTIVATE_ROLE_DESCRIPTION)
    public ResponseEntity<Object> deactivateRole(
            @Parameter(description = ROLE_ID_PARAMETER_DESCRIPTION + " to deactivate",
                    example = ROLE_ID_EXAMPLE, required = true)
            @PathVariable Long roleId) {

        Optional<String> result = backOfficeUserRoleService.deactivateRole(roleId);
        return ControllerResponse.buildSuccessResponse(result, "Role deactivated successfully");
    }

    /**
     * Deletes a role from the back office system.
     *
     * @param roleId the ID of the role to delete
     * @return ResponseEntity containing success message and HTTP status 200
     */
    @DeleteMapping("/{roleId}")
    @PreAuthorize("hasAuthority('delete-backoffice-role')")
    @Operation(
            summary = DELETE_ROLE_TITLE,
            description = DELETE_ROLE_DESCRIPTION)
    public ResponseEntity<Object> deleteRole(
            @Parameter(description = ROLE_ID_PARAMETER_DESCRIPTION + " to delete",
                    example = ROLE_ID_EXAMPLE, required = true)
            @PathVariable Long roleId) {

        Optional<String> result = backOfficeUserRoleService.deleteRole(roleId);
        return ControllerResponse.buildSuccessResponse(result, "Role deleted successfully");
    }
}