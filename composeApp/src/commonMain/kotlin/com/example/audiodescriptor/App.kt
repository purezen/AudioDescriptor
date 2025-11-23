package com.example.audiodescriptor

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import cafe.adriel.voyager.navigator.Navigator
import com.example.audiodescriptor.screens.HomeScreen
import org.jetbrains.compose.ui.tooling.preview.Preview

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
            Navigator(HomeScreen(prefs))
        }
    }
}