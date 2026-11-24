package com.vitaforge.manage_product

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import com.tylerdev.vitaforge.shared.IconPrimary
import com.tylerdev.vitaforge.shared.Resources
import com.tylerdev.vitaforge.shared.components.CustomTextField
import com.tylerdev.vitaforge.shared.domain.ProductSpec
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import vitaforge.shared.generated.resources.Res
import vitaforge.shared.generated.resources.manage_product_section_specs
import vitaforge.shared.generated.resources.manage_product_specs_add
import vitaforge.shared.generated.resources.manage_product_specs_label_placeholder
import vitaforge.shared.generated.resources.manage_product_specs_remove
import vitaforge.shared.generated.resources.manage_product_specs_serving_placeholder
import vitaforge.shared.generated.resources.manage_product_specs_value_placeholder

private val SpecRowSpacing = 8.dp
private val SectionSpacing = 12.dp
private val RemoveIconSize = 20.dp

@Composable
internal fun SpecsEditor(
    servingText: String,
    specs: List<ProductSpec>,
    onServingChange: (String) -> Unit,
    onSpecChange: (Int, ProductSpec) -> Unit,
    onAddSpec: () -> Unit,
    onRemoveSpec: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(SectionSpacing)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            FormSectionLabel(stringResource(Res.string.manage_product_section_specs))
            TextButton(onClick = onAddSpec) { Text(stringResource(Res.string.manage_product_specs_add)) }
        }
        if (specs.isNotEmpty()) {
            CustomTextField(
                value = servingText,
                onValueChange = onServingChange,
                placeholder = stringResource(Res.string.manage_product_specs_serving_placeholder),
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
            )
        }
        specs.forEachIndexed { index, spec ->
            SpecRow(
                spec = spec,
                onSpecChange = { onSpecChange(index, it) },
                onRemove = { onRemoveSpec(index) },
            )
        }
    }
}

@Composable
private fun SpecRow(
    spec: ProductSpec,
    onSpecChange: (ProductSpec) -> Unit,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(SpecRowSpacing),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CustomTextField(
            modifier = Modifier.weight(1f),
            value = spec.label,
            onValueChange = { onSpecChange(spec.copy(label = it)) },
            placeholder = stringResource(Res.string.manage_product_specs_label_placeholder),
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
        )
        CustomTextField(
            modifier = Modifier.weight(1f),
            value = spec.value,
            onValueChange = { onSpecChange(spec.copy(value = it)) },
            placeholder = stringResource(Res.string.manage_product_specs_value_placeholder),
        )
        IconButton(onClick = onRemove) {
            Icon(
                modifier = Modifier.size(RemoveIconSize),
                painter = painterResource(Resources.Icon.TrashCan),
                contentDescription = stringResource(Res.string.manage_product_specs_remove),
                tint = IconPrimary,
            )
        }
    }
}
