package com.digicore.omnexa.common.lib.terminal.util;

/*
 * @author Oluwatobi Ogunwuyi
 * @createdOn May-08(Wed)-2024
 */

import org.springframework.stereotype.Component;

@Component
public class PinBlockCalculator {

  private PinBlockCalculator() {}

  private static final String PIN_BLOCK_FILLER = "F";

  public static String calculatePinBlock(String pin, String pan, String pinKey) throws Exception {
    // PIN Block Part 1
    String pinBlockPart1 = "0" + pin.length() + pin + repeat(PIN_BLOCK_FILLER, 10);

    // Extract relevant digits from PAN
    String pinBlockPart2 = generatePinBlockPart2(pan);

    // Convert PIN block parts to byte arrays
    byte[] part1Bytes = TripleDesCipher.string2Hex(pinBlockPart1);
    byte[] part2Bytes = TripleDesCipher.string2Hex(pinBlockPart2);

    // Perform XOR operation
    byte[] xorResult = xor(part1Bytes, part2Bytes);

    return TripleDesCipher.encrypt(TripleDesCipher.hex2String(xorResult), pinKey)
        .toUpperCase(); // Ensure uppercase output
  }

  public static String extractPinFromPinBlock(String pan, String pinBlock, String privateKey)
      throws Exception {
    String originalXorResult = TripleDesCipher.decrypt(pinBlock, privateKey);

    String pinBlockPart2 = generatePinBlockPart2(pan);

    String plainPinBlock =
        TripleDesCipher.hex2String(
            xor(
                TripleDesCipher.string2Hex(originalXorResult),
                TripleDesCipher.string2Hex(pinBlockPart2)));

    // plainPinBlock = plainPinBlock.substring(2);

    return plainPinBlock.substring(2, 6);
  }

  private static String repeat(String str, int count) {
    return String.valueOf(str).repeat(Math.max(0, count));
  }

  private static byte[] xor(byte[] a, byte[] b) {
    int length = Math.min(a.length, b.length);
    byte[] result = new byte[length];
    for (int i = 0; i < length; i++) {
      result[i] = (byte) (a[i] ^ b[i]);
    }
    return result;
  }

  private static String generatePinBlockPart2(String pan) {
    // Extract relevant digits from PAN
    String panSubstring = pan.substring(pan.length() - 13, pan.length() - 1);

    // PIN Block Part 2
    return "0000" + panSubstring;
  }
}
