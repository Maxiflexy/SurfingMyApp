package com.digicore.omni.root.services.modules.merchants.submerchant.model.response;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Onyekachi Ejemba
 * @createdOn Aug-14(Thu)-2025
 */
@Getter
@Setter
@Builder
public class SubMerchantRegistrationResponse {
    private String subMerchantId;
    private String message;
}
