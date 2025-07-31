/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.common.lib.authorization.dto.request;

import com.digicore.omnexa.common.lib.authorization.contract.AuthorizationRequest;
import com.digicore.omnexa.common.lib.authorization.dto.response.BaseRoleDTO;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jul-21(Mon)-2025
 */
@Getter
@Setter
@ToString
public class RoleCreationDTO extends BaseRoleDTO implements AuthorizationRequest, Serializable {
  private Set<String> permissions = new HashSet<>();
}
