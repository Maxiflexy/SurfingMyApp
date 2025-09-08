package com.digicore.omni.root.services.modules.merchants.submerchant.model.dto;


import com.digicore.omni.root.services.modules.merchants.submerchant.model.request.SubMerchantOnboardRequest;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Onyekachi Ejemba
 * @createdOn Aug-14(Thu)-2025
 */
@Getter
@Setter
public class SubMerchantRegistrationDTO extends SubMerchantOnboardRequest {

    private String parentMerchantId;
    private String businessName;
}
