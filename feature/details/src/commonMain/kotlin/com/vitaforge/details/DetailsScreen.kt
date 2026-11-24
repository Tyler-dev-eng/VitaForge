package com.vitaforge.details

import ContentWithMessageBar
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.tylerdev.vitaforge.shared.BorderIdle
import com.tylerdev.vitaforge.shared.FontSize
import com.tylerdev.vitaforge.shared.IconPrimary
import com.tylerdev.vitaforge.shared.Resources
import com.tylerdev.vitaforge.shared.Surface
import com.tylerdev.vitaforge.shared.SurfaceLighter
import com.tylerdev.vitaforge.shared.TextPrimary
import com.tylerdev.vitaforge.shared.TextSecondary
import com.tylerdev.vitaforge.shared.bebasNeueFontFamily
import com.tylerdev.vitaforge.shared.components.PrimaryButton
import com.tylerdev.vitaforge.shared.domain.Product
import com.tylerdev.vitaforge.shared.domain.ProductCategory
import com.tylerdev.vitaforge.shared.robotoCondensedFontFamily
import com.tylerdev.vitaforge.shared.util.DisplayResult
import com.vitaforge.details.component.FlavourChip
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import rememberMessageBarState
import vitaforge.shared.generated.resources.Res
import vitaforge.shared.generated.resources.details_add_to_cart
import vitaforge.shared.generated.resources.details_added_to_cart
import vitaforge.shared.generated.resources.details_flavour_label
import vitaforge.shared.generated.resources.details_product_image
import vitaforge.shared.generated.resources.details_title
import vitaforge.shared.generated.resources.details_weight_icon
import vitaforge.shared.generated.resources.product_card_price
import vitaforge.shared.generated.resources.product_card_weight
import vitaforge.shared.generated.resources.profile_back

private val ContentPadding = 24.dp
private val SectionSpacing = 16.dp
private val ImageCornerRadius = 12.dp
private val PillCornerRadius = 8.dp
private val WeightIconSize = 14.dp
private val ItemSpacing = 8.dp
private const val IMAGE_ASPECT_RATIO = 1.2f
private const val TITLE_LINE_HEIGHT_MULTIPLIER = 1.1
private const val DESCRIPTION_LINE_HEIGHT_MULTIPLIER = 1.4

@Composable
fun DetailsScreen(navigateBack: () -> Unit) {
    val messageBarState = rememberMessageBarState()
    val viewModel = koinViewModel<DetailsViewModel>()
    val product by viewModel.product.collectAsStateWithLifecycle()
    val selectedFlavour by viewModel.selectedFlavour.collectAsStateWithLifecycle()
    val isAddingToCart by viewModel.isAddingToCart.collectAsStateWithLifecycle()
    val addedToCartMessage = stringResource(Res.string.details_added_to_cart)
    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                DetailsEvent.AddedToCart -> messageBarState.addSuccess(addedToCartMessage)
                is DetailsEvent.AddToCartFailed -> messageBarState.addError(event.message)
            }
        }
    }
    Scaffold(
        containerColor = Surface,
        topBar = { DetailsTopBar(navigateBack = navigateBack) },
    ) { paddingValues ->
        product.DisplayResult(
            onLoading = {},
            onSuccess = { selectedProduct ->
                ContentWithMessageBar(
                    messageBarState = messageBarState,
                    errorMaxLines = 2,
                    contentBackgroundColor = Surface,
                    modifier =
                        Modifier
                            .padding(
                                top = paddingValues.calculateTopPadding(),
                                bottom = paddingValues.calculateBottomPadding(),
                            ),
                ) {
                    Column {
                        ProductContent(
                            modifier = Modifier.weight(1f),
                            product = selectedProduct,
                        )
                        PurchaseBar(
                            flavours = selectedProduct.flavours.orEmpty(),
                            selectedFlavour = selectedFlavour,
                            isAddingToCart = isAddingToCart,
                            onAddToCartClick = viewModel::addItemToCart,
                            onFlavourClick = viewModel::selectFlavour,
                        )
                    }
                }
            },
            onError = {},
        )
    }
}

