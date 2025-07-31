/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.merchant.modules.onboarding.facade;

import static com.digicore.omnexa.common.lib.constant.message.MessageConstant.CONFLICT;

import com.digicore.omnexa.common.lib.enums.ProfileVerificationStatus;
import com.digicore.omnexa.common.lib.exception.OmnexaException;
import com.digicore.omnexa.common.lib.facade.contract.Facade;
import com.digicore.omnexa.common.lib.profile.contract.ProfileService;
import com.digicore.omnexa.merchant.modules.profile.helper.ProfileHelper;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jul-28(Mon)-2025
 */
@Component
@RequiredArgsConstructor
public class MerchantVerificationFacade implements Facade<String, Void> {
  private final ProfileService merchantProfileService;
  private final ProfileService merchantUserProfileService;
  private final ProfileHelper profileHelper;

  public static final String MERCHANT_EMAIL_VERIFICATION = "merchantEmailVerification";

  @Override
  public Optional<Void> process(String request) {
    String[] parts = request.split("-");
    if (parts.length < 2) {
      throw new OmnexaException("Invalid request format. Expected: merchantId-profileId");
    }

    String merchantId = "S-".concat(parts[0]);
    String profileId = parts[1];

    if (profileHelper.merchantIsVerified(merchantId)) {
      throw new OmnexaException(
          profileHelper.getMessagePropertyConfig().getOnboardMessage(CONFLICT),
          HttpStatus.CONFLICT);
    }

    merchantProfileService.updateProfileStatus(
        merchantId, null, ProfileVerificationStatus.VERIFIED);
    merchantUserProfileService.updateProfileStatus(
        profileId, null, ProfileVerificationStatus.VERIFIED);

    return Optional.empty();
  }

  @Override
  public String getType() {
    return MERCHANT_EMAIL_VERIFICATION;
  }
}
