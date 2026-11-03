package com.vitaforge.data.domain

import com.tylerdev.vitaforge.shared.domain.CartItem
import com.tylerdev.vitaforge.shared.domain.Customer
import com.tylerdev.vitaforge.shared.util.RequestState
import kotlinx.coroutines.flow.Flow

interface CustomerRepository {
    fun getCurrentUserId(): String?

    suspend fun createCustomer(
        id: String,
        displayName: String?,
        email: String?,
    ): Result<Unit>

    fun readCustomerFlow(): Flow<RequestState<Customer>>

    suspend fun updateCustomer(customer: Customer): Result<Unit>

    suspend fun addItemToCart(cartItem: CartItem): Result<Unit>

    suspend fun updateCartItemQuantity(
        id: String,
        quantity: Int,
    ): Result<Unit>

    suspend fun deleteCartItem(id: String): Result<Unit>

    suspend fun deleteAllCartItems(): Result<Unit>

    suspend fun signOut(): Result<Unit>
}
