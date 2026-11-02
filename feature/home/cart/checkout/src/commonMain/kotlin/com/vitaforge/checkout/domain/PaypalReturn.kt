package com.vitaforge.checkout.domain

sealed interface PaypalReturn {
    data class Approved(
        val orderId: String,
    ) : PaypalReturn

    data object Cancelled : PaypalReturn
}
