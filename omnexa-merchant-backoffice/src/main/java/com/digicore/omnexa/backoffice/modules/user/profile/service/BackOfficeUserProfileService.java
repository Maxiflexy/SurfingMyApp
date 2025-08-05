/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.backoffice.modules.user.profile.service;

import static com.digicore.omnexa.common.lib.constant.message.MessageConstant.*;
import static com.digicore.omnexa.common.lib.constant.message.MessagePlaceHolderConstant.PROFILE;
import static com.digicore.omnexa.common.lib.constant.system.SystemConstant.*;

import com.digicore.omnexa.backoffice.modules.user.profile.data.model.BackOfficeUserProfile;
import com.digicore.omnexa.backoffice.modules.user.profile.data.repository.BackOfficeUserProfileRepository;
import com.digicore.omnexa.backoffice.modules.user.profile.dto.request.BackOfficeProfileEditRequest;
import com.digicore.omnexa.backoffice.modules.user.profile.dto.response.BackOfficeProfileEditResponse;
import com.digicore.omnexa.backoffice.modules.user.profile.dto.response.BackOfficeUserProfileDTO;
import com.digicore.omnexa.common.lib.api.ApiError;
import com.digicore.omnexa.common.lib.enums.ProfileStatus;
import com.digicore.omnexa.common.lib.enums.ProfileVerificationStatus;
import com.digicore.omnexa.common.lib.exception.OmnexaException;
import com.digicore.omnexa.common.lib.onboarding.contract.OnboardingRequest;
import com.digicore.omnexa.common.lib.onboarding.contract.OnboardingResponse;
import com.digicore.omnexa.common.lib.onboarding.dto.request.SignupRequest;
import com.digicore.omnexa.common.lib.onboarding.dto.request.UserInviteRequest;
import com.digicore.omnexa.common.lib.onboarding.dto.response.UserInviteResponse;
import com.digicore.omnexa.common.lib.profile.contract.ProfileService;
import com.digicore.omnexa.common.lib.profile.dto.request.ProfileEditRequest;
import com.digicore.omnexa.common.lib.profile.dto.response.ProfileEditResponse;
import com.digicore.omnexa.common.lib.profile.dto.response.ProfileInfoResponse;
import com.digicore.omnexa.common.lib.properties.MessagePropertyConfig;
import com.digicore.omnexa.common.lib.util.BeanUtilWrapper;
import com.digicore.omnexa.common.lib.util.PagenationUtil;
import com.digicore.omnexa.common.lib.util.PaginatedResponse;
import com.digicore.omnexa.common.lib.util.RequestUtil;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

/**
 * Service implementation for back office user operations.
 *
 * <p>Handles user invitation and signup processes including validation, persistence, and email
 * notifications.
 *
 * <p>This service implements the {@link ProfileService} interface and provides methods for
 * creating, updating, and retrieving user profiles.
 *
 * <p>It also includes validation logic for onboarding requests and error handling for various
 * scenarios.
 *
 * <p>Transactional annotations are used to ensure database consistency.
 *
 * @author Onyekachi Ejemba
 * @createdOn Jul-08(Tue)-2025
 */
@Service
@RequiredArgsConstructor
public class BackOfficeUserProfileService implements ProfileService {
  private final BackOfficeUserProfileRepository backOfficeUserProfileRepository;
  private final MessagePropertyConfig messagePropertyConfig;

  private static final Logger log = LoggerFactory.getLogger(BackOfficeUserProfileService.class);

