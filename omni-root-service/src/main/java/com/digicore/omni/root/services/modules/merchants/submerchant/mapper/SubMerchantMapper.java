package com.digicore.omni.root.services.modules.merchants.submerchant.mapper;

import com.digicore.omni.data.lib.modules.merchant.model.MerchantProfile;
import com.digicore.omni.data.lib.modules.merchant.model.SubMerchantInvite;
import com.digicore.omni.root.services.modules.merchants.submerchant.model.request.SubMerchantInviteRequest;
import com.digicore.omni.root.services.modules.merchants.submerchant.model.response.SubMerchantResponse;
import net.bytebuddy.utility.RandomString;
import org.springframework.stereotype.Component;

/**
 * @author mosh
 * @role software engineer
 * @createdOn 13 Wed Aug, 2025
 */

@Component
public class SubMerchantMapper {

    public SubMerchantInvite toSubMerchantInvite(SubMerchantInviteRequest request, MerchantProfile merchantProfile){
        SubMerchantInvite subMerchantInvite = new SubMerchantInvite();
        subMerchantInvite.setBusinessName(request.getBusinessName());
        subMerchantInvite.setEmail(request.getEmail());
        subMerchantInvite.setMerchantProfileId(merchantProfile.getId());
        subMerchantInvite.setMerchantProfile(merchantProfile);
        subMerchantInvite.setInviteCode(RandomString.make(25));

        return subMerchantInvite;
    }

    public SubMerchantResponse mapToSubMerchantResponse(MerchantProfile merchantProfile) {
        String businessName = merchantProfile.getMerchantBusinessDetails() != null
                ? merchantProfile.getMerchantBusinessDetails().getBusinessName()
                : "";

        String subMerchantName = String.format("%s %s",
                merchantProfile.getFirstName() != null ? merchantProfile.getFirstName() : "",
                merchantProfile.getLastName() != null ? merchantProfile.getLastName() : "").trim();

        return SubMerchantResponse.builder()
                .merchantId(merchantProfile.getMerchantId())
                .businessName(businessName)
                .subMerchantName(subMerchantName)
                .email(merchantProfile.getEmail())
                .phoneNumber(merchantProfile.getPhoneNumber())
                .status(merchantProfile.getMerchantStatus())
                .dateCreated(merchantProfile.getDateCreated())
                .isActive(merchantProfile.isActive())
                .build();
    }
}
