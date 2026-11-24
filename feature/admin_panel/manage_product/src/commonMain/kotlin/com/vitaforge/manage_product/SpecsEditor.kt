package com.vitaforge.manage_product

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
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
private val SpecKeyboardOptions =
    KeyboardOptions(
        capitalization = KeyboardCapitalization.Sentences,
        imeAction = ImeAction.Done,
    )

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
    val focusManager = LocalFocusManager.current
    val clearFocusOnDone = KeyboardActions(onDone = { focusManager.clearFocus() })
    val newRowRequester = remember { BringIntoViewRequester() }
    val newRowFocusRequester = remember { FocusRequester() }
    var previousCount by remember { mutableIntStateOf(specs.size) }
    LaunchedEffect(specs.size) {
        if (specs.size > previousCount) {
            newRowRequester.bringIntoView()
            newRowFocusRequester.requestFocus()
        }
        previousCount = specs.size
    }
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
                keyboardOptions = SpecKeyboardOptions,
            keyboardActions = clearFocusOnDone,
            )
        }
        specs.forEachIndexed { index, spec ->
            val isLast = index == specs.lastIndex
            SpecRow(
                modifier = if (isLast) Modifier.bringIntoViewRequester(newRowRequester) else Modifier,
                labelFocusRequester = if (isLast) newRowFocusRequester else null,
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
    labelFocusRequester: FocusRequester? = null,
) {
    val focusManager = LocalFocusManager.current
    val clearFocusOnDone = KeyboardActions(onDone = { focusManager.clearFocus() })
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(SpecRowSpacing),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CustomTextField(
            modifier = Modifier.weight(1f).then(if (labelFocusRequester != null) Modifier.focusRequester(labelFocusRequester) else Modifier),
            value = spec.label,
            onValueChange = { onSpecChange(spec.copy(label = it)) },
            placeholder = stringResource(Res.string.manage_product_specs_label_placeholder),
            keyboardOptions = SpecKeyboardOptions,
            keyboardActions = clearFocusOnDone,
        )
        CustomTextField(
            modifier = Modifier.weight(1f),
            value = spec.value,
            onValueChange = { onSpecChange(spec.copy(value = it)) },
            placeholder = stringResource(Res.string.manage_product_specs_value_placeholder),
            keyboardOptions = SpecKeyboardOptions,
            keyboardActions = clearFocusOnDone,
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
