package com.vitaforge.manage_product.usecase

import com.tylerdev.vitaforge.shared.domain.Flavour
import com.tylerdev.vitaforge.shared.util.RequestState
import com.vitaforge.data.domain.FlavourRepository
import kotlinx.coroutines.flow.Flow

internal class ObserveFlavoursUseCase(
    private val flavourRepository: FlavourRepository,
) {
    operator fun invoke(): Flow<RequestState<List<Flavour>>> = flavourRepository.observeFlavours()
}
