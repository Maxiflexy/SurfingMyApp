package com.digicore.omni.root.services.modules.merchants.submerchant.model.response;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author Onyekachi Ejemba
 * @createdOn Aug-14(Thu)-2025
 */
@Getter
@Setter
@Builder
public class SubMerchantListResponse {
    private List<SubMerchantResponse> subMerchants;
    private int totalElements;
    private int totalPages;
    private int currentPage;
    private int pageSize;
    private boolean hasNext;
    private boolean hasPrevious;
}
