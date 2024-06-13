package com.hotelsubscription.backend.dto

import com.hotelsubscription.backend.entity.Status
import com.hotelsubscription.backend.entity.Term
import java.time.LocalDate

data class SubscriptionResponse(
    val id: Long,
    val hotelId: Long,
    val startDate: LocalDate,
    val nextPayment: LocalDate,
    val endDate: LocalDate?,
    val term: Term,
    val status: Status
)
