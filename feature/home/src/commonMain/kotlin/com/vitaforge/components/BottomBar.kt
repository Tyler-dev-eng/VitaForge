package com.vitaforge.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import com.tylerdev.vitaforge.shared.FontSize
import com.tylerdev.vitaforge.shared.IconPrimary
import com.tylerdev.vitaforge.shared.IconSecondary
import com.tylerdev.vitaforge.shared.SurfaceBrand
import com.tylerdev.vitaforge.shared.SurfaceLighter
import com.tylerdev.vitaforge.shared.TextOnBrand
import com.tylerdev.vitaforge.shared.robotoCondensedFontFamily
import com.vitaforge.domain.BottomBarDestination
import org.jetbrains.compose.resources.painterResource

private val BadgeOffset = 8.dp
private val BadgeMinSize = 16.dp
private val BadgePadding = 3.dp
private const val MAX_BADGE_COUNT = 99

@Composable
fun BottomBar(
    modifier: Modifier = Modifier,
    selected: BottomBarDestination,
    cartItemCount: Int,
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
                targetValue = if (selected == destination) IconSecondary else IconPrimary,
            )
            Column(
                modifier =
                    Modifier.clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                    ) { onSelect(destination) },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Box {
                    Icon(
                        painter = painterResource(destination.icon),
                        tint = animateTint,
                        contentDescription = destination.title,
                    )
                    if (destination == BottomBarDestination.Cart && cartItemCount > 0) {
                        CartBadge(
                            modifier = Modifier.align(Alignment.TopEnd).offset(x = BadgeOffset, y = -BadgeOffset),
                            count = cartItemCount,
                        )
                    }
                }
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

@Composable
private fun CartBadge(
    count: Int,
    modifier: Modifier = Modifier,
) {
    val label = if (count > MAX_BADGE_COUNT) "$MAX_BADGE_COUNT+" else count.toString()
    Box(
        modifier =
            modifier
                .defaultMinSize(minWidth = BadgeMinSize, minHeight = BadgeMinSize)
                .clip(CircleShape)
                .background(SurfaceBrand)
                .padding(horizontal = BadgePadding),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = label,
            color = TextOnBrand,
            fontWeight = FontWeight.Bold,
            fontSize = FontSize.EXTRA_SMALL,
            textAlign = TextAlign.Center,
        )
    }
}
