package com.hotelsubscription.backend.controller

import com.hotelsubscription.backend.dto.SubscriptionRequest
import com.hotelsubscription.backend.dto.SubscriptionResponse
import com.hotelsubscription.backend.entity.Status
import com.hotelsubscription.backend.exception.ErrorResponse
import com.hotelsubscription.backend.service.SubscriptionService
import jakarta.validation.Valid
import org.springframework.data.jpa.domain.AbstractPersistable_
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime


@RestController
@RequestMapping("/api/subscriptions")
class SubscriptionControllerImpl(
    private val subscriptionService: SubscriptionService
) : SubscriptionController {

    override fun startSubscription(@Valid @RequestBody request: SubscriptionRequest): ResponseEntity<SubscriptionResponse> {
        val subscription = subscriptionService.startSubscription(request.hotelId!!, request.startDate!!, request.term!!)
        return ResponseEntity.ok(subscription)
    }

    override fun cancelSubscription(@PathVariable id: Long): ResponseEntity<SubscriptionResponse> {
        val subscription = subscriptionService.cancelSubscription(id)
        return ResponseEntity.ok(subscription)
    }

    override fun getAllSubscriptions(): ResponseEntity<List<SubscriptionResponse>> {
        val subscriptions = subscriptionService.getAllSubscriptions()
        return ResponseEntity.ok(subscriptions)
    }

    override fun restartSubscription(@PathVariable id: Long): ResponseEntity<SubscriptionResponse> {
        val subscription = subscriptionService.restartSubscription(id)
        return ResponseEntity.ok(subscription)
    }

    override fun hasActiveSubscription(@PathVariable hotelId: Long): ResponseEntity<Boolean> {
        val hasActive = subscriptionService.hasActiveSubscription(hotelId)
        return ResponseEntity.ok(hasActive)
    }

    override fun getSubscriptionById(@PathVariable id: Long): ResponseEntity<SubscriptionResponse> {
        val subscription = subscriptionService.getSubscriptionById(id)
        return ResponseEntity.ok(subscription)
    }

    override fun getSubscriptionsByStatus(@RequestParam status: Status): ResponseEntity<List<SubscriptionResponse>> {
        return ResponseEntity.ok(subscriptionService.getSubscriptionsByStatus(status))
    }

    override fun getSubscriptionsByMonth(@RequestParam month: Int): ResponseEntity<List<SubscriptionResponse>> {
        return ResponseEntity.ok(subscriptionService.getSubscriptionsByMonth(month))
    }

}
