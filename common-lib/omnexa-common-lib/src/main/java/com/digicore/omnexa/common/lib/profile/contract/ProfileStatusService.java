/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.common.lib.profile.contract;

/**
 * Service interface for profile status management.
 *
 * <p>Handles enabling, disabling, and status checks of a profile.
 *
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jun-24(Tue)-2025
 */
public interface ProfileStatusService {

  /**
   * Enables a profile.
   *
   * @param profileId the ID of the profile to enable.
   */
  void enableProfile(String profileId);

  /**
   * Disables a profile.
   *
   * @param profileId the ID of the profile to disable.
   */
  void disableProfile(String profileId);

  /**
   * Checks whether a profile exists.
   *
   * @param profileId the ID of the profile.
   * @return true if the profile exists; false otherwise.
   */
  default boolean existsByProfileId(String profileId) {
    return false;
  }

  /**
   * Checks whether a profile exists.
   *
   * @param email the email of the profile.
   * @return true if the profile exists; false otherwise.
   */
  default boolean existsByEmail(String email) {
    return false;
  }

  /**
   * Checks whether a profile is currently active.
   *
   * @param email the email of the profile.
   * @return true if the profile is active; false otherwise.
   */
  default boolean isActiveByEmail(String email) {
    return false;
  }

  /**
   * Checks whether a profile is currently active.
   *
   * @param profileId the ID of the profile.
   * @return true if the profile is active; false otherwise.
   */
  default boolean isActiveByProfileId(String profileId) {
    return false;
  }
}
