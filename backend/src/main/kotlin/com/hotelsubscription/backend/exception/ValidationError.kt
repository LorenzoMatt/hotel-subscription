package com.hotelsubscription.backend.exception

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Detailed information about a validation error")
data class ValidationError(

    @Schema(description = "Field where the validation error occurred")
    val field: String,

    @Schema(description = "Description of the validation error")
    val error: String,

    @Schema(description = "Invalid value provided for the field")
    val value: Any?
)
