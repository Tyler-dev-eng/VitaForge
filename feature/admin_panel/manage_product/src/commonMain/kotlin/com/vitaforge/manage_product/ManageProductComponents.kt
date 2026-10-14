package com.vitaforge.manage_product

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.tylerdev.vitaforge.shared.BorderIdle
import com.tylerdev.vitaforge.shared.FontSize
import com.tylerdev.vitaforge.shared.Orange
import com.tylerdev.vitaforge.shared.Surface
import com.tylerdev.vitaforge.shared.SurfaceDarker
import com.tylerdev.vitaforge.shared.TextMuted
import com.tylerdev.vitaforge.shared.TextPrimary
import com.tylerdev.vitaforge.shared.bebasNeueFontFamily

private val CardShape = RoundedCornerShape(12.dp)

@Composable
internal fun FormSectionLabel(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        modifier = modifier,
        text = text.uppercase(),
        fontFamily = bebasNeueFontFamily(),
        fontSize = FontSize.REGULAR,
        color = TextMuted,
    )
}

internal data class BadgeToggle(
    val title: String,
    val subtitle: String,
    val isChecked: Boolean,
    val onCheckedChange: (Boolean) -> Unit,
)

@Composable
internal fun BadgeToggleCard(
    toggles: List<BadgeToggle>,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .clip(CardShape)
                .border(width = 1.dp, color = BorderIdle, shape = CardShape),
    ) {
        toggles.forEachIndexed { index, toggle ->
            if (index > 0) HorizontalDivider(color = BorderIdle)
            BadgeToggleRow(toggle = toggle)
        }
    }
}

@Composable
private fun BadgeToggleRow(
    toggle: BadgeToggle,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = toggle.title, fontSize = FontSize.EXTRA_REGULAR, color = TextPrimary)
            Text(text = toggle.subtitle, fontSize = FontSize.SMALL, color = TextMuted)
        }
        Switch(
            checked = toggle.isChecked,
            onCheckedChange = toggle.onCheckedChange,
            colors =
                SwitchDefaults.colors(
                    checkedTrackColor = Orange,
                    uncheckedTrackColor = SurfaceDarker,
                    checkedThumbColor = Surface,
                    uncheckedThumbColor = Surface,
                    checkedBorderColor = Orange,
                    uncheckedBorderColor = SurfaceDarker,
                ),
        )
    }
}
