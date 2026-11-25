package com.vitaforge.data.domain

import com.tylerdev.vitaforge.shared.domain.Size
import com.tylerdev.vitaforge.shared.util.RequestState
import kotlinx.coroutines.flow.Flow

interface SizeRepository {
    fun observeSizes(): Flow<RequestState<List<Size>>>

    suspend fun addSize(name: String): Result<Size>

    suspend fun deleteSize(name: String): Result<Unit>
}
