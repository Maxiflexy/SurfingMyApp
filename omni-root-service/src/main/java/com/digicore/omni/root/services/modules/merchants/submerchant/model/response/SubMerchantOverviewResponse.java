package com.digicore.omni.root.services.modules.merchants.submerchant.model.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Onyekachi Ejemba
 * @createdOn Sep-01(Mon)-2025
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubMerchantOverviewResponse {

    private Long totalSubMerchants;
    private CollectionSummary totalCollections;
    private CollectionSummary totalCardCollections;
    private CollectionSummary webAndPosCollections;
    private CollectionSummary webAndPosCollectionsCards;
    private CollectionSummary webAndPosCollectionVirtualAccount;
    private CollectionSummary webAndPosCollectionsUssd;
    private CollectionSummary webAndPosCollectionsQr;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CollectionSummary {
        private Double value;
        private Long count;
    }

}
