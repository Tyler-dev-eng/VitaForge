package com.vitaforge.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.tylerdev.vitaforge.shared.Alpha
import com.tylerdev.vitaforge.shared.Error
import com.tylerdev.vitaforge.shared.FontSize
import com.tylerdev.vitaforge.shared.IconPrimary
import com.tylerdev.vitaforge.shared.Resources
import com.tylerdev.vitaforge.shared.SurfaceDarker
import com.tylerdev.vitaforge.shared.TextMuted
import com.tylerdev.vitaforge.shared.TextPrimary
import com.tylerdev.vitaforge.shared.bebasNeueFontFamily
import com.vitaforge.domain.DrawerItem
import org.jetbrains.compose.resources.painterResource

private val ItemCornerRadius = 12.dp
private val ItemIconContainerSize = 36.dp
private val ItemIconSize = 20.dp
private val ChevronSize = 12.dp

@Composable
fun DrawerItemCard(
    drawerItem: DrawerItem,
    onClick: () -> Unit,
) {
    val isDestructive = drawerItem == DrawerItem.SignOut
    val contentColor = if (isDestructive) Error else TextPrimary
    val iconBackground = if (isDestructive) Error.copy(alpha = Alpha.TEN_PERCENT) else SurfaceDarker

    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(ItemCornerRadius))
                .clickable(onClick = onClick)
                .padding(vertical = 12.dp, horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier =
                Modifier
                    .size(ItemIconContainerSize)
                    .background(iconBackground, CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = painterResource(drawerItem.icon),
                contentDescription = null,
                modifier = Modifier.size(ItemIconSize),
                tint = if (isDestructive) Error else IconPrimary,
            )
        }
        Spacer(modifier = Modifier.width(14.dp))
        Text(
            text = drawerItem.title.uppercase(),
            modifier = Modifier.weight(1f),
            color = contentColor,
            fontFamily = bebasNeueFontFamily(),
            fontSize = FontSize.MEDIUM,
        )
        if (!isDestructive) {
            Icon(
                painter = painterResource(Resources.Icon.ChevronRight),
                contentDescription = null,
                modifier = Modifier.size(ChevronSize),
                tint = TextMuted,
            )
        }
    }
}
