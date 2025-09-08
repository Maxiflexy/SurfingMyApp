package com.digicore.omnexa.common.lib.terminal.util;

/*
 * @author Oluwatobi Ogunwuyi
 * @createdOn May-20(Mon)-2024
 */

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class HSMHelper {

  private HSMHelper() {}

  public static String generateClearZPK(String encryptedZMK, String issuerZPK) throws Exception {

    issuerZPK = issuerZPK.substring(0, 32);

    String partA = encryptedZMK.substring(0, 16);
    String partB = encryptedZMK.substring(16);

    // Apply Variant on ZMK partB
    String partBXorResult = ISOHelper.performXOROperation(ISOHelper.A6, partB.substring(0, 2));
    String variantedZMKOne = partA.concat(partBXorResult.concat(partB.substring(2)));

    String encryptedVariantedZMKOne =
        TripleDesCipher.decrypt(issuerZPK.substring(0, 16).concat("0".repeat(16)), variantedZMKOne);

    // Apply Variant on ZMK partB
    String partAXorResult = ISOHelper.performXOROperation(ISOHelper.FIVE_A, partB.substring(0, 2));
    String variantedZMKTwo = partA.concat(partAXorResult.concat(partB.substring(2)));

    String encryptedVariantedZMKTwo =
        TripleDesCipher.decrypt(issuerZPK.substring(16).concat("0".repeat(16)), variantedZMKTwo);

    return encryptedVariantedZMKOne
        .substring(0, 16)
        .concat(encryptedVariantedZMKTwo.substring(0, 16))
        .toUpperCase();
  }
}
