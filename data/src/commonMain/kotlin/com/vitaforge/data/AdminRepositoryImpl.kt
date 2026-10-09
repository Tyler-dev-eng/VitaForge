package com.vitaforge.data

import com.tylerdev.vitaforge.shared.domain.Product
import com.vitaforge.data.domain.AdminRepository
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.firestore.firestore
import dev.gitlive.firebase.storage.File
import dev.gitlive.firebase.storage.storage
import kotlinx.coroutines.withTimeout
import kotlin.uuid.Uuid

class AdminRepositoryImpl : AdminRepository {
    override fun getCurrentUserId() = Firebase.auth.currentUser?.uid

    override suspend fun createNewProduct(
        product: Product,
        onSuccess: () -> Unit,
        onError: (String) -> Unit,
    ) {
        try {
            val currentUserId = getCurrentUserId()
            if (currentUserId != null) {
                val firestore = Firebase.firestore
                val productCollection = firestore.collection(collectionPath = "product")
                productCollection.document(product.id).set(product.copy(title = product.title.lowercase()))
                onSuccess()
            } else {
                onError("User is not available.")
            }
        } catch (e: Exception) {
            onError("Error while creating a new product: ${e.message}")
        }
    }

    override suspend fun uploadImageToStorage(file: File): String? =
        (
            if (getCurrentUserId() != null) {
                val storage = Firebase.storage.reference
                val imagePath = storage.child("images/${Uuid.random().toHexString()}")
                try {
                    withTimeout(timeMillis = 20000) {
                        imagePath.putFile(file)
                        imagePath.getDownloadUrl()
                    }
                } catch (e: Exception) {
                    null
                }
            } else {
                null
            }
        )
}
