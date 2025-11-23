package com.example.audiodescriptor

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
actual fun getCacheDirectoryPath(): String {
    return LocalContext.current.cacheDir.absolutePath
}