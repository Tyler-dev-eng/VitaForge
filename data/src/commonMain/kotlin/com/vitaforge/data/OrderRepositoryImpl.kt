package com.vitaforge.data

import com.tylerdev.vitaforge.shared.domain.Order
import com.vitaforge.data.domain.CustomerRepository
import com.vitaforge.data.domain.OrderRepository
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.firestore.firestore

class OrderRepositoryImpl(
    private val customerRepository: CustomerRepository,
) : OrderRepository {
    override fun getCurrentUserId(): String? = Firebase.auth.currentUser?.uid

    override suspend fun createTheOrder(order: Order): Result<Unit> =
        runRepository(errorPrefix = "Error while creating the order") {
            getCurrentUserId() ?: throw RepositoryException(USER_NOT_AVAILABLE)
            Firebase.firestore
                .collection(collectionPath = "order")
                .document(order.id)
                .set(order)
            // The order is already stored; a failed cart cleanup must not fail it.
            customerRepository.deleteAllCartItems()
            Unit
        }
}
