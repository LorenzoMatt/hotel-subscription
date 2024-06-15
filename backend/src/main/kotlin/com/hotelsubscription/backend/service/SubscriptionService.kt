package com.hotelsubscription.backend.service

import com.hotelsubscription.backend.dto.SubscriptionResponse
import com.hotelsubscription.backend.entity.Term
import java.time.LocalDate

interface SubscriptionService {
    fun startSubscription(hotelId: Long, startDate: LocalDate, term: Term): SubscriptionResponse
    fun cancelSubscription(subscriptionId: Long): SubscriptionResponse
    fun getAllSubscriptions(): List<SubscriptionResponse>
    fun restartSubscription(subscriptionId: Long): SubscriptionResponse
    fun hasActiveSubscription(hotelId: Long?): Boolean
    fun getSubscriptionById(subscriptionId: Long): SubscriptionResponse
}
