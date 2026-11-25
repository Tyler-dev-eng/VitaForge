package com.vitaforge.manage_product.usecase

import com.tylerdev.vitaforge.shared.domain.Colour
import com.tylerdev.vitaforge.shared.util.RequestState
import com.vitaforge.data.domain.ColourRepository
import kotlinx.coroutines.flow.Flow

class ObserveColoursUseCase(
    private val colourRepository: ColourRepository,
) {
    operator fun invoke(): Flow<RequestState<List<Colour>>> = colourRepository.observeColours()
}
