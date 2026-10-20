package com.vitaforge.cart.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.tylerdev.vitaforge.shared.Alpha
import com.tylerdev.vitaforge.shared.FontSize
import com.tylerdev.vitaforge.shared.IconOlive
import com.tylerdev.vitaforge.shared.Resources
import com.tylerdev.vitaforge.shared.SurfaceBrand
import com.tylerdev.vitaforge.shared.TextOlive
import com.tylerdev.vitaforge.shared.TextPrimary
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import vitaforge.shared.generated.resources.Res
import vitaforge.shared.generated.resources.cart_free_shipping_detail
import vitaforge.shared.generated.resources.cart_free_shipping_icon_description
import vitaforge.shared.generated.resources.cart_free_shipping_title

private val BannerShape = RoundedCornerShape(16.dp)
private val BannerPaddingHorizontal = 12.dp
private val BannerPaddingVertical = 8.dp
private val BannerIconSize = 20.dp

@Composable
fun FreeShippingBanner(modifier: Modifier = Modifier) {
    val title = stringResource(Res.string.cart_free_shipping_title)
    val detail = stringResource(Res.string.cart_free_shipping_detail)
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .clip(BannerShape)
                .background(SurfaceBrand.copy(alpha = Alpha.TWENTY_PERCENT))
                .padding(horizontal = BannerPaddingHorizontal, vertical = BannerPaddingVertical),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            modifier = Modifier.size(BannerIconSize),
            painter = painterResource(Resources.Icon.DeliveryTruck),
            contentDescription = stringResource(Res.string.cart_free_shipping_icon_description),
            tint = IconOlive,
        )
        Text(
            text =
                buildAnnotatedString {
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = TextOlive)) { append(title) }
                    append(detail)
                },
            fontSize = FontSize.EXTRA_SMALL,
            color = TextPrimary,
        )
    }
}
