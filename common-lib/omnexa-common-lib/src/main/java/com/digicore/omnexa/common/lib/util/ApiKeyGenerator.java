/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.common.lib.util;

import com.digicore.omnexa.common.lib.exception.OmnexaException;
import java.security.MessageDigest;
import java.security.SecureRandom;

/**
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jul-28(Mon)-2025
 */
public class ApiKeyGenerator {
  private static final int KEY_PART_LENGTH = 20;

  public static String generateApiKey() {
    byte[] randomBytes = new byte[16];
    new SecureRandom().nextBytes(randomBytes);

    return hashAndTruncate(randomBytes);
  }

  private static String hashAndTruncate(byte[] data) {
    try {
      MessageDigest digest = MessageDigest.getInstance("SHA-256");
      byte[] hashed = digest.digest(data);

      String hex = bytesToHex(hashed);
      return hex.substring(0, Math.min(hex.length(), ApiKeyGenerator.KEY_PART_LENGTH));
    } catch (Exception e) {
      throw new OmnexaException("Hashing failed", e);
    }
  }

  private static String bytesToHex(byte[] bytes) {
    StringBuilder result = new StringBuilder();
    for (byte b : bytes) {
      result.append(String.format("%02X", b));
    }
    return result.toString();
  }
}
