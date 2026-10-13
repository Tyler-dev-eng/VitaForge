package com.vitaforge.admin_panel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tylerdev.vitaforge.shared.domain.Product
import com.tylerdev.vitaforge.shared.domain.ProductCategory
import com.tylerdev.vitaforge.shared.util.RequestState
import com.vitaforge.data.domain.AdminRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlin.time.Duration.Companion.milliseconds

class AdminPanelViewModel(
    val adminRepository: AdminRepository,
) : ViewModel() {
    private val _selectedCategory = MutableStateFlow<ProductCategory?>(null)
    val selectedCategory: StateFlow<ProductCategory?> = _selectedCategory.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val products =
        adminRepository
            .readLastTenProducts()
            .filterByCategory()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = RequestState.Loading,
            )

    fun selectCategory(category: ProductCategory?) {
        _selectedCategory.value = category
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val filteredProducts =
        searchQuery
            .debounce(500.milliseconds)
            .flatMapLatest { query ->
                if (query.isBlank()) {
                    products
                } else {
                    adminRepository.searchProductByTitle(query).filterByCategory()
                }
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = RequestState.Loading,
            )

    private fun Flow<RequestState<List<Product>>>.filterByCategory(): Flow<RequestState<List<Product>>> =
        combine(_selectedCategory) { state, category ->
            when (state) {
                is RequestState.Success -> RequestState.Success(state.data.filterBy(category))
                else -> state
            }
        }

    private fun List<Product>.filterBy(category: ProductCategory?): List<Product> =
        if (category == null) this else filter { it.category == category.name }
}
