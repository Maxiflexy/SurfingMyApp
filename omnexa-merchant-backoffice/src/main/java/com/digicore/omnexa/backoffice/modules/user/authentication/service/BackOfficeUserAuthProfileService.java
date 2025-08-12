/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.backoffice.modules.user.authentication.service;

import static com.digicore.omnexa.common.lib.constant.message.MessageConstant.*;
import static com.digicore.omnexa.common.lib.constant.message.MessagePlaceHolderConstant.PROFILE;
import static com.digicore.omnexa.common.lib.constant.system.SystemConstant.SYSTEM_DEFAULT_DUPLICATE_ERROR;
import static com.digicore.omnexa.common.lib.constant.system.SystemConstant.SYSTEM_DEFAULT_INVALID_REQUEST_ERROR;

import com.digicore.omnexa.backoffice.modules.user.authentication.data.model.BackOfficeUserAuthProfile;
import com.digicore.omnexa.backoffice.modules.user.authentication.data.repository.BackOfficeUserAuthProfileRepository;
import com.digicore.omnexa.backoffice.modules.user.authentication.dto.BackOfficeUserAuthProfileOnboardingRequest;
import com.digicore.omnexa.backoffice.modules.user.profile.data.model.BackOfficeUserProfile;
import com.digicore.omnexa.common.lib.exception.OmnexaException;
import com.digicore.omnexa.common.lib.onboarding.contract.OnboardingRequest;
import com.digicore.omnexa.common.lib.onboarding.contract.OnboardingResponse;
import com.digicore.omnexa.common.lib.onboarding.dto.response.UserInviteResponse;
import com.digicore.omnexa.common.lib.profile.contract.ProfileService;
import com.digicore.omnexa.common.lib.properties.MessagePropertyConfig;
import com.digicore.omnexa.common.lib.util.BeanUtilWrapper;
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
public class BackOfficeUserAuthProfileService implements ProfileService {

  private static final Logger logger =
      LoggerFactory.getLogger(BackOfficeUserAuthProfileService.class);

  private final BackOfficeUserAuthProfileRepository backOfficeUserAuthProfileRepository;

  private final MessagePropertyConfig messagePropertyConfig;

  private final EntityManager entityManager;
  private final PasswordEncoder passwordEncoder;

  @Transactional(rollbackOn = Exception.class)
  @Override
  public OnboardingResponse createProfile(OnboardingRequest request) {
    if (request instanceof BackOfficeUserAuthProfileOnboardingRequest req) {
      validateOnboardingRequest(req.getUsername());
      BackOfficeUserAuthProfile user = new BackOfficeUserAuthProfile();
      BeanUtilWrapper.copyNonNullProperties(req, user);
      BackOfficeUserProfile backOfficeUserProfile =
          entityManager.getReference(BackOfficeUserProfile.class, req.getBackOfficeUserProfileId());
      user.setBackOfficeUserProfile(backOfficeUserProfile);
      user.setPassword(passwordEncoder.encode(req.getPassword()));
      backOfficeUserAuthProfileRepository.save(user);
      return new UserInviteResponse();
    }
    throw new OmnexaException(
        messagePropertyConfig.getOnboardMessage(INVALID, SYSTEM_DEFAULT_INVALID_REQUEST_ERROR),
        HttpStatus.BAD_REQUEST);
  }

  private void validateOnboardingRequest(String username) {
    if (backOfficeUserAuthProfileRepository.existsByUsername(username)) {
      String errorMessage =
          messagePropertyConfig
              .getOnboardMessage(DUPLICATE, SYSTEM_DEFAULT_DUPLICATE_ERROR)
              .replace(PROFILE, username);
      logger.error(errorMessage);
      throw new OmnexaException(errorMessage, HttpStatus.BAD_REQUEST);
    }
  }
}
