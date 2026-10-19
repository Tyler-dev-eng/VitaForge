package com.vitaforge.details

sealed interface DetailsEvent {
    data object AddedToCart : DetailsEvent

    data class AddToCartFailed(
        val message: String,
    ) : DetailsEvent
}
