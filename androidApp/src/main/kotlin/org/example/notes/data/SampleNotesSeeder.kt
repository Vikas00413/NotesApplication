package org.example.notes.data

import org.example.notes.data.local.entity.Priority
import org.example.notes.data.local.entity.SyncStatus
import org.example.notes.domain.model.Notes
import org.example.notes.domain.repository.NotesRepository
import org.example.notes.platform.currentTimeMillis

class SampleNotesSeeder(
    private val repository: NotesRepository,
) {
    suspend fun seedIfEmpty() {

        val now = currentTimeMillis()
        sampleNotes(now).forEach { note ->
            repository.saveNote(note)
        }
    }

    private fun sampleNotes(baseTime: Long): List<Notes> = listOf(
        Notes(
            id = 0,
            title = "Buy groceries",
            priority = Priority.HIGH,
            syncStatus = SyncStatus.SYNCED,
            createdAt = baseTime.toString(),
            lastUpdatedAt = baseTime,
            isDone = false,
        ),
        Notes(
            id = 0,
            title = "Finish KMP project",
            priority = Priority.MEDIUM,
            syncStatus = SyncStatus.PENDING_UPDATE,
            createdAt = (baseTime - 1_000).toString(),
            lastUpdatedAt = baseTime - 1_000,
            isDone = true,
        ),
        Notes(
            id = 0,
            title = "Read Compose documentation",
            priority = Priority.LOW,
            syncStatus = SyncStatus.SYNCED,
            createdAt = (baseTime - 2_000).toString(),
            lastUpdatedAt = baseTime - 2_000,
            isDone = false,
        ),
        Notes(
            id = 0,
            title = "Team meeting at 4 PM",
            priority = Priority.HIGH,
            syncStatus = SyncStatus.PENDING_CREATE,
            createdAt = (baseTime - 3_000).toString(),
            lastUpdatedAt = baseTime - 3_000,
            isDone = false,
        ),
        Notes(
            id = 0,
            title = "Workout session",
            priority = Priority.MEDIUM,
            syncStatus = SyncStatus.SYNCED,
            createdAt = (baseTime - 4_000).toString(),
            lastUpdatedAt = baseTime - 4_000,
            isDone = true,
        ),
    )
}
