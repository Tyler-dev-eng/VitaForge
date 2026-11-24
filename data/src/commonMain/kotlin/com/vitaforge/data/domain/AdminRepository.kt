package com.vitaforge.data.domain

import com.tylerdev.vitaforge.shared.domain.ImageFile
import com.tylerdev.vitaforge.shared.domain.Product
import com.tylerdev.vitaforge.shared.util.RequestState
import kotlinx.coroutines.flow.Flow

interface AdminRepository {
    fun getCurrentUserId(): String?

    suspend fun createNewProduct(product: Product): Result<Unit>

    suspend fun uploadImageToStorage(file: ImageFile): Result<String>

    suspend fun deleteImageFromStorage(downloadUrl: String): Result<Unit>

    fun readAllProducts(): Flow<RequestState<List<Product>>>

    suspend fun readProductById(id: String): Result<Product>

    suspend fun updateProductThumbnail(
        productId: String,
        downloadUrl: String,
    ): Result<Unit>

    suspend fun updateProduct(product: Product): Result<Unit>

    suspend fun deleteProduct(productId: String): Result<Unit>

    fun searchProductByTitle(searchQuery: String): Flow<RequestState<List<Product>>>
}
