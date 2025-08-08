/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.common.lib.file.contract;

import com.digicore.omnexa.common.lib.api.ApiError;
import com.digicore.omnexa.common.lib.exception.OmnexaException;

/**
 * @author Oluwatobi Ogunwuyi
 * @createdOn Aug-05(Tue)-2025
 */
public interface DocumentUploadService<T, V> {
  default T uploadSingleDocument(V request) {
    throw new OmnexaException(
        new ApiError("implementation not found for this contract", "DOC_IMP_001"));
  }

  default T uploadMultipleDocument(V request) {
    throw new OmnexaException(
        new ApiError("implementation not found for this contract", "DOC_IMP_002"));
  }

  default T downloadSingleDocument(V request) {
    throw new OmnexaException(
        new ApiError("implementation not found for this contract", "DOC_IMP_003"));
  }

  default T downloadMultipleDocument(V request) {
    throw new OmnexaException(
        new ApiError("implementation not found for this contract", "DOC_IMP_004"));
  }

  default T setDocumentUploadLocation(V request) {
    throw new OmnexaException(
        new ApiError("implementation not found for this contract", "DOC_IMP_005"));
  }
}
