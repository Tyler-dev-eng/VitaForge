package com.vitaforge.products_overview.component

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
import androidx.compose.ui.unit.dp
import com.tylerdev.vitaforge.shared.FontSize
import com.tylerdev.vitaforge.shared.SurfaceDarker
import com.tylerdev.vitaforge.shared.TextPrimary
import com.tylerdev.vitaforge.shared.domain.ProductCategory
import com.tylerdev.vitaforge.shared.robotoCondensedFontFamily

private val ChipHeight = 40.dp
private val DotSize = 10.dp

@Composable
fun CategoryChipRow(
    modifier: Modifier = Modifier,
    onCategoryClick: (ProductCategory) -> Unit,
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(
            items = ProductCategory.entries,
            key = { it.name },
            contentType = { "chip" },
        ) { category ->
            Row(
                modifier =
                    Modifier
                        .height(ChipHeight)
                        .clip(CircleShape)
                        .background(SurfaceDarker)
                        .clickable { onCategoryClick(category) }
                        .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Spacer(
                    Modifier
                        .size(DotSize)
                        .clip(CircleShape)
                        .background(category.colour),
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = category.title.uppercase(),
                    fontSize = FontSize.SMALL,
                    fontFamily = robotoCondensedFontFamily(),
                    color = TextPrimary,
                )
            }
        }
    }
}
