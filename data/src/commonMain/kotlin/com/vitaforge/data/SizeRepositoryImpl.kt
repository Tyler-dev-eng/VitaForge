package com.vitaforge.data

import com.tylerdev.vitaforge.shared.domain.Size
import com.tylerdev.vitaforge.shared.util.RequestState
import com.vitaforge.data.domain.SizeRepository
import kotlinx.coroutines.flow.Flow

internal class SizeRepositoryImpl : SizeRepository {
    private val options = NamedOptionCollection(path = "sizes", label = "Size", createOption = ::Size)

    override fun observeSizes(): Flow<RequestState<List<Size>>> = options.observe()

    override suspend fun addSize(name: String): Result<Size> = options.add(name)
}

fun createSizeRepository(): SizeRepository = SizeRepositoryImpl()
