package com.digicore.omni.root.services.modules.merchants.outlets.model.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author mosh
 * @role software engineer
 * @createdOn 14 Thu Aug, 2025
 */

@Getter
@Setter
@Builder
public class MerchantOutletResponse {

    private Long id;

    private String title;

    private String country;

    private String state;
    private String lga;
    private String address;

    private String status;
    private LocalDateTime createdDate;
}
