package com.tylerdev.vitaforge.shared.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.tylerdev.vitaforge.shared.BorderIdle
import com.tylerdev.vitaforge.shared.FontSize
import com.tylerdev.vitaforge.shared.Resources
import com.tylerdev.vitaforge.shared.Surface
import com.tylerdev.vitaforge.shared.SurfaceBadgeNew
import com.tylerdev.vitaforge.shared.SurfaceBrand
import com.tylerdev.vitaforge.shared.TextMuted
import com.tylerdev.vitaforge.shared.TextOnBadgeNew
import com.tylerdev.vitaforge.shared.TextOnBrand
import com.tylerdev.vitaforge.shared.TextPrimary
import com.tylerdev.vitaforge.shared.TextSecondary
import com.tylerdev.vitaforge.shared.domain.Product
import com.tylerdev.vitaforge.shared.domain.ProductCategory
import com.tylerdev.vitaforge.shared.robotoCondensedFontFamily
import com.tylerdev.vitaforge.shared.util.formatAmount
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import vitaforge.shared.generated.resources.Res
import vitaforge.shared.generated.resources.cart_price
import vitaforge.shared.generated.resources.product_badge_new
import vitaforge.shared.generated.resources.product_badge_popular
import vitaforge.shared.generated.resources.product_badge_sale

private val CardShape = RoundedCornerShape(16.dp)
private val ImageShape = RoundedCornerShape(12.dp)
private val BadgeShape = RoundedCornerShape(50)

private data class ProductBadge(
    val label: StringResource,
    val containerColor: Color,
    val contentColor: Color,
)

private fun Product.badge(): ProductBadge? =
    when {
        isDiscounted -> ProductBadge(Res.string.product_badge_sale, SurfaceBrand, TextOnBrand)
        isNew -> ProductBadge(Res.string.product_badge_new, SurfaceBadgeNew, TextOnBadgeNew)
        isPopular -> ProductBadge(Res.string.product_badge_popular, SurfaceBrand, TextOnBrand)
        else -> null
    }

@Composable
fun ProductCard(
    modifier: Modifier = Modifier,
    product: Product,
    onClick: (String) -> Unit,
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .clip(CardShape)
                .border(width = 1.dp, color = BorderIdle, shape = CardShape)
                .background(Surface)
                .clickable { onClick(product.id) },
    ) {
        AsyncImage(
            modifier =
                Modifier
                    .width(120.dp)
                    .clip(ImageShape)
                    .border(
                        width = 1.dp,
                        color = BorderIdle,
                        shape = ImageShape,
                    ),
            model =
                ImageRequest
                    .Builder(LocalPlatformContext.current)
                    .data(product.coverImage)
                    .crossfade(true)
                    .build(),
            contentDescription = "Product Image",
            contentScale = ContentScale.Crop,
        )

        Column(
            modifier =
                Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(12.dp),
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = product.title.uppercase(),
                fontSize = FontSize.MEDIUM,
                color = TextPrimary,
                fontFamily = robotoCondensedFontFamily(),
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(Modifier.height(4.dp))
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = product.description,
                fontSize = FontSize.SMALL,
                lineHeight = FontSize.SMALL * 1.3,
                color = TextMuted,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(Modifier.weight(1f))
            ProductFooter(product = product)
        }
    }
}

@Composable
private fun ProductWeight(product: Product) {
    AnimatedContent(targetState = product.category) { category ->
        if (ProductCategory.valueOf(category) == ProductCategory.Accessories) {
            Spacer(modifier = Modifier.height(0.dp))
        } else {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    modifier = Modifier.size(14.dp),
                    painter = painterResource(Resources.Icon.Scales),
                    contentDescription = "Weight",
                    tint = TextPrimary,
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    text = "${product.weight}g",
                    fontSize = FontSize.EXTRA_SMALL,
                    color = TextPrimary,
                )
            }
        }
    }
}

@Composable
private fun ProductFooter(product: Product) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            ProductWeight(product = product)
            ProductBadge(badge = product.badge())
        }
        Column(horizontalAlignment = Alignment.End) {
            product.discountedFromPrice?.let { OldPriceText(oldPrice = it, fontSize = FontSize.EXTRA_SMALL) }
            Text(
                text = stringResource(Res.string.cart_price, formatAmount(product.price)),
                fontSize = FontSize.MEDIUM,
                fontWeight = FontWeight.Bold,
                color = TextSecondary,
            )
        }
    }
}

@Composable
private fun ProductBadge(badge: ProductBadge?) {
    if (badge == null) return
    Text(
        modifier =
            Modifier
                .clip(BadgeShape)
                .background(badge.containerColor)
                .padding(horizontal = 8.dp, vertical = 2.dp),
        text = stringResource(badge.label).uppercase(),
        fontSize = FontSize.EXTRA_SMALL,
        fontWeight = FontWeight.Bold,
        color = badge.contentColor,
    )
}
