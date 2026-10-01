package com.vitaforge.home

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.tylerdev.vitaforge.shared.Alpha
import com.tylerdev.vitaforge.shared.FontSize
import com.tylerdev.vitaforge.shared.IconPrimary
import com.tylerdev.vitaforge.shared.Resources
import com.tylerdev.vitaforge.shared.Surface
import com.tylerdev.vitaforge.shared.SurfaceLighter
import com.tylerdev.vitaforge.shared.TextPrimary
import com.tylerdev.vitaforge.shared.bebasNeueFontFamily
import com.tylerdev.vitaforge.shared.navigation.Screen
import com.tylerdev.vitaforge.shared.util.getScreenWidth
import com.vitaforge.components.BottomBar
import com.vitaforge.components.CustomDrawer
import com.vitaforge.domain.BottomBarDestination
import com.vitaforge.domain.CustomDrawerState
import com.vitaforge.domain.isOpened
import com.vitaforge.domain.opposite
import org.jetbrains.compose.resources.painterResource

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

    val screenWidth = remember { getScreenWidth() }
    var drawerState by remember { mutableStateOf(CustomDrawerState.Closed) }

    val offsetValue by remember { derivedStateOf { (screenWidth / 1.5).dp } }
    val animatedOffset by animateDpAsState(
        targetValue =
            if (drawerState.isOpened()) offsetValue else 0.dp,
    )
    val animatedScale by animateFloatAsState(
        targetValue = if (drawerState.isOpened()) 0.9f else 1f,
    )
    val animatedRadius by animateDpAsState(
        targetValue = if (drawerState.isOpened()) 20.dp else 0.dp,
    )
    val animatedBackground by animateColorAsState(
        targetValue =
            if (drawerState.isOpened()) SurfaceLighter else Surface,
    )

    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .background(animatedBackground),
    ) {
        CustomDrawer(
            userName = "Alex Mercer",
            userTier = "Athlete Tier • Active",
            onProfileClick = {},
            onContactUsClick = {},
            onSignOutClick = {},
            onAdminClick = {},
        )
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(animatedRadius))
                    .offset(x = animatedOffset)
                    .scale(scale = animatedScale)
                    .shadow(
                        elevation = 20.dp,
                        shape = RoundedCornerShape(animatedRadius),
                        ambientColor = Color.Black.copy(alpha = Alpha.DISABLED),
                        spotColor = Color.Black.copy(alpha = Alpha.DISABLED),
                    ),
        ) {
            Scaffold(
                containerColor = Surface,
                topBar = {
                    CenterAlignedTopAppBar(
                        title = {
                            AnimatedContent(
                                targetState = selectedDestination,
                            ) { destination ->
                                Text(
                                    text =
                                        if (destination == BottomBarDestination.ProductsOverview) {
                                            "VitaForge"
                                        } else {
                                            destination.title
                                        },
                                    fontFamily = bebasNeueFontFamily(),
                                    fontSize = FontSize.LARGE,
                                    color = TextPrimary,
                                )
                            }
                        },
                        navigationIcon = {
                            AnimatedContent(
                                targetState = drawerState,
                            ) { drawer ->
                                if (drawer.isOpened()) {
                                    IconButton(onClick = {
                                        drawerState = drawerState.opposite()
                                    }) {
                                        Icon(
                                            painter = painterResource(Resources.Icon.Close),
                                            contentDescription = "Close Icon",
                                            tint = IconPrimary,
                                        )
                                    }
                                } else {
                                    IconButton(onClick = {
                                        drawerState = drawerState.opposite()
                                    }) {
                                        Icon(
                                            painter = painterResource(Resources.Icon.Menu),
                                            contentDescription = "Menu Icon",
                                            tint = IconPrimary,
                                        )
                                    }
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
            if (drawerState.isOpened()) {
                Box(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .pointerInput(Unit) {
                                detectTapGestures { drawerState = CustomDrawerState.Closed }
                            },
                )
            }
        }
    }
}
