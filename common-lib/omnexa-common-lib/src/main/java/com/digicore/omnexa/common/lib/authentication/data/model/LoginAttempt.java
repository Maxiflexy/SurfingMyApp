/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.common.lib.authentication.data.model;

import com.digicore.omnexa.common.lib.converter.AuthenticationTypeConverter;
import com.digicore.omnexa.common.lib.enums.AuthenticationType;
import com.digicore.omnexa.common.lib.model.Auditable;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.time.ZonedDateTime;
import lombok.*;

/**
 * Base model class extending {@link Auditable} to include soft deletion support.
 *
 * <p>This class serves as a common superclass for all JPA entities that require:
 *
 * <ul>
 *   <li>Auditing metadata (createdBy, createdDate, etc.)
 *   <li>Soft deletion via the {@code deleted} flag
 *   <li>Serialization support
 * </ul>
 *
 * <p>Annotated with {@code @MappedSuperclass}, it allows inheriting entities to reuse its fields
 * and behavior. The {@code @JsonIgnoreProperties(ignoreUnknown = true)} annotation ensures unknown
 * JSON fields are ignored during deserialization, making the model more resilient to changes.
 *
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jun-25(Wed)-2025
 */
@Entity
@Table(name = "login_attempt")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class LoginAttempt extends Auditable<String> implements Serializable {

  private ZonedDateTime lastLoginAt;
  private ZonedDateTime lastFailedLoginAt;
  private ZonedDateTime automatedUnlockTime;
  private int failedLoginAttempts;
  private String lastLoginIp;
  private String deviceInfo;
  private boolean loginLocked;
  private String username;
  private String name;

  @Convert(converter = AuthenticationTypeConverter.class)
  private AuthenticationType authenticationType;
}
