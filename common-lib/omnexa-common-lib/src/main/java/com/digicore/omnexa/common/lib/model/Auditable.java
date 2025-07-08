/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.common.lib.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * Abstract base class to provide auditing metadata for JPA entities.
 *
 * <p>This class defines common audit fields such as:
 *
 * <ul>
 *   <li><strong>createdBy</strong> and <strong>createdDate</strong>
 *   <li><strong>lastModifiedBy</strong> and <strong>lastModifiedDate</strong>
 *   <li><strong>version</strong> for optimistic locking
 *   <li><strong>id</strong> as the primary key
 * </ul>
 *
 * <p>It is annotated with {@code @MappedSuperclass} so that all inheriting entities automatically
 * inherit these fields without requiring them to redefine audit logic.
 *
 * @param <T> the user or principal type used for tracking who created or modified the entity
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jun-24(Tue)-2025
 */
@MappedSuperclass
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class Auditable<T> {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @CreatedBy protected T createdBy;

  @CreatedDate protected LocalDateTime createdDate;

  @LastModifiedBy protected T lastModifiedBy;

  @LastModifiedDate protected LocalDateTime lastModifiedDate;

  @Version private Integer version;
}
