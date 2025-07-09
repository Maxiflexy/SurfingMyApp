/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omxena.backoffice.user.service;

import com.digicore.omnexa.backoffice.modules.user.data.model.BackOfficeUser;
import com.digicore.omnexa.backoffice.modules.user.data.repository.BackOfficeUserRepository;
import com.digicore.omnexa.backoffice.modules.user.dto.request.InviteUserRequest;
import com.digicore.omnexa.backoffice.modules.user.dto.request.SignupRequest;
import com.digicore.omnexa.backoffice.modules.user.dto.response.InviteUserResponse;
import com.digicore.omnexa.backoffice.modules.user.dto.response.SignupResponse;
import com.digicore.omnexa.common.lib.api.ApiError;
import com.digicore.omnexa.common.lib.exception.OmnexaException;
import com.digicore.omnexa.common.lib.properties.MessagePropertyConfig;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static com.digicore.omnexa.common.lib.constant.message.MessageConstant.*;
import static com.digicore.omnexa.common.lib.constant.message.MessagePlaceHolderConstant.PROFILE;

/**
 * Service implementation for back office user operations.
 *
 * <p>Handles user invitation and signup processes including validation,
 * persistence, and email notifications.
 *
 * @author Onyekachi Ejemba
 * @createdOn Jul-08(Tue)-2025
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BackOfficeUserService {

    private final BackOfficeUserRepository backOfficeUserRepository;
    private final MessagePropertyConfig messagePropertyConfig;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$");

    /**
     * Invites a new user to the back office system.
     *
     * @param request invitation request containing email and first name
     * @return invitation response with user details
     * @throws OmnexaException if user already exists
     */
    @Transactional(rollbackOn = Exception.class)
    public InviteUserResponse inviteUser(InviteUserRequest request) {
        // Check if user already exists
        if (backOfficeUserRepository.existsByEmail(request.getEmail())) {
            String message = messagePropertyConfig.getOnboardMessage(DUPLICATE)
                    .replace(PROFILE, request.getEmail());
            log.error(message);
            throw new OmnexaException(message, HttpStatus.BAD_REQUEST);
        }

        // Create new user with invitation status
        BackOfficeUser user = new BackOfficeUser();
        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setCreatedBy("system");

        BackOfficeUser savedUser = backOfficeUserRepository.save(user);

        // Send invitation email
        emailService.sendInvitationEmail(request.getEmail(), request.getFirstName());

        return InviteUserResponse.builder()
                .email(savedUser.getEmail())
                .userId(savedUser.getUserId())
                .message("Invitation sent successfully to " + request.getEmail())
                .build();
    }

    /**
     * Completes user signup process.
     *
     * @param request signup request with user details
     * @return signup response with created user information
     * @throws OmnexaException if validation fails or user not found
     */
    @Transactional(rollbackOn = Exception.class)
    public SignupResponse signup(SignupRequest request) {
        List<ApiError> errors = validateSignupRequest(request);
        if (!errors.isEmpty()) {
            throw new OmnexaException(
                    messagePropertyConfig.getOnboardMessage(INVALID),
                    HttpStatus.BAD_REQUEST,
                    errors);
        }

        // Find invited user
        BackOfficeUser user = backOfficeUserRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> {
                    String message = messagePropertyConfig.getOnboardMessage(NOT_FOUND)
                            .replace(PROFILE, request.getEmail());
                    return new OmnexaException(message, HttpStatus.NOT_FOUND);
                });

        // Update user details
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setLastModifiedBy("system");

        BackOfficeUser savedUser = backOfficeUserRepository.save(user);

        return SignupResponse.builder()
                .userId(savedUser.getUserId())
                .email(savedUser.getEmail())
                .fullName(savedUser.getFirstName() + " " + savedUser.getLastName())
                .status(savedUser.getStatus())
                .message("Signup completed successfully. Your account is pending activation.")
                .build();
    }

    /**
     * Validates signup request parameters.
     *
     * @param request signup request to validate
     * @return list of validation errors
     */
    private List<ApiError> validateSignupRequest(SignupRequest request) {
        List<ApiError> errors = new ArrayList<>();

        // Validate password match
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            errors.add(new ApiError("Password and confirm password do not match"));
        }

        // Validate password complexity
        if (!PASSWORD_PATTERN.matcher(request.getPassword()).matches()) {
            errors.add(new ApiError(
                    "Password must be at least 8 characters long and contain at least one uppercase letter, " +
                            "one lowercase letter, one number, and one special character"
            ));
        }

        return errors;
    }
}