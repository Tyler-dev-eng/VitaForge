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
import com.tylerdev.vitaforge.shared.util.formatAmount
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import vitaforge.shared.generated.resources.Res
import vitaforge.shared.generated.resources.cart_price
import vitaforge.shared.generated.resources.checkout_back
import vitaforge.shared.generated.resources.checkout_title

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
                        text = stringResource(Res.string.checkout_title),
                        fontFamily = bebasNeueFontFamily(),
                        fontSize = FontSize.LARGE,
                        color = TextPrimary,
                    )
                },
                actions = {
                    Text(
                        text = stringResource(Res.string.cart_price, formatAmount(totalAmount)),
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
                            contentDescription = stringResource(Res.string.checkout_back),
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
