package com.vitaforge.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tylerdev.vitaforge.shared.util.RequestState
import com.vitaforge.data.domain.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class DetailsViewModel(
    productRepository: ProductRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    val product =
        productRepository
            .readProductByIdFlow(savedStateHandle.get<String>("id") ?: "")
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = RequestState.Idle,
            )

    private val _quantity = MutableStateFlow(MIN_QUANTITY)
    val quantity: StateFlow<Int> = _quantity.asStateFlow()

    fun incrementQuantity() {
        _quantity.update { (it + 1).coerceAtMost(MAX_QUANTITY) }
    }

    fun decrementQuantity() {
        _quantity.update { (it - 1).coerceAtLeast(MIN_QUANTITY) }
    }

    private companion object {
        const val MIN_QUANTITY = 1
        const val MAX_QUANTITY = 10
    }
}
