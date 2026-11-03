package com.vitaforge.checkout.domain

import com.tylerdev.vitaforge.shared.Constants.PAYPAL_AUTH_ENDPOINT
import com.tylerdev.vitaforge.shared.Constants.PAYPAL_AUTH_KEY
import com.tylerdev.vitaforge.shared.Constants.PAYPAL_CHECKOUT_ENDPOINT
import com.tylerdev.vitaforge.shared.Constants.RETURN_URL
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.Url
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlin.io.encoding.Base64
import kotlin.uuid.Uuid

class PaypalApi {
    private val client = createPayPalHttpClient()

    private val _accessToken = MutableStateFlow("")
    val accessToken: StateFlow<String> = _accessToken.asStateFlow()

    private val returnEvents = Channel<PaypalReturn>(Channel.BUFFERED)
    val paymentReturns: Flow<PaypalReturn> = returnEvents.receiveAsFlow()

    /** Called by the platform hosts when the app is opened via the PayPal return/cancel deep link. */
    fun handleReturnUrl(url: String) {
        val parsed = runCatching { Url(url) }.getOrNull() ?: return
        val expected = Url(RETURN_URL)
        if (parsed.protocol != expected.protocol || parsed.host != expected.host) return

        val orderId = parsed.parameters["token"]
        val event =
            if (parsed.parameters.contains("success") && orderId != null) {
                PaypalReturn.Approved(orderId)
            } else {
                PaypalReturn.Cancelled
            }
        returnEvents.trySend(event)
    }

    suspend fun captureOrder(
        orderId: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit,
    ) {
        try {
            val token = _accessToken.value.ifBlank { requestAccessToken() }
            val response =
                client.post(urlString = "$PAYPAL_CHECKOUT_ENDPOINT/$orderId/capture") {
                    headers {
                        append(HttpHeaders.Authorization, "Bearer $token")
                        append(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                    }
                }
            if (response.status == HttpStatusCode.OK || response.status == HttpStatusCode.Created) {
                onSuccess()
            } else {
                onError("Error while capturing the payment: ${response.status} - ${response.bodyAsText()}")
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            onError("Error while capturing the payment: ${e.message}")
        }
    }

    suspend fun fetchAccessToken(
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit,
    ) {
        try {
            onSuccess(requestAccessToken())
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            onError("Error while fetching an Access Token: ${e.message}")
        }
    }

    suspend fun beginCheckout(
        amount: Amount,
        fullName: String,
        shippingAddress: ShippingAddress,
        onSuccess: () -> Unit,
        onError: (String) -> Unit,
    ) {
        try {
            val token = _accessToken.value.ifBlank { requestAccessToken() }
            val uniqueId = Uuid.random().toHexString()
            val orderRequest =
                OrderRequest(
                    purchaseUnits =
                        listOf(
                            PurchaseUnit(
                                referenceId = uniqueId,
                                amount = amount,
                                shipping =
                                    Shipping(
                                        name = Name(fullName = fullName),
                                        address = shippingAddress,
                                    ),
                            ),
                        ),
                )

            val response =
                client.post(urlString = PAYPAL_CHECKOUT_ENDPOINT) {
                    headers {
                        append(HttpHeaders.Authorization, "Bearer $token")
                        append(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                        append("PayPal-Request-Id", uniqueId)
                    }
                    setBody(orderRequest)
                }

            if (response.status == HttpStatusCode.OK || response.status == HttpStatusCode.Created) {
                val orderResponse = response.body<OrderResponse>()
                val payerLink = orderResponse.links.firstOrNull { it.rel == "payer-action" }?.href

                withContext(Dispatchers.Main) {
                    handleUrl(
                        url = payerLink,
                        onSuccess = onSuccess,
                        onError = onError,
                    )
                }
            } else {
                onError("Error while starting the checkout: ${response.status} - ${response.bodyAsText()}")
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            onError("Error while starting the checkout: ${e.message}")
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
        if (url == null) {
            onError("Error while opening a web browser: URL is null")
            return
        }

        openWebBrowser(url = url)
        onSuccess()
    }
}

// Opening a URL needs a platform API (Android Intent / iOS UIApplication).
internal expect fun openWebBrowser(url: String)

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
