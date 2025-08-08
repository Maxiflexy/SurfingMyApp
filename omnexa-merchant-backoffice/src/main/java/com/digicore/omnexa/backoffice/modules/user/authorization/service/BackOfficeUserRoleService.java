/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.backoffice.modules.user.authorization.service;

import static com.digicore.omnexa.common.lib.constant.message.MessageConstant.NOT_FOUND;
import static com.digicore.omnexa.common.lib.constant.system.SystemConstant.SYSTEM_DEFAULT_NOT_FOUND_ERROR;

import com.digicore.omnexa.backoffice.modules.user.authorization.data.model.BackOfficeUserRole;
import com.digicore.omnexa.backoffice.modules.user.authorization.helper.AuthorizationHelper;
import com.digicore.omnexa.backoffice.modules.user.authorization.mapper.RoleResponseMapper;
import com.digicore.omnexa.common.lib.authorization.contract.AuthorizationRequest;
import com.digicore.omnexa.common.lib.authorization.contract.AuthorizationResponse;
import com.digicore.omnexa.common.lib.authorization.contract.RoleService;
import com.digicore.omnexa.common.lib.authorization.dto.request.RoleCreationDTO;
import com.digicore.omnexa.common.lib.exception.OmnexaException;
import com.digicore.omnexa.common.lib.util.BeanUtilWrapper;
import com.digicore.omnexa.common.lib.util.PagenationUtil;
import com.digicore.omnexa.common.lib.util.PaginatedResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service implementation for back office user role operations.
 *
 * <p>This service handles comprehensive role management including creation, updating, activation,
 * deactivation, deletion, and retrieval with proper validation by delegating validation logic to
 * specialized helpers while maintaining focus on business logic.
 *
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jul-21(Mon)-2025
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BackOfficeUserRoleService implements RoleService {

  private final AuthorizationHelper authorizationHelper;

  /**
   * Creates a new role with comprehensive validation.
   *
   * @param request the role creation request
   */
  @Override
  @Transactional(rollbackFor = Exception.class)
  public void createRole(AuthorizationRequest request) {
    RoleCreationDTO roleCreationDTO = (RoleCreationDTO) request;

    // Delegate validation to helper
    authorizationHelper.validateRoleName(roleCreationDTO.getName());
    authorizationHelper.validateRolePermissions(roleCreationDTO.getPermissions());

    // Create and save the role
    roleCreationDTO.setActive(true);
    BackOfficeUserRole newRole = new BackOfficeUserRole();
    BeanUtilWrapper.copyNonNullProperties(roleCreationDTO, newRole);

    // Set permissions using the existing helper
    newRole.setPermissions(
        authorizationHelper.retrieveSelectedPermissions(roleCreationDTO.getPermissions()));

    authorizationHelper.getBackOfficeUserRoleRepository().save(newRole);
  }

  /**
   * Updates an existing role with comprehensive validation.
   *
   * @param request the updated role data
   * @return Optional containing the role name if successful
   */
  @Transactional(rollbackFor = Exception.class)
  public void editRole(AuthorizationRequest request) {
    RoleCreationDTO roleCreationDTO = (RoleCreationDTO) request;

    // Delegate validation to helper
    authorizationHelper.validateRoleName(roleCreationDTO.getName());
    authorizationHelper.validateRolePermissions(roleCreationDTO.getPermissions());

    // Find and validate role exists
    BackOfficeUserRole existingRole =
        authorizationHelper
            .getBackOfficeUserRoleRepository()
            .findFirstByName(roleCreationDTO.getName())
            .orElseThrow(
                () ->
                    new OmnexaException(
                        authorizationHelper
                            .getMessagePropertyConfig()
                            .getRoleMessage(NOT_FOUND, SYSTEM_DEFAULT_NOT_FOUND_ERROR)));

    // Update role properties
    existingRole.setDescription(roleCreationDTO.getDescription());
    existingRole.setActive(roleCreationDTO.isActive());

    // Update permissions using existing helper
    existingRole.setPermissions(
        authorizationHelper.retrieveSelectedPermissions(roleCreationDTO.getPermissions()));

    authorizationHelper.getBackOfficeUserRoleRepository().save(existingRole);
  }

  /**
   * Activates a role.
   *
   * @return Optional containing the role name if successful
   */
  @Transactional(rollbackFor = Exception.class)
  public void enableRole(String roleName) {

    // Find and validate role exists
    BackOfficeUserRole role = authorizationHelper.retrieveRoleEnity(roleName);

    // Validate role can be activated
    authorizationHelper.validateRoleCanBeActivated(role);

    // Activate the role
    role.setActive(true);
    authorizationHelper.getBackOfficeUserRoleRepository().save(role);
  }

  /**
   * Deactivates a role.
   *
   * @return Optional containing the role name if successful
   */
  @Transactional(rollbackFor = Exception.class)
  public void disableRole(String roleName) {

    // Find and validate role exists
    BackOfficeUserRole role = authorizationHelper.retrieveRoleEnity(roleName);

    // Validate role can be deactivated
    authorizationHelper.validateRoleCanBeDeactivated(role);

    // Deactivate the role
    role.setActive(false);
    authorizationHelper.getBackOfficeUserRoleRepository().save(role);

    log.info("Successfully deactivated role: {}", role.getName());
  }

  /**
   * Deletes a role.
   *
   * @return Optional containing the role name if successful
   */
  @Transactional(rollbackFor = Exception.class)
  public void deleteRole(String roleName) {

    BackOfficeUserRole role = authorizationHelper.retrieveRoleEnity(roleName);
    role.setDeleted(true);
    role.setActive(false);
    authorizationHelper.getBackOfficeUserRoleRepository().delete(role);
  }

  /**
   * Retrieves all roles with pagination support using common lib utilities.
   *
   * @param pageNumber the page number (1-based, optional, default: 1)
   * @param pageSize the page size (optional, max 15, default: 15)
   * @return paginated role response using common lib PaginatedResponse
   */
  public PaginatedResponse<AuthorizationResponse> retrieveAllRole(
      Integer pageNumber, Integer pageSize, String identifier) {
    // Use common lib pagination utility for validation
    PagenationUtil.ValidationResult validationResult =
        PagenationUtil.getValidatedPaginationParameters(pageNumber, pageSize);

    // Create pageable with sorting by creation date (newest first) using common lib utility
    Pageable pageable =
        PagenationUtil.createPageable(
            validationResult.validatedPageNumber(), validationResult.validatedPageSize());

    // Retrieve roles from repository
    Page<BackOfficeUserRole> rolePage =
        authorizationHelper.getBackOfficeUserRoleRepository().findAll(pageable);

    // Map to RoleDTO list using existing common lib DTO
    List<AuthorizationResponse> roleDTOs =
        RoleResponseMapper.mapEntitiesToRoleDTOs(rolePage.getContent());

    // Build paginated response using common lib PaginatedResponse

    return PaginatedResponse.<AuthorizationResponse>builder()
        .content(roleDTOs)
        .currentPage(validationResult.validatedPageNumber())
        .totalItems(rolePage.getTotalElements())
        .totalPages(rolePage.getTotalPages())
        .isFirstPage(rolePage.isFirst())
        .isLastPage(rolePage.isLast())
        .build();
  }

  @Override
  public AuthorizationResponse retrieveRole(String roleName, String merchantId) {
    return authorizationHelper.retrieveRole(roleName);
  }
}
