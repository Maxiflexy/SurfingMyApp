/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.backoffice.modules.user.authentication.data.model;

import com.digicore.omnexa.common.lib.model.BaseLoginModel;
import jakarta.persistence.*;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Entity class representing the authentication profile of a backoffice user.
 *
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jun-25(Wed)-2025
 */
@Entity
@Table(
    name = "backoffice_user_login_profile",
    indexes = {@Index(name = "idx_last_login", columnList = "lastLoginAt")})
@Getter
@Setter
@ToString
public class BackOfficeUserLoginProfile extends BaseLoginModel implements Serializable {

  @OneToOne
  @JoinColumn(name = "backoffice_user_auth_profile_id", nullable = false)
  private BackOfficeUserAuthProfile backOfficeUserAuthProfile;
}
