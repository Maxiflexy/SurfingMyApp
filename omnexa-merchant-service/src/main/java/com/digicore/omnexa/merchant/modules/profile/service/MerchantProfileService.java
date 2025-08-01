/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.merchant.modules.profile.service;

import static com.digicore.omnexa.common.lib.constant.message.MessageConstant.*;
import static com.digicore.omnexa.common.lib.constant.message.MessagePlaceHolderConstant.PROFILE;
import static com.digicore.omnexa.common.lib.constant.system.SystemConstant.*;
import static com.digicore.omnexa.merchant.modules.profile.helper.ProfileHelper.*;

import com.digicore.omnexa.common.lib.api.ApiError;
import com.digicore.omnexa.common.lib.enums.ProfileStatus;
import com.digicore.omnexa.common.lib.enums.ProfileVerificationStatus;
import com.digicore.omnexa.common.lib.exception.OmnexaException;
import com.digicore.omnexa.common.lib.onboarding.contract.OnboardingRequest;
import com.digicore.omnexa.common.lib.onboarding.contract.OnboardingResponse;
import com.digicore.omnexa.common.lib.profile.contract.ProfileService;
import com.digicore.omnexa.common.lib.util.BeanUtilWrapper;
import com.digicore.omnexa.common.lib.util.RequestUtil;
import com.digicore.omnexa.merchant.modules.profile.data.model.MerchantProfile;
import com.digicore.omnexa.merchant.modules.profile.data.model.kyc.MerchantKycProfile;
import com.digicore.omnexa.merchant.modules.profile.dto.request.MerchantOnboardingRequest;
import com.digicore.omnexa.merchant.modules.profile.dto.response.MerchantOnboardingResponse;
import com.digicore.omnexa.merchant.modules.profile.helper.ProfileHelper;
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
public class MerchantProfileService implements ProfileService {
  private static final Logger log = LoggerFactory.getLogger(MerchantProfileService.class);

  private final ProfileHelper profileHelper;

  // Required field validation mapping - immutable and reusable
  private static final Map<String, Function<MerchantOnboardingRequest, String>> REQUIRED_FIELDS =
      Map.of(
          "Business Name", MerchantOnboardingRequest::getBusinessName,
          "Business Email", MerchantOnboardingRequest::getBusinessEmail,
          "First Name", MerchantOnboardingRequest::getFirstName,
          "Last Name", MerchantOnboardingRequest::getLastName,
          "Phone Number", MerchantOnboardingRequest::getBusinessPhoneNumber,
          "Password", MerchantOnboardingRequest::getPassword);

