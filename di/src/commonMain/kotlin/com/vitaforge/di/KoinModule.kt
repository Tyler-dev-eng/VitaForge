package com.vitaforge.di

import com.vitaforge.admin_panel.AdminPanelViewModel
import com.vitaforge.auth.AuthViewModel
import com.vitaforge.cart.CartViewModel
import com.vitaforge.category_search.CategorySearchViewModel
import com.vitaforge.checkout.CheckoutViewModel
import com.vitaforge.checkout.domain.PaypalApi
import com.vitaforge.data.AdminRepositoryImpl
import com.vitaforge.data.CustomerRepositoryImpl
import com.vitaforge.data.OrderRepositoryImpl
import com.vitaforge.data.ProductRepositoryImpl
import com.vitaforge.data.createFlavourRepository
import com.vitaforge.data.domain.AdminRepository
import com.vitaforge.data.domain.CustomerRepository
import com.vitaforge.data.domain.FlavourRepository
import com.vitaforge.data.domain.OrderRepository
import com.vitaforge.data.domain.ProductRepository
import com.vitaforge.details.DetailsViewModel
import com.vitaforge.home.HomeGraphViewModel
import com.vitaforge.manage_product.ManageProductViewModel
import com.vitaforge.manage_product.usecase.AddFlavourUseCase
import com.vitaforge.manage_product.usecase.ObserveFlavoursUseCase
import com.vitaforge.products_overview.ProductOverviewViewModel
import com.vitaforge.profile.ProfileViewModel
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import org.koin.mp.KoinPlatform

val sharedModule =
    module {
        single<CustomerRepository> { CustomerRepositoryImpl() }
        single<AdminRepository> { AdminRepositoryImpl() }
        single<ProductRepository> { ProductRepositoryImpl() }
        single<FlavourRepository> { createFlavourRepository() }
        single<OrderRepository> { OrderRepositoryImpl(get()) }
        single<PaypalApi> { PaypalApi() }
        factoryOf(::ObserveFlavoursUseCase)
        factoryOf(::AddFlavourUseCase)
        viewModelOf(::AuthViewModel)
        viewModelOf(::HomeGraphViewModel)
        viewModelOf(::ProfileViewModel)
        viewModelOf(::ManageProductViewModel)
        viewModelOf(::AdminPanelViewModel)
        viewModelOf(::ProductOverviewViewModel)
        viewModelOf(::DetailsViewModel)
        viewModelOf(::CartViewModel)
        viewModelOf(::CategorySearchViewModel)
        viewModelOf(::CheckoutViewModel)
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
