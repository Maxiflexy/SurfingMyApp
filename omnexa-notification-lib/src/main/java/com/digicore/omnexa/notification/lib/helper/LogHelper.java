package com.digicore.omnexa.notification.lib.helper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * @author mosh
 * @role software engineer
 * @createdOn 01 Fri Aug, 2025
 */
@UtilityClass
@Slf4j
public class LogHelper {

  private static final ObjectMapper OBJECT_MAPPER;

  private static final Set<String> SENSITIVE_KEYS =
      Set.of("password", "pin", "token", "secret", "otp", "headers", "topic", "tokens");

  private static final String ANSI_RESET = "\u001B[0m";
  private static final String ANSI_GREEN = "\u001B[32m";
  private static final String ANSI_CYAN = "\u001B[36m";
  private static final String ANSI_YELLOW = "\u001B[33m";

  public static final ExecutorService LOG_EXECUTOR =
      new ThreadPoolExecutor(
          4, // corePoolSize (always kept alive)
          16, // maxPoolSize (burst capacity)
          60L,
          TimeUnit.SECONDS, // idle thread keep-alive time
          new ArrayBlockingQueue<>(1000), // work queue size
          new ThreadPoolExecutor.CallerRunsPolicy() // fallback if full
          );

  static {
    OBJECT_MAPPER =
        new ObjectMapper()
            .registerModules(new JavaTimeModule())
            .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
  }

  public <T> void logObject(T obj, String traceId) {
    LOG_EXECUTOR.submit(
        () -> {
          if (obj == null) {
            log.warn(ANSI_YELLOW + "Attempted to log null object." + ANSI_RESET);
            return;
          }

          try {
            Map<String, Object> map = convertToMap(obj);
            map.put("omnexaTraceId", traceId);
            sanitizeMap(map);
            log.info(
                ANSI_GREEN + "{}" + ANSI_RESET + ": " + ANSI_CYAN + "{}" + ANSI_RESET,
                obj.getClass().getSimpleName(),
                OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(map));
          } catch (Exception e) {
            log.error(ANSI_YELLOW + "Failed to log object: {}" + ANSI_RESET, obj);
          }
        });
  }

  private <T> Map<String, Object> convertToMap(T obj) throws IOException {
    if (obj instanceof String str && isJsonObject(str)) {
      return OBJECT_MAPPER.readValue(str, new TypeReference<>() {});
    }
    return OBJECT_MAPPER.convertValue(obj, new TypeReference<>() {});
  }

  private boolean isJsonObject(String str) {
    String trimmed = str.trim();
    return trimmed.startsWith("{") && trimmed.endsWith("}");
  }

  private void sanitizeMap(Map<String, Object> map) {
    if (isNullOrEmpty(map)) return;

    for (Map.Entry<String, Object> entry : map.entrySet()) {
      processEntry(entry);
    }
  }

  @SneakyThrows
  private void processEntry(Map.Entry<String, Object> entry) {
    String key = entry.getKey();
    Object value = entry.getValue();

    if (value instanceof Map<?, ?> nestedMap) {
      sanitizeMap(castToMap(nestedMap));
    } else if (value instanceof List<?> list) {
      sanitizeList(list);
    }

    try {
      if (value instanceof String strVal && isJsonObject(strVal)) {
        Map<String, Object> parsedMap =
            OBJECT_MAPPER.readValue(strVal, new TypeReference<Map<String, Object>>() {});
        sanitizeMap(parsedMap);
        entry.setValue(parsedMap);
      } else if (value instanceof Map) {
        @SuppressWarnings("unchecked")
        Map<String, Object> innerMap = (Map<String, Object>) value;
        sanitizeMap(innerMap);
      }
      // Handle Lists of maps or JSON strings if needed
    } catch (Exception e) {
      log.warn(ANSI_YELLOW + "Could not process entry: {}" + ANSI_RESET, key, e);
    }

    if (shouldSanitize(key, value)) {
      entry.setValue("***");
    }
  }

  private boolean shouldSanitize(String key, Object value) {
    return SENSITIVE_KEYS.contains(key.toLowerCase())
        && value != null
        && (!(value instanceof String s) || !s.isBlank())
        && (!(value instanceof List<?> l) || !l.isEmpty())
        && (!(value instanceof Map<?, ?> m) || !m.isEmpty());
  }

  @SuppressWarnings("unchecked")
  private Map<String, Object> castToMap(Map<?, ?> map) {
    if (map == null) return Collections.emptyMap();
    return (Map<String, Object>) map;
  }

  private boolean isNullOrEmpty(Map<?, ?> map) {
    return map == null || map.isEmpty();
  }

  @SuppressWarnings("unchecked")
  private void sanitizeList(List<?> list) {
    if (list == null || list.isEmpty()) return;

    for (Object item : list) {
      if (item instanceof Map<?, ?> nestedMap) {
        sanitizeMap((Map<String, Object>) nestedMap);
      } else if (item instanceof List<?> nestedList) {
        sanitizeList(nestedList);
      }
    }
  }
}
