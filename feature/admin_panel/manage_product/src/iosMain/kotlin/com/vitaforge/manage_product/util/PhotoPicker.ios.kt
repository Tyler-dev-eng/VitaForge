package com.vitaforge.manage_product.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.tylerdev.vitaforge.shared.domain.ImageFile
import platform.Foundation.NSURL
import platform.UIKit.UIApplication
import platform.UIKit.UIImagePickerController
import platform.UIKit.UIImagePickerControllerDelegateProtocol
import platform.UIKit.UIImagePickerControllerImageURL
import platform.UIKit.UIImagePickerControllerSourceType
import platform.UIKit.UIModalPresentationFullScreen
import platform.UIKit.UINavigationController
import platform.UIKit.UINavigationControllerDelegateProtocol
import platform.UIKit.UITabBarController
import platform.UIKit.UIViewController
import platform.darwin.NSObject

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class PhotoPicker {
    private var openPhotoPicker = mutableStateOf(false)

    // UIKit holds the delegate weakly, so keep it alive while the picker is shown
    private var pickerDelegate: PickerDelegate? = null

    @Composable
    actual fun InitialisePhotoPicker(onImageSelect: (ImageFile?) -> Unit) {
        val openPhotoPickerState by remember { openPhotoPicker }

        LaunchedEffect(openPhotoPickerState) {
            if (openPhotoPickerState) {
                val viewController = getCurrentViewController()
                val pickerDelegate =
                    PickerDelegate(
                        callback = { file ->
                            this@PhotoPicker.pickerDelegate = null
                            openPhotoPicker.value = false
                            onImageSelect(file)
                        },
                    ).also { this@PhotoPicker.pickerDelegate = it }
                val picker =
                    UIImagePickerController().apply {
                        // Full screen so the sheet can't be swiped away without calling the delegate
                        modalPresentationStyle = UIModalPresentationFullScreen
                        sourceType =
                            UIImagePickerControllerSourceType.UIImagePickerControllerSourceTypePhotoLibrary
                        mediaTypes = listOf("public.image", "public.heif")
                        delegate = pickerDelegate
                    }

                viewController?.presentViewController(
                    picker,
                    animated = true,
                    completion = null,
                )
            }
        }
    }

    actual fun open() {
        openPhotoPicker.value = true
    }

    private fun getCurrentViewController(): UIViewController? {
        val rootViewController = UIApplication.sharedApplication.keyWindow?.rootViewController
        return findTopViewController(rootViewController)
    }

    private fun findTopViewController(viewController: UIViewController?): UIViewController? =
        when (viewController) {
            is UINavigationController -> {
                findTopViewController(viewController.visibleViewController)
            }

            is UITabBarController -> {
                findTopViewController(viewController.selectedViewController)
            }

            is UIViewController -> {
                viewController.presentedViewController?.let { findTopViewController(it) }
                    ?: viewController
            }

            else -> {
                viewController
            }
        }

    // Delegate class implementing the necessary protocols
    private class PickerDelegate(
        private val callback: (ImageFile?) -> Unit,
    ) : NSObject(),
        UIImagePickerControllerDelegateProtocol,
        UINavigationControllerDelegateProtocol {
        // Override the method to handle the media picked by the user
        override fun imagePickerController(
            picker: UIImagePickerController,
            didFinishPickingMediaWithInfo: Map<Any?, *>,
        ) {
            // Extract the URL of the picked media
            val url = didFinishPickingMediaWithInfo[UIImagePickerControllerImageURL] as? NSURL
            callback(url?.absoluteString?.let { ImageFile(it) })
            picker.dismissViewControllerAnimated(true, completion = null)
        }

        // Override the method to handle the cancellation of the picker
        override fun imagePickerControllerDidCancel(picker: UIImagePickerController) {
            callback(null)
            picker.dismissViewControllerAnimated(true, completion = null)
        }
    }
}
