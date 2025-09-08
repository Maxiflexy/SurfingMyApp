package com.digicore.omni.root.services.modules.merchants.account.controller;

import com.digicore.omni.data.lib.modules.merchant.dto.MerchantUserDTO;
import com.digicore.omni.root.services.modules.common.utils.CommonUtils;
import com.digicore.omni.root.services.modules.merchants.account.service.MerchantUserService;
import com.digicore.omni.root.services.modules.merchants.account.util.MerchantAssemblerModel;
import com.digicore.request.processor.annotations.LogActivity;
import com.digicore.request.processor.annotations.TokenValid;
import com.digicore.request.processor.enums.LogActivityType;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/merchant-account/process/")
public class MerchantUserController {

    @Autowired
    public MerchantUserService merchantUserService;

    @Autowired
    private MerchantAssemblerModel merchantAssemblerModel;

    @TokenValid()
    @PatchMapping("toggle-live-mode")
    public ResponseEntity<Object> toggleLiveMode() {
            return CommonUtils.buildSuccessResponse(merchantUserService.toggleLiveMode());
    }

    //todo temp pass
    @TokenValid()
    @PostMapping("invite-user")
    public ResponseEntity<Object> inviteUser( @Valid @RequestBody MerchantUserDTO merchantUserDTO) {
        return merchantUserService.inviteUser(merchantUserDTO);
    }

    @TokenValid()
    @GetMapping("get-available-roles")
    public ResponseEntity<Object> getRoles()  {
        return CommonUtils.buildSuccessResponse(merchantUserService.getAvailableRoles());
    }

    @TokenValid()
    @GetMapping("get-all-merchants-users")
    public ResponseEntity<Object> getAllMerchantsUser(@RequestParam(name = "pageNumber", defaultValue = "0") int pageNumber, @RequestParam(name = "pageSize", defaultValue = "5") int pageSize) {
        return CommonUtils.buildSuccessResponse(merchantUserService.getAllMerchantUsers(pageNumber,pageSize));

    }

    @TokenValid()
    @GetMapping("get-user")
    public ResponseEntity<Object> getAMerchantsUser(@RequestParam(name = "email") String email) {
        return CommonUtils.buildSuccessResponse(merchantUserService.getMerchantUsersDetail(email));

    }

    @LogActivity(activity = LogActivityType.UPDATE_ACTIVITY)
    @TokenValid()
    @PatchMapping("toggle-enable-disable-merchant-user")
    public ResponseEntity<Object> toggleEnableDisableMerchantUser(@RequestParam("email") String email) {
        return CommonUtils.buildSuccessResponse(merchantUserService.toggleEnableDisableMerchantUser(email));
    }

    @TokenValid()
    @GetMapping("search-merchants-users-by-name")
    public ResponseEntity<Object> getAllMerchantsUser(@RequestParam(name = "pageNumber", defaultValue = "0") int pageNumber, @RequestParam(name = "pageSize", defaultValue = "5") int pageSize, @RequestParam("name") String name) {
        return CommonUtils.buildSuccessResponse(merchantUserService.searchMerchantAdminByName(pageNumber,pageSize, name));

    }

}
