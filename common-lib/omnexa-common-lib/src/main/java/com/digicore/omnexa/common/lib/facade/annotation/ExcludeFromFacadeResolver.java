/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.common.lib.facade.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to exclude a class from the facade resolver mechanism.
 *
 * <p>This annotation can be applied to types to indicate that they should not be included in the
 * facade resolution process. It is typically used to mark classes that are not intended to be
 * exposed or processed by facade-related logic.
 *
 * <p>Usage:
 *
 * <pre>
 * &#64;ExcludeFromFacadeResolver
 * public class SomeClass {
 *     // Implementation details
 * }
 * </pre>
 *
 * <p>Retention policy: - {@link RetentionPolicy#RUNTIME}: The annotation is available at runtime.
 *
 * <p>Target: - {@link ElementType#TYPE}: The annotation can only be applied to classes, interfaces,
 * or enums.
 *
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jul-04(Fri)-2025
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcludeFromFacadeResolver {}
