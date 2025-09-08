/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.common.lib.terminal.dto.response;

import com.digicore.omnexa.common.lib.enums.ProfileStatus;
import com.digicore.omnexa.common.lib.profile.dto.response.ProfileInfoResponse;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO for representing a merchant outlet profile.
 *
 * <p>This class encapsulates the details of a merchant outlet, including its title, country, state,
 * local government area (LGA), and address.
 *
 * @author Hossana Chukwunyere
 * @createdOn Jul-31(Thu)-2025
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class MerchantOutletProfileDTO implements ProfileInfoResponse {
  public static final String MERCHANT_OUTLET_PROFILE_DTO_CLASS_NAME =
      "com.digicore.omnexa.common.lib.terminal.dto.response.MerchantOutletProfileDTO";
  private String profileId;
  private String outletTitle;
  private String country;
  private String state;
  private String lga;
  private String address;
  private String merchantId;
  private ProfileStatus status;
  private List<TerminalResponseDTO> terminals = new ArrayList<>();

  /** Default constructor for MerchantOutletProfileDto. */
  public MerchantOutletProfileDTO(
      String profileId,
      String outletTitle,
      String country,
      String state,
      String lga,
      String address,
      String merchantId,
      ProfileStatus status) {
    this.profileId = profileId;
    this.outletTitle = outletTitle;
    this.country = country;
    this.state = state;
    this.lga = lga;
    this.address = address;
    this.merchantId = merchantId;
    this.status = status;
  }

  public MerchantOutletProfileDTO(
      String outletTitle, String address, String merchantId, ProfileStatus status) {
    this.outletTitle = outletTitle;
    this.address = address;
    this.merchantId = merchantId;
    this.status = status;
  }
}
