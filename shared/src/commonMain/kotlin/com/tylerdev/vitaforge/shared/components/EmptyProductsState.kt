package com.tylerdev.vitaforge.shared.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.tylerdev.vitaforge.shared.FontSize
import com.tylerdev.vitaforge.shared.IconBrick
import com.tylerdev.vitaforge.shared.IconOlive
import com.tylerdev.vitaforge.shared.IconPrimary
import com.tylerdev.vitaforge.shared.Resources
import com.tylerdev.vitaforge.shared.Surface
import com.tylerdev.vitaforge.shared.SurfaceBadgeNew
import com.tylerdev.vitaforge.shared.SurfaceBrand
import com.tylerdev.vitaforge.shared.SurfaceDarker
import com.tylerdev.vitaforge.shared.SurfaceLighter
import com.tylerdev.vitaforge.shared.TextMuted
import com.tylerdev.vitaforge.shared.TextPrimary
import com.tylerdev.vitaforge.shared.TextSecondary
import com.tylerdev.vitaforge.shared.bebasNeueFontFamily
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import vitaforge.shared.generated.resources.Res
import vitaforge.shared.generated.resources.no_products_illustration
import vitaforge.shared.generated.resources.no_products_message
import vitaforge.shared.generated.resources.no_products_refresh
import vitaforge.shared.generated.resources.no_products_status
import vitaforge.shared.generated.resources.no_products_title
import vitaforge.shared.generated.resources.verification_badge
import vitaforge.shared.generated.resources.verification_blends_subtitle
import vitaforge.shared.generated.resources.verification_blends_title
import vitaforge.shared.generated.resources.verification_hplc_subtitle
import vitaforge.shared.generated.resources.verification_hplc_title
import vitaforge.shared.generated.resources.verification_sport_subtitle
import vitaforge.shared.generated.resources.verification_sport_title
import vitaforge.shared.generated.resources.verification_title

private val ScreenPadding = 24.dp
private val SectionSpacing = 24.dp
private val ContentSpacing = 16.dp
private val IllustrationSize = 160.dp
private val IllustrationImageSize = 96.dp
private val OrbitDotSize = 8.dp
private val OrbitDotInset = 4.dp
private val StatusDotSize = 10.dp
private val StatusPillOffset = 16.dp
private val StatusPillElevation = 2.dp
private val CardShape = RoundedCornerShape(16.dp)
private val CardElevation = 2.dp
private val CardIconContainerSize = 40.dp
private val CardIconSize = 20.dp

@Composable
fun EmptyProductsState(
    onRefreshClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(ScreenPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(SectionSpacing),
    ) {
        EmptyProductsIllustration()
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(ContentSpacing),
        ) {
            Text(
                text = stringResource(Res.string.no_products_title).uppercase(),
                fontFamily = bebasNeueFontFamily(),
                fontSize = FontSize.LARGE,
                color = TextPrimary,
                textAlign = TextAlign.Center,
            )
            Text(
                text = stringResource(Res.string.no_products_message),
                fontSize = FontSize.EXTRA_REGULAR,
                color = TextMuted,
                textAlign = TextAlign.Center,
            )
        }
        PrimaryButton(
            text = stringResource(Res.string.no_products_refresh).uppercase(),
            icon = Resources.Icon.Reset,
            onClick = onRefreshClick,
        )
        VerificationProtocol()
    }
}

@Composable
private fun EmptyProductsIllustration(modifier: Modifier = Modifier) {
    Box(modifier = modifier.size(IllustrationSize)) {
        Box(
            modifier = Modifier.fillMaxSize().clip(CircleShape).background(SurfaceLighter),
            contentAlignment = Alignment.Center,
        ) {
            Image(
                modifier = Modifier.size(IllustrationImageSize),
                painter = painterResource(Resources.Icon.CatSleep),
                contentDescription = stringResource(Res.string.no_products_illustration),
            )
        }
        OrbitDot(Modifier.align(Alignment.TopCenter).padding(top = OrbitDotInset))
        OrbitDot(Modifier.align(Alignment.BottomCenter).padding(bottom = OrbitDotInset))
        OrbitDot(Modifier.align(Alignment.CenterStart).padding(start = OrbitDotInset))
        OrbitDot(Modifier.align(Alignment.CenterEnd).padding(end = OrbitDotInset))
        StatusPill(Modifier.align(Alignment.BottomCenter).offset(y = StatusPillOffset))
    }
}

@Composable
private fun OrbitDot(modifier: Modifier = Modifier) {
    Box(modifier.size(OrbitDotSize).clip(CircleShape).background(SurfaceBadgeNew))
}

@Composable
private fun StatusPill(modifier: Modifier = Modifier) {
    Row(
        modifier =
            modifier
                .shadow(StatusPillElevation, CircleShape)
                .clip(CircleShape)
                .background(Surface)
                .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Box(Modifier.size(StatusDotSize).clip(CircleShape).background(SurfaceBrand))
        Text(
            text = stringResource(Res.string.no_products_status).uppercase(),
            fontSize = FontSize.SMALL,
            color = TextMuted,
        )
    }
}

@Composable
private fun VerificationProtocol(modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(ContentSpacing)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(Res.string.verification_title).uppercase(),
                fontFamily = bebasNeueFontFamily(),
                fontSize = FontSize.REGULAR,
                color = TextMuted,
            )
            Text(
                text = stringResource(Res.string.verification_badge).uppercase(),
                fontFamily = bebasNeueFontFamily(),
                fontSize = FontSize.REGULAR,
                color = TextSecondary,
            )
        }
        Row(
            modifier = Modifier.height(IntrinsicSize.Min),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            VerificationCard(
                modifier = Modifier.weight(1f).fillMaxHeight(),
                icon = Resources.Icon.RedCheck,
                iconTint = Color.Unspecified,
                title = stringResource(Res.string.verification_hplc_title),
                subtitle = stringResource(Res.string.verification_hplc_subtitle),
            )
            VerificationCard(
                modifier = Modifier.weight(1f).fillMaxHeight(),
                icon = Resources.Icon.TrustShield,
                iconTint = IconOlive,
                title = stringResource(Res.string.verification_sport_title),
                subtitle = stringResource(Res.string.verification_sport_subtitle),
            )
            VerificationCard(
                modifier = Modifier.weight(1f).fillMaxHeight(),
                icon = Resources.Icon.HplcTested,
                iconTint = IconBrick,
                title = stringResource(Res.string.verification_blends_title),
                subtitle = stringResource(Res.string.verification_blends_subtitle),
            )
        }
    }
}

@Composable
private fun VerificationCard(
    icon: DrawableResource,
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    iconTint: Color = IconPrimary,
) {
    Column(
        modifier =
            modifier
                .shadow(CardElevation, CardShape)
                .clip(CardShape)
                .background(Surface)
                .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Box(
            modifier = Modifier.size(CardIconContainerSize).clip(CircleShape).background(SurfaceDarker),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                modifier = Modifier.size(CardIconSize),
                painter = painterResource(icon),
                contentDescription = null,
                tint = iconTint,
            )
        }
        Text(
            text = title.uppercase(),
            fontFamily = bebasNeueFontFamily(),
            fontSize = FontSize.REGULAR,
            color = TextPrimary,
            textAlign = TextAlign.Center,
        )
        Text(
            text = subtitle,
            fontSize = FontSize.SMALL,
            color = TextMuted,
            textAlign = TextAlign.Center,
        )
    }
}
