package com.example.audiodescriptor

import android.Manifest
import android.content.Context
import android.media.MediaRecorder
import android.os.Build
import android.os.Looper
import android.os.Handler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import co.touchlab.kermit.Logger
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import java.io.File
import kotlin.math.log10

actual class AudioRecorder(private val context: Context) {
    private var mediaRecorder: MediaRecorder? = null

    private var handler = Handler(Looper.getMainLooper())
    private var progressRunnable: Runnable? = null

    actual fun start(outputFile: String?, onProgress: (Double) -> Unit) {
        stop()

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

            val finalOutputFile = outputFile ?: File(context.cacheDir, "temp_record.mp4").absolutePath
            setOutputFile(finalOutputFile)

            try {
                prepare()
                start()
                Logger.d("Recording started")
            } catch (e: Exception) {
                Logger.d("Recording failed", e)
                recorder.release()
                mediaRecorder = null
                return
//                e.printStackTrace()
            }
        }
        mediaRecorder = recorder

        progressRunnable = Runnable {
            mediaRecorder?.let {
                val amplitude = it.maxAmplitude
                // calculating decibel
                val db = if (amplitude > 0) {
                    20 * log10(amplitude.toDouble())
                } else {
                    0.0
                }

                onProgress(db)
                handler.postDelayed(progressRunnable!!, 250)
            }
        }

        handler.post(progressRunnable!!)
    }

    actual fun stop() {
        progressRunnable?.let { handler.removeCallbacks(it) }
        progressRunnable = null

        mediaRecorder?.apply {
            try {
                Logger.d("Stopping recording")
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