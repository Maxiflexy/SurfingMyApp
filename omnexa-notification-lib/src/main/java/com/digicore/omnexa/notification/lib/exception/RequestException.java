package com.digicore.omnexa.notification.lib.exception;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author mosh
 * @role software engineer
 * @createdOn 31 Thu Jul, 2025
 */
@RequiredArgsConstructor
@Component
@Data
public class RequestException extends RuntimeException {

  private String code;
  private String actualMessage;

  public RequestException(ExceptionCode exceptionCode) {
    super(
        exceptionCode.getCode().startsWith("5")
            ? ExceptionOf.System.InternalError.SERVER_ERROR.getMessage()
            : exceptionCode.getMessage());

    this.code = exceptionCode.getCode();
    this.actualMessage = exceptionCode.getMessage();
  }

  public RequestException(ExceptionCode exceptionCode, Throwable cause) {
    super(
        exceptionCode.getCode().startsWith("5")
            ? ExceptionOf.System.InternalError.SERVER_ERROR.getMessage()
            : exceptionCode.getMessage(),
        cause);

    this.code = exceptionCode.getCode();
    this.actualMessage = exceptionCode.getMessage();
  }
}
