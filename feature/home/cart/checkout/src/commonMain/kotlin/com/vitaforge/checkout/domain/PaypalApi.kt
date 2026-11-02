package com.vitaforge.checkout.domain

import com.tylerdev.vitaforge.shared.Constants.PAYPAL_AUTH_ENDPOINT
import com.tylerdev.vitaforge.shared.Constants.PAYPAL_AUTH_KEY
import com.tylerdev.vitaforge.shared.Constants.PAYPAL_CHECKOUT_ENDPOINT
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.Parameters
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.encodeBase64
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.json.Json
import kotlin.io.encoding.Base64

class PaypalApi {
    private val client = createPayPalHttpClient()

    private val _accessToken = MutableStateFlow("")
    val accessToken: StateFlow<String> = _accessToken.asStateFlow()

    suspend fun fetchAccessToken(
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit,
    ) {
        try {
            onSuccess(requestAccessToken())
        } catch (e: Exception) {
            onError("Error while fetching an Access Token: ${e.message}")
        }
    }

    private suspend fun requestAccessToken(): String {
        val authKey = Base64.encode(PAYPAL_AUTH_KEY.encodeToByteArray())
        val response =
            client.post(urlString = PAYPAL_AUTH_ENDPOINT) {
                headers {
                    append(HttpHeaders.Authorization, "Basic $authKey")
                    append(
                        HttpHeaders.ContentType,
                        ContentType.Application.FormUrlEncoded.toString(),
                    )
                }
                setBody("grant_type=client_credentials")
            }

        if (response.status != HttpStatusCode.OK) {
            throw IllegalStateException("${response.status} - ${response.bodyAsText()}")
        }

        val tokenResponse = response.body<PaypalTokenResponse>()
        _accessToken.value = tokenResponse.accessToken
        return tokenResponse.accessToken
    }

    private fun handleUrl(
        url: String?,
        onSuccess: () -> Unit,
        onError: (String) -> Unit,
    ) {
    }
}

// Keep engine selection platform-specific so iOS uses Darwin for TLS requests.
internal expect fun createPayPalHttpClient(): HttpClient

internal fun HttpClientConfig<*>.configurePayPalHttpClient() {
    install(ContentNegotiation) {
        json(
            Json {
                ignoreUnknownKeys = true
                encodeDefaults = true
            },
        )
    }
}
