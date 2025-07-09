/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omxena.backoffice.user.facade;

import com.digicore.omnexa.backoffice.modules.user.dto.request.InviteUserRequest;
import com.digicore.omnexa.backoffice.modules.user.dto.request.SignupRequest;
import com.digicore.omnexa.backoffice.modules.user.dto.response.InviteUserResponse;
import com.digicore.omnexa.backoffice.modules.user.dto.response.SignupResponse;
import com.digicore.omnexa.backoffice.modules.user.service.BackOfficeUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Facade for back office user operations.
 *
 * <p>Provides a simplified interface for user-related operations
 * following the facade pattern.
 *
 * @author Onyekachi Ejemba
 * @createdOn Jul-08(Tue)-2025
 */
@Component
@RequiredArgsConstructor
public class BackOfficeUserFacade {

    private final BackOfficeUserService backOfficeUserService;

    /**
     * Processes user invitation request.
     *
     * @param request invitation request
     * @return invitation response
     */
    public InviteUserResponse inviteUser(InviteUserRequest request) {
        return backOfficeUserService.inviteUser(request);
    }

    /**
     * Processes user signup request.
     *
     * @param request signup request
     * @return signup response
     */
    public SignupResponse signup(SignupRequest request) {
        return backOfficeUserService.signup(request);
    }
}