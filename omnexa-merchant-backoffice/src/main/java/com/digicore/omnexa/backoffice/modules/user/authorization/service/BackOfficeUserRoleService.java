/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.backoffice.modules.user.authorization.service;

import static com.digicore.omnexa.backoffice.modules.user.authorization.helper.AuthorizationHelper.retrieveSelectedPermissions;

import com.digicore.omnexa.backoffice.modules.user.authorization.data.model.BackOfficeUserRole;
import com.digicore.omnexa.backoffice.modules.user.authorization.data.repository.BackOfficeUserPermissionRepository;
import com.digicore.omnexa.backoffice.modules.user.authorization.data.repository.BackOfficeUserRoleRepository;
import com.digicore.omnexa.backoffice.modules.user.authorization.helper.AuthorizationHelper;
import com.digicore.omnexa.backoffice.modules.user.authorization.mapper.RoleResponseMapper;
import com.digicore.omnexa.common.lib.authorization.contract.AuthorizationRequest;
import com.digicore.omnexa.common.lib.authorization.contract.AuthorizationResponse;
import com.digicore.omnexa.common.lib.authorization.contract.RoleService;
import com.digicore.omnexa.common.lib.authorization.dto.request.RoleCreationDTO;
import com.digicore.omnexa.common.lib.authorization.dto.response.RoleDTO;
import com.digicore.omnexa.common.lib.properties.MessagePropertyConfig;
import com.digicore.omnexa.common.lib.util.BeanUtilWrapper;
import com.digicore.omnexa.common.lib.util.PagenationUtil;
import com.digicore.omnexa.common.lib.util.PaginatedResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service implementation for back office user role operations.
 *
 * <p>This service handles comprehensive role management including creation, updating,
 * activation, deactivation, deletion, and retrieval with proper validation by delegating
 * validation logic to specialized helpers while maintaining focus on business logic.
 *
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jul-21(Mon)-2025
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BackOfficeUserRoleService implements RoleService {

  private final BackOfficeUserPermissionRepository backOfficeUserPermissionRepository;
  private final BackOfficeUserRoleRepository backOfficeUserRoleRepository;
  private final MessagePropertyConfig messagePropertyConfig;
  private final AuthorizationHelper authorizationHelper;
  //private final RoleValidationHelper roleValidationHelper;
  //private final RoleOperationsHelper roleOperationsHelper;

  /**
   * Creates a new role with comprehensive validation.
   *
   * @param request the role creation request
   */
  @Override
  @Transactional(rollbackFor = Exception.class)
  public void createRole(AuthorizationRequest request) {
    RoleCreationDTO roleCreationDTO = (RoleCreationDTO) request;

    log.info("Creating role: {}", roleCreationDTO.getName());

    // Delegate validation to helper
    authorizationHelper.validateRoleName(roleCreationDTO.getName());

    // Create and save the role
    roleCreationDTO.setActive(true);
    BackOfficeUserRole newRole = new BackOfficeUserRole();
    BeanUtilWrapper.copyNonNullProperties(roleCreationDTO, newRole);

    // Set permissions using the existing helper
    newRole.setPermissions(
            retrieveSelectedPermissions(
                    roleCreationDTO.getPermissions(),
                    messagePropertyConfig,
                    backOfficeUserPermissionRepository));

    backOfficeUserRoleRepository.save(newRole);
    log.info("Successfully created role: {}", roleCreationDTO.getName());
  }

  /**
   * Updates an existing role with comprehensive validation.
   *
   * @param roleId the ID of the role to update
   * @param roleCreationDTO the updated role data
   * @return Optional containing the role name if successful
   */
  @Transactional(rollbackFor = Exception.class)
  public Optional<String> updateRole(Long roleId, RoleCreationDTO roleCreationDTO) {
    log.info("Updating role with ID: {}", roleId);

    // Validate role ID
    authorizationHelper.validateRoleId(roleId);

    // Find and validate role exists
    BackOfficeUserRole existingRole = authorizationHelper.findRoleById(roleId);

    // Validate role can be modified (not system reserved)
    authorizationHelper.validateRoleCanBeModified(existingRole);

    // Validate new role name if it's being changed
    if (!existingRole.getName().equals(roleCreationDTO.getName().trim())) {
      // Validate against system reserved roles
      authorizationHelper.validateRoleName(roleCreationDTO.getName());
      // Validate uniqueness excluding current role
      authorizationHelper.validateRoleNameForUpdate(roleCreationDTO.getName(), roleId);
    }

    // Update role properties
    existingRole.setName(roleCreationDTO.getName().trim());
    existingRole.setDescription(roleCreationDTO.getDescription());
    existingRole.setActive(roleCreationDTO.isActive());

    // Update permissions using existing helper
    existingRole.setPermissions(
            retrieveSelectedPermissions(
                    roleCreationDTO.getPermissions(),
                    messagePropertyConfig,
                    backOfficeUserPermissionRepository));

    backOfficeUserRoleRepository.save(existingRole);
    log.info("Successfully updated role: {}", existingRole.getName());

    return Optional.of(existingRole.getName());
  }

  /**
   * Activates a role.
   *
   * @param roleId the ID of the role to activate
   * @return Optional containing the role name if successful
   */
  @Transactional(rollbackFor = Exception.class)
  public Optional<String> activateRole(Long roleId) {
    log.info("Activating role with ID: {}", roleId);

    // Validate role ID
    authorizationHelper.validateRoleId(roleId);

    // Find and validate role exists
    BackOfficeUserRole role = authorizationHelper.findRoleById(roleId);

    // Validate role can be activated
    authorizationHelper.validateRoleCanBeActivated(role);

    // Activate the role
    role.setActive(true);
    backOfficeUserRoleRepository.save(role);

    log.info("Successfully activated role: {}", role.getName());
    return Optional.of(role.getName());
  }

  /**
   * Deactivates a role.
   *
   * @param roleId the ID of the role to deactivate
   * @return Optional containing the role name if successful
   */
  @Transactional(rollbackFor = Exception.class)
  public Optional<String> deactivateRole(Long roleId) {
    log.info("Deactivating role with ID: {}", roleId);

    // Validate role ID
    authorizationHelper.validateRoleId(roleId);

    // Find and validate role exists
    BackOfficeUserRole role = authorizationHelper.findRoleById(roleId);

    // Validate role can be deactivated
    authorizationHelper.validateRoleCanBeDeactivated(role);

    // Deactivate the role
    role.setActive(false);
    backOfficeUserRoleRepository.save(role);

    log.info("Successfully deactivated role: {}", role.getName());
    return Optional.of(role.getName());
  }

  /**
   * Deletes a role.
   *
   * @param roleId the ID of the role to delete
   * @return Optional containing the role name if successful
   */
  @Transactional(rollbackFor = Exception.class)
  public Optional<String> deleteRole(Long roleId) {
    log.info("Deleting role with ID: {}", roleId);

    // Validate role ID
    authorizationHelper.validateRoleId(roleId);

    // Find and validate role exists
    BackOfficeUserRole role = authorizationHelper.findRoleById(roleId);

    authorizationHelper.validateRoleCanBeModified(role);

    String roleName = role.getName();
    role.setDeleted(true);

    backOfficeUserRoleRepository.delete(role);

    log.info("Successfully deleted role: {}", roleName);
    return Optional.of(roleName);
  }

  /**
   * Retrieves a specific role by ID with detailed information.
   *
   * @param roleId the ID of the role to retrieve
   * @return Optional containing the role details if found
   */
  public Optional<RoleDTO> getRoleById(Long roleId) {
    log.info("Retrieving role with ID: {}", roleId);

    // Validate role ID
    authorizationHelper.validateRoleId(roleId);

    // Find the role
    BackOfficeUserRole role = authorizationHelper.findRoleById(roleId);

    // Map to response DTO using existing common lib DTO
    RoleDTO roleResponse = RoleResponseMapper.mapEntityToRoleDTO(role);

    log.info("Successfully retrieved role: {} (ID: {})", role.getName(), roleId);
    return Optional.of(roleResponse);
  }

  /**
   * Retrieves all roles with pagination support using common lib utilities.
   *
   * @param pageNumber the page number (1-based, optional, default: 1)
   * @param pageSize the page size (optional, max 15, default: 15)
   * @return paginated role response using common lib PaginatedResponse
   */
  public PaginatedResponse<RoleDTO> getAllRoles(Integer pageNumber, Integer pageSize) {
    log.info("Retrieving all roles with pagination: page={}, size={}", pageNumber, pageSize);

    // Use common lib pagination utility for validation
    PagenationUtil.ValidationResult validationResult =
            PagenationUtil.getValidatedPaginationParameters(pageNumber, pageSize);

    // Create pageable with sorting by creation date (newest first) using common lib utility
    Pageable pageable = PagenationUtil.createPageable(
            validationResult.validatedPageNumber(),
            validationResult.validatedPageSize()
    );

    // Retrieve roles from repository
    Page<BackOfficeUserRole> rolePage = backOfficeUserRoleRepository.findAll(pageable);

    // Map to RoleDTO list using existing common lib DTO
    List<RoleDTO> roleDTOs = RoleResponseMapper.mapEntitiesToRoleDTOs(rolePage.getContent());

    // Build paginated response using common lib PaginatedResponse
    PaginatedResponse<RoleDTO> response = PaginatedResponse.<RoleDTO>builder()
            .content(roleDTOs)
            .currentPage(validationResult.validatedPageNumber())
            .totalItems(rolePage.getTotalElements())
            .totalPages(rolePage.getTotalPages())
            .isFirstPage(rolePage.isFirst())
            .isLastPage(rolePage.isLast())
            .build();

    log.info("Successfully retrieved {} roles (page {} of {})",
            roleDTOs.size(), response.getCurrentPage(), response.getTotalPages());

    return response;
  }
}