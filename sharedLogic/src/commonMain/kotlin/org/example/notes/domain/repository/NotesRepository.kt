package org.example.notes.domain.repository

import kotlinx.coroutines.flow.Flow
import org.example.notes.domain.model.Notes

interface NotesRepository {
    fun observeNotes(): Flow<List<Notes>>

    suspend fun getNoteById(id: Int): Notes?

    suspend fun markNoteAsDone(id: Int)
    suspend fun markNoteAsNotDone(id: Int)

    suspend fun deleteNoteById(id: Int)
    suspend fun getUnSyncedNotes(): List<Notes>
    suspend fun getIncompleteNotes(): List<Notes>

    suspend fun saveNote(
        notes: Notes
    )

    suspend fun updateNote(
        notes: Notes
    )


    suspend fun syncPendingNotes(): Result<Int>
}