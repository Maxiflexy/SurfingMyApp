/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.common.lib.profile.dto.response;

/**
 * Marker interface for profile edit responses.
 *
 * <p>This interface serves as a contract for classes that represent responses to profile edit
 * operations. It does not define any methods or fields but acts as a marker to categorize such
 * responses within the application.
 *
 * <p>Usage: - Implement this interface in classes that encapsulate profile edit response data. -
 * Example:
 *
 * <pre>
 *   public class UserProfileEditResponse implements ProfileEditResponse {
 *       private String status;
 *       private String message;
 *       // Getters and setters
 *   }
 *   </pre>
 *
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jul-04(Fri)-2025
 */
public interface ProfileEditResponse {}
