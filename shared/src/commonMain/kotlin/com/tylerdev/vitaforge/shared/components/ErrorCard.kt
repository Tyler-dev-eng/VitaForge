package com.tylerdev.vitaforge.shared.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.tylerdev.vitaforge.shared.FontSize
import com.tylerdev.vitaforge.shared.Resources
import com.tylerdev.vitaforge.shared.TextOnErrorContainer
import org.jetbrains.compose.resources.stringResource
import vitaforge.shared.generated.resources.Res
import vitaforge.shared.generated.resources.error_card_retry

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
