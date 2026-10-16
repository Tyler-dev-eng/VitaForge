package com.vitaforge.products_overview.component

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.tylerdev.vitaforge.shared.SurfaceDarker
import com.tylerdev.vitaforge.shared.SurfaceSecondary
import org.jetbrains.compose.resources.stringResource
import vitaforge.shared.generated.resources.Res
import vitaforge.shared.generated.resources.home_carousel_indicator_description

private val DotHeight = 6.dp
private val DotWidth = 6.dp
private val SelectedDotWidth = 24.dp

@Composable
fun CarouselIndicator(
    modifier: Modifier = Modifier,
    count: Int,
    selectedIndex: Int?,
) {
    val description =
        stringResource(
            Res.string.home_carousel_indicator_description,
            (selectedIndex ?: 0) + 1,
            count,
        )
    Row(
        modifier = modifier.fillMaxWidth().semantics { contentDescription = description },
        horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterHorizontally),
    ) {
        repeat(count) { index ->
            val isSelected = index == selectedIndex
            val dotWidth by animateDpAsState(if (isSelected) SelectedDotWidth else DotWidth)
            Row(
                Modifier
                    .height(DotHeight)
                    .width(dotWidth)
                    .clip(CircleShape)
                    .background(if (isSelected) SurfaceSecondary else SurfaceDarker),
            ) {}
        }
    }
}
