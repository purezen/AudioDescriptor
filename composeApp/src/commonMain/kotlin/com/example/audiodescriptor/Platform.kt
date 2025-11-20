package com.example.audiodescriptor

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform