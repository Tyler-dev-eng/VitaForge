@file:Suppress("ktlint:standard:no-wildcard-imports")

package com.tylerdev.vitaforge

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Suppress("ktlint:standard:function-naming")
@Composable
fun App(content: @Composable () -> Unit) {
    MaterialTheme {
        content()
    }
}
