package com.digicore.omni.root.services.modules.merchants.account.util;

//import com.digicore.omni.root.services.modules.backoffice.merchant_management.BackOfficeMerchantManagementController;
import com.digicore.omni.data.lib.modules.merchant.apimodel.MerchantProfileApiModel;
import com.digicore.omni.root.services.modules.backoffice.merchant_management.BackOfficeMerchantManagementController;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class MerchantAssemblerModel implements RepresentationModelAssembler<MerchantProfileApiModel,
        EntityModel<MerchantProfileApiModel>> {
    @Override
    public EntityModel<MerchantProfileApiModel> toModel(MerchantProfileApiModel merchant) {

            return EntityModel.of(merchant,
                    linkTo(methodOn(BackOfficeMerchantManagementController.class).getMerchant(merchant.getMerchantId())).withRel("get a merchant by id"));

    }

    @Override
    public CollectionModel<EntityModel<MerchantProfileApiModel>> toCollectionModel(
            Iterable<? extends MerchantProfileApiModel> entities) {
        List<EntityModel<MerchantProfileApiModel>> merchants = new ArrayList<>();
        for (MerchantProfileApiModel merchant: entities){
            merchants.add(toModel(merchant));
        }

            return CollectionModel.of(merchants,
                    linkTo(methodOn(BackOfficeMerchantManagementController.class).getAllMerchants(0, 10))
                            .withSelfRel());

    }
}
