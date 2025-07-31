/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.common.lib.authentication.helper;

import com.blueconic.browscap.*;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import org.springframework.stereotype.Service;

/*
 * @author Oluwatobi Ogunwuyi
 * @createdOn Mar-26(Wed)-2025
 */

@Service
public class DeviceDetectionHelper {
  private final UserAgentParser parser;

  public DeviceDetectionHelper() throws IOException, ParseException {
    // Initialize UserAgentParser
    this.parser =
        new UserAgentService()
            .loadParser(
                Arrays.asList(
                    BrowsCapField.BROWSER,
                    BrowsCapField.BROWSER_TYPE,
                    BrowsCapField.BROWSER_MAJOR_VERSION,
                    BrowsCapField.DEVICE_TYPE,
                    BrowsCapField.PLATFORM,
                    BrowsCapField.PLATFORM_VERSION));
  }

  public String getDeviceInfo(HttpServletRequest request) {
    String userAgent = request.getHeader("User-Agent");
    if (userAgent == null) {
      return "Unknown Device";
    }

    // Parse user-agent string
    Capabilities capabilities = parser.parse(userAgent);
    return String.format(
        "Browser: %s %s, Device: %s, OS: %s",
        capabilities.getBrowser(),
        capabilities.getBrowserMajorVersion(),
        capabilities.getDeviceType(),
        capabilities.getPlatform());
  }
}
