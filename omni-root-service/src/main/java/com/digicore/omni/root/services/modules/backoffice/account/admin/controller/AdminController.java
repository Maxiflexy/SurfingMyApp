///*
// * Created by Monsuru (7/8/2022)
// */
//package com.digicore.omni.root.services.modules.backoffice.account.admin.controller;
//
//
//import com.digicore.omni.root.lib.modules.common.apimodel.SignIn;
//import com.digicore.omni.root.services.modules.backoffice.account.admin.service.AdminAccountService;
//import com.digicore.omni.root.services.modules.common.utils.CommonUtils;
//import com.digicore.omni.root.services.modules.merchants.authentication.apiModel.LoginResponseApiModel;
//import com.digicore.request.processor.annotations.LogActivity;
//import com.digicore.request.processor.enums.LogActivityType;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api/v1/backoffice-admin/process/")
//@Validated
//public class AdminController {
//
//    private final AdminAccountService adminAccountService;
//
//    private final String pageAndSizeDefault = "0";
//
//    @Autowired
//    public AdminController(AdminAccountService adminAccountService) {
//        this.adminAccountService = adminAccountService;
//    }
//
//    //@LogActivity(activity = LogActivityType.LOGIN_ACTIVITY)
//    @PostMapping("login")
//    public ResponseEntity<Object> login(@RequestBody SignIn signIn) {
//        return adminAccountService.authenticateAdminUser(signIn);
//    }
//
//    @LogActivity(activity = LogActivityType.DELETE_ACTIVITY)
//    @DeleteMapping("delete-backoffice-profile")
//    @PreAuthorize("authentication.principal.claims.get('username') == 'zeus'")
//    public ResponseEntity<Object> deleteBackOfficeProfile(@RequestParam("email") String email)  {
//        adminAccountService.deleteBackOfficeProfile(email);
//        return CommonUtils.buildSuccessResponse(null,null);
//    }
//
//    @LogActivity(activity = LogActivityType.DELETE_ACTIVITY)
//    @DeleteMapping("delete-merchant-profile")
//    @PreAuthorize("authentication.principal.claims.get('username') == 'zeus'")
//    public ResponseEntity<Object> deleteMerchantProfile(@RequestParam("email") String email)  {
//        adminAccountService.deleteMerchantProfile(email);
//        return CommonUtils.buildSuccessResponse(null,null);
//
//    }
//
//}
