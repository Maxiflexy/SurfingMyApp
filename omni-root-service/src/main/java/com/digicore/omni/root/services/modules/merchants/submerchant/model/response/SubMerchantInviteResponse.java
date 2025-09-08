package com.digicore.omni.root.services.modules.merchants.submerchant.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author mosh
 * @role software engineer
 * @createdOn 13 Wed Aug, 2025
 */

@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SubMerchantInviteResponse {
    private Long id;
    private String businessName;
    private String email;
    private String status;
    private String inviteCode;
    private LocalDateTime dateCreated;
}
