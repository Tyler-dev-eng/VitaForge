package com.vitaforge.manage_product

import ContentWithMessageBar
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.tylerdev.vitaforge.shared.BorderError
import com.tylerdev.vitaforge.shared.BorderIdle
import com.tylerdev.vitaforge.shared.ButtonPrimary
import com.tylerdev.vitaforge.shared.FontSize
import com.tylerdev.vitaforge.shared.IconPrimary
import com.tylerdev.vitaforge.shared.Resources
import com.tylerdev.vitaforge.shared.Surface
import com.tylerdev.vitaforge.shared.SurfaceLighter
import com.tylerdev.vitaforge.shared.TextPrimary
import com.tylerdev.vitaforge.shared.bebasNeueFontFamily
import com.tylerdev.vitaforge.shared.components.AlertTextField
import com.tylerdev.vitaforge.shared.components.CustomTextField
import com.tylerdev.vitaforge.shared.components.ErrorCard
import com.tylerdev.vitaforge.shared.components.LoadingCard
import com.tylerdev.vitaforge.shared.components.PrimaryButton
import com.tylerdev.vitaforge.shared.components.dialog.CategoryDialog
import com.tylerdev.vitaforge.shared.domain.ProductCategory
import com.tylerdev.vitaforge.shared.util.DisplayResult
import com.tylerdev.vitaforge.shared.util.RequestState
import com.vitaforge.manage_product.util.PhotoPicker
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import rememberMessageBarState
import vitaforge.shared.generated.resources.Res
import vitaforge.shared.generated.resources.manage_product_add_bullet
import vitaforge.shared.generated.resources.manage_product_back
import vitaforge.shared.generated.resources.manage_product_delete_confirm
import vitaforge.shared.generated.resources.manage_product_delete_message
import vitaforge.shared.generated.resources.manage_product_delete_title
import vitaforge.shared.generated.resources.manage_product_discard_confirm
import vitaforge.shared.generated.resources.manage_product_discard_message
import vitaforge.shared.generated.resources.manage_product_discard_title
import vitaforge.shared.generated.resources.manage_product_flavour_add_cancel
import vitaforge.shared.generated.resources.manage_product_keep_editing
import vitaforge.shared.generated.resources.manage_product_badge_discounted_subtitle
import vitaforge.shared.generated.resources.manage_product_badge_discounted_title
import vitaforge.shared.generated.resources.manage_product_badge_new_subtitle
import vitaforge.shared.generated.resources.manage_product_badge_new_title
import vitaforge.shared.generated.resources.manage_product_badge_popular_subtitle
import vitaforge.shared.generated.resources.manage_product_badge_popular_title
import vitaforge.shared.generated.resources.manage_product_section_badges
import vitaforge.shared.generated.resources.manage_product_section_category
import vitaforge.shared.generated.resources.manage_product_section_description
import vitaforge.shared.generated.resources.manage_product_section_flavours
import vitaforge.shared.generated.resources.manage_product_section_media
import vitaforge.shared.generated.resources.manage_product_section_old_price
import vitaforge.shared.generated.resources.manage_product_section_price
import vitaforge.shared.generated.resources.manage_product_section_title
import vitaforge.shared.generated.resources.manage_product_section_weight
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
    val focusManager = LocalFocusManager.current
    val thumbnailUploaderState = viewModel.thumbnailUploaderState
    var showCategoryDialog by remember { mutableStateOf(false) }
    var dropDownMenuOpened by remember { mutableStateOf(false) }
    var showAddFlavourDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showDiscardDialog by remember { mutableStateOf(false) }
    val photoPicker = koinInject<PhotoPicker>()
    var priceText by remember { mutableStateOf("") }
    var oldPriceText by remember { mutableStateOf("") }

    LaunchedEffect(screenState.price) {
        if (priceText.toDoubleOrNull() != screenState.price) {
            priceText = screenState.price.toPriceText()
        }
    }

    LaunchedEffect(screenState.oldPrice) {
        if (oldPriceText.toDoubleOrNull() != screenState.oldPrice) {
            oldPriceText = screenState.oldPrice?.toPriceText().orEmpty()
        }
    }

    val requestBack = {
        if (viewModel.hasUnsavedChanges) showDiscardDialog = true else navigateBack()
    }

    photoPicker.InitialisePhotoPicker(
        onImageSelect = { file ->
            viewModel.uploadThumbnailToStorage(
                file = file,
                onSuccess = { messageBarState.addSuccess("Image successfully uploaded!") },
            )
        },
    )
    if (showAddFlavourDialog) {
        AddFlavourDialog(
            onDismiss = { showAddFlavourDialog = false },
            onConfirm = { name ->
                showAddFlavourDialog = false
                viewModel.addFlavour(name = name, onError = { message -> messageBarState.addError(message) })
            },
        )
    }
    if (showDeleteDialog) {
        ConfirmDialog(
            title = stringResource(Res.string.manage_product_delete_title),
            message = stringResource(Res.string.manage_product_delete_message),
            confirmText = stringResource(Res.string.manage_product_delete_confirm),
            dismissText = stringResource(Res.string.manage_product_flavour_add_cancel),
            onDismiss = { showDeleteDialog = false },
            onConfirm = {
                showDeleteDialog = false
                viewModel.deleteProduct(
                    onSuccess = navigateBack,
                    onError = { message -> messageBarState.addError(message) },
                )
            },
        )
    }
    if (showDiscardDialog) {
        ConfirmDialog(
            title = stringResource(Res.string.manage_product_discard_title),
            message = stringResource(Res.string.manage_product_discard_message),
            confirmText = stringResource(Res.string.manage_product_discard_confirm),
            dismissText = stringResource(Res.string.manage_product_keep_editing),
            onDismiss = { showDiscardDialog = false },
            onConfirm = {
                showDiscardDialog = false
                navigateBack()
            },
        )
    }
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
                    IconButton(onClick = requestBack) {
                        Icon(
                            painter = painterResource(Resources.Icon.Back),
                            contentDescription = stringResource(Res.string.manage_product_back),
                            tint = IconPrimary,
                        )
                    }
                },
                actions = {
                    if (id != null) {
                        Box {
                            IconButton(onClick = { dropDownMenuOpened = true }) {
                                Icon(
                                    painter = painterResource(Resources.Icon.VerticalMenu),
                                    contentDescription = "Vertical menu Icon",
                                    tint = IconPrimary,
                                )
                            }
                        }
                        DropdownMenu(
                            containerColor = Surface,
                            expanded = dropDownMenuOpened,
                            onDismissRequest = { dropDownMenuOpened = false },
                        ) {
                            DropdownMenuItem(
                                leadingIcon = {
                                    Icon(
                                        modifier = Modifier.size(14.dp),
                                        painter = painterResource(Resources.Icon.TrashCan),
                                        contentDescription = "Delete Icon",
                                        tint = IconPrimary,
                                    )
                                },
                                onClick = {
                                    dropDownMenuOpened = false
                                    showDeleteDialog = true
                                },
                                text = { Text(text = "Delete", color = TextPrimary) },
                            )
                        }
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
                    modifier =
                        Modifier
                            .weight(1f)
                            .pointerInput(Unit) { detectTapGestures { focusManager.clearFocus() } }
                            .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    FormSectionLabel(stringResource(Res.string.manage_product_section_media))
                    Box(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .height(300.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(SurfaceLighter)
                                .dashedBorder(color = if (viewModel.isThumbnailInvalid) BorderError else BorderIdle)
                                .clickable(enabled = thumbnailUploaderState.isIdle()) {
                                    photoPicker.open()
                                },
                        contentAlignment = Alignment.Center,
                    ) {
                        thumbnailUploaderState.DisplayResult(
                            onIdle = { ImageUploadPlaceholder() },
                            onLoading = { LoadingCard() },
                            onSuccess = {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.TopEnd,
                                ) {
                                    AsyncImage(
                                        modifier = Modifier.fillMaxSize(),
                                        model =
                                            ImageRequest
                                                .Builder(LocalPlatformContext.current)
                                                .data(screenState.thumbnail)
                                                .crossfade(true)
                                                .build(),
                                        contentDescription = "Product thumbnail",
                                        contentScale = ContentScale.Fit,
                                        alignment = Alignment.Center,
                                    )

                                    Box(
                                        modifier =
                                            Modifier
                                                .clip(RoundedCornerShape(6.dp))
                                                .padding(top = 12.dp, end = 12.dp)
                                                .background(ButtonPrimary)
                                                .clickable {
                                                    viewModel.deleteThumbnailFromStorage(
                                                        onSuccess = {
                                                            messageBarState.addSuccess("Image successfully deleted!")
                                                        },
                                                        onError = { message ->
                                                            messageBarState.addError(message)
                                                        },
                                                    )
                                                }.padding(12.dp),
                                        contentAlignment = Alignment.Center,
                                    ) {
                                        Icon(
                                            modifier = Modifier.size(14.dp),
                                            painter = painterResource(Resources.Icon.TrashCan),
                                            contentDescription = "Delete photo",
                                        )
                                    }
                                }
                            },
                            onError = { message ->
                                ErrorCard(
                                    message = message,
                                    onRetryClick = {
                                        viewModel.updateThumbnailUploaderState(RequestState.Idle)
                                        photoPicker.open()
                                    },
                                )
                            },
                        )
                    }
                    FormSectionLabel(stringResource(Res.string.manage_product_section_title))
                    CustomTextField(
                        value = screenState.title,
                        onValueChange = viewModel::updateTitle,
                        placeholder = "e.g., Pure Iso-Whey Hydrolyzed",
                        error = viewModel.isTitleInvalid,
                        keyboardOptions =
                            KeyboardOptions(
                                capitalization = KeyboardCapitalization.Words,
                                imeAction = ImeAction.Next,
                            ),
                    )
                    FormSectionLabel(stringResource(Res.string.manage_product_section_category))
                    AlertTextField(
                        modifier = Modifier.fillMaxWidth(),
                        text = screenState.category.title,
                        onClick = { showCategoryDialog = true },
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        FormSectionLabel(stringResource(Res.string.manage_product_section_description))
                        TextButton(onClick = viewModel::addDescriptionBullet) {
                            Text(stringResource(Res.string.manage_product_add_bullet))
                        }
                    }
                    CustomTextField(
                        modifier = Modifier.height(170.dp),
                        value = screenState.description,
                        onValueChange = viewModel::updateDescription,
                        placeholder = "Enter detailed formula specs, benefits, and usage instructions...",
                        expanded = true,
                        error = viewModel.isDescriptionInvalid,
                        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        AnimatedVisibility(
                            modifier = Modifier.weight(1f),
                            visible = screenState.category != ProductCategory.Accessories,
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                FormSectionLabel(stringResource(Res.string.manage_product_section_weight))
                                CustomTextField(
                                    value = screenState.weight?.toString().orEmpty(),
                                    onValueChange = { viewModel.updateWeight(parseWeightInput(it)) },
                                    placeholder = "Weight",
                                    keyboardOptions =
                                        KeyboardOptions(
                                            keyboardType = KeyboardType.Number,
                                            imeAction = ImeAction.Next,
                                        ),
                                )
                            }
                        }
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                        ) {
                            FormSectionLabel(stringResource(Res.string.manage_product_section_price))
                            CustomTextField(
                                value = priceText,
                                onValueChange = { value ->
                                    if (isValidPriceInput(value)) {
                                        priceText = value
                                        viewModel.updatePrice(value.toDoubleOrNull() ?: 0.0)
                                    }
                                },
                                placeholder = "Price",
                                error = viewModel.isPriceInvalid,
                                keyboardOptions =
                                    KeyboardOptions(
                                        keyboardType = KeyboardType.Decimal,
                                        imeAction = ImeAction.Done,
                                    ),
                            )
                        }
                    }
                    AnimatedVisibility(visible = screenState.isDiscounted) {
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            FormSectionLabel(stringResource(Res.string.manage_product_section_old_price))
                            CustomTextField(
                                value = oldPriceText,
                                onValueChange = { value ->
                                    if (isValidPriceInput(value)) {
                                        oldPriceText = value
                                        viewModel.updateOldPrice(value.toDoubleOrNull())
                                    }
                                },
                                placeholder = stringResource(Res.string.manage_product_section_old_price),
                                error = viewModel.isOldPriceInvalid,
                                keyboardOptions =
                                    KeyboardOptions(
                                        keyboardType = KeyboardType.Decimal,
                                        imeAction = ImeAction.Done,
                                    ),
                            )
                        }
                    }
                    SpecsEditor(
                        servingText = screenState.specsServing,
                        specs = screenState.specs,
                        onServingChange = viewModel::updateSpecsServing,
                        onSpecChange = viewModel::updateSpec,
                        onAddSpec = viewModel::addSpec,
                        onRemoveSpec = viewModel::removeSpec,
                    )
                    AnimatedVisibility(
                        visible = screenState.category != ProductCategory.Accessories,
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            FormSectionLabel(stringResource(Res.string.manage_product_section_flavours))
                            FlavourChips(
                                flavours = viewModel.availableFlavours,
                                selectedFlavours = screenState.selectedFlavours,
                                onToggleFlavour = viewModel::toggleFlavour,
                                onAddClick = { showAddFlavourDialog = true },
                            )
                        }
                    }
                    FormSectionLabel(stringResource(Res.string.manage_product_section_badges))
                    BadgeToggleCard(
                        toggles =
                            listOf(
                                BadgeToggle(
                                    title = stringResource(Res.string.manage_product_badge_new_title),
                                    subtitle = stringResource(Res.string.manage_product_badge_new_subtitle),
                                    isChecked = screenState.isNew,
                                    onCheckedChange = viewModel::updateNew,
                                ),
                                BadgeToggle(
                                    title = stringResource(Res.string.manage_product_badge_popular_title),
                                    subtitle = stringResource(Res.string.manage_product_badge_popular_subtitle),
                                    isChecked = screenState.isPopular,
                                    onCheckedChange = viewModel::updatePopular,
                                ),
                                BadgeToggle(
                                    title = stringResource(Res.string.manage_product_badge_discounted_title),
                                    subtitle = stringResource(Res.string.manage_product_badge_discounted_subtitle),
                                    isChecked = screenState.isDiscounted,
                                    onCheckedChange = viewModel::updateDiscounted,
                                ),
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
                        focusManager.clearFocus()
                        if (id != null) {
                            viewModel.updateProduct(
                                onSuccess = { messageBarState.addSuccess("Product successfully updated!") },
                                onError = { message -> messageBarState.addError(message) },
                            )
                        } else {
                            viewModel.createNewProduct(
                                onSuccess = { messageBarState.addSuccess("Product successfully added!") },
                                onError = { message -> messageBarState.addError(message) },
                            )
                        }
                    },
                    icon = if (id == null) Resources.Icon.Plus else Resources.Icon.Check,
                    isLoading = viewModel.isSaving,
                )
            }
        }
    }
}

private fun Double.toPriceText(): String = if (this == 0.0) "" else toString().removeSuffix(".0")
