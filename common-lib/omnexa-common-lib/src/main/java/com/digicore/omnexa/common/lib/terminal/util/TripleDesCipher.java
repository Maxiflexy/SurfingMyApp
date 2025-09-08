package com.digicore.omnexa.common.lib.terminal.util;

/*
 * @author Oluwatobi Ogunwuyi
 * @createdOn May-03(Fri)-2024
 */

import java.util.Arrays;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author Jose Luis Montes de Oca
 */
public class TripleDesCipher {
  private TripleDesCipher() {}

  public static String TRIPLE_DES_TRANSFORMATION = "DESede/ECB/Nopadding";

  public static String ALGORITHM = "DESede";

  public static byte[] getKey(byte[] key) {
    byte[] bKey = new byte[24];
    int i;
    if (key.length == 8) {
      for (i = 0; i < 8; ++i) {
        bKey[i] = key[i];
        bKey[i + 8] = key[i];
        bKey[i + 16] = key[i];
      }
    } else if (key.length == 16) {
      for (i = 0; i < 8; ++i) {
        bKey[i] = key[i];
        bKey[i + 8] = key[i + 8];
        bKey[i + 16] = key[i];
      }
    } else if (key.length == 24) {
      bKey = Arrays.copyOf(key, key.length);
    }

    return bKey;
  }

  public static String hex2String(byte[] data) {
    if (data == null) {
      return "";
    } else {
      StringBuilder result = new StringBuilder();

      for (byte datum : data) {
        int tmp = datum >> 4;
        result.append(Integer.toString(tmp & 15, 16));
        tmp = datum & 15;
        result.append(Integer.toString(tmp & 15, 16));
      }

      return result.toString();
    }
  }

  public static byte[] string2Hex(String data) {
    byte[] result = new byte[data.length() / 2];

    for (int i = 0; i < data.length(); i += 2) {
      result[i / 2] = (byte) Integer.parseInt(data.substring(i, i + 2), 16);
    }

    return result;
  }

  private static byte[] h2b(String hex) {
    if ((hex.length() & 0x01) == 0x01) throw new IllegalArgumentException();
    byte[] bytes = new byte[hex.length() / 2];
    for (int idx = 0; idx < bytes.length; ++idx) {
      int hi = Character.digit((int) hex.charAt(idx * 2), 16);
      int lo = Character.digit((int) hex.charAt(idx * 2 + 1), 16);
      if ((hi < 0) || (lo < 0)) throw new IllegalArgumentException();
      bytes[idx] = (byte) ((hi << 4) | lo);
    }
    return bytes;
  }

  private static String b2h(byte[] bytes) {
    char[] hex = new char[bytes.length * 2];
    for (int idx = 0; idx < bytes.length; ++idx) {
      int hi = (bytes[idx] & 0xF0) >>> 4;
      int lo = (bytes[idx] & 0x0F);
      hex[idx * 2] = (char) (hi < 10 ? '0' + hi : 'A' - 10 + hi);
      hex[idx * 2 + 1] = (char) (lo < 10 ? '0' + lo : 'A' - 10 + lo);
    }
    return new String(hex);
  }

  public static String decrypt(String input, String keyPlain) throws Exception {
    // byte[] tmp = h2b(keyPlain);
    // byte[] key = new byte[24];
    //  System.arraycopy(tmp, 0, key, 0, 16);
    //  System.arraycopy(tmp, 0, key, 16, 8);
    Cipher cipher = Cipher.getInstance(TRIPLE_DES_TRANSFORMATION);
    cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(getKey(string2Hex(keyPlain)), ALGORITHM));
    byte[] plaintext = cipher.doFinal(string2Hex(input));
    return hex2String(plaintext);
  }

  public static String encrypt(String input, String keyPlain) throws Exception {
    // byte[] tmp = h2b(keyPlain);
    // byte[] key = new byte[24];
    //  System.arraycopy(tmp, 0, key, 0, 16);
    //  System.arraycopy(tmp, 0, key, 16, 8);
    Cipher cipher = Cipher.getInstance(TRIPLE_DES_TRANSFORMATION);
    cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(getKey(string2Hex(keyPlain)), ALGORITHM));
    byte[] plaintext = cipher.doFinal(string2Hex(input));
    return hex2String(plaintext);
  }
}
