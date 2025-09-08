package com.digicore.omni.root.services.util;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SupportServiceRequest {
    private String sender;
    private String receiver;
    private String body;
    private String subject;
}
