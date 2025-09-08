package com.digicore.omnexa.notification.lib.exception;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * @author mosh
 * @role software engineer
 * @createdOn 31 Thu Jul, 2025
 */
@Data
public class ExceptionCode {

  private String code;
  private String message;

  public ExceptionCode(String code, String message) {
    this.code = code;
    this.message = message;
  }

  public RequestException exception() {
    return new RequestException(this);
  }

  public RequestException exception(String message) {
    this.message = message;
    return new RequestException(this);
  }

  public RequestException exceptionWithPlaceholders(String... placeholders) {
    for (String placeholder : placeholders) {
      this.message = StringUtils.replaceOnce(this.message, "{}", placeholder);
    }
    return new RequestException(this);
  }

  public RequestException exception(Throwable cause) {
    this.message = cause.getMessage();
    return new RequestException(this, cause);
  }

  public RequestException exception(String message, Throwable cause) {
    this.message = message;
    return new RequestException(this, cause);
  }
}
