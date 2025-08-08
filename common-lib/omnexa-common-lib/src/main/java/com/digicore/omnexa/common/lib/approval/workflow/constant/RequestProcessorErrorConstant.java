/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.common.lib.approval.workflow.constant;

/*
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jan-26(Sun)-2025
 */

public class RequestProcessorErrorConstant {
  public static final String OPERATION_NOT_SUPPORTED_MESSAGE = "no handler for request type ";

  public static final String MULTIPLE_HANDLER_FOR_REQUEST_FOUND_MESSAGE =
      "multiple request handlers found for request type ";

  public static final String REQUEST_PROCESSOR_ERROR_MESSAGE =
      "kindly reach out to support for help";
  public static final String REQUEST_PROCESSOR_ERROR_CODE = "REQ_PRO_001";

  public static final String PRETTY_PRINTER =
      " <<<< while trying to handle a request, this error was encountered : {}  >>>>";
}
