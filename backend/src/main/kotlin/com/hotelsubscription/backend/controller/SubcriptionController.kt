package com.hotelsubscription.backend.controller

import com.hotelsubscription.backend.dto.SubscriptionRequest
import com.hotelsubscription.backend.dto.SubscriptionResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@Validated
interface SubscriptionController {

    @Operation(summary = "Start a new subscription for a specific hotel")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Subscription created successfully",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = SubscriptionResponse::class))]),
        ApiResponse(responseCode = "400", description = "Invalid input", content = [Content(mediaType = "application/json")]),
        ApiResponse(responseCode = "400", description = "Hotel already has an active subscription", content = [Content(mediaType = "application/json")])
    ])
    @PostMapping
    fun startSubscription(@Valid @RequestBody request: SubscriptionRequest): ResponseEntity<SubscriptionResponse>

    @Operation(summary = "Cancel an existing subscription")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Subscription canceled successfully",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = SubscriptionResponse::class))]),
        ApiResponse(responseCode = "404", description = "Subscription not found", content = [Content(mediaType = "application/json")]),
        ApiResponse(responseCode = "400", description = "Cannot cancel a non-active subscription", content = [Content(mediaType = "application/json")])
    ])
    @PostMapping("/{id}/cancel")
    fun cancelSubscription(@PathVariable id: Long): ResponseEntity<SubscriptionResponse>

    @Operation(summary = "Get all existing subscriptions")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "List of subscriptions retrieved successfully",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = SubscriptionResponse::class))])
    ])
    @GetMapping
    fun getAllSubscriptions(): ResponseEntity<List<SubscriptionResponse>>

    @Operation(summary = "Restart a canceled subscription")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Subscription restarted successfully",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = SubscriptionResponse::class))]),
        ApiResponse(responseCode = "404", description = "Subscription not found", content = [Content(mediaType = "application/json")]),
        ApiResponse(responseCode = "400", description = "Only canceled subscriptions can be restarted", content = [Content(mediaType = "application/json")])
    ])
    @PostMapping("/{id}/restart")
    fun restartSubscription(@PathVariable id: Long): ResponseEntity<SubscriptionResponse>
}
