package com.vitaforge.categories.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.tylerdev.vitaforge.shared.FontSize
import com.tylerdev.vitaforge.shared.IconPrimary
import com.tylerdev.vitaforge.shared.Resources
import com.tylerdev.vitaforge.shared.SurfaceDarker
import com.tylerdev.vitaforge.shared.TextPrimary
import com.tylerdev.vitaforge.shared.bebasNeueFontFamily
import com.tylerdev.vitaforge.shared.domain.ProductCategory
import org.jetbrains.compose.resources.painterResource

private val CardPadding = 16.dp
private val CardMinHeight = 48.dp
private val ContentSpacing = 14.dp
private val ColourDotSize = 10.dp
private val ChevronContainerSize = 32.dp
private val ChevronSize = 12.dp

@Composable
fun CategoryCard(
    category: ProductCategory,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .heightIn(min = CardMinHeight)
                .clickable(onClick = onClick)
                .padding(CardPadding),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(ContentSpacing),
    ) {
        Box(
            modifier =
                Modifier
                    .size(ColourDotSize)
                    .clip(CircleShape)
                    .background(category.colour),
        )
        Text(
            text = category.title.uppercase(),
            modifier = Modifier.weight(1f),
            fontSize = FontSize.EXTRA_REGULAR,
            fontFamily = bebasNeueFontFamily(),
            color = TextPrimary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        Box(
            modifier =
                Modifier
                    .size(ChevronContainerSize)
                    .clip(CircleShape)
                    .background(SurfaceDarker),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = painterResource(Resources.Icon.ChevronRight),
                contentDescription = null,
                modifier = Modifier.size(ChevronSize),
                tint = IconPrimary,
            )
        }
    }
}
