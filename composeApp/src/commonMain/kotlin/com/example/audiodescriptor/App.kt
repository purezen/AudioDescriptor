package com.example.audiodescriptor

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    Navigator(HomeScreen())
}

class HomeScreen() : Screen {
    @Composable
    override fun Content() {
        Text("AudioDescriptor")
        val navigator = LocalNavigator.currentOrThrow
        Button(onClick = {
          navigator.push(SecondScreen())
        }) {
          Text("Go to second screen")
        }
    }
}

class SecondScreen(): Screen {
    @Composable
    override fun Content() {
        Text("Hello 2")
    }
}