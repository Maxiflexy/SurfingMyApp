/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.common.lib.approval.rule.data.repository;

import com.digicore.omnexa.common.lib.approval.rule.data.model.ApprovalRule;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/*
 * @author Oluwatobi Ogunwuyi
 * @createdOn Feb-09(Sun)-2025
 */

public interface ApprovalRuleRepository extends JpaRepository<ApprovalRule, Long> {
  List<ApprovalRule> findAllByIsDeletedFalseAndActivityAndModule(String activity, String module);

  //  Optional<ApprovalRule> findFirstByIsDeletedFalseAndActivityModule(
  //      String activity, String module);

  Optional<ApprovalRule> findFirstByIsDeletedFalseAndModuleAndGlobal(String module, boolean global);

  List<ApprovalRule> findAllByIsDeletedFalse();

  List<ApprovalRule> findAllByIsDeletedFalseAndModule(String module);

  Optional<ApprovalRule> findFirstByIsDeletedFalseAndActivityAndModuleAndGlobal(
      String activity, String module, boolean global);
}
