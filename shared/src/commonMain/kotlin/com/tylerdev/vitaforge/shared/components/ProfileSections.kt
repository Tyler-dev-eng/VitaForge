package com.tylerdev.vitaforge.shared.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.tylerdev.vitaforge.shared.FontSize
import com.tylerdev.vitaforge.shared.IconPrimary
import com.tylerdev.vitaforge.shared.Resources
import com.tylerdev.vitaforge.shared.Surface
import com.tylerdev.vitaforge.shared.SurfaceDarker
import com.tylerdev.vitaforge.shared.TextMuted
import com.tylerdev.vitaforge.shared.TextPrimary
import com.tylerdev.vitaforge.shared.TextSecondary
import com.tylerdev.vitaforge.shared.bebasNeueFontFamily
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import vitaforge.shared.generated.resources.Res
import vitaforge.shared.generated.resources.profile_action_orders
import vitaforge.shared.generated.resources.profile_action_orders_subtitle
import vitaforge.shared.generated.resources.profile_action_payments
import vitaforge.shared.generated.resources.profile_action_payments_subtitle
import vitaforge.shared.generated.resources.profile_action_preferences
import vitaforge.shared.generated.resources.profile_action_preferences_subtitle
import vitaforge.shared.generated.resources.profile_back
import vitaforge.shared.generated.resources.profile_footer
import vitaforge.shared.generated.resources.profile_section_account
import vitaforge.shared.generated.resources.profile_section_details
import vitaforge.shared.generated.resources.profile_section_details_hint
import vitaforge.shared.generated.resources.profile_sign_out
import vitaforge.shared.generated.resources.profile_title
import vitaforge.shared.generated.resources.profile_update
import vitaforge.shared.generated.resources.profile_update_success

private val CardShape = RoundedCornerShape(12.dp)
private val CardElevation = 2.dp

@Composable
fun ProfileTopBar(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TopAppBar(
        modifier = modifier,
        title = {
            Text(
                text = stringResource(Res.string.profile_title).uppercase(),
                fontFamily = bebasNeueFontFamily(),
                fontSize = FontSize.LARGE,
                color = TextPrimary,
            )
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    painter = painterResource(Resources.Icon.Back),
                    contentDescription = stringResource(Res.string.profile_back),
                    tint = IconPrimary,
                )
            }
        },
        colors =
            TopAppBarDefaults.topAppBarColors(
                containerColor = Surface,
                scrolledContainerColor = Surface,
                navigationIconContentColor = Surface,
                titleContentColor = TextPrimary,
                actionIconContentColor = IconPrimary,
            ),
    )
}

@Composable
fun ProfileDetailsHeader(modifier: Modifier = Modifier) {
    SectionHeader(
        modifier = modifier,
        title = stringResource(Res.string.profile_section_details),
        hint = stringResource(Res.string.profile_section_details_hint),
    )
}

@Composable
fun ProfileAccountOperations(
    onOrdersClick: () -> Unit,
    onPaymentsClick: () -> Unit,
    onPreferencesClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(12.dp)) {
        SectionHeader(title = stringResource(Res.string.profile_section_account))
        Column(modifier = Modifier.shadow(CardElevation, CardShape).clip(CardShape).background(Surface)) {
            ProfileActionRow(
                icon = Resources.Icon.OrderHistory,
                title = stringResource(Res.string.profile_action_orders),
                subtitle = stringResource(Res.string.profile_action_orders_subtitle),
                onClick = onOrdersClick,
            )
            HorizontalDivider(color = SurfaceDarker)
            ProfileActionRow(
                icon = Resources.Icon.PaymentMethods,
                title = stringResource(Res.string.profile_action_payments),
                subtitle = stringResource(Res.string.profile_action_payments_subtitle),
                onClick = onPaymentsClick,
            )
            HorizontalDivider(color = SurfaceDarker)
            ProfileActionRow(
                icon = Resources.Icon.Settings,
                title = stringResource(Res.string.profile_action_preferences),
                subtitle = stringResource(Res.string.profile_action_preferences_subtitle),
                onClick = onPreferencesClick,
            )
        }
    }
}

@Composable
fun profileUpdateSuccessMessage(): String = stringResource(Res.string.profile_update_success)

@Composable
fun ProfileActions(
    onUpdateClick: () -> Unit,
    onSignOutClick: () -> Unit,
    modifier: Modifier = Modifier,
    isUpdateEnabled: Boolean = true,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        PrimaryButton(
            text = stringResource(Res.string.profile_update),
            icon = Resources.Icon.Check,
            enabled = isUpdateEnabled,
            onClick = onUpdateClick,
        )
        TextButton(onClick = onSignOutClick) {
            Icon(
                painter = painterResource(Resources.Icon.SignOut),
                contentDescription = null,
                modifier = Modifier.size(18.dp),
                tint = TextSecondary,
            )
            Text(
                modifier = Modifier.padding(start = 6.dp),
                text = stringResource(Res.string.profile_sign_out).uppercase(),
                fontSize = FontSize.REGULAR,
                color = TextSecondary,
            )
        }
    }
}

@Composable
fun ProfileFooter(modifier: Modifier = Modifier) {
    Text(
        modifier = modifier.fillMaxWidth(),
        text = stringResource(Res.string.profile_footer).uppercase(),
        fontFamily = bebasNeueFontFamily(),
        fontSize = FontSize.REGULAR,
        color = TextMuted,
        textAlign = TextAlign.Center,
    )
}

@Composable
private fun SectionHeader(
    title: String,
    modifier: Modifier = Modifier,
    hint: String? = null,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = title.uppercase(),
            fontFamily = bebasNeueFontFamily(),
            fontSize = FontSize.EXTRA_MEDIUM,
            color = TextPrimary,
        )
        if (hint != null) {
            Text(text = hint.uppercase(), fontSize = FontSize.EXTRA_SMALL, color = TextMuted)
        }
    }
}
