package io.pleo.antaeus.models

import java.util.*

data class Bill(
        val id: Int,
        val customerId: Int,
        val referenceId: Int,
        val status: String,
        val paymentLink: String,
        val currency: String,
        val targetMonth:  Int,
        val datePaid: String
)