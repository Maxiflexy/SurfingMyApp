/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omxena.backoffice.user.data.model;

import com.digicore.omnexa.backoffice.modules.user.data.converter.BackOfficeUserStatusConverter;
import com.digicore.omnexa.backoffice.modules.user.data.enums.BackOfficeUserStatus;
import com.digicore.omnexa.common.lib.model.BaseModel;
import com.digicore.omnexa.common.lib.util.RequestUtil;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.RandomStringUtils;

import java.io.Serializable;

/**
 * Entity representing a back office user in the system.
 *
 * <p>This entity stores user information including personal details,
 * authentication credentials, and status information. It extends
 * BaseModel to inherit audit fields.
 *
 * @author Onyekachi Ejemba
 * @createdOn Jul-08(Tue)-2025
 */
@Entity
@Table(
        name = "back_office_user",
        indexes = {
                @Index(name = "idx_email", columnList = "email"),
                @Index(name = "idx_user_id", columnList = "userId"),
                @Index(name = "idx_status", columnList = "status")
        })
@Getter
@Setter
@ToString(exclude = "password")
public class BackOfficeUser extends BaseModel implements Serializable {

    /** Unique user identifier in format AH + 8 digits */
    @Column(unique = true, nullable = false)
    private String userId;

    /** User's email address */
    @Column(unique = true, nullable = false)
    private String email;

    /** User's first name */
    private String firstName;

    /** User's last name */
    private String lastName;

    /** Encrypted password */
    private String password;

    /** User account status */
    @Convert(converter = BackOfficeUserStatusConverter.class)
    @Column(nullable = false)
    private BackOfficeUserStatus status = BackOfficeUserStatus.INACTIVE;

    @PrePersist
    public void generateUserId() {
        if (RequestUtil.nullOrEmpty(getUserId())) {
            setUserId("AH" + RandomStringUtils.secure().nextNumeric(8));
        }
    }
}