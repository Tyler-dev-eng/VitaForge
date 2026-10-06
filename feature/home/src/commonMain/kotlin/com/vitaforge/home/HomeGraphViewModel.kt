package com.vitaforge.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vitaforge.data.domain.CustomerRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

private const val SUBSCRIPTION_TIMEOUT_MS = 5_000L

class HomeGraphViewModel(
    private val customerRepository: CustomerRepository,
) : ViewModel() {
    val userName: StateFlow<String> =
        customerRepository
            .readCustomerFlow()
            .map { state ->
                state
                    .getSuccessDataOrNull()
                    ?.let { "${it.firstName} ${it.lastName}".trim() }
                    .orEmpty()
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(SUBSCRIPTION_TIMEOUT_MS),
                initialValue = "",
            )

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
