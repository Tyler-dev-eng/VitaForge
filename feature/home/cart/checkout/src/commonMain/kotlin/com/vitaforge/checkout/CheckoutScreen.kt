package com.vitaforge.checkout

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tylerdev.vitaforge.shared.FontSize
import com.tylerdev.vitaforge.shared.IconPrimary
import com.tylerdev.vitaforge.shared.Resources
import com.tylerdev.vitaforge.shared.Surface
import com.tylerdev.vitaforge.shared.TextPrimary
import com.tylerdev.vitaforge.shared.bebasNeueFontFamily
import org.jetbrains.compose.resources.painterResource

@Composable
fun CheckoutScreen(
    totalAmount: Double,
    navigateBack: () -> Unit,
) {
    Scaffold(
        containerColor = Surface,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Checkout",
                        fontFamily = bebasNeueFontFamily(),
                        fontSize = FontSize.LARGE,
                        color = TextPrimary,
                    )
                },
                actions = {
                    Text(
                        text = "$$totalAmount",
                        fontSize = FontSize.EXTRA_MEDIUM,
                        fontWeight = FontWeight.Medium,
                        color = TextPrimary,
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(
                            painter = painterResource(Resources.Icon.Back),
                            contentDescription = "Back arrow icon",
                            tint = IconPrimary,
                        )
                    }
                },
                colors =
                    TopAppBarDefaults.topAppBarColors(
                        containerColor = Surface,
                        scrolledContainerColor = Surface,
                        navigationIconContentColor = IconPrimary,
                        titleContentColor = TextPrimary,
                        actionIconContentColor = IconPrimary,
                    ),
            )
        },
    ) {}
}
