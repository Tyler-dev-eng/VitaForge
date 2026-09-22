@file:Suppress("ktlint:standard:no-wildcard-imports")

package com.tylerdev.vitaforge

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.tylerdev.vitaforge.shared.bebasNeueFontFamily
import org.jetbrains.compose.resources.painterResource
import vitaforge.shared.generated.resources.Res
import vitaforge.shared.generated.resources.compose_multiplatform

@Suppress("ktlint:standard:function-naming")
@Composable
fun App(content: @Composable () -> Unit) {
    MaterialTheme {
        content()
    }
}
