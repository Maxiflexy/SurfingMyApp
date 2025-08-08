package com.digicore.omnexa.common.lib.util;

import static java.util.Arrays.stream;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Oluwatobi Ogunwuyi
 * @createdOn Oct-17(Mon)-2022
 */
@UtilityClass
public class MoneyUtil {

  /** Defined centrally, to allow for easy changes to the rounding mode. */
  private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_EVEN;

  private static final BigDecimal TWO = new BigDecimal("2");
  private static final BigDecimal HUNDRED = BigDecimal.valueOf(100);

  public BigDecimal getSum(BigDecimal... amountToSum) {
    return stream(amountToSum).reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  public BigDecimal getAverage(BigDecimal... amountToSum) {
    return rounded(
        getSum(amountToSum).divide(BigDecimal.valueOf(amountToSum.length), ROUNDING_MODE));
  }

  public BigDecimal getPercentage(BigDecimal amount, BigDecimal percentage) {
    BigDecimal result = amount.multiply(percentage);
    result = result.divide(HUNDRED, ROUNDING_MODE);
    return rounded(result);
  }

  public BigDecimal getAmountFromAmountInMinor(String amountInMinor) {
    if (StringUtils.isEmpty(amountInMinor)) return BigDecimal.ZERO;
    amountInMinor = amountInMinor.replace(",", "");
    return new BigDecimal(amountInMinor).divide(HUNDRED, 4, ROUNDING_MODE);
  }

  private String stripNonNumeric(String amount) {
    Pattern pattern = Pattern.compile("\\d+");
    Matcher matcher = pattern.matcher(amount);
    if (matcher.find()) {
      return matcher.group(0);
    } else {
      return "0";
    }
  }

  private BigDecimal rounded(BigDecimal number) {
    return number.setScale(2, ROUNDING_MODE);
  }

  public static boolean leftIsLessThanRight(BigDecimal left, BigDecimal right) {
    return left.compareTo(right) < 0;
  }

  public static boolean leftIsGreaterThanRight(BigDecimal left, BigDecimal right) {
    return left.compareTo(right) > 0;
  }

  public static boolean leftIsLessThanOrEqualT0Right(BigDecimal left, BigDecimal right) {
    return left.compareTo(right) <= 0;
  }

  public static boolean leftIsGreaterThanOrEqualRight(BigDecimal left, BigDecimal right) {
    return left.compareTo(right) >= 0;
  }

  public static boolean leftIsEqualToRight(BigDecimal left, BigDecimal right) {
    return left.compareTo(right) == 0;
  }

  public static boolean leftIsNotEqualToRight(BigDecimal left, BigDecimal right) {
    return !leftIsEqualToRight(left, right);
  }

  public static String formatWithCommas(BigDecimal amount) {
    return String.format(Locale.ENGLISH, "%,.2f", amount.setScale(2, RoundingMode.HALF_EVEN));
  }

  public static boolean leftIsFractionOfRight(BigDecimal left, BigDecimal right) {
    return right.remainder(left).compareTo(BigDecimal.ZERO) == 0;
  }

  public static Long convertMajorToMinor(BigDecimal amount) {
    return amount.multiply(BigDecimal.valueOf(100)).longValue();
  }

  public static String convertToMinor(String amount) {
    BigDecimal major = new BigDecimal(amount.replace(",", "")); // Remove commas
    return major
        .multiply(BigDecimal.valueOf(100))
        .toBigInteger()
        .toString(); // Convert to minor unit
  }

  public static String convertToMajor(String amount) {
    BigDecimal major = getAmountFromAmountInMinor(amount);
    return major.toString();
  }

  public static String convertToFormattedMajor(String amount) {
    amount = amount.toLowerCase();
    if (amount.contains("e")) {
      amount = formatAmount(amount);
    }
    BigDecimal major = getAmountFromAmountInMinor(amount);
    return formatWithCommas(major);
  }

  public static String formatAmount(String exponentialInput) {
    BigDecimal amount = new BigDecimal(exponentialInput);
    DecimalFormat formatter = new DecimalFormat("#,##0.00");
    return formatter.format(amount);
  }

  public static BigDecimal convertMinorToMajor(Long amount) {
    if (amount == null || amount == 0) {
      return BigDecimal.ZERO;
    }

    return BigDecimal.valueOf(amount)
        .divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_EVEN)
        .setScale(2, RoundingMode.HALF_EVEN);
  }

  public static BigDecimal getPercentageValueOf(BigDecimal value, BigDecimal percentage) {
    return value.multiply(percentage.divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_EVEN));
  }

  public static BigDecimal divide(BigDecimal dividend, BigDecimal divisor) {
    return dividend.divide(divisor, 4, RoundingMode.HALF_EVEN).setScale(2, RoundingMode.HALF_EVEN);
  }

  public static BigDecimal convertToTwoDecimal(BigDecimal value) {
    return value.setScale(2, RoundingMode.HALF_EVEN);
  }

  public static String maskAccountNumber(String str) {
    if (str == null || str.length() < 7) {
      return str;
    }

    String start = str.substring(0, 3);
    String end = str.substring(str.length() - 3);
    String middle = "*".repeat(str.length() - 6);

    return start + middle + end;
  }
}
