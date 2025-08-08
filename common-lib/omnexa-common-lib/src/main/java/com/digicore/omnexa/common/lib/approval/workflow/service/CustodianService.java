/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.common.lib.approval.workflow.service;

import com.digicore.omnexa.common.lib.approval.workflow.annotation.MakerChecker;
import com.digicore.omnexa.common.lib.audit.service.OmnexaAuditLogService;
import com.digicore.omnexa.common.lib.properties.MessagePropertyConfig;
import com.digicore.omnexa.common.lib.util.RequestUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Component;

/*
 * @author Oluwatobi Ogunwuyi
 * @createdOn Mar-13(Thu)-2025
 */

@Component
@Slf4j
@RequiredArgsConstructor
@Transactional
public class CustodianService {
  private final OmnexaAuditLogService regulatoryLoggingService;
  private final MessagePropertyConfig messagePropertyConfig;

  public Object process(ProceedingJoinPoint joinPoint, MakerChecker makerChecker) throws Throwable {
    log.info("<<< custodian triggered action >>>");
    String requestData = RequestUtil.getGsonMapper().toJson(joinPoint.getArgs()[1]);
    Object response = joinPoint.proceed();
    regulatoryLoggingService.log(
        makerChecker.activity(),
        makerChecker.module(),
        messagePropertyConfig.getAuditMessage(makerChecker.activity()),
        requestData);
    return response;
  }
}
