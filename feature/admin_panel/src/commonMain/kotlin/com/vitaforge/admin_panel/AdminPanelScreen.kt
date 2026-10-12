package com.vitaforge.admin_panel

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tylerdev.vitaforge.shared.ButtonPrimary
import com.tylerdev.vitaforge.shared.FontSize
import com.tylerdev.vitaforge.shared.IconPrimary
import com.tylerdev.vitaforge.shared.Resources
import com.tylerdev.vitaforge.shared.Surface
import com.tylerdev.vitaforge.shared.SurfaceDarker
import com.tylerdev.vitaforge.shared.SurfaceLighter
import com.tylerdev.vitaforge.shared.TextMuted
import com.tylerdev.vitaforge.shared.TextPrimary
import com.tylerdev.vitaforge.shared.bebasNeueFontFamily
import com.tylerdev.vitaforge.shared.components.LoadingCard
import com.tylerdev.vitaforge.shared.components.ProductCard
import com.tylerdev.vitaforge.shared.domain.Product
import com.tylerdev.vitaforge.shared.robotoCondensedFontFamily
import com.tylerdev.vitaforge.shared.util.DisplayResult
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import vitaforge.shared.generated.resources.Res
import vitaforge.shared.generated.resources.admin_panel_add_product
import vitaforge.shared.generated.resources.admin_panel_back
import vitaforge.shared.generated.resources.admin_panel_empty_message
import vitaforge.shared.generated.resources.admin_panel_empty_title
import vitaforge.shared.generated.resources.admin_panel_product_count
import vitaforge.shared.generated.resources.admin_panel_search
import vitaforge.shared.generated.resources.admin_panel_title

private val FabShape = RoundedCornerShape(16.dp)

@Composable
fun AdminPanelScreen(
    navigateBack: () -> Unit,
    navigateToManageProduct: (String?) -> Unit,
) {
    val viewModel = koinViewModel<AdminPanelViewModel>()
    val products by viewModel.products.collectAsStateWithLifecycle()
    val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()
    Scaffold(
        containerColor = SurfaceLighter,
        topBar = { AdminPanelTopBar(navigateBack = navigateBack) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navigateToManageProduct(null) },
                shape = FabShape,
                containerColor = ButtonPrimary,
                contentColor = IconPrimary,
            ) {
                Icon(
                    painter = painterResource(Resources.Icon.Plus),
                    contentDescription = stringResource(Res.string.admin_panel_add_product),
                )
            }
        },
    ) { paddingValues ->
        Column(
            modifier =
                Modifier.padding(
                    top = paddingValues.calculateTopPadding(),
                    bottom = paddingValues.calculateBottomPadding(),
                ),
        ) {
            CategoryFilterRow(
                modifier = Modifier.padding(vertical = 8.dp),
                selectedCategory = selectedCategory,
                onCategorySelected = viewModel::selectCategory,
            )
            products.DisplayResult(
                onLoading = { LoadingCard(modifier = Modifier.fillMaxSize()) },
                onSuccess = { latestProducts ->
                    ProductList(
                        products = latestProducts,
                        onProductClick = navigateToManageProduct,
                    )
                },
                onError = {},
            )
        }
    }
}

@Composable
private fun AdminPanelTopBar(navigateBack: () -> Unit) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(Res.string.admin_panel_title),
                fontFamily = bebasNeueFontFamily(),
                fontSize = FontSize.LARGE,
                color = TextPrimary,
            )
        },
        navigationIcon = {
            IconButton(onClick = navigateBack) {
                Icon(
                    painter = painterResource(Resources.Icon.Back),
                    contentDescription = stringResource(Res.string.admin_panel_back),
                    tint = IconPrimary,
                )
            }
        },
        actions = {
            IconButton(onClick = {}) {
                Icon(
                    painter = painterResource(Resources.Icon.Search),
                    contentDescription = stringResource(Res.string.admin_panel_search),
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
}

@Composable
private fun ProductList(
    products: List<Product>,
    onProductClick: (String?) -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .background(SurfaceDarker)
                    .padding(horizontal = 12.dp, vertical = 8.dp),
            text = stringResource(Res.string.admin_panel_product_count, products.size).uppercase(),
            fontFamily = robotoCondensedFontFamily(),
            fontSize = FontSize.REGULAR,
            color = TextPrimary,
        )
        if (products.isEmpty()) {
            EmptyProducts()
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(
                    items = products,
                    key = { it.id },
                    contentType = { "product" },
                ) { product ->
                    ProductCard(
                        product = product,
                        onClick = { onProductClick(product.id) },
                    )
                }
            }
        }
    }
}

@Composable
private fun EmptyProducts() {
    Box(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        contentAlignment = Alignment.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = stringResource(Res.string.admin_panel_empty_title).uppercase(),
                fontFamily = bebasNeueFontFamily(),
                fontSize = FontSize.EXTRA_MEDIUM,
                color = TextPrimary,
            )
            Text(
                text = stringResource(Res.string.admin_panel_empty_message),
                fontSize = FontSize.REGULAR,
                color = TextMuted,
            )
        }
    }
}
