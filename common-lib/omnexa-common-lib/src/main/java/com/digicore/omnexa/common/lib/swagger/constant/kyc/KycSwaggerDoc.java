/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.common.lib.swagger.constant.kyc;

/**
 * @author Hossana Chukwunyere
 * @createdOn Aug-04(Mon)-2025
 */
public class KycSwaggerDoc {

  private KycSwaggerDoc() {}

  public static final String KYC_API = "kyc/";

  public static final String KYC_CONTROLLER_TITLE = "KYC-Module";

  /** Description for the Onboarding controller in Swagger documentation. */
  public static final String KYC_CONTROLLER_DESCRIPTION =
      "This module contains all required APIs to complete KYC details submission.";

  public static final String KYC_CONTROLLER_UPDATE_TITLE = "Update Merchant KYC Details";

  public static final String KYC_CONTROLLER_UPDATE_DESCRIPTION =
      """
This endpoint allows incremental saving and updating of a merchant’s KYC profile.
Once the merchant completes and submits the KYC details,
the kycStatus should be set to SUBMITTED to trigger the review process in the Back Office.
""";

  public static final String KYC_CONTROLLER_GET_TITLE = "Get Merchant KYC Details";

  public static final String KYC_CONTROLLER_GET_DESCRIPTION =
      "Retrieve a merchant’s KYC profile details.";
}
