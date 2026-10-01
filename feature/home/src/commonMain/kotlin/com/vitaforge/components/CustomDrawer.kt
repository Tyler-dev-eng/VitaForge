package com.vitaforge.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.tylerdev.vitaforge.shared.Black
import com.tylerdev.vitaforge.shared.Error
import com.tylerdev.vitaforge.shared.FontSize
import com.tylerdev.vitaforge.shared.GrayDarker
import com.tylerdev.vitaforge.shared.IconPrimary
import com.tylerdev.vitaforge.shared.Resources
import com.tylerdev.vitaforge.shared.Surface
import com.tylerdev.vitaforge.shared.SurfaceBrand
import com.tylerdev.vitaforge.shared.SurfaceDarker
import com.tylerdev.vitaforge.shared.TextMuted
import com.tylerdev.vitaforge.shared.TextPrimary
import com.tylerdev.vitaforge.shared.TextSecondary
import com.tylerdev.vitaforge.shared.bebasNeueFontFamily
import com.vitaforge.domain.DrawerItem
import org.jetbrains.compose.resources.painterResource

private const val DRAWER_WIDTH_FRACTION = 0.72f
private val DrawerCardRadius = 16.dp
private val AvatarSize = 48.dp
private val AdminIconSize = 40.dp

@Composable
fun CustomDrawer(
    userName: String,
    userTier: String,
    onProfileClick: () -> Unit,
    onContactUsClick: () -> Unit,
    onSignOutClick: () -> Unit,
    onAdminClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxHeight()
                .fillMaxWidth(DRAWER_WIDTH_FRACTION)
                .background(Surface),
    ) {
        DrawerHeader(
            userName = userName,
            userTier = userTier,
        )
        Column(
            modifier = Modifier.weight(1f).padding(horizontal = 12.dp, vertical = 16.dp),
        ) {
            val navigationItems = DrawerItem.entries.filter { it != DrawerItem.Admin }
            navigationItems.forEachIndexed { index, item ->
                if (index > 0) HorizontalDivider(color = GrayDarker)
                DrawerItemCard(
                    drawerItem = item,
                    onClick = {
                        when (item) {
                            DrawerItem.Profile -> onProfileClick()
                            DrawerItem.ContactUs -> onContactUsClick()
                            DrawerItem.SignOut -> onSignOutClick()
                            else -> Unit
                        }
                    },
                )
            }
        }
        DrawerAdminRow(onClick = onAdminClick)
    }
}

@Composable
private fun DrawerHeader(
    userName: String,
    userTier: String,
) {
    Column(modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 50.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top,
        ) {
            Column {
                Text(
                    text =
                        buildAnnotatedString {
                            withStyle(SpanStyle(color = Error)) { append("VITA") }
                            withStyle(SpanStyle(color = TextPrimary)) { append("FORGE") }
                        },
                    fontFamily = bebasNeueFontFamily(),
                    fontSize = FontSize.LARGE,
                )
                Text(
                    text = "PRECISION PERFORMANCE NUTRITION",
                    color = TextMuted,
                    fontSize = FontSize.EXTRA_SMALL,
                )
            }
        }
        Spacer(Modifier.height(20.dp))
        DrawerProfileCard(userName = userName, userTier = userTier)
    }
}

@Composable
private fun DrawerProfileCard(
    userName: String,
    userTier: String,
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(DrawerCardRadius))
                .background(SurfaceDarker)
                .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier.size(AvatarSize).background(GrayDarker, CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = painterResource(Resources.Icon.User),
                contentDescription = null,
                tint = IconPrimary,
            )
        }
        Spacer(Modifier.width(12.dp))
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = userName, color = TextPrimary, fontSize = FontSize.REGULAR)
                Spacer(Modifier.width(6.dp))
                Icon(
                    painter = painterResource(Resources.Icon.Verified),
                    contentDescription = "Verified",
                    modifier = Modifier.size(16.dp),
                    tint = TextSecondary,
                )
            }
            Text(text = userTier, color = TextMuted, fontSize = FontSize.SMALL)
        }
    }
}

@Composable
private fun DrawerAdminRow(onClick: () -> Unit) {
    Column(
        modifier =
            Modifier
                .navigationBarsPadding()
                .padding(horizontal = 20.dp)
                .padding(bottom = 24.dp),
    ) {
        HorizontalDivider(color = GrayDarker)
        Spacer(Modifier.height(12.dp))
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(DrawerCardRadius))
                    .background(SurfaceDarker)
                    .clickable(onClick = onClick)
                    .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier.size(AdminIconSize).background(Black, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    painter = painterResource(DrawerItem.Admin.icon),
                    contentDescription = null,
                    tint = SurfaceBrand,
                )
            }
            Spacer(Modifier.width(12.dp))
            Text(
                text = "ADMIN PANEL",
                modifier = Modifier.weight(1f),
                color = TextPrimary,
                fontFamily = bebasNeueFontFamily(),
                fontSize = FontSize.MEDIUM,
            )
            Text(
                text = "ADMIN",
                modifier =
                    Modifier
                        .background(SurfaceBrand, RoundedCornerShape(6.dp))
                        .padding(horizontal = 10.dp, vertical = 4.dp),
                color = TextPrimary,
                fontFamily = bebasNeueFontFamily(),
                fontSize = FontSize.SMALL,
            )
        }
    }
}
