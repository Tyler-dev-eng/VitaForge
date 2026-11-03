package com.vitaforge.data

import com.tylerdev.vitaforge.shared.domain.ImageFile
import dev.gitlive.firebase.storage.File
import platform.Foundation.NSURL

internal actual fun ImageFile.toStorageFile(): File {
    val url = requireNotNull(NSURL.URLWithString(uri)) { "Invalid image URI: $uri" }
    return File(url)
}
