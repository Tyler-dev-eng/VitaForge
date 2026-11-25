package com.vitaforge.manage_product.usecase

import com.tylerdev.vitaforge.shared.domain.Colour
import com.vitaforge.data.domain.ColourRepository

class AddColourUseCase(
    private val colourRepository: ColourRepository,
) {
    suspend operator fun invoke(name: String): Result<Colour> {
        val trimmedName = name.trim()
        if (trimmedName.isEmpty()) {
            return Result.failure(IllegalArgumentException("Colour name is empty."))
        }
        return colourRepository.addColour(trimmedName)
    }
}
