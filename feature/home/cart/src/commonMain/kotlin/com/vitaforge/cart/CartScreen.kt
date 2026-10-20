package com.vitaforge.cart

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tylerdev.vitaforge.shared.util.DisplayResult
import com.tylerdev.vitaforge.shared.util.RequestState
import com.vitaforge.cart.component.CartItemCard
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CartScreen() {
    val viewModel = koinViewModel<CartViewModel>()
    val cartItemsWithProducts by viewModel.cartItemsWithProducts.collectAsStateWithLifecycle(RequestState.Loading)

    cartItemsWithProducts.DisplayResult(
        onLoading = {},
        onError = {},
        onSuccess = { data ->
            if (data.isNotEmpty()) {
                LazyColumn(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    items(
                        items = data,
                        key = { pair -> pair.first.id },
                        contentType = { "cart_item" },
                    ) { pair ->
                        CartItemCard(
                            cartItem = pair.first,
                            product = pair.second,
                            onMinusClick = { },
                            onPlusClick = { },
                            onDeleteClick = { },
                        )
                    }
                }
            } else {
                // TODO:: Show empty cart
            }
        },
    )
}
