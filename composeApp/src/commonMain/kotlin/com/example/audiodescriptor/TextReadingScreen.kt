package com.example.audiodescriptor

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import cafe.adriel.voyager.core.screen.Screen
import co.touchlab.kermit.Logger
import com.example.audiodescriptor.data.DummyText
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
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
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

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
                Logger.Companion.e(e.toString())
            }

            val savedJson = dataStore.data.map {
                it[recordingsListKey]
            }.first()
            if (savedJson != null) {
                Logger.Companion.d("Saved JSON: $")
                savedRecordings = Json.Default.decodeFromString<List<RecordingData>>(savedJson)
            }
        }

        Column(
            modifier = Modifier.Companion.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.Companion.CenterHorizontally,
        ) {
            Text("Text Reading Screen")
            Text(
                text = readingText,
                style = MaterialTheme.typography.bodyLarge
            )

            Button(onClick = {
                if (isRecording) {
                    audioRecorder.stop()
                    val duration = (Clock.System.now().toEpochMilliseconds() - recordingStartTime)

                    scope.launch {
                        currentRecordingPath?.let { path ->
                            val newRecording = RecordingData(
                                taskType = "text_reading",
                                text = readingText,
                                audioPath = path,
                                duration_sec = 10,
                                timestamp = Clock.System.now().toString()
                            )

                            val updatedList = savedRecordings + newRecording
                            dataStore.edit { settings ->
                                settings[recordingsListKey] =
                                    Json.encodeToString(updatedList)
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