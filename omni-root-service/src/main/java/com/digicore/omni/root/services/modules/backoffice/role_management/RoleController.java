package com.digicore.omni.root.services.modules.backoffice.role_management;




import com.digicore.omni.data.lib.modules.backoffice.dto.BackOfficeRoleInviteDTO;
import com.digicore.omni.data.lib.modules.backoffice.dto.CustomRoleDTO;
import com.digicore.omni.data.lib.modules.backoffice.dto.EditRoleDTO;
import com.digicore.omni.root.lib.modules.backoffice.service.BackOfficeRoleManagementService;
import com.digicore.omni.root.services.modules.common.utils.CommonUtils;
import com.digicore.request.processor.annotations.TokenValid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jan-30(Mon)-2023
 */

@RestController
@RequestMapping("/api/v1/backoffice-role/process/")
@Slf4j
@RequiredArgsConstructor
public class RoleController {

    private final BackOfficeRoleManagementService roleManagementService;

    @TokenValid()
    @GetMapping("get-available-roles")
    public ResponseEntity<Object> getRoles()  {
        return CommonUtils.buildSuccessResponse(roleManagementService.getAvailableBackOfficeRoles());
    }

    @TokenValid()
    @GetMapping("get-available-roles-by-filter")
    public ResponseEntity<Object> getAvailableRolesByRoleName(@RequestParam(name = "roleName") String roleName){
        return CommonUtils.buildSuccessResponse(roleManagementService.getAvailableRolesByRoleName(roleName));
    }

    @TokenValid()
    @GetMapping("get-available-backoffice-roles-and-users")
    public ResponseEntity<Object> getAllBackOfficeRolesAndUsers(@RequestParam(value = "pageNumber", defaultValue = "0",required = false) int pageNumber,
                                                                                 @RequestParam(value = "pageSize", defaultValue = "10",required = false) int pageSize) {
        return CommonUtils.buildSuccessResponse(roleManagementService.getRolesWithUserAndCount(pageNumber, pageSize));
    }

    @TokenValid()
    @PostMapping("create-custom-role")
    public ResponseEntity<Object> createBackOfficeCustomRole(@RequestBody CustomRoleDTO customRoleDTO) {
        return CommonUtils.buildSuccessResponse(roleManagementService.createCustomBackOfficeRole(customRoleDTO));
    }

    @TokenValid()
    @GetMapping("get-all-system-permissions")
    public ResponseEntity<Object> getAllSystemPermissions() {
        return CommonUtils.buildSuccessResponse(roleManagementService.getAllSystemPermissions());
    }

    @TokenValid()
    @PatchMapping("invite-backoffice-user-to-new-role")
    public ResponseEntity<Object> inviteBackOfficeUserToNewRole(@RequestBody @Validated BackOfficeRoleInviteDTO roleInviteDTO) {
        return CommonUtils.buildSuccessResponse(roleManagementService.inviteBackOfficeUserToNewRole(roleInviteDTO));
    }

    @TokenValid()
    @GetMapping("search-backoffice-user-by-role")
    public ResponseEntity<Object> searchBackOfficeUserByRole(@RequestParam(name = "roleName") String roleName,
                                                             @RequestParam(value = "pageNumber", defaultValue = "0",required = false) int pageNumber,
                                                             @RequestParam(value = "pageSize", defaultValue = "10",required = false) int pageSize) {
        return CommonUtils.buildSuccessResponse(roleManagementService.searchBackOfficeUserByRole(roleName, pageNumber, pageSize));
    }

    @TokenValid()
    @PatchMapping("edit-role-details")
    public ResponseEntity<Object> editRoleDetails(@RequestBody @Validated EditRoleDTO editRoleDTO) {
        return CommonUtils.buildSuccessResponse(roleManagementService.updateExistingRole(editRoleDTO));
    }


}
