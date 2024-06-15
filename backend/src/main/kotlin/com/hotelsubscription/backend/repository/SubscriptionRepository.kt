package com.hotelsubscription.backend.repository

import com.hotelsubscription.backend.entity.Status
import com.hotelsubscription.backend.entity.Subscription
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface SubscriptionRepository : JpaRepository<Subscription, Long> {
    fun findByHotelIdAndStatus(hotelId: Long, status: Status): List<Subscription>
    @Query("SELECT COUNT(s) > 0 FROM Subscription s WHERE s.hotelId = :hotelId AND s.status = :status")
    fun existsByHotelIdAndStatus(hotelId: Long?, status: Status?): Boolean
    fun findByStatus(status: Status): List<Subscription>
}
