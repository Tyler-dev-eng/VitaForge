package com.vitaforge.data

import com.tylerdev.vitaforge.shared.domain.ImageFile
import com.tylerdev.vitaforge.shared.domain.Product
import com.tylerdev.vitaforge.shared.util.RequestState
import com.vitaforge.data.domain.AdminRepository
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.firestore.Direction
import dev.gitlive.firebase.firestore.firestore
import dev.gitlive.firebase.storage.storage
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.uuid.Uuid

private const val UPLOAD_TIMEOUT_MILLIS = 20_000L

class AdminRepositoryImpl : AdminRepository {
    override fun getCurrentUserId() = Firebase.auth.currentUser?.uid

    private fun requireUserId(): String = getCurrentUserId() ?: throw RepositoryException(USER_NOT_AVAILABLE)

    override suspend fun createNewProduct(product: Product): Result<Unit> =
        runRepository(errorPrefix = "Error while creating a new product") {
            requireUserId()
            Firebase.firestore
                .collection(collectionPath = "product")
                .document(product.id)
                .set(product.copy(title = product.title.lowercase()))
        }

    override suspend fun uploadImageToStorage(file: ImageFile): Result<String> =
        runRepository(errorPrefix = "Error while uploading the image") {
            requireUserId()
            val imagePath = Firebase.storage.reference.child(path = "images/${Uuid.random().toHexString()}")
            val downloadUrl =
                withTimeoutOrNull(timeMillis = UPLOAD_TIMEOUT_MILLIS) {
                    imagePath.putFile(file.toStorageFile())
                    imagePath.getDownloadUrl()
                }
            if (downloadUrl.isNullOrEmpty()) {
                throw RepositoryException("Failed to retrieve a download URL for the image after upload.")
            }
            downloadUrl
        }

    override suspend fun deleteImageFromStorage(downloadUrl: String): Result<Unit> =
        runRepository(errorPrefix = "Error while deleting a thumbnail") {
            val storagePath =
                extractFirebaseStoragePath(downloadUrl)
                    ?: throw RepositoryException("Storage Path is null.")
            Firebase.storage.reference(storagePath).delete()
        }

