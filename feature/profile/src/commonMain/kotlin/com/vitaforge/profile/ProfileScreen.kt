package com.vitaforge.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tylerdev.vitaforge.shared.SurfaceLighter
import com.tylerdev.vitaforge.shared.components.ProfileAccountOperations
import com.tylerdev.vitaforge.shared.components.ProfileActions
import com.tylerdev.vitaforge.shared.components.ProfileDetailsHeader
import com.tylerdev.vitaforge.shared.components.ProfileErrorCard
import com.tylerdev.vitaforge.shared.components.ProfileErrorSupport
import com.tylerdev.vitaforge.shared.components.ProfileFooter
import com.tylerdev.vitaforge.shared.components.ProfileForm
import com.tylerdev.vitaforge.shared.components.ProfileFormLoading
import com.tylerdev.vitaforge.shared.components.ProfileIdentityCard
import com.tylerdev.vitaforge.shared.components.ProfileLoadingCard
import com.tylerdev.vitaforge.shared.components.ProfileTopBar
import com.tylerdev.vitaforge.shared.util.DisplayResult
import org.koin.compose.viewmodel.koinViewModel

private const val SAMPLE_COMPLETION_PERCENT = 94
private const val SAMPLE_POINTS = 420
private const val SAMPLE_STATUS = "PRO"

@Composable
fun ProfileScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .background(SurfaceLighter)
                .windowInsetsPadding(WindowInsets.safeDrawing),
    ) {
        ProfileTopBar(onBackClick = onNavigateBack)
        ProfileContent()
    }
}

@Composable
private fun ProfileContent(modifier: Modifier = Modifier) {
    val viewModel = koinViewModel<ProfileViewModel>()
    val screenState = viewModel.screenState
    val isFormValid = viewModel.isFormValid

    Column(
        modifier =
            modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        if (viewModel.screenReady.isError()) {
            ProfileErrorCard(onRetryClick = viewModel::retry)
            ProfileErrorSupport(onContactSupportClick = {}, onExportCrashClick = {})
            return@Column
        }
        if (viewModel.screenReady.isLoading()) {
            ProfileLoadingCard()
        } else {
            ProfileIdentityCard(
                name = screenState.firstName + " " + screenState.lastName,
                completionPercent = SAMPLE_COMPLETION_PERCENT,
                points = SAMPLE_POINTS,
                status = SAMPLE_STATUS,
                onChangePhotoClick = {},
            )
        }
        ProfileDetailsHeader()
        viewModel.screenReady.DisplayResult(
            onLoading = { ProfileFormLoading() },
            onSuccess = {
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
            },
            onError = {},
        )
        ProfileAccountOperations(
            onOrdersClick = {},
            onPaymentsClick = {},
            onPreferencesClick = {},
        )
        ProfileActions(
            onUpdateClick = {},
            onSignOutClick = {},
            isUpdateEnabled = isFormValid,
        )
        ProfileFooter()
    }
}
