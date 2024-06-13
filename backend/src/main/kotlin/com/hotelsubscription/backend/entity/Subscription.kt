package com.hotelsubscription.backend.entity

import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name = "subscriptions")
data class Subscription(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    val hotelId: Long,

    @Column(nullable = false)
    val startDate: LocalDate,

    @Column(nullable = false)
    val nextPayment: LocalDate,

    @Column(nullable = true)
    val endDate: LocalDate? = null,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val term: Term,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val status: Status
)

enum class Term {
    MONTHLY,
    YEARLY
}

enum class Status {
    ACTIVE,
    EXPIRED,
    CANCELED
}
