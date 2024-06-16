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
    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    @Transactional
    @Scheduled(cron = "0 1 0 * * ?") // Runs daily at one minute past midnight
    fun updateExpiredSubscriptions() {
        logger.info("Starting the process to update expired subscriptions")
        val today = LocalDate.now()
        val updatedCount = subscriptionRepository.updateExpiredSubscriptions(today, Status.EXPIRED, Status.ACTIVE)
        if (updatedCount > 0) {
            logger.info("Successfully updated {} subscriptions to expired status", updatedCount)
        } else {
            logger.info("No subscriptions needed updating today")
        }
    }
}