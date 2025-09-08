package com.digicore.omni.root.services.modules.merchants.submerchant.facade;

import com.digicore.common.util.BeanUtilWrapper;
import com.digicore.omnexa.notification.lib.contract.NotificationRequestType;
import com.digicore.omnexa.notification.lib.contract.email.model.EmailRequest;
import com.digicore.omnexa.notification.lib.service.PluggableEmailService;
import com.digicore.omni.data.lib.modules.merchant.dto.MerchantBasicInformationDTO;
import com.digicore.omni.data.lib.modules.merchant.enums.MerchantStatus;
import com.digicore.omni.data.lib.modules.merchant.model.SubMerchantInvite;
import com.digicore.omni.root.lib.modules.merchant.service.MerchantService;
import com.digicore.omni.root.services.modules.merchants.onboarding.services.MerchantOnboardingService;
import com.digicore.omni.root.services.modules.merchants.submerchant.mapper.SubMerchantMapper;
import com.digicore.omni.root.services.modules.merchants.submerchant.model.dto.SubMerchantRegistrationDTO;
import com.digicore.omni.root.services.modules.merchants.submerchant.model.request.SubMerchantInviteRequest;
import com.digicore.omni.root.services.modules.merchants.submerchant.model.request.SubMerchantOnboardRequest;
import com.digicore.omni.root.services.modules.merchants.submerchant.model.response.SubMerchantInviteResponse;
import com.digicore.omni.root.services.modules.merchants.submerchant.model.response.SubMerchantListResponse;
import com.digicore.omni.root.services.modules.merchants.submerchant.model.response.SubMerchantResponse;
import com.digicore.omni.root.services.modules.merchants.submerchant.service.SubMerchantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author mosh
 * @role software engineer
 * @createdOn 13 Wed Aug, 2025
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class SubMerchantFacade {

 private final SubMerchantService subMerchantService;
 private final SubMerchantMapper subMerchantMapper;
 private final PluggableEmailService pluggableEmailService;
 private final MerchantOnboardingService merchantOnboardingService;
 private final MerchantService merchantService;

 public static final String VERIFICATION_LINK = "verificationLink";
 public static final String SEND_SUB_MERCHANT_INVITE_EMAIL_TEMPLATE = "SEND_SUB_MERCHANT_INVITE_EMAIL_TEMPLATE";
 public static final String BUSINESS_NAME = "businessName";
 public static final String SEND_SUB_MERCHANT_INVITE = "Merchant Invite";

 public SubMerchantInviteResponse subMerchantInvite(SubMerchantInviteRequest request){
  SubMerchantInvite subMerchantInvite = subMerchantService.inviteSubMerchant(request);

  pluggableEmailService.getEngine(pluggableEmailService.getNotificationPropConfig().getEmailChannelType()).
          sendEmailAsync(buildSubMerchantInviteMail(subMerchantInvite));

  return SubMerchantInviteResponse.builder().businessName(subMerchantInvite.getBusinessName()).build();
 }


 @Transactional
 public ResponseEntity<Object> registerSubMerchant(SubMerchantOnboardRequest request) {
  SubMerchantInvite subMerchantUser = subMerchantService.getMerchantInviteByEmailAndCodeAndStatusPending(request.getEmail(), request.getVerificationCode());
  subMerchantUser.setStatus("ACTIVE");
  subMerchantService.saveSubMerchantInvite(subMerchantUser);

  MerchantBasicInformationDTO merchantBasicInformationDTO = new MerchantBasicInformationDTO();
  merchantBasicInformationDTO.setBusinessName(subMerchantUser.getBusinessName());
  merchantBasicInformationDTO.setPhoneNumber(request.getPhoneNumber());
  merchantBasicInformationDTO.setEmail(subMerchantUser.getEmail());
  merchantOnboardingService.validateMerchantCreationRequest(merchantBasicInformationDTO);

  SubMerchantRegistrationDTO subMerchantRegistrationDTO = new SubMerchantRegistrationDTO();
  BeanUtilWrapper.copyNonNullProperties(request, subMerchantRegistrationDTO);

  subMerchantRegistrationDTO.setBusinessName(subMerchantUser.getBusinessName());
  subMerchantRegistrationDTO.setParentMerchantId(subMerchantUser.getMerchantProfile().getMerchantId());
  return merchantOnboardingService.createSubMerchantAccountAndProfile(subMerchantRegistrationDTO);
 }

 private EmailRequest buildSubMerchantInviteMail(SubMerchantInvite subMerchantInvite) {
  Map<String, Object> placeHolders = new HashMap<>();
  placeHolders.put("username", subMerchantInvite.getBusinessName());
  placeHolders.put("firstName", pluggableEmailService.getNotificationPropConfig().getServiceBaseUrl().concat("?email="
          .concat(subMerchantInvite.getEmail()).concat("&verificationCode=".concat(subMerchantInvite.getInviteCode()))));
  return EmailRequest.builder()
          .useTemplate(true)
          .sender(pluggableEmailService.getNotificationPropConfig().getSender(NotificationRequestType.SEND_SUB_MERCHANT_INVITE_EMAIL, null))
          .subject(pluggableEmailService.getNotificationPropConfig().getSubject(NotificationRequestType.SEND_SUB_MERCHANT_INVITE_EMAIL, SEND_SUB_MERCHANT_INVITE))
          .placeHolders(placeHolders)
          .recipients(Set.of(subMerchantInvite.getEmail()))
          .templateName(pluggableEmailService.getNotificationPropConfig().getTemplate(NotificationRequestType.SEND_SUB_MERCHANT_INVITE_EMAIL, SEND_SUB_MERCHANT_INVITE_EMAIL_TEMPLATE))
          .build();
 }

}
