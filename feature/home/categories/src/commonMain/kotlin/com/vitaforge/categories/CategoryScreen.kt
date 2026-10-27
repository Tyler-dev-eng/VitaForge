package com.vitaforge.categories

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.tylerdev.vitaforge.shared.Surface
import com.tylerdev.vitaforge.shared.SurfaceDarker
import com.tylerdev.vitaforge.shared.domain.ProductCategory
import com.vitaforge.categories.components.CategoryCard

private val ScreenPadding = 16.dp
private val ListCornerRadius = 16.dp
private val DividerThickness = 1.dp

@Composable
fun CategoryScreen(modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxSize().padding(ScreenPadding)) {
        Column(
            modifier =
                Modifier
                    .clip(RoundedCornerShape(ListCornerRadius))
                    .background(Surface),
        ) {
            ProductCategory.entries.forEachIndexed { index, category ->
                if (index > 0) {
                    HorizontalDivider(thickness = DividerThickness, color = SurfaceDarker)
                }
                CategoryCard(category = category, onClick = {})
            }
        }
    }
}
