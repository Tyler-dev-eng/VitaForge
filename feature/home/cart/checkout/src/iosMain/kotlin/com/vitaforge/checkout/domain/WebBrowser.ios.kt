package com.vitaforge.checkout.domain

import platform.Foundation.NSURL
import platform.UIKit.UIApplication

internal actual fun openWebBrowser(url: String) {
    val nsUrl = NSURL.URLWithString(url) ?: return
    UIApplication.sharedApplication.openURL(nsUrl, options = emptyMap<Any?, Any>(), completionHandler = null)
}
