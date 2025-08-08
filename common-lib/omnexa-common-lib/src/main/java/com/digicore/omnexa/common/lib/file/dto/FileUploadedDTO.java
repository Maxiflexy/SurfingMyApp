/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.common.lib.file.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

/*
 * @author Oluwatobi Ogunwuyi
 * @createdOn Feb-17(Mon)-2025
 */

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FileUploadedDTO {
  private String filePath;
  private String fileId;
  private String documentType;
  private String identifier;
}
