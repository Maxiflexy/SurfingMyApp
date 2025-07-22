/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.backoffice.modules.user.authentication.data.model;

import com.digicore.omnexa.backoffice.modules.user.profile.data.model.BackOfficeUserProfile;
import com.digicore.omnexa.common.lib.model.BaseAuthModel;
import jakarta.persistence.*;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jul-18(Fri)-2025
 */
@Entity
@Table(
    name = "backoffice_user_auth_profile",
    indexes = {@Index(name = "idx_username", columnList = "username")})
@Getter
@Setter
@ToString
public class BackOfficeUserAuthProfile extends BaseAuthModel implements Serializable {
  @OneToOne(cascade = CascadeType.ALL, optional = false)
  @JoinColumn(name = "backoffice_user_profile_id", nullable = false)
  private BackOfficeUserProfile backOfficeUserProfile;

  @OneToOne(mappedBy = "backOfficeUserAuthProfile", cascade = CascadeType.ALL, optional = false)
  private BackOfficeUserLoginProfile backOfficeUserLoginProfile;
}
