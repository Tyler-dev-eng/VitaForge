package com.vitaforge.navigation

import androidx.compose.ui.window.ComposeUIViewController
import com.mmk.kmpauth.core.KMPAuth
import com.mmk.kmpauth.google.google
import com.tylerdev.vitaforge.App
import com.tylerdev.vitaforge.shared.Constants.GOOGLE_WEB_CLIENT_ID
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.initialize
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController {
    Firebase.initialize()
    KMPAuth.initialize {
        google(serverId = GOOGLE_WEB_CLIENT_ID)
    }
    return ComposeUIViewController {
        App {
            SetupNavGraph()
        }
    }
}
