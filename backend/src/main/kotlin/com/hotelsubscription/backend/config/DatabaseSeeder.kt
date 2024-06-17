package com.hotelsubscription.backend.config

import com.hotelsubscription.backend.entity.Status
import com.hotelsubscription.backend.entity.Subscription
import com.hotelsubscription.backend.entity.Term
import com.hotelsubscription.backend.repository.SubscriptionRepository
import jakarta.annotation.PostConstruct
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import java.time.LocalDate

@Configuration
class DatabaseSeeder(
    private val subscriptionRepository: SubscriptionRepository
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @Value("\${app.seed-subscriptions:false}")
    private var seedEnabled: Boolean = false

    @PostConstruct
    fun init() {
        if (seedEnabled) {
            seedSubscriptions()
        }
    }

    private fun seedSubscriptions() {
        if (subscriptionRepository.count() == 0L) {
            val defaultSubscriptions = (1..10).map { index ->
                Subscription(
                    hotelId = index.toLong(),
                    startDate = LocalDate.now().plusDays(index.toLong()),
                    nextPayment = LocalDate.now().plusMonths(1).plusDays(index.toLong()),
                    term = if (index % 2 == 0) Term.MONTHLY else Term.YEARLY,
                    status = Status.ACTIVE
                )
            }
            subscriptionRepository.saveAll(defaultSubscriptions)
            logger.info("Database seeded with default subscriptions.")
        }
    }
}
