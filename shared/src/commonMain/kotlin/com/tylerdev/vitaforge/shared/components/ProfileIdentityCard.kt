package com.tylerdev.vitaforge.shared.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.tylerdev.vitaforge.shared.Alpha
import com.tylerdev.vitaforge.shared.FontSize
import com.tylerdev.vitaforge.shared.IconWhite
import com.tylerdev.vitaforge.shared.Resources
import com.tylerdev.vitaforge.shared.Surface
import com.tylerdev.vitaforge.shared.SurfaceBrand
import com.tylerdev.vitaforge.shared.SurfaceDarker
import com.tylerdev.vitaforge.shared.SurfaceSecondary
import com.tylerdev.vitaforge.shared.TextMuted
import com.tylerdev.vitaforge.shared.TextOlive
import com.tylerdev.vitaforge.shared.TextOnBrand
import com.tylerdev.vitaforge.shared.TextPrimary
import com.tylerdev.vitaforge.shared.TextSecondary
import com.tylerdev.vitaforge.shared.bebasNeueFontFamily
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import vitaforge.shared.generated.resources.Res
import vitaforge.shared.generated.resources.profile_change_photo
import vitaforge.shared.generated.resources.profile_member_since
import vitaforge.shared.generated.resources.profile_member_tier
import vitaforge.shared.generated.resources.profile_stat_complete
import vitaforge.shared.generated.resources.profile_stat_points
import vitaforge.shared.generated.resources.profile_stat_status

private val CardShape = RoundedCornerShape(12.dp)
private val AvatarSize = 96.dp
private val AvatarRingWidth = 4.dp
private val CardElevation = 2.dp
private val CameraButtonSize = 32.dp
private val BrandBarHeight = 4.dp
private val StatDividerHeight = 24.dp

@Composable
fun ProfileIdentityCard(
    name: String,
    completionPercent: Int,
    points: Int,
    status: String,
    onChangePhotoClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .shadow(CardElevation, CardShape)
                .clip(CardShape)
                .background(Surface),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .height(BrandBarHeight)
                .background(Brush.horizontalGradient(listOf(SurfaceSecondary, SurfaceBrand, SurfaceSecondary))),
        )
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            ProfileAvatar(onChangePhotoClick = onChangePhotoClick)
            Text(
                text = name.uppercase(),
                fontFamily = bebasNeueFontFamily(),
                fontSize = FontSize.LARGE,
                color = TextPrimary,
                textAlign = TextAlign.Center,
            )
            TierBadge()
            Text(
                text = stringResource(Res.string.profile_member_since),
                fontSize = FontSize.SMALL,
                color = TextMuted,
            )
            ProfileStats(completionPercent, points, status)
        }
    }
}

@Composable
private fun ProfileAvatar(
    onChangePhotoClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        Box(
            modifier =
                Modifier
                    .size(
                        AvatarSize,
                    ).border(AvatarRingWidth, SurfaceDarker, CircleShape)
                    .padding(AvatarRingWidth)
                    .clip(CircleShape)
                    .background(SurfaceDarker),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = painterResource(Resources.Icon.User),
                contentDescription = null,
                modifier = Modifier.size(AvatarSize / 2),
                tint = TextMuted,
            )
        }
        IconButton(
            onClick = onChangePhotoClick,
            modifier =
                Modifier
                    .align(Alignment.BottomEnd)
                    .size(CameraButtonSize)
                    .background(SurfaceSecondary, CircleShape),
        ) {
            Icon(
                painter = painterResource(Resources.Icon.Camera),
                contentDescription = stringResource(Res.string.profile_change_photo),
                modifier = Modifier.size(16.dp),
                tint = IconWhite,
            )
        }
    }
}

@Composable
private fun TierBadge(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.clip(CircleShape).background(SurfaceBrand).padding(horizontal = 12.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Icon(
            painter = painterResource(Resources.Icon.ProfileBadge),
            contentDescription = null,
            modifier = Modifier.size(14.dp),
            tint = TextOnBrand,
        )
        Text(
            text = stringResource(Res.string.profile_member_tier).uppercase(),
            fontSize = FontSize.EXTRA_SMALL,
            color = TextOnBrand,
        )
    }
}

@Composable
private fun ProfileStats(
    completionPercent: Int,
    points: Int,
    status: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(SurfaceDarker)
                .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        ProfileStat("$completionPercent%", stringResource(Res.string.profile_stat_complete), TextSecondary)
        StatDivider()
        ProfileStat("$points", stringResource(Res.string.profile_stat_points), TextPrimary)
        StatDivider()
        ProfileStat(status, stringResource(Res.string.profile_stat_status), TextOlive)
    }
}

@Composable
private fun ProfileStat(
    value: String,
    label: String,
    valueColour: Color,
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            fontFamily = bebasNeueFontFamily(),
            fontSize = FontSize.EXTRA_MEDIUM,
            color = valueColour,
        )
        Text(text = label.uppercase(), fontSize = FontSize.EXTRA_SMALL, color = TextMuted)
    }
}

@Composable
private fun StatDivider() {
    Box(Modifier.width(1.dp).height(StatDividerHeight).background(TextMuted.copy(alpha = Alpha.DISABLED)))
}
