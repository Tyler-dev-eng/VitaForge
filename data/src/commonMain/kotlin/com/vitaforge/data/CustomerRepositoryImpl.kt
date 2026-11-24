package com.vitaforge.data

import com.tylerdev.vitaforge.shared.domain.CartItem
import com.tylerdev.vitaforge.shared.domain.Customer
import com.tylerdev.vitaforge.shared.util.RequestState
import com.vitaforge.data.domain.CustomerRepository
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.firestore.DocumentSnapshot
import dev.gitlive.firebase.firestore.firestore
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.serialization.Serializable
import kotlin.collections.mapOf

@Serializable
private data class CartUpdate(
    val cart: List<CartItem>,
)

private fun List<CartItem>.withItemAdded(newItem: CartItem): List<CartItem> {
    val hasMatch = any { it.productId == newItem.productId && it.flavour == newItem.flavour }
    if (!hasMatch) return this + newItem
    return map { item ->
        if (item.productId == newItem.productId && item.flavour == newItem.flavour) {
            item.copy(quantity = item.quantity + newItem.quantity)
        } else {
            item
        }
    }
}

class CustomerRepositoryImpl : CustomerRepository {
    override fun getCurrentUserId(): String? = Firebase.auth.currentUser?.uid

    override suspend fun createCustomer(
        id: String,
        displayName: String?,
        email: String?,
    ): Result<Unit> =
        runRepository(errorPrefix = "Error creating customer") {
            val customerCollection = Firebase.firestore.collection(collectionPath = "customer")
            if (!customerCollection.document(id).get().exists) {
                val nameParts = displayName?.split(" ")
                val customer =
                    Customer(
                        id = id,
                        firstName = nameParts?.firstOrNull() ?: "Unknown",
                        lastName = nameParts?.lastOrNull() ?: "Unknown",
                        email = email ?: "Unknown",
                    )
                customerCollection.document(id).set(customer)
                customerCollection
                    .document(id)
                    .collection("privateData")
                    .document("role")
                    .set(mapOf("isAdmin" to false))
            }
        }

    override fun readCustomerFlow(): Flow<RequestState<Customer>> =
        channelFlow {
            try {
                val userId = getCurrentUserId()
                if (userId != null) {
                    val database = Firebase.firestore
                    database
                        .collection(collectionPath = "customer")
                        .document(userId)
                        .snapshots
                        .collectLatest { document ->
                            if (document.exists) {
                                val privateData =
                                    database
                                        .collection(collectionPath = "customer")
                                        .document(userId)
                                        .collection("privateData")
                                        .document("role")
                                        .get()
                                val customer =
                                    Customer(
                                        id = document.id,
                                        firstName = document.get(field = "firstName"),
                                        lastName = document.get(field = "lastName"),
                                        email = document.get(field = "email"),
                                        city = document.get(field = "city"),
                                        postalCode = document.get(field = "postalCode"),
                                        address = document.get(field = "address"),
                                        phoneNumber = document.get(field = "phoneNumber"),
                                        cart = document.get(field = "cart"),
                                        isAdmin = privateData.takeIf { it.exists }?.get<Boolean?>(field = "isAdmin") ?: false,
                                    )
                                send(RequestState.Success(data = customer))
                            } else {
                                send(RequestState.Error("Queried customer document does not exist."))
                            }
                        }
                } else {
                    send(RequestState.Error("User is not available."))
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                send(RequestState.Error("Error while reading a Customer information. ${e.toFriendlyReason()}"))
            }
        }

    override suspend fun updateCustomer(customer: Customer): Result<Unit> =
        runRepository(errorPrefix = "Error while updating a Customer information") {
            requireUserId()
            val customerDocument = Firebase.firestore.collection(collectionPath = "customer").document(customer.id)
            if (!customerDocument.get().exists) throw RepositoryException("Customer document does not exist.")
            customerDocument.updateFields {
                "firstName" to customer.firstName
                "lastName" to customer.lastName
                "city" to customer.city
                "postalCode" to customer.postalCode
                "address" to customer.address
                "phoneNumber" to customer.phoneNumber
            }
        }

    override suspend fun addItemToCart(cartItem: CartItem): Result<Unit> =
        runRepository(errorPrefix = "Error while adding item to cart") {
            val firestore = Firebase.firestore
            val customerDocument = firestore.collection(collectionPath = "customer").document(requireUserId())
            val hasCustomer =
                firestore.runTransaction {
                    val snapshot = get(customerDocument)
                    if (snapshot.exists) {
                        val existingCart = snapshot.get<List<CartItem>?>(field = "cart").orEmpty()
                        set(customerDocument, CartUpdate(cart = existingCart.withItemAdded(cartItem)), merge = true)
                    }
                    snapshot.exists
                }
            if (!hasCustomer) throw RepositoryException("Customer document does not exist.")
        }

    override suspend fun updateCartItemQuantity(
        id: String,
        quantity: Int,
    ): Result<Unit> =
        runRepository(errorPrefix = "Error while updating cart item quantity") {
            val customer = existingCustomer()
            val updatedCart =
                customer
                    .get<List<CartItem>?>(field = "cart")
                    ?.map { cartItem -> if (cartItem.id == id) cartItem.copy(quantity = quantity) else cartItem }
            customer.reference.updateFields { "cart" to updatedCart }
        }

    override suspend fun deleteCartItem(id: String): Result<Unit> =
        runRepository(errorPrefix = "Error while deleting cart item") {
            val customer = existingCustomer()
            val updatedCart =
                customer
                    .get<List<CartItem>?>(field = "cart")
                    ?.filterNot { it.id == id }
                    ?: throw RepositoryException("Updated cart is null.")
            customer.reference.updateFields { "cart" to updatedCart }
        }

    override suspend fun deleteAllCartItems(): Result<Unit> =
        runRepository(errorPrefix = "Error while deleting all products from cart") {
            existingCustomer().reference.update(data = mapOf("cart" to emptyList<CartItem>()))
        }

    override suspend fun signOut(): Result<Unit> =
        runRepository(errorPrefix = "Error signing out") {
            Firebase.auth.signOut()
        }

    private fun requireUserId(): String = getCurrentUserId() ?: throw RepositoryException(USER_NOT_AVAILABLE)

    private suspend fun existingCustomer(): DocumentSnapshot {
        val customer =
            Firebase.firestore
                .collection(collectionPath = "customer")
                .document(requireUserId())
                .get()
        if (!customer.exists) throw RepositoryException("Customer document does not exist.")
        return customer
    }
}
