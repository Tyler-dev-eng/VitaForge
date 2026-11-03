package com.vitaforge.category_search

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tylerdev.vitaforge.shared.domain.Product
import com.tylerdev.vitaforge.shared.domain.ProductCategory
import com.tylerdev.vitaforge.shared.util.RequestState
import com.vitaforge.data.domain.ProductRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlin.time.Duration.Companion.milliseconds

private val SearchDebounce = 300.milliseconds
private const val UNKNOWN_CATEGORY_MESSAGE = "Category not found."

class CategorySearchViewModel(
    private val productRepository: ProductRepository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()
    private val retryCount = MutableStateFlow(0)
    private val category: ProductCategory? =
        savedStateHandle.get<String>("category")?.let { name ->
            ProductCategory.entries.firstOrNull { it.name == name }
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    private val categoryProducts =
        retryCount
            .flatMapLatest {
                val category = category ?: return@flatMapLatest flowOf(RequestState.Error(UNKNOWN_CATEGORY_MESSAGE))
                productRepository
                    .readProductsByCategoryFlow(category = category)
                    .onStart { emit(RequestState.Loading) }
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = RequestState.Loading,
            )

    @OptIn(FlowPreview::class)
    val products =
        combine(
            categoryProducts,
            searchQuery.debounce(SearchDebounce),
        ) { state, query ->
            when (state) {
                is RequestState.Success -> RequestState.Success(state.data.filterByTitle(query))
                else -> state
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = RequestState.Loading,
        )

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun retry() {
        retryCount.update { it + 1 }
    }

    private fun List<Product>.filterByTitle(query: String): List<Product> =
        if (query.isBlank()) this else filter { it.title.contains(query.trim(), ignoreCase = true) }
}
