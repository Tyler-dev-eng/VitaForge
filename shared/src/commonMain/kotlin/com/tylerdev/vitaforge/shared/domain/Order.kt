package com.tylerdev.vitaforge.shared.domain

import kotlinx.serialization.Serializable
import kotlin.uuid.Uuid

@Serializable
data class Order(
    val id: String = Uuid.random().toHexString(),
    val customerId: String,
    val items: List<CartItem>,
    val totalAmount: Double,
    val token: String? = null,
)
