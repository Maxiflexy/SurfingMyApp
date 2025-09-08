/*
 * Created by Monsuru (9/9/2022)
 */

package com.digicore.omni.root.services.modules.backoffice.merchant_management;



import com.digicore.common.util.ClientUtil;
import com.digicore.omni.data.lib.modules.backoffice.dto.BaseWebFeeConfiguration;
import com.digicore.omni.data.lib.modules.common.dtos.TransactionFilterDTO;
import com.digicore.omni.data.lib.modules.common.dtos.compliance.BackOfficeMerchantComplianceRequest;
import com.digicore.omni.data.lib.modules.common.enums.FeeConfigurationType;
import com.digicore.omni.data.lib.modules.common.exception.CommonExceptionProcessor;
import com.digicore.omni.data.lib.modules.merchant.dto.MerchantFulfillmentDTO;
import com.digicore.omni.data.lib.modules.merchant.dto.MerchantLiveApprovalDTO;
import com.digicore.omni.data.lib.modules.merchant.dto.MerchantProfileEditDTO;
import com.digicore.omni.data.lib.modules.merchant.enums.ComplianceStatus;
import com.digicore.omni.data.lib.modules.merchant.enums.MerchantStatus;
import com.digicore.omni.data.lib.modules.merchant.service.MerchantFeeConfigurationService;
import com.digicore.omni.root.lib.modules.backoffice.service.BackOfficeMerchantManagementService;


import com.digicore.omni.root.lib.modules.common.services.ReportGeneratorService;
import com.digicore.omni.root.services.modules.backoffice.merchant_management.service.BackOfficeFulfillmentEmailService;
import com.digicore.omni.root.services.modules.backoffice.merchant_management.service.MerchantManagementComplianceService;
import com.digicore.omni.root.services.modules.common.utils.CommonUtils;
import com.digicore.omni.root.services.modules.common.utils.DownloadHeadersUtils;
import com.digicore.request.processor.annotations.LogActivity;
import com.digicore.request.processor.annotations.TokenValid;
import com.digicore.request.processor.enums.LogActivityType;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author Monsuru <br/>
 * @since Sep-09(Fri)-2022
 */
@RestController
@RequestMapping("/api/v1/backoffice/merchant-management/process/")
@Slf4j
@RequiredArgsConstructor
public class BackOfficeMerchantManagementController {

    private final BackOfficeMerchantManagementService merchantManagementService;

    private final MerchantManagementComplianceService merchantManagementComplianceService;

    private final MerchantFeeConfigurationService merchantFeeConfigurationService;

    private final BackOfficeFulfillmentEmailService backOfficeFulfillmentEmailService;

    private final String pageAndSizeDefault = "1";

    @TokenValid()
    @GetMapping("get-all-merchants-details")
    public ResponseEntity<Object> getAllMerchants(@RequestParam(name = "pageNumber", defaultValue = "0") int pageNumber,
                                                  @RequestParam(name = "pageSize", defaultValue = "5") int pageSize) {
        return CommonUtils.buildSuccessResponse(merchantManagementService.getAllMerchants(pageNumber,pageSize));
    }

    @TokenValid()
    @GetMapping("get-merchant-{merchantId}-details")
    public ResponseEntity<Object> getMerchant(@PathVariable String merchantId)  {

            return CommonUtils.buildSuccessResponse(merchantManagementService.getMerchantById(merchantId));

    }

    @TokenValid()
    @GetMapping("filter-merchant")
    public ResponseEntity<Object> filterMerchants(@RequestParam Map<String, String> searchParams,
                                                  @RequestParam(name = "pageNumber", defaultValue = pageAndSizeDefault) Integer pageNumber,
                                                  @RequestParam(name = "pageSize", defaultValue = pageAndSizeDefault) Integer pageSize) {
        return CommonUtils.buildSuccessResponse(merchantManagementService.filterMerchants(searchParams, pageNumber, pageSize));
    }

