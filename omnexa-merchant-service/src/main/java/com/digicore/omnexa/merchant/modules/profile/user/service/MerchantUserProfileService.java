/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.merchant.modules.profile.user.service;

import static com.digicore.omnexa.common.lib.constant.message.MessageConstant.*;
import static com.digicore.omnexa.common.lib.constant.message.MessageConstant.REQUIRED;
import static com.digicore.omnexa.common.lib.constant.message.MessagePlaceHolderConstant.PROFILE;
import static com.digicore.omnexa.common.lib.constant.system.SystemConstant.*;

import com.digicore.omnexa.common.lib.api.ApiError;
import com.digicore.omnexa.common.lib.enums.ProfileStatus;
import com.digicore.omnexa.common.lib.enums.ProfileVerificationStatus;
import com.digicore.omnexa.common.lib.exception.OmnexaException;
import com.digicore.omnexa.common.lib.onboarding.contract.OnboardingRequest;
import com.digicore.omnexa.common.lib.onboarding.contract.OnboardingResponse;
import com.digicore.omnexa.common.lib.profile.contract.ProfileService;
import com.digicore.omnexa.common.lib.util.BeanUtilWrapper;
import com.digicore.omnexa.common.lib.util.RequestUtil;
import com.digicore.omnexa.merchant.modules.profile.dto.response.MerchantOnboardingResponse;
import com.digicore.omnexa.merchant.modules.profile.helper.ProfileHelper;
import com.digicore.omnexa.merchant.modules.profile.user.data.model.MerchantUserProfile;
import com.digicore.omnexa.merchant.modules.profile.user.dto.request.MerchantUserOnboardingRequest;
import jakarta.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service implementation for merchant user profile management operations.
 *
 * <p>This service handles the creation and validation of merchant user profiles, ensuring proper
 * linkage to merchant profiles and data integrity.
 *
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jun-24(Tue)-2025
 */
@Service
@RequiredArgsConstructor
public class MerchantUserProfileService implements ProfileService {
  private static final Logger log = LoggerFactory.getLogger(MerchantUserProfileService.class);

  private final ProfileHelper profileHelper;

  // Required field validation mapping - immutable and reusable
  private static final Map<String, Function<MerchantUserOnboardingRequest, String>>
      REQUIRED_FIELDS =
          Map.of(
              "First Name", MerchantUserOnboardingRequest::getFirstName,
              "Last Name", MerchantUserOnboardingRequest::getLastName);

