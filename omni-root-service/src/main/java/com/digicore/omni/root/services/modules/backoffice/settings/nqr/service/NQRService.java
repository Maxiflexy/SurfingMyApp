package com.digicore.omni.root.services.modules.backoffice.settings.nqr.service;

import com.digicore.api.helper.services.ApiClientService;
import com.digicore.omni.data.lib.modules.backoffice.exception.BackOfficeExceptionProcessor;
import com.digicore.omni.data.lib.modules.backoffice.model.NQRMerchant;
import com.digicore.omni.data.lib.modules.backoffice.repository.NQRMerchantRepository;
import com.digicore.omni.payment.common.lib.modules.qr.nqr.request.MerchantBindCollectionAccountRequest;
import com.digicore.omni.payment.common.lib.modules.qr.nqr.request.MerchantCreationRequest;
import com.digicore.omni.payment.common.lib.modules.qr.nqr.request.SubMerchantCreationRequest;
import com.digicore.omni.payment.common.lib.modules.qr.nqr.response.MerchantBindCollectionAccountResponse;
import com.digicore.omni.payment.common.lib.modules.qr.nqr.response.MerchantCreationDetailResponse;
import com.digicore.omni.payment.common.lib.modules.qr.nqr.response.MerchantCreationResponse;
import com.digicore.omni.payment.common.lib.modules.qr.nqr.response.NQRFixedResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

