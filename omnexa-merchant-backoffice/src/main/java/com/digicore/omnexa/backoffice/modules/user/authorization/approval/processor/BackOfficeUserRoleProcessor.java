/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.backoffice.modules.user.authorization.approval.processor;

import com.digicore.omnexa.backoffice.modules.user.authorization.approval.BackOfficeUserRoleProxyService;
import com.digicore.omnexa.common.lib.approval.workflow.annotation.RequestHandler;
import com.digicore.omnexa.common.lib.approval.workflow.annotation.RequestType;
import com.digicore.omnexa.common.lib.approval.workflow.constant.RequestHandlerType;
import lombok.RequiredArgsConstructor;

/*
 * @author Daudu John
 * @createdOn Mar-12(Wed)-2025
 */

@RequestHandler(type = RequestHandlerType.PROCESS_MAKER_REQUESTS)
@RequiredArgsConstructor
public class BackOfficeUserRoleProcessor {
  private final BackOfficeUserRoleProxyService customerUserRoleOperations;

  @RequestType(name = "createRole")
  public Object createRole(Object approvalDecisionDTO) {
    return customerUserRoleOperations.createRole(null, approvalDecisionDTO);
  }

  @RequestType(name = "deleteRole")
  public Object deleteRole(Object approvalDecisionDTO) {
    return customerUserRoleOperations.deleteRole(null, approvalDecisionDTO);
  }

  @RequestType(name = "enableRole")
  public Object enableRole(Object approvalDecisionDTO) {
    return customerUserRoleOperations.enableRole(null, approvalDecisionDTO);
  }

  @RequestType(name = "disableRole")
  public Object disableRole(Object approvalDecisionDTO) {
    return customerUserRoleOperations.disableRole(null, approvalDecisionDTO);
  }

  @RequestType(name = "editRole")
  public Object editRole(Object approvalDecisionDTO) {
    return customerUserRoleOperations.editRole(null, approvalDecisionDTO);
  }
}
