///*
// * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
// * Unauthorized use or distribution is strictly prohibited.
// * For details, see the LICENSE file.
// */
//
//package com.digicore.omnexa.backoffice.modules.user.data.repository;
//
//import com.digicore.omnexa.backoffice.modules.user.data.enums.BackOfficeUserStatus;
//import com.digicore.omnexa.backoffice.modules.user.data.model.BackOfficeUser;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//
//import java.util.Optional;
//
///**
// * Repository interface for BackOfficeUser entity operations.
// *
// * <p>Provides database operations for back office users including
// * finding users by email and checking existence.
// *
// * @author Onyekachi Ejemba
// * @createdOn Jul-08(Tue)-2025
// */
//public interface BackOfficeUserRepository extends JpaRepository<BackOfficeUser, Long> {
//
//    /**
//     * Finds a back office user by email.
//     *
//     * @param email the email to search for
//     * @return an Optional containing the user if found
//     */
//    Optional<BackOfficeUser> findByEmail(String email);
//
//    /**
//     * Checks if a user exists with the given email.
//     *
//     * @param email the email to check
//     * @return true if user exists, false otherwise
//     */
//    boolean existsByEmail(String email);
//
//    /**
//     * Finds a back office user by userId.
//     *
//     * @param userId the userId to search for
//     * @return an Optional containing the user if found
//     */
//    Optional<BackOfficeUser> findByUserId(String userId);
//
//    /**
//     * Finds users with search and filter capabilities.
//     *
//     * @param searchTerm search term for name or email (can be null)
//     * @param status user status filter (can be null)
//     * @param pageable pagination information
//     * @return paginated users matching criteria
//     */
//    @Query("SELECT u FROM BackOfficeUser u WHERE " +
//            "(:searchTerm IS NULL OR :searchTerm = '' OR " +
//            "LOWER(CONCAT(COALESCE(u.firstName, ''), ' ', COALESCE(u.lastName, ''))) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
//            "LOWER(u.email) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) AND " +
//            "(:status IS NULL OR u.status = :status)")
//    Page<BackOfficeUser> findUsersWithSearchAndFilter(
//            @Param("searchTerm") String searchTerm,
//            @Param("status") BackOfficeUserStatus status,
//            Pageable pageable);
//
//    /**
//     * Finds users with search capability only.
//     *
//     * @param searchTerm search term for name or email
//     * @param pageable pagination information
//     * @return paginated users matching search criteria
//     */
//    @Query("SELECT u FROM BackOfficeUser u WHERE " +
//            "LOWER(CONCAT(COALESCE(u.firstName, ''), ' ', COALESCE(u.lastName, ''))) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
//            "LOWER(u.email) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
//    Page<BackOfficeUser> findUsersBySearch(
//            @Param("searchTerm") String searchTerm,
//            Pageable pageable);
//
//    /**
//     * Finds users with status filter only.
//     *
//     * @param status user status filter
//     * @param pageable pagination information
//     * @return paginated users matching status criteria
//     */
//    Page<BackOfficeUser> findByStatus(BackOfficeUserStatus status, Pageable pageable);
//
//}