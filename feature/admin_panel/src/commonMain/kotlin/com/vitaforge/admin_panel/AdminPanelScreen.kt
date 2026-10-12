package com.vitaforge.admin_panel

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tylerdev.vitaforge.shared.ButtonPrimary
import com.tylerdev.vitaforge.shared.FontSize
import com.tylerdev.vitaforge.shared.IconPrimary
import com.tylerdev.vitaforge.shared.Resources
import com.tylerdev.vitaforge.shared.Surface
import com.tylerdev.vitaforge.shared.TextPrimary
import com.tylerdev.vitaforge.shared.bebasNeueFontFamily
import com.tylerdev.vitaforge.shared.components.LoadingCard
import com.tylerdev.vitaforge.shared.components.ProductCard
import com.tylerdev.vitaforge.shared.util.DisplayResult
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import vitaforge.shared.generated.resources.Res
import vitaforge.shared.generated.resources.admin_panel_add_product
import vitaforge.shared.generated.resources.admin_panel_back
import vitaforge.shared.generated.resources.admin_panel_search
import vitaforge.shared.generated.resources.admin_panel_title

@Composable
fun AdminPanelScreen(
    navigateBack: () -> Unit,
    navigateToManageProduct: (String?) -> Unit,
) {
    val viewModel = koinViewModel<AdminPanelViewModel>()
    val products = viewModel.products.collectAsState()
    var searchBarVisible by mutableStateOf(false)
    Scaffold(
        containerColor = Surface,
        topBar = {
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
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navigateToManageProduct(null) },
                containerColor = ButtonPrimary,
                contentColor = IconPrimary,
                content = {
                    Icon(
                        painter = painterResource(Resources.Icon.Plus),
                        contentDescription = stringResource(Res.string.admin_panel_add_product),
                    )
                },
            )
        },
    ) { paddingValues ->
        products.value.DisplayResult(
            modifier = Modifier.padding(top = paddingValues.calculateTopPadding(), bottom = paddingValues.calculateBottomPadding()),
            onLoading = { LoadingCard(modifier = Modifier.fillMaxSize()) },
            onSuccess = { latestProducts ->
                LazyColumn(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    items(
                        items = latestProducts,
                        key = { it.id },
                    ) { product ->
                        ProductCard(
                            product = product,
                            onClick = { navigateToManageProduct(product.id) },
                        )
                    }
                }
            },
            onError = {},
        )
    }
}