@Composable
private fun DetailsTopBar(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TopAppBar(
        modifier = modifier,
        title = {
            Text(
                text = stringResource(Res.string.details_title),
                fontFamily = bebasNeueFontFamily(),
                fontSize = FontSize.LARGE,
                color = TextPrimary,
            )
        },
        navigationIcon = {
            IconButton(onClick = navigateBack) {
                Icon(
                    painter = painterResource(Resources.Icon.Back),
                    contentDescription = stringResource(Res.string.profile_back),
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
            ),
    )
}

@Composable
private fun ProductContent(
    product: Product,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .verticalScroll(rememberScrollState())
                .padding(horizontal = ContentPadding)
                .padding(top = ItemSpacing, bottom = SectionSpacing),
        verticalArrangement = Arrangement.spacedBy(SectionSpacing),
    ) {
        ProductImage(thumbnail = product.thumbnail)
        ProductHeader(product = product)
        Text(
            text = product.description,
            fontSize = FontSize.REGULAR,
            lineHeight = FontSize.REGULAR * DESCRIPTION_LINE_HEIGHT_MULTIPLIER,
            color = TextSecondary,
        )
        SpecsTable(product = product)
    }
}

@Composable
private fun ProductImage(
    thumbnail: String,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(size = ImageCornerRadius)
    AsyncImage(
        modifier =
            modifier
                .fillMaxWidth()
                .aspectRatio(IMAGE_ASPECT_RATIO)
                .clip(shape)
                .border(width = 1.dp, color = BorderIdle, shape = shape),
        model =
            ImageRequest
                .Builder(LocalPlatformContext.current)
                .data(thumbnail)
                .crossfade(true)
                .build(),
        contentDescription = stringResource(Res.string.details_product_image),
        contentScale = ContentScale.Fit,
        alignment = Alignment.Center,
    )
}

@Composable
private fun ProductHeader(
    product: Product,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(ItemSpacing)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            val weight = product.weight
            if (weight == null || ProductCategory.valueOf(product.category) == ProductCategory.Accessories) {
                Spacer(modifier = Modifier.weight(1f))
            } else {
                WeightPill(weight = weight)
            }
            Text(
                text = stringResource(Res.string.product_card_price, product.price),
                fontSize = FontSize.EXTRA_MEDIUM,
                color = TextPrimary,
                fontWeight = FontWeight.Bold,
            )
        }
        Text(
            text = product.title.uppercase(),
            fontSize = FontSize.LARGE,
            lineHeight = FontSize.LARGE * TITLE_LINE_HEIGHT_MULTIPLIER,
            fontFamily = bebasNeueFontFamily(),
            color = TextPrimary,
        )
    }
}

@Composable
private fun WeightPill(
    weight: Int,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier =
            modifier
                .clip(RoundedCornerShape(size = PillCornerRadius))
                .background(SurfaceLighter)
                .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            modifier = Modifier.size(WeightIconSize),
            painter = painterResource(Resources.Icon.Weight),
            contentDescription = stringResource(Res.string.details_weight_icon),
            tint = IconPrimary,
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = stringResource(Res.string.product_card_weight, weight),
            fontSize = FontSize.REGULAR,
            color = TextPrimary,
        )
    }
}

@Composable
private fun PurchaseBar(
    flavours: List<String>,
    selectedFlavour: String?,
    isAddingToCart: Boolean,
    onAddToCartClick: () -> Unit,
    onFlavourClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(topStart = ImageCornerRadius, topEnd = ImageCornerRadius))
                .background(SurfaceLighter)
                .navigationBarsPadding()
                .padding(ContentPadding),
        verticalArrangement = Arrangement.spacedBy(SectionSpacing),
    ) {
        if (flavours.isNotEmpty()) {
            FlavourSelector(
                flavours = flavours,
                selectedFlavour = selectedFlavour,
                onFlavourClick = onFlavourClick,
            )
        }
        PrimaryButton(
            icon = Resources.Icon.Cart,
            text = stringResource(Res.string.details_add_to_cart),
            enabled = !isAddingToCart && (flavours.isEmpty() || selectedFlavour != null),
            onClick = onAddToCartClick,
        )
    }
}

@Composable
private fun FlavourSelector(
    flavours: List<String>,
    selectedFlavour: String?,
    onFlavourClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(ItemSpacing)) {
        Text(
            text = stringResource(Res.string.details_flavour_label).uppercase(),
            fontSize = FontSize.SMALL,
            fontWeight = FontWeight.Medium,
            fontFamily = robotoCondensedFontFamily(),
            color = TextSecondary,
        )
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(ItemSpacing),
            horizontalArrangement = Arrangement.spacedBy(ItemSpacing),
        ) {
            flavours.forEach { flavour ->
                FlavourChip(
                    flavour = flavour,
                    isSelected = flavour == selectedFlavour,
                    onClick = { onFlavourClick(flavour) },
                )
            }
        }
    }
}
