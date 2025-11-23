package com.example.audiodescriptor.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow

class HomeScreen(private val prefs: DataStore<Preferences>) : Screen {
    @Composable
    override fun Content() {
        Column(
            modifier = Modifier.Companion.padding(20.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.Companion.CenterHorizontally
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
                navigator.push(NoiseTestScreen(prefs))
            }) {
                Text("START SAMPLE TASK")
            }

            Button(onClick = {
                navigator.push(RecordingsList(prefs))
            }) {
                Text("VIEW RECORDINGS")
            }
        }
    }
}