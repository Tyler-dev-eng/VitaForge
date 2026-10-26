package com.vitaforge.cart.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.tylerdev.vitaforge.shared.FontSize
import com.tylerdev.vitaforge.shared.IconSecondary
import com.tylerdev.vitaforge.shared.Resources
import com.tylerdev.vitaforge.shared.Surface
import com.tylerdev.vitaforge.shared.SurfaceBrand
import com.tylerdev.vitaforge.shared.SurfaceDarker
import com.tylerdev.vitaforge.shared.TextMuted
import com.tylerdev.vitaforge.shared.TextPrimary
import com.tylerdev.vitaforge.shared.bebasNeueFontFamily
import com.tylerdev.vitaforge.shared.components.PrimaryButton
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import vitaforge.shared.generated.resources.Res
import vitaforge.shared.generated.resources.cart_empty_guarantee_subtitle
import vitaforge.shared.generated.resources.cart_empty_guarantee_title
import vitaforge.shared.generated.resources.cart_empty_hplc_subtitle
import vitaforge.shared.generated.resources.cart_empty_hplc_title
import vitaforge.shared.generated.resources.cart_empty_illustration
import vitaforge.shared.generated.resources.cart_empty_message
import vitaforge.shared.generated.resources.cart_empty_shipping_subtitle
import vitaforge.shared.generated.resources.cart_empty_shipping_title
import vitaforge.shared.generated.resources.cart_empty_start_shopping
import vitaforge.shared.generated.resources.cart_empty_status
import vitaforge.shared.generated.resources.cart_empty_title

private val ScreenPadding = 16.dp
private val SectionSpacing = 24.dp
private val CardShape = RoundedCornerShape(24.dp)
private val CardElevation = 2.dp
private val CardPadding = 16.dp
private val IllustrationSize = 144.dp
private val StatusDotSize = 6.dp
private val StatusPillShape = CircleShape
private val FeatureIconContainerSize = 32.dp
private val FeatureIconSize = 18.dp

@Composable
fun EmptyCartState(
    onStartShoppingClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(ScreenPadding),
        verticalArrangement = Arrangement.spacedBy(SectionSpacing),
    ) {
        EmptyCartCard(onStartShoppingClick = onStartShoppingClick)
        EmptyCartFeatures()
    }
}

@Composable
private fun EmptyCartCard(
    onStartShoppingClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .shadow(CardElevation, CardShape)
                .clip(CardShape)
                .background(Surface)
                .padding(CardPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(CardPadding),
    ) {
        Image(
            modifier = Modifier.size(IllustrationSize),
            painter = painterResource(Resources.Image.EmptyCart),
            contentDescription = stringResource(Res.string.cart_empty_illustration),
        )
        EmptyCartStatusPill()
        Text(
            text = stringResource(Res.string.cart_empty_title).uppercase(),
            fontFamily = bebasNeueFontFamily(),
            fontSize = FontSize.EXTRA_MEDIUM,
            color = TextPrimary,
            textAlign = TextAlign.Center,
        )
        Text(
            text = stringResource(Res.string.cart_empty_message),
            fontSize = FontSize.SMALL,
            color = TextMuted,
            textAlign = TextAlign.Center,
        )
        PrimaryButton(
            text = stringResource(Res.string.cart_empty_start_shopping).uppercase(),
            icon = Resources.Icon.ArrowRight,
            onClick = onStartShoppingClick,
        )
    }
}

@Composable
private fun EmptyCartStatusPill(modifier: Modifier = Modifier) {
    Row(
        modifier =
            modifier
                .clip(StatusPillShape)
                .background(SurfaceDarker)
                .padding(horizontal = 10.dp, vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Box(Modifier.size(StatusDotSize).clip(CircleShape).background(SurfaceBrand))
        Text(
            text = stringResource(Res.string.cart_empty_status).uppercase(),
            fontSize = FontSize.EXTRA_SMALL,
            fontWeight = FontWeight.SemiBold,
            color = TextMuted,
        )
    }
}

@Composable
private fun EmptyCartFeatures(modifier: Modifier = Modifier) {
    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .shadow(CardElevation, CardShape)
                .clip(CardShape)
                .background(Surface)
                .padding(CardPadding),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        EmptyCartFeatureRow(
            icon = Resources.Icon.DeliveryTruck,
            title = stringResource(Res.string.cart_empty_shipping_title),
            subtitle = stringResource(Res.string.cart_empty_shipping_subtitle),
        )
        HorizontalDivider(color = SurfaceDarker)
        EmptyCartFeatureRow(
            icon = Resources.Icon.HplcTested,
            title = stringResource(Res.string.cart_empty_hplc_title),
            subtitle = stringResource(Res.string.cart_empty_hplc_subtitle),
        )
        HorizontalDivider(color = SurfaceDarker)
        EmptyCartFeatureRow(
            icon = Resources.Icon.Verified,
            title = stringResource(Res.string.cart_empty_guarantee_title),
            subtitle = stringResource(Res.string.cart_empty_guarantee_subtitle),
        )
    }
}

@Composable
private fun EmptyCartFeatureRow(
    icon: DrawableResource,
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Box(
            modifier = Modifier.size(FeatureIconContainerSize).clip(CircleShape).background(SurfaceDarker),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                modifier = Modifier.size(FeatureIconSize),
                painter = painterResource(icon),
                contentDescription = null,
                tint = IconSecondary,
            )
        }
        Column {
            Text(
                text = title.uppercase(),
                fontSize = FontSize.SMALL,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary,
            )
            Text(
                text = subtitle,
                fontSize = FontSize.EXTRA_SMALL,
                color = TextMuted,
            )
        }
    }
}
