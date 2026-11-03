package com.vitaforge.data

import com.tylerdev.vitaforge.shared.domain.Product
import com.tylerdev.vitaforge.shared.domain.ProductCategory
import com.tylerdev.vitaforge.shared.util.RequestState
import com.vitaforge.data.domain.ProductRepository
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.firestore.firestore
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest

class ProductRepositoryImpl : ProductRepository {
    override fun getCurrentUserId() = Firebase.auth.currentUser?.uid

    override fun readNewProducts(): Flow<RequestState<List<Product>>> =
        channelFlow {
            try {
                val userId = getCurrentUserId()
                if (userId != null) {
                    val database = Firebase.firestore
                    database
                        .collection(collectionPath = "product")
                        .where { "isNew" equalTo true }
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
                                        weight = document.get(field = "weight"),
                                        price = document.get(field = "price"),
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
                send(RequestState.Error("Error while reading new and discounted products: ${e.message}"))
            }
        }

    override fun readProductByIdFlow(id: String): Flow<RequestState<Product>> =
        channelFlow {
            try {
                val userId = getCurrentUserId()
                if (userId != null) {
                    val database = Firebase.firestore
                    database
                        .collection(collectionPath = "product")
                        .document(id)
                        .snapshots
                        .collectLatest { document ->
                            if (document.exists) {
                                val product =
                                    Product(
                                        id = document.id,
                                        title = document.get(field = "title"),
                                        createdAt = document.get(field = "createdAt"),
                                        description = document.get(field = "description"),
                                        thumbnail = document.get(field = "thumbnail"),
                                        category = document.get(field = "category"),
                                        flavours = document.get(field = "flavours"),
                                        weight = document.get(field = "weight"),
                                        price = document.get(field = "price"),
                                        isPopular = document.get(field = "isPopular"),
                                        isDiscounted = document.get(field = "isDiscounted"),
                                        isNew = document.get(field = "isNew"),
                                    )
                                send(RequestState.Success(product))
                            } else {
                                send(RequestState.Error("Product not found."))
                            }
                        }
                } else {
                    send(RequestState.Error("User is not available."))
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                send(RequestState.Error(" Error while reading product ${e.message}"))
            }
        }

    override fun readProductByIdsFlow(ids: List<String>): Flow<RequestState<List<Product>>> =
        channelFlow {
            try {
                val userId = getCurrentUserId()
                if (userId != null) {
                    val database = Firebase.firestore
                    val productCollection = database.collection(collectionPath = "product")
                    val allProducts = mutableListOf<Product>()
                    val chunks = ids.chunked(10)

                    chunks.forEachIndexed { index, chunk ->
                        productCollection
                            .where { "id" inArray chunk }
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
                                            weight = document.get(field = "weight"),
                                            price = document.get(field = "price"),
                                            isPopular = document.get(field = "isPopular"),
                                            isDiscounted = document.get(field = "isDiscounted"),
                                            isNew = document.get(field = "isNew"),
                                        )
                                    }
                                allProducts.addAll(products)

                                if (index == chunks.lastIndex) {
                                    send(RequestState.Success(allProducts))
                                }
                            }
                    }
                } else {
                    send(RequestState.Error("User is not available."))
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                send(RequestState.Error(" Error while reading product ${e.message}"))
            }
        }

    override fun readProductsByCategoryFlow(category: ProductCategory): Flow<RequestState<List<Product>>> =
        channelFlow {
            try {
                val userId = getCurrentUserId()
                if (userId != null) {
                    val database = Firebase.firestore
                    database
                        .collection(collectionPath = "product")
                        .where { "category" equalTo category.name }
                        .snapshots
                        .collectLatest { query ->
                            val products =
                                query.documents.map {
                                    Product(
                                        id = it.id,
                                        title = it.get(field = "title"),
                                        createdAt = it.get(field = "createdAt"),
                                        description = it.get(field = "description"),
                                        thumbnail = it.get(field = "thumbnail"),
                                        category = it.get(field = "category"),
                                        flavours = it.get(field = "flavours"),
                                        weight = it.get(field = "weight"),
                                        price = it.get(field = "price"),
                                        isPopular = it.get(field = "isPopular"),
                                        isDiscounted = it.get(field = "isDiscounted"),
                                        isNew = it.get(field = "isNew"),
                                    )
                                }
                            send(RequestState.Success(products.map { it.copy(title = it.title.uppercase()) }))
                        }
                } else {
                    send(RequestState.Error("User is not available."))
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                send(RequestState.Error(" Error while reading product ${e.message}"))
            }
        }

    override fun readDiscountedProducts(): Flow<RequestState<List<Product>>> =
        channelFlow {
            try {
                val userId = getCurrentUserId()
                if (userId != null) {
                    val database = Firebase.firestore
                    database
                        .collection(collectionPath = "product")
                        .where { "isDiscounted" equalTo true }
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
                                        weight = document.get(field = "weight"),
                                        price = document.get(field = "price"),
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
                send(RequestState.Error("Error while reading new and discounted products: ${e.message}"))
            }
        }
}
