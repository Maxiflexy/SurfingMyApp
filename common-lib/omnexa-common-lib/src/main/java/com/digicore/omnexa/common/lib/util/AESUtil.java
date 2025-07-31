/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.common.lib.util;

import com.digicore.omnexa.common.lib.exception.OmnexaException;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jul-28(Mon)-2025
 */
public class AESUtil {
  private AESUtil() {}

  private static final String AES_ALGORITHM = "AES";
  private static final String TRANSFORMATION = "AES/CBC/NoPadding";

  public static String encrypt(String dataToEncrypt, String key) {
    try {
      Cipher cipher = Cipher.getInstance(TRANSFORMATION);
      SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), AES_ALGORITHM);
      IvParameterSpec ivspec = new IvParameterSpec(key.getBytes());
      cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
      int blockSize = 16;
      byte[] data = dataToEncrypt.getBytes();
      int padding = blockSize - data.length % blockSize;
      byte[] paddedData = new byte[data.length + padding];
      System.arraycopy(data, 0, paddedData, 0, data.length);
      byte[] encrypted = cipher.doFinal(paddedData);
      Base64.Encoder encoder = Base64.getEncoder();
      return encoder.encodeToString(encrypted);
    } catch (Exception e) {
      throw new OmnexaException("Error while encrypting", e);
    }
  }

  public static String decrypt(String encryptedData, String key) {
    try {
      Base64.Decoder decoder = Base64.getDecoder();
      byte[] encrypted1 = decoder.decode(encryptedData);
      Cipher cipher = Cipher.getInstance(TRANSFORMATION);
      SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), AES_ALGORITHM);
      IvParameterSpec ivspec = new IvParameterSpec(key.getBytes());
      cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);
      byte[] original = cipher.doFinal(encrypted1);
      String originalString = new String(original);
      return originalString.trim();
    } catch (Exception e) {
      throw new OmnexaException("Error while decrypting", e);
    }
  }

  public static byte[] generateAESKeyBytes(int bits) {
    try {
      KeyGenerator keyGen = KeyGenerator.getInstance("AES");
      keyGen.init(bits); // 128, 192, or 256
      SecretKey secretKey = keyGen.generateKey();
      return secretKey.getEncoded();
    } catch (Exception e) {
      throw new OmnexaException("Error while generating AES key", e);
    }
  }

  public static String generateBase64AESKey() {
    byte[] key = generateAESKeyBytes(256);
    return Base64.getEncoder().encodeToString(key);
  }
}
