package com.vitaforge.data

import com.tylerdev.vitaforge.shared.domain.Colour
import com.tylerdev.vitaforge.shared.util.RequestState
import com.vitaforge.data.domain.ColourRepository
import kotlinx.coroutines.flow.Flow

internal class ColourRepositoryImpl : ColourRepository {
    private val options = NamedOptionCollection(path = "colours", label = "Colour", createOption = ::Colour)

    override fun observeColours(): Flow<RequestState<List<Colour>>> = options.observe()

    override suspend fun addColour(name: String): Result<Colour> = options.add(name)
}

fun createColourRepository(): ColourRepository = ColourRepositoryImpl()
