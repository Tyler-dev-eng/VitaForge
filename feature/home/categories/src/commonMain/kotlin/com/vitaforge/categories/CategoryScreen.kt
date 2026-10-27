package com.vitaforge.categories

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tylerdev.vitaforge.shared.domain.ProductCategory
import com.vitaforge.categories.components.CategoryCard

private val ScreenPadding = 12.dp
private val CardSpacing = 12.dp

@Composable
fun CategoryScreen(modifier: Modifier = Modifier) {
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .padding(ScreenPadding),
        verticalArrangement = Arrangement.spacedBy(CardSpacing),
    ) {
        ProductCategory.entries.forEach { category ->
            CategoryCard(category = category, onClick = {})
        }
    }
}
