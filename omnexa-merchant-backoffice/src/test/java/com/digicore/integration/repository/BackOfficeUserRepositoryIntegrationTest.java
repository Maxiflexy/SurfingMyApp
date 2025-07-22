/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.integration.repository;

import com.digicore.omnexa.backoffice.modules.user.data.enums.BackOfficeUserStatus;
import com.digicore.omnexa.backoffice.modules.user.data.model.BackOfficeUser;
import com.digicore.omnexa.backoffice.modules.user.data.repository.BackOfficeUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for BackOfficeUserRepository using H2 database.
 *
 * @author Test Framework
 * @createdOn Jan-26(Sun)-2025
 */
@DataJpaTest
@ActiveProfiles("test")
@DisplayName("BackOfficeUserRepository Integration Tests")
class BackOfficeUserRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BackOfficeUserRepository backOfficeUserRepository;

    private BackOfficeUser testUser1;
    private BackOfficeUser testUser2;
    private BackOfficeUser testUser3;

    @BeforeEach
    void setUp() {
        // Create test users
        testUser1 = createTestUser("user1@example.com", "John", "Doe", BackOfficeUserStatus.ACTIVE);
        testUser2 = createTestUser("user2@example.com", "Jane", "Smith", BackOfficeUserStatus.INACTIVE);
        testUser3 = createTestUser("user3@example.com", "Bob", "Johnson", BackOfficeUserStatus.SUSPENDED);

        // Persist test users
        entityManager.persistAndFlush(testUser1);
        entityManager.persistAndFlush(testUser2);
        entityManager.persistAndFlush(testUser3);
    }

    @Test
    @DisplayName("Should find user by email")
    void shouldFindUserByEmail() {
        // When
        Optional<BackOfficeUser> result = backOfficeUserRepository.findByEmail("user1@example.com");

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getEmail()).isEqualTo("user1@example.com");
        assertThat(result.get().getFirstName()).isEqualTo("John");
        assertThat(result.get().getLastName()).isEqualTo("Doe");
    }

    @Test
    @DisplayName("Should return empty when user not found by email")
    void shouldReturnEmptyWhenUserNotFoundByEmail() {
        // When
        Optional<BackOfficeUser> result = backOfficeUserRepository.findByEmail("nonexistent@example.com");

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Should check if user exists by email")
    void shouldCheckIfUserExistsByEmail() {
        // When & Then
        assertThat(backOfficeUserRepository.existsByEmail("user1@example.com")).isTrue();
        assertThat(backOfficeUserRepository.existsByEmail("nonexistent@example.com")).isFalse();
    }

    @Test
    @DisplayName("Should find user by userId")
    void shouldFindUserByUserId() {
        // When
        Optional<BackOfficeUser> result = backOfficeUserRepository.findByUserId(testUser1.getUserId());

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getUserId()).isEqualTo(testUser1.getUserId());
    }

    @Test
    @DisplayName("Should find users by status")
    void shouldFindUsersByStatus() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);

        // When
        Page<BackOfficeUser> result = backOfficeUserRepository.findByStatus(BackOfficeUserStatus.ACTIVE, pageable);

        // Then
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getStatus()).isEqualTo(BackOfficeUserStatus.ACTIVE);
        assertThat(result.getContent().get(0).getEmail()).isEqualTo("user1@example.com");
    }

    @Test
    @DisplayName("Should find users by search term")
    void shouldFindUsersBySearchTerm() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);

        // When - search by first name
        Page<BackOfficeUser> result1 = backOfficeUserRepository.findUsersBySearch("John", pageable);

        // Then
        assertThat(result1.getContent()).hasSize(1);
        assertThat(result1.getContent().get(0).getFirstName()).isEqualTo("John");

        // When - search by email
        Page<BackOfficeUser> result2 = backOfficeUserRepository.findUsersBySearch("user2", pageable);

        // Then
        assertThat(result2.getContent()).hasSize(1);
        assertThat(result2.getContent().get(0).getEmail()).contains("user2");

        // When - search by last name
        Page<BackOfficeUser> result3 = backOfficeUserRepository.findUsersBySearch("Johnson", pageable);

        // Then
        assertThat(result3.getContent()).hasSize(1);
        assertThat(result3.getContent().get(0).getLastName()).isEqualTo("Johnson");
    }

    @Test
    @DisplayName("Should find users with search and filter")
    void shouldFindUsersWithSearchAndFilter() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);

        // When - search and filter
        Page<BackOfficeUser> result = backOfficeUserRepository.findUsersWithSearchAndFilter(
                "John", BackOfficeUserStatus.ACTIVE, pageable);

        // Then
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getFirstName()).isEqualTo("John");
        assertThat(result.getContent().get(0).getStatus()).isEqualTo(BackOfficeUserStatus.ACTIVE);
    }

    @Test
    @DisplayName("Should return empty result when search and filter don't match")
    void shouldReturnEmptyResultWhenSearchAndFilterDontMatch() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);

        // When - search for John but filter by INACTIVE status
        Page<BackOfficeUser> result = backOfficeUserRepository.findUsersWithSearchAndFilter(
                "John", BackOfficeUserStatus.INACTIVE, pageable);

        // Then
        assertThat(result.getContent()).isEmpty();
    }

    @Test
    @DisplayName("Should handle case insensitive search")
    void shouldHandleCaseInsensitiveSearch() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);

        // When - search with different cases
        Page<BackOfficeUser> result1 = backOfficeUserRepository.findUsersBySearch("JOHN", pageable);
        Page<BackOfficeUser> result2 = backOfficeUserRepository.findUsersBySearch("john", pageable);
        Page<BackOfficeUser> result3 = backOfficeUserRepository.findUsersBySearch("JoHn", pageable);

        // Then
        assertThat(result1.getContent()).hasSize(1);
        assertThat(result2.getContent()).hasSize(1);
        assertThat(result3.getContent()).hasSize(1);
    }

    @Test
    @DisplayName("Should generate userId on persist")
    void shouldGenerateUserIdOnPersist() {
        // Given
        BackOfficeUser newUser = createTestUser("newuser@example.com", "New", "User", BackOfficeUserStatus.ACTIVE);
        newUser.setUserId(null); // Ensure userId is null before persist

        // When
        BackOfficeUser savedUser = entityManager.persistAndFlush(newUser);

        // Then
        assertThat(savedUser.getUserId()).isNotNull();
        assertThat(savedUser.getUserId()).startsWith("AH");
        assertThat(savedUser.getUserId()).hasSize(10); // AH + 8 digits
    }

    @Test
    @DisplayName("Should maintain userId if already set")
    void shouldMaintainUserIdIfAlreadySet() {
        // Given
        String predefinedUserId = "AH99999999";
        BackOfficeUser newUser = createTestUser("another@example.com", "Another", "User", BackOfficeUserStatus.ACTIVE);
        newUser.setUserId(predefinedUserId);

        // When
        BackOfficeUser savedUser = entityManager.persistAndFlush(newUser);

        // Then
        assertThat(savedUser.getUserId()).isEqualTo(predefinedUserId);
    }

    @Test
    @DisplayName("Should handle pagination correctly")
    void shouldHandlePaginationCorrectly() {
        // Given
        Pageable firstPage = PageRequest.of(0, 2);
        Pageable secondPage = PageRequest.of(1, 2);

        // When
        Page<BackOfficeUser> page1 = backOfficeUserRepository.findAll(firstPage);
        Page<BackOfficeUser> page2 = backOfficeUserRepository.findAll(secondPage);

        // Then
        assertThat(page1.getContent()).hasSize(2);
        assertThat(page1.getTotalElements()).isEqualTo(3);
        assertThat(page1.getTotalPages()).isEqualTo(2);
        assertThat(page1.isFirst()).isTrue();
        assertThat(page1.isLast()).isFalse();

        assertThat(page2.getContent()).hasSize(1);
        assertThat(page2.getTotalElements()).isEqualTo(3);
        assertThat(page2.getTotalPages()).isEqualTo(2);
        assertThat(page2.isFirst()).isFalse();
        assertThat(page2.isLast()).isTrue();
    }

    private BackOfficeUser createTestUser(String email, String firstName, String lastName, BackOfficeUserStatus status) {
        BackOfficeUser user = new BackOfficeUser();
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setStatus(status);
        user.setCreatedBy("test");
        user.setLastModifiedBy("test");
        return user;
    }
}