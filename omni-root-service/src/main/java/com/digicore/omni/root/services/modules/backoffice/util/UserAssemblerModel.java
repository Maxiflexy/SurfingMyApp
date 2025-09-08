/*
 * Created by Monsuru (7/8/2022)
 */
package com.digicore.omni.root.services.modules.backoffice.util;

import com.digicore.omni.root.services.modules.backoffice.account.user.controller.BackOfficeUserController;
import com.digicore.omni.root.services.modules.merchants.authentication.apiModel.LoginResponseApiModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserAssemblerModel implements RepresentationModelAssembler<LoginResponseApiModel, EntityModel<LoginResponseApiModel>> {

    @Override
    public EntityModel<LoginResponseApiModel> toModel(LoginResponseApiModel backOfficeUser) {

            return EntityModel.of(backOfficeUser,
                    WebMvcLinkBuilder.linkTo(methodOn(BackOfficeUserController.class).getBackOfficeUser((String)backOfficeUser.getAdditionalInformation().get("userId"))).withSelfRel(),
                    linkTo(methodOn(BackOfficeUserController.class).getAllBackOfficeUsers(0, 25)).withRel("backoffice users"));

    }

    @Override
    public CollectionModel<EntityModel<LoginResponseApiModel>> toCollectionModel(Iterable<? extends LoginResponseApiModel> backOfficeUsers) {

        List<EntityModel<LoginResponseApiModel>> models = new ArrayList<>();
        for (LoginResponseApiModel backOfficeUser : backOfficeUsers) {
            models.add(toModel(backOfficeUser));
        }

            return CollectionModel.of(models,
                    linkTo(methodOn(BackOfficeUserController.class).getAllBackOfficeUsers(0, 25))
                            .withSelfRel());

    }
}
