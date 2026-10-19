package com.vitaforge.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.tylerdev.vitaforge.shared.navigation.Screen
import com.vitaforge.admin_panel.AdminPanelScreen
import com.vitaforge.auth.AuthScreen
import com.vitaforge.details.DetailsScreen
import com.vitaforge.home.HomeGraphScreen
import com.vitaforge.manage_product.ManageProductScreen
import com.vitaforge.profile.ProfileScreen

@Suppress("ktlint:standard:function-naming")
@Composable
fun SetupNavGraph(startDestination: Screen) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {
        composable<Screen.Auth> {
            AuthScreen(navigateToHome = {
                navController.navigate(Screen.HomeGraph) {
                    popUpTo(Screen.Auth) { inclusive = true }
                }
            })
        }

        composable<Screen.HomeGraph> {
            HomeGraphScreen(
                navigateToAuth = {
                    navController.navigate(Screen.Auth) {
                        popUpTo(Screen.HomeGraph) { inclusive = true }
                    }
                },
                navigateToProfile = { navController.navigate(Screen.Profile) },
                navigateToAdminPanel = { navController.navigate(Screen.AdminPanel) },
                navigateToDetails = { id -> navController.navigate(Screen.Details(id)) },
            )
        }

        composable<Screen.Profile> {
            ProfileScreen(onNavigateBack = { navController.navigateUp() })
        }

        composable<Screen.AdminPanel> {
            AdminPanelScreen(
                navigateBack = { navController.navigateUp() },
                navigateToManageProduct = { id ->
                    navController.navigate(Screen.ManageProduct(id))
                },
            )
        }

        composable<Screen.ManageProduct> {
            val id = it.toRoute<Screen.ManageProduct>().id
            ManageProductScreen(
                id = id,
                navigateBack = { navController.navigateUp() },
            )
        }

        composable<Screen.Details> {
            DetailsScreen(navigateBack = { navController.navigateUp() })
        }
    }
}