  @Transactional(rollbackFor = Exception.class)
  @Override
  public OnboardingResponse createProfile(OnboardingRequest profile) {
    MerchantOnboardingRequest request = castToMerchantRequest(profile);

    // Validate first - let validation errors bubble up with correct status codes
    validateOnboardingRequest(request);

    // Only wrap actual persistence operations in try-catch
    try {
      MerchantProfile merchantProfile = createMerchantProfile(request);
      profileHelper.getMerchantProfileRepository().save(merchantProfile);

      log.info(
          "Successfully created merchant profile with ID: {} for business: {}",
          merchantProfile.getMerchantId(),
          request.getBusinessName());

      return buildOnboardingResponse(merchantProfile);

    } catch (DataIntegrityViolationException e) {
      log.error(
          "Database constraint violation during merchant profile creation for business: {}",
          request.getBusinessName(),
          e);
      throw new OmnexaException(
          "Merchant profile creation failed due to data constraint violation", HttpStatus.CONFLICT);
    } catch (Exception e) {
      log.error(
          "Unexpected error during merchant profile creation for business: {}",
          request.getBusinessName(),
          e);
      throw new OmnexaException(
          "Merchant profile creation failed", HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

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

  /**
   * Safely casts OnboardingRequest to MerchantOnboardingRequest.
   *
   * @param profile the profile to cast
   * @return MerchantOnboardingRequest
   * @throws OmnexaException if casting fails
   */
  private MerchantOnboardingRequest castToMerchantRequest(OnboardingRequest profile) {
    if (!(profile instanceof MerchantOnboardingRequest)) {
      throw new OmnexaException(
          profileHelper
              .getMessagePropertyConfig()
              .getOnboardMessage(INVALID, SYSTEM_DEFAULT_INVALID_REQUEST_ERROR),
          HttpStatus.BAD_REQUEST);
    }
    return (MerchantOnboardingRequest) profile;
  }

  /**
   * Creates and configures merchant profile with KYC profile.
   *
   * @param request the validated onboarding request
   * @return configured MerchantProfile
   */
  private MerchantProfile createMerchantProfile(MerchantOnboardingRequest request) {
    MerchantProfile merchantProfile = new MerchantProfile();
    BeanUtilWrapper.copyNonNullProperties(request, merchantProfile);

    // Create and link KYC profile
    MerchantKycProfile kycProfile = new MerchantKycProfile();
    kycProfile.setMerchantProfile(merchantProfile);
    merchantProfile.setMerchantKycProfile(kycProfile);
    merchantProfile.setMerchantId(generateMerchantId());

    return merchantProfile;
  }

  /**
   * Validates merchant onboarding request.
   *
   * @param request the request to validate
   * @throws OmnexaException if validation fails
   */
  private void validateOnboardingRequest(MerchantOnboardingRequest request) {
    List<ApiError> errors = new ArrayList<>();

    // Validate required fields
    validateRequiredFields(request, errors);

    // Validate terms acceptance
    validateTermsAcceptance(request, errors);

    // Check for duplicates
    validateNoDuplicates(request, errors);

    // Throw consolidated errors
    throwIfValidationFailed(errors);
  }

  /** Validates all required fields using the predefined mapping. */
  private void validateRequiredFields(MerchantOnboardingRequest request, List<ApiError> errors) {
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
  }

  /** Validates terms and conditions acceptance. */
  private void validateTermsAcceptance(MerchantOnboardingRequest request, List<ApiError> errors) {
    if (!request.isTermsAccepted()) {
      String message =
          profileHelper
              .getMessagePropertyConfig()
              .getOnboardMessage(REQUIRED, SYSTEM_DEFAULT_REQUIRED_FIELD_MISSING_ERROR)
              .replace(PROFILE, "Terms and Conditions");
      errors.add(new ApiError(message));
    }
  }

  /** Validates no duplicate business name or email exists. */
  private void validateNoDuplicates(MerchantOnboardingRequest request, List<ApiError> errors) {
    if (profileHelper
        .getMerchantProfileRepository()
        .existsByBusinessNameOrBusinessEmail(
            request.getBusinessName(), request.getBusinessEmail())) {

      String message =
          profileHelper
              .getMessagePropertyConfig()
              .getOnboardMessage(DUPLICATE, SYSTEM_DEFAULT_DUPLICATE_ERROR)
              .replace(PROFILE, request.getBusinessName() + " or " + request.getBusinessEmail());
      errors.add(new ApiError(message));
    }
  }

  /** Throws validation exception if errors exist. */
  private void throwIfValidationFailed(List<ApiError> errors) {
    if (!errors.isEmpty()) {
      errors.forEach(
          error -> log.warn("Merchant onboarding validation failed: {}", error.getMessage()));

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
   * @param merchantProfile the created merchant profile
   * @return MerchantOnboardingResponse
   */
  private MerchantOnboardingResponse buildOnboardingResponse(MerchantProfile merchantProfile) {
    MerchantOnboardingResponse response = new MerchantOnboardingResponse();
    response.setMerchantProfileId(merchantProfile.getId());
    response.setUsername(merchantProfile.getBusinessEmail());
    return response;
  }

  /**
   * Generates cryptographically secure merchant ID.
   *
   * @return formatted merchant ID (e.g., "S-123456")
   */
  private String generateMerchantId() {
    int randomNumber = profileHelper.getSecureRandom().nextInt(RANDOM_ID_BOUND) + RANDOM_ID_OFFSET;
    return MERCHANT_ID_PREFIX + randomNumber;
  }
}
