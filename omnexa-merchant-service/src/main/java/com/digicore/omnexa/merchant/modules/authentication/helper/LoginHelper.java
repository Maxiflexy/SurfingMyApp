/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.merchant.modules.authentication.helper;

import static com.digicore.omnexa.common.lib.constant.message.MessageConstant.NOT_FOUND;
import static com.digicore.omnexa.common.lib.constant.message.MessagePlaceHolderConstant.ROLE_NAME;
import static com.digicore.omnexa.common.lib.constant.system.SystemConstant.SYSTEM_DEFAULT_NOT_FOUND_ERROR;
import static com.digicore.omnexa.common.lib.constant.system.SystemConstant.SYSTEM_MERCHANT_ROLE_NAME;

import com.digicore.omnexa.common.lib.authentication.dto.request.LoginRequestDTO;
import com.digicore.omnexa.common.lib.authentication.dto.response.LoginResponseDTO;
import com.digicore.omnexa.common.lib.authorization.dto.response.PermissionDTO;
import com.digicore.omnexa.common.lib.authorization.dto.response.RoleDTO;
import com.digicore.omnexa.common.lib.exception.OmnexaException;
import com.digicore.omnexa.common.lib.properties.MessagePropertyConfig;
import com.digicore.omnexa.common.lib.util.BeanUtilWrapper;
import com.digicore.omnexa.merchant.modules.authentication.dto.response.MerchantLoginProfileDTO;
import com.digicore.omnexa.merchant.modules.authentication.util.JwtUtil;
import com.digicore.omnexa.merchant.modules.authorization.data.model.MerchantUserRole;
import com.digicore.omnexa.merchant.modules.authorization.data.repository.MerchantUserRoleRepository;
import java.util.*;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

/**
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jul-29(Tue)-2025
 */
@Component
@RequiredArgsConstructor
public class LoginHelper {
  public static final String LOGIN_DETAIL_DTO_CLASS_NAME =
      "com.digicore.omnexa.merchant.modules.authentication.dto.response.MerchantLoginProfileDTO";
  private final JwtUtil jwtUtil;
  private final MerchantUserRoleRepository merchantUserRoleRepository;
  private final MessagePropertyConfig messagePropertyConfig;

  public LoginResponseDTO getLoginResponse(
      LoginRequestDTO loginRequestDTO, MerchantLoginProfileDTO userDetails) {

    userDetails.setPermissions(
        getGrantedAuthorities(userDetails.getRole(), userDetails.getMerchantId()));
    Map<String, String> claims = JwtUtil.buildClaims(loginRequestDTO.getUsername(), userDetails);
    Map<String, Object> additionalInformation = getAdditionalInformation(userDetails);

    return LoginResponseDTO.builder()
        .accessToken(jwtUtil.createJwtForClaims(loginRequestDTO.getUsername(), claims))
        .additionalInformation(additionalInformation)
        .build();
  }

  private static Map<String, Object> getAdditionalInformation(MerchantLoginProfileDTO userDetails) {
    Map<String, Object> additionalInformation = new HashMap<>();
    additionalInformation.put("name", userDetails.getName());
    additionalInformation.put("role", userDetails.getRole());
    additionalInformation.put("kycStatus", userDetails.getKycStatus());
    additionalInformation.put("merchantId", userDetails.getMerchantId());
    additionalInformation.put("businessName", userDetails.getBusinessName());
    return additionalInformation;
  }

  private Set<SimpleGrantedAuthority> getGrantedAuthorities(
      String assignedRole, String merchantId) {
    RoleDTO roleDTO = retrieveRole(assignedRole, merchantId);
    List<String> rolePermissions;
    rolePermissions = roleDTO.getPermissions().stream().map(PermissionDTO::getName).toList();

    Set<String> consolidatedPermissions = new HashSet<>(rolePermissions);
    return consolidatedPermissions.stream()
        .map(SimpleGrantedAuthority::new)
        .collect(Collectors.toSet());
  }

  public RoleDTO retrieveRole(String roleName, String merchantId) {
    MerchantUserRole userRole;

    if (SYSTEM_MERCHANT_ROLE_NAME.equalsIgnoreCase(roleName))
      userRole =
          merchantUserRoleRepository
              .findFirstByName(roleName)
              .orElseThrow(
                  () ->
                      new OmnexaException(
                          messagePropertyConfig
                              .getRoleMessage(NOT_FOUND, SYSTEM_DEFAULT_NOT_FOUND_ERROR)
                              .replace(ROLE_NAME, roleName)));
    else
      userRole =
          merchantUserRoleRepository
              .findFirstByNameAndMerchantProfileMerchantId(roleName, merchantId)
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
}
