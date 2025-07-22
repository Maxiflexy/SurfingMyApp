/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.backoffice.modules.user.data.converter;

import com.digicore.omnexa.common.lib.converter.StringEnumConverter;
import com.digicore.omnexa.backoffice.modules.user.data.enums.BackOfficeUserStatus;
import jakarta.persistence.Converter;

/**
 * JPA converter for BackOfficeUserStatus enum.
 *
 * <p>Converts between BackOfficeUserStatus enum values and their string
 * representation for database persistence.
 *
 * @author Onyekachi Ejemba
 *  * @createdOn Jul-08(Tue)-2025
 */
@Converter
public class BackOfficeUserStatusConverter extends StringEnumConverter<BackOfficeUserStatus> {

    public BackOfficeUserStatusConverter() {
        super(BackOfficeUserStatus.class);
    }
}