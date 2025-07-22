/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.backoffice.modules.user.onboarding.facade;

import com.digicore.omnexa.common.lib.facade.contract.Facade;
import com.digicore.omnexa.common.lib.notification.EmailService;
import com.digicore.omnexa.common.lib.onboarding.contract.OnboardingRequest;
import com.digicore.omnexa.common.lib.profile.contract.ProfileService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Facade for back office user operations.
 *
 * <p>Provides a simplified interface for user-related operations following the facade pattern.
 *
 * @author Onyekachi Ejemba
 * @createdOn Jul-08(Tue)-2025
 */
@Component
@RequiredArgsConstructor
public class BackOfficeUserInvitationFacade implements Facade<OnboardingRequest, Void> {
  private final ProfileService backOfficeUserProfileService;
  private final EmailService emailService;

  @Override
  public Optional<Void> process(OnboardingRequest request) {
    backOfficeUserProfileService.createProfile(request);
    emailService.sendInvitationEmail("", "");
    return Optional.empty();
  }

  @Override
  public String getType() {
    return "backOfficeUserInvitation";
  }
}
