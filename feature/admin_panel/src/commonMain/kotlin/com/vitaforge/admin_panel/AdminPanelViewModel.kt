package com.vitaforge.admin_panel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tylerdev.vitaforge.shared.util.RequestState
import com.vitaforge.data.domain.AdminRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class AdminPanelViewModel(
    val adminRepository: AdminRepository,
) : ViewModel() {
    val products =
        adminRepository
            .readLastTenProducts()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = RequestState.Loading,
            )
}