  /**
   * Creates a new user profile based on the provided onboarding request.
   *
   * @param profile the onboarding request containing user details
   * @return an {@link OnboardingResponse} indicating the result of the operation
   * @throws OmnexaException if the email already exists or the request is invalid
   */
  @Override
  @Transactional(rollbackOn = Exception.class)
  public OnboardingResponse createProfile(OnboardingRequest profile) {
    if (profile instanceof UserInviteRequest request) {
      if (backOfficeUserProfileRepository.existsByEmail(request.getEmail())) {
        throw new OmnexaException(
            buildDuplicateErrorMessage(request.getEmail()), HttpStatus.BAD_REQUEST);
      }
      BackOfficeUserProfile user = new BackOfficeUserProfile();
      user.setEmail(request.getEmail());
      user.setFirstName(request.getFirstName());
      user.setLastName("");
      user.setRole(request.getRole());
      backOfficeUserProfileRepository.save(user);
      UserInviteResponse userInviteResponse = new UserInviteResponse();
      userInviteResponse.setEmail(request.getEmail());
      userInviteResponse.setFirstName(request.getFirstName());
      return userInviteResponse;
    }
    throw new OmnexaException(
        messagePropertyConfig.getOnboardMessage(INVALID, SYSTEM_DEFAULT_INVALID_REQUEST_ERROR),
        HttpStatus.BAD_REQUEST);
  }

  /**
   * Updates an existing user profile.
   *
   * @param request the profile edit request containing updated user details
   * @return a {@link ProfileEditResponse} indicating the result of the operation
   * @throws OmnexaException if the profile ID does not exist or the request is invalid
   */
  @Override
  @Transactional(rollbackOn = Exception.class)
  public ProfileEditResponse updateProfile(ProfileEditRequest request) {
    if (request instanceof BackOfficeProfileEditRequest editRequest) {
      String profileId = editRequest.getProfileId();

      if (backOfficeUserProfileRepository.existsByProfileId(profileId)) {
        backOfficeUserProfileRepository.updateProfile(
            editRequest.getEmail(),
            editRequest.getFirstName(),
            editRequest.getLastName(),
            editRequest.getRole(),
            profileId);
        return new BackOfficeProfileEditResponse();
      }
      throw new OmnexaException(buildNotFoundErrorMessage(profileId), HttpStatus.NOT_FOUND);
    }

    throw new OmnexaException(
        messagePropertyConfig.getOnboardMessage(INVALID, SYSTEM_DEFAULT_INVALID_REQUEST_ERROR),
        HttpStatus.BAD_REQUEST);
  }

  /**
   * Updates the status and verification status of a user profile.
   *
   * @param profileId the ID of the profile to update
   * @param profileStatus the new profile status
   * @param profileVerificationStatus the new profile verification status
   * @throws OmnexaException if the profile ID does not exist
   */
  @Override
  public void updateProfileStatus(
      String profileId,
      ProfileStatus profileStatus,
      ProfileVerificationStatus profileVerificationStatus) {
    if (backOfficeUserProfileRepository.existsByProfileId(profileId)) {
      backOfficeUserProfileRepository.updateProfileStatuses(
          profileStatus, profileVerificationStatus, profileId);
    } else {
      throw new OmnexaException(buildNotFoundErrorMessage(profileId), HttpStatus.NOT_FOUND);
    }
  }

  /**
   * Validates an onboarding request and retrieves the corresponding profile information.
   *
   * @param request the onboarding request to validate
   * @return a {@link ProfileEditResponse} containing the profile information
   * @throws OmnexaException if the request is invalid or the profile is not found
   */
  @Override
  public ProfileEditResponse validateOnboardingRequest(OnboardingRequest request) {
    if (request instanceof SignupRequest signupRequest) {
      validateOnboardingRequest(signupRequest);
      BackOfficeUserProfileDTO user =
          backOfficeUserProfileRepository
              .findProfileStatusesByEmail(signupRequest.getEmail())
              .orElseThrow(
                  () ->
                      new OmnexaException(
                          buildNotFoundErrorMessage(signupRequest.getEmail()),
                          HttpStatus.NOT_FOUND));
      if (!ProfileVerificationStatus.PENDING_INVITE_ACCEPTANCE.equals(
          user.getProfileVerificationStatus()))
        throw new OmnexaException(
            buildDuplicateErrorMessage(signupRequest.getEmail()), HttpStatus.BAD_REQUEST);

      BackOfficeProfileEditResponse response = new BackOfficeProfileEditResponse();
      BeanUtilWrapper.copyNonNullProperties(user, response);
      response.setBackOfficeUserProfileId(user.getId());
      return response;
    }
    throw new OmnexaException(
        messagePropertyConfig.getOnboardMessage(INVALID, SYSTEM_DEFAULT_INVALID_REQUEST_ERROR),
        HttpStatus.BAD_REQUEST);
  }

