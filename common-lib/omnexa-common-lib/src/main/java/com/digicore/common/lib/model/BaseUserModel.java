/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.common.lib.model;

import com.digicore.common.lib.util.RequestUtil;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Base class for user-related entities, extending {@link BaseModel} to include common user fields.
 *
 * <p>This class provides reusable fields such as:
 *
 * <ul>
 *   <li><strong>email</strong>: the user's email address
 *   <li><strong>firstName</strong>: the user's first name (required)
 *   <li><strong>lastName</strong>: the user's last name (required)
 *   <li><strong>phoneNumber</strong>: the user's contact number (optional)
 * </ul>
 *
 * <p>Intended to be used as a superclass for entity classes representing users (e.g., Customer,
 * Admin). It inherits soft-delete and audit metadata from {@link BaseModel}.
 *
 * <p>Unknown properties during deserialization are ignored to enhance flexibility.
 *
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jun-25(Wed)-2025
 */
@Getter
@Setter
@MappedSuperclass
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
public class BaseUserModel extends BaseModel implements Serializable {

  private String email;

  @Column(nullable = false)
  private String firstName;

  @Column(nullable = false)
  private String lastName;

  private String phoneNumber;

  private String profileId;

  private String profilePictureUrl;

  @PrePersist
  public void generateProfileId() {
    if (RequestUtil.nullOrEmpty(getProfileId()))
      setProfileId("MU".concat(RequestUtil.generateProfileId()));
  }
}
