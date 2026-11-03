package com.vitaforge.data

import android.net.Uri
import com.tylerdev.vitaforge.shared.domain.ImageFile
import dev.gitlive.firebase.storage.File

internal actual fun ImageFile.toStorageFile(): File = File(Uri.parse(uri))
