package org.example.notes.data.local.entity

import kotlinx.serialization.Serializable

@Serializable
enum class Priority(var color: Int) {
    LOW(color = 0xFF4CAF50.toInt()), // Green
    MEDIUM(color = 0xFFFF9800.toInt()), // Orange
    HIGH(color = 0xFFCE93D8.toInt()) // Light purple
}