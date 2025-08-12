package com.digicore.omnexa.backoffice.modules.user.authorization.controller;


import com.digicore.omnexa.common.lib.api.ControllerResponse;
import com.digicore.omnexa.common.lib.authorization.contract.PermissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.digicore.omnexa.common.lib.api.ApiVersion.API_V1;
import static com.digicore.omnexa.common.lib.swagger.constant.authorization.AuthorizationSwaggerDocConstant.*;

/**
 * @author Onyekachi Ejemba
 * @createdOn Aug-08(Fri)-2025
 */
@RestController
@RequestMapping(API_V1 + PERMISSION_API)
@RequiredArgsConstructor
@Tag(name = PERMISSIONS_MANAGEMENT_CONTROLLER_TITLE, description = PERMISSIONS_MANAGEMENT_CONTROLLER_DESCRIPTION)
public class BackOfficePermissionController {

    private final PermissionService permissionService;

    @GetMapping()
    @PreAuthorize("hasAuthority('view-backoffice-permissions')")
    @Operation(
            summary = PERMISSION_CONTROLLER_RETRIEVE_ALL_PERMISSIONS_TITLE,
            description = PERMISSION_CONTROLLER_RETRIEVE_ALL_PERMISSIONS_DESCRIPTION)
    public ResponseEntity<Object> getAllPermissions() {
        return ControllerResponse.buildSuccessResponse(
                permissionService.getAllPermissions(), "Request retrieved successfully");
    }
}
