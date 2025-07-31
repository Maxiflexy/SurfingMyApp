/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.common.lib.authentication.contract;

import com.digicore.omnexa.common.lib.exception.OmnexaException;

/**
 * Service interface for authenticating users.
 *
 * <p>This interface defines the contract for user authentication services, including methods for
 * validating credentials and issuing authentication responses such as tokens or session
 * information.
 *
 * <p>Implementations of this interface should handle the authentication logic and provide
 * appropriate responses or exceptions in case of errors.
 *
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jun-24(Tue)-2025
 */
public interface AuthenticationService {

  /**
   * Authenticates a user with the provided credentials.
   *
   * <p>This method validates the login request and returns an authentication response if the
   * credentials are valid. If the credentials are invalid or the account is restricted, an {@link
   * OmnexaException} is thrown.
   *
   * @param request The login request containing user credentials.
   * @return The login response containing authentication details.
   * @throws OmnexaException If the credentials are invalid or the account is restricted.
   */
  AuthenticationResponse authenticate(AuthenticationRequest request);
}
