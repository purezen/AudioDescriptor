package com.example.audiodescriptor.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import co.touchlab.kermit.Logger
import com.example.audiodescriptor.TaskSelectionScreen
import com.example.audiodescriptor.rememberAudioRecorder
import kotlin.math.roundToInt

class NoiseTestScreen(): Screen {
    @Composable
    override fun Content() {
        val audioRecorder = rememberAudioRecorder()

        var dBs by remember { mutableStateOf(0.0) }
        val animatedDBs by animateFloatAsState(targetValue = dBs.toFloat())

        var isRecording by remember { mutableStateOf(false) }

        Column(
            modifier = Modifier.Companion.fillMaxSize(),
            horizontalAlignment = Alignment.Companion.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Decibel meter", style = MaterialTheme.typography.headlineMedium)

            DecibelBar(
                decibels = animatedDBs,
                modifier = Modifier.Companion
                    .width(100.dp)
                    .height(300.dp)
            )

            Text("Current Decibels: ${animatedDBs.roundToInt()}")

            Button(onClick = {
                if (!isRecording) {
                    audioRecorder.start { db ->
                        Logger.Companion.d("deciBel reading: ${db}")
                        dBs = db
                    }
                } else {
                    audioRecorder.stop()
                    dBs = 0.0
                }

                isRecording = !isRecording
            }) {
                Text(if (isRecording) "STOP TEST" else "START TEST")
            }

            val navigator = LocalNavigator.currentOrThrow
            Button(onClick = {
                if (isRecording) {
                    dBs = 0.0
                    audioRecorder.stop()
                    isRecording = false
                }

                navigator.push(TaskSelectionScreen())
            }) {
                Text("PROCEED")
            }
        }
    }
}


@Composable
fun DecibelBar(decibels: Float, modifier: Modifier = Modifier) {
    val maxDecibels = 100f
    val fraction = ( decibels / maxDecibels ).coerceIn(0f, 1f)

    Box(
        modifier.background(Color.LightGray.copy(alpha = 0.5f), shape = MaterialTheme.shapes.medium)
            .clip(MaterialTheme.shapes.medium),
        contentAlignment = Alignment.BottomCenter
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(fraction)
                .background(MaterialTheme.colorScheme.primary)
        )
    }
}