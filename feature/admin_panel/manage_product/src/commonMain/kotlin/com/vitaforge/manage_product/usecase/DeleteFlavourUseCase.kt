package com.vitaforge.manage_product.usecase

import com.vitaforge.data.domain.FlavourRepository

class DeleteFlavourUseCase(
    private val flavourRepository: FlavourRepository,
) {
    suspend operator fun invoke(name: String): Result<Unit> = flavourRepository.deleteFlavour(name)
}
