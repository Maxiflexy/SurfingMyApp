/*
 * Created by Monsuru (7/8/2022)
 */
package com.digicore.omni.root.services.modules.backoffice.account.user.controller;

import com.digicore.omni.data.lib.modules.backoffice.dto.BackOfficeUserDTO;
import com.digicore.omni.root.services.modules.backoffice.account.user.service.BackOfficeUserService;
import com.digicore.omni.root.services.modules.common.utils.CommonUtils;
import com.digicore.request.processor.annotations.LogActivity;
import com.digicore.request.processor.annotations.TokenValid;
import com.digicore.request.processor.enums.LogActivityType;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/backoffice-user/process/")
@Validated
@Slf4j
@RequiredArgsConstructor
public class BackOfficeUserController {

    private final BackOfficeUserService backOfficeUserService;


    //todo temp pass

    @TokenValid()
    @PostMapping("send-invitation")
    public ResponseEntity<Object> inviteUser(@Valid @RequestBody BackOfficeUserDTO backOfficeUserDTO) {
        return backOfficeUserService.inviteBackOfficeUser(backOfficeUserDTO);
    }

    //    @PreAuthorize("hasAuthority('zeus-view-user')")
    @TokenValid()
    @GetMapping("get-all-backoffice-users-details")
    public ResponseEntity<Object> getAllBackOfficeUsers(@RequestParam(name = "pageNumber", defaultValue = "0") int pageNumber,
                                                        @RequestParam(name = "pageSize", defaultValue = "5") int pageSize) {
        return CommonUtils.buildSuccessResponse(backOfficeUserService.getAllBackOfficeUsers(pageNumber, pageSize));
    }

    @TokenValid()
    @GetMapping("filter-backoffice-users-by-name")
    public ResponseEntity<Object> filterBackofficeUsersByName(@RequestParam(name = "pageNumber", defaultValue = "0") int pageNumber,
                                                              @RequestParam(name = "pageSize", defaultValue = "10") int pageSize,
                                                              @RequestParam(name = "name") String backOfficeUserName) {
        return CommonUtils.buildSuccessResponse(backOfficeUserService.filterBackOfficeUsersByName(pageNumber, pageSize, backOfficeUserName));
    }

    @TokenValid()
    @GetMapping("get-backoffice-user-{backOfficeUserId}-details")
    public ResponseEntity<Object> getBackOfficeUser(@PathVariable String backOfficeUserId) {
        return CommonUtils.buildSuccessResponse(backOfficeUserService.getBackOfficeUserById(backOfficeUserId));
    }

    @LogActivity(activity = LogActivityType.UPDATE_ACTIVITY)
    @TokenValid()
    @PatchMapping("toggle-enable-disable-merchant")
    public ResponseEntity<Object> toggleEnableDisableMerchant(@RequestParam("merchantId") String merchantId) {
        return CommonUtils.buildSuccessResponse(backOfficeUserService.toggleEnableDisableMerchantAndMerchantUser(merchantId));
    }

    @LogActivity(activity = LogActivityType.UPDATE_ACTIVITY)
    @TokenValid()
    @PatchMapping("toggle-enable-disable-backoffice-user")
    public ResponseEntity<Object> toggleEnableDisableBackOfficeUser(@RequestParam("email") String email) {
        return CommonUtils.buildSuccessResponse(backOfficeUserService.toggleEnableDisableBackOfficeUser(email));
    }

}
