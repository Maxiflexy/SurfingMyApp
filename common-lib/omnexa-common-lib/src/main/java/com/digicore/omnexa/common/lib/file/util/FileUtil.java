/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.common.lib.file.util;

import com.digicore.omnexa.common.lib.api.ApiError;
import com.digicore.omnexa.common.lib.exception.OmnexaException;
import com.digicore.omnexa.common.lib.file.dto.FileUploadedDTO;
import com.digicore.omnexa.common.lib.file.propeties.FilePropertyConfig;
import java.io.File;
import java.io.IOException;
import java.util.Base64;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

/*
 * @author Oluwatobi Ogunwuyi
 * @createdOn Feb-17(Mon)-2025
 */

@Slf4j
@Getter
public abstract class FileUtil {
  private static final String VIDEO_MP4_VALUE = "video/mp4";
  private static final String TEXT_CSV_VALUE = "text/csv";
  private static final String APK_FILE_VALUE = "application/vnd.android.package-archive";
  public static final String FILE_TYPE_REJECTED_MESSAGE = "file type rejected";
  public static final String FILE_TYPE_REJECTED_CODE = "FIL_001";

  public static final String FILE_SIZE_REJECTED_MESSAGE = "file size rejected";
  public static final String FILE_SIZE_REJECTED_CODE = "FIL_002";

  public static final String INVALID_FILE_MESSAGE = "invalid file supplied";
  public static final String INVALID_FILE_CODE = "FIL_003";
  protected final FilePropertyConfig filePropertyConfig;

  protected FileUtil(FilePropertyConfig filePropertyConfig) {
    this.filePropertyConfig = filePropertyConfig;
    this.fileUploadDirectory = filePropertyConfig.getFileUploadDirectory();
    this.minFileUploadSize = filePropertyConfig.getMinFileUploadSize();
    this.maxFileUploadSize = filePropertyConfig.getMaxFileUploadSize();
  }

  private final String fileUploadDirectory;

  private final int minFileUploadSize;

  private final int maxFileUploadSize;

  public String saveFile(String type, MultipartFile file, String fileName) {
    return saveFile(type, file, fileName, true);
  }

  public String saveFile(String encodedString, String fileName) throws IOException {
    byte[] decodedBytes = Base64.getDecoder().decode(encodedString);
    FileUtils.writeByteArrayToFile(new File(fileUploadDirectory.concat(fileName)), decodedBytes);
    return fileUploadDirectory.concat(fileName);
  }

  public String saveFile(
      String filePath, MultipartFile file, String fileName, boolean fileIsDocument) {
    try {
      if (fileIsDocument) {
        if (fileTypeForDocumentIsNotValid(file) && fileSizeIsNotValid(file))
          throw new OmnexaException(
              HttpStatus.BAD_REQUEST,
              new ApiError(FILE_TYPE_REJECTED_MESSAGE, FILE_TYPE_REJECTED_CODE));
      } else if (fileTypeForMultiMediaIsNotValid(file) && fileSizeIsNotValid(file))
        throw new OmnexaException(
            HttpStatus.BAD_REQUEST,
            new ApiError(FILE_TYPE_REJECTED_MESSAGE, FILE_TYPE_REJECTED_CODE));
      return getSavedFilePath(filePath, file, fileName);
    } catch (Exception e) {
      // TODO add custom fileStorageException
      log.error("Could not save the file because : {}", e.getMessage());
      e.printStackTrace();
      throw new OmnexaException("Could not save the file");
    }
  }

  protected abstract String getSavedFilePath(String type, MultipartFile file, String fileName)
      throws IOException;

  public String fileName(String uuid, MultipartFile file) {
    return fileName(uuid, file, true);
  }

  public String fileName(String uuid, MultipartFile file, boolean fileIsDocument) {
    if (fileIsDocument) {
      if (fileTypeForDocumentIsNotValid(file))
        throw new OmnexaException(
            HttpStatus.BAD_REQUEST,
            new ApiError(FILE_TYPE_REJECTED_MESSAGE, FILE_TYPE_REJECTED_CODE));
    } else {
      if (fileTypeForMultiMediaIsNotValid(file))
        throw new OmnexaException(
            HttpStatus.BAD_REQUEST,
            new ApiError(FILE_TYPE_REJECTED_MESSAGE, FILE_TYPE_REJECTED_CODE));
    }
    if (fileSizeIsNotValid(file))
      throw new OmnexaException(
          HttpStatus.BAD_REQUEST,
          new ApiError(FILE_SIZE_REJECTED_MESSAGE, FILE_SIZE_REJECTED_CODE));
    String originalFileName = file.getOriginalFilename();
    if (originalFileName == null || file.isEmpty())
      throw new OmnexaException(
          HttpStatus.BAD_REQUEST, new ApiError(INVALID_FILE_MESSAGE, INVALID_FILE_CODE));
    return String.format(
        "%s%s",
        uuid,
        originalFileName.lastIndexOf(".") == -1
            ? ".jpg"
            : originalFileName.substring(originalFileName.lastIndexOf(".")));
  }

  public abstract void deleteFile(FileUploadedDTO fileUploadedDTO);

  public abstract String moveFile(String sourcePath, String destinationPath, String fileName);

  public abstract void moveFiles(String sourcePath, String destinationPath);

  private boolean fileTypeForDocumentIsNotValid(MultipartFile file) {
    log.warn("calling the file type method {}", file.getContentType());
    return !MediaType.TEXT_PLAIN_VALUE.equalsIgnoreCase(file.getContentType())
        && !MediaType.APPLICATION_PDF_VALUE.equalsIgnoreCase(file.getContentType())
        && !MediaType.IMAGE_JPEG_VALUE.equalsIgnoreCase(file.getContentType())
        && !MediaType.IMAGE_PNG_VALUE.equalsIgnoreCase(file.getContentType())
        && !MediaType.APPLICATION_OCTET_STREAM_VALUE.equalsIgnoreCase(file.getContentType())
        && !TEXT_CSV_VALUE.equalsIgnoreCase(file.getContentType())
        && !APK_FILE_VALUE.equalsIgnoreCase(file.getContentType());
  }

  private boolean fileTypeForMultiMediaIsNotValid(MultipartFile file) {
    return !VIDEO_MP4_VALUE.equalsIgnoreCase(file.getContentType())
        && !APK_FILE_VALUE.equalsIgnoreCase(file.getContentType());
  }

  public static String getMediaType(String fileExtension) {
    return switch (fileExtension) {
      case "txt" -> MediaType.TEXT_PLAIN_VALUE;
      case "jpeg" -> MediaType.IMAGE_JPEG_VALUE;
      case "pdf" -> MediaType.APPLICATION_PDF_VALUE;
      case "apk" -> APK_FILE_VALUE;
      default -> MediaType.APPLICATION_OCTET_STREAM_VALUE;
    };
  }

  private boolean fileSizeIsNotValid(MultipartFile file) {
    return file.getSize() < minFileUploadSize || file.getSize() > maxFileUploadSize;
  }

  public abstract byte[] getSavedFile(FileUploadedDTO fileUploadedDTO) throws IOException;
}
