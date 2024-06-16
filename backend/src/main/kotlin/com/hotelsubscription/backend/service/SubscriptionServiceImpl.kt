package com.hotelsubscription.backend.service

import com.hotelsubscription.backend.dto.SubscriptionResponse
import com.hotelsubscription.backend.entity.Status
import com.hotelsubscription.backend.entity.Subscription
import com.hotelsubscription.backend.entity.Term
import com.hotelsubscription.backend.repository.SubscriptionRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service
@Transactional
class SubscriptionServiceImpl(
    private val subscriptionRepository: SubscriptionRepository
) : SubscriptionService {
    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun startSubscription(hotelId: Long, startDate: LocalDate, term: Term): SubscriptionResponse {
        logger.debug("Starting subscription for hotelId: {}", hotelId)
        val activeSubscriptions = subscriptionRepository.findByHotelIdAndStatus(hotelId, Status.ACTIVE)
        if (activeSubscriptions.isNotEmpty()) {
            logger.warn(
                "Attempt to start a new subscription for hotelId: {} which already has an active subscription",
                hotelId
            )
            throw IllegalStateException("Hotel already has an active subscription")
        }
        val nextPaymentDate = if (term == Term.MONTHLY) startDate.plusMonths(1) else startDate.plusYears(1)
        val subscription = Subscription(
            hotelId = hotelId,
            startDate = startDate,
            nextPayment = nextPaymentDate,
            term = term,
            status = Status.ACTIVE
        )
        return subscriptionRepository.save(subscription).also {
            logger.info("Subscription started successfully with ID: {}", it.id)
        }.toResponse()
    }

    override fun cancelSubscription(subscriptionId: Long): SubscriptionResponse {
        logger.debug("Cancelling subscription with ID: {}", subscriptionId)
        val subscription = subscriptionRepository.findById(subscriptionId).orElseThrow {
            logger.error("No subscription found with ID: {}", subscriptionId)
            NoSuchElementException("Subscription not found")
        }
        if (subscription.status != Status.ACTIVE) {
            logger.warn("Attempt to cancel a non-active subscription with ID: {}", subscriptionId)
            throw IllegalStateException("Cannot cancel a subscription that is not active")
        }
        val updatedSubscription = subscription.copy(status = Status.CANCELED, endDate = LocalDate.now())
        return subscriptionRepository.save(updatedSubscription).also {
            logger.info("Subscription cancelled successfully with ID: {}", subscriptionId)
        }.toResponse()
    }

    @Transactional(readOnly = true)
    override fun getAllSubscriptions(): List<SubscriptionResponse> {
        logger.debug("Fetching all subscriptions")
        return subscriptionRepository.findAll().map { it.toResponse() }.also {
            logger.info("Fetched {} subscriptions", it.size)
        }
    }

    override fun restartSubscription(subscriptionId: Long): SubscriptionResponse {
        logger.debug("Restarting subscription with ID: {}", subscriptionId)
        val subscriptionToRestart = subscriptionRepository.findById(subscriptionId).orElseThrow {
            logger.error("No subscription found with ID: {}", subscriptionId)
            NoSuchElementException("Subscription not found")
        }
        val activeSubscriptions =
            subscriptionRepository.findByHotelIdAndStatus(subscriptionToRestart.hotelId, Status.ACTIVE)
        if (activeSubscriptions.isNotEmpty()) {
            logger.warn("Attempt to restart a subscription for hotelId: ${subscriptionToRestart.hotelId} which already has an active subscription")
            throw IllegalStateException("Hotel already has an active subscription")
        }
        val nextPaymentDate =
            if (subscriptionToRestart.term == Term.MONTHLY) LocalDate.now().plusMonths(1) else LocalDate.now()
                .plusYears(1)
        val updatedSubscription =
            subscriptionToRestart.copy(status = Status.ACTIVE, nextPayment = nextPaymentDate, endDate = null)
        return subscriptionRepository.save(updatedSubscription).also {
            logger.info("Subscription restarted successfully with ID: {}", subscriptionId)
        }.toResponse()
    }

    @Transactional(readOnly = true)
    override fun getSubscriptionById(subscriptionId: Long): SubscriptionResponse {
        logger.debug("Fetching subscription by ID: {}", subscriptionId)
        return subscriptionRepository.findById(subscriptionId).orElseThrow {
            logger.error("No subscription found with ID: {}", subscriptionId)
            NoSuchElementException("Subscription not found")
        }.also {
            logger.info("Fetched subscription with ID: {}", subscriptionId)
        }.toResponse()
    }

    @Transactional(readOnly = true)
    override fun getSubscriptionsByStatus(status: Status): List<SubscriptionResponse> {
        logger.debug("Fetching subscriptions by status: {}", status)
        return subscriptionRepository.findByStatus(status).also {
            logger.info("Fetched {} subscriptions for status: {}", it.size, status)
        }.map { it.toResponse() }
    }

    @Transactional(readOnly = true)
    override fun getSubscriptionsByMonth(month: Int): List<SubscriptionResponse> {
        logger.debug("Fetching subscriptions by month: {}", month)
        return subscriptionRepository.findByMonth(month).also {
            logger.info("Fetched {} subscriptions for month: {}", it.size, month)
        }.map { it.toResponse() }
    }

    @Transactional(readOnly = true)
    override fun hasActiveSubscription(hotelId: Long): Boolean {
        logger.debug("Checking if there is an active subscription for hotelId: {}", hotelId)
        return subscriptionRepository.existsByHotelIdAndStatus(hotelId, Status.ACTIVE).also {
            logger.info("Active subscription status for hotelId: {} is {}", hotelId, it)
        }
    }

    private fun Subscription.toResponse(): SubscriptionResponse {
        logger.debug("Converting subscription to response with ID: {}", this.id)
        return SubscriptionResponse(
            id = this.id,
            hotelId = this.hotelId,
            startDate = this.startDate,
            nextPayment = this.nextPayment,
            endDate = this.endDate,
            term = this.term,
            status = this.status
        ).also {
            logger.trace("Converted subscription: {}", it)
        }
    }
}
