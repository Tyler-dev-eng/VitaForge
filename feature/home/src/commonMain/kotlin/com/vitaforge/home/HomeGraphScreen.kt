package com.vitaforge.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.tylerdev.vitaforge.shared.Surface
import com.tylerdev.vitaforge.shared.navigation.Screen
import com.vitaforge.components.BottomBar
import com.vitaforge.domain.BottomBarDestination

@Composable
fun HomeGraphScreen() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()

    val selectedDestination by remember {
        derivedStateOf {
            val destination = backStackEntry?.destination
            BottomBarDestination.entries.firstOrNull { destination?.hasRoute(it.screen::class) == true }
                ?: BottomBarDestination.ProductsOverview
        }
    }
    Scaffold(
        containerColor = Surface,
    ) { paddingValues ->
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(
                        top = paddingValues.calculateTopPadding(),
                        bottom = paddingValues.calculateBottomPadding(),
                    ),
        ) {
            NavHost(
                modifier = Modifier.weight(1f),
                navController = navController,
                startDestination = Screen.ProductsOverview,
            ) {
                composable<Screen.ProductsOverview> { }
                composable<Screen.Cart> { }
                composable<Screen.Categories> { }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Box(modifier = Modifier.padding(12.dp)) {
                BottomBar(
                    selected = selectedDestination,
                    onSelect = { destination ->
                        navController.navigate(destination.screen) {
                            launchSingleTop = true
                            popUpTo<Screen.ProductsOverview> {
                                saveState = true
                                inclusive = false
                            }
                            restoreState = true
                        }
                    },
                )
            }
        }
    }
}
