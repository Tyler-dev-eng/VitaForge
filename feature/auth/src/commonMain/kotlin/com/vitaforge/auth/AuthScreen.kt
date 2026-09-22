package com.vitaforge.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.tylerdev.vitaforge.shared.Alpha
import com.tylerdev.vitaforge.shared.Black
import com.tylerdev.vitaforge.shared.Error
import com.tylerdev.vitaforge.shared.FontSize
import com.tylerdev.vitaforge.shared.Surface
import com.tylerdev.vitaforge.shared.TextPrimary
import com.tylerdev.vitaforge.shared.bebasNeueFontFamily
import com.vitaforge.auth.component.GoogleButton

@Suppress("ktlint:standard:function-naming")
@Composable
fun AuthScreen() {
    Scaffold { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().background(Surface).padding(24.dp),
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text =
                        buildAnnotatedString {
                            withStyle(SpanStyle(color = Error)) {
                                append("VITA")
                            }
                            withStyle(SpanStyle(color = Black)) {
                                append("FORGE")
                            }
                        },
                    textAlign = TextAlign.Center,
                    fontFamily = bebasNeueFontFamily(),
                    fontSize = FontSize.EXTRA_LARGE,
                )
                Text(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .alpha(Alpha.HALF),
                    text = "Sign in to continue",
                    textAlign = TextAlign.Center,
                    fontSize = FontSize.EXTRA_REGULAR,
                    color = TextPrimary,
                )
            }
            GoogleButton(
                isLoading = false,
                onClicked = {},
            )
        }
    }
}
