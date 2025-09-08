/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.common.lib.transaction.converter;

import com.digicore.omnexa.common.lib.converter.StringEnumConverter;
import com.digicore.omnexa.common.lib.transaction.enums.Currency;
import jakarta.persistence.Converter;

/**
 * A JPA converter class for converting `Status` enums to their string representation and vice
 * versa. This class extends the `StringEnumConverter` to provide the conversion logic.
 *
 * <p>Usage:
 *
 * <ul>
 *   <li>Automatically converts `Status` enum values to strings when persisting to the database.
 *   <li>Automatically converts strings back to `Status` enum values when reading from the database.
 * </ul>
 *
 * <p>Annotations:
 *
 * <ul>
 *   <li>{@link Converter}: Marks this class as a JPA converter.
 * </ul>
 *
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jun-25(Wed)-2025
 */
@Converter
public class CurrencyConverter extends StringEnumConverter<Currency> {

  /** Default constructor that initializes the `StringEnumConverter` with the `Status` class. */
  public CurrencyConverter() {
    super(Currency.class);
  }
}
