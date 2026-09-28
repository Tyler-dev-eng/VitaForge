package com.tylerdev.vitaforge

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.mmk.kmpauth.core.KMPAuth
import com.mmk.kmpauth.google.google
import com.tylerdev.vitaforge.shared.Constants.GOOGLE_WEB_CLIENT_ID
import com.vitaforge.navigation.SetupNavGraph

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        KMPAuth.initialize {
            google(serverId = GOOGLE_WEB_CLIENT_ID)
        }

        setContent {
            App {
                SetupNavGraph()
            }
        }
    }
}
