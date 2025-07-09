/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.service.modules.onboarding.facade;

import com.digicore.common.lib.facade.contract.Facade;
import com.digicore.common.lib.onboarding.contract.OnboardingRequest;
import com.digicore.common.lib.onboarding.contract.OnboardingResponse;
import com.digicore.common.lib.profile.contract.service.ProfileService;
import com.digicore.common.lib.util.BeanUtilWrapper;
import com.digicore.service.modules.profile.dto.response.MerchantOnboardingResponse;
import com.digicore.service.modules.profile.user.dto.request.MerchantUserOnboardingRequest;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Facade implementation for handling merchant onboarding operations.
 *
 * <p>This class coordinates the merchant onboarding process by interacting with profile services
 * for both merchant and merchant user profiles. It uses the facade pattern to encapsulate the
 * onboarding logic and ensure separation of concerns.
 *
 * <p>Features: - Creates a merchant profile using the provided onboarding request. - Creates a
 * merchant user profile linked to the merchant profile. - Utilizes a utility class to copy non-null
 * properties between objects.
 *
 * <p>Usage: - Pass an {@link OnboardingRequest} object to the `process` method to initiate the
 * onboarding process. - Example:
 *
 * <pre>
 *   MerchantOnboardingFacade facade = new MerchantOnboardingFacade(profileService, userProfileService);
 *   facade.process(onboardingRequest);
 *   </pre>
 *
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jun-23(Mon)-2025
 */
@Component
@RequiredArgsConstructor
public class MerchantOnboardingFacade implements Facade<OnboardingRequest, Void> {
  private final ProfileService merchantProfileService;
  private final ProfileService merchantUserProfileService;
  private final ProfileService merchantUserAuthProfileService;

  /**
   * Processes the merchant onboarding request.
   *
   * <p>This method creates a merchant profile and, if successful, creates a corresponding merchant
   * user profile. It uses the {@link BeanUtilWrapper} to copy non-null properties from the
   * onboarding request to the user onboarding request.
   *
   * @param profile the onboarding request containing merchant details.
   * @return an {@link Optional} containing {@code null} upon successful processing.
   */
  @Override
  public Optional<Void> process(OnboardingRequest profile) {
    OnboardingResponse merchantOnboardingResponse = merchantProfileService.createProfile(profile);
    if (merchantOnboardingResponse instanceof MerchantOnboardingResponse onboardingResponse) {
      MerchantUserOnboardingRequest userOnboardingRequest = new MerchantUserOnboardingRequest();
      BeanUtilWrapper.copyNonNullProperties(profile, userOnboardingRequest);
      userOnboardingRequest.setMerchantProfileId(onboardingResponse.getMerchantProfileId());
      OnboardingResponse merchantUserOnboardingResponse =  merchantUserProfileService.createProfile(userOnboardingRequest);
      if (merchantUserOnboardingResponse instanceof MerchantOnboardingResponse userOnboardingResponse) {
        MerchantUserOnboardingRequest userAuthOnboardingRequest = new MerchantUserOnboardingRequest();
        BeanUtilWrapper.copyNonNullProperties(profile, userAuthOnboardingRequest);
        userAuthOnboardingRequest.setUsername(onboardingResponse.getUsername());
        userAuthOnboardingRequest.setMerchantProfileId(
                userOnboardingResponse.getMerchantProfileId());
        merchantUserAuthProfileService.createProfile(userAuthOnboardingRequest);
      }
    }
    return Optional.empty();
  }

  /**
   * Returns the type of the facade.
   *
   * <p>This method identifies the type of facade as "merchantOnboarding".
   *
   * @return a string representing the facade type.
   */
  @Override
  public String getType() {
    return "merchantOnboarding";
  }
}
