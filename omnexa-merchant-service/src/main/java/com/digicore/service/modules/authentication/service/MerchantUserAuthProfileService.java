/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.service.modules.authentication.service;

import static com.digicore.common.lib.constant.message.MessagePlaceHolderConstant.PROFILE;

import com.digicore.common.lib.exception.OmnexaException;
import com.digicore.common.lib.onboarding.contract.OnboardingRequest;
import com.digicore.common.lib.onboarding.contract.OnboardingResponse;
import com.digicore.common.lib.profile.contract.service.ProfileService;
import com.digicore.common.lib.properties.MessagePropertyConfig;
import com.digicore.common.lib.util.BeanUtilWrapper;
import com.digicore.service.modules.authentication.data.model.MerchantUserAuthProfile;
import com.digicore.service.modules.authentication.data.repository.MerchantUserAuthProfileRepository;
import com.digicore.service.modules.profile.dto.response.MerchantOnboardingResponse;
import com.digicore.service.modules.profile.user.data.model.MerchantUserProfile;
import com.digicore.service.modules.profile.user.dto.request.MerchantUserOnboardingRequest;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
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

  private final MerchantUserAuthProfileRepository merchantUserAuthProfileRepository;

  private final MessagePropertyConfig messagePropertyConfig;

  private final EntityManager entityManager;
  private final PasswordEncoder passwordEncoder;
  @Transactional(rollbackOn = Exception.class)
  @Override
  public OnboardingResponse createProfile(OnboardingRequest request) {
    if (request instanceof MerchantUserOnboardingRequest req) {
      validateOnboardingRequest(req.getUsername());
      MerchantUserAuthProfile user = new MerchantUserAuthProfile();
      BeanUtilWrapper.copyNonNullProperties(req, user);
      MerchantUserProfile merchantUserProfile =
          entityManager.getReference(MerchantUserProfile.class, req.getMerchantProfileId());
      user.setMerchantUserProfile(merchantUserProfile);
      user.setPassword(passwordEncoder.encode(req.getPassword()));
      merchantUserAuthProfileRepository.save(user);
      return new MerchantOnboardingResponse();
    }
    throw new OmnexaException(
        messagePropertyConfig.getOnboardMessage(INVALID), "SE100", HttpStatus.BAD_REQUEST);
  }

  private void validateOnboardingRequest(String username) {
    if (merchantUserAuthProfileRepository.existsByUsername(username))
      logger.error(messagePropertyConfig.getOnboardMessage(DUPLICATE).replace(PROFILE, username));
  }
}
