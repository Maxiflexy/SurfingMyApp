/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.merchant.modules.profile.service;

import static com.digicore.omnexa.common.lib.constant.message.MessageConstant.NOT_FOUND;
import static com.digicore.omnexa.common.lib.constant.system.SystemConstant.SYSTEM_MERCHANT_ID_PLACEHOLDER;

import com.digicore.omnexa.common.lib.exception.OmnexaException;
import com.digicore.omnexa.common.lib.file.contract.DocumentUploadService;
import com.digicore.omnexa.common.lib.file.dto.FileUploadedDTO;
import com.digicore.omnexa.common.lib.file.propeties.FilePropertyConfig;
import com.digicore.omnexa.common.lib.file.util.FileUtil;
import com.digicore.omnexa.common.lib.properties.MessagePropertyConfig;
import com.digicore.omnexa.common.lib.util.RequestUtil;
import com.digicore.omnexa.merchant.modules.profile.data.model.kyc.MerchantKycDocument;
import com.digicore.omnexa.merchant.modules.profile.data.repository.MerchantKycDocumentRepository;
import com.digicore.omnexa.merchant.modules.profile.data.repository.MerchantProfileRepository;
import com.digicore.omnexa.merchant.modules.profile.dto.request.MerchantKycProfileDocumentUploadDTO;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Hossana Chukwunyere
 * @createdOn Aug-05(Tue)-2025
 */
