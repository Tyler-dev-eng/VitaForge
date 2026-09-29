package com.vitaforge.auth

import ContentWithMessageBar
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.mmk.kmpauth.google.rememberGoogleAuthState
import com.tylerdev.vitaforge.shared.Alpha
import com.tylerdev.vitaforge.shared.Black
import com.tylerdev.vitaforge.shared.Error
import com.tylerdev.vitaforge.shared.FontSize
import com.tylerdev.vitaforge.shared.Surface
import com.tylerdev.vitaforge.shared.TextPrimary
import com.tylerdev.vitaforge.shared.White
import com.tylerdev.vitaforge.shared.bebasNeueFontFamily
import com.vitaforge.auth.component.GoogleButton
import rememberMessageBarState

@Suppress("ktlint:standard:function-naming")
@Composable
fun AuthScreen() {
    var loadingState by remember { mutableStateOf(false) }
    val messageBarState = rememberMessageBarState()

    ContentWithMessageBar(
        messageBarState = messageBarState,
        errorMaxLines = 2,
        successContainerColor = Color(0xFF2E7D32),
        successContentColor = White,
        errorContainerColor = Error,
        errorContentColor = White,
        contentBackgroundColor = Surface,
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(Surface)
                    .navigationBarsPadding()
                    .padding(24.dp),
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
            val googleAuthState =
                rememberGoogleAuthState(
                    linkAccount = false,
                    onResult = { result ->
                        result
                            .onSuccess { user ->
                                loadingState = false
                                messageBarState.addSuccess(
                                    "Signed in as ${user.email ?: user.displayName ?: "user"}",
                                )
                            }.onFailure { error ->
                                loadingState = false
                                val message =
                                    when {
                                        error.message?.contains("A network error") == true ->
                                            "Network error. Please check your connection and try again."
                                        error.message?.contains("Id token is null") == true ->
                                            "Sign-in failed. Please try again."
                                        else ->
                                            error.message?.replace(Regex("\\s*\\(com\\.google\\.GIDSignIn -?\\d+\\)"), "")?.trim()
                                                ?: "Sign-in failed. Please try again."
                                    }
                                messageBarState.addError(message)
                            }
                    },
                )
            GoogleButton(
                isLoading = loadingState,
                onClicked = {
                    loadingState = true
                    googleAuthState.launch()
                },
            )
        }
    }
}
