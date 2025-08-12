/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.backoffice.modules.merchant.profile.dto.response;

import com.digicore.omnexa.common.lib.enums.KycStatus;
import com.digicore.omnexa.common.lib.profile.dto.request.ProfileEditRequest;
import com.digicore.omnexa.common.lib.profile.dto.response.ProfileInfoResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * @author Onyekachi Ejemba
 * @createdOn Aug-11(Mon)-2025
 */
@Getter
@Setter
public class MerchantKycProfileResponse implements ProfileEditRequest, ProfileInfoResponse {
  private BusinessProfile businessProfile;
  private BankDetails bankDetails;
  private KycDetails kycDetails;
  private KycStatus kycStatus;
  private String comment;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private List<MerchantKycCommentDTO> comments;

  @Getter
  @Setter
  static class BusinessProfile {
    private String businessName;
    private String businessRegistrationType;
    private String industry;
    private String businessDescription;
    private String businessWebsite;
    private String businessTaxNumber;
    private String businessAddress;
    private String businessCity;
    private String businessState;
    private String businessCountry;
    private String businessPhoneNumber;
  }

  @Getter
  @Setter
  static class BankDetails {
    private String bankName;
    private String bankAccountNumber;
  }

  @Getter
  @Setter
  static class KycDetails {
    private List<Document> kycDocuments;
    private List<Director> directors;
  }

  @Getter
  @Setter
  static class Document {
    private String type;
    private String documentFileUrl;
  }

  @Getter
  @Setter
  static class Director {
    private String directorFirstName;
    private String directorLastName;
    private String directorPhoneNumber;
    private String directorBVN;
    private Document document;
  }

  @Getter
  @Setter
  @AllArgsConstructor
  @NoArgsConstructor
  public static class MerchantKycCommentDTO {
    private String comment;
    private String commenter;
  }
}
