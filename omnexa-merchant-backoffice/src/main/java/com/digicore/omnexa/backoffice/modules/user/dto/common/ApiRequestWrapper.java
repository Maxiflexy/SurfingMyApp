package com.digicore.omnexa.backoffice.modules.user.dto.common;//package com.digicore.omnexa.backoffice.modules.user.dto.common;
//
//import io.swagger.v3.oas.annotations.media.Schema;
//import jakarta.validation.Valid;
//import lombok.Getter;
//import lombok.Setter;
//
//import java.time.ZonedDateTime;
//import java.util.UUID;
//
//@Getter
//@Setter
//@Schema(description = "Standard API request wrapper")
//public class ApiRequestWrapper<T> {
//
//    @Schema(description = "Unique request identifier", example = "req_1234567890abcdef")
//    private String requestId = "req_" + UUID.randomUUID().toString().replace("-", "");
//
//    @Schema(description = "Request timestamp")
//    private ZonedDateTime timestamp = ZonedDateTime.now();
//
//    @Valid
//    @Schema(description = "Request payload")
//    private T data;
//}