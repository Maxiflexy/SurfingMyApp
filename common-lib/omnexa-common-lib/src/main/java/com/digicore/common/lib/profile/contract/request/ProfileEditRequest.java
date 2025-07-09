/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.common.lib.profile.contract.request;

/**
 * Marker interface for profile edit requests.
 *
 * <p>This interface serves as a contract for classes that represent requests to edit profile
 * information. It does not define any methods or fields but acts as a marker to categorize such
 * requests within the application.
 *
 * <p>Usage: - Implement this interface in classes that encapsulate profile edit request data. -
 * Example:
 *
 * <pre>
 *   public class UserProfileEditRequest implements ProfileEditRequest {
 *       private String username;
 *       private String email;
 *       // Getters and setters
 *   }
 *   </pre>
 *
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jul-04(Fri)-2025
 */
public interface ProfileEditRequest {}
