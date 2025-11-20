package com.example.audiodescriptor

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    HomeScreen()
}

@Composable
fun HomeScreen() {
    Text("AudioDescriptor")
}