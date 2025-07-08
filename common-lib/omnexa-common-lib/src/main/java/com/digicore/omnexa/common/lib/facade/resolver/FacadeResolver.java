/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.common.lib.facade.resolver;

import com.digicore.omnexa.common.lib.exception.OmnexaException;
import com.digicore.omnexa.common.lib.facade.annotation.ExcludeFromFacadeResolver;
import com.digicore.omnexa.common.lib.facade.contract.Facade;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

/**
 * Resolves and manages facade implementations based on their type.
 *
 * <p>This class is responsible for mapping facade types to their corresponding implementations and
 * providing a mechanism to resolve a facade by its type. Facades annotated with {@link
 * ExcludeFromFacadeResolver} are excluded from the resolution process.
 *
 * <p>Usage: - Instantiate this class with a list of all available facades. - Use {@link
 * #resolve(String)} to retrieve a facade implementation by its type.
 *
 * <p>Features: - Prevents duplicate facade types by throwing an {@link OmnexaException}. - Ensures
 * unsupported or deprecated types are handled gracefully.
 *
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jul-03(Thu)-2025
 */
@Component
public class FacadeResolver {

  private final Map<String, Facade<?, ?>> facadeMap;

  /**
   * Constructs a FacadeResolver with a list of all available facades.
   *
   * <p>Filters out facades annotated with {@link ExcludeFromFacadeResolver} and maps facade types
   * to their implementations. Throws an {@link OmnexaException} if duplicate facade types are
   * detected.
   *
   * @param allFacades A list of all available facade implementations.
   */
  public FacadeResolver(List<Facade<?, ?>> allFacades) {
    this.facadeMap =
        allFacades.stream()
            .filter(
                facade -> !facade.getClass().isAnnotationPresent(ExcludeFromFacadeResolver.class))
            .collect(
                Collectors.toMap(
                    Facade::getType,
                    Function.identity(),
                    (existing, duplicate) -> {
                      throw new OmnexaException(
                          "Duplicate Facade type detected: " + existing.getType());
                    }));
  }

  /**
   * Resolves a facade implementation by its type.
   *
   * <p>Retrieves the facade corresponding to the given type. Throws an {@link OmnexaException} if
   * the type is unsupported or deprecated.
   *
   * @param type The type of the facade to resolve.
   * @param <T> The type of the request object handled by the facade.
   * @param <R> The type of the response object returned by the facade.
   * @return The resolved facade implementation.
   * @throws OmnexaException If the type is unsupported or deprecated.
   */
  @SuppressWarnings("unchecked")
  public <T, R> Facade<T, R> resolve(String type) {
    Facade<?, ?> facade = facadeMap.get(type);
    if (facade == null) {
      throw new OmnexaException("Unsupported or deprecated type: " + type);
    }
    return (Facade<T, R>) facade;
  }
}
