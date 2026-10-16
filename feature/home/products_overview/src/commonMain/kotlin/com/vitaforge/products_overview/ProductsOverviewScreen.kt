package com.vitaforge.products_overview

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tylerdev.vitaforge.shared.FontSize
import com.tylerdev.vitaforge.shared.TextMuted
import com.tylerdev.vitaforge.shared.TextPrimary
import com.tylerdev.vitaforge.shared.TextSecondary
import com.tylerdev.vitaforge.shared.components.ConnectionErrorState
import com.tylerdev.vitaforge.shared.components.EmptyProductsState
import com.tylerdev.vitaforge.shared.components.ProductCard
import com.tylerdev.vitaforge.shared.domain.Product
import com.tylerdev.vitaforge.shared.domain.ProductCategory
import com.tylerdev.vitaforge.shared.robotoCondensedFontFamily
import com.tylerdev.vitaforge.shared.util.DisplayResult
import com.vitaforge.products_overview.component.CarouselIndicator
import com.vitaforge.products_overview.component.CategoryChipRow
import com.vitaforge.products_overview.component.MainProductCard
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import vitaforge.shared.generated.resources.Res
import vitaforge.shared.generated.resources.home_category_no_discounts
import vitaforge.shared.generated.resources.home_departments_count
import vitaforge.shared.generated.resources.home_discounted_supplements
import vitaforge.shared.generated.resources.home_targeted_disciplines
import kotlin.math.abs

@Composable
fun ProductOverviewScreen() {
    val viewModel = koinViewModel<ProductOverviewViewModel>()
    val products by viewModel.products.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()
    var selectedCategory by rememberSaveable { mutableStateOf<ProductCategory?>(null) }

    val centeredIndex: Int? by remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val viewportCenter = layoutInfo.viewportStartOffset + layoutInfo.viewportEndOffset / 2
            layoutInfo.visibleItemsInfo
                .minByOrNull { item ->
                    val itemCenter = item.offset + item.size / 2
                    abs(itemCenter - viewportCenter)
                }?.index
        }
    }

    products.DisplayResult(
        modifier = Modifier.fillMaxSize(),
        onLoading = {
        },
        onSuccess = { productList ->
            AnimatedContent(
                targetState = productList.distinctBy { it.id },
            ) { uniqueProducts ->
                if (uniqueProducts.isNotEmpty()) {
                    val newProducts =
                        remember(uniqueProducts) {
                            uniqueProducts.filter { it.isNew }.sortedBy { it.createdAt }.take(MAX_NEW_PRODUCTS)
                        }
                    val discountedProducts =
                        remember(uniqueProducts, selectedCategory) {
                            uniqueProducts
                                .filter { it.isDiscounted && (selectedCategory == null || it.category == selectedCategory?.name) }
                                .sortedBy { it.createdAt }
                                .take(MAX_DISCOUNTED_PRODUCTS)
                        }
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        item(key = "new_carousel", contentType = "carousel") {
                            NewProductsCarousel(
                                products = newProducts,
                                listState = listState,
                                centeredIndex = centeredIndex,
                            )
                        }
                        item(key = "categories", contentType = "categories") {
                            CategoriesSection(
                                selectedCategory = selectedCategory,
                                onCategoryClick = { category ->
                                    selectedCategory = category.takeUnless { it == selectedCategory }
                                },
                            )
                        }
                        item(key = "discounted_title", contentType = "title") {
                            Text(
                                modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp),
                                text = stringResource(Res.string.home_discounted_supplements).uppercase(),
                                fontSize = FontSize.EXTRA_MEDIUM,
                                fontFamily = robotoCondensedFontFamily(),
                                color = TextPrimary,
                            )
                        }
                        val category = selectedCategory
                        if (category != null && discountedProducts.isEmpty()) {
                            item(key = "category_empty", contentType = "empty") {
                                Text(
                                    modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp),
                                    text = stringResource(Res.string.home_category_no_discounts, category.title.lowercase()),
                                    fontSize = FontSize.REGULAR,
                                    color = TextMuted,
                                )
                            }
                        }
                        items(
                            items = discountedProducts,
                            key = { it.id },
                            contentType = { "discounted_product" },
                        ) { product ->
                            ProductCard(
                                modifier = Modifier.padding(horizontal = 12.dp),
                                product = product,
                                onClick = {},
                            )
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

@Composable
private fun NewProductsCarousel(
    products: List<Product>,
    listState: LazyListState,
    centeredIndex: Int?,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        LazyRow(
            state = listState,
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            itemsIndexed(
                items = products,
                key = { _, item -> item.id },
                contentType = { _, _ -> "new_product" },
            ) { index, product ->
                val isLarge = index == centeredIndex
                val animatedScale by animateFloatAsState(
                    targetValue = if (isLarge) 1f else INACTIVE_CARD_SCALE,
                    animationSpec = tween(CARD_SCALE_DURATION_MS),
                )
                MainProductCard(
                    modifier =
                        Modifier
                            .scale(animatedScale)
                            .height(300.dp)
                            .fillParentMaxWidth(CARD_WIDTH_FRACTION),
                    product = product,
                    isLarge = isLarge,
                    onClick = {},
                )
            }
        }
        Spacer(Modifier.height(8.dp))
        CarouselIndicator(count = products.size, selectedIndex = centeredIndex)
    }
}

@Composable
private fun CategoriesSection(
    selectedCategory: ProductCategory?,
    onCategoryClick: (ProductCategory) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = stringResource(Res.string.home_targeted_disciplines).uppercase(),
                fontSize = FontSize.SMALL,
                fontFamily = robotoCondensedFontFamily(),
                color = TextMuted,
            )
            Text(
                text = stringResource(Res.string.home_departments_count, ProductCategory.entries.size),
                fontSize = FontSize.SMALL,
                fontFamily = robotoCondensedFontFamily(),
                color = TextSecondary,
            )
        }
        Spacer(Modifier.height(8.dp))
        CategoryChipRow(selectedCategory = selectedCategory, onCategoryClick = onCategoryClick)
    }
}

private const val MAX_NEW_PRODUCTS = 5
private const val MAX_DISCOUNTED_PRODUCTS = 3
private const val INACTIVE_CARD_SCALE = 0.8f
private const val CARD_SCALE_DURATION_MS = 300
private const val CARD_WIDTH_FRACTION = 0.6f
