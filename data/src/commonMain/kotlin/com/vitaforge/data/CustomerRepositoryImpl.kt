package com.vitaforge.data

import com.tylerdev.vitaforge.shared.domain.CartItem
import com.tylerdev.vitaforge.shared.domain.Customer
import com.tylerdev.vitaforge.shared.util.RequestState
import com.vitaforge.data.domain.CustomerRepository
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.FirebaseUser
import dev.gitlive.firebase.auth.auth
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
        user: FirebaseUser?,
        onSuccess: () -> Unit,
        onError: (String) -> Unit,
    ) {
        try {
            if (user != null) {
                val customerCollection = Firebase.firestore.collection(collectionPath = "customer")
                val customer =
                    Customer(
                        id = user.uid,
                        firstName = user.displayName?.split(" ")?.firstOrNull() ?: "Unknown",
                        lastName = user.displayName?.split(" ")?.lastOrNull() ?: "Unknown",
                        email = user.email ?: "Unknown",
                    )

                if (customerCollection.document(user.uid).get().exists) {
                    onSuccess()
                } else {
                    customerCollection.document(user.uid).set(customer)
                    customerCollection
                        .document(user.uid)
                        .collection("privateData")
                        .document("role")
                        .set(mapOf("isAdmin" to false))
                    onSuccess()
                }
            } else {
                onError("User is null")
            }
        } catch (e: Exception) {
            onError("Error creating customer: ${e.message}")
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
            } catch (e: Exception) {
                send(RequestState.Error("Error while reading a Customer information: ${e.message}"))
            }
        }

    override suspend fun updateCustomer(
        customer: Customer,
        onSuccess: () -> Unit,
        onError: (String) -> Unit,
    ) {
        try {
            val userId = getCurrentUserId()
            if (userId != null) {
                val firestore = Firebase.firestore
                val customerCollection = firestore.collection(collectionPath = "customer")
                val existingCustomer =
                    customerCollection
                        .document(customer.id)
                        .get()
                if (existingCustomer.exists) {
                    customerCollection
                        .document(customer.id)
                        .updateFields {
                            "firstName" to customer.firstName
                            "lastName" to customer.lastName
                            "city" to customer.city
                            "postalCode" to customer.postalCode
                            "address" to customer.address
                            "phoneNumber" to customer.phoneNumber
                        }
                    onSuccess()
                }
            }
        } catch (e: Exception) {
            onError("Error while updating a Customer information: ${e.message}")
        }
    }

    override suspend fun addItemToCart(cartItem: CartItem): RequestState<Unit> {
        val userId = getCurrentUserId() ?: return RequestState.Error("User is not available.")
        return try {
            val firestore = Firebase.firestore
            val customerDocument = firestore.collection(collectionPath = "customer").document(userId)
            val hasCustomer =
                firestore.runTransaction {
                    val snapshot = get(customerDocument)
                    if (snapshot.exists) {
                        val existingCart = snapshot.get<List<CartItem>?>(field = "cart").orEmpty()
                        set(customerDocument, CartUpdate(cart = existingCart.withItemAdded(cartItem)), merge = true)
                    }
                    snapshot.exists
                }
            if (hasCustomer) {
                RequestState.Success(Unit)
            } else {
                RequestState.Error("Customer document does not exist.")
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            RequestState.Error("Error while adding item to cart: ${e.message}")
        }
    }

    override suspend fun updateCartItemQuantity(
        id: String,
        quantity: Int,
        onSuccess: () -> Unit,
        onError: (String) -> Unit,
    ) {
        try {
            val userId = getCurrentUserId()
            if (userId != null) {
                val database = Firebase.firestore
                val customerCollection = database.collection(collectionPath = "customer")

                val existingCustomer =
                    customerCollection
                        .document(userId)
                        .get()

                if (existingCustomer.exists) {
                    val existingCart = existingCustomer.get<List<CartItem>?>(field = "cart")
                    val updatedCart =
                        existingCart?.map { cartItem ->
                            if (cartItem.id == id) {
                                cartItem.copy(quantity = quantity)
                            } else {
                                cartItem
                            }
                        }
                    customerCollection
                        .document(userId)
                        .updateFields {
                            "cart" to updatedCart
                        }
                    onSuccess()
                } else {
                    onError("Customer document does not exist.")
                }
            } else {
                onError("User is not available.")
            }
        } catch (e: Exception) {
            onError("Error while updating cart item quantity: ${e.message}")
        }
    }

    override suspend fun deleteCartItem(
        id: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit,
    ) {
        try {
            val userId = getCurrentUserId()
            if (userId != null) {
                val database = Firebase.firestore
                val customerCollection = database.collection(collectionPath = "customer")

                val existingCustomer =
                    customerCollection
                        .document(userId)
                        .get()

                if (existingCustomer.exists) {
                    val existingCart = existingCustomer.get<List<CartItem>?>(field = "cart")
                    val updatedCart = existingCart?.filterNot { it.id == id }
                    if (updatedCart == null) {
                        onError("Updated cart is null.")
                        return
                    }
                    customerCollection
                        .document(userId)
                        .updateFields {
                            "cart" to updatedCart
                        }
                    onSuccess()
                } else {
                    onError("Customer document does not exist.")
                }
            } else {
                onError("User is not available.")
            }
        } catch (e: Exception) {
            onError("Error while deleting cart item: ${e.message}")
        }
    }

    override suspend fun signOut(): RequestState<Unit> =
        try {
            Firebase.auth.signOut()
            RequestState.Success(Unit)
        } catch (e: Exception) {
            RequestState.Error("Error signing out: ${e.message}")
        }
}
