/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.backoffice.modules.user.profile.dto.response;

import com.digicore.omnexa.common.lib.profile.dto.response.ProfileEditResponse;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jul-17(Thu)-2025
 */
@Getter
@Setter
public class BackOfficeProfileEditResponse implements ProfileEditResponse {
  private String profileId;
  private Long backOfficeUserProfileId;
  private String firstName;
  private String lastName;
  private String email;
  private String role;
}
