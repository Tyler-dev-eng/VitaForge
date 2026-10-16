package com.vitaforge.products_overview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tylerdev.vitaforge.shared.util.RequestState
import com.vitaforge.data.domain.ProductRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class ProductOverviewViewModel(
    productRepository: ProductRepository,
) : ViewModel() {
    private val retryCount = MutableStateFlow(0)

    @OptIn(ExperimentalCoroutinesApi::class)
    val products =
        retryCount
            .flatMapLatest {
                combine(
                    productRepository.readNewProducts(),
                    productRepository.readDiscountedProducts(),
                ) { new, discounted ->
                    when {
                        new.isSuccess() && discounted.isSuccess() -> {
                            RequestState.Success(new.getSuccessData() + discounted.getSuccessData())
                        }

                        new.isError() -> new

                        discounted.isError() -> discounted

                        else -> RequestState.Loading
                    }
                }.onStart { emit(RequestState.Loading) }
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = RequestState.Loading,
            )

    fun retry() {
        retryCount.update { it + 1 }
    }
}
