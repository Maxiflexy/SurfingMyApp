/*
 * Created by Monsuru (7/8/2022)
 */
package com.digicore.omni.root.services.modules.backoffice.account.user.service;

import com.digicore.api.helper.response.ApiError;
import com.digicore.notification.lib.request.NotificationRequestType;
import com.digicore.notification.lib.request.NotificationServiceRequest;
import com.digicore.omni.data.lib.modules.backoffice.apimodel.BackOfficeUserApiModel;
import com.digicore.omni.data.lib.modules.backoffice.dto.BackOfficeUserDTO;
import com.digicore.omni.data.lib.modules.common.apimodel.PaginatedResponseApiModel;
import com.digicore.omni.data.lib.modules.common.apimodel.UserAccountApiModel;
import com.digicore.omni.data.lib.modules.common.enums.UserStatus;

import com.digicore.omni.root.lib.modules.backoffice.service.BackOfficeService;


import com.digicore.omni.root.services.modules.backoffice.account.admin.util.BackOfficerUserInvitationAssemblerModel;
import com.digicore.omni.root.services.modules.common.utils.CommonUtils;
import com.digicore.otp.service.NotificationDispatcher;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class BackOfficeUserService {

    private static final Logger log = Logger.getLogger(BackOfficeUserService.class.getName());

    @Value("${base.path}")
    private String baseUrl;

    @Value("${invite-user.path}")
    private String path;

    private final BackOfficeService backOfficeService;

    @Value("${omni.root.mail.invite-backoffice:Back Office User Invitation}")
    private String inviteSubject;

    @Value( "${omni.root.mail.merchant-account-disabled-update.subject:Account Disabled}")
    private String accountDisabledSubject;


    private final CommonUtils commonUtils;

    private final BackOfficerUserInvitationAssemblerModel invitationAssemblerModel;

    private final NotificationDispatcher notificationDispatcher;


    public PaginatedResponseApiModel<BackOfficeUserApiModel> getAllBackOfficeUsers(int page, int size) {
        return backOfficeService.getAllBackOfficeUsers(page, size);
    }

    public BackOfficeUserApiModel getBackOfficeUserById(String backOfficeUserId)  {
        return backOfficeService.getBackOfficeUserById(backOfficeUserId);
    }

    //todo temp pass

    @Transactional
    public ResponseEntity<Object> inviteBackOfficeUser(BackOfficeUserDTO backOfficeUserDTO) {
        if (backOfficeService.emailIsTaken(backOfficeUserDTO.getEmail()))
             return CommonUtils.buildFailureResponse(List.of(new ApiError("Invite had been sent to the email supplied already","03")),HttpStatus.BAD_REQUEST);
        String password = commonUtils.generateTemporaryPasswordAndCreateUserAccount(UserAccountApiModel.builder()
                .firstName(backOfficeUserDTO.getFirstName())
                .lastName(backOfficeUserDTO.getLastName())
                .email(backOfficeUserDTO.getEmail())
                .username(backOfficeUserDTO.getEmail())
                .role(backOfficeUserDTO.getRole())
                .permissions(backOfficeUserDTO.getPermissions())
                .build());

        backOfficeUserDTO.setActive(false);


        backOfficeService.saveUser(backOfficeUserDTO);

         commonUtils.sendInviteMail(NotificationServiceRequest.builder()
                .firstName(backOfficeUserDTO.getFirstName())
                .notificationSubject(inviteSubject)
                .userCode(password)
                        .recipients(List.of(backOfficeUserDTO.getEmail()))
                .notificationRequestType(NotificationRequestType.SEND_INVITE_FOR_BACKOFFICE_EMAIL)
                .userRole(backOfficeUserDTO.getRole()).build());

         return CommonUtils.buildSuccessResponse();
    }

    public BackOfficeUserApiModel toggleEnableDisableBackOfficeUser(String email)  {
        return backOfficeService.toggleEnableDisableBackOfficeUser(email);
    }

    public Map<String, UserStatus> toggleEnableDisableMerchantAndMerchantUser(String merchantId)  {
        Map<String, UserStatus> toggledEnableDisableMerchant = backOfficeService.toggleEnableDisableMerchant(merchantId);

        UserStatus merchantUserStatus = toggledEnableDisableMerchant.get("merchantStatusUpdated");

        if (merchantUserStatus == UserStatus.DISABLED) {
            sendMerchantDisabledUpdateEmail(merchantId);
        }

        return toggledEnableDisableMerchant;
    }


    public PaginatedResponseApiModel<BackOfficeUserApiModel> filterBackOfficeUsersByName(int page, int size, String backOfficeUserName) {
        return backOfficeService.filterBackOfficeUsersByName(page, size, backOfficeUserName);
    }

    private void sendMerchantDisabledUpdateEmail(String merchantId){
        notificationDispatcher.dispatchEmail(NotificationServiceRequest.builder()
                .firstName(backOfficeService.getMerchantProfile(merchantId).getFirstName())
                .notificationSubject(accountDisabledSubject)
                .recipients(List.of(backOfficeService.getMerchantProfile(merchantId).getEmail()))
                .notificationRequestType(NotificationRequestType.SEND_MERCHANT_DISABLED_UPDATE_EMAIL)
                .build());
    }
}
