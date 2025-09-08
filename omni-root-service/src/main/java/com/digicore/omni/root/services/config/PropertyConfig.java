package com.digicore.omni.root.services.config;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationPropertiesScan
@ConfigurationProperties(prefix = "omni.application")
public class PropertyConfig {


    private String[] complianceDocumentTypes = {"Logo","Proof Of Address","International Passport","Driving License",
            "Voter's Card","National ID","CAC Registration Certificate","NIN","SCUML", "MEMART", "Director Particulars","Incorporation Certificate"};

    private String[] cardTypes = {"MASTERCARD, VISA, VERVE, AFRIGO"};
    private String[] webCardCurrency = {"NGN","USD"};
    private String[] webTransferCurrency = {"NGN"};
    private String[] webUSSDCurrency = {"NGN"};
    private String[] webQRCurrency = {"NGN"};
    private String[] posCardCurrency = {"NGN"};
    private String[] posTransferCurrency = {"NGN"};
    private String[] posUSSDCurrency = {"NGN"};
    private String[] posQRCurrency = {"NGN"};
    private String[] apiCardCurrency = {"NGN","USD"};
    private String[] apiTransferCurrency = {"NGN"};
    private String[] apiUSSDCurrency = {"NGN"};
    private String[] apiQRCurrency = {"NGN"};

}
