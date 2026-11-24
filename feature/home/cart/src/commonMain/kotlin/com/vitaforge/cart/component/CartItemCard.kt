package com.vitaforge.cart.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.tylerdev.vitaforge.shared.FontSize
import com.tylerdev.vitaforge.shared.Resources
import com.tylerdev.vitaforge.shared.SurfaceLighter
import com.tylerdev.vitaforge.shared.TextMuted
import com.tylerdev.vitaforge.shared.TextPrimary
import com.tylerdev.vitaforge.shared.TextSecondary
import com.tylerdev.vitaforge.shared.components.QuantityCounter
import com.tylerdev.vitaforge.shared.domain.CartItem
import com.tylerdev.vitaforge.shared.domain.Product
import com.tylerdev.vitaforge.shared.domain.QuantityCounterSize
import com.tylerdev.vitaforge.shared.robotoCondensedFontFamily
import com.tylerdev.vitaforge.shared.util.formatAmount
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import vitaforge.shared.generated.resources.Res
import vitaforge.shared.generated.resources.cart_item_remove_description
import vitaforge.shared.generated.resources.cart_item_subtitle_separator
import vitaforge.shared.generated.resources.cart_item_thumbnail_description
import vitaforge.shared.generated.resources.cart_price
import vitaforge.shared.generated.resources.cart_price_each
import vitaforge.shared.generated.resources.cart_weight_grams
import vitaforge.shared.generated.resources.cart_weight_kilograms

private val CardShape = RoundedCornerShape(16.dp)
private val CardPadding = 12.dp
private val ThumbnailSize = 80.dp
private val DeleteIconSize = 18.dp
private const val GRAMS_PER_KILOGRAM = 1000
private const val MIN_QUANTITY = 1
private const val MAX_TITLE_LINES = 2

@Composable
fun CartItemCard(
    modifier: Modifier = Modifier,
    product: Product,
    cartItem: CartItem,
    onMinusClick: () -> Unit,
    onPlusClick: () -> Unit,
    onDeleteClick: () -> Unit,
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .clip(CardShape)
                .background(SurfaceLighter)
                .padding(CardPadding),
        horizontalArrangement = Arrangement.spacedBy(CardPadding),
    ) {
        CartItemThumbnail(product = product, flavour = cartItem.flavour)
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            CartItemHeader(
                product = product,
                cartItem = cartItem,
                onDeleteClick = onDeleteClick,
            )
            CartItemFooter(
                product = product,
                cartItem = cartItem,
                onMinusClick = onMinusClick,
                onPlusClick = onPlusClick,
            )
        }
    }
}

@Composable
private fun CartItemThumbnail(
    product: Product,
    flavour: String?,
) {
    AsyncImage(
        modifier =
            Modifier
                .size(ThumbnailSize)
                .clip(CardShape),
        model =
            ImageRequest
                .Builder(LocalPlatformContext.current)
                .data(product.imageFor(flavour))
                .crossfade(true)
                .build(),
        contentDescription = stringResource(Res.string.cart_item_thumbnail_description, product.title),
        contentScale = ContentScale.Crop,
    )
}

@Composable
private fun CartItemHeader(
    product: Product,
    cartItem: CartItem,
    onDeleteClick: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = product.title.uppercase(),
                fontFamily = robotoCondensedFontFamily(),
                fontSize = FontSize.REGULAR,
                color = TextPrimary,
                fontWeight = FontWeight.Medium,
                maxLines = MAX_TITLE_LINES,
                overflow = TextOverflow.Ellipsis,
            )
            cartItemSubtitle(product = product, cartItem = cartItem)?.let { subtitle ->
                Text(
                    text = subtitle,
                    fontSize = FontSize.SMALL,
                    color = TextMuted,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
        IconButton(onClick = onDeleteClick) {
            Icon(
                modifier = Modifier.size(DeleteIconSize),
                painter = painterResource(Resources.Icon.TrashCan),
                contentDescription = stringResource(Res.string.cart_item_remove_description, product.title),
                tint = TextMuted,
            )
        }
    }
}

@Composable
private fun CartItemFooter(
    product: Product,
    cartItem: CartItem,
    onMinusClick: () -> Unit,
    onPlusClick: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom,
    ) {
        Column {
            product.discountedFromPrice?.let { oldPrice ->
                Text(
                    text = stringResource(Res.string.cart_price, formatAmount(oldPrice * cartItem.quantity)),
                    fontSize = FontSize.EXTRA_SMALL,
                    color = TextMuted,
                    textDecoration = TextDecoration.LineThrough,
                )
            }
            Text(
                text = stringResource(Res.string.cart_price, formatAmount(product.price * cartItem.quantity)),
                fontSize = FontSize.MEDIUM,
                color = TextSecondary,
                fontWeight = FontWeight.Bold,
            )
            if (cartItem.quantity > 1) {
                Text(
                    text =
                        stringResource(
                            Res.string.cart_price_each,
                            stringResource(Res.string.cart_price, formatAmount(product.price)),
                        ),
                    fontSize = FontSize.EXTRA_SMALL,
                    color = TextMuted,
                )
            }
        }
        QuantityCounter(
            size = QuantityCounterSize.Small,
            value = cartItem.quantity.toString(),
            onMinusClick = onMinusClick,
            onPlusClick = onPlusClick,
            isMinusEnabled = cartItem.quantity > MIN_QUANTITY,
        )
    }
}

@Composable
private fun cartItemSubtitle(
    product: Product,
    cartItem: CartItem,
): String? {
    val weight =
        product.weight?.let { grams ->
            if (grams >= GRAMS_PER_KILOGRAM) {
                val kilograms = (grams.toDouble() / GRAMS_PER_KILOGRAM).toString().removeSuffix(".0")
                stringResource(Res.string.cart_weight_kilograms, kilograms)
            } else {
                stringResource(Res.string.cart_weight_grams, grams)
            }
        }
    return listOfNotNull(cartItem.flavour, weight)
        .takeIf { it.isNotEmpty() }
        ?.joinToString(separator = stringResource(Res.string.cart_item_subtitle_separator))
}
