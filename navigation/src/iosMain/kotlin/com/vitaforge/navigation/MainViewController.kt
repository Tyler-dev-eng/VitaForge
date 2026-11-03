package com.vitaforge.navigation

import androidx.compose.ui.window.ComposeUIViewController
import com.mmk.kmpauth.core.KMPAuth
import com.mmk.kmpauth.google.google
import com.mmk.kmpnotifier.KMPNotifier
import com.mmk.kmpnotifier.notification.configuration.NotificationPlatformConfiguration
import com.mmk.kmpnotifier.push.firebase.FirebasePush
import com.tylerdev.vitaforge.App
import com.tylerdev.vitaforge.shared.Constants.GOOGLE_WEB_CLIENT_ID
import com.tylerdev.vitaforge.shared.navigation.Screen
import com.vitaforge.di.initialiseKoin
import com.vitaforge.di.isUserSignedIn
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.initialize
import platform.UIKit.UIViewController

@Suppress("ktlint:standard:function-naming")
fun MainViewController(): UIViewController {
    Firebase.initialize()
    initialiseKoin()
    KMPNotifier.initialize(
        configuration = NotificationPlatformConfiguration.Ios(),
        FirebasePush,
    )
    KMPAuth.initialize {
        google(serverId = GOOGLE_WEB_CLIENT_ID)
    }
    val startDestination = if (isUserSignedIn()) Screen.HomeGraph else Screen.Auth
    return ComposeUIViewController {
        App {
            SetupNavGraph(startDestination = startDestination)
        }
    }
}

fun handlePaypalReturn(url: String) = com.vitaforge.di.handlePaypalReturn(url)
