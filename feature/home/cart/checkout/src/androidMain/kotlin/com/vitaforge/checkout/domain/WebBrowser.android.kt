package com.vitaforge.checkout.domain

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import org.koin.mp.KoinPlatform

internal actual fun openWebBrowser(url: String) {
    val context = KoinPlatform.getKoin().get<Context>()
    val intent =
        Intent(Intent.ACTION_VIEW, url.toUri())
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    context.startActivity(intent)
}
