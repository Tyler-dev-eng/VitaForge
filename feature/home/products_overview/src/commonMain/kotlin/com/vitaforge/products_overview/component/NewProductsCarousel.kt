package com.vitaforge.products_overview.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import com.tylerdev.vitaforge.shared.domain.Product
import kotlinx.coroutines.delay

private val CardHeight = 300.dp
private val IndicatorSpacing = 8.dp
private const val CARD_WIDTH_FRACTION = 0.6f
private const val INACTIVE_CARD_SCALE = 0.8f
private const val CARD_SCALE_DURATION_MS = 300
private const val SLIDE_INTERVAL_MS = 3_000L
private const val SLIDE_DURATION_MS = 600

/** A looping slideshow of [products] that moves on by itself and pauses while the user drags it. */
@Composable
internal fun NewProductsCarousel(
    products: List<Product>,
    onProductClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (products.isEmpty()) return
    val isLooping = products.size > 1
    val pagerState =
        rememberPagerState(
            initialPage = if (isLooping) loopStartPage(products.size) else 0,
            pageCount = { if (isLooping) Int.MAX_VALUE else products.size },
        )
    if (isLooping) AutoSlide(pagerState)
    Column(modifier = modifier) {
        BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
            HorizontalPager(
                state = pagerState,
                contentPadding = PaddingValues(horizontal = maxWidth * ((1f - CARD_WIDTH_FRACTION) / 2f)),
            ) { page ->
                val product = products[page % products.size]
                CarouselCard(
                    product = product,
                    isCentred = page == pagerState.currentPage,
                    onClick = { onProductClick(product.id) },
                )
            }
        }
        Spacer(Modifier.height(IndicatorSpacing))
        CarouselIndicator(count = products.size, selectedIndex = pagerState.currentPage % products.size)
    }
}

/** The first page in the middle of the loop that shows the first product, so the user can swipe both ways. */
private fun loopStartPage(productCount: Int): Int {
    val middle = Int.MAX_VALUE / 2
    return middle - middle % productCount
}

@Composable
private fun AutoSlide(pagerState: PagerState) {
    val isDragged by pagerState.interactionSource.collectIsDraggedAsState()
    // Restarting on every settled page gives each card the full interval, including after a swipe.
    LaunchedEffect(pagerState, pagerState.settledPage, isDragged) {
        if (isDragged) return@LaunchedEffect
        delay(SLIDE_INTERVAL_MS)
        pagerState.animateScrollToPage(page = pagerState.settledPage + 1, animationSpec = tween(SLIDE_DURATION_MS))
    }
}

@Composable
private fun CarouselCard(
    product: Product,
    isCentred: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val animatedScale by animateFloatAsState(
        targetValue = if (isCentred) 1f else INACTIVE_CARD_SCALE,
        animationSpec = tween(CARD_SCALE_DURATION_MS),
    )
    MainProductCard(
        modifier = modifier.scale(animatedScale).height(CardHeight).fillMaxWidth(),
        product = product,
        isLarge = isCentred,
        onClick = { onClick() },
    )
}
