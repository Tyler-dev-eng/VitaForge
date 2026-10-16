package com.vitaforge.products_overview.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.tylerdev.vitaforge.shared.Alpha
import com.tylerdev.vitaforge.shared.FontSize
import com.tylerdev.vitaforge.shared.IconWhite
import com.tylerdev.vitaforge.shared.Resources
import com.tylerdev.vitaforge.shared.SurfaceScrim
import com.tylerdev.vitaforge.shared.TextBrand
import com.tylerdev.vitaforge.shared.TextWhite
import com.tylerdev.vitaforge.shared.domain.Product
import com.tylerdev.vitaforge.shared.domain.ProductCategory
import com.tylerdev.vitaforge.shared.robotoCondensedFontFamily
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import vitaforge.shared.generated.resources.Res
import vitaforge.shared.generated.resources.product_card_price
import vitaforge.shared.generated.resources.product_card_thumbnail_description
import vitaforge.shared.generated.resources.product_card_weight
import vitaforge.shared.generated.resources.product_card_weight_icon_description

@Composable
fun MainProductCard(
    modifier: Modifier = Modifier,
    product: Product,
    isLarge: Boolean = false,
    onClick: (String) -> Unit,
) {
    val infiniteTransition = rememberInfiniteTransition()
    val animatedScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.25f,
        animationSpec =
            infiniteRepeatable(
                animation = tween(10000, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse,
            ),
    )

    val animatedRotation by
        infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 10f,
            animationSpec =
                infiniteRepeatable(
                    animation = tween(10000, easing = LinearEasing),
                    repeatMode = RepeatMode.Reverse,
                ),
        )

    Box(
        modifier =
            modifier
                .fillMaxHeight()
                .clip(RoundedCornerShape(12.dp))
                .clickable { onClick(product.id) },
    ) {
        AsyncImage(
            modifier =
                Modifier
                    .fillMaxSize()
                    .animateContentSize()
                    .then(
                        if (isLarge) {
                            Modifier
                                .scale(animatedScale)
                                .rotate(animatedRotation)
                        } else {
                            Modifier
                        },
                    ),
            model =
                ImageRequest
                    .Builder(LocalPlatformContext.current)
                    .data(product.thumbnail)
                    .crossfade(true)
                    .build(),
            contentDescription = stringResource(Res.string.product_card_thumbnail_description),
            contentScale = ContentScale.Crop,
        )
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(
                        brush =
                            Brush.verticalGradient(
                                colors =
                                    listOf(
                                        SurfaceScrim,
                                        SurfaceScrim.copy(Alpha.ZERO),
                                    ),
                                startY = Float.POSITIVE_INFINITY,
                                endY = 0.0f,
                            ),
                    ),
        )
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(12.dp),
            verticalArrangement = Arrangement.Bottom,
        ) {
            Text(
                text = product.title,
                fontSize = FontSize.EXTRA_MEDIUM,
                fontWeight = FontWeight.Medium,
                color = TextWhite,
                fontFamily = robotoCondensedFontFamily(),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = product.description,
                fontSize = FontSize.REGULAR,
                lineHeight = FontSize.REGULAR * 1.3f,
                color = TextWhite.copy(Alpha.HALF),
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                AnimatedContent(
                    targetState = product.category,
                ) { category ->
                    val weight = product.weight
                    if (weight == null || ProductCategory.valueOf(category) == ProductCategory.Accessories) {
                        Spacer(Modifier)
                    } else {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                modifier = Modifier.size(14.dp),
                                painter = painterResource(Resources.Icon.Weight),
                                contentDescription = stringResource(Res.string.product_card_weight_icon_description),
                                tint = IconWhite,
                            )
                            Spacer(Modifier.width(4.dp))
                            Text(
                                text = stringResource(Res.string.product_card_weight, weight),
                                fontSize = FontSize.EXTRA_SMALL,
                                color = TextWhite,
                            )
                        }
                    }
                }
                Text(
                    text = stringResource(Res.string.product_card_price, product.price),
                    fontSize = FontSize.EXTRA_REGULAR,
                    color = TextBrand,
                    fontWeight = FontWeight.Medium,
                )
            }
        }
    }
}
