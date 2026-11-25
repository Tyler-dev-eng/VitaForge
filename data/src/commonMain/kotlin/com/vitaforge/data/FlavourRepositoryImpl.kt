package com.vitaforge.data

import com.tylerdev.vitaforge.shared.domain.Flavour
import com.tylerdev.vitaforge.shared.util.RequestState
import com.vitaforge.data.domain.FlavourRepository
import kotlinx.coroutines.flow.Flow

internal class FlavourRepositoryImpl : FlavourRepository {
    private val options = NamedOptionCollection(path = "flavours", label = "Flavour", createOption = ::Flavour)

    override fun observeFlavours(): Flow<RequestState<List<Flavour>>> = options.observe()

    override suspend fun addFlavour(name: String): Result<Flavour> = options.add(name)

    override suspend fun deleteFlavour(name: String): Result<Unit> = options.delete(name)
}

fun createFlavourRepository(): FlavourRepository = FlavourRepositoryImpl()