  @Transactional(rollbackFor = Exception.class)
  @Override
  public OnboardingResponse createProfile(OnboardingRequest profile) {
    MerchantUserOnboardingRequest request = castToMerchantUserRequest(profile);

    // Validate first - let validation errors bubble up with correct status codes
    validateOnboardingRequest(request);

    // Only wrap actual persistence operations in try-catch
    try {
      MerchantUserProfile userProfile = createMerchantUserProfile(request);
      profileHelper.getMerchantUserProfileRepository().save(userProfile);

      log.info(
          "Successfully created merchant user profile with ID: {} for merchant: {}",
          userProfile.getId(),
          request.getMerchantProfileId());

      return buildOnboardingResponse(userProfile);

    } catch (EntityNotFoundException e) {
      log.error(
          "Merchant profile not found during user profile creation. MerchantProfileId: {}",
          request.getMerchantProfileId(),
          e);
      throw new OmnexaException("Referenced merchant profile not found", HttpStatus.BAD_REQUEST);
    } catch (DataIntegrityViolationException e) {
      log.error(
          "Database constraint violation during merchant user profile creation for merchant: {}",
          request.getMerchantProfileId(),
          e);
      throw new OmnexaException(
          "Merchant user profile creation failed due to data constraint violation",
          HttpStatus.CONFLICT);
    } catch (Exception e) {
      log.error(
          "Unexpected error during merchant user profile creation for merchant: {}",
          request.getMerchantProfileId(),
          e);
      throw new OmnexaException(
          "Merchant user profile creation failed", HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @Override
  public void updateProfileStatus(
      String profileId,
      ProfileStatus profileStatus,
      ProfileVerificationStatus profileVerificationStatus) {
    profileHelper
        .getMerchantUserProfileRepository()
        .conditionalUpdateProfileStatuses(
            profileStatus != null ? profileStatus.toString() : null,
            profileVerificationStatus != null ? profileVerificationStatus.toString() : null,
            profileId);
  }

  /**
   * Safely casts OnboardingRequest to MerchantUserOnboardingRequest.
   *
   * @param profile the profile to cast
   * @return MerchantUserOnboardingRequest
   * @throws OmnexaException if casting fails
   */
  private MerchantUserOnboardingRequest castToMerchantUserRequest(OnboardingRequest profile) {
    if (!(profile instanceof MerchantUserOnboardingRequest)) {
      throw new OmnexaException(
          profileHelper
              .getMessagePropertyConfig()
              .getOnboardMessage(INVALID, SYSTEM_DEFAULT_INVALID_REQUEST_ERROR),
          HttpStatus.BAD_REQUEST);
    }
    return (MerchantUserOnboardingRequest) profile;
  }

  /**
   * Creates and configures merchant user profile with merchant reference.
   *
   * @param request the validated onboarding request
   * @return configured MerchantUserProfile
   */
  private MerchantUserProfile createMerchantUserProfile(MerchantUserOnboardingRequest request) {
    MerchantUserProfile userProfile = new MerchantUserProfile();
    BeanUtilWrapper.copyNonNullProperties(request, userProfile);

    userProfile.setMerchantProfile(
        profileHelper.getMerchantProfileByReference(request.getMerchantProfileId()));

    userProfile.setRole(SYSTEM_MERCHANT_ROLE_NAME);

    return userProfile;
  }

  /**
   * Validates merchant user onboarding request.
   *
   * @param request the request to validate
   * @throws OmnexaException if validation fails
   */
  private void validateOnboardingRequest(MerchantUserOnboardingRequest request) {
    List<ApiError> errors = validateRequiredFields(request);

    throwIfValidationFailed(errors);
  }

  /**
   * Validates all required fields using the predefined mapping.
   *
   * @param request the request to validate
   * @return list of validation errors
   */
  private List<ApiError> validateRequiredFields(MerchantUserOnboardingRequest request) {
    List<ApiError> errors = new ArrayList<>();

    REQUIRED_FIELDS.forEach(
        (fieldName, fieldExtractor) -> {
          String fieldValue = fieldExtractor.apply(request);
          if (RequestUtil.nullOrEmpty(fieldValue)) {
            String message =
                profileHelper
                    .getMessagePropertyConfig()
                    .getOnboardMessage(REQUIRED, SYSTEM_DEFAULT_REQUIRED_FIELD_MISSING_ERROR)
                    .replace(PROFILE, fieldName);
            errors.add(new ApiError(message));
          }
        });

    return errors;
  }

  /**
   * Throws validation exception if errors exist.
   *
   * @param errors the list of validation errors
   * @throws OmnexaException if errors are present
   */
  private void throwIfValidationFailed(List<ApiError> errors) {
    if (!errors.isEmpty()) {
      errors.forEach(
          error -> log.warn("Merchant user onboarding validation failed: {}", error.getMessage()));

      throw new OmnexaException(
          profileHelper
              .getMessagePropertyConfig()
              .getOnboardMessage(INVALID, SYSTEM_DEFAULT_INVALID_REQUEST_ERROR),
          HttpStatus.BAD_REQUEST,
          errors);
    }
  }

  /**
   * Builds standardized onboarding response.
   *
   * @param userProfile the created merchant user profile
   * @return MerchantOnboardingResponse
   */
  private MerchantOnboardingResponse buildOnboardingResponse(MerchantUserProfile userProfile) {
    MerchantOnboardingResponse response = new MerchantOnboardingResponse();
    response.setMerchantProfileId(userProfile.getId());
    return response;
  }
}
