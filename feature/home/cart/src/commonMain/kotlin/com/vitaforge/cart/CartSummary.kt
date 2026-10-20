package com.vitaforge.cart

import androidx.compose.runtime.Immutable
import com.tylerdev.vitaforge.shared.domain.CartItem
import com.tylerdev.vitaforge.shared.domain.Product

private const val ESTIMATED_TAX_RATE = 0.08

@Immutable
data class CartSummary(
    val subtotal: Double = 0.0,
    val tax: Double = 0.0,
    val total: Double = 0.0,
)

fun List<Pair<CartItem, Product>>.toCartSummary(): CartSummary {
    val subtotal = sumOf { (cartItem, product) -> product.price * cartItem.quantity }
    val tax = subtotal * ESTIMATED_TAX_RATE
    return CartSummary(
        subtotal = subtotal,
        tax = tax,
        total = subtotal + tax,
    )
}
