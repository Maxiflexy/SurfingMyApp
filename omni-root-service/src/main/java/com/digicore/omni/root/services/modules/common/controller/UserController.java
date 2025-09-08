package com.digicore.omni.root.services.modules.common.controller;

import com.digicore.omni.root.services.modules.backoffice.authentication.service.BackofficeLoginService;
import com.digicore.omni.root.services.modules.common.authentication.TokenRefresher;
import com.digicore.omni.root.services.modules.common.utils.CommonUtils;
import com.digicore.request.processor.annotations.TokenValid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

/**
 * @author Oluwatobi Ogunwuyi
 * @createdOn Sep-04(Sun)-2022
 */

@RestController
@RequestMapping("/api/v1/user/process/")
public class UserController {

    private final TokenRefresher tokenRefresher;

    private final BackofficeLoginService backofficeLoginService;

    public UserController(TokenRefresher tokenRefresher, BackofficeLoginService backofficeLoginService) {
        this.tokenRefresher = tokenRefresher;
        this.backofficeLoginService = backofficeLoginService;
    }

    @TokenValid()
    @GetMapping("retrieve-refresh-token")
    public ResponseEntity<Object> getRefreshToken(Principal principal, @RequestHeader("Authorization") String expiredToken)  {
        return CommonUtils.buildSuccessResponse(tokenRefresher.getRefreshToken(principal,expiredToken));
    }

    @TokenValid()
    @PatchMapping("revoke-token")
    public ResponseEntity<Object> revokeToken()  {
        tokenRefresher.revokeToken();
        return CommonUtils.buildSuccessResponse("Token revoked successfully");
    }

}
