package com.hotelsubscription.backend.controller

import com.hotelsubscription.backend.dto.SubscriptionRequest
import com.hotelsubscription.backend.dto.SubscriptionResponse
import com.hotelsubscription.backend.service.SubscriptionService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

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
}
