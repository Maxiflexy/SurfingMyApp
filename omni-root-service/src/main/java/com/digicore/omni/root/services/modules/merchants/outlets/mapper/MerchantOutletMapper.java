package com.digicore.omni.root.services.modules.merchants.outlets.mapper;

import com.digicore.omni.data.lib.modules.merchant.enums.OutletStatus;
import com.digicore.omni.data.lib.modules.merchant.model.MerchantOutlet;
import com.digicore.omni.data.lib.modules.merchant.model.MerchantProfile;
import com.digicore.omni.data.lib.modules.merchant.projection.MerchantOutletProjection;
import com.digicore.omni.root.services.modules.merchants.outlets.model.request.CreateOutletRequest;
import com.digicore.omni.root.services.modules.merchants.outlets.model.request.UpdateOutletRequest;
import com.digicore.omni.root.services.modules.merchants.outlets.model.response.MerchantOutletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * @author mosh
 * @role software engineer
 * @createdOn 14 Thu Aug, 2025
 */

@Component
public class MerchantOutletMapper {

 public MerchantOutlet toMerchantOutlet(CreateOutletRequest request, MerchantProfile merchantProfile){
  MerchantOutlet merchantOutlet = new MerchantOutlet();
  merchantOutlet.setTitle(request.getTitle());
  merchantOutlet.setCountry(request.getCountry());
  merchantOutlet.setState(request.getState());
  merchantOutlet.setLga(request.getLga());
  merchantOutlet.setAddress(request.getAddress());
  merchantOutlet.setStatus(OutletStatus.ACTIVE.name());
  merchantOutlet.setMerchantProfile(merchantProfile);
  merchantOutlet.setMerchantProfileId(merchantProfile.getId());

  return merchantOutlet;
 }

 public MerchantOutletResponse toMerchantOutletResponse(MerchantOutlet merchantOutlet){

  return MerchantOutletResponse.builder()
          .id(merchantOutlet.getId())
          .title(merchantOutlet.getTitle())
          .lga(merchantOutlet.getLga())
          .country(merchantOutlet.getCountry())
          .state(merchantOutlet.getState())
          .address(merchantOutlet.getAddress())
          .status(merchantOutlet.getStatus())
          .createdDate(merchantOutlet.getCreatedDate())
          .build();
 }

 public MerchantOutlet toMerchantOutlet(UpdateOutletRequest request, MerchantOutlet merchantOutlet){
  merchantOutlet.setTitle(StringUtils.isBlank(request.getTitle())? merchantOutlet.getTitle() : request.getTitle());
  merchantOutlet.setCountry(StringUtils.isBlank(request.getCountry())? merchantOutlet.getCountry() : request.getCountry());
  merchantOutlet.setState(StringUtils.isBlank(request.getState())? merchantOutlet.getState() : request.getState());
  merchantOutlet.setLga(StringUtils.isBlank(request.getLga())? merchantOutlet.getLga() : request.getLga());
  merchantOutlet.setAddress(StringUtils.isBlank(request.getAddress())? merchantOutlet.getAddress() : request.getAddress());
  merchantOutlet.setStatus(OutletStatus.ACTIVE.name());

  return merchantOutlet;
 }

 public MerchantOutletResponse toMerchantOutletResponse(MerchantOutletProjection merchantOutletProjection){

  return MerchantOutletResponse.builder()
          .id(merchantOutletProjection.getId())
          .title(merchantOutletProjection.getTitle())
          .lga(merchantOutletProjection.getLga())
          .country(merchantOutletProjection.getCountry())
          .state(merchantOutletProjection.getState())
          .address(merchantOutletProjection.getAddress())
          .status(merchantOutletProjection.getStatus())
          .createdDate(merchantOutletProjection.getCreatedDate())
          .build();
 }
}
