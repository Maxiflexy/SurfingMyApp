package com.digicore.omnexa.common.lib.swagger.constant;

/**
 * @author Onyekachi Ejemba
 * @createdOn Jul-31(Thu)-2025
 */
public class CommonEndpointSwaggerDoc {

  public static final String FILTER_API = "filter";
  public static final String SEARCH_API = "search";
  public static final String MERCHANT_API = "merchant";
  public static final String DOCUMENT_API = "document";

  /** Title for the Backoffice controller in Swagger documentation. */
  public static final String FILTER_TITLE = "Filter";

  /** Description for the BACKOFFICE controller in Swagger documentation. */
  public static final String FILTER_DESCRIPTION = "This endpoint is used to filter.";

  /** Title for the Backoffice controller in Swagger documentation. */
  public static final String SEARCH_TITLE = "Search";

  /** Description for the BACKOFFICE controller in Swagger documentation. */
  public static final String SEARCH_DESCRIPTION = "This endpoint is used to search";

  public static final String PAGE_NUMBER = "1";
  public static final String PAGE_NUMBER_DESCRIPTION = "Page number (1-based)";
  public static final String PAGE_SIZE = "15";
  public static final String PAGE_SIZE_DESCRIPTION = "Page size (max 15)";

  public static final String UPLOAD_DOCUMENT_TITLE = "Upload Documents";

  public static final String UPLOAD_DOCUMENT_DESCRIPTION =
      """
                This endpoint handles the uploading of Documents.
                This endpoint has a maximum document upload size;
                This endpoint processes Multifile parts.
          """;
}
