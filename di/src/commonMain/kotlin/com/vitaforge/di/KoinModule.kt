package com.vitaforge.di

import com.vitaforge.auth.AuthViewModel
import com.vitaforge.data.CustomerRepositoryImpl
import com.vitaforge.data.domain.CustomerRepository
import com.vitaforge.home.HomeGraphViewModel
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import org.koin.mp.KoinPlatform

val sharedModule =
    module {
        single<CustomerRepository> { CustomerRepositoryImpl() }
        viewModelOf(::AuthViewModel)
        viewModelOf(::HomeGraphViewModel)
    }

fun initialiseKoin(config: (KoinApplication.() -> Unit)? = null) {
    startKoin {
        config?.invoke(this)
        modules(sharedModule)
    }
}

fun isUserSignedIn(): Boolean =
    KoinPlatform
        .getKoin()
        .get<CustomerRepository>()
        .getCurrentUserId() != null
