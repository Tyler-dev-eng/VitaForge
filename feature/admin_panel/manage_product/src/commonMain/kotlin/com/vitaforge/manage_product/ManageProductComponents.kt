package com.vitaforge.manage_product

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.tylerdev.vitaforge.shared.Black
import com.tylerdev.vitaforge.shared.BorderIdle
import com.tylerdev.vitaforge.shared.FontSize
import com.tylerdev.vitaforge.shared.IconPrimary
import com.tylerdev.vitaforge.shared.IconWhite
import com.tylerdev.vitaforge.shared.Orange
import com.tylerdev.vitaforge.shared.Resources
import com.tylerdev.vitaforge.shared.Surface
import com.tylerdev.vitaforge.shared.SurfaceDarker
import com.tylerdev.vitaforge.shared.TextMuted
import com.tylerdev.vitaforge.shared.TextOnErrorContainer
import com.tylerdev.vitaforge.shared.TextPrimary
import com.tylerdev.vitaforge.shared.TextSecondary
import com.tylerdev.vitaforge.shared.TextWhite
import com.tylerdev.vitaforge.shared.bebasNeueFontFamily
import com.tylerdev.vitaforge.shared.components.CustomTextField
import com.tylerdev.vitaforge.shared.domain.Flavour
import com.tylerdev.vitaforge.shared.util.RequestState
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import vitaforge.shared.generated.resources.Res
import vitaforge.shared.generated.resources.manage_product_flavour_add
import vitaforge.shared.generated.resources.manage_product_flavour_add_cancel
import vitaforge.shared.generated.resources.manage_product_flavour_add_confirm
import vitaforge.shared.generated.resources.manage_product_flavour_add_placeholder
import vitaforge.shared.generated.resources.manage_product_flavour_add_title
import vitaforge.shared.generated.resources.manage_product_flavours_loading
import vitaforge.shared.generated.resources.manage_product_flavours_show_all
import vitaforge.shared.generated.resources.manage_product_flavours_show_less
import vitaforge.shared.generated.resources.manage_product_upload_description
import vitaforge.shared.generated.resources.manage_product_upload_hint
import vitaforge.shared.generated.resources.manage_product_upload_title

private val CardCornerRadius = 12.dp
private val CardShape = RoundedCornerShape(CardCornerRadius)
private val DashStrokeWidth = 2.dp
private val DashLength = 10.dp
private val UploadIconContainerSize = 56.dp
private val UploadIconSize = 24.dp
private val UploadBadgeSize = 20.dp
private val UploadBadgeIconSize = 12.dp
private val UploadContentSpacing = 12.dp
private val UploadHorizontalPadding = 24.dp
private val ChipHeight = 32.dp
private val ChipShape = RoundedCornerShape(ChipHeight / 2)
private val ChipMinTouchTarget = 48.dp
private val ChipHorizontalPadding = 14.dp
private val ChipSpacing = 8.dp
private val ChipContentSpacing = 6.dp
private val ChipIconSize = 12.dp
private val ChipDashStrokeWidth = 1.dp
private val ChipDashLength = 4.dp

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

internal fun Modifier.dashedBorder(
    color: Color,
    cornerRadius: Dp = CardCornerRadius,
    strokeWidth: Dp = DashStrokeWidth,
    dashLength: Dp = DashLength,
): Modifier =
    drawBehind {
        val dash = dashLength.toPx()
        drawRoundRect(
            color = color,
            cornerRadius = CornerRadius(cornerRadius.toPx()),
            style = Stroke(width = strokeWidth.toPx(), pathEffect = PathEffect.dashPathEffect(floatArrayOf(dash, dash))),
        )
    }

