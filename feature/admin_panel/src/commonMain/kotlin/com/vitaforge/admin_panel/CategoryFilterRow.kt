package com.vitaforge.admin_panel

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.tylerdev.vitaforge.shared.FontSize
import com.tylerdev.vitaforge.shared.SurfaceDarker
import com.tylerdev.vitaforge.shared.SurfaceSecondary
import com.tylerdev.vitaforge.shared.TextPrimary
import com.tylerdev.vitaforge.shared.TextWhite
import com.tylerdev.vitaforge.shared.domain.ProductCategory
import com.tylerdev.vitaforge.shared.robotoCondensedFontFamily
import org.jetbrains.compose.resources.stringResource
import vitaforge.shared.generated.resources.Res
import vitaforge.shared.generated.resources.admin_panel_filter_all

private val ChipHeight = 40.dp
private val DotSize = 8.dp

@Composable
internal fun CategoryFilterRow(
    modifier: Modifier = Modifier,
    selectedCategory: ProductCategory?,
    onCategorySelected: (ProductCategory?) -> Unit,
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        contentPadding =
            androidx.compose.foundation.layout
                .PaddingValues(horizontal = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        item(key = "all", contentType = "chip") {
            FilterChip(
                label = stringResource(Res.string.admin_panel_filter_all),
                dotColor = null,
                isSelected = selectedCategory == null,
                onClick = { onCategorySelected(null) },
            )
        }
        items(
            items = ProductCategory.entries,
            key = { it.name },
            contentType = { "chip" },
        ) { category ->
            FilterChip(
                label = category.title,
                dotColor = category.colour,
                isSelected = selectedCategory == category,
                onClick = { onCategorySelected(category) },
            )
        }
    }
}

@Composable
private fun FilterChip(
    label: String,
    dotColor: Color?,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier =
            modifier
                .height(ChipHeight)
                .clip(CircleShape)
                .background(if (isSelected) SurfaceSecondary else SurfaceDarker)
                .clickable(onClick = onClick)
                .padding(horizontal = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (dotColor != null) {
            Spacer(
                Modifier
                    .size(DotSize)
                    .clip(CircleShape)
                    .background(dotColor),
            )
            Spacer(Modifier.width(6.dp))
        }
        Text(
            text = label.uppercase(),
            fontSize = FontSize.SMALL,
            fontFamily = robotoCondensedFontFamily(),
            color = if (isSelected) TextWhite else TextPrimary,
        )
    }
}
