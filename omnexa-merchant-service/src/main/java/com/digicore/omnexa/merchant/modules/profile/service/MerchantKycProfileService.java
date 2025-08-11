/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.merchant.modules.profile.service;

import static com.digicore.omnexa.common.lib.constant.message.MessageConstant.INVALID;
import static com.digicore.omnexa.common.lib.constant.message.MessageConstant.NOT_FOUND;
import static com.digicore.omnexa.common.lib.constant.system.SystemConstant.SYSTEM_DEFAULT_INVALID_REQUEST_ERROR;
import static com.digicore.omnexa.common.lib.constant.system.SystemConstant.SYSTEM_MERCHANT_ID_PLACEHOLDER;
import static com.digicore.omnexa.common.lib.util.RequestUtil.getValueFromAccessToken;
import static com.digicore.omnexa.common.lib.util.RequestUtil.nullOrEmpty;

import com.digicore.omnexa.common.lib.enums.KycStatus;
import com.digicore.omnexa.common.lib.exception.OmnexaException;
import com.digicore.omnexa.common.lib.profile.contract.ProfileService;
import com.digicore.omnexa.common.lib.profile.dto.request.ProfileEditRequest;
import com.digicore.omnexa.common.lib.profile.dto.response.ProfileEditResponse;
import com.digicore.omnexa.common.lib.profile.dto.response.ProfileInfoResponse;
import com.digicore.omnexa.common.lib.properties.MessagePropertyConfig;
import com.digicore.omnexa.common.lib.util.RequestUtil;
import com.digicore.omnexa.merchant.modules.profile.data.model.kyc.MerchantKycProfile;
import com.digicore.omnexa.merchant.modules.profile.data.repository.MerchantKycProfileRepository;
import com.digicore.omnexa.merchant.modules.profile.dto.response.MerchantKycProfileResponse;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

