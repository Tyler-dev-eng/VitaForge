package com.vitaforge.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import com.tylerdev.vitaforge.shared.FontSize
import com.tylerdev.vitaforge.shared.IconPrimary
import com.tylerdev.vitaforge.shared.IconSecondary
import com.tylerdev.vitaforge.shared.SurfaceLighter
import com.tylerdev.vitaforge.shared.robotoCondensedFontFamily
import com.vitaforge.domain.BottomBarDestination
import org.jetbrains.compose.resources.painterResource

@Composable
fun BottomBar(
    modifier: Modifier = Modifier,
    selected: Boolean,
    onSelect: (BottomBarDestination) -> Unit,
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(color = SurfaceLighter)
                .padding(vertical = 24.dp, horizontal = 36.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        BottomBarDestination.entries.forEach { destination ->
            val animateTint by animateColorAsState(
                targetValue = if (selected) IconSecondary else IconPrimary,
            )
            Column(
                modifier = Modifier.clickable { onSelect(destination) },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Icon(
                    painter = painterResource(destination.icon),
                    tint = animateTint,
                    contentDescription = destination.title,
                )
                Text(
                    text = destination.title.uppercase(),
                    color = animateTint,
                    fontFamily = robotoCondensedFontFamily(),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = FontSize.EXTRA_SMALL,
                    letterSpacing = 0.05.em,
                )
            }
        }
    }
}
