package com.tylerdev.vitaforge.shared.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.tylerdev.vitaforge.shared.Alpha
import com.tylerdev.vitaforge.shared.BorderIdle
import com.tylerdev.vitaforge.shared.FontSize
import com.tylerdev.vitaforge.shared.Resources
import com.tylerdev.vitaforge.shared.SurfaceLighter
import com.tylerdev.vitaforge.shared.TextPrimary
import com.tylerdev.vitaforge.shared.TextSecondary
import com.tylerdev.vitaforge.shared.domain.Product
import com.tylerdev.vitaforge.shared.robotoCondensedFontFamily
import org.jetbrains.compose.resources.painterResource

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
                .clip(RoundedCornerShape(size = 12.dp))
                .border(
                    width = 1.dp,
                    color = BorderIdle,
                    shape = RoundedCornerShape(12.dp),
                ).background(SurfaceLighter)
                .clickable { onClick(product.id) },
    ) {
        AsyncImage(
            modifier =
                Modifier
                    .width(120.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .border(
                        width = 1.dp,
                        color = BorderIdle,
                        shape = RoundedCornerShape(12.dp),
                    ),
            model =
                ImageRequest
                    .Builder(LocalPlatformContext.current)
                    .data(product.thumbnail)
                    .crossfade(true)
                    .build(),
            contentDescription = "Product Image",
            contentScale = ContentScale.Crop,
        )

        Column(
            modifier =
                Modifier
                    .weight(1f)
                    .padding(12.dp),
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = product.title,
                fontSize = FontSize.MEDIUM,
                color = TextPrimary,
                fontFamily = robotoCondensedFontFamily(),
                fontWeight = FontWeight.Medium,
            )
            Spacer(Modifier.height(4.dp))
            Text(
                modifier = Modifier.fillMaxWidth().alpha(Alpha.HALF),
                text = product.description,
                fontSize = FontSize.REGULAR,
                color = TextPrimary,
                fontFamily = robotoCondensedFontFamily(),
                fontWeight = FontWeight.Medium,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Row {
                    Icon(
                        modifier = Modifier.size(14.dp),
                        painter = painterResource(Resources.Icon.Weight),
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
                Text(
                    text = "R${product.price}",
                    fontSize = FontSize.EXTRA_REGULAR,
                    fontWeight = FontWeight.Medium,
                    color = TextSecondary,
                )
            }
        }
    }
}
