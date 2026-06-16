package org.example.notes.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class NoteEntityDto(
    @PrimaryKey(autoGenerate = true)
    val localId:Int = 0,
    val syncStatus: String,
    val serverId: Int? = null,
    val title: String,
    val priority: String,
    val createdAt: String?,
    val lastUpdatedAt: Long,
    val isDone: Boolean = false,
)
