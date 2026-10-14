package com.vitaforge.products_overview

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tylerdev.vitaforge.shared.Alpha
import com.tylerdev.vitaforge.shared.FontSize
import com.tylerdev.vitaforge.shared.TextPrimary
import com.tylerdev.vitaforge.shared.components.ConnectionErrorState
import com.tylerdev.vitaforge.shared.components.EmptyProductsState
import com.tylerdev.vitaforge.shared.components.ProductCard
import com.tylerdev.vitaforge.shared.util.DisplayResult
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProductOverviewScreen() {
    val viewModel = koinViewModel<ProductOverviewViewModel>()
    val products by viewModel.products.collectAsStateWithLifecycle()

    products.DisplayResult(
        modifier = Modifier.fillMaxSize(),
        onLoading = {
        },
        onSuccess = { productList ->
            AnimatedContent(
                targetState = productList,
            ) {
                if (productList.isNotEmpty()) {
                    Column {
                        Spacer(Modifier.height(24.dp))
                        Text(
                            modifier = Modifier.fillMaxWidth().alpha(Alpha.HALF),
                            text = "Discounted Products",
                            fontSize = FontSize.EXTRA_REGULAR,
                            color = TextPrimary,
                            textAlign = TextAlign.Center,
                        )
                        Spacer(Modifier.height(16.dp))
                        LazyColumn(
                            modifier = Modifier.padding(horizontal = 12.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                        ) {
                            items(
                                items = productList,
                                key = { it.id },
                            ) { product ->
                                ProductCard(product = product, onClick = {})
                            }
                        }
                    }
                } else {
                    EmptyProductsState(onRefreshClick = viewModel::retry)
                }
            }
        },
        onError = {
            ConnectionErrorState(onRetryClick = viewModel::retry)
        },
    )
}
