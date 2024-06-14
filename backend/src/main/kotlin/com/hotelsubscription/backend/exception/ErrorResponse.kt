package com.hotelsubscription.backend.exception

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

@Schema(description = "Standard error response object for the API")
data class ErrorResponse(

    @Schema(description = "The timestamp of the error occurrence")
    val timestamp: LocalDateTime,

    @Schema(description = "HTTP status code")
    val status: Int,

    @Schema(description = "Error type or short description")
    val error: String,

    @Schema(description = "Detailed error message")
    val message: String,

    @Schema(description = "Request path that generated the error")
    val path: String,

    @Schema(description = "List of field-specific errors, if any", nullable = true)
    val errors: List<ValidationError>? = null
)
