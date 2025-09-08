package com.digicore.omni.root.services.modules.merchants.submerchant.model.request;


import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Onyekachi Ejemba
 * @createdOn Aug-14(Thu)-2025
 */
@Getter
@Setter
public class VerificationRequest {
    @NotBlank(message = "Verification code is required")
    private String verificationCode;
}
