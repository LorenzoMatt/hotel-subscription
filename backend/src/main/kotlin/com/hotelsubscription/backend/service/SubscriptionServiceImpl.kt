package com.hotelsubscription.backend.service

import com.hotelsubscription.backend.dto.SubscriptionResponse
import com.hotelsubscription.backend.entity.Status
import com.hotelsubscription.backend.entity.Subscription
import com.hotelsubscription.backend.entity.Term
import com.hotelsubscription.backend.repository.SubscriptionRepository
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class SubscriptionServiceImpl(
    private val subscriptionRepository: SubscriptionRepository
) : SubscriptionService {

    override fun startSubscription(hotelId: Long, startDate: LocalDate, term: Term): SubscriptionResponse {
        val activeSubscriptions = subscriptionRepository.findByHotelIdAndStatus(hotelId, Status.ACTIVE)
        if (activeSubscriptions.isNotEmpty()) {
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
        return subscriptionRepository.save(subscription).toResponse()
    }

    override fun cancelSubscription(subscriptionId: Long): SubscriptionResponse {
        val subscription = subscriptionRepository.findById(subscriptionId)
            .orElseThrow { NoSuchElementException("Subscription not found") }
        if (subscription.status != Status.ACTIVE) {
            throw IllegalStateException("Cannot cancel a subscription that is not active")
        }
        val updatedSubscription = subscription.copy(status = Status.CANCELED, endDate = LocalDate.now())
        return subscriptionRepository.save(updatedSubscription).toResponse()
    }

    override fun getAllSubscriptions(): List<SubscriptionResponse> {
        return subscriptionRepository.findAll().map { it.toResponse() }
    }

    override fun restartSubscription(subscriptionId: Long): SubscriptionResponse {
        val subscriptionToRestart = subscriptionRepository.findById(subscriptionId)
            .orElseThrow { NoSuchElementException("Subscription not found") }

        val activeSubscriptions =
            subscriptionRepository.findByHotelIdAndStatus(subscriptionToRestart.hotelId, Status.ACTIVE)
        if (activeSubscriptions.isNotEmpty()) {
            throw IllegalStateException("Hotel already has an active subscription")
        }

        val nextPaymentDate =
            if (subscriptionToRestart.term == Term.MONTHLY) LocalDate.now().plusMonths(1) else LocalDate.now()
                .plusYears(1)
        val updatedSubscription =
            subscriptionToRestart.copy(status = Status.ACTIVE, nextPayment = nextPaymentDate, endDate = null)
        return subscriptionRepository.save(updatedSubscription).toResponse()

    }

    override fun getSubscriptionById(subscriptionId: Long): SubscriptionResponse {
        val subscription = subscriptionRepository.findById(subscriptionId)
            .orElseThrow { NoSuchElementException("Subscription not found") }
        return subscription.toResponse()
    }

    override fun getSubscriptionsByStatus(status: Status): List<SubscriptionResponse> {
        return subscriptionRepository.findByStatus(status).map { it.toResponse() }
    }

    override fun hasActiveSubscription(hotelId: Long?): Boolean {
        return subscriptionRepository.existsByHotelIdAndStatus(hotelId, Status.ACTIVE)
    }

    private fun Subscription.toResponse(): SubscriptionResponse {
        return SubscriptionResponse(
            id = this.id,
            hotelId = this.hotelId,
            startDate = this.startDate,
            nextPayment = this.nextPayment,
            endDate = this.endDate,
            term = this.term,
            status = this.status
        )
    }
}
