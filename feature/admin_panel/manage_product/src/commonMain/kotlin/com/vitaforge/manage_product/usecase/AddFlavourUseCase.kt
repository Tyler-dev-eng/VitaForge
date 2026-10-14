package com.vitaforge.manage_product.usecase

import com.tylerdev.vitaforge.shared.domain.Flavour
import com.vitaforge.data.domain.FlavourRepository

class AddFlavourUseCase(
    private val flavourRepository: FlavourRepository,
) {
    suspend operator fun invoke(name: String): Result<Flavour> {
        val trimmedName = name.trim()
        if (trimmedName.isEmpty()) {
            return Result.failure(IllegalArgumentException("Flavour name is empty."))
        }
        return flavourRepository.addFlavour(trimmedName)
    }
}
