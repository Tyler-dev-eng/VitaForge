package com.tylerdev.vitaforge.shared.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.TextUnit
import com.tylerdev.vitaforge.shared.TextMuted
import com.tylerdev.vitaforge.shared.util.formatAmount
import org.jetbrains.compose.resources.stringResource
import vitaforge.shared.generated.resources.Res
import vitaforge.shared.generated.resources.product_card_price

/** A struck-through pre-discount price. */
@Composable
fun OldPriceText(
    oldPrice: Double,
    fontSize: TextUnit,
    modifier: Modifier = Modifier,
) {
    Text(
        modifier = modifier,
        text = stringResource(Res.string.product_card_price, formatAmount(oldPrice)),
        fontSize = fontSize,
        color = TextMuted,
        textDecoration = TextDecoration.LineThrough,
    )
}
