package com.vitaforge.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tylerdev.vitaforge.shared.util.RequestState
import com.vitaforge.data.domain.ProductRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class DetailsViewModel(
    private val productRepository: ProductRepository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {
    val product =
        productRepository
            .readProductByIdFlow(savedStateHandle.get<String>("id") ?: "")
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = RequestState.Idle,
            )
}
