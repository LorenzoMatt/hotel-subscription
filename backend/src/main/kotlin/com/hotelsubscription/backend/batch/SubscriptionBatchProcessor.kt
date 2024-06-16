package com.hotelsubscription.backend.batch

import com.hotelsubscription.backend.entity.Status
import com.hotelsubscription.backend.repository.SubscriptionRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service
class SubscriptionBatchProcessor(
    private val subscriptionRepository: SubscriptionRepository
) {
    companion object {
        private val logger: Logger = LoggerFactory.getLogger(this::class.java)
    }

    @Transactional
    @Scheduled(cron = "0 1 0 * * ?") // Runs daily at one minute past midnight
    fun updateExpiredSubscriptions() {
        try {
            val today = LocalDate.now()
            val updatedCount = subscriptionRepository.updateExpiredSubscriptions(today, Status.EXPIRED, Status.ACTIVE)
            logger.info("$updatedCount subscriptions have been updated to expired.")
        } catch (e: Exception) {
            logger.error("Failed to update expired subscriptions", e)
        }
    }
}
