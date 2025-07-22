/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.backoffice.modules.user.profile.dto.response;

import com.digicore.omnexa.common.lib.enums.ProfileStatus;
import com.digicore.omnexa.common.lib.enums.ProfileVerificationStatus;
import com.digicore.omnexa.common.lib.profile.dto.response.ProfileInfoResponse;
import java.time.ZonedDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jul-17(Thu)-2025
 */
@Getter
@Setter
public class BackOfficeUserProfileDTO implements ProfileInfoResponse {

  public static final String BACKOFFICE_USER_PROFILE_DTO =
      "com.digicore.omnexa.backoffice.modules.user.profile.dto.response.BackOfficeUserProfileDTO";

  public BackOfficeUserProfileDTO(
      Long id,
      String profileId,
      ProfileStatus profileStatus,
      ProfileVerificationStatus profileVerificationStatus) {
    this.id = id;
    this.profileId = profileId;
    this.profileStatus = profileStatus;
    this.profileVerificationStatus = profileVerificationStatus;
  }

  public BackOfficeUserProfileDTO(
      String email, String firstName, String lastName, String profileId, String role) {
    this.email = email;
    this.firstName = firstName;
    this.lastName = lastName;
    this.profileId = profileId;
    this.role = role;
  }

  private Long id;
  private String email;
  private String firstName;
  private String lastName;
  private String phoneNumber;
  private String profileId;
  private String profilePictureUrl;
  private String role;
  private ProfileStatus profileStatus;
  private String username;
  private ZonedDateTime createdDate;
  private ProfileVerificationStatus profileVerificationStatus;
}
