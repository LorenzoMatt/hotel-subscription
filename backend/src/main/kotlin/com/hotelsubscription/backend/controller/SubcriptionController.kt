package com.hotelsubscription.backend.controller

import com.hotelsubscription.backend.dto.SubscriptionRequest
import com.hotelsubscription.backend.dto.SubscriptionResponse
import com.hotelsubscription.backend.entity.Status
import com.hotelsubscription.backend.exception.ErrorResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*


@Validated
interface SubscriptionController {

    @Operation(summary = "Start a new subscription for a specific hotel")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "Subscription created successfully",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = SubscriptionResponse::class)
                )]
            ),
            ApiResponse(
                responseCode = "400", description = "Invalid input",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ErrorResponse::class)
                )]
            ),
            ApiResponse(
                responseCode = "400", description = "Hotel already has an active subscription",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ErrorResponse::class)
                )]
            ),
            ApiResponse(
                responseCode = "500", description = "Internal Server Error",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ErrorResponse::class)
                )]
            )
        ]
    )
    @PostMapping
    fun startSubscription(@Valid @RequestBody request: SubscriptionRequest): ResponseEntity<SubscriptionResponse>

    @Operation(summary = "Cancel an existing subscription")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "Subscription canceled successfully",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = SubscriptionResponse::class)
                )]
            ),
            ApiResponse(
                responseCode = "404", description = "Subscription not found",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ErrorResponse::class)
                )]
            ),
            ApiResponse(
                responseCode = "400", description = "Cannot cancel a non-active subscription",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ErrorResponse::class)
                )]
            ),
            ApiResponse(
                responseCode = "500", description = "Internal Server Error",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ErrorResponse::class)
                )]
            )
        ]
    )
    @PostMapping("/{id}/cancel")
    fun cancelSubscription(@PathVariable id: Long): ResponseEntity<SubscriptionResponse>

    @Operation(summary = "Get all existing subscriptions")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "List of subscriptions retrieved successfully",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = SubscriptionResponse::class)
                )]
            ),
            ApiResponse(
                responseCode = "500", description = "Internal Server Error",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ErrorResponse::class)
                )]
            )
        ]
    )
    @GetMapping
    fun getAllSubscriptions(): ResponseEntity<List<SubscriptionResponse>>

    @Operation(summary = "Restart a canceled subscription")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "Subscription restarted successfully",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = SubscriptionResponse::class)
                )]
            ),
            ApiResponse(
                responseCode = "404", description = "Subscription not found",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ErrorResponse::class)
                )]
            ),
            ApiResponse(
                responseCode = "400", description = "Only canceled subscriptions can be restarted",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ErrorResponse::class)
                )]
            ),
            ApiResponse(
                responseCode = "500", description = "Internal Server Error",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ErrorResponse::class)
                )]
            )
        ]
    )
    @PostMapping("/{id}/restart")
    fun restartSubscription(@PathVariable id: Long): ResponseEntity<SubscriptionResponse>

    @Operation(summary = "Check if there is an active subscription for a hotel")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Successfully retrieved the active subscription status",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = Boolean::class))]
            ),
            ApiResponse(
                responseCode = "500", description = "Internal Server Error",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ErrorResponse::class)
                )]
            )
        ]
    )
    @GetMapping("/has-active/{hotelId}")
    fun hasActiveSubscription(@PathVariable hotelId: Long?): ResponseEntity<Boolean>

    @Operation(summary = "Retrieve a subscription by ID")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Subscription retrieved successfully",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = SubscriptionResponse::class)
                )]
            ),
            ApiResponse(
                responseCode = "404",
                description = "Subscription not found",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ErrorResponse::class)
                )]
            ),
            ApiResponse(
                responseCode = "500",
                description = "Internal Server Error",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ErrorResponse::class)
                )]
            )
        ]
    )
    @GetMapping("/{id}")
    fun getSubscriptionById(@PathVariable id: Long): ResponseEntity<SubscriptionResponse>

    @Operation(summary = "Get subscriptions by status")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "List of subscriptions retrieved successfully",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = SubscriptionResponse::class)
                )]
            ),
            ApiResponse(
                responseCode = "500",
                description = "Internal Server Error",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ErrorResponse::class)
                )]
            )
        ]
    )
    @GetMapping("/status")
    fun getSubscriptionsByStatus(@RequestParam status: Status): ResponseEntity<List<SubscriptionResponse>>

}