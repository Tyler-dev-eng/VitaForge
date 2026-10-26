package com.tylerdev.vitaforge.shared.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tylerdev.vitaforge.shared.Alpha
import com.tylerdev.vitaforge.shared.ButtonDisabled
import com.tylerdev.vitaforge.shared.FontSize
import com.tylerdev.vitaforge.shared.IconPrimary
import com.tylerdev.vitaforge.shared.Resources
import com.tylerdev.vitaforge.shared.SurfaceBrand
import com.tylerdev.vitaforge.shared.SurfaceLighter
import com.tylerdev.vitaforge.shared.TextPrimary
import com.tylerdev.vitaforge.shared.domain.QuantityCounterSize
import org.jetbrains.compose.resources.painterResource

@Composable
fun QuantityCounter(
    modifier: Modifier = Modifier,
    size: QuantityCounterSize,
    value: String,
    onMinusClick: () -> Unit,
    onPlusClick: () -> Unit,
    isMinusEnabled: Boolean = true,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(size.spacing),
    ) {
        Box(
            modifier =
                Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(if (isMinusEnabled) SurfaceBrand else ButtonDisabled)
                    .clickable(enabled = isMinusEnabled) { onMinusClick() }
                    .padding(size.padding),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                modifier = Modifier.size(14.dp),
                painter = painterResource(Resources.Icon.Minus),
                contentDescription = "",
                tint = if (isMinusEnabled) IconPrimary else IconPrimary.copy(alpha = Alpha.DISABLED),
            )
        }
        Box(
            modifier =
                Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(SurfaceLighter)
                    .padding(size.padding),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "x$value",
                fontSize = FontSize.SMALL,
                lineHeight = FontSize.SMALL * 1,
                fontWeight = FontWeight.Medium,
                color = TextPrimary,
            )
        }
        Box(
            modifier =
                Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(SurfaceBrand)
                    .clickable { onPlusClick() }
                    .padding(size.padding),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                modifier = Modifier.size(14.dp),
                painter = painterResource(Resources.Icon.Plus),
                contentDescription = "",
                tint = IconPrimary,
            )
        }
    }
}
