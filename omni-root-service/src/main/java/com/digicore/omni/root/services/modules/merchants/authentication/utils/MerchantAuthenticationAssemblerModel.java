package com.digicore.omni.root.services.modules.merchants.authentication.utils;

import com.digicore.omni.root.services.modules.backoffice.merchant_management.BackOfficeMerchantManagementController;
import com.digicore.omni.root.services.modules.merchants.authentication.apiModel.LoginResponseApiModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * @author Oluwatobi Ogunwuyi
 * @createdOn Aug-08(Mon)-2022
 */

@Component
public class MerchantAuthenticationAssemblerModel implements RepresentationModelAssembler<LoginResponseApiModel, EntityModel<LoginResponseApiModel>> {
    @Override
    public EntityModel<LoginResponseApiModel> toModel(LoginResponseApiModel entity) {

            return EntityModel.of(entity,
                    linkTo(methodOn(BackOfficeMerchantManagementController.class)
                            .getMerchant((String) entity.getAdditionalInformation().get("merchantId"))).withSelfRel());

    }
}