  /**
   * Retrieves a user profile by email.
   *
   * @param email the email of the profile to retrieve
   * @return a {@link ProfileInfoResponse} containing the profile information
   * @throws OmnexaException if the profile is not found or the verification status is invalid
   */
  @Override
  public ProfileInfoResponse getProfileByEmail(String email) {
    BackOfficeUserProfileDTO user =
        backOfficeUserProfileRepository
            .findProfileStatusesByEmail(email)
            .orElseThrow(
                () -> new OmnexaException(buildNotFoundErrorMessage(email), HttpStatus.NOT_FOUND));
    if (!ProfileVerificationStatus.PENDING_INVITE_ACCEPTANCE.equals(
        user.getProfileVerificationStatus())) {
      String message = buildDuplicateErrorMessage(email);
      throw new OmnexaException(message, HttpStatus.BAD_REQUEST);
    }

    return user;
  }

  /**
   * Retrieves a paginated list of all back office users without any filters.
   *
   * @param pageNumber page number (1-based, optional, default: 1)
   * @param pageSize page size (max 16, optional, default: 16)
   * @return paginated user list response
   */
  @Override
  public PaginatedResponse<ProfileInfoResponse> getAllProfiles(
      Integer pageNumber, Integer pageSize) {

    PagenationUtil.ValidationResult validationResult =
        PagenationUtil.getValidatedPaginationParameters(pageNumber, pageSize);
    Pageable pageable =
        PagenationUtil.createPageable(
            validationResult.validatedPageNumber(), validationResult.validatedPageSize());

    Page<BackOfficeUserProfileDTO> userPage =
        backOfficeUserProfileRepository.findAllUsersPaginated(pageable);

    return buildPaginatedResponse(userPage, validationResult.validatedPageNumber());
  }