    @TokenValid()
    @GetMapping("filter-merchants")
    public ResponseEntity<Object> filterMerchantsWithGenericFilter(@RequestParam String searchKey,
                                                                   @RequestParam(name = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
                                                                   @RequestParam(name = "pageSize", defaultValue = "10", required = false) Integer pageSize) {
        return CommonUtils.buildSuccessResponse(merchantManagementService.searchMerchantProfileByGenericFilter(searchKey, pageNumber, pageSize));
    }

    @TokenValid()
    @PatchMapping("send-merchant-compliance-for-approval")
    public ResponseEntity<Object> approveMerchantCompliance(@ModelAttribute BackOfficeMerchantComplianceRequest request)  {

            return CommonUtils.buildSuccessResponse(merchantManagementComplianceService.approveMerchantCompliance(request,null));

    }

    @TokenValid()
    @PatchMapping("decline-merchant-{merchantId}-compliance")
    public ResponseEntity<Object> declineMerchantCompliance(@PathVariable("merchantId") String merchantId)  {

            return CommonUtils.buildSuccessResponse(merchantManagementComplianceService.declineMerchantCompliance(merchantId));

    }

    @TokenValid()
    @GetMapping("download-in-pdf")
    public void downloadInPdf(@RequestParam Map<String, String> filterMap,
                              HttpServletResponse response) throws IOException {
        HttpServletResponse res = DownloadHeadersUtils.prepareDownload(response,"application/pdf","pdf", "merchants-profiles");
        merchantManagementService.exportAllMerchantAsPDFFile(res,filterMap);
    }

//    @TokenValid()
//    @GetMapping("download-in-csv")
//    @Deprecated
//    public void downloadInCSV(@RequestParam Map<String, String> filterMap,
//                              HttpServletResponse response) throws IOException {
//        HttpServletResponse res = DownloadHeadersUtils.prepareDownload(response,"text/csv","csv", "merchants-profiles");
//        merchantManagementService.exportAllMerchantAsCSV(res,filterMap);
//    }

    @TokenValid()
    @GetMapping("download-in-excel")
    public void downloadInExcel(@RequestParam Map<String, String> filterMap,
                              HttpServletResponse response) throws IOException {
        HttpServletResponse res = DownloadHeadersUtils.prepareDownload(response,"application/octet-stream","xlsx", "merchants-profiles");
        merchantManagementService.exportAllMerchantAsExcelFile(res,filterMap);
    }

    @TokenValid()
    @ResponseBody
    @GetMapping(value = "download-{merchantId}-document")
    public void downloadFile(@PathVariable String merchantId, @RequestParam String documentType, HttpServletResponse response)  {
        String pathToFile =  merchantManagementService.getPathToUploadedFile(merchantId, documentType);
        try (FileInputStream in = new FileInputStream(pathToFile)) {
            String originalFileName = new File(pathToFile).getName();
            String fileExtension = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);

            response.addHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", merchantId.concat("_").concat(documentType).concat(".").concat(fileExtension)));
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            IOUtils.copy(in, response.getOutputStream());
            response.flushBuffer();
        } catch ( IOException e) {
            throw CommonExceptionProcessor.genError(e.getMessage());
        }
    }

    @TokenValid()
    @PostMapping("update-merchant-fee-configuration")
    public ResponseEntity<Object> updateMerchantFeeConfig(@RequestParam("merchantId") String merchantId, @RequestBody BaseWebFeeConfiguration baseWebFeeConfiguration) {
        baseWebFeeConfiguration.setMerchantId(merchantId);
        merchantManagementComplianceService.updateMerchantFeeConfiguration(baseWebFeeConfiguration, null);
        return CommonUtils.buildSuccessResponse();
    }

    @TokenValid()
    @GetMapping("fetch-merchant-fee-configuration")
    public ResponseEntity<Object> getMerchantFeeConfig(@RequestParam("merchantId") String merchantId, @RequestParam("feeConfigurationType") FeeConfigurationType feeConfigurationType) {
        return CommonUtils.buildSuccessResponse(merchantFeeConfigurationService.getFeeConfiguration(merchantId,feeConfigurationType));
    }

