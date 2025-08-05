/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.backoffice.modules.user.management.facade;

import com.digicore.omnexa.common.lib.facade.contract.Facade;
import com.digicore.omnexa.common.lib.onboarding.contract.OnboardingRequest;
import com.digicore.omnexa.common.lib.onboarding.contract.OnboardingResponse;
import com.digicore.omnexa.common.lib.onboarding.dto.response.UserInviteResponse;
import com.digicore.omnexa.common.lib.profile.contract.ProfileService;
import com.digicore.omnexa.notification.lib.contract.NotificationRequestType;
import com.digicore.omnexa.notification.lib.contract.email.model.EmailRequest;
import com.digicore.omnexa.notification.lib.service.PluggableEmailService;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
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
  private final PluggableEmailService pluggableEmailService;
  public static final String INVITATION_LINK = "invitationLink";
  public static final String SEND_VERIFICATION_EMAIL_TEMPLATE = "SEND_VERIFICATION_EMAIL";
  public static final String FIRST_NAME = "firstName";
  public static final String ONBOARDING_VERIFICATION = "Onboarding verification";

  @Override
  public Optional<Void> process(OnboardingRequest request) {
    OnboardingResponse onboardingResponse = backOfficeUserProfileService.createProfile(request);
    pluggableEmailService
        .getEngine(pluggableEmailService.getNotificationPropConfig().getEmailChannelType())
        .sendEmailAsync(
            buildBackOfficeOnboardVerificationMail(castToUserInviteResponse(onboardingResponse)));
    return Optional.empty();
  }

  private EmailRequest buildBackOfficeOnboardVerificationMail(
      UserInviteResponse userInviteResponse) {
    Map<String, Object> placeHolders = new HashMap<>();
    placeHolders.put(FIRST_NAME, userInviteResponse.getFirstName());
    placeHolders.put(
        INVITATION_LINK,
        pluggableEmailService
            .getNotificationPropConfig()
            .getServiceBaseUrl()
            .concat("?email=".concat(userInviteResponse.getEmail())));
    return EmailRequest.builder()
        .useTemplate(true)
        .sender(
            pluggableEmailService
                .getNotificationPropConfig()
                .getSender(NotificationRequestType.SEND_INVITE_EMAIL, null))
        .subject(
            pluggableEmailService
                .getNotificationPropConfig()
                .getSubject(NotificationRequestType.SEND_INVITE_EMAIL, ONBOARDING_VERIFICATION))
        .placeHolders(placeHolders)
        .recipients(Set.of(userInviteResponse.getEmail()))
        .templateName(
            pluggableEmailService
                .getNotificationPropConfig()
                .getTemplate(
                    NotificationRequestType.SEND_INVITE_EMAIL, SEND_VERIFICATION_EMAIL_TEMPLATE))
        .build();
  }

  private UserInviteResponse castToUserInviteResponse(OnboardingResponse response) {
    return (UserInviteResponse) response;
  }

  @Override
  public String getType() {
    return "backOfficeUserInvitation";
  }
}
