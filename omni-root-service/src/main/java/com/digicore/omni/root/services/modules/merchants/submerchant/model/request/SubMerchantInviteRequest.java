package com.digicore.omni.root.services.modules.merchants.submerchant.model.request;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * @author mosh
 * @role software engineer
 * @createdOn 13 Wed Aug, 2025
 */

@Setter
@Getter
public class SubMerchantInviteRequest {

 @NotBlank
 private String businessName;

 @NotBlank
 @Pattern(
         regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
         message = "Invalid email format"
 )
 private String email;
}
