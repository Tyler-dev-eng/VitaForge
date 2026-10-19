package com.vitaforge.details

import ContentWithMessageBar
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
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
import com.tylerdev.vitaforge.shared.components.QuantityCounter
import com.tylerdev.vitaforge.shared.domain.ProductCategory
import com.tylerdev.vitaforge.shared.domain.QuantityCounterSize
import com.tylerdev.vitaforge.shared.robotoCondensedFontFamily
import com.tylerdev.vitaforge.shared.util.DisplayResult
import com.vitaforge.details.component.FlavourChip
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import rememberMessageBarState
import vitaforge.shared.generated.resources.Res
import vitaforge.shared.generated.resources.profile_back

@Composable
fun DetailsScreen(navigateBack: () -> Unit) {
    val messageBarState = rememberMessageBarState()
    val viewModel = koinViewModel<DetailsViewModel>()
    val product by viewModel.product.collectAsStateWithLifecycle()
    val quantity = viewModel.quantity
    Scaffold(
        containerColor = Surface,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Details",
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
                actions = {
                    QuantityCounter(
                        size = QuantityCounterSize.Large,
                        value = quantity.toString(),
                        onMinusClick = { if (quantity > 1) viewModel.updateQuantity(quantity - 1) },
                        onPlusClick = { if (quantity < 10) viewModel.updateQuantity(quantity + 1) },
                    )
                    Spacer(Modifier.width(16.dp))
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
                        Column(
                            modifier =
                                Modifier
                                    .weight(1f)
                                    .verticalScroll(rememberScrollState())
                                    .padding(horizontal = 24.dp)
                                    .padding(top = 12.dp),
                        ) {
                            AsyncImage(
                                modifier =
                                    Modifier
                                        .fillMaxWidth()
                                        .height(300.dp)
                                        .clip(RoundedCornerShape(size = 12.dp))
                                        .border(
                                            width = 1.dp,
                                            color = BorderIdle,
                                            shape = RoundedCornerShape(size = 12.dp),
                                        ),
                                model =
                                    ImageRequest
                                        .Builder(LocalPlatformContext.current)
                                        .data(selectedProduct.thumbnail)
                                        .crossfade(true)
                                        .build(),
                                contentDescription = "Product Image",
                                contentScale = ContentScale.Crop,
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                            ) {
                                AnimatedContent(
                                    targetState = selectedProduct.category,
                                ) { category ->
                                    if (ProductCategory.valueOf(category) == ProductCategory.Accessories) {
                                        Spacer(modifier = Modifier.weight(1f))
                                    } else {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                        ) {
                                            Icon(
                                                modifier = Modifier.size(14.dp),
                                                painter = painterResource(Resources.Icon.Weight),
                                                contentDescription = "Weight icon",
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(
                                                text = "${selectedProduct.weight}g",
                                                fontSize = FontSize.REGULAR,
                                                color = TextPrimary,
                                            )
                                        }
                                    }
                                }
                                Text(
                                    text = "R${selectedProduct.price}",
                                    fontSize = FontSize.MEDIUM,
                                    color = TextSecondary,
                                    fontWeight = FontWeight.Medium,
                                )
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = selectedProduct.title.uppercase(),
                                fontSize = FontSize.EXTRA_MEDIUM,
                                fontWeight = FontWeight.Medium,
                                fontFamily = robotoCondensedFontFamily(),
                                color = TextPrimary,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = selectedProduct.description,
                                fontSize = FontSize.REGULAR,
                                lineHeight = FontSize.REGULAR * 1.3,
                                color = TextPrimary,
                            )
                        }
                        Column(
                            modifier =
                                Modifier
                                    .background(
                                        if (selectedProduct.flavours?.isNotEmpty() == true) {
                                            SurfaceLighter
                                        } else {
                                            Surface
                                        },
                                    ).padding(all = 24.dp),
                        ) {
                            if (selectedProduct.flavours?.isNotEmpty() == true) {
                                FlowRow(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalArrangement = Arrangement.spacedBy(8.dp),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                ) {
                                    selectedProduct.flavours?.forEach { flavour ->
                                        FlavourChip(
                                            flavour = flavour,
                                            onClick = {},
                                            isSelected = true,
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(24.dp))
                            }
                            PrimaryButton(
                                icon = Resources.Icon.Cart,
                                text = "Add to Cart",
                                onClick = {},
                            )
                        }
                    }
                }
            },
            onError = {},
        )
    }
}
