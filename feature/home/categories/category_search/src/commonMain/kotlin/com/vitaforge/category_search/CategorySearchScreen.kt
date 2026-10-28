package com.vitaforge.category_search

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tylerdev.vitaforge.shared.BorderIdle
import com.tylerdev.vitaforge.shared.FontSize
import com.tylerdev.vitaforge.shared.IconPrimary
import com.tylerdev.vitaforge.shared.Resources
import com.tylerdev.vitaforge.shared.Surface
import com.tylerdev.vitaforge.shared.SurfaceLighter
import com.tylerdev.vitaforge.shared.TextMuted
import com.tylerdev.vitaforge.shared.TextPrimary
import com.tylerdev.vitaforge.shared.components.ProductCard
import com.tylerdev.vitaforge.shared.domain.ProductCategory
import com.tylerdev.vitaforge.shared.robotoCondensedFontFamily
import com.tylerdev.vitaforge.shared.util.DisplayResult
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import vitaforge.shared.generated.resources.Res
import vitaforge.shared.generated.resources.admin_panel_back
import vitaforge.shared.generated.resources.admin_panel_search

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategorySearchScreen(
    navigateBack: () -> Unit,
    navigateToDetails: (String) -> Unit,
    category: ProductCategory,
) {
    val viewModel = koinViewModel<CategorySearchViewModel>()
    val products by viewModel.products.collectAsStateWithLifecycle()
    var searchBarVisible by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()

    Scaffold(
        containerColor = Surface,
        topBar = {
            AnimatedContent(
                targetState = searchBarVisible,
            ) { visible ->
                if (visible) {
                    SearchBar(
                        modifier = Modifier.padding(horizontal = 12.dp).fillMaxWidth(),
                        expanded = false,
                        onExpandedChange = {},
                        colors =
                            SearchBarDefaults.colors(
                                containerColor = SurfaceLighter,
                                dividerColor = BorderIdle,
                            ),
                        inputField = {
                            SearchBarDefaults.InputField(
                                modifier = Modifier.fillMaxWidth(),
                                query = searchQuery,
                                onQueryChange = viewModel::updateSearchQuery,
                                expanded = false,
                                onExpandedChange = {},
                                onSearch = { focusManager.clearFocus() },
                                placeholder = {
                                    Text(
                                        text = stringResource(Res.string.admin_panel_search),
                                        fontFamily = robotoCondensedFontFamily(),
                                        fontSize = FontSize.REGULAR,
                                        color = TextMuted,
                                    )
                                },
                                trailingIcon = {
                                    IconButton(onClick = {
                                        if (searchQuery.isNotEmpty()) {
                                            viewModel.updateSearchQuery("")
                                        } else {
                                            searchBarVisible = false
                                        }
                                    }) {
                                        Icon(
                                            modifier = Modifier.size(14.dp),
                                            painter = painterResource(Resources.Icon.Close),
                                            contentDescription = "",
                                            tint = IconPrimary,
                                        )
                                    }
                                },
                            )
                        },
                    ) {}
                } else {
                    CategoriesTopBar(
                        navigateBack = navigateBack,
                        onSearchClick = { searchBarVisible = true },
                        categoryTitle = category.title,
                    )
                }
            }
        },
    ) { padding ->
        products.DisplayResult(
            modifier =
                Modifier
                    .padding(
                        top = padding.calculateTopPadding(),
                        bottom = padding.calculateBottomPadding(),
                    ),
            onLoading = {},
            onError = {},
            onSuccess = { categoryProducts ->
                AnimatedContent(
                    targetState = categoryProducts,
                ) { products ->
                    if (products.isNotEmpty()) {
                        LazyColumn(
                            modifier =
                                Modifier
                                    .fillMaxSize()
                                    .padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                        ) {
                            items(
                                items = products,
                                key = { it.id },
                            ) { product ->
                                ProductCard(
                                    product = product,
                                    onClick = navigateToDetails,
                                )
                            }
                        }
                    } else {
                        // TODO:: Empty state
                    }
                }
            },
        )
    }
}

@Composable
private fun CategoriesTopBar(
    navigateBack: () -> Unit,
    onSearchClick: () -> Unit,
    categoryTitle: String,
) {
    TopAppBar(
        title = {
            Text(
                text = categoryTitle,
                fontFamily = robotoCondensedFontFamily(),
                fontSize = FontSize.LARGE,
                color = IconPrimary,
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
            IconButton(onClick = { onSearchClick() }) {
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
