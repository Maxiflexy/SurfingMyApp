/*
 * Created by Monsuru (31/8/2022)
 */

/*
 * Created by Monsuru (31/8/2022)
 */

package com.digicore.omni.root.services.modules.common.authentication;

import com.digicore.api.helper.response.ApiError;
import com.digicore.api.helper.response.ApiResponseJson;
import com.digicore.omni.data.lib.modules.common.apimodel.SignIn;
import com.digicore.omni.data.lib.modules.common.dtos.UserDTO;
import com.digicore.omni.root.lib.modules.common.services.LoginAttemptService;
import com.digicore.omni.root.services.config.JwtHelper;
import com.digicore.omni.root.services.modules.common.utils.CommonUtils;
import com.digicore.omni.root.services.modules.merchants.authentication.apiModel.LoginResponseApiModel;
import com.digicore.omni.root.services.modules.merchants.authentication.apiModel.LoginWith2faRequest;
import com.digicore.request.processor.enums.LogActivityType;
import com.digicore.request.processor.processors.AuditLogProcessor;
import com.digicore.request.processor.processors.TokenVaultProcessor;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: Monsuru <br/>
 * Created on 31/08/2022 00:18
 */

@Service
@RequiredArgsConstructor
public class LoginService {

    private final JwtHelper jwtHelper;
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final AuditLogProcessor auditLogProcessor;
    private final TokenVaultProcessor tokenVaultProcessor;
    private final LoginAttemptService loginAttemptService;
    @Value("${omni.root.authentication.max-attempt-count:3}")
    private String maxAttemptCountString;


    private static String getMapperToken() {
        return RandomStringUtils.randomAlphanumeric(12);
    }

    public ResponseEntity<Object> authenticate(SignIn signIn, String merchantId) {
        ApiResponseJson<?> apiResponseJson = (ApiResponseJson<?>) getUserDTO(signIn.getUsername()).getBody();
        assert apiResponseJson != null;
        UserDTO userDetails = (UserDTO) apiResponseJson.getData();


        if (passwordEncoder.matches(signIn.getPassword(), userDetails.getPassword())) {
            loginAttemptService.verifyLockStatus(userDetails.getUsername(), true);
            return getLoginResponseApiModel(signIn.getUsername(), userDetails,merchantId);
        }
        loginAttemptService.verifyLockStatus(userDetails.getUsername(), false);
        return CommonUtils.buildFailureResponse(List.of(new ApiError("Login attempt failed. Kindly retry","04")),HttpStatus.UNAUTHORIZED);
    }

    public ResponseEntity<Object> authenticate(LoginWith2faRequest loginWith2faRequest, String merchantId) {
        ResponseEntity<Object> response = getUserDTO(loginWith2faRequest.getUsername());
        ApiResponseJson<?> authResult = (ApiResponseJson<?>) response.getBody();
        if (!authResult.isSuccess())
            return response;
        UserDTO userDetails = (UserDTO) authResult.getData();
        //  if (tokenVaultProcessor.confirmToken(loginWith2faRequest.getLoginKey())) {
        return getLoginResponseApiModel(loginWith2faRequest.getUsername(), userDetails, merchantId);
        // }
        //  return CommonUtils.buildFailureResponse(List.of(new ApiError("invalid login key supplied","07")),HttpStatus.BAD_REQUEST);

    }

    private ResponseEntity<Object> getLoginResponseApiModel(String username, UserDTO userDetails, String merchantId) {
        String token = getMapperToken();
        Map<String, String> claims = CommonUtils.getClaims(username, userDetails);
        Map<String, Object> additionalInformation = new HashMap<>();

        additionalInformation.put("email", username);
        additionalInformation.put("client", userDetails.getEmail());
        claims.put("client_mapper", token);
        claims.put("email", userDetails.getEmail());
        claims.put("name", userDetails.getName());
        claims.put("role", userDetails.getRole());
        claims.put("merchantId", merchantId);

        if (userDetails.isDefaultPassword()) {
            additionalInformation.put("isDefaultPassword", userDetails.isDefaultPassword());
        }

        tokenVaultProcessor.saveUserToTokenVault(username, token);
        auditLogProcessor.saveAuditWithDescription(userDetails.getRole(), userDetails.getName(), userDetails.getEmail(), LogActivityType.LOGIN_ACTIVITY, userDetails.getName().concat(" ").concat("Successfully logged in"));

        return CommonUtils.buildSuccessResponse(LoginResponseApiModel.builder()
                .accessToken(jwtHelper.createJwtForClaims(username, claims))
                .additionalInformation(additionalInformation)
                .build(), null);

    }

    public ResponseEntity<Object> authenticateWithoutGeneratingAccessToken(SignIn signIn) {
        ApiResponseJson<?> apiResponseJson = (ApiResponseJson<?>) getUserDTO(signIn.getUsername()).getBody();
        UserDTO userDetails = (UserDTO) apiResponseJson.getData();

        if (userDetails != null && passwordEncoder.matches(signIn.getPassword(), userDetails.getPassword())) {
            Map<String, Object> additionalInformation = new HashMap<>();
            String token = getMapperToken();
            additionalInformation.put("LoginKey", token);
            tokenVaultProcessor.saveUserToTokenVault(signIn.getUsername(), token);
            return CommonUtils.buildSuccessResponse(LoginResponseApiModel.builder()
                    .accessToken("")
                    .additionalInformation(additionalInformation)
                    .build(), null);
        }
        return CommonUtils.buildFailureResponse(List.of(new ApiError("Login attempt failed. Kindly retry", "04")), HttpStatus.UNAUTHORIZED);

    }

    private ResponseEntity<Object> getUserDTO(String username) {
        UserDTO userDetails;
        try {
            userDetails = (UserDTO) userDetailsService.loadUserByUsername(username);
        } catch (UsernameNotFoundException e) {
            return CommonUtils.buildFailureResponse(List.of(new ApiError(String.format("no profile  found for %s", username), "")), HttpStatus.NOT_FOUND);
        }
        return CommonUtils.buildSuccessResponse(userDetails, null);
    }


}
