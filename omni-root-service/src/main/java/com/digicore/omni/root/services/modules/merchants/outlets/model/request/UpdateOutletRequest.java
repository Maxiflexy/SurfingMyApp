package com.digicore.omni.root.services.modules.merchants.outlets.model.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author mosh
 * @role software engineer
 * @createdOn 14 Thu Aug, 2025
 */

@Getter
@Setter
public class UpdateOutletRequest {

 @NotNull
 private Long id;

 private String title;

 private String country;

 private String state;
 private String lga;
 private String address;
}
