package com.digicore.omnexa.common.lib.feign.config;

import com.digicore.omnexa.common.lib.api.ApiResponseJson;
import com.digicore.omnexa.common.lib.exception.OmnexaException;
import com.digicore.omnexa.common.lib.util.RequestUtil;
import feign.Response;
import feign.codec.ErrorDecoder;
import java.nio.charset.StandardCharsets;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

/**
 * @author Onyekachi Ejemba
 * @createdOn Aug-04(Mon)-2025
 */
@Configuration
public class FeignErrorDecoder implements ErrorDecoder {

  @Override
  public Exception decode(String methodKey, Response response) {
    String body = null;

    try {
      if (response.body() != null) {
        body = new String(response.body().asInputStream().readAllBytes(), StandardCharsets.UTF_8);
      }
    } catch (Exception e) {
      return new OmnexaException(
          "Unable to read error body", HttpStatus.valueOf(response.status()));
    }

    // Try to parse as JSON
    if (body != null && !body.isBlank()) {
      try {
        ApiResponseJson<?> apiResponseJson =
            RequestUtil.getObjectMapper().readValue(body, ApiResponseJson.class);
        return new OmnexaException(
            apiResponseJson.getMessage(),
            HttpStatus.valueOf(response.status()),
            apiResponseJson.getErrors());
      } catch (Exception e) {
        // Fallback: could not parse JSON
        return new OmnexaException(
            "Error from service: " + body, HttpStatus.valueOf(response.status()));
      }
    }

    // No body at all
    return new OmnexaException(
        "Service returned error with no body", HttpStatus.valueOf(response.status()));
  }
}
