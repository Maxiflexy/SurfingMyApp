/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.common.lib.approval.rule.dto;

import java.util.List;
import lombok.*;

/*
 * @author Oluwatobi Ogunwuyi
 * @createdOn Feb-10(Mon)-2025
 */

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ActivityTypeDTO {
  private List<Module> modules;

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Module {
    private String name;
    private boolean supportThresholdConfiguration = false;
    private List<String> activities;
  }
}
