package com.tylerdev.vitaforge.shared.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.tylerdev.vitaforge.shared.FontSize
import com.tylerdev.vitaforge.shared.Resources
import com.tylerdev.vitaforge.shared.SurfaceDarker
import com.tylerdev.vitaforge.shared.TextMuted
import com.tylerdev.vitaforge.shared.TextPrimary
import com.tylerdev.vitaforge.shared.bebasNeueFontFamily
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

private val IconContainerSize = 40.dp

@Composable
fun ProfileActionRow(
    icon: DrawableResource,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth().clickable(onClick = onClick).padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Box(
            modifier = Modifier.size(IconContainerSize).clip(CircleShape).background(SurfaceDarker),
            contentAlignment = Alignment.Center,
        ) {
            Icon(painterResource(icon), contentDescription = null, modifier = Modifier.size(20.dp), tint = TextPrimary)
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title.uppercase(),
                fontFamily = bebasNeueFontFamily(),
                fontSize = FontSize.MEDIUM,
                color = TextPrimary,
            )
            Text(text = subtitle, fontSize = FontSize.SMALL, color = TextMuted)
        }
        Icon(
            painter = painterResource(Resources.Icon.ChevronRight),
            contentDescription = null,
            modifier = Modifier.size(18.dp),
            tint = TextMuted,
        )
    }
}
