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
import com.tylerdev.vitaforge.shared.components.LoadingCard
import com.tylerdev.vitaforge.shared.components.PrimaryButton
import com.tylerdev.vitaforge.shared.util.DisplayResult
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import vitaforge.shared.generated.resources.Res
import vitaforge.shared.generated.resources.payment_completed_continue
import vitaforge.shared.generated.resources.payment_completed_error_title
import vitaforge.shared.generated.resources.payment_completed_success_message
import vitaforge.shared.generated.resources.payment_completed_success_title
import vitaforge.shared.generated.resources.payment_completed_try_again

private val StatusIconSize = 96.dp
private val ScreenPadding = 24.dp
private val ContentSpacing = 16.dp

@Composable
fun PaymentCompletedScreen(
    modifier: Modifier = Modifier,
    onDone: () -> Unit,
) {
    val viewModel = koinViewModel<PaymentViewModel>()
    viewModel.screenState.DisplayResult(
        modifier =
            modifier
                .fillMaxSize()
                .background(Surface)
                .systemBarsPadding(),
        onLoading = { LoadingCard() },
        onError = { message ->
            PaymentStatusContent(isSuccess = false, message = message, onDone = onDone)
        },
        onSuccess = {
            PaymentStatusContent(
                isSuccess = true,
                message = stringResource(Res.string.payment_completed_success_message),
                onDone = onDone,
            )
        },
    )
}

@Composable
private fun PaymentStatusContent(
    isSuccess: Boolean,
    message: String,
    onDone: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize().padding(ScreenPadding),
        verticalArrangement = Arrangement.spacedBy(ContentSpacing),
    ) {
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(ContentSpacing, Alignment.CenterVertically),
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
                text = message,
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
