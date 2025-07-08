/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.common.lib.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.apache.commons.lang3.StringUtils;

/**
 * A JPA converter class for converting enum values to their string representation and vice versa.
 * This class is generic and can be used with any enum type.
 *
 * <p>Usage:
 *
 * <ul>
 *   <li>Automatically converts enum values to strings when persisting to the database.
 *   <li>Automatically converts strings back to enum values when reading from the database.
 * </ul>
 *
 * <p>Annotations:
 *
 * <ul>
 *   <li>{@link Converter}: Marks this class as a JPA converter.
 * </ul>
 *
 * @param <E> The enum type to be converted.
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jun-25(Wed)-2025
 */
@Converter
public class StringEnumConverter<E extends Enum<E>> implements AttributeConverter<E, String> {

  private final Class<E> enumType;

  /**
   * Constructor to initialize the converter with the specific enum type.
   *
   * @param enumType The class of the enum type to be converted.
   */
  protected StringEnumConverter(Class<E> enumType) {
    this.enumType = enumType;
  }

  /**
   * Converts an enum value to its string representation for database storage.
   *
   * @param attribute The enum value to be converted.
   * @return The string representation of the enum value, or null if the attribute is null.
   */
  @Override
  public String convertToDatabaseColumn(E attribute) {
    return attribute != null ? attribute.name() : null;
  }

  /**
   * Converts a string from the database to its corresponding enum value.
   *
   * @param dbData The string representation of the enum value.
   * @return The enum value corresponding to the string, or null if the string is blank.
   */
  @Override
  public E convertToEntityAttribute(String dbData) {
    return (StringUtils.isNotBlank(dbData) ? Enum.valueOf(this.enumType, dbData) : null);
  }
}
