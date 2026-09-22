package com.tylerdev.vitaforge

import androidx.compose.material3.Text
import androidx.compose.ui.window.ComposeUIViewController

fun MainViewController() =
    ComposeUIViewController {
        App {
            // TODO: wire up SetupNavGraph() from :navigation once the iOS framework links it.
            Text("VitaForge")
        }
    }