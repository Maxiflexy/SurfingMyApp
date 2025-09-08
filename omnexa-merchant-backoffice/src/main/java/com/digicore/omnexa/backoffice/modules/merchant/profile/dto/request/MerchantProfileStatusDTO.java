/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.backoffice.modules.merchant.profile.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO for merchant profile status toggle requests in the maker-checker workflow.
 *
 * @author Generated
 * @createdOn Aug-12(Tue)-2025
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MerchantProfileStatusDTO {

    public static final String MERCHANT_PROFILE_STATUS_DTO =
            "com.digicore.omnexa.backoffice.modules.merchant.profile.dto.request.MerchantProfileStatusDTO";

    private String merchantId;
    private boolean enabled;
}