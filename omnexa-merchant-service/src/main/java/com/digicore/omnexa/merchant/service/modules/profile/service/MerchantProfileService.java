/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.merchant.service.modules.profile.service;

import static com.digicore.omnexa.common.lib.constant.message.MessageConstant.*;
import static com.digicore.omnexa.common.lib.constant.message.MessagePlaceHolderConstant.PROFILE;

import com.digicore.omnexa.common.lib.api.ApiError;
import com.digicore.omnexa.common.lib.exception.OmnexaException;
import com.digicore.omnexa.common.lib.onboarding.contract.OnboardingRequest;
import com.digicore.omnexa.common.lib.onboarding.contract.OnboardingResponse;
import com.digicore.omnexa.common.lib.profile.contract.request.ProfileEditRequest;
import com.digicore.omnexa.common.lib.profile.contract.response.ProfileEditResponse;
import com.digicore.omnexa.common.lib.profile.contract.response.ProfileInfoResponse;
import com.digicore.omnexa.common.lib.profile.contract.service.ProfileService;
import com.digicore.omnexa.common.lib.properties.MessagePropertyConfig;
import com.digicore.omnexa.common.lib.util.BeanUtilWrapper;
import com.digicore.omnexa.common.lib.util.RequestUtil;
import com.digicore.omnexa.merchant.service.modules.profile.data.model.MerchantProfile;
import com.digicore.omnexa.merchant.service.modules.profile.data.model.MerchantProfileStatus;
import com.digicore.omnexa.merchant.service.modules.profile.data.repository.MerchantProfileRepository;
import com.digicore.omnexa.merchant.service.modules.profile.dto.request.MerchantOnboardingRequest;
import com.digicore.omnexa.merchant.service.modules.profile.dto.response.MerchantOnboardingResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

/**
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jun-24(Tue)-2025
 */
@Service
@RequiredArgsConstructor
public class MerchantProfileService implements ProfileService {
  private final MerchantProfileRepository merchantProfileRepository;
  private final MessagePropertyConfig messagePropertyConfig;

  private static final Logger logger = LoggerFactory.getLogger(MerchantProfileService.class);

  @Transactional(rollbackOn = Exception.class)
  @Override
  public OnboardingResponse createProfile(OnboardingRequest profile) {
    if (profile instanceof MerchantOnboardingRequest req) {
      validateOnboardingRequest(req);
      MerchantProfile merchantProfile = new MerchantProfile();
      BeanUtilWrapper.copyNonNullProperties(req, merchantProfile);
      MerchantProfileStatus merchantProfileStatus = new MerchantProfileStatus();
      merchantProfileStatus.setMerchantProfile(merchantProfile);
      merchantProfile.setMerchantProfileStatus(merchantProfileStatus);
      merchantProfile.setMerchantId("");
      merchantProfileRepository.save(merchantProfile);
      return buildOnboardingResponse(merchantProfile);
    }
    throw new OmnexaException(
        messagePropertyConfig.getOnboardMessage(INVALID), "SE100", HttpStatus.BAD_REQUEST);
  }

  private void validateOnboardingRequest(MerchantOnboardingRequest req) {
    List<ApiError> onboardingErrors = new ArrayList<>();

    // 1. Required field checks using field label + supplier
    Map<String, Supplier<String>> requiredFields =
        Map.of(
            "Business Name", req::getBusinessName,
            "Business Email", req::getBusinessEmail,
            "First Name", req::getFirstName,
            "Last Name", req::getLastName,
            "Phone Number", req::getPhoneNumber,
            "Password", req::getPassword);

    requiredFields.forEach(
        (label, supplier) -> {
          if (RequestUtil.nullOrEmpty(supplier.get())) {
            String message =
                messagePropertyConfig.getOnboardMessage(REQUIRED).replace(PROFILE, label);
            onboardingErrors.add(new ApiError(message));
          }
        });

    // 2. Terms acceptance check
    if (!req.isTermsAccepted()) {
      String message =
          messagePropertyConfig
              .getOnboardMessage(REQUIRED)
              .replace(PROFILE, "Terms and Conditions");
      onboardingErrors.add(new ApiError(message));
    }

    // 3. Duplicate check
    if (merchantProfileRepository.existsByBusinessNameOrBusinessEmail(
        req.getBusinessName(), req.getBusinessEmail())) {
      String dupMessage =
          messagePropertyConfig.getOnboardMessage(DUPLICATE).replace(PROFILE, req.getBusinessName())
              + " or "
              + req.getBusinessEmail();
      onboardingErrors.add(new ApiError(dupMessage));
    }

    // 4. Throw if there are errors
    if (!onboardingErrors.isEmpty()) {
      onboardingErrors.forEach(
          error -> logger.warn("Merchant onboarding validation failed: {}", error.getMessage()));
      throw new OmnexaException(
          messagePropertyConfig.getOnboardMessage(INVALID),
          HttpStatus.BAD_REQUEST,
          onboardingErrors);
    }
  }

  private MerchantOnboardingResponse buildOnboardingResponse(MerchantProfile merchantProfile) {
    MerchantOnboardingResponse onboardingResponse = new MerchantOnboardingResponse();
    onboardingResponse.setMerchantProfileId(merchantProfile.getId());
    onboardingResponse.setUsername(merchantProfile.getBusinessEmail());
    return onboardingResponse;
  }
}
