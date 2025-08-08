/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.common.lib.properties;

import com.digicore.omnexa.common.lib.approval.rule.dto.ActivityTypeDTO;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/*
 * @author Oluwatobi Ogunwuyi
 * @createdOn Feb-10(Mon)-2025
 */

@Component
@ConfigurationProperties(prefix = "omnexa.feature")
@Getter
@Setter
public class FeaturePropertyConfig {
  private List<ActivityTypeDTO.Module> modules;
  private String logo;
  private String primaryColor;
  private String secondaryColor;
}
