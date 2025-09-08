package com.digicore.omni.root.services.modules.common.controller;


import com.digicore.omni.data.lib.modules.common.exception.CommonExceptionProcessor;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author Oluwatobi Ogunwuyi
 * @createdOn Apr-24(Mon)-2023
 */

@Controller
public class LicenseController {

    @ResponseBody
    @GetMapping(value = "/download-license")
    public void downloadFile(HttpServletResponse response)  {
        String pathToFile =  "LICENSE.txt";
        try (FileInputStream in = new FileInputStream(pathToFile)) {
            String originalFileName = new File(pathToFile).getName();
            String fileExtension = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);

            response.addHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", "digi".concat("_").concat(".").concat(fileExtension)));
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            IOUtils.copy(in, response.getOutputStream());
            response.flushBuffer();
        } catch ( IOException e) {
            throw CommonExceptionProcessor.genError(e.getMessage());
        }
    }
}
