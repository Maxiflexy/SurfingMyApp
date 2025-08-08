package com.digicore.omnexa.common.lib.feign.config;

import com.digicore.omnexa.common.lib.api.ApiResponseJson;
import com.digicore.omnexa.common.lib.exception.OmnexaException;
import com.digicore.omnexa.common.lib.util.RequestUtil;
import feign.Response;
import feign.codec.ErrorDecoder;
import java.io.IOException;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

/**
 * @author Onyekachi Ejemba
 * @createdOn Aug-04(Mon)-2025
 */
@Configuration
public class FeignErrorDecoder implements ErrorDecoder {

  @Override
  public Exception decode(String s, Response response) {
    try {
      ApiResponseJson<?> apiResponseJson =
          RequestUtil.getObjectMapper()
              .readValue(response.body().asInputStream(), ApiResponseJson.class);
      return new OmnexaException(
          apiResponseJson.getMessage(),
          HttpStatus.valueOf(response.status()),
          apiResponseJson.getErrors());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
