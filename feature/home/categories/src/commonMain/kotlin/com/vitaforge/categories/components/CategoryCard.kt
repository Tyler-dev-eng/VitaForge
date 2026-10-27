package com.vitaforge.categories.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.tylerdev.vitaforge.shared.BorderIdle
import com.tylerdev.vitaforge.shared.FontSize
import com.tylerdev.vitaforge.shared.SurfaceLighter
import com.tylerdev.vitaforge.shared.TextPrimary
import com.tylerdev.vitaforge.shared.bebasNeueFontFamily
import com.tylerdev.vitaforge.shared.domain.ProductCategory

private val CardCornerRadius = 12.dp
private val CardBorderWidth = 1.dp
private val CardHorizontalPadding = 16.dp
private val CardVerticalPadding = 20.dp
private val CardMinHeight = 48.dp
private val ColourDotSize = 12.dp
private val ColourDotSpacing = 12.dp

@Composable
fun CategoryCard(
    category: ProductCategory,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    val shape = RoundedCornerShape(CardCornerRadius)
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .heightIn(min = CardMinHeight)
                .clip(shape)
                .background(SurfaceLighter)
                .border(width = CardBorderWidth, color = BorderIdle, shape = shape)
                .clickable(onClick = onClick)
                .padding(horizontal = CardHorizontalPadding, vertical = CardVerticalPadding),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier =
                Modifier
                    .size(ColourDotSize)
                    .clip(CircleShape)
                    .background(category.colour),
        )
        Spacer(modifier = Modifier.width(ColourDotSpacing))
        Text(
            text = category.title,
            fontSize = FontSize.EXTRA_MEDIUM,
            fontFamily = bebasNeueFontFamily(),
            color = TextPrimary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}
