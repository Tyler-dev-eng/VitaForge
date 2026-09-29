package com.vitaforge.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vitaforge.data.domain.CustomerRepository
import dev.gitlive.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch

class AuthViewModel(
    private val customerRepository: CustomerRepository,
) : ViewModel() {
    fun createCustomer(
        user: FirebaseUser?,
        onSuccess: () -> Unit,
        onError: (String) -> Unit,
    ) {
        viewModelScope.launch {
            customerRepository.createCustomer(user, onSuccess, onError)
        }
    }
}
