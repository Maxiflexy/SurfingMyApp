package com.digicore.omnexa.common.lib.feign;

import static com.digicore.omnexa.common.lib.api.ApiVersion.API_V1;
import static com.digicore.omnexa.common.lib.swagger.constant.CommonEndpointSwaggerDoc.MERCHANT_API;

import com.digicore.omnexa.common.lib.api.ApiResponseJson;
import com.digicore.omnexa.common.lib.enums.ProfileStatus;
import com.digicore.omnexa.common.lib.feign.config.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Onyekachi Ejemba
 * @createdOn Aug-04(Mon)-2025
 */
@FeignClient(name = "omnexa-merchant", configuration = FeignClientConfig.class)
public interface MerchantFeignClient {

  /**
   * Retrieves all merchant profiles with pagination.
   *
   * @param pageNumber the page number (1-based indexing, optional)
   * @param pageSize the page size (maximum 16, optional)
   * @return paginated response containing merchant profile information
   */
  @GetMapping(API_V1 + MERCHANT_API)
  ResponseEntity<ApiResponseJson<Object>> getAllMerchantProfile(
      @RequestParam(required = false) Integer pageNumber,
      @RequestParam(required = false) Integer pageSize);

  /**
   * Searches merchant profiles by business name or business email.
   *
   * @param search the search term to match against business name or business email
   * @param pageNumber the page number (1-based indexing, optional)
   * @param pageSize the page size (maximum 16, optional)
   * @return paginated response containing matching merchant profiles
   */
  @GetMapping(API_V1 + MERCHANT_API + "/search")
  ResponseEntity<ApiResponseJson<Object>> searchMerchantProfiles(
      @RequestParam String search,
      @RequestParam(required = false) Integer pageNumber,
      @RequestParam(required = false) Integer pageSize);

  /**
   * Filters merchant profiles by profile status.
   *
   * @param profileStatus the profile status to filter by
   * @param pageNumber the page number (1-based indexing, optional)
   * @param pageSize the page size (maximum 16, optional)
   * @return paginated response containing merchant profiles with the specified status
   */
  @GetMapping(API_V1 + MERCHANT_API + "/filter")
  ResponseEntity<ApiResponseJson<Object>> filterMerchantProfilesByStatus(
      @RequestParam ProfileStatus profileStatus,
      @RequestParam(required = false) Integer pageNumber,
      @RequestParam(required = false) Integer pageSize);
}
