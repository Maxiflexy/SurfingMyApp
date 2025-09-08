/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.common.lib.feign.config;

import feign.RequestTemplate;
import feign.codec.EncodeException;
import feign.codec.Encoder;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Oluwatobi Ogunwuyi
 * @createdOn Aug-12(Tue)-2025
 */
public class DynamicMultipartEncoder implements Encoder {

  private final Encoder delegate;

  public DynamicMultipartEncoder(Encoder delegate) {
    this.delegate = delegate;
  }

  @Override
  public void encode(Object object, Type bodyType, RequestTemplate template)
      throws EncodeException {
    if (isPojo(object)) {
      Map<String, Object> parts = new HashMap<>();
      flattenObject("", object, parts);
      delegate.encode(parts, MAP_STRING_WILDCARD, template);
    } else {
      delegate.encode(object, bodyType, template);
    }
  }

  private void flattenObject(String prefix, Object obj, Map<String, Object> parts) {
    if (obj == null) return;

    if (obj instanceof MultipartFile
        || obj instanceof String
        || obj instanceof Number
        || obj instanceof Boolean) {
      parts.put(prefix, obj);
      return;
    }

    Class<?> clazz = obj.getClass();
    if (clazz.isArray()) {
      int length = Array.getLength(obj);
      for (int i = 0; i < length; i++) {
        flattenObject(prefix + "[" + i + "]", Array.get(obj, i), parts);
      }
    } else if (Iterable.class.isAssignableFrom(clazz)) {
      int i = 0;
      for (Object item : (Iterable<?>) obj) {
        flattenObject(prefix + "[" + i + "]", item, parts);
        i++;
      }
    } else {
      for (Field field : clazz.getDeclaredFields()) {
        field.setAccessible(true);
        try {
          Object value = field.get(obj);
          String fieldName = prefix.isEmpty() ? field.getName() : prefix + "." + field.getName();
          flattenObject(fieldName, value, parts);
        } catch (IllegalAccessException e) {
          throw new EncodeException("Error encoding multipart data", e);
        }
      }
    }
  }

  private boolean isPojo(Object obj) {
    if (obj == null) return false;
    Class<?> clazz = obj.getClass();
    return !(clazz.isPrimitive()
        || obj instanceof String
        || obj instanceof Number
        || obj instanceof Boolean
        || obj instanceof MultipartFile
        || clazz.isArray()
        || Iterable.class.isAssignableFrom(clazz)
        || Map.class.isAssignableFrom(clazz));
  }

  private static final Type MAP_STRING_WILDCARD =
      new ParameterizedType() {
        public Type[] getActualTypeArguments() {
          return new Type[] {String.class, Object.class};
        }

        public Type getRawType() {
          return Map.class;
        }

        public Type getOwnerType() {
          return null;
        }
      };
}