@Composable
internal fun ImageUploadPlaceholder(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(horizontal = UploadHorizontalPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(UploadContentSpacing),
    ) {
        Box {
            Box(
                modifier =
                    Modifier
                        .size(UploadIconContainerSize)
                        .clip(CircleShape)
                        .background(Surface)
                        .border(width = 1.dp, color = BorderIdle, shape = CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    modifier = Modifier.size(UploadIconSize),
                    painter = painterResource(Resources.Icon.Image),
                    contentDescription = stringResource(Res.string.manage_product_upload_description),
                    tint = IconPrimary,
                )
            }
            Box(
                modifier =
                    Modifier
                        .align(Alignment.BottomEnd)
                        .size(UploadBadgeSize)
                        .clip(CircleShape)
                        .background(Orange),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    modifier = Modifier.size(UploadBadgeIconSize),
                    painter = painterResource(Resources.Icon.Plus),
                    contentDescription = null,
                    tint = IconWhite,
                )
            }
        }
        Text(
            text = stringResource(Res.string.manage_product_upload_title).uppercase(),
            fontFamily = bebasNeueFontFamily(),
            fontSize = FontSize.EXTRA_REGULAR,
            color = TextPrimary,
            textAlign = TextAlign.Center,
        )
        Text(
            text = stringResource(Res.string.manage_product_upload_hint),
            fontSize = FontSize.SMALL,
            color = TextMuted,
            textAlign = TextAlign.Center,
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun FlavourChips(
    flavours: RequestState<List<Flavour>>,
    selectedFlavours: List<String>,
    onToggleFlavour: (String) -> Unit,
    onAddClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    when (flavours) {
        is RequestState.Success -> {
            val options =
                remember(flavours.data, selectedFlavours) {
                    val names = flavours.data.map { it.name }
                    names + selectedFlavours.filter { selected -> names.none { it.equals(selected, ignoreCase = true) } }
                }
            var isExpanded by rememberSaveable { mutableStateOf(false) }
            val visibleOptions =
                if (isExpanded) options else options.filter { name -> selectedFlavours.any { it.equals(name, ignoreCase = true) } }
            Column(modifier = modifier.fillMaxWidth()) {
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(ChipSpacing),
                ) {
                    visibleOptions.forEach { name ->
                        FlavourChip(
                            label = name,
                            isSelected = selectedFlavours.any { it.equals(name, ignoreCase = true) },
                            onClick = { onToggleFlavour(name) },
                        )
                    }
                    AddFlavourChip(onClick = onAddClick)
                }
                if (options.size > selectedFlavours.size || isExpanded) {
                    TextButton(onClick = { isExpanded = !isExpanded }) {
                        Text(
                            text =
                                if (isExpanded) {
                                    stringResource(Res.string.manage_product_flavours_show_less)
                                } else {
                                    stringResource(Res.string.manage_product_flavours_show_all, options.size)
                                },
                        )
                    }
                }
            }
        }

        is RequestState.Error ->
            Text(
                modifier = modifier,
                text = flavours.message,
                fontSize = FontSize.REGULAR,
                color = TextOnErrorContainer,
            )

        else ->
            Text(
                modifier = modifier,
                text = stringResource(Res.string.manage_product_flavours_loading),
                fontSize = FontSize.REGULAR,
                color = TextMuted,
            )
    }
}

@Composable
private fun FlavourChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val background by animateColorAsState(if (isSelected) Black else Surface)
    val contentColour by animateColorAsState(if (isSelected) TextWhite else TextPrimary)
    ChipTouchTarget(
        modifier = modifier,
        selected = isSelected,
        role = Role.Checkbox,
        onClick = onClick,
    ) {
        Row(
            modifier =
                Modifier
                    .height(ChipHeight)
                    .clip(ChipShape)
                    .background(background)
                    .border(width = 1.dp, color = if (isSelected) Black else BorderIdle, shape = ChipShape)
                    .padding(horizontal = ChipHorizontalPadding),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(ChipContentSpacing),
        ) {
            Text(
                text = label,
                fontSize = FontSize.REGULAR,
                color = contentColour,
            )
            AnimatedVisibility(visible = isSelected) {
                Icon(
                    modifier = Modifier.size(ChipIconSize),
                    painter = painterResource(Resources.Icon.Close),
                    contentDescription = null,
                    tint = IconWhite,
                )
            }
        }
    }
}

@Composable
private fun AddFlavourChip(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ChipTouchTarget(
        modifier = modifier,
        selected = false,
        role = Role.Button,
        onClick = onClick,
    ) {
        Row(
            modifier =
                Modifier
                    .height(ChipHeight)
                    .dashedBorder(
                        color = TextMuted,
                        cornerRadius = ChipHeight / 2,
                        strokeWidth = ChipDashStrokeWidth,
                        dashLength = ChipDashLength,
                    ).padding(horizontal = ChipHorizontalPadding),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(ChipContentSpacing),
        ) {
            Icon(
                modifier = Modifier.size(ChipIconSize),
                painter = painterResource(Resources.Icon.Plus),
                contentDescription = null,
                tint = IconPrimary,
            )
            Text(
                text = stringResource(Res.string.manage_product_flavour_add),
                fontSize = FontSize.REGULAR,
                color = TextPrimary,
            )
        }
    }
}

@Composable
private fun ChipTouchTarget(
    selected: Boolean,
    role: Role,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Box(
        modifier =
            modifier
                .heightIn(min = ChipMinTouchTarget)
                .selectable(selected = selected, role = role, onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        content()
    }
}

@Composable
internal fun AddFlavourDialog(
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit,
) {
    var name by remember { mutableStateOf("") }
    AlertDialog(
        containerColor = Surface,
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(Res.string.manage_product_flavour_add_title),
                fontSize = FontSize.EXTRA_MEDIUM,
                color = TextPrimary,
            )
        },
        text = {
            CustomTextField(
                value = name,
                onValueChange = { name = it },
                placeholder = stringResource(Res.string.manage_product_flavour_add_placeholder),
            )
        },
        confirmButton = {
            TextButton(
                enabled = name.isNotBlank(),
                onClick = { onConfirm(name) },
                colors = ButtonDefaults.textButtonColors(contentColor = TextSecondary),
            ) {
                Text(
                    text = stringResource(Res.string.manage_product_flavour_add_confirm),
                    fontSize = FontSize.REGULAR,
                    fontWeight = FontWeight.Medium,
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                colors = ButtonDefaults.textButtonColors(contentColor = TextPrimary),
            ) {
                Text(
                    text = stringResource(Res.string.manage_product_flavour_add_cancel),
                    fontSize = FontSize.REGULAR,
                    fontWeight = FontWeight.Medium,
                )
            }
        },
    )
}

@Composable
internal fun ConfirmDialog(
    title: String,
    message: String,
    confirmText: String,
    dismissText: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        containerColor = Surface,
        onDismissRequest = onDismiss,
        title = { Text(text = title, fontSize = FontSize.EXTRA_MEDIUM, color = TextPrimary) },
        text = { Text(text = message, fontSize = FontSize.REGULAR, color = TextPrimary) },
        confirmButton = {
            TextButton(
                onClick = onConfirm,
                colors = ButtonDefaults.textButtonColors(contentColor = TextSecondary),
            ) {
                Text(text = confirmText, fontSize = FontSize.REGULAR, fontWeight = FontWeight.Medium)
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                colors = ButtonDefaults.textButtonColors(contentColor = TextPrimary),
            ) {
                Text(text = dismissText, fontSize = FontSize.REGULAR, fontWeight = FontWeight.Medium)
            }
        },
    )
}
