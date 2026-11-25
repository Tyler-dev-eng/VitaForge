package com.vitaforge.manage_product.usecase

import com.vitaforge.data.domain.ColourRepository

class DeleteColourUseCase(
    private val colourRepository: ColourRepository,
) {
    suspend operator fun invoke(name: String): Result<Unit> = colourRepository.deleteColour(name)
}
