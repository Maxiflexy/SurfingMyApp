package com.digicore.omni.root.services.modules.backoffice.merchant_management.processor;

import com.digicore.omni.root.services.modules.backoffice.merchant_management.service.MerchantManagementComplianceService;
import com.digicore.request.processor.annotations.RequestHandler;
import com.digicore.request.processor.annotations.RequestType;
import com.digicore.request.processor.enums.RequestHandlerType;
import lombok.RequiredArgsConstructor;

/**
 * @author Oluwatobi Ogunwuyi
 * @createdOn Oct-30(Sun)-2022
 */


@RequestHandler(type = RequestHandlerType.MERCHANT_COMPLIANCE_CHECKER)
@RequiredArgsConstructor
public class MerchantManagementComplianceRequestProcessor {

  private final MerchantManagementComplianceService merchantComplianceService;


    @RequestType(name = "approveMerchantCompliance")
    public Object approveMerchantCompliance(Object request)  {
       return merchantComplianceService.approveMerchantCompliance(request,null);

    }

    @RequestType(name = "editMerchantProfile")
    public Object editMerchantProfile(Object request) {
        return merchantComplianceService.editMerchantProfile(request, null);
    }

//    @RequestType(name = "declineMerchantCompliance")
//    public Object declineMerchantCompliance(Object request)  {
//        return merchantComplianceService.approveMerchantCompliance(request);
//
//    }








}
