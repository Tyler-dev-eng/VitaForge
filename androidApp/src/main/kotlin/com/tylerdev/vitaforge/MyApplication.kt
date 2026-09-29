package com.tylerdev.vitaforge

import android.app.Application
import com.google.firebase.Firebase
import com.google.firebase.initialize
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
    }
}
