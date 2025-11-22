package com.example.audiodescriptor

import android.Manifest
import android.content.Context
import android.media.MediaRecorder
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

actual class AudioRecorder(private val context: Context) {
    private var mediaRecorder: MediaRecorder? = null

    actual fun start(outputFile: String?) {
        val recorder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            MediaRecorder(context)
        } else {
            @Suppress("DEPRECIATION")
            MediaRecorder()
        }

        recorder.apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setOutputFile(outputFile)

            try {
                prepare()
                start()
            } catch (e: Exception) {
                // Handle exceptions, e.g., file path is invalid
                e.printStackTrace()
            }
        }
        mediaRecorder = recorder
    }

    actual fun stop() {
        mediaRecorder?.apply {
            try {
                stop()
                release()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        mediaRecorder = null
    }
}

// 2. Composable to remember the recorder and handle permissions
@OptIn(ExperimentalPermissionsApi::class)
@Composable
actual fun rememberAudioRecorder(): AudioRecorder {
    val context = LocalContext.current
    val permissionState = rememberPermissionState(Manifest.permission.RECORD_AUDIO)

    // A side-effect to request permission when the composable enters the screen
    LaunchedEffect(Unit) {
        if (!permissionState.status.isGranted) {
            permissionState.launchPermissionRequest()
        }
    }

    // Return the actual recorder instance
    return remember { AudioRecorder(context) }
}