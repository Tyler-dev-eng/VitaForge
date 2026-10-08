package com.tylerdev.vitaforge.shared.components.dialog

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tylerdev.vitaforge.shared.Alpha
import com.tylerdev.vitaforge.shared.FontSize
import com.tylerdev.vitaforge.shared.IconPrimary
import com.tylerdev.vitaforge.shared.Resources
import com.tylerdev.vitaforge.shared.Surface
import com.tylerdev.vitaforge.shared.TextPrimary
import com.tylerdev.vitaforge.shared.TextSecondary
import com.tylerdev.vitaforge.shared.domain.ProductCategory
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import vitaforge.shared.generated.resources.Res
import vitaforge.shared.generated.resources.category_picker_cancel
import vitaforge.shared.generated.resources.category_picker_confirm
import vitaforge.shared.generated.resources.category_picker_title

@Composable
fun CategoryDialog(
    category: ProductCategory,
    onDismiss: () -> Unit,
    onConfirmClicked: (ProductCategory) -> Unit,
) {
    var selectedCategory by remember(category) { mutableStateOf(category) }
    AlertDialog(
        containerColor = Surface,
        title = {
            Text(
                text = stringResource(Res.string.category_picker_title),
                fontSize = FontSize.EXTRA_MEDIUM,
                color = TextPrimary,
            )
        },
        text = {
            Column(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(300.dp),
            ) {
                ProductCategory.entries.forEach { category ->
                    val animatedBackground by animateColorAsState(
                        targetValue =
                            if (category ==
                                selectedCategory
                            ) {
                                category.colour.copy(alpha = Alpha.TWENTY_PERCENT)
                            } else {
                                Color.Transparent
                            },
                    )
                    Row(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(6.dp))
                                .background(animatedBackground)
                                .clickable { selectedCategory = category }
                                .padding(horizontal = 12.dp, vertical = 16.dp),
                    ) {
                        Text(
                            modifier = Modifier.weight(1f),
                            text = category.title,
                            color = TextPrimary,
                            fontSize = FontSize.REGULAR,
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        AnimatedVisibility(
                            visible = category == selectedCategory,
                        ) {
                            Icon(
                                modifier = Modifier.size(14.dp),
                                painter = painterResource(Resources.Icon.Check),
                                contentDescription = null,
                                tint = IconPrimary,
                            )
                        }
                    }
                }
            }
        },
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = { onConfirmClicked(selectedCategory) },
                colors =
                    ButtonDefaults.textButtonColors(
                        containerColor = Color.Transparent,
                        contentColor = TextSecondary,
                    ),
            ) {
                Text(
                    text = stringResource(Res.string.category_picker_confirm),
                    fontSize = FontSize.REGULAR,
                    fontWeight = FontWeight.Medium,
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                colors =
                    ButtonDefaults.textButtonColors(
                        containerColor = Color.Transparent,
                        contentColor = TextPrimary.copy(alpha = Alpha.HALF),
                    ),
            ) {
                Text(
                    text = stringResource(Res.string.category_picker_cancel),
                    fontSize = FontSize.REGULAR,
                    fontWeight = FontWeight.Medium,
                )
            }
        },
    )
}
