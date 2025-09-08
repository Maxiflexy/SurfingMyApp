/*
 * Created by Monsuru (7/8/2022)
 */
package com.digicore.omni.root.services.modules.backoffice.onboarding.controller;


import com.digicore.omni.root.services.modules.backoffice.onboarding.service.OnBoardingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/backoffice-signup/process/")
@Validated
public class OnBoardingController {

    private final OnBoardingService onBoardingService;

    @Autowired
    public OnBoardingController(OnBoardingService onBoardingService) {
        this.onBoardingService = onBoardingService;
    }

//    @PatchMapping("confirm-{inviteToken}-token")
//    public ResponseEntity<Object> confirmInvitationToken(@PathVariable String inviteToken)  {
//
//            return CommonUtils.buildSuccessResponse(onBoardingService.confirmInvitationToken(inviteToken));
//
//    }

//    @PatchMapping("confirm-{otp}-otp")
//    public ResponseEntity<Object> confirmInvitationOtp(@PathVariable Integer otp)  {
//        try{
//            return CommonUtils.buildSuccessResponse(onBoardingService.confirmInvitationOtp(otp));
//        }catch (InvalidInviteException e){
//            throw new ApiException(e);
//        }
//    }
}
