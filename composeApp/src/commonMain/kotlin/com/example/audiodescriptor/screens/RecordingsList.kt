package com.example.audiodescriptor.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import cafe.adriel.voyager.core.screen.Screen
import co.touchlab.kermit.Logger
import com.example.audiodescriptor.RecordingData
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json

class RecordingsList(private val prefs: DataStore<Preferences>): Screen {
    @Composable
    override fun Content() {
        var savedRecordings by remember { mutableStateOf<List<RecordingData>>(emptyList()) }
        val recordingsListKey = stringPreferencesKey("recordings_list")
        val dataStore = prefs

        LaunchedEffect(Unit) {
            val savedJson = dataStore.data.map { preferences ->
                preferences[recordingsListKey]
            }.first()

            if (savedJson != null) {
                try {
                    savedRecordings = Json.Default.decodeFromString<List<RecordingData>>(savedJson)
                    Logger.Companion.d("Loaded ${savedRecordings.size} recordings from DataStore.")
                } catch (e: Exception) {
                    Logger.Companion.e("Failed to decode recordings from JSON", e)
                }
            } else {
                Logger.Companion.d("No recordings found in DataStore.")
            }
        }

        Column(
            modifier = Modifier.Companion.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.Companion.CenterHorizontally
        ) {
            Text(
                "All Recordings",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.Companion.padding(bottom = 16.dp)
            )

            if (savedRecordings.isEmpty()) {
                Text("No recordings found.")
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp) // Add space between cards
                ) {
                    items(savedRecordings) { recording ->
                        RecordingListItem(recording) // Use a dedicated composable for the item
                    }
                }
            }
        }
    }
}

@Composable
fun RecordingListItem(recording: RecordingData) {
    Card(
        modifier = Modifier.Companion.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.Companion.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp) // Space between text lines
        ) {
            Text(
                text = "Task: ${recording.taskType}",
                style = MaterialTheme.typography.titleMedium,
                fontSize = 18.sp // Slightly larger font for the title
            )
            Spacer(modifier = Modifier.Companion.height(4.dp))
            Text(
                text = "File: ${recording.audioPath.substringAfterLast('/')}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant // Softer color
            )
            Text(
                text = "Date: ${recording.timestamp}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "Duration: ${recording.duration_sec} seconds",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}