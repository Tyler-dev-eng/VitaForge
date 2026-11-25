package com.vitaforge.data.domain

import com.tylerdev.vitaforge.shared.domain.Colour
import com.tylerdev.vitaforge.shared.util.RequestState
import kotlinx.coroutines.flow.Flow

interface ColourRepository {
    fun observeColours(): Flow<RequestState<List<Colour>>>

    suspend fun addColour(name: String): Result<Colour>
}
