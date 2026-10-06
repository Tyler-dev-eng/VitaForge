package com.vitaforge.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tylerdev.vitaforge.shared.domain.Country
import com.tylerdev.vitaforge.shared.domain.PhoneNumber
import com.tylerdev.vitaforge.shared.util.RequestState
import com.vitaforge.data.domain.CustomerRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class ProfileScreenState(
    val id: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val city: String? = null,
    val postalCode: Int? = null,
    val address: String? = null,
    val country: Country = Country.UnitedKingdom,
    val phoneNumber: PhoneNumber? = null,
)

class ProfileViewModel(
    private val customerRepository: CustomerRepository,
) : ViewModel() {
    var screenReady: RequestState<Unit> by mutableStateOf(RequestState.Loading)
    var screenState: ProfileScreenState by mutableStateOf(ProfileScreenState())
        private set

    private val customer =
        customerRepository.readCustomerFlow().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = RequestState.Loading,
        )

    init {
        viewModelScope.launch {
            customerRepository.readCustomerFlow().collectLatest { data ->
                if (data.isSuccess()) {
                    val fetchedCustomer = data.getSuccessData()
                    screenState =
                        ProfileScreenState(
                            id = fetchedCustomer.id,
                            firstName = fetchedCustomer.firstName,
                            lastName = fetchedCustomer.lastName,
                            email = fetchedCustomer.email,
                            city = fetchedCustomer.city,
                            postalCode = fetchedCustomer.postalCode,
                            address = fetchedCustomer.address,
                            phoneNumber = fetchedCustomer.phoneNumber,
                            country =
                                Country.entries.firstOrNull { it.dialCode == fetchedCustomer.phoneNumber?.dialCode }
                                    ?: Country.UnitedKingdom,
                        )
                    screenReady = RequestState.Success(Unit)
                } else if (data.isError()) {
                    screenReady = RequestState.Error(data.getErrorMessage())
                }
            }
        }
    }
}
