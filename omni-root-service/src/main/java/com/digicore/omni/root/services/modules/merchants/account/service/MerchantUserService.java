package com.digicore.omni.root.services.modules.merchants.account.service;


import com.digicore.api.helper.exception.ZeusRuntimeException;
import com.digicore.api.helper.response.ApiError;
import com.digicore.common.util.ClientUtil;
import com.digicore.notification.lib.request.NotificationRequestType;
import com.digicore.notification.lib.request.NotificationServiceRequest;
import com.digicore.notification.lib.request.TransactionNotificationRequest;
import com.digicore.omni.data.lib.modules.common.apimodel.PaginatedResponseApiModel;
import com.digicore.omni.data.lib.modules.common.apimodel.UserAccountApiModel;
import com.digicore.omni.data.lib.modules.common.permission.dtos.RoleApiModel;
import com.digicore.omni.data.lib.modules.merchant.apimodel.MerchantProfileApiModel;
import com.digicore.omni.data.lib.modules.merchant.apimodel.MerchantUserApiModel;
import com.digicore.omni.data.lib.modules.merchant.dto.MerchantUserDTO;
import com.digicore.omni.data.lib.modules.merchant.model.MerchantProfile;


import com.digicore.omni.data.lib.modules.merchant.util.MerchantServiceUtils;
import com.digicore.omni.root.lib.modules.common.services.UserAccountService;


import com.digicore.omni.root.lib.modules.merchant.service.MerchantService;
import com.digicore.omni.root.lib.modules.merchant.service.MerchantUserAccountService;
import com.digicore.omni.root.services.modules.common.utils.CommonUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MerchantUserService {
    private final MerchantService merchantService;

    private final CommonUtils commonUtils;

    private final UserAccountService loggedInUserAccountService;

    private final MerchantServiceUtils merchantServiceUtils;

    @Value("${omni.root.mail.backoffice-user: Invitation to join zest backoffice}")
    private String inviteSubject;
    @Value("${omni.root.mail.backoffice-sender: You just onboarded a user}")
    private String inviteSenderSubject;

    private final MerchantUserAccountService userAccountService;



    //todo temp pass
    @Transactional
    public ResponseEntity<Object> inviteUser(MerchantUserDTO merchantUserDTO) {
        if (userAccountService.emailIsTaken(merchantUserDTO.getEmail()))
            throw new ZeusRuntimeException("An Error Occurred", HttpStatus.BAD_REQUEST,List.of(new ApiError("User has been invited already","08")));

        String password = commonUtils.generateTemporaryPasswordAndCreateUserAccount(UserAccountApiModel.builder()
                .firstName(merchantUserDTO.getFirstName())
                .lastName(merchantUserDTO.getLastName())
                .email(merchantUserDTO.getEmail())
                .username(merchantUserDTO.getEmail())
                .role(merchantUserDTO.getRole())
                .permissions(merchantUserDTO.getPermissions())
                .build());

        merchantUserDTO.setActive(false);


        userAccountService.saveMerchantUser(merchantUserDTO);

        MerchantProfile merchantProfile = merchantServiceUtils.fetchMerchantProfileByUserEmail(merchantUserDTO.getEmail());

        commonUtils.sendInviteMail(NotificationServiceRequest.builder()
                .firstName(merchantUserDTO.getFirstName())
                .notificationSubject(inviteSubject)
                .recipients(List.of(merchantUserDTO.getEmail()))
                .userCode(password)
                .notificationRequestType(NotificationRequestType.SEND_INVITE_FOR_MERCHANT_EMAIL)
                .userRole(merchantUserDTO.getRole())
                .build());


         commonUtils.sendInviteMail(NotificationServiceRequest.builder()
                .firstName(merchantProfile.getFirstName())
                .notificationSubject(inviteSenderSubject)
                .transactionNotificationRequest(TransactionNotificationRequest.builder()
                        .merchantUserName(merchantUserDTO.getFirstName().concat(" ").concat(merchantUserDTO.getLastName()))
                        .merchantName(merchantProfile.getMerchantBusinessDetails().getBusinessName())
                        .build())
                .userRole(merchantUserDTO.getRole())
                .recipients(List.of(merchantProfile.getEmail(), ClientUtil.getLoggedInUsername()))
                .notificationRequestType(NotificationRequestType.SEND_USER_INVITE_NOTICE_TO_MERCHANT_EMAIL)
                .build());

         return CommonUtils.buildSuccessResponse();
    }


    public MerchantProfileApiModel toggleLiveMode()  {
        return merchantService.toggleLiveMode();
    }


    public PaginatedResponseApiModel<MerchantUserApiModel> getAllMerchantUsers(int page, int size) {
        return userAccountService.fetchAllUserByMerchant(page, size);
    }

    public PaginatedResponseApiModel<MerchantUserApiModel> searchMerchantAdminByName(int page, int size, String name) {
        return userAccountService.searchMerchantUserByName(page, size, name);
    }

    public MerchantUserApiModel getMerchantUsersDetail(String email) {
        return userAccountService.fetchUserDetailsByEmail(email);
    }

    public List<RoleApiModel> getAvailableRoles() {
        return userAccountService.getAvailableMerchantRoles();
    }


    public MerchantUserApiModel toggleEnableDisableMerchantUser(String email){
        return merchantService.toggleEnableDisableMerchantUser(email);
    }
}
