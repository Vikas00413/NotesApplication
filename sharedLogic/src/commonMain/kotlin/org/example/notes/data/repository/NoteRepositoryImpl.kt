package org.example.notes.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.example.notes.data.local.dao.NoteDao
import org.example.notes.data.local.entity.NoteEntityDto
import org.example.notes.data.mapper.toNoteEntity
import org.example.notes.domain.model.Notes
import org.example.notes.domain.repository.NotesRepository

class NoteRepositoryImpl(val noteDao: NoteDao) : NotesRepository {
    override fun observeNotes(): Flow<List<Notes>> {
        return noteDao.observeAllNotes().map { note -> note.map { it.toNoteEntity() } }

    }

    override suspend fun getNoteById(id: Int): Notes? {
        return noteDao.getNoteById(id)?.toNoteEntity()
    }


    override suspend fun markNoteAsDone(id: Int) {
        noteDao.markNoteAsDone(id)
    }

    override suspend fun markNoteAsNotDone(id: Int) {
        noteDao.markNoteAsNotDone(id)
    }

    override suspend fun deleteNoteById(id: Int) {
        noteDao.deleteNoteById(id)
    }

    override suspend fun getUnSyncedNotes(): List<Notes> {
        return noteDao.getUnSyncedNotes().map { it.toNoteEntity() }
    }

    override suspend fun getIncompleteNotes(): List<Notes> {
        return noteDao.getInCompletedNotes().map { it.toNoteEntity() }
    }

    override suspend fun saveNote(notes: Notes) {
        noteDao.insertNote(
            NoteEntityDto(
                localId = 0,
                title = notes.title,
                priority = notes.priority.name,
                createdAt = notes.createdAt,
                lastUpdatedAt = notes.lastUpdatedAt,
                isDone = notes.isDone,
                syncStatus = notes.syncStatus.name
            )
        )
    }

    override suspend fun updateNote(notes: Notes) {
        noteDao.updateNote(
            NoteEntityDto(
                localId = notes.id,
                title = notes.title,
                priority = notes.priority.name,
                createdAt = notes.createdAt,
                lastUpdatedAt = notes.lastUpdatedAt,
                isDone = notes.isDone,
                syncStatus = notes.syncStatus.name
            )
        )
    }


    override suspend fun syncPendingNotes(): Result<Int> {
        TODO("Not yet implemented")
    }


}