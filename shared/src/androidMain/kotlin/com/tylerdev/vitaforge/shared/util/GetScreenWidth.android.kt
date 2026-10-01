package com.tylerdev.vitaforge.shared.util

actual fun getScreenWidth(): Float =
    android.content.res.Resources
        .getSystem()
        .displayMetrics.widthPixels /
        android.content.res.Resources
            .getSystem()
            .displayMetrics.density
