package com.vitaforge.data

import com.tylerdev.vitaforge.shared.domain.ImageFile
import dev.gitlive.firebase.storage.File

internal expect fun ImageFile.toStorageFile(): File