  /**
   * Retrieves a paginated list of back office users filtered by search term.
   *
   * @param search search term for firstName, lastName, or email (required)
   * @param pageNumber page number (1-based, optional, default: 1)
   * @param pageSize page size (max 16, optional, default: 16)
   * @return paginated user list response filtered by search term
   */
  @Override
  public PaginatedResponse<ProfileInfoResponse> searchProfilesPaginated(
      String search, Integer pageNumber, Integer pageSize) {
    log.debug("Searching profiles with term='{}', page={}, size={}", search, pageNumber, pageSize);

    String trimmedSearch;
    try {
      trimmedSearch = PagenationUtil.validateSearchTerm(search, "Search term");
    } catch (Exception e) {
      throw new OmnexaException(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    PagenationUtil.ValidationResult validationResult =
        PagenationUtil.getValidatedPaginationParameters(pageNumber, pageSize);
    Pageable pageable =
        PagenationUtil.createPageable(
            validationResult.validatedPageNumber(), validationResult.validatedPageSize());

    Page<BackOfficeUserProfileDTO> userPage =
        backOfficeUserProfileRepository.findUsersBySearchPaginated(trimmedSearch, pageable);

    return buildPaginatedResponse(userPage, validationResult.validatedPageNumber());
  }

  /**
   * Retrieves a paginated list of back office users filtered by profile status.
   *
   * @param profileStatus filter by profile status (required)
   * @param pageNumber page number (1-based, optional, default: 1)
   * @param pageSize page size (max 16, optional, default: 16)
   * @return paginated user list response filtered by profile status
   */
  @Override
  public PaginatedResponse<ProfileInfoResponse> getProfilesByStatusPaginated(
      ProfileStatus profileStatus, Integer pageNumber, Integer pageSize) {
    log.debug(
        "Retrieving profiles with status='{}', page={}, size={}",
        profileStatus,
        pageNumber,
        pageSize);

    if (RequestUtil.isNull(profileStatus)) {
      throw new OmnexaException("Profile status is required", HttpStatus.BAD_REQUEST);
    }

    PagenationUtil.ValidationResult validationResult =
        PagenationUtil.getValidatedPaginationParameters(pageNumber, pageSize);
    Pageable pageable =
        PagenationUtil.createPageable(
            validationResult.validatedPageNumber(), validationResult.validatedPageSize());

    Page<BackOfficeUserProfileDTO> userPage =
        backOfficeUserProfileRepository.findUsersByStatusPaginated(profileStatus, pageable);

    return buildPaginatedResponse(userPage, validationResult.validatedPageNumber());
  }

  /**
   * Validates the fields of a signup request.
   *
   * @param req the signup request to validate
   * @throws OmnexaException if validation errors are found
   */
  private void validateOnboardingRequest(SignupRequest req) {
    List<ApiError> onboardingErrors = getApiErrors(req);

    // Throw if there are errors
    if (!onboardingErrors.isEmpty()) {
      onboardingErrors.forEach(
          error ->
              log.error("Back office user onboarding validation failed: {}", error.getMessage()));
      throw new OmnexaException(
          messagePropertyConfig.getOnboardMessage(INVALID, SYSTEM_DEFAULT_INVALID_REQUEST_ERROR),
          HttpStatus.BAD_REQUEST,
          onboardingErrors);
    }
  }

  /**
   * Retrieves a list of validation errors for a signup request.
   *
   * @param req the signup request to validate
   * @return a list of {@link ApiError} objects representing validation errors
   */
  private List<ApiError> getApiErrors(SignupRequest req) {
    List<ApiError> onboardingErrors = new ArrayList<>();

    // Required field checks using field label + supplier
    Map<String, Supplier<String>> requiredFields =
        Map.of(
            "First Name", req::getFirstName,
            "Last Name", req::getLastName,
            "Email", req::getEmail,
            "Password", req::getLastName);

    requiredFields.forEach(
        (label, supplier) -> {
          if (RequestUtil.nullOrEmpty(supplier.get())) {
            String message =
                messagePropertyConfig
                    .getOnboardMessage(REQUIRED, SYSTEM_DEFAULT_REQUIRED_FIELD_MISSING_ERROR)
                    .replace(PROFILE, label);
            onboardingErrors.add(new ApiError(message));
          }
        });
    return onboardingErrors;
  }

  /**
   * Builds a "not found" error message for a given profile ID.
   *
   * @param profileId the profile ID that was not found
   * @return the error message
   */
  private String buildNotFoundErrorMessage(String profileId) {
    String message =
        messagePropertyConfig
            .getOnboardMessage(NOT_FOUND, SYSTEM_DEFAULT_NOT_FOUND_ERROR)
            .replace(PROFILE, profileId);
    log.error(message);
    return message;
  }

  /**
   * Builds a "duplicate" error message for a given email.
   *
   * @param email the email that is a duplicate
   * @return the error message
   */
  private String buildDuplicateErrorMessage(String email) {
    String message =
        messagePropertyConfig
            .getOnboardMessage(DUPLICATE, SYSTEM_DEFAULT_DUPLICATE_ERROR)
            .replace(PROFILE, email);
    log.error(message);
    return message;
  }

  /**
   * Builds a paginated response from a Page object.
   *
   * <p>This method remains in the service as it's specific to the BackOfficeUserProfileDTO and
   * PaginatedResponse types.
   *
   * @param userPage the Page object from repository
   * @param validatedPageNumber the validated page number (1-based)
   * @return PaginatedResponse
   */
  private PaginatedResponse<ProfileInfoResponse> buildPaginatedResponse(
      Page<BackOfficeUserProfileDTO> userPage, int validatedPageNumber) {
    return PaginatedResponse.<ProfileInfoResponse>builder()
        .content(
            userPage.getContent().stream()
                .map(backOfficeUserProfileDTO -> (ProfileInfoResponse) backOfficeUserProfileDTO)
                .toList())
        .currentPage(validatedPageNumber) // Return 1-based page number
        .totalItems(userPage.getTotalElements())
        .totalPages(userPage.getTotalPages())
        .isFirstPage(userPage.isFirst())
        .isLastPage(userPage.isLast())
        .build();
  }
}
