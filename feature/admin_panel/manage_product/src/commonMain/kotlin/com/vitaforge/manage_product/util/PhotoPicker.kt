package com.vitaforge.manage_product.util

import androidx.compose.runtime.Composable
import com.tylerdev.vitaforge.shared.domain.ImageFile

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect class PhotoPicker {
    @Composable
    fun InitialisePhotoPicker(onImageSelect: (ImageFile?) -> Unit)

    fun open()
}
