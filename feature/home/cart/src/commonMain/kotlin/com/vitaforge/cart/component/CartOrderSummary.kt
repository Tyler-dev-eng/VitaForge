package com.vitaforge.cart.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tylerdev.vitaforge.shared.BorderIdle
import com.tylerdev.vitaforge.shared.FontSize
import com.tylerdev.vitaforge.shared.SurfaceLighter
import com.tylerdev.vitaforge.shared.TextMuted
import com.tylerdev.vitaforge.shared.TextOlive
import com.tylerdev.vitaforge.shared.TextPrimary
import com.tylerdev.vitaforge.shared.bebasNeueFontFamily
import com.tylerdev.vitaforge.shared.robotoCondensedFontFamily
import com.tylerdev.vitaforge.shared.util.formatAmount
import com.vitaforge.cart.CartSummary
import org.jetbrains.compose.resources.stringResource
import vitaforge.shared.generated.resources.Res
import vitaforge.shared.generated.resources.cart_price
import vitaforge.shared.generated.resources.cart_summary_shipping
import vitaforge.shared.generated.resources.cart_summary_shipping_free
import vitaforge.shared.generated.resources.cart_summary_subtotal
import vitaforge.shared.generated.resources.cart_summary_tax
import vitaforge.shared.generated.resources.cart_summary_title
import vitaforge.shared.generated.resources.cart_summary_total
import vitaforge.shared.generated.resources.cart_summary_total_note

private val SummaryShape = RoundedCornerShape(16.dp)
private val SummaryPadding = 12.dp

@Composable
fun CartOrderSummary(
    modifier: Modifier = Modifier,
    summary: CartSummary,
) {
    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .clip(SummaryShape)
                .background(SurfaceLighter)
                .padding(SummaryPadding),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = stringResource(Res.string.cart_summary_title).uppercase(),
            fontFamily = robotoCondensedFontFamily(),
            fontSize = FontSize.REGULAR,
            fontWeight = FontWeight.Medium,
            color = TextPrimary,
        )
        SummaryRow(
            label = stringResource(Res.string.cart_summary_subtotal),
            value = stringResource(Res.string.cart_price, formatAmount(summary.subtotal)),
        )
        SummaryRow(
            label = stringResource(Res.string.cart_summary_shipping),
            value = stringResource(Res.string.cart_summary_shipping_free),
            valueColor = TextOlive,
            valueWeight = FontWeight.Bold,
        )
        SummaryRow(
            label = stringResource(Res.string.cart_summary_tax),
            value = stringResource(Res.string.cart_price, formatAmount(summary.tax)),
        )
        HorizontalDivider(color = BorderIdle)
        SummaryTotal(total = stringResource(Res.string.cart_price, formatAmount(summary.total)))
    }
}

@Composable
private fun SummaryRow(
    label: String,
    value: String,
    valueColor: Color = TextPrimary,
    valueWeight: FontWeight = FontWeight.SemiBold,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = label,
            fontSize = FontSize.REGULAR,
            color = TextMuted,
        )
        Text(
            text = value,
            fontSize = FontSize.REGULAR,
            fontWeight = valueWeight,
            color = valueColor,
        )
    }
}

@Composable
private fun SummaryTotal(total: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom,
    ) {
        Column {
            Text(
                text = stringResource(Res.string.cart_summary_total).uppercase(),
                fontFamily = robotoCondensedFontFamily(),
                fontSize = FontSize.REGULAR,
                fontWeight = FontWeight.Medium,
                color = TextPrimary,
            )
            Text(
                text = stringResource(Res.string.cart_summary_total_note),
                fontSize = FontSize.EXTRA_SMALL,
                color = TextMuted,
            )
        }
        Text(
            text = total,
            fontFamily = bebasNeueFontFamily(),
            fontSize = FontSize.LARGE,
            color = TextPrimary,
        )
    }
}
