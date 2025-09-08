package com.digicore.omni.root.services.modules.merchants.authentication.apiModel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * @author Oluwatobi Ogunwuyi
 * @createdOn Aug-08(Mon)-2022
 */

@Getter
@Setter
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginResponseApiModel {
    private String accessToken;
    private Map<String,Object> additionalInformation;



}
