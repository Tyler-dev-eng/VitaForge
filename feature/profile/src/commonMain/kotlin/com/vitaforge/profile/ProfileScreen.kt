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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tylerdev.vitaforge.shared.SurfaceLighter
import com.tylerdev.vitaforge.shared.components.ProfileAccountOperations
import com.tylerdev.vitaforge.shared.components.ProfileActions
import com.tylerdev.vitaforge.shared.components.ProfileDetailsHeader
import com.tylerdev.vitaforge.shared.components.ProfileFooter
import com.tylerdev.vitaforge.shared.components.ProfileForm
import com.tylerdev.vitaforge.shared.components.ProfileIdentityCard
import com.tylerdev.vitaforge.shared.domain.Country

private const val SAMPLE_COMPLETION_PERCENT = 94
private const val SAMPLE_POINTS = 420
private const val SAMPLE_STATUS = "PRO"

@Composable
fun ProfileScreen(modifier: Modifier = Modifier) {
    var country by remember { mutableStateOf(Country.UnitedKingdom) }

    Column(
        modifier =
            modifier
                .fillMaxSize()
                .background(SurfaceLighter)
                .verticalScroll(rememberScrollState())
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .padding(horizontal = 24.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        ProfileIdentityCard(
            name = "John Doe",
            completionPercent = SAMPLE_COMPLETION_PERCENT,
            points = SAMPLE_POINTS,
            status = SAMPLE_STATUS,
            onChangePhotoClick = {},
        )
        ProfileDetailsHeader()
        ProfileForm(
            firstName = "John",
            onFirstNameChange = {},
            lastName = "Doe",
            onLastNameChange = {},
            email = "william.henry.harrison@example-pet-store.com",
            city = "London",
            onCityChange = {},
            postalCode = 12345,
            onPostalCodeChange = {},
            address = "123 London Road",
            onAddressChange = {},
            phoneNumber = "1234567890",
            onPhoneNumberChange = {},
            country = country,
            onCountrySelect = { country = it },
        )
        ProfileAccountOperations(
            onOrdersClick = {},
            onPaymentsClick = {},
            onPreferencesClick = {},
        )
        ProfileActions(onUpdateClick = {}, onSignOutClick = {})
        ProfileFooter()
    }
}
