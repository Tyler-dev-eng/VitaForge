package com.tylerdev.vitaforge.shared.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tylerdev.vitaforge.shared.Alpha
import com.tylerdev.vitaforge.shared.ButtonDisabled
import com.tylerdev.vitaforge.shared.ButtonPrimary
import com.tylerdev.vitaforge.shared.FontSize
import com.tylerdev.vitaforge.shared.TextPrimary
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

private val LOADING_INDICATOR_SIZE = 14.dp
private val LOADING_INDICATOR_STROKE = 2.dp

@Composable
fun PrimaryButton(
    modifier: Modifier = Modifier,
    text: String,
    icon: DrawableResource? = null,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    onClick: () -> Unit,
) {
    Button(
        modifier = modifier.fillMaxWidth(),
        onClick = { if (!isLoading) onClick() },
        enabled = enabled,
        shape = RoundedCornerShape(size = 6.dp),
        colors =
            ButtonDefaults.buttonColors(
                containerColor = ButtonPrimary,
                contentColor = TextPrimary,
                disabledContainerColor = ButtonDisabled,
                disabledContentColor = TextPrimary.copy(alpha = Alpha.DISABLED),
            ),
        contentPadding = PaddingValues(all = 20.dp),
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(LOADING_INDICATOR_SIZE),
                color = TextPrimary,
                strokeWidth = LOADING_INDICATOR_STROKE,
            )
        } else if (icon != null) {
            Icon(
                painter = painterResource(icon),
                contentDescription = null,
                modifier = Modifier.size(14.dp),
                tint = Color.Unspecified,
            )
        }
        Spacer(Modifier.width(12.dp))
        Text(
            text = text,
            fontSize = FontSize.REGULAR,
            fontWeight = FontWeight.Medium,
        )
    }
}
