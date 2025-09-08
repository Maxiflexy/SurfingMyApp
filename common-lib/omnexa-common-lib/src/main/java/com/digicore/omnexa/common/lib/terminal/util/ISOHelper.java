package com.digicore.omnexa.common.lib.terminal.util;

/*
 * @author Oluwatobi Ogunwuyi
 * @createdOn May-14(Tue)-2024
 */

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.jpos.iso.ISOChannel;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOUtil;
import org.jpos.iso.packager.GenericPackager;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ISOHelper {
  private ISOHelper() {}

  public static final String A6 = "A6";
  public static final String FIVE_A = "5A";

  public static String getTransactionDateAndTime(LocalDateTime localDateTime) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMddhhmmss");
    return localDateTime.format(formatter);
  }

  public static String getTransactionDate(LocalDate localDate) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMdd");
    return localDate.format(formatter);
  }

  public static String getTransactionTime(LocalTime localTime) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HHmmss");
    return localTime.format(formatter);
  }

  public static String getStan() {
    SimpleDateFormat format = new SimpleDateFormat("hhmmss");
    return format.format(new Date());
  }

  public static String getRRN(String stan) {
    SimpleDateFormat format = new SimpleDateFormat("yyMMdd");
    return format.format(new Date()) + stan;
  }

  public static String getTransmissionDate(String stan) {
    SimpleDateFormat format = new SimpleDateFormat("MMdd");
    return format.format(new Date()) + stan;
  }

  public static String getAcquirerId(String acquirerIns) {
    String acquirer = "";
    if (acquirerIns.length() < 11) {
      String leadZero = "";
      for (int i = 1, j = acquirerIns.length(); i <= (11 - j); i++) leadZero += "0";
      acquirer = leadZero + acquirerIns;
    } else acquirer = acquirerIns;
    return acquirer;
  }

  public static void logISOMsg(ISOMsg msg) {
    log.info("----START-----");
    try {
      log.info("  MTI : " + msg.getMTI());
      for (int i = 1; i <= msg.getMaxField(); i++) {
        if (msg.hasField(i)) {
          log.info("    Field-" + i + " : " + msg.getString(i));
        }
      }
    } catch (ISOException e) {
      e.printStackTrace();
    } finally {
      log.info("----END-----");
    }
  }

  private static String generateComponentKey() {
    StringBuilder stringBuilder = new StringBuilder();

    while (stringBuilder.length() < 16) {
      int randomInt = (int) (Math.random() * 0xFFFFFF);
      stringBuilder.append(Integer.toHexString(randomInt).toUpperCase());
    }
    return stringBuilder.substring(0, 16);
  }

  public static String generate16DigitComponentKey() {
    return generateComponentKey();
  }

  public static String generate32DigitComponentKey() {
    return generate16DigitComponentKey().concat(generate16DigitComponentKey());
  }

  public static String generateHash256Value(String msg, String key) {
    MessageDigest m = null;
    String hashText = null;
    byte[] actualKeyBytes = hexStringToBytes(key);

    try {
      m = MessageDigest.getInstance("SHA-256");
      m.update(actualKeyBytes, 0, actualKeyBytes.length);
      m.update(msg.getBytes(StandardCharsets.UTF_8), 0, msg.length());
      hashText = new BigInteger(1, m.digest()).toString(16);
    } catch (NoSuchAlgorithmException ex) {
      ex.printStackTrace();
    }

    if (hashText.length() < 64) {
      int numberOfZeroes = 64 - hashText.length();
      String zeroes = "";

      for (int i = 0; i < numberOfZeroes; i++) zeroes = zeroes + "0";

      hashText = zeroes + hashText;
    }

    return hashText;
  }

  private static byte[] hex2byte(byte[] b, int offset, int len) {
    byte[] d = new byte[len];
    for (int i = 0; i < len * 2; i++) {
      int shift = i % 2 == 1 ? 0 : 4;
      d[i >> 1] |= Character.digit((char) b[offset + i], 16) << shift;
    }
    return d;
  }

  private static byte[] hexStringToBytes(String s) {
    return hex2byte(s.getBytes(), 0, s.length() >> 1);
  }

  public static byte[] prepareByteStream(byte[] isoBytes) throws ISOException {

    String content = new String(isoBytes);
    int x = content.length();

    String binlng = Integer.toBinaryString(x);

    String headerlenght = Integer.toHexString(Integer.parseInt(binlng, 2));

    headerlenght = String.format("%4s", headerlenght).replace(' ', '0');

    String contenttohex = "";
    for (int i = 0; i < content.length(); i++) {
      if (content.charAt(i) <= 9) {
        contenttohex = contenttohex + '0';
      }
      contenttohex = contenttohex + (Integer.toHexString(content.charAt(i)));
    }
    // Building the Message with Header and ISO Message
    String completemsg = headerlenght + contenttohex;

    byte[] databyte = ISOUtil.hex2byte(completemsg.trim().toUpperCase());

    return databyte;
  }

  public static ISOMsg parseFromStringToISOMsg(String message, GenericPackager packager)
      throws Exception {
    try {
      // Load package from resources directory.
      ISOMsg isoMsg = new ISOMsg();
      isoMsg.setPackager(packager);
      isoMsg.unpack(message.getBytes());
      return isoMsg;
    } catch (ISOException e) {
      return null;
    }
  }

  public static Map<String, Object> extractRawParametersFromISOMsg(ISOMsg isoMsg)
      throws ISOException {
    Map<String, Object> rawParameters = new HashMap<>();
    String _62 = null;
    if (isoMsg == null) {
      return null;
    }
    for (int i = 1; i <= isoMsg.getMaxField(); i++) {
      if (isoMsg.hasField(i)) {
        if (i == 62) _62 = isoMsg.getString(i);
      }
    }
    while (_62.length() > 0) {
      // ctms date time
      if (_62.contains("02014")) {
        int i = _62.indexOf("02014");
        _62 = _62.substring(i + 5 + 14);
        continue;
      }

      // card acceptor code
      if (_62.contains("03015")) {
        int i = _62.indexOf("03015");
        rawParameters.put("cardAcceptorCode", _62.substring(i + 5, i + 5 + 15));
        _62 = _62.substring(i + 5 + 15);
        continue;
      }

      // response timeout
      if (_62.contains("04002")) {
        int i = _62.indexOf("04002");
        rawParameters.put("responseTimeout", _62.substring(i + 5, i + 5 + 2));
        _62 = _62.substring(i + 5 + 2);
      }

      // currency code
      if (_62.contains("05003")) {
        int i = _62.indexOf("05003");
        rawParameters.put("currencyCode", _62.substring(i + 5, i + 5 + 3));
        _62 = _62.substring(i + 5 + 3);
        continue;
      }

      // country code
      if (_62.contains("06003")) {
        int i = _62.indexOf("06003");
        rawParameters.put("countryCode", _62.substring(i + 5, i + 5 + 3));
        _62 = _62.substring(i + 5 + 3);
        continue;
      }

      //            call home time
      if (_62.contains("07002")) {
        int i = _62.indexOf("07002");
        rawParameters.put("callHomeTime", _62.substring(i + 5, i + 5 + 2));
        _62 = _62.substring(i + 5 + 2);
        continue;
      }

      if (_62.contains("08004")) {
        int i = _62.indexOf("08004");

        rawParameters.put("categoryCode", _62.substring(i + 5, i + 5 + 4));
        _62 = _62.substring(i + 5 + 4);
      }

      // merchant Name and Location
      if (_62.contains("52040")) {
        int i = _62.indexOf("52040");
        rawParameters.put("merchantNameAndLocation", _62.substring(i + 5, i + 5 + 40));

        _62 = _62.substring(i + 5 + 40);
        continue;
      }
      break;
    }

    return rawParameters;
  }

  public static String combineKeyComponents(String a, String b) {
    StringBuilder stringBuilder = new StringBuilder();
    for (int i = 0; i < a.length(); i++) {
      int charA = Character.digit(a.charAt(i), 16);
      int charB = Character.digit(b.charAt(i), 16);
      if (charA != -1 && charB != -1) { // Check for valid hex digit
        stringBuilder.append(Integer.toHexString(charA ^ charB).toUpperCase());
      } else {
        // Handle invalid hex characters (optional)
        stringBuilder.append('?'); // Or throw an exception
      }
    }
    return stringBuilder.toString();
  }

  public static String performXOROperation(
      String dynamicHexadecimalString, String staticHexadecimalString) {
    // Convert hexadecimal strings to integers
    int staticHexadecimalInt = Integer.parseInt(staticHexadecimalString, 16);
    int dynamicHexadecimalInt = Integer.parseInt(dynamicHexadecimalString, 16);

    // Perform XOR operation
    int result = staticHexadecimalInt ^ dynamicHexadecimalInt;

    // Convert result back to hexadecimal string
    String hexResult = Integer.toHexString(result).toUpperCase();

    //    // Ensure the result has two characters
    if (hexResult.length() == 1) {
      hexResult = "0" + hexResult; // prepend 0 if necessary
    }
    return hexResult;
  }

  public static void disconnect(ISOChannel isoChannel) {
    try {
      log.info("Disconnecting from server.....");
      isoChannel.disconnect();
      log.info("Disconnected from server!");
    } catch (Exception e) {
      log.error(
          "An error occurred while trying to disconnect from iso server : {}", e.getMessage());
    }
  }
}
