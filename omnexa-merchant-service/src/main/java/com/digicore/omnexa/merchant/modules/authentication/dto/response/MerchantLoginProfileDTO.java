/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.merchant.modules.authentication.dto.response;

import com.digicore.omnexa.common.lib.enums.KycStatus;
import java.io.Serializable;
import java.util.Collection;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jul-29(Tue)-2025
 */
@Getter
@Setter
public class MerchantLoginProfileDTO implements UserDetails, Serializable {
  private String merchantId;
  private String businessName;
  private String name;
  private String role;
  private KycStatus kycStatus;
  private boolean required;
  private String username;
  private String password;
  private Set<SimpleGrantedAuthority> permissions;

  public MerchantLoginProfileDTO(
      String merchantId,
      String businessName,
      String name,
      String role,
      KycStatus kycStatus,
      String username,
      String password) {
    this.merchantId = merchantId;
    this.businessName = businessName;
    this.name = name;
    this.role = role;
    this.kycStatus = kycStatus;
    this.username = username;
    this.password = password;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return this.permissions;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return username;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
