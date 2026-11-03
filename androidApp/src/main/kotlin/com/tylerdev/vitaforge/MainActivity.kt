package com.tylerdev.vitaforge

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.mmk.kmpauth.core.KMPAuth
import com.mmk.kmpauth.google.google
import com.mmk.kmpnotifier.permission.permissionUtil
import com.tylerdev.vitaforge.shared.Constants.GOOGLE_WEB_CLIENT_ID
import com.tylerdev.vitaforge.shared.navigation.Screen
import com.vitaforge.di.handlePaypalReturn
import com.vitaforge.di.isUserSignedIn
import com.vitaforge.navigation.SetupNavGraph

class MainActivity : ComponentActivity() {
    private val permissionUtil by permissionUtil()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle =
                SystemBarStyle.light(
                    Color.TRANSPARENT,
                    Color.TRANSPARENT,
                ),
            navigationBarStyle =
                SystemBarStyle.light(
                    Color.TRANSPARENT,
                    Color.TRANSPARENT,
                ),
        )

        permissionUtil.askNotificationPermission()

        KMPAuth.initialize {
            google(serverId = GOOGLE_WEB_CLIENT_ID)
        }

        if (savedInstanceState == null) handleDeepLink(intent)

        val startDestination = if (isUserSignedIn()) Screen.HomeGraph else Screen.Auth

        setContent {
            App {
                SetupNavGraph(startDestination = startDestination)
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleDeepLink(intent)
    }

    private fun handleDeepLink(intent: Intent?) {
        intent?.data?.toString()?.let(::handlePaypalReturn)
    }
}
