/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.common.lib.approval.rule.data.model;

import com.digicore.omnexa.common.lib.model.Auditable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.io.Serializable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/*
 * @author Oluwatobi Ogunwuyi
 * @createdOn Feb-09(Sun)-2025
 */

@Entity
@Table(name = "approval_rule")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class ApprovalRule extends Auditable<String> implements Serializable {
  private boolean isDeleted = false;

  private String activity;
  private String module;

  @Column(columnDefinition = "text")
  private String ruleConfig;

  private boolean global;
  private boolean supportThresholdConfiguration = false;
}
