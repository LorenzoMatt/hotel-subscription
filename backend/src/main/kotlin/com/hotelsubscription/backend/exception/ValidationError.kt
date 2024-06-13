package com.hotelsubscription.backend.exception

data class ValidationError(
    val field: String,
    val error: String,
    val value: Any?
)
