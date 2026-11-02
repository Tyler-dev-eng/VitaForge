package com.vitaforge.checkout

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tylerdev.vitaforge.shared.Alpha
import com.tylerdev.vitaforge.shared.BorderIdle
import com.tylerdev.vitaforge.shared.FontSize
import com.tylerdev.vitaforge.shared.Resources
import com.tylerdev.vitaforge.shared.TextMuted
import com.tylerdev.vitaforge.shared.TextPrimary
import com.tylerdev.vitaforge.shared.bebasNeueFontFamily
import com.tylerdev.vitaforge.shared.components.PrimaryButton
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import vitaforge.shared.generated.resources.Res
import vitaforge.shared.generated.resources.checkout_or
import vitaforge.shared.generated.resources.checkout_pay_on_delivery
import vitaforge.shared.generated.resources.checkout_pay_with_paypal
import vitaforge.shared.generated.resources.checkout_payment_method

private val SectionSpacing = 16.dp
private val DividerSpacing = 12.dp
private val ButtonCornerRadius = 6.dp
private val ButtonPadding = 20.dp
private val ButtonIconSize = 14.dp
private val IconSpacing = 12.dp

@Composable
internal fun PaymentOptions(
    isEnabled: Boolean,
    onPayWithPayPal: () -> Unit,
    onPayOnDelivery: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(SectionSpacing),
    ) {
        Text(
            text = stringResource(Res.string.checkout_payment_method),
            fontFamily = bebasNeueFontFamily(),
            fontSize = FontSize.MEDIUM,
            color = TextPrimary,
        )
        PrimaryButton(
            text = stringResource(Res.string.checkout_pay_with_paypal),
            icon = Resources.Image.PayPalLogo,
            enabled = isEnabled,
            onClick = onPayWithPayPal,
        )
        OrDivider()
        SecondaryPaymentButton(
            text = stringResource(Res.string.checkout_pay_on_delivery),
            enabled = isEnabled,
            onClick = onPayOnDelivery,
        )
    }
}

@Composable
private fun OrDivider(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(DividerSpacing),
    ) {
        HorizontalDivider(modifier = Modifier.weight(1f), color = BorderIdle)
        Text(
            text = stringResource(Res.string.checkout_or),
            fontSize = FontSize.SMALL,
            color = TextMuted,
        )
        HorizontalDivider(modifier = Modifier.weight(1f), color = BorderIdle)
    }
}

@Composable
private fun SecondaryPaymentButton(
    text: String,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedButton(
        modifier = modifier.fillMaxWidth(),
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(size = ButtonCornerRadius),
        border = BorderStroke(width = 1.dp, color = if (enabled) TextPrimary else BorderIdle),
        colors =
            ButtonDefaults.outlinedButtonColors(
                contentColor = TextPrimary,
                disabledContentColor = TextPrimary.copy(alpha = Alpha.DISABLED),
            ),
        contentPadding = PaddingValues(all = ButtonPadding),
    ) {
        Icon(
            painter = painterResource(Resources.Icon.Card),
            contentDescription = null,
            modifier = Modifier.size(ButtonIconSize),
        )
        Spacer(Modifier.width(IconSpacing))
        Text(
            text = text,
            fontSize = FontSize.REGULAR,
            fontWeight = FontWeight.Medium,
        )
    }
}
