/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.backoffice.modules.user.authentication.dto.response;

import java.io.Serializable;
import java.util.Collection;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * DTO representing the authentication profile of a back office user for login purposes.
 *
 * <p>This class implements {@link UserDetails} to integrate with Spring Security
 * and {@link Serializable} for object serialization.
 *
 * @author Onyekachi Ejemba
 * @createdOn Jul-31(Thu)-2025
 */
@Getter
@Setter
public class BackOfficeLoginProfileDTO implements UserDetails, Serializable {

    private String profileId;
    private String role;
    private String username;
    private String firstName;
    private String lastName;
    private String password;
    private Set<SimpleGrantedAuthority> permissions;

    public BackOfficeLoginProfileDTO(
            String profileId,
            String role,
            String username,
            String firstName,
            String lastName,
            String password) {
        this.profileId = profileId;
        this.role = role;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
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