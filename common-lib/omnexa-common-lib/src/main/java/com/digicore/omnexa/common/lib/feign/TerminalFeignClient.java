package com.digicore.omnexa.common.lib.feign;

import static com.digicore.omnexa.common.lib.api.ApiVersion.API_V1;
import static com.digicore.omnexa.common.lib.swagger.constant.CommonEndpointSwaggerDoc.DETAIL_API;
import static com.digicore.omnexa.common.lib.swagger.constant.authentication.AuthenticationSwaggerDocConstant.AUTHENTICATION_API;
import static com.digicore.omnexa.common.lib.swagger.constant.terminal.TerminalSwaggerDoc.TERMINAL_API;

import com.digicore.omnexa.common.lib.api.ApiResponseJson;
import com.digicore.omnexa.common.lib.feign.config.FeignClientConfig;
import com.digicore.omnexa.common.lib.terminal.dto.request.TerminalLoginRequestDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Oluwatobi Ogunwuyi
 * @createdOn Aug-11(Mon)-2025
 */
@FeignClient(name = "omnexa-terminal-management", configuration = FeignClientConfig.class)
public interface TerminalFeignClient {

  @GetMapping(API_V1 + TERMINAL_API + DETAIL_API)
  ResponseEntity<ApiResponseJson<Object>> getTerminalDetails(@RequestParam String serialId);

  @PostMapping(API_V1 + AUTHENTICATION_API)
  ResponseEntity<ApiResponseJson<Object>> authenticateTerminal(
      @RequestBody TerminalLoginRequestDTO terminalLoginRequestDTO);
}
