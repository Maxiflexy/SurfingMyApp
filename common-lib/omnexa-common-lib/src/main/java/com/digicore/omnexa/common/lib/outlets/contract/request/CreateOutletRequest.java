/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.common.lib.outlets.contract.request;

/**
 * @author Hossana Chukwunyere
 * @createdOn Jul-11(Fri)-2025
 */
public interface CreateOutletRequest {
  String getOutletTitle();

  String getCountry();

  String getState();

  String getLga();

  String getAddress();
}
