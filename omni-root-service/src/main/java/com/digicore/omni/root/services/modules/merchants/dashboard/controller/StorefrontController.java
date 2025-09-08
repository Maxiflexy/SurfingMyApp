package com.digicore.omni.root.services.modules.merchants.dashboard.controller;


import com.digicore.omni.data.lib.modules.common.exception.CommonExceptionProcessor;
import com.digicore.omni.root.services.modules.merchants.dashboard.service.DashBoardService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@RestController
@RequestMapping("/storefront/")
@RequiredArgsConstructor
public class StorefrontController {

    private final DashBoardService dashBoardService;

    @GetMapping("get-profile-picture")
    public void getMerchantProfilePicture(@RequestParam String merchantId, HttpServletResponse response) {
        String pathToFile = dashBoardService.getMerchantPathToUploadFile(merchantId);
        try (FileInputStream in = new FileInputStream(pathToFile)) {
            String originalFileName = new File(pathToFile).getName();
            String fileExtension = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);

            response.addHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", merchantId.concat("_").concat("Logo").concat(".").concat(fileExtension)));
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            IOUtils.copy(in, response.getOutputStream());
            response.flushBuffer();
        } catch (IOException e) {
            throw CommonExceptionProcessor.genError(e.getMessage());
        }
    }
}
