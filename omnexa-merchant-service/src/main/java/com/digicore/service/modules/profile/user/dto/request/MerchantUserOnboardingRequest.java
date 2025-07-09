/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.service.modules.profile.user.dto.request;

import com.digicore.common.lib.onboarding.contract.OnboardingRequest;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a request for onboarding a user associated with a merchant. This class implements the
 * {@link OnboardingRequest} interface and provides the necessary fields for user onboarding.
 *
 * <p>Annotations:
 *
 * <ul>
 *   <li>{@link Getter}, {@link Setter}: Lombok annotations for generating getter and setter
 *       methods.
 * </ul>
 *
 * <p>Fields:
 *
 * <ul>
 *   <li>merchantProfileId: The ID of the merchant profile associated with the user.
 *   <li>firstName: The first name of the user.
 *   <li>lastName: The last name of the user.
 *   <li>password: The password for the user account.
 * </ul>
 *
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jun-25(Wed)-2025
 */
@Getter
@Setter
public class MerchantUserOnboardingRequest implements OnboardingRequest {

  /** The ID of the merchant profile associated with the user. */
  private Long merchantProfileId;

  /** The first name of the user. */
  private String firstName;

  /** The last name of the user. */
  private String lastName;

  /** The password for the user account. */
  private String password;

  private String username;
}
