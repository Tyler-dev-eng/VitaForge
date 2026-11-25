package com.vitaforge.data.domain

import com.tylerdev.vitaforge.shared.domain.Flavour
import com.tylerdev.vitaforge.shared.util.RequestState
import kotlinx.coroutines.flow.Flow

interface FlavourRepository {
    fun observeFlavours(): Flow<RequestState<List<Flavour>>>

    suspend fun addFlavour(name: String): Result<Flavour>

    suspend fun deleteFlavour(name: String): Result<Unit>
}
