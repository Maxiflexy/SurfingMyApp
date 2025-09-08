package com.digicore.omni.root.services.modules.merchants.outlets.model.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * @author mosh
 * @role software engineer
 * @createdOn 14 Thu Aug, 2025
 */

@Getter
@Setter
public class CreateOutletRequest {

 @NotBlank
 private String title;

 @NotBlank
 private String country;

 @NotBlank
 private String state;
 private String lga;
 private String address;
}
