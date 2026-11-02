package com.tylerdev.vitaforge.shared

object Constants {
    const val GOOGLE_WEB_CLIENT_ID = "903209547720-ljl169dkfvb0pe5nknfl14lhjhrl8lt7.apps.googleusercontent.com"

    // Credentials are generated from local.properties into PaypalCredentials at build time.
    const val PAYPAL_AUTH_KEY = "${PaypalCredentials.CLIENT_ID}:${PaypalCredentials.SECRET_ID}"
    const val PAYPAL_AUTH_ENDPOINT = "https://api-m.sandbox.paypal.com/v1/oauth2/token"
    const val PAYPAL_CHECKOUT_ENDPOINT = "https://api-m.sandbox.paypal.com/v2/checkout/orders"
    const val RETURN_URL = "com.tylerdev.vitaforge://paypalpay?success=true"
    const val CANCEL_URL = "com.tylerdev.vitaforge://paypalpay?cancel=true"
}
