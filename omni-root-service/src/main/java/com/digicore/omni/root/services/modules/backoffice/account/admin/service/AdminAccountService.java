/*
 * Created by Monsuru (7/8/2022)
 */
package com.digicore.omni.root.services.modules.backoffice.account.admin.service;


import com.digicore.api.helper.response.ApiResponseJson;
import com.digicore.omni.data.lib.modules.common.apimodel.SignIn;
import com.digicore.omni.data.lib.modules.common.apimodel.UserAccountApiModel;
import com.digicore.omni.root.lib.modules.backoffice.service.BackOfficeService;

import com.digicore.omni.root.lib.modules.common.services.UserAccountService;
import com.digicore.omni.root.lib.modules.common.utils.AuthInformationUtils;
import com.digicore.omni.root.services.modules.common.authentication.LoginService;
import com.digicore.omni.root.services.modules.common.utils.CommonUtils;
import com.digicore.omni.root.services.modules.merchants.authentication.apiModel.LoginResponseApiModel;
import com.digicore.request.processor.enums.LogActivityType;
import com.digicore.request.processor.model.AuditLog;
import com.digicore.request.processor.processors.AuditLogProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AdminAccountService {

    private final BackOfficeService backOfficeService;

    private final LoginService loginService;

    private final UserAccountService userAccountService;

    private final AuditLogProcessor auditLogProcessor;

    private final AuthInformationUtils authInformationUtils;







    public ResponseEntity<Object> authenticateAdminUser(SignIn signIn)  {

        //you can always add extra info to the additional information
        ResponseEntity<Object> response = loginService.authenticate(signIn,null);
        ApiResponseJson<?> authResult = (ApiResponseJson<?>) response.getBody();
        if (!authResult.isSuccess())
            return response;
        LoginResponseApiModel responseApiModel = (LoginResponseApiModel) authResult.getData();
        Map<String, Object> extraInfo;
        extraInfo = responseApiModel.getAdditionalInformation();
        extraInfo.put("userName", signIn.getUsername());
        responseApiModel.setAdditionalInformation(extraInfo);
        return CommonUtils.buildSuccessResponse(responseApiModel,null);
    }


    public void deleteBackOfficeProfile(String email)  {
        UserAccountApiModel userAccount = userAccountService.fetchUserByEmail(email);
        backOfficeService.deleteBackOfficeProfile(email);
            AuditLog auditLog = authInformationUtils.getAuthenticatedUserInfo();
            auditLogProcessor.saveAuditWithDescription(auditLog.getRole(), auditLog.getUser(), auditLog.getEmail(), LogActivityType.DELETE_ACTIVITY,userAccount.getFirstName().concat(" ").concat(userAccount.getLastName().concat("has been deleted")) );

    }

    public void deleteMerchantProfile(String email)  {
        UserAccountApiModel userAccount = userAccountService.fetchUserByEmail(email);
        backOfficeService.deleteMerchantProfile(email);
        AuditLog auditLog = authInformationUtils.getAuthenticatedUserInfo();
        auditLogProcessor.saveAuditWithDescription(auditLog.getRole(), auditLog.getUser(), auditLog.getEmail(), LogActivityType.DELETE_ACTIVITY,userAccount.getFirstName().concat(" ").concat(userAccount.getLastName().concat("has been deleted")) );

    }
}
