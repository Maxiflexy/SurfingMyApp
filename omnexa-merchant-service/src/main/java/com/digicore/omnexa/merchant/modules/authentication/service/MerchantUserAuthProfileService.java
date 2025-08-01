/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.merchant.modules.authentication.service;

import static com.digicore.omnexa.common.lib.constant.message.MessageConstant.*;
import static com.digicore.omnexa.common.lib.constant.message.MessagePlaceHolderConstant.PROFILE;
import static com.digicore.omnexa.common.lib.constant.system.SystemConstant.*;

import com.digicore.omnexa.common.lib.exception.OmnexaException;
import com.digicore.omnexa.common.lib.onboarding.contract.OnboardingRequest;
import com.digicore.omnexa.common.lib.onboarding.contract.OnboardingResponse;
import com.digicore.omnexa.common.lib.profile.contract.ProfileService;
import com.digicore.omnexa.common.lib.util.BeanUtilWrapper;
import com.digicore.omnexa.merchant.modules.authentication.data.model.MerchantUserAuthProfile;
import com.digicore.omnexa.merchant.modules.profile.dto.response.MerchantOnboardingResponse;
import com.digicore.omnexa.merchant.modules.profile.helper.ProfileHelper;
import com.digicore.omnexa.merchant.modules.profile.user.dto.request.MerchantUserOnboardingRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

/**
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jul-06(Sun)-2025
 */
@Service
@RequiredArgsConstructor
public class MerchantUserAuthProfileService implements ProfileService {

  private static final Logger logger =
      LoggerFactory.getLogger(MerchantUserAuthProfileService.class);
  private final ProfileHelper profileHelper;

  @Transactional(rollbackOn = Exception.class)
  @Override
  public OnboardingResponse createProfile(OnboardingRequest request) {
    if (request instanceof MerchantUserOnboardingRequest req) {
      validateOnboardingRequest(req.getUsername());
      MerchantUserAuthProfile user = new MerchantUserAuthProfile();
      BeanUtilWrapper.copyNonNullProperties(req, user);
      user.setMerchantUserProfile(
          profileHelper.getMerchantUserProfileByReference(req.getMerchantProfileId()));
      user.setPassword(profileHelper.getPasswordEncoder().encode(req.getPassword()));
      profileHelper.getMerchantUserAuthProfileRepository().save(user);
      return new MerchantOnboardingResponse();
    }
    throw new OmnexaException(
        profileHelper
            .getMessagePropertyConfig()
            .getOnboardMessage(INVALID, SYSTEM_DEFAULT_INVALID_REQUEST_ERROR),
        HttpStatus.BAD_REQUEST);
  }

  private void validateOnboardingRequest(String username) {
    if (profileHelper.getMerchantUserAuthProfileRepository().existsByUsername(username)) {
      String errorMessage =
          profileHelper
              .getMessagePropertyConfig()
              .getOnboardMessage(DUPLICATE, SYSTEM_DEFAULT_DUPLICATE_ERROR)
              .replace(PROFILE, username);
      logger.error(errorMessage);
      throw new OmnexaException(errorMessage, HttpStatus.BAD_REQUEST);
    }
  }
}
