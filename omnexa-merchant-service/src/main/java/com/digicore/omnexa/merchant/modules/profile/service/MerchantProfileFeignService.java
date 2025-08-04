/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.merchant.modules.profile.service;

import com.digicore.omnexa.common.lib.api.ApiError;
import com.digicore.omnexa.common.lib.enums.ProfileStatus;
import com.digicore.omnexa.common.lib.enums.ProfileVerificationStatus;
import com.digicore.omnexa.common.lib.exception.OmnexaException;
import com.digicore.omnexa.common.lib.onboarding.contract.OnboardingRequest;
import com.digicore.omnexa.common.lib.onboarding.contract.OnboardingResponse;
import com.digicore.omnexa.common.lib.profile.contract.ProfileService;
import com.digicore.omnexa.common.lib.profile.dto.response.ProfileInfoResponse;
import com.digicore.omnexa.common.lib.util.BeanUtilWrapper;
import com.digicore.omnexa.common.lib.util.PaginatedResponse;
import com.digicore.omnexa.common.lib.util.RequestUtil;
import com.digicore.omnexa.merchant.modules.profile.data.model.MerchantProfile;
import com.digicore.omnexa.merchant.modules.profile.data.model.kyc.MerchantKycProfile;
import com.digicore.omnexa.merchant.modules.profile.dto.request.MerchantOnboardingRequest;
import com.digicore.omnexa.merchant.modules.profile.dto.response.MerchantOnboardingResponse;
import com.digicore.omnexa.merchant.modules.profile.helper.ProfileHelper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static com.digicore.omnexa.common.lib.constant.message.MessageConstant.*;
import static com.digicore.omnexa.common.lib.constant.message.MessagePlaceHolderConstant.PROFILE;
import static com.digicore.omnexa.common.lib.constant.system.SystemConstant.*;
import static com.digicore.omnexa.merchant.modules.profile.helper.ProfileHelper.*;

/**
 * Service implementation for merchant profile management operations.
 *
 * <p>This service handles the creation and validation of merchant profiles, ensuring data integrity
 * and business rule compliance during onboarding.
 *
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jun-24(Tue)-2025
 */
@Service
@RequiredArgsConstructor
public class MerchantProfileFeignService implements ProfileService {
  private static final Logger log = LoggerFactory.getLogger(MerchantProfileFeignService.class);

  private final ProfileHelper profileHelper;

  @Override
  public void updateProfileStatus(
      String profileId,
      ProfileStatus profileStatus,
      ProfileVerificationStatus profileVerificationStatus) {
    profileHelper
        .getMerchantProfileRepository()
        .conditionalUpdateProfileStatuses(
            profileStatus != null ? profileStatus.toString() : null,
            profileVerificationStatus != null ? profileVerificationStatus.toString() : null,
            profileId);
  }

  @Override
  public PaginatedResponse<ProfileInfoResponse> getAllProfiles(Integer pageNumber, Integer pageSize) {
    return ProfileService.super.getAllProfiles(pageNumber, pageSize);
  }
}
