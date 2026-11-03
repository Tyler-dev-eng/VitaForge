package com.vitaforge.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tylerdev.vitaforge.shared.util.RequestState
import com.vitaforge.data.domain.CustomerRepository
import com.vitaforge.data.domain.errorMessage
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

    val cartItemCount: StateFlow<Int> =
        customerRepository
            .readCustomerFlow()
            .map { state ->
                state
                    .getSuccessDataOrNull()
                    ?.cart
                    ?.sumOf { it.quantity }
                    ?: 0
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(SUBSCRIPTION_TIMEOUT_MS),
                initialValue = 0,
            )

    val customer =
        customerRepository.readCustomerFlow().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(SUBSCRIPTION_TIMEOUT_MS),
            initialValue = RequestState.Loading,
        )

    fun signOut(
        onSuccess: () -> Unit,
        onError: (String) -> Unit,
    ) {
        viewModelScope.launch {
            val result = customerRepository.signOut()

            result.fold(onSuccess = { onSuccess() }, onFailure = { onError(result.errorMessage()) })
        }
    }
}
