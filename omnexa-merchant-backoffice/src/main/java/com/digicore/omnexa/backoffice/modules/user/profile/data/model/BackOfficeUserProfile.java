/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.backoffice.modules.user.profile.data.model;

import com.digicore.omnexa.common.lib.converter.ProfileStatusConverter;
import com.digicore.omnexa.common.lib.converter.ProfileVerificationStatusConverter;
import com.digicore.omnexa.common.lib.enums.ProfileStatus;
import com.digicore.omnexa.common.lib.enums.ProfileVerificationStatus;
import com.digicore.omnexa.common.lib.model.BaseUserModel;
import com.digicore.omnexa.common.lib.util.RequestUtil;
import jakarta.persistence.*;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.RandomStringUtils;

/**
 * Entity representing a back office user in the system.
 *
 * <p>This entity stores user information including personal details, authentication credentials,
 * and status information. It extends BaseModel to inherit audit fields.
 *
 * @author Onyekachi Ejemba
 * @createdOn Jul-08(Tue)-2025
 */
@Entity
@Table(
    name = "backoffice_user_profile",
    indexes = {@Index(name = "idx_email", columnList = "email")})
@Getter
@Setter
public class BackOfficeUserProfile extends BaseUserModel implements Serializable {

  /**
   * The current status of the user. This field is converted to and from its string representation
   * using {@link ProfileStatusConverter}.
   */
  @Convert(converter = ProfileStatusConverter.class)
  private ProfileStatus profileStatus = ProfileStatus.INACTIVE;

  @Convert(converter = ProfileVerificationStatusConverter.class)
  private ProfileVerificationStatus profileVerificationStatus =
      ProfileVerificationStatus.PENDING_EMAIL_VERIFICATION;

  @PrePersist
  public void generateUserId() {
    if (RequestUtil.nullOrEmpty(getProfileId())) {
      setProfileId("B" + RandomStringUtils.secure().nextNumeric(6));
    }
  }
}
