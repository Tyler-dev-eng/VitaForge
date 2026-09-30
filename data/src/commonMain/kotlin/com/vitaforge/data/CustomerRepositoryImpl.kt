package com.vitaforge.data

import com.tylerdev.vitaforge.shared.domain.Customer
import com.vitaforge.data.domain.CustomerRepository
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.FirebaseUser
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.firestore.firestore

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
                    onSuccess()
                }
            } else {
                onError("User is null")
            }
        } catch (e: Exception) {
            onError("Error creating customer: ${e.message}")
        }
    }
}
