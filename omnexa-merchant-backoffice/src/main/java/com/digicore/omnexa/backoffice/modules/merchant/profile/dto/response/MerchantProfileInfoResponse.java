/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.backoffice.modules.merchant.profile.dto.response;

import com.digicore.omnexa.common.lib.enums.ProfileStatus;
import com.digicore.omnexa.common.lib.profile.dto.response.ProfileInfoResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Response DTO for merchant user profile information.
 *
 * <p>This class implements {@link ProfileInfoResponse} to provide merchant user profile details for
 * external service consumption.
 *
 * @author Onyekachi Ejemba
 * @createdOn Aug-05(Tue)-2025
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MerchantProfileInfoResponse implements ProfileInfoResponse {

  /** The profile ID of the merchant user. */
  private String merchantId;

  private String businessName;

  /** The email address of the merchant user. */
  private String businessEmail;

  /** The phone number of the merchant user. */
  private String businessPhoneNumber;

  /** The current status of the merchant user profile. */
  private ProfileStatus profileStatus;
}
