/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.common.lib.file.propeties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.stereotype.Component;

/*
 * @author Oluwatobi Ogunwuyi
 * @createdOn Feb-17(Mon)-2025
 */

@Component
@ConfigurationPropertiesScan
@ConfigurationProperties(prefix = "omnexa.file")
@Getter
@Setter
public class FilePropertyConfig {
  private String fileUploadDirectory;

  private int minFileUploadSize;

  private int maxFileUploadSize;

  private String s3BucketName;
  private String s3AccessKey;
  private String s3SecretKey;
  private String s3Region;
}
