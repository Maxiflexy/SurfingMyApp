/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.common.lib.backgound.startup;

/**
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jul-18(Fri)-2025
 */
public interface StartupService {
  default void updateSystemPermissions() {}

  default void updateSystemRoles() {}

  default void updateSystemUsers() {}
}
