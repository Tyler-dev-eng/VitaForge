package com.tylerdev.vitaforge.shared.components

import androidx.compose.foundation.background
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.tylerdev.vitaforge.shared.FontSize
import com.tylerdev.vitaforge.shared.Resources
import com.tylerdev.vitaforge.shared.SurfaceErrorContainer
import com.tylerdev.vitaforge.shared.TextOnErrorContainer
import com.tylerdev.vitaforge.shared.bebasNeueFontFamily
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import vitaforge.shared.generated.resources.Res
import vitaforge.shared.generated.resources.error_card_icon_description
import vitaforge.shared.generated.resources.error_card_retry
import vitaforge.shared.generated.resources.error_card_title

private val IconContainerSize = 56.dp
private val IconSize = 24.dp

@Composable
fun ErrorCard(
    message: String,
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
    ) {
        Box(
            modifier = Modifier.size(IconContainerSize).clip(CircleShape).background(SurfaceErrorContainer),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                modifier = Modifier.size(IconSize),
                painter = painterResource(Resources.Icon.Image),
                contentDescription = stringResource(Res.string.error_card_icon_description),
                tint = TextOnErrorContainer,
            )
        }
        Text(
            text = stringResource(Res.string.error_card_title).uppercase(),
            fontFamily = bebasNeueFontFamily(),
            fontSize = FontSize.EXTRA_REGULAR,
            color = TextOnErrorContainer,
            textAlign = TextAlign.Center,
        )
        Text(
            text = message,
            fontSize = FontSize.REGULAR,
            color = TextOnErrorContainer,
            textAlign = TextAlign.Center,
        )
        PrimaryButton(
            text = stringResource(Res.string.error_card_retry).uppercase(),
            icon = Resources.Icon.Reset,
            onClick = onRetryClick,
        )
    }
}
