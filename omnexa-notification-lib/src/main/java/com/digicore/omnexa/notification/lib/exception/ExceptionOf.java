package com.digicore.omnexa.notification.lib.exception;

/**
 * @author mosh
 * @role software engineer
 * @createdOn 31 Thu Jul, 2025
 */
public class ExceptionOf {

  public static class Business {
    // 400xx
    public static class BadRequest {
      public static final ExceptionCode BAD_REQUEST =
          new ExceptionCode(
              "400000",
              "The request could not be completed due to malformed syntax. Please crosscheck and try again.");
    }

    // 401xx
    public static class Authorization {
      public static ExceptionCode UNAUTHORIZED =
          new ExceptionCode("401000", "Invalid authentication provided");
    }

    // 403xx
    public static class Forbidden {
      public static final ExceptionCode FORBIDDEN =
          new ExceptionCode(
              "403000", "You do not have sufficient permissions to access this resource");
    }

    // 404xx
    public static class NotFound {
      public static final ExceptionCode NOT_FOUND =
          new ExceptionCode("404000", "The requested resource was not found in the system");
    }

    // 409xx
    public static class Conflict {}
  }

  public static class System {

    // 429xx
    public static class RateLimit {
      public static ExceptionCode RATE_LIMIT_EXCEEDED =
          new ExceptionCode("429000", "Too many requests, please try again later.");
    }

    // 500xx
    public static class InternalError {
      public static ExceptionCode SERVER_ERROR =
          new ExceptionCode(
              "500000",
              "An unexpected error occurred while processing your request. Please try again later.");
    }
  }
}
