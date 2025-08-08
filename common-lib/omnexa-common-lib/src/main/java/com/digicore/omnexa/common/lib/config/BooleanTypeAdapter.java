/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.common.lib.config;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;

/*
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jan-27(Mon)-2025
 */

public class BooleanTypeAdapter implements JsonDeserializer<Boolean> {
  @Override
  public Boolean deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
      throws JsonParseException {
    if (json.isJsonPrimitive() && json.getAsJsonPrimitive().isBoolean()) {
      return json.getAsBoolean();
    } else {
      // Handle the case when the value is not a boolean in the JSON data
      return false; // or throw an exception, or set a custom default value
    }
  }
}
