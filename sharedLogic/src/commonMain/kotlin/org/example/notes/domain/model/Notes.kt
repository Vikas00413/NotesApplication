package org.example.notes.domain.model

import kotlinx.serialization.Serializable
import org.example.notes.data.local.entity.Priority
import org.example.notes.data.local.entity.SyncStatus

@Serializable
data class Notes(
    val id: Int,
    val title: String,
    val priority: Priority = Priority.LOW,
    val syncStatus: SyncStatus = SyncStatus.SYNCED,
    val createdAt: String? = null,
    val lastUpdatedAt: Long = 0L,
    val isDone: Boolean = false,
)