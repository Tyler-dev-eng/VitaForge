package com.tylerdev.vitaforge.shared.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.tylerdev.vitaforge.shared.FontSize
import com.tylerdev.vitaforge.shared.Resources
import com.tylerdev.vitaforge.shared.TextMuted
import com.tylerdev.vitaforge.shared.TextPrimary
import com.tylerdev.vitaforge.shared.components.dialog.CountryPickerDialog
import com.tylerdev.vitaforge.shared.domain.Country
import org.jetbrains.compose.resources.stringResource
import vitaforge.shared.generated.resources.Res
import vitaforge.shared.generated.resources.profile_email_locked
import vitaforge.shared.generated.resources.profile_label_address
import vitaforge.shared.generated.resources.profile_label_city
import vitaforge.shared.generated.resources.profile_label_email
import vitaforge.shared.generated.resources.profile_label_first_name
import vitaforge.shared.generated.resources.profile_label_last_name
import vitaforge.shared.generated.resources.profile_label_phone
import vitaforge.shared.generated.resources.profile_label_postal_code
import vitaforge.shared.generated.resources.profile_placeholder_address
import vitaforge.shared.generated.resources.profile_placeholder_city
import vitaforge.shared.generated.resources.profile_placeholder_first_name
import vitaforge.shared.generated.resources.profile_placeholder_last_name
import vitaforge.shared.generated.resources.profile_placeholder_phone
import vitaforge.shared.generated.resources.profile_placeholder_postal_code

@Composable
fun ProfileForm(
    modifier: Modifier = Modifier,
    country: Country,
    onCountrySelect: (Country) -> Unit,
    firstName: String,
    onFirstNameChange: (String) -> Unit,
    lastName: String,
    onLastNameChange: (String) -> Unit,
    email: String,
    city: String?,
    onCityChange: (String) -> Unit,
    postalCode: Int?,
    onPostalCodeChange: (Int?) -> Unit,
    address: String?,
    onAddressChange: (String) -> Unit,
    phoneNumber: String?,
    onPhoneNumberChange: (String) -> Unit,
) {
    var showCountryDialog by remember { mutableStateOf(false) }

    AnimatedVisibility(
        visible = showCountryDialog,
    ) {
        CountryPickerDialog(
            country = country,
            onDismiss = { showCountryDialog = false },
            onConfirm = { selectedCountry ->
                showCountryDialog = false
                onCountrySelect(selectedCountry)
            },
        )
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            LabelledField(stringResource(Res.string.profile_label_first_name), Modifier.weight(1f)) {
                CustomTextField(
                    value = firstName,
                    placeholder = stringResource(Res.string.profile_placeholder_first_name),
                    onValueChange = onFirstNameChange,
                    error = firstName.length !in 3..50,
                )
            }
            LabelledField(stringResource(Res.string.profile_label_last_name), Modifier.weight(1f)) {
                CustomTextField(
                    value = lastName,
                    placeholder = stringResource(Res.string.profile_placeholder_last_name),
                    onValueChange = onLastNameChange,
                    error = lastName.length !in 3..50,
                )
            }
        }
        LabelledField(
            label = stringResource(Res.string.profile_label_email),
            hint = stringResource(Res.string.profile_email_locked),
        ) {
            CustomTextField(
                value = email,
                onValueChange = {},
                enabled = false,
                trailingIcon = Resources.Icon.EmailVerified,
            )
        }
        LabelledField(stringResource(Res.string.profile_label_phone)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                AlertTextField(
                    text = "+${country.dialCode}",
                    icon = country.flag,
                    onClick = { showCountryDialog = true },
                )
                Spacer(modifier = Modifier.width(12.dp))
                CustomTextField(
                    value = phoneNumber ?: "",
                    placeholder = stringResource(Res.string.profile_placeholder_phone),
                    onValueChange = onPhoneNumberChange,
                    error = phoneNumber.toString().length !in 5..30,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                )
            }
        }
        LabelledField(stringResource(Res.string.profile_label_address)) {
            CustomTextField(
                value = address ?: "",
                placeholder = stringResource(Res.string.profile_placeholder_address),
                onValueChange = onAddressChange,
                error = address?.length !in 3..50,
                trailingIcon = Resources.Icon.LocationPin,
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            LabelledField(stringResource(Res.string.profile_label_city), Modifier.weight(1f)) {
                CustomTextField(
                    value = city ?: "",
                    placeholder = stringResource(Res.string.profile_placeholder_city),
                    onValueChange = onCityChange,
                    error = city?.length !in 3..50,
                )
            }
            LabelledField(stringResource(Res.string.profile_label_postal_code), Modifier.weight(1f)) {
                CustomTextField(
                    value = "${postalCode ?: ""}",
                    placeholder = stringResource(Res.string.profile_placeholder_postal_code),
                    onValueChange = { onPostalCodeChange(it.toIntOrNull()) },
                    error = postalCode == null || postalCode.toString().length !in 3..8,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                )
            }
        }
    }
}

@Composable
private fun LabelledField(
    label: String,
    modifier: Modifier = Modifier,
    hint: String? = null,
    content: @Composable () -> Unit,
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(text = label.uppercase(), fontSize = FontSize.SMALL, color = TextPrimary)
            if (hint != null) {
                Text(text = hint, fontSize = FontSize.SMALL, color = TextMuted)
            }
        }
        content()
    }
}
