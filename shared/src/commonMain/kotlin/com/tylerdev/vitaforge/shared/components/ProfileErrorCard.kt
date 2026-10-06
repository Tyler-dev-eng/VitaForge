package com.tylerdev.vitaforge.shared.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.tylerdev.vitaforge.shared.FontSize
import com.tylerdev.vitaforge.shared.Resources
import com.tylerdev.vitaforge.shared.Surface
import com.tylerdev.vitaforge.shared.SurfaceDarker
import com.tylerdev.vitaforge.shared.SurfaceError
import com.tylerdev.vitaforge.shared.SurfaceErrorContainer
import com.tylerdev.vitaforge.shared.TextMuted
import com.tylerdev.vitaforge.shared.TextOnErrorContainer
import com.tylerdev.vitaforge.shared.TextPrimary
import com.tylerdev.vitaforge.shared.bebasNeueFontFamily
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import vitaforge.shared.generated.resources.Res
import vitaforge.shared.generated.resources.profile_error_export
import vitaforge.shared.generated.resources.profile_error_export_subtitle
import vitaforge.shared.generated.resources.profile_error_illustration
import vitaforge.shared.generated.resources.profile_error_message
import vitaforge.shared.generated.resources.profile_error_retry
import vitaforge.shared.generated.resources.profile_error_status
import vitaforge.shared.generated.resources.profile_error_support
import vitaforge.shared.generated.resources.profile_error_support_subtitle
import vitaforge.shared.generated.resources.profile_error_title

private val CardShape = RoundedCornerShape(12.dp)
private val CardElevation = 2.dp
private val StatusDotSize = 6.dp
private val IllustrationSize = 144.dp

@Composable
fun ProfileErrorCard(
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .shadow(CardElevation, CardShape)
                .clip(CardShape)
                .background(Surface)
                .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        ProfileErrorStatusPill()
        Image(
            painter = painterResource(Resources.Image.ProfileError),
            contentDescription = stringResource(Res.string.profile_error_illustration),
            modifier = Modifier.size(IllustrationSize),
        )
        Text(
            text = stringResource(Res.string.profile_error_title).uppercase(),
            fontFamily = bebasNeueFontFamily(),
            fontSize = FontSize.LARGE,
            color = TextPrimary,
            textAlign = TextAlign.Center,
        )
        Text(
            text = stringResource(Res.string.profile_error_message),
            fontSize = FontSize.REGULAR,
            color = TextMuted,
            textAlign = TextAlign.Center,
        )
        PrimaryButton(
            text = stringResource(Res.string.profile_error_retry).uppercase(),
            icon = Resources.Icon.Reset,
            onClick = onRetryClick,
        )
    }
}

@Composable
private fun ProfileErrorStatusPill(modifier: Modifier = Modifier) {
    Row(
        modifier =
            modifier
                .clip(CircleShape)
                .background(SurfaceErrorContainer)
                .padding(horizontal = 12.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Box(Modifier.size(StatusDotSize).clip(CircleShape).background(SurfaceError))
        Text(
            text = stringResource(Res.string.profile_error_status).uppercase(),
            fontSize = FontSize.EXTRA_SMALL,
            color = TextOnErrorContainer,
        )
    }
}

@Composable
fun ProfileErrorSupport(
    onContactSupportClick: () -> Unit,
    onExportCrashClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.shadow(CardElevation, CardShape).clip(CardShape).background(Surface)) {
        ProfileActionRow(
            icon = Resources.Icon.ContactUs,
            title = stringResource(Res.string.profile_error_support),
            subtitle = stringResource(Res.string.profile_error_support_subtitle),
            onClick = onContactSupportClick,
        )
        HorizontalDivider(color = SurfaceDarker)
        ProfileActionRow(
            icon = Resources.Icon.BugReport,
            title = stringResource(Res.string.profile_error_export),
            subtitle = stringResource(Res.string.profile_error_export_subtitle),
            onClick = onExportCrashClick,
        )
    }
}
