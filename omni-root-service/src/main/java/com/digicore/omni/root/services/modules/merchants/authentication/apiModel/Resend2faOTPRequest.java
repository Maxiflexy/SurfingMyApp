package com.digicore.omni.root.services.modules.merchants.authentication.apiModel;


import com.digicore.common.validator.RequestBodyChecker;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Resend2faOTPRequest {

    @RequestBodyChecker(message = "username is required")
    private   String username;
    @RequestBodyChecker(message = "loginKey is required")
    private   String loginKey;
}
