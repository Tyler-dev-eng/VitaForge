package com.vitaforge.profile

import androidx.compose.runtime.Composable
import com.tylerdev.vitaforge.shared.components.ProfileForm

@Composable
fun ProfileScreen() {
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
    )
}
