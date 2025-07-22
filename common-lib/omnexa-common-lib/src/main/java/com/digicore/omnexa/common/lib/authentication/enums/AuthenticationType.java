/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.common.lib.authentication.enums;

/**
 * Enum representing the different types of authentication mechanisms supported within the system.
 *
 * <p>This can be used to identify or enforce specific authentication methods during login,
 * transaction verification, or sensitive operations.
 *
 * <ul>
 *   <li><b>PASSWORD</b> – Traditional username/password authentication.
 *   <li><b>FINGERPRINT</b> – Biometric authentication using fingerprint scans.
 *   <li><b>OTP</b> – One-Time Passwords sent via SMS, email, or authenticator apps.
 *   <li><b>PIN</b> – Personal Identification Number, typically a short numeric code.
 *   <li><b>HARD_TOKEN</b> – Physical security tokens used to generate codes.
 *   <li><b>SOFT_TOKEN</b> – Software-based token generators (e.g., mobile apps like Google
 *       Authenticator).
 * </ul>
 *
 * <p>Useful in enforcing multi-factor authentication (MFA), audit trails, or adaptive security
 * flows.
 *
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jun-25(Wed)-2025
 */
public enum AuthenticationType {
  PASSWORD,
  FINGERPRINT,
  OTP,
  PIN,
  HARD_TOKEN,
  SOFT_TOKEN
}
