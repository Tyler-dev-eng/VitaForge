package com.tylerdev.vitaforge.shared.domain

import kotlinx.serialization.Serializable
import kotlin.uuid.Uuid

@Serializable
data class CartItem(
    val id: String = Uuid.random().toHexString(),
    val productId: String,
    val flavour: String? = null,
    val quantity: Int,
)