    override fun readAllProducts(): Flow<RequestState<List<Product>>> =
        channelFlow {
            try {
                val userId = getCurrentUserId()
                if (userId != null) {
                    val database = Firebase.firestore
                    database
                        .collection(collectionPath = "product")
                        .orderBy("createdAt", Direction.DESCENDING)
                        .snapshots
                        .collectLatest { query ->
                            val products =
                                query.documents.map { document ->
                                    Product(
                                        id = document.id,
                                        title = document.get(field = "title"),
                                        createdAt = document.get(field = "createdAt"),
                                        description = document.get(field = "description"),
                                        thumbnail = document.get(field = "thumbnail"),
                                        category = document.get(field = "category"),
                                        flavours = document.get(field = "flavours"),
                                        flavourImages = document.get(field = "flavourImages"),
                                        colours = document.get(field = "colours"),
                                        weight = document.get(field = "weight"),
                                        specsServing = document.get(field = "specsServing"),
                                        specs = document.get(field = "specs"),
                                        price = document.get(field = "price"),
                                        oldPrice = document.get(field = "oldPrice"),
                                        isPopular = document.get(field = "isPopular"),
                                        isDiscounted = document.get(field = "isDiscounted"),
                                        isNew = document.get(field = "isNew"),
                                    )
                                }
                            send(RequestState.Success(data = products.map { it.copy(title = it.title.uppercase()) }))
                        }
                } else {
                    send(RequestState.Error("User is not available."))
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                send(RequestState.Error("Error while reading the products. ${e.toFriendlyReason()}"))
            }
        }

    override suspend fun readProductById(id: String): Result<Product> =
        runRepository(errorPrefix = "Error while reading a selected product") {
            requireUserId()
            val productDocument =
                Firebase.firestore
                    .collection(collectionPath = "product")
                    .document(id)
                    .get()
            if (!productDocument.exists) throw RepositoryException("Selected product not found.")
            val product =
                Product(
                    id = productDocument.id,
                    title = productDocument.get(field = "title"),
                    createdAt = productDocument.get(field = "createdAt"),
                    description = productDocument.get(field = "description"),
                    thumbnail = productDocument.get(field = "thumbnail"),
                    category = productDocument.get(field = "category"),
                    flavours = productDocument.get(field = "flavours"),
                    flavourImages = productDocument.get(field = "flavourImages"),
                    colours = productDocument.get(field = "colours"),
                    weight = productDocument.get(field = "weight"),
                    specsServing = productDocument.get(field = "specsServing"),
                    specs = productDocument.get(field = "specs"),
                    price = productDocument.get(field = "price"),
                    oldPrice = productDocument.get(field = "oldPrice"),
                    isPopular = productDocument.get(field = "isPopular"),
                    isDiscounted = productDocument.get(field = "isDiscounted"),
                    isNew = productDocument.get(field = "isNew"),
                )
            product.copy(title = product.title.uppercase())
        }

    override suspend fun updateProductThumbnail(
        productId: String,
        downloadUrl: String,
    ): Result<Unit> =
        runRepository(errorPrefix = "Error while updating thumbnail") {
            requireUserId()
            val productDocument =
                Firebase.firestore
                    .collection(collectionPath = "product")
                    .document(documentPath = productId)
            if (!productDocument.get().exists) throw RepositoryException("Product does not exist.")
            productDocument.updateFields { "thumbnail" to downloadUrl }
        }

    override suspend fun updateProduct(product: Product): Result<Unit> =
        runRepository(errorPrefix = "Error while updating a product") {
            requireUserId()
            val productDocument =
                Firebase.firestore
                    .collection(collectionPath = "product")
                    .document(product.id)
            if (!productDocument.get().exists) throw RepositoryException("Selected Product not found.")
            productDocument.update(product.copy(title = product.title.lowercase()))
        }

    override suspend fun deleteProduct(productId: String): Result<Unit> =
        runRepository(errorPrefix = "Error while deleting product") {
            requireUserId()
            val productDocument =
                Firebase.firestore
                    .collection(collectionPath = "product")
                    .document(documentPath = productId)
            if (!productDocument.get().exists) throw RepositoryException("Product does not exist.")
            productDocument.delete()
        }

    override fun searchProductByTitle(searchQuery: String): Flow<RequestState<List<Product>>> =
        channelFlow {
            try {
                val userId = getCurrentUserId()
                if (userId != null) {
                    val database = Firebase.firestore

                    database
                        .collection(collectionPath = "product")
                        .snapshots
                        .collectLatest { query ->
                            val products =
                                query.documents.map { document ->
                                    Product(
                                        id = document.id,
                                        title = document.get(field = "title"),
                                        createdAt = document.get<Long?>(field = "createdAt") ?: 0L,
                                        description = document.get(field = "description"),
                                        thumbnail = document.get(field = "thumbnail"),
                                        category = document.get(field = "category"),
                                        flavours = document.get(field = "flavours"),
                                        flavourImages = document.get(field = "flavourImages"),
                                        colours = document.get(field = "colours"),
                                        weight = document.get(field = "weight"),
                                        specsServing = document.get(field = "specsServing"),
                                        specs = document.get(field = "specs"),
                                        price = document.get(field = "price"),
                                        oldPrice = document.get(field = "oldPrice"),
                                        isPopular = document.get(field = "isPopular"),
                                        isDiscounted = document.get(field = "isDiscounted"),
                                        isNew = document.get(field = "isNew"),
                                    )
                                }
                            send(
                                RequestState.Success(
                                    products
                                        .filter { it.title.contains(searchQuery.trim(), ignoreCase = true) }
                                        .map { it.copy(title = it.title.uppercase()) },
                                ),
                            )
                        }
                } else {
                    send(RequestState.Error("User is not available."))
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                send(RequestState.Error("Error while searching products. ${e.toFriendlyReason()}"))
            }
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
