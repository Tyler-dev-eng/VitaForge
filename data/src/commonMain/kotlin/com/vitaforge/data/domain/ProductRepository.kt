package com.vitaforge.data.domain

import com.tylerdev.vitaforge.shared.domain.Product
import com.tylerdev.vitaforge.shared.util.RequestState
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    fun getCurrentUserId(): String?

    fun readDiscountedProducts(): Flow<RequestState<List<Product>>>

    fun readNewProducts(): Flow<RequestState<List<Product>>>
}
