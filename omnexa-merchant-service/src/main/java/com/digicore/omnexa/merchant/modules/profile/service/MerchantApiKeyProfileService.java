/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.merchant.modules.profile.service;

import static com.digicore.omnexa.common.lib.constant.message.MessageConstant.INVALID;
import static com.digicore.omnexa.common.lib.constant.system.SystemConstant.SYSTEM_DEFAULT_INVALID_REQUEST_ERROR;
import static com.digicore.omnexa.common.lib.util.AESUtil.generateBase64AESKey;
import static com.digicore.omnexa.common.lib.util.ApiKeyGenerator.generateApiKey;

import com.digicore.omnexa.common.lib.exception.OmnexaException;
import com.digicore.omnexa.common.lib.onboarding.contract.OnboardingRequest;
import com.digicore.omnexa.common.lib.onboarding.contract.OnboardingResponse;
import com.digicore.omnexa.common.lib.profile.contract.ProfileService;
import com.digicore.omnexa.common.lib.properties.MessagePropertyConfig;
import com.digicore.omnexa.merchant.modules.profile.data.model.MerchantApiKeyProfile;
import com.digicore.omnexa.merchant.modules.profile.data.repository.MerchantApiKeyProfileRepository;
import com.digicore.omnexa.merchant.modules.profile.dto.request.MerchantApiKeyOnboardingRequest;
import com.digicore.omnexa.merchant.modules.profile.dto.response.MerchantOnboardingResponse;
import com.digicore.omnexa.merchant.modules.profile.helper.ProfileHelper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jul-28(Mon)-2025
 */
@Service
@RequiredArgsConstructor
public class MerchantApiKeyProfileService implements ProfileService {
  private static final Logger log = LoggerFactory.getLogger(MerchantApiKeyProfileService.class);

  private final MerchantApiKeyProfileRepository merchantApiKeyProfileRepository;

  private final ProfileHelper profileHelper;
  private final MessagePropertyConfig messagePropertyConfig;

  @Transactional(rollbackFor = Exception.class)
  @Override
  public OnboardingResponse createProfile(OnboardingRequest profile) {
    MerchantApiKeyOnboardingRequest request = castToMerchantApiKeyOnboardingRequest(profile);
    MerchantApiKeyProfile merchantApiKeyProfile = new MerchantApiKeyProfile();
    merchantApiKeyProfile.setMerchantProfile(
        profileHelper.getMerchantProfileByReference(request.getMerchantProfileId()));
    merchantApiKeyProfile.setTestPublicKey(profileHelper.encrypt(generateApiKey()));
    merchantApiKeyProfile.setTestSecretKey(profileHelper.encrypt(generateApiKey()));
    merchantApiKeyProfile.setTestEncryptionKey(profileHelper.encrypt(generateBase64AESKey()));
    merchantApiKeyProfile.setLiveMode(false);

    merchantApiKeyProfileRepository.save(merchantApiKeyProfile);
    return new MerchantOnboardingResponse();
  }

  private MerchantApiKeyOnboardingRequest castToMerchantApiKeyOnboardingRequest(
      OnboardingRequest profile) {
    if (!(profile instanceof MerchantApiKeyOnboardingRequest)) {
      throw new OmnexaException(
          messagePropertyConfig.getOnboardMessage(INVALID, SYSTEM_DEFAULT_INVALID_REQUEST_ERROR),
          HttpStatus.BAD_REQUEST);
    }
    return (MerchantApiKeyOnboardingRequest) profile;
  }
}
