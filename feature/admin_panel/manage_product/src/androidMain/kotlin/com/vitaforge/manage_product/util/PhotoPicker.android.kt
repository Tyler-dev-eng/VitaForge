package com.vitaforge.manage_product.util

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import com.tylerdev.vitaforge.shared.domain.ImageFile

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class PhotoPicker {
    private var openPhotoPicker = mutableStateOf(false)

    @Composable
    actual fun InitialisePhotoPicker(onImageSelect: (ImageFile?) -> Unit) {
        val pickMedia =
            rememberLauncherForActivityResult(
                contract = ActivityResultContracts.PickVisualMedia(),
            ) { uri ->
                uri?.let { onImageSelect(ImageFile(it.toString())) } ?: onImageSelect(null)
                openPhotoPicker.value = false
            }

        LaunchedEffect(openPhotoPicker.value) {
            if (openPhotoPicker.value) {
                pickMedia.launch(
                    PickVisualMediaRequest(
                        ActivityResultContracts.PickVisualMedia.ImageOnly,
                    ),
                )
            }
        }
    }

    actual fun open() {
        openPhotoPicker.value = true
    }
}
