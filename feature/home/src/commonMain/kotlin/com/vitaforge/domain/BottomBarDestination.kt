package com.vitaforge.domain

import com.tylerdev.vitaforge.shared.Resources
import com.tylerdev.vitaforge.shared.navigation.Screen
import org.jetbrains.compose.resources.DrawableResource

enum class BottomBarDestination(
    val icon: DrawableResource,
    val title: String,
    val screen: Screen,
) {
    ProductsOverview(
        icon = Resources.Icon.Home,
        title = "Home",
        screen = Screen.ProductsOverview,
    ),
    Cart(
        icon = Resources.Icon.Cart,
        title = "Cart",
        screen = Screen.Cart,
    ),
    Categories(
        icon = Resources.Icon.Categories,
        title = "Categories",
        screen = Screen.Categories,
    ),
}
