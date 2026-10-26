package com.vitaforge.cart

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tylerdev.vitaforge.shared.FontSize
import com.tylerdev.vitaforge.shared.IconOlive
import com.tylerdev.vitaforge.shared.Resources
import com.tylerdev.vitaforge.shared.TextMuted
import com.tylerdev.vitaforge.shared.components.PrimaryButton
import com.tylerdev.vitaforge.shared.domain.CartItem
import com.tylerdev.vitaforge.shared.domain.Product
import com.tylerdev.vitaforge.shared.util.DisplayResult
import com.tylerdev.vitaforge.shared.util.RequestState
import com.vitaforge.cart.component.CartItemCard
import com.vitaforge.cart.component.CartOrderSummary
import com.vitaforge.cart.component.EmptyCartState
import com.vitaforge.cart.component.FreeShippingBanner
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import rememberMessageBarState
import vitaforge.shared.generated.resources.Res
import vitaforge.shared.generated.resources.cart_checkout
import vitaforge.shared.generated.resources.cart_guarantee
import vitaforge.shared.generated.resources.cart_guarantee_icon_description
import vitaforge.shared.generated.resources.cart_price

private val ScreenPadding = 16.dp
private val SectionSpacing = 12.dp
private val GuaranteeIconSize = 16.dp

@Composable
fun CartScreen(onStartShoppingClick: () -> Unit) {
    val viewModel = koinViewModel<CartViewModel>()
    val cartItemsWithProducts by viewModel.cartItemsWithProducts.collectAsStateWithLifecycle(RequestState.Loading)
    val cartSummary by viewModel.cartSummary.collectAsStateWithLifecycle()
    val messageBarState = rememberMessageBarState()

    cartItemsWithProducts.DisplayResult(
        onLoading = {},
        onError = {},
        onSuccess = { data ->
            if (data.isNotEmpty()) {
                CartContent(
                    items = data,
                    summary = cartSummary,
                    viewModel = viewModel,
                )
            } else {
                EmptyCartState(onStartShoppingClick = onStartShoppingClick)
            }
        },
        transitionSpec = fadeIn() togetherWith fadeOut(),
    )
}

@Composable
private fun CartContent(
    modifier: Modifier = Modifier,
    items: List<Pair<CartItem, Product>>,
    summary: CartSummary,
    viewModel: CartViewModel,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(ScreenPadding),
        verticalArrangement = Arrangement.spacedBy(SectionSpacing),
    ) {
        item(key = "free_shipping", contentType = "banner") {
            FreeShippingBanner()
        }
        items(
            items = items,
            key = { pair -> pair.first.id },
            contentType = { "cart_item" },
        ) { pair ->
            CartItemCard(
                cartItem = pair.first,
                product = pair.second,
                onMinusClick = {
                    viewModel.updateCartItemQuantity(
                        id = pair.first.id,
                        quantity = pair.first.quantity - 1,
                        onSuccess = { },
                        onError = {
                        },
                    )
                },
                onPlusClick = {
                    viewModel.updateCartItemQuantity(
                        id = pair.first.id,
                        quantity = pair.first.quantity + 1,
                        onSuccess = { },
                        onError = { },
                    )
                },
                onDeleteClick = {
                    viewModel.deleteCartItem(
                        id = pair.first.id,
                        onSuccess = { },
                        onError = { },
                    )
                },
            )
        }
        item(key = "summary", contentType = "summary") {
            CartOrderSummary(summary = summary)
        }
        item(key = "checkout", contentType = "checkout") {
            CartCheckout(total = summary.total)
        }
    }
}

@Composable
private fun CartCheckout(
    modifier: Modifier = Modifier,
    total: Double,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(SectionSpacing),
    ) {
        PrimaryButton(
            text =
                stringResource(
                    Res.string.cart_checkout,
                    stringResource(Res.string.cart_price, formatAmount(total)),
                ),
            onClick = { },
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                modifier = Modifier.size(GuaranteeIconSize),
                painter = painterResource(Resources.Icon.Verified),
                contentDescription = stringResource(Res.string.cart_guarantee_icon_description),
                tint = IconOlive,
            )
            Text(
                modifier = Modifier.padding(start = 4.dp),
                text = stringResource(Res.string.cart_guarantee).uppercase(),
                fontSize = FontSize.EXTRA_SMALL,
                color = TextMuted,
                textAlign = TextAlign.Center,
            )
        }
    }
}
