/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.merchant.service.modules.profile.data.enums;

/**
 * Enum representing the classification type of a merchant profile.
 *
 * <p>This helps differentiate between core merchant entities such as aggregators, direct merchants,
 * and sub-merchants. Useful for routing, access control, onboarding logic, and reporting.
 *
 * <ul>
 *   <li><b>AGGREGATOR</b> – A top-level merchant that manages or onboards sub-merchants.
 *   <li><b>DIRECT_MERCHANT</b> – A merchant that operates independently and is not under any
 *       aggregator.
 *   <li><b>SUB_MERCHANT</b> – A merchant that is registered under or operates through an
 *       aggregator.
 * </ul>
 *
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jun-25(Wed)-2025
 */
public enum MerchantProfileType {

  /** A top-level merchant that manages or sponsors sub-merchants. */
  AGGREGATOR,

  /** A merchant that operates independently without an aggregator. */
  DIRECT_MERCHANT,

  /** A merchant operating under an aggregator. */
  SUB_MERCHANT
}
