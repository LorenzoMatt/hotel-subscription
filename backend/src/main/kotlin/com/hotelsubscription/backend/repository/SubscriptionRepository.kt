package com.hotelsubscription.backend.repository

import com.hotelsubscription.backend.entity.Status
import com.hotelsubscription.backend.entity.Subscription
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.history.RevisionRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDate


@Repository
interface SubscriptionRepository : JpaRepository<Subscription, Long>, RevisionRepository<Subscription, Long, Long> {
    fun findByHotelIdAndStatus(hotelId: Long, status: Status): List<Subscription>
    @Query("SELECT COUNT(s) > 0 FROM Subscription s WHERE s.hotelId = :hotelId AND s.status = :status")
    fun existsByHotelIdAndStatus(hotelId: Long?, status: Status?): Boolean
    fun findByStatus(status: Status): List<Subscription>
    @Query("SELECT s FROM Subscription s WHERE MONTH(s.startDate) = :month")
    fun findByMonth(month: Int): List<Subscription>
    @Modifying
    @Query("UPDATE Subscription s SET s.status = :newStatus WHERE s.nextPayment <= :cutoffDate AND s.status = :currentStatus")
    fun updateExpiredSubscriptions(
        @Param("cutoffDate") cutoffDate: LocalDate,
        @Param("newStatus") newStatus: Status,
        @Param("currentStatus") currentStatus: Status
    ): Int
}
