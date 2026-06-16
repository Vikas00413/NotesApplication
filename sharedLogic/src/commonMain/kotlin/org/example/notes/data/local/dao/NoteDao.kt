package org.example.notes.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import org.example.notes.data.local.entity.NoteEntityDto

@Dao
interface NoteDao {

    @Query("SELECT * FROM notes ORDER BY lastUpdatedAt DESC")
    fun observeAllNotes(): Flow<List<NoteEntityDto>>

    @Query("SELECT * FROM notes ORDER BY lastUpdatedAt DESC")
    suspend fun getAllNotes(): List<NoteEntityDto>

     @Query("SELECT * FROM notes WHERE localId = :id LIMIT 1")
    suspend fun getNoteById(id: Int): NoteEntityDto?

    @Query("SELECT * FROM notes WHERE syncStatus != 'SYNCED' ORDER BY lastUpdatedAt DESC")
    suspend fun getUnSyncedNotes(): List<NoteEntityDto>

    @Query("SELECT * FROM notes WHERE isDone != 1 ORDER BY lastUpdatedAt DESC")
    suspend fun getInCompletedNotes(): List<NoteEntityDto>

    @Query("SELECT * FROM notes WHERE serverId = :id LIMIT 1")
    suspend fun getNoteByServerId(id: Int): NoteEntityDto?


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: NoteEntityDto): Long

    @Update
    suspend fun updateNote(note: NoteEntityDto)

    @Query("UPDATE notes SET isDone = 1 WHERE localId = :id")
    suspend fun markNoteAsDone(id: Int)

    @Query("UPDATE notes SET isDone = 0 WHERE localId = :id")
    suspend fun markNoteAsNotDone(id: Int)

    @Query("DELETE FROM notes WHERE localId = :id")
    suspend fun deleteNoteById(id: Int)

    @Delete
    suspend fun deleteNote(note: NoteEntityDto)

    @Query("DELETE FROM notes")
    suspend fun clearAll()
}