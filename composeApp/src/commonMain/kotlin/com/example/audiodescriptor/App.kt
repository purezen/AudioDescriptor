package com.example.audiodescriptor

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import co.touchlab.kermit.Logger
import com.example.audiodescriptor.data.DummyText
import com.example.audiodescriptor.screens.NoiseTestScreen
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

// 'expect' declarations have to be mentioned here only
expect class AudioRecorder {
    fun start(outputFile: String? = null, onProgress: (Double) -> Unit)
    fun stop()
}

@Composable
expect fun rememberAudioRecorder(): AudioRecorder

@Composable
expect fun getCacheDirectoryPath(): String

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun App(
    prefs: DataStore<Preferences>,
) {
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
            Navigator(TextReadingScreen(prefs))
        }
    }
}

@Serializable
data class RecordingData(
    val taskType: String,
    val text: String,
    val audioPath: String,
    val duration_sec: Int,
    val timestamp: String,
)

class TextReadingScreen(private val prefs: DataStore<Preferences>): Screen {
    @OptIn(ExperimentalTime::class)
    @Composable
    override fun Content() {
        var readingText by remember { mutableStateOf("Loading Text.. ") }

        val audioRecorder = rememberAudioRecorder()
        var isRecording by remember { mutableStateOf(false) }
        val cacheDir = getCacheDirectoryPath()
        var currentRecordingPath by remember { mutableStateOf<String?>(null) }
        var recordingStartTime by remember { mutableStateOf(0L) }

        val dataStore = prefs
        val scope = rememberCoroutineScope()
        val recordingsListKey = stringPreferencesKey("recordings_list")
        var savedRecordings by remember { mutableStateOf<List<RecordingData>>(emptyList()) }

        LaunchedEffect(Unit) {
            try {
                readingText = getData()
            } catch (e: Exception) {
                Logger.e(e.toString())
            }

            val savedJson = dataStore.data.map {
                it[recordingsListKey]
            }.first()
            if (savedJson != null) {
                Logger.d("Saved JSON: $")
                savedRecordings = Json.decodeFromString<List<RecordingData>>(savedJson)
            }
        }

        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text("Text Reading Screen")
            Text(
                text = readingText,
                style = MaterialTheme.typography.bodyLarge
            )

            Button(onClick = {
                if (isRecording) {
                    audioRecorder.stop()
//                    val duration = (System.currentTimeMillis() - )

                    scope.launch {
                        currentRecordingPath?.let { path ->
                            val newRecording = RecordingData(
                                taskType = "text_reading",
                                text = readingText,
                                audioPath = path,
                                duration_sec = 10,
                                timestamp = "date"
                            )

                            val updatedList = savedRecordings + newRecording
                            dataStore.edit { settings ->
                                settings[recordingsListKey] = Json.encodeToString(updatedList)
                            }
                            savedRecordings = updatedList
                        }
                    }
                } else {
                    val path = "$cacheDir/record_${Clock.System.now().toEpochMilliseconds()}.mp4"
                    currentRecordingPath = path
                    recordingStartTime = Clock.System.now().toEpochMilliseconds()
                    audioRecorder.start(path) {}
                }
                isRecording = !isRecording
            }) {
                Text(if (isRecording) "STOP RECORDING" else "START READING")
            }
        }
    }

    suspend fun getData(): String {
        val client = HttpClient(CIO) {
            install(ContentNegotiation) {
                json()
            }
        }

        val response: DummyText = client.get("https://dummyjson.com/products").body()
        client.close()
        val products = response.products?.get(0)?.description
        return products ?: "No Products"
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
            Text(
                "Let's start with a sample task for practice",
                style = MaterialTheme.typography.headlineMedium,
            )

            Text(
                "Pehele hum ek sample task karte hain",
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
//            Button(onClick = {
//                navigator.push(TextReadingScreen())
//            }) {
//                Text("Text Reading")
//            }

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
