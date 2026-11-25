package com.vitaforge.manage_product

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tylerdev.vitaforge.shared.BorderIdle
import com.tylerdev.vitaforge.shared.FontSize
import com.tylerdev.vitaforge.shared.IconPrimary
import com.tylerdev.vitaforge.shared.Resources
import com.tylerdev.vitaforge.shared.Surface
import com.tylerdev.vitaforge.shared.SurfaceErrorContainer
import com.tylerdev.vitaforge.shared.TextOnErrorContainer
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import vitaforge.shared.generated.resources.Res
import vitaforge.shared.generated.resources.manage_product_menu_delete
import vitaforge.shared.generated.resources.manage_product_menu_description

private val MenuShape = RoundedCornerShape(12.dp)
private val MenuMinWidth = 200.dp
private val IconContainerSize = 32.dp
private val IconSize = 16.dp

/** The overflow button of the edit screen, with the menu anchored to it. */
@Composable
internal fun ProductOptionsMenu(
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var isExpanded by remember { mutableStateOf(false) }
    Box(modifier = modifier) {
        IconButton(onClick = { isExpanded = true }) {
            Icon(
                painter = painterResource(Resources.Icon.VerticalMenu),
                contentDescription = stringResource(Res.string.manage_product_menu_description),
                tint = IconPrimary,
            )
        }
        DropdownMenu(
            modifier = Modifier.widthIn(min = MenuMinWidth),
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false },
            shape = MenuShape,
            containerColor = Surface,
            border = BorderStroke(width = 1.dp, color = BorderIdle),
        ) {
            DeleteMenuItem(
                onClick = {
                    isExpanded = false
                    onDeleteClick()
                },
            )
        }
    }
}

@Composable
private fun DeleteMenuItem(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    DropdownMenuItem(
        modifier = modifier,
        leadingIcon = {
            Box(
                modifier = Modifier.size(IconContainerSize).clip(CircleShape).background(SurfaceErrorContainer),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    modifier = Modifier.size(IconSize),
                    painter = painterResource(Resources.Icon.TrashCan),
                    contentDescription = null,
                    tint = TextOnErrorContainer,
                )
            }
        },
        text = {
            Text(
                text = stringResource(Res.string.manage_product_menu_delete),
                fontSize = FontSize.EXTRA_REGULAR,
                fontWeight = FontWeight.Medium,
                color = TextOnErrorContainer,
            )
        },
        onClick = onClick,
    )
}