    @TokenValid()
    @PostMapping("move-merchant-to-live-mode")
    public ResponseEntity<Object> moveMerchantToLiveMode(@RequestBody MerchantLiveApprovalDTO liveApprovalDTO)  {
        merchantManagementService.moveMerchantToLive(liveApprovalDTO);
        backOfficeFulfillmentEmailService.sendMerchantApprovalEmail(liveApprovalDTO.getMerchantId());
        return CommonUtils.buildSuccessResponse();
    }

    @TokenValid()
    @PostMapping("decline-merchant-approval")
    public ResponseEntity<Object> declineMerchantApproval(@RequestBody MerchantFulfillmentDTO declineDTO) {
        merchantManagementService.declineMerchantApproval(declineDTO);
        backOfficeFulfillmentEmailService.sendMerchantDeclineEmail(declineDTO.getMerchantId());
        backOfficeFulfillmentEmailService.sendFulfillmentAnalystMerchantDeclineEmail(declineDTO.getMerchantId());
            return CommonUtils.buildSuccessResponse();

    }


    @TokenValid()
    @GetMapping("filter-backoffice-merchants")
    public ResponseEntity<Object> filterBackOfficeMerchantProfile(@RequestParam(value = "complianceStatus", required = false) ComplianceStatus complianceStatus,
                                                                  @RequestParam(value = "merchantStatus", required = false) MerchantStatus merchantStatus,
                                                                  @RequestParam(value = "startDate", required = false) String startDate,
                                                                  @RequestParam(value = "endDate", required = false) String endDate,
                                                                  @RequestParam(name = "pageNumber", defaultValue = pageAndSizeDefault) Integer pageNumber,
                                                                  @RequestParam(name = "pageSize", defaultValue = pageAndSizeDefault) Integer pageSize) {
        return CommonUtils.buildSuccessResponse(merchantManagementService.filterAllBackOfficeMerchantProfile(pageNumber, pageSize, complianceStatus, merchantStatus, startDate, endDate));
    }

//    @TokenValid()
//    @GetMapping("download-in-csv")
//    public void downloadInCSV(@RequestParam(value = "complianceStatus", required = false) ComplianceStatus complianceStatus,
//                              @RequestParam(value = "merchantStatus", required = false) MerchantStatus merchantStatus,
//                              @RequestParam(value = "startDate", required = false) String startDate,
//                              @RequestParam(value = "endDate", required = false) String endDate,
//                              HttpServletResponse response) throws IOException {
//        HttpServletResponse res = DownloadHeadersUtils.prepareDownload(response,"text/csv","csv", "merchants-profiles");
//        merchantManagementService.exportAllMerchantProfilesAsCSV(res, complianceStatus, merchantStatus, startDate, endDate);
//    }

    @TokenValid()
    @GetMapping("download-in-csv")
    public ResponseEntity<Object> downloadInCSV(
            @RequestParam(value = "startDate") String startDate,
            @RequestParam(value = "endDate") String endDate,
            @RequestParam(value = "complianceStatus", required = false,defaultValue = "null") String complianceStatus,
            @RequestParam(value = "merchantStatus", required = false,defaultValue = "null") String merchantStatus) throws  ExecutionException, InterruptedException {


        CompletableFuture<ReportGeneratorService.ReportResponse> future =
                merchantManagementService.exportAllMerchant(complianceStatus,merchantStatus,startDate, endDate, ClientUtil.getLoggedInUsername());


        return CommonUtils.buildSuccessResponse(future.get());
    }


    @TokenValid()
    @LogActivity(activity = LogActivityType.UPDATE_ACTIVITY)
    @PatchMapping("edit-merchant-profile-details")
    public ResponseEntity<Object> editMerchantProfileDetails(@Valid @RequestBody MerchantProfileEditDTO merchantProfileEditDTO) {
        return CommonUtils.buildSuccessResponse(merchantManagementComplianceService.editMerchantProfile(merchantProfileEditDTO, null));
    }

}
