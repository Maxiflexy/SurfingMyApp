/*
 * Created by Monsuru (5/9/2022)
 */

package com.digicore.omni.root.services.modules.backoffice.account.admin.util;

import com.digicore.omni.root.services.modules.backoffice.account.admin.apimodel.InvitationApiModel;
import com.digicore.omni.root.services.modules.backoffice.account.user.controller.BackOfficeUserController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * @author Monsuru <br/>
 * @since Sep-05(Mon)-2022
 */
@Component
public class BackOfficerUserInvitationAssemblerModel implements
        RepresentationModelAssembler<InvitationApiModel, EntityModel<InvitationApiModel>> {
    @Override
    public EntityModel<InvitationApiModel> toModel(InvitationApiModel entity) {


            return EntityModel.of(entity,
                    WebMvcLinkBuilder.linkTo(methodOn(BackOfficeUserController.class).getAllBackOfficeUsers(1, 25))
                            .withRel("view all back office users"));

    }
}
