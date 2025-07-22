/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.merchant.service.modules.profile.user.service;

import static com.digicore.omnexa.common.lib.constant.message.MessageConstant.*;
import static com.digicore.omnexa.common.lib.constant.message.MessageConstant.REQUIRED;
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
import com.digicore.omnexa.merchant.service.modules.profile.dto.request.MerchantOnboardingRequest;
import com.digicore.omnexa.merchant.service.modules.profile.dto.response.MerchantOnboardingResponse;
import com.digicore.omnexa.merchant.service.modules.profile.user.data.model.MerchantUserProfile;
import com.digicore.omnexa.merchant.service.modules.profile.user.data.model.MerchantUserProfileStatus;
import com.digicore.omnexa.merchant.service.modules.profile.user.data.repository.MerchantUserProfileRepository;
import com.digicore.omnexa.merchant.service.modules.profile.user.dto.request.MerchantUserOnboardingRequest;
import jakarta.persistence.EntityManager;

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
public class MerchantUserProfileService implements ProfileService {
  private final MerchantUserProfileRepository merchantUserProfileRepository;
  private final EntityManager entityManager;
  private final MessagePropertyConfig messagePropertyConfig;

  private static final Logger logger = LoggerFactory.getLogger(MerchantUserProfileService.class);

  @Transactional(rollbackOn = Exception.class)
  @Override
  public OnboardingResponse createProfile(OnboardingRequest profile) {
    if (profile instanceof MerchantUserOnboardingRequest req) {
      validateOnboardingRequest(req);
      MerchantUserProfile user = new MerchantUserProfile();
      BeanUtilWrapper.copyNonNullProperties(req, user);
      MerchantProfile merchantRef =
          entityManager.getReference(MerchantProfile.class, req.getMerchantProfileId());
      user.setMerchantProfile(merchantRef);
      MerchantUserProfileStatus status = new MerchantUserProfileStatus();
      status.setMerchantUserProfile(user);
      user.setMerchantUserProfileStatus(status);
      merchantUserProfileRepository.save(user);
      return buildOnboardingResponse(user);
    }
    throw new OmnexaException(
        messagePropertyConfig.getOnboardMessage(INVALID), "SE100", HttpStatus.BAD_REQUEST);
  }

  private void validateOnboardingRequest(MerchantUserOnboardingRequest req) {
    List<ApiError> onboardingErrors = new ArrayList<>();

    // 1. Required field checks using field label + supplier
    Map<String, Supplier<String>> requiredFields =
            Map.of(
                    "First Name", req::getFirstName,
                    "Last Name", req::getLastName);

    requiredFields.forEach(
            (label, supplier) -> {
              if (RequestUtil.nullOrEmpty(supplier.get())) {
                String message =
                        messagePropertyConfig.getOnboardMessage(REQUIRED).replace(PROFILE, label);
                onboardingErrors.add(new ApiError(message));
              }
            });

    // 4. Throw if there are errors
    if (!onboardingErrors.isEmpty()) {
      onboardingErrors.forEach(
              error -> logger.warn("Merchant user onboarding validation failed: {}", error.getMessage()));
      throw new OmnexaException(
              messagePropertyConfig.getOnboardMessage(INVALID),
              HttpStatus.BAD_REQUEST,
              onboardingErrors);
    }
  }

  private MerchantOnboardingResponse buildOnboardingResponse(MerchantUserProfile merchantProfile) {
    MerchantOnboardingResponse onboardingResponse = new MerchantOnboardingResponse();
    onboardingResponse.setMerchantProfileId(merchantProfile.getId());
    return onboardingResponse;
  }
}
