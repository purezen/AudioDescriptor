package com.example.audiodescriptor

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.audiodescriptor.screens.NoiseTestScreen
import org.jetbrains.compose.ui.tooling.preview.Preview

// 'expect' declarations have to be mentioned here only
expect class AudioRecorder {
    fun start(outputFile: String? = null, onProgress: (Double) -> Unit)
    fun stop()
}

@Composable
expect fun rememberAudioRecorder(): AudioRecorder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun App() {
    Scaffold(topBar = {
        TopAppBar(
            colors = topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = Color.White,
            ),
            title = {
                Text("AudioDescriptor")
            }
        )
    })  { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            Navigator(HomeScreen())
        }
    }
}

class HomeScreen() : Screen {
    @Composable
    override fun Content() {
        Column(
            modifier = Modifier.padding(20.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Let's start with a sample task for practice",
                style = MaterialTheme.typography.headlineMedium,
            )

            Text("Pehele hum ek sample task karte hain",
                style = MaterialTheme.typography.headlineSmall,
            )

            val navigator = LocalNavigator.currentOrThrow
            Button(onClick = {
                navigator.push(NoiseTestScreen())
            }) {
                Text("START SAMPLE TASK")
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
