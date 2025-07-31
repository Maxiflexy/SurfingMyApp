/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.merchant.modules.authorization.data.repository;

import static com.digicore.omnexa.common.lib.authorization.dto.response.PermissionDTO.PERMISSION_DTO;

import com.digicore.omnexa.common.lib.authorization.dto.response.PermissionDTO;
import com.digicore.omnexa.merchant.modules.authorization.data.model.MerchantUserPermission;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jul-21(Mon)-2025
 */
public interface MerchantUserPermissionRepository
    extends JpaRepository<MerchantUserPermission, Long> {

  @Query("SELECT new " + PERMISSION_DTO + "(p.name) FROM MerchantUserPermission p")
  List<PermissionDTO> retrieveAllPermissionName();

  List<MerchantUserPermission> findByNameIn(Collection<String> names);

  Optional<MerchantUserPermission> findFirstByNameOrderByCreatedDate(String name);
}
