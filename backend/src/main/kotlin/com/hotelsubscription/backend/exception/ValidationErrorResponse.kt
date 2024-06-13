package com.hotelsubscription.backend.exception

import java.time.LocalDateTime

data class ValidationErrorResponse(
    val timestamp: LocalDateTime,
    val status: Int,
    val error: String,
    val message: String,
    val path: String,
    val errors: List<ValidationError>
)
