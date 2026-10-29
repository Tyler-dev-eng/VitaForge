package com.vitaforge.data.domain

import com.tylerdev.vitaforge.shared.domain.Order

interface OrderRepository {
    fun getCurrentUserId(): String?

    suspend fun createOrder(
        order: Order,
        onSuccess: () -> Unit,
        onError: (String) -> Unit,
    )
}
