package com.digicore.omni.root.services.modules.common.utils;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public final class DownloadHeadersUtils {

    public static HttpServletResponse prepareDownload(HttpServletResponse response, String contentType, String extension, String fileNamePrefix){
        response.setContentType(contentType);
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=" + fileNamePrefix + "_" + currentDateTime + "." + extension;;
        response.setHeader(headerKey, headerValue);
        return response;
    }
}
