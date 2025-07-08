/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.common.lib.profile.contract.response;

/**
 * Marker interface for profile information responses.
 *
 * <p>This interface serves as a contract for classes that represent responses containing profile
 * information. It does not define any methods or fields but acts as a marker to categorize such
 * responses within the application.
 *
 * <p>Usage: - Implement this interface in classes that encapsulate profile information response
 * data. - Example:
 *
 * <pre>
 *   public class UserProfileInfoResponse implements ProfileInfoResponse {
 *       private String name;
 *       private String email;
 *       // Getters and setters
 *   }
 *   </pre>
 *
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jul-04(Fri)-2025
 */
public interface ProfileInfoResponse {}
