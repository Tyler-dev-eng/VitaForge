package com.vitaforge.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vitaforge.data.domain.CustomerRepository
import kotlinx.coroutines.launch

class HomeGraphViewModel(
    private val customerRepository: CustomerRepository,
) : ViewModel() {
    fun signOut(
        onSuccess: () -> Unit,
        onError: (String) -> Unit,
    ) {
        viewModelScope.launch {
            val result = customerRepository.signOut()

            if (result.isSuccess()) {
                onSuccess()
            } else {
                onError(result.getErrorMessage())
            }
        }
    }
}
