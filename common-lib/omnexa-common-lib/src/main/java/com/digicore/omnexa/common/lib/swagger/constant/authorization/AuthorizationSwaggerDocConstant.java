/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.common.lib.swagger.constant.authorization;

/**
 * Swagger documentation constants for authorization/role management operations.
 *
 * <p>This class contains all the constant values used for Swagger API documentation in the
 * authorization module, specifically for role management operations.
 *
 * @author Onyekachi Ejemba
 * @createdOn Aug-04(Mon)-2025
 */
public class AuthorizationSwaggerDocConstant {

  // API Path Constants
  public static final String ROLES_API = "roles/";

  // Controller Constants
  public static final String ROLE_MANAGEMENT_CONTROLLER_TITLE = "Role Management";
  public static final String ROLE_MANAGEMENT_CONTROLLER_DESCRIPTION =
      "Endpoints for managing back office user roles";

  public static final String GET_ROLE_BY_ID_TITLE = "Retrieve a specific role";
  public static final String GET_ROLE_BY_ID_DESCRIPTION =
      "Retrieves detailed information about a specific role including name, status, description, and permissions.";

  public static final String GET_ALL_ROLES_TITLE = "Retrieve all roles";
  public static final String GET_ALL_ROLES_DESCRIPTION =
      "Retrieves a paginated list of all roles with their details including name, status, description, and permissions.";

  public static final String CREATE_ROLE_TITLE = "Create a new role";
  public static final String CREATE_ROLE_DESCRIPTION =
      "Creates a new role with specified permissions. Role name must not be a system reserved role (CUSTODIAN, AUTHORIZER, INITIATOR) and must be unique.";

  public static final String UPDATE_ROLE_TITLE = "Update an existing role";
  public static final String UPDATE_ROLE_DESCRIPTION =
      "Updates an existing role's details and permissions. System reserved roles (CUSTODIAN, AUTHORIZER, INITIATOR) cannot be updated.";

  public static final String ACTIVATE_ROLE_TITLE = "Activate a role";
  public static final String ACTIVATE_ROLE_DESCRIPTION =
      "Activates a deactivated role, making it available for assignment to users.";

  public static final String DEACTIVATE_ROLE_TITLE = "Deactivate a role";
  public static final String DEACTIVATE_ROLE_DESCRIPTION =
      "Deactivates a role, preventing it from being assigned to new users. System reserved roles cannot be deactivated.";

  public static final String DELETE_ROLE_TITLE = "Delete a role";
  public static final String DELETE_ROLE_DESCRIPTION =
      "Deletes a role from the system. System reserved roles cannot be deleted. Only roles not currently assigned to users can be deleted.";

  public static final String ROLE_NAME_PARAMETER_DESCRIPTION = "The name of the role";
  public static final String PAGE_NUMBER_PARAMETER_DESCRIPTION = "Page number (1-based)";
  public static final String PAGE_SIZE_PARAMETER_DESCRIPTION = "Number of roles per page (max 15)";

  public static final String ROLE_ID_EXAMPLE = "1";
  public static final String PAGE_NUMBER_EXAMPLE = "1";
  public static final String PAGE_SIZE_EXAMPLE = "15";
  public static final String ROLE_NAME_EXAMPLE = "CUSTODIAN";
}
