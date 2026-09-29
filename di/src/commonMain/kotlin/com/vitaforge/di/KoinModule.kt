package com.vitaforge.di

import org.koin.core.KoinApplication
import org.koin.core.context.startKoin

fun initialiseKoin(config: (KoinApplication.() -> Unit)? = null) {
    startKoin {
        config?.invoke(this)
    }
}
