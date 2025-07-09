/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.service.modules.profile.dto.request;

import com.digicore.common.lib.onboarding.contract.OnboardingRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Data transfer object representing a standard merchant onboarding request.
 *
 * <p>This class encapsulates the data required for onboarding a merchant, including business
 * details, contact information, and credentials. It implements the {@link OnboardingRequest}
 * interface to support polymorphic handling of onboarding requests.
 *
 * <p>Features: - Field-level validation for required fields and proper formatting. - Integration
 * with Swagger for API documentation. - Includes fields such as business name, email, first and
 * last names, phone number, password, and terms acceptance.
 *
 * <p>Usage: - Populate this object with merchant details and pass it to the onboarding API. -
 * Example:
 *
 * <pre>
 *   {
 *       "businessName": "Digicore Ltd",
 *       "businessEmail": "support@digicoreltd.com",
 *       "firstName": "Oluwatobi",
 *       "lastName": "Ogunwuyi",
 *       "phoneNumber": "07087982874",
 *       "password": "C00k1ng@0723",
 *       "termsAccepted": true
 *   }
 *   </pre>
 *
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jun-24(Tue)-2025
 */
@Getter
@Setter
@Schema(description = "Merchant onboarding request")
public class MerchantOnboardingRequest implements OnboardingRequest {

  /** The business name of the merchant. */
  @NotBlank(message = "{merchant.businessName.required}")
  @Schema(description = "Business name", example = "Digicore Ltd")
  private String businessName;

  /** The business email of the merchant. Must be a valid email address. */
  @NotBlank(message = "{merchant.businessEmail.required}")
  @Email(message = "{merchant.businessEmail.invalid}")
  @Schema(description = "Business email", example = "support@digicoreltd.com")
  private String businessEmail;

  /** The first name of the merchant. */
  @NotBlank(message = "{merchant.firstName.required}")
  @Schema(description = "First name", example = "Oluwatobi")
  private String firstName;

  /** The last name of the merchant. */
  @NotBlank(message = "{merchant.lastName.required}")
  @Schema(description = "Last name", example = "Ogunwuyi")
  private String lastName;

  /** The phone number of the merchant. Must meet length constraints. */
  @NotBlank(message = "{merchant.phone.required}")
  @Size(min = 10, max = 15, message = "{merchant.phone.length}")
  @Schema(description = "Phone number", example = "07087982874")
  private String phoneNumber;

  /** The password for the merchant's account. Must meet minimum length requirements. */
  @NotBlank(message = "{merchant.password.required}")
  @Size(min = 8, message = "{merchant.password.minLength}")
  @Schema(description = "Password", example = "C00k1ng@0723")
  private String password;

  /** Indicates whether the merchant has accepted the terms and conditions. */
  @AssertTrue(message = "{merchant.terms.notAccepted}")
  @Schema(description = "Terms acceptance", example = "true")
  private boolean termsAccepted;
}
