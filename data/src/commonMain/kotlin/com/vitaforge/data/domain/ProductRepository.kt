package com.vitaforge.data.domain

import com.tylerdev.vitaforge.shared.domain.Product
import com.tylerdev.vitaforge.shared.domain.ProductCategory
import com.tylerdev.vitaforge.shared.util.RequestState
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    fun getCurrentUserId(): String?

    fun readDiscountedProducts(): Flow<RequestState<List<Product>>>

    fun readNewProducts(): Flow<RequestState<List<Product>>>

    fun readProductByIdFlow(id: String): Flow<RequestState<Product>>

    fun readProductByIdsFlow(ids: List<String>): Flow<RequestState<List<Product>>>

    fun readProductsByCategoryFlow(category: ProductCategory): Flow<RequestState<List<Product>>>
}
