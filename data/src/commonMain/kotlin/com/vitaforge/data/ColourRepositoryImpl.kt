package com.vitaforge.data

import com.tylerdev.vitaforge.shared.domain.Colour
import com.tylerdev.vitaforge.shared.util.RequestState
import com.vitaforge.data.domain.ColourRepository
import kotlinx.coroutines.flow.Flow

internal class ColourRepositoryImpl : ColourRepository {
    private val options = NamedOptionCollection(path = "colours", label = "Colour", createOption = ::Colour)

    override fun observeColours(): Flow<RequestState<List<Colour>>> = options.observe()

    override suspend fun addColour(name: String): Result<Colour> = options.add(name)

    override suspend fun deleteColour(name: String): Result<Unit> = options.delete(name)
}

fun createColourRepository(): ColourRepository = ColourRepositoryImpl()
