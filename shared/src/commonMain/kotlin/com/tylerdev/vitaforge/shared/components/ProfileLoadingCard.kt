package com.tylerdev.vitaforge.shared.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.tylerdev.vitaforge.shared.Alpha
import com.tylerdev.vitaforge.shared.Surface
import com.tylerdev.vitaforge.shared.SurfaceDarker

private const val PULSE_DURATION_MS = 800
private const val FORM_PLACEHOLDER_COUNT = 4

private val CardShape = RoundedCornerShape(12.dp)
private val BlockShape = RoundedCornerShape(6.dp)
private val CardElevation = 2.dp
private val AvatarSize = 96.dp
private val NameWidth = 160.dp
private val NameHeight = 28.dp
private val BadgeWidth = 140.dp
private val BadgeHeight = 24.dp
private val SinceWidth = 200.dp
private val SinceHeight = 14.dp
private val StatsHeight = 64.dp
private val FieldHeight = 56.dp

@Composable
fun ProfileLoadingCard(modifier: Modifier = Modifier) {
    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .shadow(CardElevation, CardShape)
                .clip(CardShape)
                .background(Surface)
                .padding(horizontal = 16.dp, vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        ShimmerBlock(Modifier.size(AvatarSize), CircleShape)
        ShimmerBlock(Modifier.size(NameWidth, NameHeight))
        ShimmerBlock(Modifier.size(BadgeWidth, BadgeHeight), CircleShape)
        ShimmerBlock(Modifier.size(SinceWidth, SinceHeight))
        ShimmerBlock(Modifier.fillMaxWidth().height(StatsHeight))
    }
}

@Composable
fun ProfileFormLoading(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        repeat(FORM_PLACEHOLDER_COUNT) {
            ShimmerBlock(Modifier.fillMaxWidth().height(FieldHeight))
        }
    }
}

@Composable
private fun ShimmerBlock(
    modifier: Modifier = Modifier,
    shape: Shape = BlockShape,
) {
    val transition = rememberInfiniteTransition(label = "profile loading pulse")
    val pulseAlpha by transition.animateFloat(
        initialValue = Alpha.HALF,
        targetValue = Alpha.FULL,
        animationSpec = infiniteRepeatable(tween(PULSE_DURATION_MS), RepeatMode.Reverse),
        label = "profile loading alpha",
    )
    Box(
        modifier =
            modifier
                .graphicsLayer { alpha = pulseAlpha }
                .clip(shape)
                .background(SurfaceDarker),
    )
}
