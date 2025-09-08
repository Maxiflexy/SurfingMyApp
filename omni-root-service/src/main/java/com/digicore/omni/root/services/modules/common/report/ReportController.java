package com.digicore.omni.root.services.modules.common.report;
/*
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jan-25(Thu)-2024
 */

import com.digicore.common.util.ClientUtil;
import com.digicore.omni.data.lib.modules.common.exception.CommonExceptionProcessor;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.FileInputStream;
import java.io.IOException;

import static com.digicore.omni.root.lib.modules.common.services.ReportGeneratorService.extractUsername;

@RestController
@RequestMapping("/api/v1/report/")
@Slf4j
@RequiredArgsConstructor
public class ReportController {

    @Value("${digicore.file.upload.directory:/digicore}")
    private String fileUploadDirectory;

    @ResponseBody
    @GetMapping(value = "download/{csvFileName}")
    public void downloadFile(@PathVariable String csvFileName, HttpServletResponse response) {
        String extractedEmail = extractUsername(ClientUtil.getLoggedInUsername());
        String pathToFile = fileUploadDirectory.concat("reports/").concat(extractedEmail.concat("/")).concat(csvFileName.concat(".csv"));
        try (FileInputStream in = new FileInputStream(pathToFile)) {
            response.addHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", csvFileName.concat(".csv")));
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            IOUtils.copy(in, response.getOutputStream());
            response.flushBuffer();
        } catch (IOException e) {
            throw CommonExceptionProcessor.genError(e.getMessage());
        }
    }

    @ResponseBody
    @GetMapping(value = "download-zipped/{zipFileName}")
    public void downloadZipFile(@PathVariable String zipFileName, HttpServletResponse response) {
        String extractedEmail = extractUsername(zipFileName);
        int indexOfHyphen = zipFileName.lastIndexOf("_");
        String originalZipFileName = zipFileName.substring(indexOfHyphen + 1);
        String pathToFile = fileUploadDirectory.concat("reports/").concat(extractedEmail.concat("/")).concat(originalZipFileName.concat(".zip"));
        try (FileInputStream in = new FileInputStream(pathToFile)) {
            response.addHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", originalZipFileName.concat(".zip")));
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            IOUtils.copy(in, response.getOutputStream());
            response.flushBuffer();
        } catch (IOException e) {
            throw CommonExceptionProcessor.genError(e.getMessage());
        }
    }


}