package com.digicore.omnexa.common.lib.api;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import java.time.ZonedDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Standard API request wrapper")
@Builder
public class ApiRequestWrapper<T> {

  @Schema(description = "Unique request identifier", example = "req_1234567890abcdef")
  private String requestId = "req_" + UUID.randomUUID().toString().replace("-", "");

  @Schema(description = "Request timestamp")
  private ZonedDateTime timestamp = ZonedDateTime.now();

  @Valid
  @Schema(description = "Request payload")
  private T data;
}
