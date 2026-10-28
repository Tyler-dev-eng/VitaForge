package com.vitaforge.category_search

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tylerdev.vitaforge.shared.domain.ProductCategory
import com.tylerdev.vitaforge.shared.util.RequestState
import com.vitaforge.data.domain.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn

class CategorySearchViewModel(
    private val productRepository: ProductRepository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()
    val products =
        productRepository
            .readProductsByCategoryFlow(
                category = ProductCategory.valueOf(savedStateHandle.get<String>("category")!!),
            ).stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = RequestState.Idle,
            )

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }
}
