package com.example.audiodescriptor.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.audiodescriptor.TextReadingScreen

class TaskSelectionScreen(private val prefs: DataStore<Preferences>): Screen {
    @Composable
    override fun Content() {
        Column {

            Text("Select task type")


            val navigator = LocalNavigator.currentOrThrow
            Button(onClick = {
                navigator.push(TextReadingScreen(prefs))
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