@Service
@RequiredArgsConstructor
public class MerchantKycProfileDocumentUploadService
    implements DocumentUploadService<List<FileUploadedDTO>, MerchantKycProfileDocumentUploadDTO> {

  private final FileUtil fileUtil;
  private final FilePropertyConfig filePropertyConfig;
  private final MerchantKycDocumentRepository merchantKycDocumentRepository;
  private final MerchantProfileRepository merchantProfileRepository;
  private final MessagePropertyConfig messagePropertyConfig;

  @Override
  public List<FileUploadedDTO> uploadMultipleDocument(
      MerchantKycProfileDocumentUploadDTO merchantKycProfileDocumentUploadDTO) {

    MerchantKycDocument merchantKycDocument =
        merchantKycDocumentRepository
            .findFirstByMerchantProfileMerchantId(
                RequestUtil.getValueFromAccessToken(SYSTEM_MERCHANT_ID_PLACEHOLDER))
            .orElse(new MerchantKycDocument());

    List<FileUploadedDTO> fileUploadedDTOS = new ArrayList<>();

    if (!RequestUtil.nullOrEmpty(merchantKycDocument.getKycDocument())) {
      fileUploadedDTOS =
          RequestUtil.getGsonMapper()
              .fromJson(
                  merchantKycDocument.getKycDocument(),
                  new TypeToken<List<FileUploadedDTO>>() {}.getType());
    }

    for (MerchantKycProfileDocumentUploadDTO.MerchantDocument merchantDocument :
        merchantKycProfileDocumentUploadDTO.getFiles()) {
      MultipartFile multipartFile = (MultipartFile) merchantDocument.getFile();
      if (multipartFile != null) {
        String documentType = merchantDocument.getDocumentType();
        String identifier = merchantDocument.getIdentifier();

        // Check if the document type already exists
        Optional<FileUploadedDTO> existingFileOpt =
            fileUploadedDTOS.stream()
                .filter(
                    file ->
                        file.getDocumentType() != null
                            && documentType.equalsIgnoreCase(file.getDocumentType())
                            && file.getIdentifier() != null
                            && identifier.equalsIgnoreCase(file.getIdentifier()))
                .findFirst();

        List<FileUploadedDTO> finalFileUploadedDTOS = fileUploadedDTOS;
        existingFileOpt.ifPresent(
            existingFile -> {
              // Delete the existing file
              fileUtil.deleteFile(existingFile);
              // Remove from the list
              finalFileUploadedDTOS.remove(existingFile);
            });

        String fileId =
            filePropertyConfig.getFileUploadDirectory().concat(UUID.randomUUID().toString());

        String pathToFile = fileUtil.saveFile(documentType, multipartFile, fileId);

        // Create new file entry
        FileUploadedDTO newFile = new FileUploadedDTO();
        newFile.setFilePath(pathToFile);
        newFile.setFileId(fileId);
        newFile.setDocumentType(documentType);
        newFile.setIdentifier(identifier);

        // Add to list
        fileUploadedDTOS.add(newFile);
      }
    }

    // Update entity and save
    merchantKycDocument.setKycDocument(RequestUtil.getGsonMapper().toJson(fileUploadedDTOS));

    if (RequestUtil.nullOrEmpty(merchantKycDocument.getMerchantProfile()))
      merchantKycDocument.setMerchantProfile(
          merchantProfileRepository
              .findByMerchantId(RequestUtil.getValueFromAccessToken(SYSTEM_MERCHANT_ID_PLACEHOLDER))
              .orElseThrow(
                  () ->
                      new OmnexaException(
                          messagePropertyConfig.getActivationMessage(NOT_FOUND),
                          HttpStatus.NOT_FOUND)));

    merchantKycDocumentRepository.save(merchantKycDocument);
    fileUploadedDTOS.forEach(fileUploadedDTO -> fileUploadedDTO.setFilePath(null));
    return fileUploadedDTOS;
  }


  @Override
  public List<FileUploadedDTO> uploadMultipleDocument(
      MerchantKycProfileDocumentUploadDTO merchantKycProfileDocumentUploadDTO, String profileId) {

    MerchantKycDocument merchantKycDocument =
        merchantKycDocumentRepository
            .findFirstByMerchantProfileMerchantId(
                RequestUtil.getValueFromAccessToken(SYSTEM_MERCHANT_ID_PLACEHOLDER))
            .orElse(new MerchantKycDocument());

    List<FileUploadedDTO> fileUploadedDTOS = new ArrayList<>();

    if (!RequestUtil.nullOrEmpty(merchantKycDocument.getKycDocument())) {
      fileUploadedDTOS =
          RequestUtil.getGsonMapper()
              .fromJson(
                  merchantKycDocument.getKycDocument(),
                  new TypeToken<List<FileUploadedDTO>>() {}.getType());
    }

    for (MerchantKycProfileDocumentUploadDTO.MerchantDocument merchantDocument :
        merchantKycProfileDocumentUploadDTO.getFiles()) {
      MultipartFile multipartFile = (MultipartFile) merchantDocument.getFile();
      if (multipartFile != null) {
        String documentType = merchantDocument.getDocumentType();
        String identifier = merchantDocument.getIdentifier();

        // Check if the document type already exists
        Optional<FileUploadedDTO> existingFileOpt =
            fileUploadedDTOS.stream()
                .filter(
                    file ->
                        file.getDocumentType() != null
                            && documentType.equalsIgnoreCase(file.getDocumentType())
                            && file.getIdentifier() != null
                            && identifier.equalsIgnoreCase(file.getIdentifier()))
                .findFirst();

        List<FileUploadedDTO> finalFileUploadedDTOS = fileUploadedDTOS;
        existingFileOpt.ifPresent(
            existingFile -> {
              // Delete the existing file
              fileUtil.deleteFile(existingFile);
              // Remove from the list
              finalFileUploadedDTOS.remove(existingFile);
            });

        String fileId =
            filePropertyConfig.getFileUploadDirectory().concat(UUID.randomUUID().toString());

        String pathToFile = fileUtil.saveFile(documentType, multipartFile, fileId);

        // Create new file entry
        FileUploadedDTO newFile = new FileUploadedDTO();
        newFile.setFilePath(pathToFile);
        newFile.setFileId(fileId);
        newFile.setDocumentType(documentType);
        newFile.setIdentifier(identifier);

        // Add to list
        fileUploadedDTOS.add(newFile);
      }
    }

    // Update entity and save
    merchantKycDocument.setKycDocument(RequestUtil.getGsonMapper().toJson(fileUploadedDTOS));

    if (RequestUtil.nullOrEmpty(merchantKycDocument.getMerchantProfile()))
      merchantKycDocument.setMerchantProfile(
          merchantProfileRepository
              .findByMerchantId(RequestUtil.getValueFromAccessToken(SYSTEM_MERCHANT_ID_PLACEHOLDER))
              .orElseThrow(
                  () ->
                      new OmnexaException(
                          messagePropertyConfig.getActivationMessage(NOT_FOUND),
                          HttpStatus.NOT_FOUND)));

    merchantKycDocumentRepository.save(merchantKycDocument);
    fileUploadedDTOS.forEach(fileUploadedDTO -> fileUploadedDTO.setFilePath(null));
    return fileUploadedDTOS;
  }
}
