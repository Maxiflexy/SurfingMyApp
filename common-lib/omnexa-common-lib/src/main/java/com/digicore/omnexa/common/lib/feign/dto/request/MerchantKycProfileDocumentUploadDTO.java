/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */
package com.digicore.omnexa.common.lib.feign.dto.request;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Onyekachi Ejemba
 * @createdOn Aug-11(Mon)-2025
 */
@Getter
@Setter
public class MerchantKycProfileDocumentUploadDTO {
  private MerchantDocument[] files;

  @Getter
  @Setter
  public static class MerchantDocument {
    private String identifier;
    private String documentType;
    private Object file;
  }
}
