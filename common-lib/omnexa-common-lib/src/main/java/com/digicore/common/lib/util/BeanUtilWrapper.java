/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.common.lib.util;

import com.digicore.common.lib.exception.OmnexaException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.BeanUtils;

/**
 * Utility class for copying non-null properties between objects. Provides methods to identify null
 * fields in a source object and exclude them during property copying.
 *
 * <p>Methods:
 *
 * <ul>
 *   <li>{@link #copyNonNullProperties(Object, Object)}: Copies non-null properties from the source
 *       object to the target object.
 *   <li>{@link #getNullFields(Object)}: Retrieves the names of fields in the source object that are
 *       null.
 * </ul>
 *
 * <p>Dependencies:
 *
 * <ul>
 *   <li>{@link BeanUtils}: Spring utility class for bean property copying.
 *   <li>{@link OmnexaException}: Custom exception for handling errors during reflection.
 * </ul>
 *
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jun-25(Wed)-2025
 */
public class BeanUtilWrapper {

  /**
   * Copies non-null properties from the source object to the target object. Excludes fields that
   * are null in the source object during the copying process.
   *
   * @param source The source object from which properties are copied.
   * @param target The target object to which properties are copied.
   */
  public static void copyNonNullProperties(Object source, Object target) {
    String[] nullFields = getNullFields(source);
    BeanUtils.copyProperties(source, target, nullFields);
  }

  /**
   * Retrieves the names of fields in the source object that are null. Uses reflection to access and
   * evaluate the fields of the source object.
   *
   * @param source The source object whose fields are evaluated.
   * @return An array of field names that are null in the source object.
   * @throws OmnexaException If an error occurs during field access.
   */
  private static String[] getNullFields(Object source) {
    Field[] declaredFields = source.getClass().getDeclaredFields();
    List<String> emptyFieldNames =
        Arrays.stream(declaredFields)
            .filter(
                (field) -> {
                  field.setAccessible(true);

                  Object o;
                  try {
                    o = field.get(source);
                  } catch (IllegalAccessException var4) {
                    IllegalAccessException e = var4;
                    throw new OmnexaException(e.getMessage());
                  }

                  return Objects.isNull(o);
                })
            .map(Field::getName)
            .toList();
    String[] emptyFieldsArray = new String[emptyFieldNames.size()];
    return emptyFieldNames.toArray(emptyFieldsArray);
  }
}
