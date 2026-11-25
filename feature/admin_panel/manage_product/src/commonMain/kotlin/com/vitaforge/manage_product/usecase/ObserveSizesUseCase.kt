package com.vitaforge.manage_product.usecase

import com.tylerdev.vitaforge.shared.domain.Size
import com.tylerdev.vitaforge.shared.domain.SizeNameComparator
import com.tylerdev.vitaforge.shared.util.RequestState
import com.vitaforge.data.domain.SizeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ObserveSizesUseCase(
    private val sizeRepository: SizeRepository,
) {
    /** All sizes, smallest first. */
    operator fun invoke(): Flow<RequestState<List<Size>>> = sizeRepository.observeSizes().map { it.sortedBySize() }

    private fun RequestState<List<Size>>.sortedBySize(): RequestState<List<Size>> =
        if (this is RequestState.Success) {
            RequestState.Success(data.sortedWith(compareBy(SizeNameComparator) { it.name }))
        } else {
            this
        }
}
