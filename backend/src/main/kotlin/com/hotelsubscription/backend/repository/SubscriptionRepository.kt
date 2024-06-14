package com.hotelsubscription.backend.repository

import com.hotelsubscription.backend.entity.Status
import com.hotelsubscription.backend.entity.Subscription
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SubscriptionRepository : JpaRepository<Subscription, Long> {
    fun findByHotelIdAndStatus(hotelId: Long, status: Status): List<Subscription>
    fun findByHotelId(hotelId: Long): List<Subscription>
}
