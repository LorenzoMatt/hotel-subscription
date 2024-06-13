package com.hotelsubscription.backend.dto

import com.hotelsubscription.backend.entity.Term
import jakarta.validation.constraints.Future
import jakarta.validation.constraints.NotNull
import java.time.LocalDate

data class SubscriptionRequest(
    @field:NotNull(message = "Hotel ID must not be null")
    val hotelId: Long?,

    @field:NotNull(message = "Start date must not be null")
    @field:Future(message = "Start date must be in the future")
    val startDate: LocalDate?,

    @field:NotNull(message = "Term must not be null")
    val term: Term?
)
