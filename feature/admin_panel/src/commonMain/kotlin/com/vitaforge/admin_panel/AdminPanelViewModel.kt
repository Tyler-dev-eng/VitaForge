package com.vitaforge.admin_panel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tylerdev.vitaforge.shared.domain.Product
import com.tylerdev.vitaforge.shared.domain.ProductCategory
import com.tylerdev.vitaforge.shared.util.RequestState
import com.vitaforge.data.domain.AdminRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class AdminPanelViewModel(
    val adminRepository: AdminRepository,
) : ViewModel() {
    private val _selectedCategory = MutableStateFlow<ProductCategory?>(null)
    val selectedCategory: StateFlow<ProductCategory?> = _selectedCategory.asStateFlow()

    val products =
        combine(
            adminRepository.readLastTenProducts(),
            _selectedCategory,
        ) { state, category ->
            when (state) {
                is RequestState.Success -> RequestState.Success(state.data.filterBy(category))
                else -> state
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = RequestState.Loading,
        )

    fun selectCategory(category: ProductCategory?) {
        _selectedCategory.value = category
    }

    private fun List<Product>.filterBy(category: ProductCategory?): List<Product> =
        if (category == null) this else filter { it.category == category.name }
}
