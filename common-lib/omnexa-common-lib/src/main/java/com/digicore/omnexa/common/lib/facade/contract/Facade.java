/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.common.lib.facade.contract;

import java.util.Optional;

/**
 * Generic interface for facade operations.
 *
 * <p>This interface defines the contract for facade implementations that process requests and
 * return responses. It provides methods for processing a request and retrieving the type of the
 * facade.
 *
 * <p>Implementations of this interface should handle the specific logic for processing requests and
 * determining the facade type.
 *
 * @param <T> The type of the request object.
 * @param <R> The type of the response object.
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jul-03(Thu)-2025
 */
public interface Facade<T, R> {

  /**
   * Processes a request and returns an optional response.
   *
   * <p>This method takes a request object, processes it, and returns an {@link Optional} containing
   * the response. If the processing fails or no response is generated, an empty {@link Optional} is
   * returned.
   *
   * @param request The request object to be processed.
   * @return An {@link Optional} containing the response object, or empty if no response is
   *     generated.
   */
  Optional<R> process(T request);

  /**
   * Retrieves the type of the facade.
   *
   * <p>This method returns a string representing the type of the facade. The type can be used to
   * identify or categorize the facade implementation.
   *
   * @return A string representing the type of the facade.
   */
  String getType();
}
