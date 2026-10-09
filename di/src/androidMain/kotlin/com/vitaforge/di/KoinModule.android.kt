package com.vitaforge.di

import com.vitaforge.manage_product.util.PhotoPicker
import org.koin.dsl.module

actual val targetModule =
    module {
        single<PhotoPicker> { PhotoPicker() }
    }
