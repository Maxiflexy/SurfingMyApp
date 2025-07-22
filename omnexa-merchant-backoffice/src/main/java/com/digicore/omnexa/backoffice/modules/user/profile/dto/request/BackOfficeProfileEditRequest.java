/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.backoffice.modules.user.profile.dto.request;

import com.digicore.omnexa.common.lib.profile.dto.request.ProfileEditRequest;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jul-17(Thu)-2025
 */
@Getter
@Setter
public class BackOfficeProfileEditRequest implements ProfileEditRequest {
  private String email;
  private String firstName;
  private String lastName;
  private String role;
  private String profileId;
  private String password;
}
