/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.common.lib.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;

/**
 * Abstract JPA converter for handling sets of enum values. Converts a set of enums to a
 * comma-separated string for database storage and vice versa for entity attribute retrieval.
 *
 * <p>Usage:
 *
 * <ul>
 *   <li>Extend this class and provide the specific enum type as the generic parameter.
 *   <li>Automatically handles conversion between `Set<Enum>` and `String`.
 * </ul>
 *
 * <p>Annotations:
 *
 * <ul>
 *   <li>{@link Converter}: Marks this class as a JPA converter.
 * </ul>
 *
 * @param <X> The enum type to be converted.
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jun-25(Wed)-2025
 */
@Converter
public abstract class EnumListConverter<X extends Enum<X>>
    implements AttributeConverter<Set<X>, String> {

  private final Class<X> enumType;

  /**
   * Constructor to initialize the converter with the specific enum type.
   *
   * @param enumType The class of the enum type to be converted.
   */
  protected EnumListConverter(Class<X> enumType) {
    this.enumType = enumType;
  }

  /**
   * Converts a set of enums to a comma-separated string for database storage.
   *
   * @param list The set of enums to be converted.
   * @return A comma-separated string representation of the enum set, or an empty string if the set
   *     is null.
   */
  @Override
  public String convertToDatabaseColumn(Set<X> list) {
    return list != null
        ? list.stream().map(Enum::name).collect(Collectors.joining(","))
        : StringUtils.EMPTY;
  }

  /**
   * Converts a comma-separated string from the database to a set of enums.
   *
   * @param joined The comma-separated string representation of the enums.
   * @return A set of enums parsed from the string, or an empty set if the string is blank.
   */
  @Override
  public Set<X> convertToEntityAttribute(String joined) {
    return StringUtils.isNotBlank(joined)
        ? Arrays.stream(joined.split(","))
            .map(name -> Enum.valueOf(enumType, name))
            .collect(Collectors.toSet())
        : Collections.emptySet();
  }
}
