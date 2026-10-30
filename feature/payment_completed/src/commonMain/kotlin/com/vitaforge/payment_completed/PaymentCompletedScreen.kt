package com.vitaforge.payment_completed

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.tylerdev.vitaforge.shared.FontSize
import com.tylerdev.vitaforge.shared.IconBrick
import com.tylerdev.vitaforge.shared.IconOlive
import com.tylerdev.vitaforge.shared.Resources
import com.tylerdev.vitaforge.shared.Surface
import com.tylerdev.vitaforge.shared.TextPrimary
import com.tylerdev.vitaforge.shared.bebasNeueFontFamily
import com.tylerdev.vitaforge.shared.components.PrimaryButton
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import vitaforge.shared.generated.resources.Res
import vitaforge.shared.generated.resources.payment_completed_continue
import vitaforge.shared.generated.resources.payment_completed_error_message
import vitaforge.shared.generated.resources.payment_completed_error_title
import vitaforge.shared.generated.resources.payment_completed_success_message
import vitaforge.shared.generated.resources.payment_completed_success_title
import vitaforge.shared.generated.resources.payment_completed_try_again

private val StatusIconSize = 96.dp

@Composable
fun PaymentCompletedScreen(
    modifier: Modifier = Modifier,
    isSuccess: Boolean,
    errorMessage: String? = null,
    onDone: () -> Unit,
) {
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .background(Surface)
                .systemBarsPadding()
                .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        ) {
            Icon(
                modifier = Modifier.size(StatusIconSize),
                painter = painterResource(if (isSuccess) Resources.Icon.Check else Resources.Icon.Close),
                contentDescription = null,
                tint = if (isSuccess) IconOlive else IconBrick,
            )
            Text(
                text =
                    stringResource(
                        if (isSuccess) {
                            Res.string.payment_completed_success_title
                        } else {
                            Res.string.payment_completed_error_title
                        },
                    ),
                fontFamily = bebasNeueFontFamily(),
                fontSize = FontSize.EXTRA_LARGE,
                color = TextPrimary,
                textAlign = TextAlign.Center,
            )
            Text(
                text =
                    if (isSuccess) {
                        stringResource(Res.string.payment_completed_success_message)
                    } else {
                        errorMessage ?: stringResource(Res.string.payment_completed_error_message)
                    },
                fontSize = FontSize.EXTRA_REGULAR,
                color = TextPrimary,
                textAlign = TextAlign.Center,
            )
        }
        PrimaryButton(
            text =
                stringResource(
                    if (isSuccess) Res.string.payment_completed_continue else Res.string.payment_completed_try_again,
                ),
            icon = Resources.Icon.ArrowRight,
            onClick = onDone,
        )
    }
}
