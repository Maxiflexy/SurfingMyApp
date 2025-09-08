package com.digicore.omni.root.services.modules.common.authentication;

import com.digicore.common.util.ClientUtil;
import com.digicore.omni.data.lib.modules.common.dtos.UserDTO;
import com.digicore.omni.data.lib.modules.common.exception.CommonExceptionProcessor;
import com.digicore.omni.root.services.config.JwtHelper;
import com.digicore.omni.root.services.modules.common.utils.CommonUtils;
import com.digicore.omni.root.services.modules.merchants.authentication.apiModel.LoginResponseApiModel;
import com.digicore.request.processor.processors.TokenVaultProcessor;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Map;

/**
 * @author Oluwatobi Ogunwuyi
 * @createdOn Sep-04(Sun)-2022
 */

@Service
@RequiredArgsConstructor
public class TokenRefresher {

    private final JwtHelper jwtHelper;
    private final UserDetailsService userDetailsService;

    private final TokenVaultProcessor tokenVaultProcessor;





    public ResponseEntity<Object> getRefreshToken(Principal principal, String expiredToken)  {
        if (!jwtHelper.isTokenExpired(expiredToken.substring(7)))
            throw CommonExceptionProcessor.genError("previous token still active, can't generate refresh token");
        return getLoginResponseApiModel(principal);
    }

    public void revokeToken()  {
        tokenVaultProcessor.removeUserFromTokenVault(ClientUtil.getLoggedInUsername());
    }

    private ResponseEntity<Object> getLoginResponseApiModel(Principal principal)  {
        UserDTO userDetails;

            userDetails = (UserDTO) userDetailsService.loadUserByUsername(principal.getName());
            String token = RandomStringUtils.randomAlphanumeric(12);
            Map<String, String> claims = CommonUtils.getClaims(principal.getName(), userDetails);
            claims.put("client_mapper", token);
            tokenVaultProcessor.saveUserToTokenVault(principal.getName(),token);
            return CommonUtils.buildSuccessResponse(LoginResponseApiModel.builder()
                    .accessToken(jwtHelper.createJwtForClaims(principal.getName(), claims))
                    .build());

    }

    public ResponseEntity<Object> getRefreshTokenAfterDefaultPasswordIsUpdated(Principal principal)  {
        return getLoginResponseApiModel(principal);
    }
}
