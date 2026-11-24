package com.vitaforge.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tylerdev.vitaforge.shared.BorderIdle
import com.tylerdev.vitaforge.shared.FontSize
import com.tylerdev.vitaforge.shared.TextMuted
import com.tylerdev.vitaforge.shared.TextPrimary
import com.tylerdev.vitaforge.shared.TextSecondary
import com.tylerdev.vitaforge.shared.bebasNeueFontFamily
import com.tylerdev.vitaforge.shared.domain.Product
import org.jetbrains.compose.resources.stringResource
import vitaforge.shared.generated.resources.Res
import vitaforge.shared.generated.resources.details_specs_title

private val SpecRowPadding = 10.dp

@Composable
internal fun SpecsTable(
    product: Product,
    modifier: Modifier = Modifier,
) {
    val specs = product.specs.orEmpty()
    if (specs.isEmpty()) return
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = SpecRowPadding),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(Res.string.details_specs_title).uppercase(),
                fontFamily = bebasNeueFontFamily(),
                fontSize = FontSize.MEDIUM,
                color = TextPrimary,
            )
            product.specsServing?.let { serving ->
                Text(text = serving, fontSize = FontSize.SMALL, color = TextMuted)
            }
        }
        specs.forEach { spec ->
            HorizontalDivider(color = BorderIdle)
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = SpecRowPadding),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(text = spec.label, fontSize = FontSize.REGULAR, color = TextSecondary)
                Text(
                    text = spec.value,
                    fontSize = FontSize.REGULAR,
                    fontWeight = FontWeight.Medium,
                    color = TextPrimary,
                )
            }
        }
    }
}
