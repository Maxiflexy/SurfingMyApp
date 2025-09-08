package com.digicore.omni.root.services.modules.merchants.onboarding.utils;

import com.digicore.omni.data.lib.modules.common.apimodel.SignIn;
import com.digicore.omni.root.services.modules.merchants.authentication.controller.MerchantAuthenticationController;
import com.digicore.omni.root.services.modules.merchants.onboarding.apiModels.SignUpResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * @author Oluwatobi Ogunwuyi
 * @createdOn Aug-09(Tue)-2022
 */
@Component
public class MerchantSignUpAssemblerModel implements RepresentationModelAssembler<SignUpResponse,
        EntityModel<SignUpResponse>> {

    @Value("${payment.gateway.root.enable.auto.sign.in.after.profile.creation:false}")
    private boolean allowAutomaticSignAfterSignUp;
    @Override
    public EntityModel<SignUpResponse> toModel(SignUpResponse entity) {
        try {
            return EntityModel.of(entity,
                //   linkTo(methodOn(MerchantAuthenticationController.class).automaticLogin(entity.getEncryptedUsername(),entity.getEncryptedPassword())).withRel("click below link to sign with the pre-encrypted credentials, HTTP method is a POST"),
                    WebMvcLinkBuilder.linkTo(methodOn(MerchantAuthenticationController.class).login(new SignIn())).withRel("click below link to login by supplying email and password as request body, HTTP method is a POST")
                    );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

