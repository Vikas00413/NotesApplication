package org.example.notes.data.mapper

import org.example.notes.data.local.entity.NoteEntityDto
import org.example.notes.data.local.entity.Priority
import org.example.notes.data.local.entity.SyncStatus
import org.example.notes.domain.model.Notes


fun NoteEntityDto.toNoteEntity(): Notes {
    return Notes(
        id = this.serverId ?: this.localId ,
        syncStatus = SyncStatus.valueOf(this.syncStatus),
        title = this.title,
        priority = Priority.valueOf(this.priority),
        createdAt = this.createdAt,
        lastUpdatedAt = this.lastUpdatedAt,
        isDone = this.isDone,
    )
}