/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.backoffice.modules.user.authentication.dto;

import com.digicore.omnexa.common.lib.onboarding.contract.OnboardingRequest;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jul-18(Fri)-2025
 */
@Getter
@Setter
public class BackOfficeUserAuthProfileOnboardingRequest implements OnboardingRequest {
  private Long backOfficeUserProfileId;
  private String username;
  private String password;
}
