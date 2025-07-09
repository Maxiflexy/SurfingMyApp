/*
 * Copyright (c) 2025 Digicore Limited. All Rights Reserved.
 * Unauthorized use or distribution is strictly prohibited.
 * For details, see the LICENSE file.
 */

package com.digicore.omnexa.merchant.backoffice.modules.user.dto.common;

import com.digicore.omnexa.common.lib.api.ApiError;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.*;

/**
 * Extended API response structure for back office APIs.
 *
 * <p>Includes additional fields like requestId and timestamp
 * as per the back office API standards.
 *
 * @param <T> The type of the data payload
 * @author Onyekachi Ejemba
 * @createdOn Jul-08(Tue)-2025
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Standard API response wrapper")
public class ApiResponseJson<T> {

    @Schema(description = "Indicates if the request was successful")
    private boolean success;

    @Schema(description = "Response message")
    private String message;

    @Schema(description = "Request identifier for tracking")
    private String requestId;

    @Schema(description = "Response timestamp")
    private ZonedDateTime timestamp;

    @Schema(description = "Response data payload")
    private T data;

    @Schema(description = "List of errors if request failed")
    private List<ApiError> errors = new ArrayList<>();

    /**
     * Adds an error to the error list.
     *
     * @param error the error to add
     */
    public void addError(ApiError error) {
        if (errors == null) {
            errors = new ArrayList<>();
        }
        errors.add(error);
    }
}