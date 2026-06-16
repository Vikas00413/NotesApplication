package org.example.notes

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform