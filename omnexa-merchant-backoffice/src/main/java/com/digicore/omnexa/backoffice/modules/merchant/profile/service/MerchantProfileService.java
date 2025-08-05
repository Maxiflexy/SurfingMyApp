package com.digicore.omnexa.backoffice.modules.merchant.profile.service;


import com.digicore.omnexa.common.lib.enums.ProfileStatus;
import com.digicore.omnexa.common.lib.exception.OmnexaException;
import com.digicore.omnexa.common.lib.feign.MerchantFeignClient;
import com.digicore.omnexa.common.lib.profile.contract.ProfileService;
import com.digicore.omnexa.common.lib.profile.dto.response.ProfileInfoResponse;
import com.digicore.omnexa.common.lib.util.PaginatedResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE;

/**
 * @author Onyekachi Ejemba
 * @createdOn Aug-04(Mon)-2025
 */
@Service
@RequiredArgsConstructor
public class MerchantProfileService implements ProfileService {

    private final MerchantFeignClient merchantFeignClient;

//    @Override
//    public PaginatedResponse<ProfileInfoResponse> getAllProfiles(Integer pageNumber, Integer pageSize) {
//        return merchantFeignClient.getAllMerchantProfile(pageNumber, pageSize).getBody();
//    }

    @Override
    public PaginatedResponse<ProfileInfoResponse> getAllProfiles(Integer pageNumber, Integer pageSize) {
        try {
            ResponseEntity<PaginatedResponse<ProfileInfoResponse>> response =
                    merchantFeignClient.getAllMerchantProfile(pageNumber, pageSize);
            return response.getBody();
        } catch (Exception e) {
            throw new OmnexaException("Failed to retrieve merchant profiles", SERVICE_UNAVAILABLE);
        }
    }

    @Override
    public PaginatedResponse<ProfileInfoResponse> searchProfilesPaginated(
            String search, Integer pageNumber, Integer pageSize) {
        try {
            ResponseEntity<PaginatedResponse<ProfileInfoResponse>> response =
                    merchantFeignClient.searchMerchantProfiles(search, pageNumber, pageSize);
            return response.getBody();
        } catch (Exception e) {
            throw new OmnexaException("Failed to search merchant profiles", SERVICE_UNAVAILABLE);
        }
    }

    @Override
    public PaginatedResponse<ProfileInfoResponse> getProfilesByStatusPaginated(
            ProfileStatus profileStatus, Integer pageNumber, Integer pageSize) {
        try {
            ResponseEntity<PaginatedResponse<ProfileInfoResponse>> response =
                    merchantFeignClient.filterMerchantProfilesByStatus(profileStatus, pageNumber, pageSize);
            return response.getBody();
        } catch (Exception e) {
            throw new OmnexaException("Failed to filter merchant profiles by status", SERVICE_UNAVAILABLE);
        }
    }
}
