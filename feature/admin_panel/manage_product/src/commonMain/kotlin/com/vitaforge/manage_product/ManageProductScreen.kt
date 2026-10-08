package com.vitaforge.manage_product

import ContentWithMessageBar
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.tylerdev.vitaforge.shared.BorderIdle
import com.tylerdev.vitaforge.shared.FontSize
import com.tylerdev.vitaforge.shared.IconPrimary
import com.tylerdev.vitaforge.shared.Resources
import com.tylerdev.vitaforge.shared.Surface
import com.tylerdev.vitaforge.shared.SurfaceLighter
import com.tylerdev.vitaforge.shared.TextPrimary
import com.tylerdev.vitaforge.shared.bebasNeueFontFamily
import com.tylerdev.vitaforge.shared.components.AlertTextField
import com.tylerdev.vitaforge.shared.components.CustomTextField
import com.tylerdev.vitaforge.shared.components.PrimaryButton
import com.tylerdev.vitaforge.shared.components.dialog.CategoryDialog
import com.tylerdev.vitaforge.shared.domain.ProductCategory
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import rememberMessageBarState
import vitaforge.shared.generated.resources.Res
import vitaforge.shared.generated.resources.manage_product_back
import vitaforge.shared.generated.resources.manage_product_title_add
import vitaforge.shared.generated.resources.manage_product_title_edit

@Composable
fun ManageProductScreen(
    id: String?,
    navigateBack: () -> Unit,
) {
    val messageBarState = rememberMessageBarState()
    val viewModel = koinViewModel<ManageProductViewModel>()
    val screenState = viewModel.screenState
    val isFormValid = viewModel.isFormValid
    var showCategoryDialog by remember { mutableStateOf(false) }

    AnimatedVisibility(
        visible = showCategoryDialog,
    ) {
        CategoryDialog(
            category = screenState.category,
            onDismiss = { showCategoryDialog = false },
            onConfirmClicked = { selectedCategory ->
                viewModel.updateCategory(selectedCategory)
                showCategoryDialog = false
            },
        )
    }

    Scaffold(
        containerColor = Surface,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text =
                            stringResource(
                                if (id == null) {
                                    Res.string.manage_product_title_add
                                } else {
                                    Res.string.manage_product_title_edit
                                },
                            ),
                        fontFamily = bebasNeueFontFamily(),
                        fontSize = FontSize.LARGE,
                        color = TextPrimary,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(
                            painter = painterResource(Resources.Icon.Back),
                            contentDescription = stringResource(Res.string.manage_product_back),
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
    ) { paddingValues ->
        ContentWithMessageBar(
            modifier = Modifier.padding(top = paddingValues.calculateTopPadding(), bottom = paddingValues.calculateBottomPadding()),
            contentBackgroundColor = Surface,
            messageBarState = messageBarState,
            errorMaxLines = 2,
        ) {
            Column(
                modifier =
                    Modifier
                        .padding(horizontal = 24.dp)
                        .padding(bottom = 24.dp, top = 12.dp)
                        .imePadding(),
            ) {
                Column(
                    modifier = Modifier.weight(1f).verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Box(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .height(300.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .border(width = 1.dp, color = BorderIdle, shape = RoundedCornerShape(12.dp))
                                .background(SurfaceLighter),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            modifier = Modifier.size(24.dp),
                            painter = painterResource(Resources.Icon.Plus),
                            contentDescription = "Add photo",
                            tint = IconPrimary,
                        )
                    }
                    CustomTextField(
                        value = screenState.title,
                        onValueChange = viewModel::updateTitle,
                        placeholder = "Title",
                    )
                    CustomTextField(
                        modifier = Modifier.height(170.dp),
                        value = screenState.description,
                        onValueChange = viewModel::updateDescription,
                        placeholder = "Description",
                        expanded = true,
                    )
                    AlertTextField(
                        modifier = Modifier.fillMaxWidth(),
                        text = screenState.category.title,
                        onClick = { showCategoryDialog = true },
                    )
                    CustomTextField(
                        value = screenState.weight?.toString() ?: "",
                        onValueChange = { viewModel.updateWeight(it.toIntOrNull() ?: 0) },
                        placeholder = "Weight (Optional)",
                        keyboardOptions =
                            KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                            ),
                    )
                    CustomTextField(
                        value = screenState.flavors,
                        onValueChange = viewModel::updateFlavors,
                        placeholder = "Flavours (Optional)",
                    )
                    CustomTextField(
                        value = screenState.price.toString(),
                        onValueChange = { value ->
                            if (value.isEmpty() || value.toDoubleOrNull() != null) {
                                viewModel.updatePrice(value.toDoubleOrNull() ?: 0.0)
                            }
                        },
                        placeholder = "Price",
                        keyboardOptions =
                            KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                            ),
                    )
                    Spacer(Modifier.height(24.dp))
                }
                PrimaryButton(
                    text =
                        if (id ==
                            null
                        ) {
                            "Add new product"
                        } else {
                            "Update product"
                        },
                    onClick = {
                        if (id != null) {
//                            viewModel.updateProduct(
//                                onSuccess = { messageBarState.addSuccess("Product successfully updated!") },
//                                onError = { message -> messageBarState.addError(message) }
//                            )
                        } else {
                            viewModel.createNewProduct(
                                onSuccess = { messageBarState.addSuccess("Product successfully added!") },
                                onError = { message -> messageBarState.addError(message) },
                            )
                        }
                    },
                    icon = if (id == null) Resources.Icon.Plus else Resources.Icon.Check,
                    enabled = isFormValid,
                )
            }
        }
    }
}
