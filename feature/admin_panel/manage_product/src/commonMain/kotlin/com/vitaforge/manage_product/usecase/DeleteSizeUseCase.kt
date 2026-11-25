package com.vitaforge.manage_product.usecase

import com.vitaforge.data.domain.SizeRepository

class DeleteSizeUseCase(
    private val sizeRepository: SizeRepository,
) {
    suspend operator fun invoke(name: String): Result<Unit> = sizeRepository.deleteSize(name)
}
