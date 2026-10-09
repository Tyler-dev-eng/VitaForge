package com.vitaforge.di

import com.vitaforge.auth.AuthViewModel
import com.vitaforge.data.AdminRepositoryImpl
import com.vitaforge.data.CustomerRepositoryImpl
import com.vitaforge.data.domain.AdminRepository
import com.vitaforge.data.domain.CustomerRepository
import com.vitaforge.home.HomeGraphViewModel
import com.vitaforge.manage_product.ManageProductViewModel
import com.vitaforge.profile.ProfileViewModel
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import org.koin.mp.KoinPlatform

val sharedModule =
    module {
        single<CustomerRepository> { CustomerRepositoryImpl() }
        single<AdminRepository> { AdminRepositoryImpl() }
        viewModelOf(::AuthViewModel)
        viewModelOf(::HomeGraphViewModel)
        viewModelOf(::ProfileViewModel)
        viewModelOf(::ManageProductViewModel)
    }

expect val targetModule: Module

fun initialiseKoin(config: (KoinApplication.() -> Unit)? = null) {
    startKoin {
        config?.invoke(this)
        modules(sharedModule, targetModule)
    }
}

fun isUserSignedIn(): Boolean =
    KoinPlatform
        .getKoin()
        .get<CustomerRepository>()
        .getCurrentUserId() != null
