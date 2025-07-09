/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.backoffice.modules.user.data.enums;

/**
 * Enum representing the status of a back office user.
 *
 * <p>This enum defines the possible states of a user account:
 * <ul>
 *   <li><b>ACTIVE</b> – User account is active and can access the system
 *   <li><b>INACTIVE</b> – User account is inactive (default state after signup)
 *   <li><b>SUSPENDED</b> – User account has been suspended
 *   <li><b>DELETED</b> – User account has been soft deleted
 * </ul>
 *
 * @author Onyekachi Ejemba
 * @createdOn Jul-08(Tue)-2025
 */
public enum BackOfficeUserStatus {
    ACTIVE,
    INACTIVE,
    SUSPENDED,
    DELETED
}