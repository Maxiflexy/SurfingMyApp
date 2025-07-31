/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.backoffice.modules.user.authentication.helper;

import static com.digicore.omnexa.common.lib.constant.message.MessageConstant.NOT_FOUND;
import static com.digicore.omnexa.common.lib.constant.message.MessagePlaceHolderConstant.ROLE_NAME;

import com.digicore.omnexa.backoffice.modules.user.authentication.dto.response.BackOfficeLoginProfileDTO;
import com.digicore.omnexa.backoffice.modules.user.authentication.util.BackOfficeJwtUtil;
import com.digicore.omnexa.backoffice.modules.user.authorization.data.model.BackOfficeUserRole;
import com.digicore.omnexa.backoffice.modules.user.authorization.data.repository.BackOfficeUserRoleRepository;
import com.digicore.omnexa.backoffice.modules.user.authorization.mapper.AuthorizationMapper;
import com.digicore.omnexa.common.lib.authentication.dto.request.LoginRequestDTO;
import com.digicore.omnexa.common.lib.authentication.dto.response.LoginResponseDTO;
import com.digicore.omnexa.common.lib.authorization.dto.response.PermissionDTO;
import com.digicore.omnexa.common.lib.authorization.dto.response.RoleDTO;
import com.digicore.omnexa.common.lib.exception.OmnexaException;
import com.digicore.omnexa.common.lib.properties.MessagePropertyConfig;
import com.digicore.omnexa.common.lib.util.BeanUtilWrapper;
import java.util.*;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

/**
 * Helper class for back office user login operations.
 *
 * <p>This helper handles the business logic for creating login responses,
 * managing user permissions, and building JWT tokens for authenticated users.
 *
 * @author Onyekachi Ejemba
 * @createdOn Jul-31(Thu)-2025
 */
@Component
@RequiredArgsConstructor
public class BackOfficeLoginHelper {

    public static final String LOGIN_DETAIL_DTO_CLASS_NAME =
            "com.digicore.omnexa.backoffice.modules.user.authentication.dto.response.BackOfficeLoginProfileDTO";

    private final BackOfficeJwtUtil jwtUtil;
    private final BackOfficeUserRoleRepository backOfficeUserRoleRepository;
    private final MessagePropertyConfig messagePropertyConfig;

    /**
     * Creates a login response with JWT token and additional user information.
     *
     * @param loginRequestDTO the login request containing credentials
     * @param userDetails the authenticated user details
     * @return LoginResponseDTO containing access token and user information
     */
    public LoginResponseDTO getLoginResponse(
            LoginRequestDTO loginRequestDTO, BackOfficeLoginProfileDTO userDetails) {

        userDetails.setPermissions(getGrantedAuthorities(userDetails.getRole()));
        Map<String, String> claims = BackOfficeJwtUtil.buildClaims(loginRequestDTO.getUsername(), userDetails);
        Map<String, Object> additionalInformation = getAdditionalInformation(userDetails);

        return LoginResponseDTO.builder()
                .accessToken(jwtUtil.createJwtForClaims(loginRequestDTO.getUsername(), claims))
                .additionalInformation(additionalInformation)
                .build();
    }

    /**
     * Builds additional information to include in the login response.
     *
     * @param userDetails the authenticated user details
     * @return Map containing additional user information
     */
    private static Map<String, Object> getAdditionalInformation(BackOfficeLoginProfileDTO userDetails) {
        Map<String, Object> additionalInformation = new HashMap<>();
        additionalInformation.put("firstName", userDetails.getFirstName());
        additionalInformation.put("lastName", userDetails.getLastName());
        additionalInformation.put("role", userDetails.getRole());
        additionalInformation.put("profileId", userDetails.getProfileId());
        return additionalInformation;
    }

    /**
     * Retrieves granted authorities for a user based on their assigned role.
     *
     * @param assignedRole the role assigned to the user
     * @return Set of SimpleGrantedAuthority objects representing user permissions
     */
    private Set<SimpleGrantedAuthority> getGrantedAuthorities(String assignedRole) {
        RoleDTO roleDTO = retrieveRole(assignedRole);
        List<String> rolePermissions = roleDTO.getPermissions().stream()
                .map(PermissionDTO::getName)
                .toList();

        Set<String> consolidatedPermissions = new HashSet<>(rolePermissions);
        return consolidatedPermissions.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
    }

    /**
     * Retrieves role information including associated permissions.
     *
     * @param roleName the name of the role to retrieve
     * @return RoleDTO containing role details and permissions
     * @throws OmnexaException if the role is not found
     */
    public RoleDTO retrieveRole(String roleName) {
        BackOfficeUserRole userRole = backOfficeUserRoleRepository
                .findFirstByName(roleName)
                .orElseThrow(() -> new OmnexaException(
                        messagePropertyConfig.getRoleMessage(NOT_FOUND).replace(ROLE_NAME, roleName)));

        return new RoleDTO(
                userRole.getName(),
                userRole.getDescription(),
                userRole.isActive(),
                userRole.getPermissions().stream()
                        .map(AuthorizationMapper::mapEntityToDTO)
                        .collect(Collectors.toSet()));
    }
}