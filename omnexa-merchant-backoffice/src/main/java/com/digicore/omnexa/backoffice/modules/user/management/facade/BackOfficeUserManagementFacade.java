/// *
// * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
// * Unauthorized use or distribution is strictly prohibited.
// * For details, see the LICENSE file.
// */
//
// package com.digicore.omnexa.backoffice.modules.user.management.facade;
//
// import com.digicore.omnexa.backoffice.modules.user.profile.dto.response.PaginatedUserResponse;
// import com.digicore.omnexa.common.lib.profile.contract.ProfileService;
// import lombok.RequiredArgsConstructor;
// import lombok.extern.slf4j.Slf4j;
// import org.springframework.stereotype.Component;
//
/// **
// * Facade for back office user management operations.
// *
// * <p>Coordinates between the controller layer and service layer for user management operations.
// * This facade follows the Single Responsibility Principle by handling specific user retrieval
// * scenarios and delegates to the ProfileService contract.
// *
// * <p>Benefits of using this facade:
// * - Provides a clean interface for user management operations
// * - Encapsulates business logic coordination
// * - Follows established architectural patterns
// * - Enables easy testing and mocking
// *
// * <p>Author: Onyekachi Ejemba
// * Created On: Jul-29(Tue)-2025
// */
// @Component
// @RequiredArgsConstructor
// @Slf4j
// public class BackOfficeUserManagementFacade {
//
//    private final ProfileService backOfficeUserProfileService;
//
//    /**
//     * Retrieves all users with pagination only.
//     *
//     * <p>This method delegates to the ProfileService to get all user profiles
//     * with pagination support. It provides a clean interface for the controller layer.
//     *
//     * @param pageNumber the page number (1-based, optional)
//     * @param pageSize the page size (max 16, optional)
//     * @return paginated user response
//     */
//    public PaginatedUserResponse getAllUsers(Integer pageNumber, Integer pageSize) {
//        log.debug("Facade: Retrieving all users with pagination: page={}, size={}", pageNumber,
// pageSize);
//        return backOfficeUserProfileService.getAllProfiles(pageNumber, pageSize);
//    }
//
//    /**
//     * Retrieves users filtered by search term with pagination.
//     *
//     * <p>This method delegates to the ProfileService to search user profiles
//     * by firstName, lastName, or email with pagination support.
//     *
//     * @param search the search term (required)
//     * @param pageNumber the page number (1-based, optional)
//     * @param pageSize the page size (max 16, optional)
//     * @return paginated user response filtered by search term
//     */
//    public PaginatedUserResponse searchUsers(String search, Integer pageNumber, Integer pageSize)
// {
//        log.debug("Facade: Searching users with term='{}', page={}, size={}", search, pageNumber,
// pageSize);
//        return backOfficeUserProfileService.searchProfiles(search, pageNumber, pageSize);
//    }
//
//    /**
//     * Retrieves users filtered by profile status with pagination.
//     *
//     * <p>This method delegates to the ProfileService to get user profiles
//     * filtered by profile status with pagination support.
//     *
//     * @param profileStatus the profile status (required)
//     * @param pageNumber the page number (1-based, optional)
//     * @param pageSize the page size (max 16, optional)
//     * @return paginated user response filtered by profile status
//     */
//    public PaginatedUserResponse getUsersByStatus(String profileStatus, Integer pageNumber,
// Integer pageSize) {
//        log.debug("Facade: Retrieving users with status='{}', page={}, size={}", profileStatus,
// pageNumber, pageSize);
//        return backOfficeUserProfileService.getProfilesByStatus(profileStatus, pageNumber,
// pageSize);
//    }
// }