/**
 * @author Hossana Chukwunyere
 * @createdOn Aug-04(Mon)-2025
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MerchantKycProfileService implements ProfileService {
  private final MessagePropertyConfig messagePropertyConfig;
  private final MerchantKycProfileRepository merchantKycProfileRepository;

  @Override
  public ProfileEditResponse updateProfile(ProfileEditRequest request) {
    MerchantKycProfileResponse merchantKycProfileUpdateRequest =
        castToMerchantKycProfileUpdateRequest(request);
    MerchantKycProfile merchantKycProfile =
        merchantKycProfileRepository
            .findFirstByMerchantProfileMerchantId(
                RequestUtil.getValueFromAccessToken(SYSTEM_MERCHANT_ID_PLACEHOLDER))
            .orElseThrow(
                () ->
                    new OmnexaException(
                        messagePropertyConfig.getActivationMessage(NOT_FOUND),
                        HttpStatus.NOT_FOUND));

    if (!nullOrEmpty(merchantKycProfileUpdateRequest.getComment()))
      setComment(merchantKycProfileUpdateRequest.getComment(), merchantKycProfile);

    String activationData =
        RequestUtil.getGsonMapper()
            .toJson(
                merchantKycProfileUpdateRequest,
                new TypeToken<MerchantKycProfileResponse>() {}.getType());

    merchantKycProfile.setKycData(activationData);

    if (KycStatus.SUBMITTED.equals(merchantKycProfileUpdateRequest.getKycStatus())) {
      merchantKycProfile.setKycStatus(KycStatus.REVIEWING);
    } else merchantKycProfile.setKycStatus(KycStatus.PENDING);

    merchantKycProfileRepository.save(merchantKycProfile);
    return null;
  }


  @Override
  public ProfileEditResponse updateProfile(ProfileEditRequest request, String profileId) {
    MerchantKycProfileResponse merchantKycProfileUpdateRequest =
            castToMerchantKycProfileUpdateRequest(request);
    MerchantKycProfile merchantKycProfile =
            merchantKycProfileRepository
                    .findFirstByMerchantProfileMerchantId(profileId)
                    .orElseThrow(
                            () ->
                                    new OmnexaException(
                                            messagePropertyConfig.getActivationMessage(NOT_FOUND),
                                            HttpStatus.NOT_FOUND));

    if (!nullOrEmpty(merchantKycProfileUpdateRequest.getComment()))
      setComment(merchantKycProfileUpdateRequest.getComment(), merchantKycProfile);

    String activationData =
            RequestUtil.getGsonMapper()
                    .toJson(
                            merchantKycProfileUpdateRequest,
                            new TypeToken<MerchantKycProfileResponse>() {}.getType());

    merchantKycProfile.setKycData(activationData);

    if (KycStatus.SUBMITTED.equals(merchantKycProfileUpdateRequest.getKycStatus())) {
      merchantKycProfile.setKycStatus(KycStatus.REVIEWING);
    } else merchantKycProfile.setKycStatus(KycStatus.PENDING);

    merchantKycProfileRepository.save(merchantKycProfile);
    return null;
  }

  @Override
  public ProfileInfoResponse getProfileById(String profileId) {
    MerchantKycProfile merchantKycProfile =
        merchantKycProfileRepository
            .findFirstByMerchantProfileMerchantId(profileId)
            .orElseThrow(
                () ->
                    new OmnexaException(
                        messagePropertyConfig.getActivationMessage(NOT_FOUND),
                        HttpStatus.NOT_FOUND));

    String kycData = merchantKycProfile.getKycData();

    MerchantKycProfileResponse merchantKycProfileResponse =
        RequestUtil.getGsonMapper().fromJson(kycData, MerchantKycProfileResponse.class);

    retrieveComments(merchantKycProfile, merchantKycProfileResponse);

    return merchantKycProfileResponse;
  }

  void retrieveComments(
      MerchantKycProfile merchantKycProfile,
      MerchantKycProfileResponse merchantKycProfileResponse) {
    if (!nullOrEmpty(merchantKycProfile.getMerchantKycComment())) {
      List<MerchantKycProfileResponse.MerchantKycCommentDTO> comments =
          RequestUtil.getGsonMapper()
              .fromJson(
                  merchantKycProfile.getMerchantKycComment(),
                  new TypeToken<
                      List<MerchantKycProfileResponse.MerchantKycCommentDTO>>() {}.getType());
      merchantKycProfileResponse.setComments(comments);
    }
  }

  private static void setComment(String comment, MerchantKycProfile merchantKycProfile) {
    List<MerchantKycProfileResponse.MerchantKycCommentDTO> comments =
        RequestUtil.getGsonMapper()
            .fromJson(
                merchantKycProfile.getMerchantKycComment(),
                new TypeToken<
                    List<MerchantKycProfileResponse.MerchantKycCommentDTO>>() {}.getType());
    if (nullOrEmpty(comments)) comments = new ArrayList<>();
    if (!nullOrEmpty(comment)) {
      MerchantKycProfileResponse.MerchantKycCommentDTO commentDTO =
          new MerchantKycProfileResponse.MerchantKycCommentDTO();
      commentDTO.setComment(comment);
      commentDTO.setCommenter(getValueFromAccessToken("name"));

      comments.add(commentDTO);
      merchantKycProfile.setMerchantKycComment(RequestUtil.getGsonMapper().toJson(comments));
    }
  }

  private MerchantKycProfileResponse castToMerchantKycProfileUpdateRequest(
      ProfileEditRequest profile) {
    if (!(profile instanceof MerchantKycProfileResponse)) {
      throw new OmnexaException(
          messagePropertyConfig.getOnboardMessage(INVALID, SYSTEM_DEFAULT_INVALID_REQUEST_ERROR),
          HttpStatus.BAD_REQUEST);
    }
    return (MerchantKycProfileResponse) profile;
  }
}
