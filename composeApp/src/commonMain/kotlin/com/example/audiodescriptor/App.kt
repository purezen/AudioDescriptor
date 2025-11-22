package com.example.audiodescriptor

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import co.touchlab.kermit.Logger
import org.jetbrains.compose.ui.tooling.preview.Preview

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

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text("Decibel meter")

            Text("Current Decibels: ${dBs}")

            Button(onClick = {
                audioRecorder.start { db ->
                    Logger.d("deciBel reading: ${db}")
                    dBs = db
                }
            }) {
                Text("Start Test")
            }

            Button(onClick = {
                audioRecorder.stop()
            }) {
                Text("STOP")
            }
        }
    }
}