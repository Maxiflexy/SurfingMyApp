/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.merchant.modules.profile.dto.request;

import com.digicore.omnexa.common.lib.onboarding.contract.OnboardingRequest;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jul-28(Mon)-2025
 */
@Getter
@Setter
public class MerchantApiKeyOnboardingRequest implements OnboardingRequest {
  private Long merchantProfileId;
}
