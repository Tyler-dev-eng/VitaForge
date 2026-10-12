package com.vitaforge.data

import com.tylerdev.vitaforge.shared.domain.Product
import com.tylerdev.vitaforge.shared.util.RequestState
import com.vitaforge.data.domain.AdminRepository
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.firestore.Direction
import dev.gitlive.firebase.firestore.firestore
import dev.gitlive.firebase.storage.File
import dev.gitlive.firebase.storage.storage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
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
                    e.printStackTrace()
                    null
                }
            } else {
                null
            }
        )

    override suspend fun deleteImageFromStorage(
        downloadUrl: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit,
    ) {
        try {
            val storagePath = extractFirebaseStoragePath(downloadUrl)
            if (storagePath != null) {
                Firebase.storage.reference(storagePath).delete()
                onSuccess()
            } else {
                onError("Storage Path is null.")
            }
        } catch (e: Exception) {
            onError("Error while deleting a thumbnail: $e")
        }
    }

    override fun readLastTenProducts(): Flow<RequestState<List<Product>>> =
        channelFlow {
            try {
                val userId = getCurrentUserId()
                if (userId != null) {
                    val firestore = Firebase.firestore
                    firestore
                        .collection(collectionPath = "product")
                        .orderBy("createdAt", direction = Direction.DESCENDING)
                        .limit(10)
                        .snapshots
                        .collectLatest { snapshot ->
                            val products =
                                snapshot.documents.map { documentSnapshot ->
                                    Product(
                                        id = documentSnapshot.id,
                                        createdAt = documentSnapshot.get(field = "createdAt"),
                                        title = documentSnapshot.get(field = "title"),
                                        description = documentSnapshot.get(field = "description"),
                                        thumbnail = documentSnapshot.get(field = "thumbnail"),
                                        category = documentSnapshot.get(field = "category"),
                                        flavours = documentSnapshot.get(field = "flavours"),
                                        weight = documentSnapshot.get(field = "weight"),
                                        price = documentSnapshot.get(field = "price"),
                                        isNew = documentSnapshot.get(field = "isNew"),
                                        isPopular = documentSnapshot.get(field = "isPopular"),
                                        isDiscounted = documentSnapshot.get(field = "isDiscounted"),
                                    )
                                }
                            send(RequestState.Success(products))
                        }
                } else {
                    send(RequestState.Error("User is not available."))
                }
            } catch (e: Exception) {
                send(RequestState.Error("Error while reading products: ${e.message}"))
            }
        }

    override suspend fun readProductById(id: String): RequestState<Product> =
        try {
            val userId = getCurrentUserId()
            if (userId != null) {
                val firestore = Firebase.firestore
                val productDocument =
                    firestore
                        .collection(collectionPath = "product")
                        .document(documentPath = id)
                        .get()
                if (productDocument.exists) {
                    val product =
                        Product(
                            id = productDocument.id,
                            createdAt = productDocument.get(field = "createdAt"),
                            title = productDocument.get(field = "title"),
                            description = productDocument.get(field = "description"),
                            thumbnail = productDocument.get(field = "thumbnail"),
                            category = productDocument.get(field = "category"),
                            flavours = productDocument.get(field = "flavours"),
                            weight = productDocument.get(field = "weight"),
                            price = productDocument.get(field = "price"),
                            isNew = productDocument.get(field = "isNew"),
                            isPopular = productDocument.get(field = "isPopular"),
                            isDiscounted = productDocument.get(field = "isDiscounted"),
                        )
                    RequestState.Success(product)
                } else {
                    RequestState.Error("Product does not exist.")
                }
            } else {
                RequestState.Error("User is not available.")
            }
        } catch (e: Exception) {
            RequestState.Error("Error while reading product: ${e.message}")
        }

    private fun extractFirebaseStoragePath(downloadUrl: String): String? {
        val startIndex = downloadUrl.indexOf("/o/") + 3
        if (startIndex < 3) return null

        val endIndex = downloadUrl.indexOf("?", startIndex)
        val encodedPath =
            if (endIndex != -1) {
                downloadUrl.substring(startIndex, endIndex)
            } else {
                downloadUrl.substring(startIndex)
            }
        return decodeFirebasePath(encodedPath)
    }

    private fun decodeFirebasePath(encodedPath: String): String =
        encodedPath
            .replace("%2F", "/")
            .replace("%20", " ")
}
