/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.common.lib.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
@Getter
@Setter
@MappedSuperclass
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
public class BaseAuthModel extends Auditable<String> implements Serializable {

  @Column(nullable = false)
  private String username;

  @Column(nullable = false)
  private String password;
}
