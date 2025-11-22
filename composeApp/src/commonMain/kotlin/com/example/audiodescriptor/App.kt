package com.example.audiodescriptor

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
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import co.touchlab.kermit.Logger
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.math.roundToInt

expect class AudioRecorder {
    fun start(outputFile: String? = null, onProgress: (Double) -> Unit)
    fun stop()
}

@Composable
expect fun rememberAudioRecorder(): AudioRecorder

@Composable
@Preview
fun App() {
    MaterialTheme {
        Navigator(HomeScreen())
    }
}

class HomeScreen() : Screen {
    @Composable
    override fun Content() {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("AudioDescriptor", style = MaterialTheme.typography.headlineLarge)

            Text("Let's start with a sample task for practice")

            Text("Pehele hum ek sample task karte hain")

            val navigator = LocalNavigator.currentOrThrow
            Button(onClick = {
                navigator.push(NoiseTestScreen())
            }) {
                Text("Start Sample Task")
            }
        }
        }
}

class NoiseTestScreen(): Screen {
    @Composable
    override fun Content() {
        val audioRecorder = rememberAudioRecorder()

        var dBs by remember { mutableStateOf(0.0)  }
        val animatedDBs by animateFloatAsState(targetValue = dBs.toFloat())

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Decibel meter", style=MaterialTheme.typography.headlineMedium)

            DecibelBar(
                decibels = animatedDBs,
                modifier = Modifier
                    .width(100.dp)
                    .height(300.dp)
            )

            Text("Current Decibels: ${animatedDBs.roundToInt()}")

            Button(onClick = {
                audioRecorder.start { db ->
                    Logger.d("deciBel reading: ${db}")
                    dBs = db
                }
            }) {
                Text("START TEST")
            }

            Button(onClick = {
                audioRecorder.stop()
            }) {
                Text("STOP")
            }

            val navigator = LocalNavigator.currentOrThrow
            Button(onClick = {
                navigator.push(TaskSelectionScreen())
            }) {
                Text("PROCEED")
            }
        }
    }
}

class TaskSelectionScreen: Screen {
    @Composable
    override fun Content() {
        Column {

            Text("Select task type")


            val navigator = LocalNavigator.currentOrThrow
            Button(onClick = {
                navigator.push(TextReadingScreen())
            }) {
                Text("Text Reading")
            }

            Button(onClick = {
                navigator.push(ImageDescriptionScreen())
            }) {
                Text("Image Description")
            }

            Button(onClick = {
                navigator.push(PhotoCaptureScreen())
            }) {
                Text("Photo Capture")
            }
        }
    }
}

class TextReadingScreen: Screen {
    @Composable
    override fun Content() {
        Text("Text Reading Screen")
    }
}

class ImageDescriptionScreen: Screen {
    @Composable
    override fun Content() {
        Text("Image Description Screen")
    }
}

class PhotoCaptureScreen: Screen {
    @Composable
    override fun Content() {
        Text("Photo Capture Screen")
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