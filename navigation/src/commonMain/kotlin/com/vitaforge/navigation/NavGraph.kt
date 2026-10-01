package com.vitaforge.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tylerdev.vitaforge.shared.navigation.Screen
import com.vitaforge.auth.AuthScreen
import com.vitaforge.home.HomeGraphScreen

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
            )
        }
    }
}
