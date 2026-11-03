package com.vitaforge.checkout

import ContentWithMessageBar
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.tylerdev.vitaforge.shared.components.ProfileForm
import com.tylerdev.vitaforge.shared.util.formatAmount
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import rememberMessageBarState
import vitaforge.shared.generated.resources.Res
import vitaforge.shared.generated.resources.cart_price
import vitaforge.shared.generated.resources.checkout_back
import vitaforge.shared.generated.resources.checkout_title

@Composable
fun CheckoutScreen(
    totalAmount: Double,
    navigateBack: () -> Unit,
    navigateToPaymentCompleted: (token: String?) -> Unit,
) {
    val messageBarState = rememberMessageBarState()
    val viewModel = koinViewModel<CheckoutViewModel>()
    val screenState = viewModel.screenState
    val isFormValid = viewModel.isFormValid

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
    ) { padding ->
        ContentWithMessageBar(
            modifier =
                Modifier
                    .padding(
                        top = padding.calculateTopPadding(),
                        bottom = padding.calculateBottomPadding(),
                    ),
            contentBackgroundColor = Surface,
            messageBarState = messageBarState,
            errorMaxLines = 2,
        ) {
            Column(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(top = 12.dp, bottom = 24.dp)
                        .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp),
            ) {
                ProfileForm(
                    firstName = screenState.firstName,
                    onFirstNameChange = viewModel::updateFirstName,
                    lastName = screenState.lastName,
                    onLastNameChange = viewModel::updateLastName,
                    email = screenState.email,
                    city = screenState.city,
                    onCityChange = viewModel::updateCity,
                    postalCode = screenState.postalCode,
                    onPostalCodeChange = viewModel::updatePostalCode,
                    address = screenState.address,
                    onAddressChange = viewModel::updateAddress,
                    phoneNumber = screenState.phoneNumber?.number,
                    onPhoneNumberChange = viewModel::updatePhoneNumber,
                    country = screenState.country,
                    onCountrySelect = viewModel::updateCountry,
                )
                PaymentOptions(
                    isEnabled = isFormValid,
                    onPayWithPayPal = {
                        viewModel.payWithPayPal(
                            onSuccess = { token -> navigateToPaymentCompleted(token) },
                            onError = { message -> messageBarState.addError(message) },
                        )
                    },
                    onPayOnDelivery = {
                        viewModel.payOnDelivery(
                            onSuccess = { navigateToPaymentCompleted(null) },
                            onError = { message -> messageBarState.addError(message) },
                        )
                    },
                )
            }
        }
    }
}