/**
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jun-04(Sun)-2023
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class NQRService {

    private final ApiClientService apiClientService;

    @Value("${web.engine.external.service.nqr.endpoint:http://localhost:8020/api/v1/nqr/}")
    private String nqrEndpoint;

    private final NQRMerchantRepository nqrMerchantRepository;

    public Object createMerchant(MerchantCreationRequest merchantCreationRequest){
        Object createNQRMerchantResponse = createNQRMerchant(merchantCreationRequest);
        log.info(">>> created NQR Merchant Response {}", createNQRMerchantResponse);
        MerchantCreationResponse createdMerchant = (MerchantCreationResponse) createNQRMerchantResponse;
        if (createdMerchant.getReturnCode().equalsIgnoreCase("SUCCESS")) {
            Object mapMerchantAccountDetails = bindMerchantAccount(MerchantBindCollectionAccountRequest.builder()
                    .mch_no(createdMerchant.getMchNo())
                    .bank_no(merchantCreationRequest.getBank_no())
                    .account_name(merchantCreationRequest.getAccount_name())
                    .institution_number(createdMerchant.getInstitutionNumber())
                    .account_number(merchantCreationRequest.getAccount_number())
                    .build());

            MerchantBindCollectionAccountResponse completedMerchantCreation = (MerchantBindCollectionAccountResponse) mapMerchantAccountDetails;
            if (completedMerchantCreation.getReturnCode().equals("Success")) {
                saveNQRMerchantDetails(createdMerchant, MerchantBindCollectionAccountRequest.builder()
                        .mch_no(createdMerchant.getMchNo())
                        .bank_no(merchantCreationRequest.getBank_no())
                        .account_name(merchantCreationRequest.getAccount_name())
                        .institution_number(createdMerchant.getInstitutionNumber())
                        .account_number(merchantCreationRequest.getAccount_number())
                        .build());
         NQRMerchant nqrMerchant = createSubMerchant(getSubMerchantCreationRequest(createdMerchant, "0"));

                nqrMerchantRepository.save(nqrMerchant);


                return MerchantCreationDetailResponse.builder()
                        .mchNo(createdMerchant.getMchNo())
                        .institutionNumber(createdMerchant.getInstitutionNumber())
                        .merchantName(createdMerchant.getMerchantName())
                        .accountName(merchantCreationRequest.getAccount_name())
                        .accountNumber(merchantCreationRequest.getAccount_number())
                        .merchantAddress(createdMerchant.getMerchantAddress())
                        .merchantTIN(createdMerchant.getMerchantTIN())
                        .bankNo(merchantCreationRequest.getBank_no())
                        .merchantEmail(createdMerchant.getMerchantEmail())
                        .merchantContactName(createdMerchant.getMerchantContactName())
                        .merchantPhoneNumber(createdMerchant.getMerchantPhoneNumber())
                        .build();
            }
        }
        throw BackOfficeExceptionProcessor.nqrProfileCreationFailed(createdMerchant.getReturnMessage());
    }

    private SubMerchantCreationRequest getSubMerchantCreationRequest(MerchantCreationResponse createdMerchant,String subFixed) {
        SubMerchantCreationRequest subMerchantCreationRequest = new SubMerchantCreationRequest();
        subMerchantCreationRequest.setMch_no(createdMerchant.getMchNo());
        subMerchantCreationRequest.setSub_fixed(subFixed);
        subMerchantCreationRequest.setName(createdMerchant.getMerchantName());
        subMerchantCreationRequest.setEmail(createdMerchant.getMerchantEmail());
        subMerchantCreationRequest.setPhone_number(createdMerchant.getMerchantPhoneNumber());
        subMerchantCreationRequest.setInstitution_number(createdMerchant.getInstitutionNumber());
        return subMerchantCreationRequest;
    }



    private Object createNQRMerchant(MerchantCreationRequest merchantCreationRequest) {
        try {
            return apiClientService.postRequest(nqrEndpoint.concat("create-merchant"), merchantCreationRequest, MerchantCreationResponse.class, MediaType.APPLICATION_JSON);
        } catch (RestClientException e) {
            throw BackOfficeExceptionProcessor.nqrProfileCreationFailed(e.getMessage());
        }
    }


    private Object bindMerchantAccount(MerchantBindCollectionAccountRequest merchantBindCollectionAccountRequest) {
        try {
            return apiClientService.postRequest(nqrEndpoint.concat("bind-merchant-collection-account"), merchantBindCollectionAccountRequest, MerchantBindCollectionAccountResponse.class, MediaType.APPLICATION_JSON);
        } catch (RestClientException e) {
            throw BackOfficeExceptionProcessor.nqrProfileCreationFailed(e.getMessage());
        }
    }

    private NQRMerchant createSubMerchant(SubMerchantCreationRequest subMerchantCreationRequest){
         NQRMerchant nqrMerchant = nqrMerchantRepository.findByMchNo(subMerchantCreationRequest.getMch_no()).orElseThrow(BackOfficeExceptionProcessor::merchantProfileCreationNotDone);

        subMerchantCreationRequest.setSub_fixed("0");
        subMerchantCreationRequest.setSub_amount("100");
        NQRFixedResponse nqrFixedResponse = apiClientService.postRequest(nqrEndpoint.concat("create-sub-merchant"), subMerchantCreationRequest, NQRFixedResponse.class, MediaType.APPLICATION_JSON);
        if (nqrFixedResponse.getReturnCode().equals("Success")){
            nqrMerchant.setSubMchNo(nqrFixedResponse.getSubMerchantNo());
            return nqrMerchant;
        }

        throw BackOfficeExceptionProcessor.subMerchantCreationFailed(nqrFixedResponse.getReturnMessage());



    }



    private void saveNQRMerchantDetails(MerchantCreationResponse merchantCreationResponse, MerchantBindCollectionAccountRequest otherMerchantBankDetails) {
        this.nqrMerchantRepository.save(NQRMerchant.builder().merchantContactName(merchantCreationResponse.getMerchantContactName()).merchantName(merchantCreationResponse.getMerchantName()).institutionNumber(merchantCreationResponse.getInstitutionNumber()).merchantPhoneNumber(merchantCreationResponse.getMerchantPhoneNumber()).merchantTIN(merchantCreationResponse.getMerchantTIN()).merchantEmail(merchantCreationResponse.getMerchantEmail()).mchNo(merchantCreationResponse.getMchNo()).merchantAddress(merchantCreationResponse.getMerchantAddress()).bankNo(otherMerchantBankDetails.getBank_no()).accountName(otherMerchantBankDetails.getAccount_name()).accountNumber(otherMerchantBankDetails.getAccount_number()).build());
    }


}
