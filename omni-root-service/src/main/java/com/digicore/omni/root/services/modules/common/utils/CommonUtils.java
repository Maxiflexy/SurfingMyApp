package com.digicore.omni.root.services.modules.common.utils;

import com.digicore.api.helper.exception.ZeusRuntimeException;
import com.digicore.api.helper.response.ApiError;
import com.digicore.api.helper.response.ApiResponseJson;
import com.digicore.notification.lib.request.NotificationServiceRequest;
import com.digicore.omni.data.lib.modules.common.apimodel.UserAccountApiModel;
import com.digicore.omni.data.lib.modules.common.dtos.UserDTO;
import com.digicore.omni.root.lib.modules.common.services.UserAccountService;
import com.digicore.omni.root.services.config.RootServiceConfig;
import com.digicore.otp.service.NotificationDispatcher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author Oluwatobi Ogunwuyi
 * @createdOn Sep-04(Sun)-2022
 */

@Component
@RequiredArgsConstructor
@Slf4j
public final class CommonUtils {

    private final UserAccountService userAccountService;

    private final NotificationDispatcher notificationDispatcher;

    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    public static Map<String, String> getClaims(String username, UserDTO userDetails) {
        Map<String, String> claims = new HashMap<>();
        claims.put("username",username);

        String authorities = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));
        claims.put(RootServiceConfig.AUTHORITIES_CLAIM_NAME, authorities);
        return claims;
    }

    public  void sendInviteMail(NotificationServiceRequest notificationServiceRequest)  {
        notificationDispatcher.dispatchEmail(notificationServiceRequest);
    }

    public  ApiResponseJson<Object> sendReminderMail(NotificationServiceRequest notificationServiceRequest)  {
        notificationDispatcher.dispatchEmail(notificationServiceRequest);
        return ApiResponseJson.<Object>builder()
                .message("Reminder sent successfully")
                .success(true)
                .data(null)
                .errors(new ArrayList<>())
                .build();
    }

    public String generateTemporaryPasswordAndCreateUserAccount(UserAccountApiModel accountApiModel)  {
        if (userAccountService.emailIsTaken(accountApiModel.getEmail()))
            throw new ZeusRuntimeException("An Error Occurred", HttpStatus.BAD_REQUEST,List.of(new ApiError(String.format("the email %s is taken", accountApiModel.getEmail()),"08")));

        String password = RandomStringUtils.randomAlphanumeric(15);
        accountApiModel.setPassword(password);
        accountApiModel.setDefaultPassword(true);
        userAccountService.saveUser(accountApiModel, true);
        return password;
    }

    public static   ResponseEntity<Object> buildSuccessResponse(Object responseData, String message ){
        return ResponseEntity.ok(ApiResponseJson.builder()
                        .success(true)

                        .data(responseData)
                        .message(StringUtils.isBlank(message)?"Request Successfully Treated":message)
                        .errors(null)
                .build());

    }

    public static   ResponseEntity<Object> buildSuccessResponse(Object responseData ){
        return ResponseEntity.ok(ApiResponseJson.builder()
                .success(true)
                .data(responseData)
                .message("Request Successfully Treated")
                .errors(null)
                .build());

    }

    public static   ResponseEntity<Object> buildSuccessResponse(){
        return ResponseEntity.ok(ApiResponseJson.builder()
                .success(true)

                        .data(null)
                .message("Request Successfully Treated")
                .errors(null)
                .build());

    }



    public static ResponseEntity<Object> buildFailureResponse(List<ApiError> apiErrors, HttpStatus httpStatus ){
        String message = "";
        if (httpStatus.is4xxClientError()){
            message = "Kindly ensure you are passing the right information, check the errors field for what could be wrong with your request";
        }else if (httpStatus.is5xxServerError()){
            message = "Oops, sorry we were unable to process your request, reach out to support for help";
        }
        return new ResponseEntity<>(ApiResponseJson.builder()
                .success(false)
                .data(null)
                .message(message)
                .errors(apiErrors)
                .build(), httpStatus);

    }

    public static boolean isValidEmail(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
    }

}
