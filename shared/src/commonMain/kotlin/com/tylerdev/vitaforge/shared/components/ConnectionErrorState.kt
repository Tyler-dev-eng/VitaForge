package com.tylerdev.vitaforge.shared.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.tylerdev.vitaforge.shared.BorderIdle
import com.tylerdev.vitaforge.shared.FontSize
import com.tylerdev.vitaforge.shared.Resources
import com.tylerdev.vitaforge.shared.Surface
import com.tylerdev.vitaforge.shared.SurfaceError
import com.tylerdev.vitaforge.shared.SurfaceLighter
import com.tylerdev.vitaforge.shared.TextMuted
import com.tylerdev.vitaforge.shared.TextPrimary
import com.tylerdev.vitaforge.shared.bebasNeueFontFamily
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import vitaforge.shared.generated.resources.Res
import vitaforge.shared.generated.resources.connection_error_illustration
import vitaforge.shared.generated.resources.connection_error_message
import vitaforge.shared.generated.resources.connection_error_retry
import vitaforge.shared.generated.resources.connection_error_title

private val ScreenPadding = 24.dp
private val ContentSpacing = 16.dp
private val IllustrationContainerSize = 112.dp
private val IllustrationSize = 64.dp
private val BadgeSize = 36.dp
private val BadgeIconSize = 16.dp
private val BadgeElevation = 4.dp

@Composable
fun ConnectionErrorState(
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize().padding(ScreenPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(ContentSpacing, Alignment.CenterVertically),
    ) {
        ConnectionErrorIllustration()
        Text(
            text = stringResource(Res.string.connection_error_title).uppercase(),
            fontFamily = bebasNeueFontFamily(),
            fontSize = FontSize.LARGE,
            color = TextPrimary,
            textAlign = TextAlign.Center,
        )
        Text(
            text = stringResource(Res.string.connection_error_message),
            fontSize = FontSize.EXTRA_REGULAR,
            color = TextMuted,
            textAlign = TextAlign.Center,
        )
        PrimaryButton(
            text = stringResource(Res.string.connection_error_retry).uppercase(),
            icon = Resources.Icon.Reset,
            onClick = onRetryClick,
        )
    }
}

@Composable
private fun ConnectionErrorIllustration(modifier: Modifier = Modifier) {
    Box(modifier = modifier.size(IllustrationContainerSize)) {
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
                    .background(SurfaceLighter)
                    .border(width = 1.dp, color = BorderIdle, shape = CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Image(
                modifier = Modifier.size(IllustrationSize),
                painter = painterResource(Resources.Image.ProfileError),
                contentDescription = stringResource(Res.string.connection_error_illustration),
            )
        }
        Box(
            modifier =
                Modifier
                    .align(Alignment.BottomEnd)
                    .size(BadgeSize)
                    .shadow(BadgeElevation, CircleShape)
                    .clip(CircleShape)
                    .background(Surface),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                modifier = Modifier.size(BadgeIconSize),
                painter = painterResource(Resources.Icon.Reset),
                contentDescription = null,
                tint = SurfaceError,
            )
        }
    }
}
