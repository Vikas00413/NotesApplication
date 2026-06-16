package org.example.notes.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import org.example.notes.data.local.dao.NoteDao
import org.example.notes.data.local.entity.NoteEntityDto

@Database(
    entities = [NoteEntityDto::class],
    version = 1,
    exportSchema = true,
)
abstract class NoteDataBase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
}