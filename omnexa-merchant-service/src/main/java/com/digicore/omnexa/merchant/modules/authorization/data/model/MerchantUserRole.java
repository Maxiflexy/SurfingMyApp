/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.merchant.modules.authorization.data.model;

import com.digicore.omnexa.common.lib.model.BaseRoleModel;
import com.digicore.omnexa.merchant.modules.profile.data.model.MerchantProfile;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jul-21(Mon)-2025
 */
@Entity
@Table(name = "merchant_user_role")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class MerchantUserRole extends BaseRoleModel implements Serializable {

  @ManyToMany(
      fetch = FetchType.LAZY,
      cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinTable(
      name = "merchant_user_role_and_permission_mapping",
      joinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")},
      inverseJoinColumns = {@JoinColumn(name = "permission_id", referencedColumnName = "id")})
  @ToString.Exclude
  private Set<MerchantUserPermission> permissions = new HashSet<>();

  @ManyToOne
  @JoinColumn(name = "merchant_profile_id")
  private MerchantProfile merchantProfile;
}
