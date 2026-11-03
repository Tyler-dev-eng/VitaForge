package com.vitaforge.data.domain

import com.tylerdev.vitaforge.shared.domain.Order

interface OrderRepository {
    fun getCurrentUserId(): String?

    suspend fun createTheOrder(
        order: Order,
        onSuccess: () -> Unit,
        onError: (String) -> Unit,
    )
}
