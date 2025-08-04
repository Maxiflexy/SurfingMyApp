/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.merchant.modules.profile.dto.response;

import com.digicore.omnexa.common.lib.onboarding.contract.OnboardingResponse;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jul-01(Tue)-2025
 */
@Getter
@Setter
public class MerchantOnboardingResponse implements OnboardingResponse {
  /** The ID of the merchant profile associated with the user. */
  private Long merchantProfileId;

  private String merchantId;
  private String profileId;
  private String email;
  private String lastName;

  private String username;
}
