/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.backoffice.modules.user.authorization.approval;

/*
 * @author Daudu John
 * @createdOn Mar-12(Wed)-2025
 */

public interface BackOfficeUserRoleProxyService {

  Object createRole(Object initialData, Object updateData, Object... files);

  Object deleteRole(Object initialData, Object updateData, Object... files);

  Object editRole(Object initialData, Object updateData, Object... files);

  Object disableRole(Object initialData, Object updateData, Object... files);

  Object enableRole(Object initialData, Object updateData, Object... files);
}
