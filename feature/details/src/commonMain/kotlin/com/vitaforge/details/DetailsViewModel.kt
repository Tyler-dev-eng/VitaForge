package com.vitaforge.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tylerdev.vitaforge.shared.domain.CartItem
import com.tylerdev.vitaforge.shared.util.RequestState
import com.vitaforge.data.domain.CustomerRepository
import com.vitaforge.data.domain.ProductRepository
import com.vitaforge.data.domain.errorMessage
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DetailsViewModel(
    productRepository: ProductRepository,
    private val customerRepository: CustomerRepository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {
    val product =
        productRepository
            .readProductByIdFlow(savedStateHandle.get<String>("id") ?: "")
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = RequestState.Idle,
            )

    private val _selectedFlavour = MutableStateFlow<String?>(null)
    val selectedFlavour: StateFlow<String?> = _selectedFlavour.asStateFlow()

    fun selectFlavour(flavour: String) {
        _selectedFlavour.value = flavour
    }

    private val _isAddingToCart = MutableStateFlow(false)
    val isAddingToCart: StateFlow<Boolean> = _isAddingToCart.asStateFlow()

    private val eventChannel = Channel<DetailsEvent>(Channel.BUFFERED)
    val events: Flow<DetailsEvent> = eventChannel.receiveAsFlow()

    fun addItemToCart() {
        val productId = savedStateHandle.get<String>("id")
        if (productId == null || _isAddingToCart.value) return
        viewModelScope.launch {
            _isAddingToCart.value = true
            val cartItem =
                CartItem(
                    productId = productId,
                    flavour = selectedFlavour.value,
                    quantity = ADDED_QUANTITY,
                )
            val result = customerRepository.addItemToCart(cartItem)
            val event =
                if (result.isSuccess) DetailsEvent.AddedToCart else DetailsEvent.AddToCartFailed(result.errorMessage())
            _isAddingToCart.value = false
            eventChannel.send(event)
        }
    }

    private companion object {
        const val ADDED_QUANTITY = 1
    }
}
