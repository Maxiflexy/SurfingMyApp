package com.digicore.omni.root.services.modules.merchants.submerchant.model.response;


import com.digicore.omni.data.lib.modules.common.enums.UserStatus;
import com.digicore.omni.data.lib.modules.merchant.enums.MerchantStatus;
import com.digicore.omni.data.lib.modules.merchant.enums.SubMerchantStatus;
import lombok.*;

import java.time.LocalDateTime;

/**
 * @author Onyekachi Ejemba
 * @createdOn Aug-14(Thu)-2025
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubMerchantResponse {
    private String merchantId;
    private String businessName;
    private String subMerchantName; // firstName + lastName
    private String email;
    private String phoneNumber;
    private MerchantStatus status;
    private LocalDateTime dateCreated;
    private boolean isActive;
}
