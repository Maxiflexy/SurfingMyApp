/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.backoffice.modules.user.service;

import com.digicore.omnexa.backoffice.modules.user.data.enums.BackOfficeUserStatus;
import com.digicore.omnexa.backoffice.modules.user.data.model.BackOfficeUser;
import com.digicore.omnexa.backoffice.modules.user.data.repository.BackOfficeUserRepository;
import com.digicore.omnexa.backoffice.modules.user.dto.request.InviteUserRequest;
import com.digicore.omnexa.backoffice.modules.user.dto.request.SignupRequest;
import com.digicore.omnexa.backoffice.modules.user.dto.request.UserListRequest;
import com.digicore.omnexa.backoffice.modules.user.dto.response.InviteUserResponse;
import com.digicore.omnexa.backoffice.modules.user.dto.response.SignupResponse;
import com.digicore.omnexa.backoffice.modules.user.dto.response.UserListResponse;
import com.digicore.omnexa.backoffice.modules.user.dto.response.UserSummary;
import com.digicore.omnexa.common.lib.api.ApiError;
import com.digicore.omnexa.common.lib.exception.OmnexaException;
import com.digicore.omnexa.common.lib.properties.MessagePropertyConfig;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    private static final int MAX_PAGE_SIZE = 16;
    private static final int DEFAULT_PAGE_SIZE = 16;

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

    /**
     * Retrieves paginated list of back office users with search and filter capabilities.
     *
     * @param pageNumber page number (1-based)
     * @param pageSize page size (max 16)
     * @param search search term for name or email
     * @param status filter by user status
     * @return paginated user list response
     */
    public UserListResponse getUserList(Integer pageNumber, Integer pageSize, String search, String status) {

        UserListRequest request = new UserListRequest();
        request.setPageNumber(pageNumber);
        request.setPageSize(pageSize);
        request.setSearch(search);
        request.setStatus(status);

        // Validate and sanitize page parameters
        int page_Number = Math.max(1, request.getPageNumber() != null ? request.getPageNumber() : 1);
        int page_Size = Math.min(MAX_PAGE_SIZE, Math.max(1, request.getPageSize() != null ? request.getPageSize() : DEFAULT_PAGE_SIZE));

        // Convert to 0-based page number for Spring Data
        int zeroBasedPageNumber = page_Number - 1;

        // Create pageable with sorting by creation date (newest first)
        Pageable pageable = PageRequest.of(zeroBasedPageNumber, page_Size, Sort.by(Sort.Direction.DESC, "createdDate"));

        // Prepare search and filter parameters
        String searchTerm = (request.getSearch() != null && !request.getSearch().trim().isEmpty())
                ? request.getSearch().trim() : null;

        BackOfficeUserStatus userStatus = null;
        if (request.getStatus() != null && !request.getStatus().trim().isEmpty()) {
            try {
                userStatus = BackOfficeUserStatus.valueOf(request.getStatus().trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                log.warn("Invalid user status provided: {}", request.getStatus());
                // Continue with null status (no filter)
            }
        }

        // Fetch paginated users based on search and filter criteria
        Page<BackOfficeUser> userPage = fetchUsers(searchTerm, userStatus, pageable);


        // Fetch paginated users
        //Page<BackOfficeUser> userPage = backOfficeUserRepository.findAll(pageable);

        // Convert to user summary DTOs
        List<UserSummary> userSummaries = userPage.getContent().stream()
                .map(this::convertToUserSummary)
                .toList();

        // Build response
        return UserListResponse.builder()
                .content(userSummaries)
                .currentPage(page_Number)
                .totalItems(userPage.getTotalElements())
                .totalPages(userPage.getTotalPages())
                .isFirstPage(userPage.isFirst())
                .isLastPage(userPage.isLast())
                .build();
    }

    /**
     * Converts BackOfficeUser entity to UserSummary DTO.
     *
     * @param user the user entity
     * @return user summary DTO
     */
    private UserSummary convertToUserSummary(BackOfficeUser user) {
        String username = buildUsername(user.getFirstName(), user.getLastName());

        return UserSummary.builder()
                .userId(user.getUserId())
                .username(username)
                .email(user.getEmail())
                .onboard_date(user.getCreatedDate())
                .user_status(user.getStatus())
                .build();
    }

    /**
     * Builds username from first name and last name.
     *
     * @param firstName user's first name
     * @param lastName user's last name
     * @return formatted username
     */
    private String buildUsername(String firstName, String lastName) {
        if (firstName == null && lastName == null) {
            return "";
        }
        if (firstName == null) {
            return lastName.trim();
        }
        if (lastName == null) {
            return firstName.trim();
        }
        return (firstName.trim() + " " + lastName.trim()).trim();
    }

    /**
     * Fetches users based on search and filter criteria.
     *
     * @param searchTerm search term for name or email
     * @param status user status filter
     * @param pageable pagination information
     * @return paginated users matching criteria
     */
    private Page<BackOfficeUser> fetchUsers(String searchTerm, BackOfficeUserStatus status, Pageable pageable) {
        if (searchTerm != null && status != null) {
            // Both search and filter
            return backOfficeUserRepository.findUsersWithSearchAndFilter(searchTerm, status, pageable);
        } else if (searchTerm != null) {
            // Search only
            return backOfficeUserRepository.findUsersBySearch(searchTerm, pageable);
        } else if (status != null) {
            // Filter only
            return backOfficeUserRepository.findByStatus(status, pageable);
        } else {
            // No search or filter
            return backOfficeUserRepository.findAll(pageable);
        }
    }
}