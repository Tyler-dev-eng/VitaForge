package com.vitaforge.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vitaforge.data.domain.CustomerRepository
import com.vitaforge.data.domain.errorMessage
import kotlinx.coroutines.launch

class AuthViewModel(
    private val customerRepository: CustomerRepository,
) : ViewModel() {
    fun createCustomer(
        id: String,
        displayName: String?,
        email: String?,
        onSuccess: () -> Unit,
        onError: (String) -> Unit,
    ) {
        viewModelScope.launch {
            val result = customerRepository.createCustomer(id = id, displayName = displayName, email = email)
            result.fold(onSuccess = { onSuccess() }, onFailure = { onError(result.errorMessage()) })
        }
    }
}
