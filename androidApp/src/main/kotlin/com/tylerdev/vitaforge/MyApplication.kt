package com.tylerdev.vitaforge

import android.app.Application
import com.google.firebase.Firebase
import com.google.firebase.initialize
import com.mmk.kmpnotifier.KMPNotifier
import com.mmk.kmpnotifier.notification.configuration.NotificationPlatformConfiguration
import com.mmk.kmpnotifier.push.firebase.FirebasePush
import com.vitaforge.di.initialiseKoin
import org.koin.android.ext.koin.androidContext

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initialiseKoin(
            config = {
                androidContext(this@MyApplication)
            },
        )
        Firebase.initialize(context = this)
        KMPNotifier.initialize(
            configuration =
                NotificationPlatformConfiguration.Android(
                    notificationIconResId = R.drawable.ic_launcher_foreground,
                ),
            FirebasePush,
        )
    }
}
