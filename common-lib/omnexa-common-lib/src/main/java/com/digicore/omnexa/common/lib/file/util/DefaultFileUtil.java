/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.common.lib.file.util;

import com.digicore.omnexa.common.lib.exception.OmnexaException;
import com.digicore.omnexa.common.lib.file.dto.FileUploadedDTO;
import com.digicore.omnexa.common.lib.file.propeties.FilePropertyConfig;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/*
 * @author Oluwatobi Ogunwuyi
 * @createdOn Feb-17(Mon)-2025
 */

@Component
@Slf4j
@Getter
public class DefaultFileUtil extends FileUtil {
  private final FilePropertyConfig filePropertyConfig;

  public DefaultFileUtil(FilePropertyConfig filePropertyConfig) {
    super(filePropertyConfig);
    log.info("<<< Instantiating DefaultFileUtil >>>");
    this.filePropertyConfig = filePropertyConfig;
  }

  @Override
  protected String getSavedFilePath(String filePath, MultipartFile file, String fileName)
      throws IOException {
    // create folder type if it doesn't exist
    Path uploadPath =
        Paths.get(filePropertyConfig.getFileUploadDirectory(), "/lucid", filePath)
            .toAbsolutePath()
            .normalize();
    Files.createDirectories(uploadPath);
    // copy file to target location
    Path targetLocation = uploadPath.resolve(fileName);
    Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
    return targetLocation.toString();
  }

  @Override
  public void moveFiles(String sourcePath, String destinationPath) {}

  @Override
  public void deleteFile(FileUploadedDTO fileUploadedDTO) {
    try {
      Files.delete(Paths.get(fileUploadedDTO.getFilePath()));
    } catch (IOException e) {
      log.error("unable to delete file, using default utils : {}", e.getMessage());
      throw new OmnexaException("unable to delete file, using default utils : ", e);
    }
  }

  @Override
  public String moveFile(String sourcePath, String destinationPath, String fileName) {
    return destinationPath;
  }

  @Override
  public byte[] getSavedFile(FileUploadedDTO fileUploadedDTO) throws IOException {
    return FileUtils.readFileToByteArray(new File(fileUploadedDTO.getFilePath()));
  }
}
