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
@ConfigurationProperties(prefix = "omni.notification.support")
public class NotificationEmailPropertyForSupportConfig {

    private String onboarding;

    private String profileActivation;

    private String transactions;

    private String disputeAndRefunds;

    private String settlement;

    private String generalEnquiries;


}
