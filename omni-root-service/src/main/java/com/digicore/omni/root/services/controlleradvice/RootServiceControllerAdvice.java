/*
 * Created by Monsuru (7/8/2022)
 */
package com.digicore.omni.root.services.controlleradvice;

import com.digicore.api.helper.exception.ZeusRuntimeException;
import com.digicore.api.helper.response.ApiError;
import com.digicore.api.helper.response.ApiResponseJson;
import com.digicore.omni.root.services.modules.common.utils.CommonUtils;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.springframework.core.MethodParameter;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.digicore.api.helper.constant.StatusCode.FILE_LIMIT_EXCEEDED_CODE;
import static com.digicore.api.helper.constant.StatusCode.FILE_NOT_FOUND_CODE;

@ControllerAdvice

public class RootServiceControllerAdvice {
       // implements ResponseBodyAdvice<Object> {
    private static final Logger logger = Logger.getLogger(RootServiceControllerAdvice.class.getName());

    @ExceptionHandler({IOException.class})
    public ResponseEntity<Object> handleFileNotFoundException(IOException exception, WebRequest request) {
        logger.severe("an error occurred: " + exception);
        List<ApiError> apiErrors = new ArrayList<>();
        apiErrors.add(new ApiError("file not found",FILE_NOT_FOUND_CODE));
        ApiResponseJson<Object> responseJson =  ApiResponseJson.builder()
              .data(null)
              .errors(apiErrors)
              .message("file requested not found")
              .build();
        return new ResponseEntity<>(responseJson, HttpStatus.BAD_REQUEST);
    }



    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<Object> handleZeusRuntimeException(SQLIntegrityConstraintViolationException exception) {
        logger.severe("Constraint Error : " + exception);
        ApiError error = new ApiError("Seems some of the information supplied are not valid, Kindly contact support for help", "45");
        ApiResponseJson<String> responseJson = ApiResponseJson.<String>builder()
                .data(null)
                .errors(new ArrayList<>())
                .message("Seems some of the information supplied are not valid, Kindly contact support for help")
                .success(false)
                .build();
        responseJson.addError(error);

        return new ResponseEntity<>((responseJson), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ AuthenticationException.class })
    public ResponseEntity<Object> handleAuthenticationException(Exception ex) {


       // IncorrectResultSizeDataAccessException
        String message = "You are not authorized to make this request";

        if ( ex instanceof  InvalidBearerTokenException || ex instanceof BadCredentialsException){
             message = "The access token supplied is invalid or expired";
        }

        ApiError error = new ApiError(message, "99");
        ApiResponseJson<String> responseJson = ApiResponseJson.<String>builder()
                .data(null)
                .errors(new ArrayList<>())
                .message("This resource is protected")
                .success(false)
                .build();
        responseJson.addError(error);

        return new ResponseEntity<>((responseJson), HttpStatus.UNAUTHORIZED);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationException(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();
        List<ApiError> errors = new ArrayList<>();
        for (FieldError fieldError : fieldErrors) {
            ApiError error = new ApiError(fieldError.getDefaultMessage(), "90");
            errors.add(error);
        }





        return CommonUtils.buildFailureResponse(errors,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex) {
        return CommonUtils.buildFailureResponse(List.of(new ApiError(ex.getMessage(),"04")),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ZeusRuntimeException.class)
    public ResponseEntity<Object> handleZeusRuntimeException(ZeusRuntimeException exception) {
        logger.severe("an error occurred: " + exception.getErrors());
        return CommonUtils.buildFailureResponse(exception.getErrors(),exception.getHttpStatus());

    }



    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        logger.severe("an error occurred: " + ex);
        return CommonUtils.buildFailureResponse(List.of(new ApiError(ex.getMethod().concat(" is not supported"))),HttpStatus.METHOD_NOT_ALLOWED);

    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        logger.severe("an error occurred: " + ex);
        return CommonUtils.buildFailureResponse(List.of(new ApiError("The required payload is missing")),HttpStatus.BAD_REQUEST);

    }


    @ExceptionHandler(ClassNotFoundException.class)
    public ResponseEntity<Object> handleClassNotFoundException(ClassNotFoundException exception) {
        logger.severe("an error occurred: " + exception);


        return CommonUtils.buildFailureResponse(List.of(new ApiError("Kindly reach out to support for help","09")),HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<Object> handleDateTimeParseException(DateTimeParseException exception) {
        logger.severe("an error occurred: " + exception);


        return CommonUtils.buildFailureResponse(List.of(new ApiError(exception.getParsedString().concat(" is not in the valid date format"),"09")),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({FileSizeLimitExceededException.class})
    public ResponseEntity<Object> handleFieldExceededException(FileSizeLimitExceededException exception, WebRequest request) {
        logger.severe("an error occurred: " + exception);
        List<ApiError> apiErrors = new ArrayList<>();
        apiErrors.add(new ApiError(exception.getMessage(),FILE_LIMIT_EXCEEDED_CODE));
        ApiResponseJson<Object> responseJson =  ApiResponseJson.builder()

                .data(null)
                .errors(apiErrors)
                .message(exception.getMessage())
                .build();
        return new ResponseEntity<>(responseJson, HttpStatus.BAD_REQUEST);
    }



    public boolean supports(MethodParameter mp, Class<? extends HttpMessageConverter<?>> type) {
        return true;
    }

    public Object beforeBodyWrite(Object body,
                                  MethodParameter mp,
                                  MediaType mt,
                                  Class<? extends HttpMessageConverter<?>> type,
                                  ServerHttpRequest shr,
                                  ServerHttpResponse shr1) {
        return body instanceof ApiResponseJson || body instanceof Resource ? this.cleanXSSObjectFields(body) :
                  new ApiResponseJson("Request Successfully Treated", true, body, new ArrayList<>());
    }

    protected Object cleanXSSObjectFields(Object body) {
        if (body == null) {
            return null;
        } else {
            if (!body.getClass().getPackageName().contains("com.digicoreltd")) return body;
            Field[] fields = body.getClass().getDeclaredFields();
            Field[] var3 = fields;
            int var4 = fields.length;

            for (int var5 = 0; var5 < var4; ++var5) {
                Field f = var3[var5];
                f.setAccessible(true);

                try {
                    Object value = f.get(body);
                    if (value != null && value instanceof String && !((String) value).isEmpty()) {
                        Object cleanValue = this.cleanXSS((String) value);
                        f.set(body, cleanValue);
                    }
                } catch (IllegalAccessException var9) {
                    logger.log(Level.SEVERE, var9.getMessage(), var9);
                }
            }
            return body;
        }
    }

    private String cleanXSS(String value) {
        value = value.replaceAll("eval\\((.*)\\)", "");
        value = value.replaceAll("[\\\"\\'][\\s]*javascript:(.*)[\\\"\\']", "\"\"");
        value = value.replaceAll("(?i)<script.*?>.*?<script.*?>", "");
        value = value.replaceAll("(?i)<script.*?>.*?</script.*?>", "");
        value = value.replaceAll("(?i)<.*?javascript:.*?>.*?</.*?>", "");
        value = value.replaceAll("(?i)<.*?\\s+on.*?>.*?</.*?>", "");
        return value;
    }
}
