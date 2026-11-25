package com.vitaforge.manage_product.usecase

import com.tylerdev.vitaforge.shared.domain.Size
import com.vitaforge.data.domain.SizeRepository

class AddSizeUseCase(
    private val sizeRepository: SizeRepository,
) {
    suspend operator fun invoke(name: String): Result<Size> {
        val trimmedName = name.trim()
        if (trimmedName.isEmpty()) {
            return Result.failure(IllegalArgumentException("Size name is empty."))
        }
        return sizeRepository.addSize(trimmedName)
    }
}
